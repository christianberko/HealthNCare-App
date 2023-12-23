package model;

import exceptions.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import view.Observer;

/** This class is responsible for managing the data of the application. */
public class HealthNCare implements Observable {
  /** The default calorie limit used if no calorie limit is specified */
  private final float DEFAULT_CALORIE_LIMIT = 2000;

  /** The default weight in pounds used if not weight entry is found */
  private final float DEFAULT_WEIGHT = 150;

  /** The recommended daily sodium limit in milligrams from the FDA */
  private final float DAILY_SODIUM_LIMIT_MG = 2300;

  private StorageManager storageManager;

  private ArrayList<LogEntry> log;
  private HashMap<String, IFood> recipes;
  private HashMap<String, Exercise> exercises;

  protected ArrayList<Observer> observers = new ArrayList<Observer>();

  public HealthNCare() {
    super();
    storageManager = new StorageManager();
    log = storageManager.getLog();
    recipes = storageManager.getRecipes();
    exercises = storageManager.getExercises();
  }

  @Override
  public void addSubscriber(Observer observer) {
      observers.add(observer);
  }

  @Override
  public void removeSubscriber(Observer observer) {
      observers.remove(observer);
  }

  @Override
  public void notifySubscribers(String messageType, String message) {
    // observers.forEach(observer -> observer.update(messageType, message));
    for (Observer o : new ArrayList<Observer>(observers)) {
      o.update(messageType, message);
    }
  }

  /**
   * @param logEntry An object that holds the string of data to be entered into the log.csv file.
   */
  public void addLogEntry(LogEntry logEntry)
      throws PastEntryConstraintException,
      ExerciseNotExistsException,
      FoodNotExistsException {
    try {
      Class<? extends LogEntry> entryClass = logEntry.getClass();
      if (isValidToEnter(logEntry)) {
        if (entryClass == ExerciseEntry.class) {
          ExerciseEntry e = (ExerciseEntry) logEntry;

          // check if exercise exists
          if (!exerciseExists(e.getExerciseName().toLowerCase())) {
            throw new ExerciseNotExistsException();
          }
        }

        if (entryClass == ConsumptionEntry.class) {
          ConsumptionEntry ce = (ConsumptionEntry) logEntry;
          if (!foodExists(ce.getFoodConsumed())) {
            throw new FoodNotExistsException();
          }
        }

        // passes all checks
        log.add(logEntry);
        notifySubscribers("updateLog", null);

      }
    } catch (PastEntryConstraintException pece) {
      throw pece;
    }
  }

  /**
   * Determines whether the desired entry is allowed to be entered. The rules are: A {@link
   * CalorieLimitEntry} or {@link WeightEntry} can't be created for a previous date or if a {@link
   * ConsumptionEntry} has been logged
   *
   * @param entry The entry to test
   */
  private boolean isValidToEnter(LogEntry entry) throws PastEntryConstraintException {
    if (entry instanceof CalorieLimitEntry || entry instanceof WeightEntry) {
      if (!entriesOnOrAfterDate(getLogEntriesOfType(ConsumptionEntry.class), entry.getDate())
          .isEmpty() && entry.getDate().isEqual(LocalDate.now())) {
        throw new PastEntryConstraintException();
      } else if (entry.getDate().isBefore(LocalDate.now())) {
        throw new PastEntryConstraintException();
      }
    }
    return true;
  }

  /**
   * @param food An object that holds the information to create a food in the food.csv file.
   */
  public void addFood(IFood food) throws FoodAlreadyExistsException {
    if (recipes.get(food.getName().toLowerCase()) != null) {
      throw new FoodAlreadyExistsException();
    } else {
      recipes.put(food.getName().toLowerCase(), food);
      notifySubscribers("updateFood", null);
    }
  }

  /** Persist the current log and foods data to the permanent storage medium */
  public void saveData() {
    storageManager.saveLog(log);
    storageManager.saveRecipes(recipes);
    notifySubscribers("updateLog", null);
    notifySubscribers("updateFood", null);
    notifySubscribers("updateExercise", null);
  }

  /**
   * Return an arraylist of foods consumed on a given day
   *
   * @param date The day to retrieve from
   * @return The foods consumed
   */
  public ArrayList<String> getFoodConsumed(LocalDate date) {
    ArrayList<String> foodsConsumed = new ArrayList<String>();
    for (LogEntry entry : log) {
      if (entry.getClass() == ConsumptionEntry.class && entry.getDate().equals(date)) {
        ConsumptionEntry ce = (ConsumptionEntry) entry;
        foodsConsumed.add(ce.getFoodConsumed() + " : " + String.valueOf(ce.getAmountConsumed()));
      }
    }
    return foodsConsumed;
  }

  /**
   * Returns a list of all the foods available
   *
   * @return ArrayList of food names listed in food.csv.
   */
  public ArrayList<String> getFoods() {
    ArrayList<String> foodList = new ArrayList<String>();
    for (IFood food : recipes.values()) {
      foodList.add(food.getName());
    }
    return foodList;
  }

  /**
   * Get the most recent weight entry up to the date provided
   *
   * @param date the max date to check for
   * @return the weight in pounds as a float
   */
  public float getWeight(LocalDate date) {
    LocalDate maxDate = LocalDate.MIN;
    float weight = DEFAULT_WEIGHT;
    for (LogEntry entry : log) {
      if (entry.getClass() == WeightEntry.class) {
        if ((entry.getDate().isBefore(date) || entry.getDate().equals(date))
            && maxDate.isBefore(entry.getDate())) {
          WeightEntry we = (WeightEntry) entry;
          weight = we.getWeight();
          maxDate = entry.getDate();
        }
      }
    }
    return weight;
  }

  /**
   * @param date Used to get the proper calorie amounts for the day.
   */
  public float calculatePercentCalorieIntake(LocalDate date) {
    return getCaloriesConsumed(date) / getCalorieLimit(date);
  }

  /**
   * @param date Used to get the proper calorie amounts for the day.
   */
  public boolean isCalorieLimitExceeded(LocalDate date) {
    float percentage = getCaloriesConsumed(date) / getCalorieLimit(date);
    boolean overLimit;
    if (percentage > 1) {
      overLimit = true;
    } else {
      overLimit = false;
    }
    return overLimit;
  }

  /**
   * Get the most recent calorie limit entry up to the date provided
   *
   * @param date The cutoff date to include entries
   * @return A float object representing number or calories
   */
  public float getCalorieLimit(LocalDate date) {
    LocalDate maxDate = LocalDate.MIN;
    CalorieLimitEntry mostRecentCalorieEntry =
        new CalorieLimitEntry(maxDate, DEFAULT_CALORIE_LIMIT);
    boolean foundEntry = false;
    for (LogEntry entry : log) {
      if (entry.getClass() == CalorieLimitEntry.class) {
        if ((entry.getDate().isBefore(date) || entry.getDate().equals(date))
            && maxDate.isBefore(entry.getDate())) {
          maxDate = entry.getDate();
          mostRecentCalorieEntry = (CalorieLimitEntry) entry;
          foundEntry = true;
        }
      }
    }
    return foundEntry ? mostRecentCalorieEntry.getCalorieLimit() : DEFAULT_CALORIE_LIMIT;
  }

  /**
   * Get the number of calories consumed for a given day. Looks through the look and adds together
   * the calories for each food item consumed.
   *
   * @param date The date to get information from
   * @return The number of calories as a float
   */
  public float getCaloriesConsumed(LocalDate date) {
    float caloriesConsumed = 0;
    for (LogEntry entry : log) {
      if (entry.getClass() == ConsumptionEntry.class && entry.getDate().equals(date)) {
        ConsumptionEntry ce = (ConsumptionEntry) entry;
        caloriesConsumed +=
            recipes.get(ce.getFoodConsumed().toLowerCase()).getCalories() * ce.getAmountConsumed();
      }
    }
    return caloriesConsumed;
  }

  /**
   * Determines wheter or not a food already exists
   *
   * @param foodName
   * @return True if it exists, False if it doesn't
   */
  public boolean foodExists(String foodName) {
    if (recipes.containsKey(foodName.toLowerCase())) {
      return true;
    }
    return false;
  }

  /**
   * Determines whether a exercise exists or not
   * 
   * @param exerciseName
   * @return True if exists, False if it doesn't
   */
  public boolean exerciseExists(String exerciseName) {
    if (exercises.get(exerciseName.toLowerCase()) != null) {
      return true;
    }
    return false;
  }

  /**
   * @param food Object to remove from food.csv
   */
  public void removeFood(IFood food) throws HasDependencyException, ItemUsedInLogException {
    // Check if the food is used in a recipe
    for (IFood f : recipes.values()) {
      if (f.dependsOn(food)) {
        // Item can't be removed since
        // it has recipes that depend on it
        throw new HasDependencyException(f.getName());
      }
    }

    // check if the food has been logged at some point
    for (ConsumptionEntry entry : getLogEntriesOfType(ConsumptionEntry.class)) {
      if (entry.getFoodConsumed().equalsIgnoreCase(food.getName())) {
        throw new ItemUsedInLogException();
      }
    }

    recipes.remove(food.getName().toLowerCase());
    notifySubscribers("updateFood", null);
  }

  public void updateBasicFood(String foodName, String[] inputFields) {
    FoodItem food = (FoodItem) getFood(foodName);
    food.setCalories(Float.parseFloat(inputFields[0]));
    food.setFat(Float.parseFloat(inputFields[1]));
    food.setCarbohydrates(Float.parseFloat(inputFields[2]));
    food.setProtein(Float.parseFloat(inputFields[3]));
    food.setSodium(Float.parseFloat(inputFields[4]));
    notifySubscribers("updateFood", null);
  }

  public void updateRecipe(String recipeName, ArrayList<RecipeItem> recipeItems) {
    Recipe recipe = (Recipe) getFood(recipeName);
    recipe.setRecipeItems(recipeItems);
    notifySubscribers("updateFood", null);
  }

  /**
   * @param foodName Name of food to remove from the daily log for the day.
   */
  public void removeEntry(LogEntry entry) {
    log.remove(entry);
    notifySubscribers("updateLog", null);
  }

  /**
   * Get the number of carbs consumed for a given day
   *
   * @param date The day to get information for
   * @return The amount of carbs in grams as a float
   */
  public float getCarbsConsumed(LocalDate date) {
    float carbsConsumed = 0;
    for (LogEntry entry : log) {
      if (entry.getClass() == ConsumptionEntry.class && entry.getDate().equals(date)) {
        ConsumptionEntry ce = (ConsumptionEntry) entry;
        carbsConsumed +=
            recipes.get(ce.getFoodConsumed().toLowerCase()).getCarbohydrates()
                * ce.getAmountConsumed();
      }
    }
    return carbsConsumed;
  }

  /**
   * Get the amount of fat consumed for a given day
   *
   * @param date The day to get information for
   * @return The amount of fat in grams as a float
   */
  public float getFatConsumed(LocalDate date) {
    float fatConsumed = 0;
    for (LogEntry entry : log) {
      if (entry.getClass() == ConsumptionEntry.class && entry.getDate().equals(date)) {
        ConsumptionEntry ce = (ConsumptionEntry) entry;
        fatConsumed +=
            recipes.get(ce.getFoodConsumed().toLowerCase()).getFat() * ce.getAmountConsumed();
      }
    }
    return fatConsumed;
  }

  /**
   * Get the amount of protein consumed for a given day
   *
   * @param date The day to get information for
   * @return The amount of protein in grams as a float
   */
  public float getProteinConsumed(LocalDate date) {
    float proteinConsumed = 0;
    for (LogEntry entry : log) {
      if (entry.getClass() == ConsumptionEntry.class && entry.getDate().equals(date)) {
        ConsumptionEntry ce = (ConsumptionEntry) entry;
        proteinConsumed +=
            recipes.get(ce.getFoodConsumed().toLowerCase()).getProtein() * ce.getAmountConsumed();
      }
    }
    return proteinConsumed;
  }

  /**
   * Get the amount of sodium consumed for a given day
   *
   * @param date The day to get inforamtion for
   * @return The amount of sodium consumed in milligrams as a float
   */
  public float getSodiumConsumed(LocalDate date) {
    float sodiumConsumed = 0;
    for (LogEntry entry : log) {
      if (entry.getClass() == ConsumptionEntry.class && entry.getDate().equals(date)) {
        ConsumptionEntry ce = (ConsumptionEntry) entry;
        sodiumConsumed +=
            recipes.get(ce.getFoodConsumed().toLowerCase()).getSodium() * ce.getAmountConsumed();
      }
    }
    return sodiumConsumed;
  }

  /**
   * Calculates the percent of the recommended sodium allowance has been consumed for a given day
   *
   * @param date The date to calulate sodium consumption
   * @return float percent of limit conusmed
   */
  public float getPercentSodiumConsumed(LocalDate date) {
    return getSodiumConsumed(date) / DAILY_SODIUM_LIMIT_MG;
  }

  /**
   * Calculates the total calories burnt from exercise for a given day
   *
   * @return number of calories burnt
   */
  public float getCaloriesExpendedFromExercise(LocalDate date) {
    float caloriesBurnt = 0;
    for (ExerciseEntry e : entriesOnDate(getLogEntriesOfType(ExerciseEntry.class), date)) {
      float caloriesPerHour = exercises.get(e.getExerciseName().toLowerCase()).getCaloriePerHour();
      float weight = getWeight(date);
      caloriesBurnt += caloriesPerHour * (weight / 100) * (e.getMinutesOfExercise() / 60);
    }

    return caloriesBurnt;
  }

  /**
   * @param foodName Name of food to get from recipies.
   */
  public IFood getFood(String foodName) {
    return recipes.get(foodName.toLowerCase());
  }

  /**
   * Filters a list of LogEntry objects on a specific date
   *
   * @param toFilter
   * @param date
   * @return
   */
  public <T extends LogEntry> ArrayList<T> entriesOnDate(ArrayList<T> toFilter, LocalDate date) {
    ArrayList<T> filtered = new ArrayList<T>();
    for (LogEntry entry : toFilter) {
      if (entry.getDate().equals(date)) {
        filtered.add((T) entry);
      }
    }
    return filtered;
  }

  public <T extends LogEntry> ArrayList<T> entriesOnOrAfterDate(
      ArrayList<T> toFilter, LocalDate date) {
    ArrayList<T> filtered = new ArrayList<T>();
    for (LogEntry entry : toFilter) {
      if (entry.getDate().isAfter(date) || entry.getDate().equals(date)) {
        filtered.add((T) entry);
      }
    }
    return filtered;
  }

  /**
   * Returns the log entries of a given type by passing in the desired class and it will return an
   * ArrayList of those types found
   *
   * @param <T> Type of {@link LogEntry}
   * @param logEntryClass {@link Class} object of the desired entries
   * @return {@code ArrayList<T>} A list of the requested objects
   */
  public <T extends LogEntry> ArrayList<T> getLogEntriesOfType(Class<T> logEntryClass) {

    ArrayList<T> subArrayList = new ArrayList<T>();
    for (LogEntry entry : log) {
      if (logEntryClass.isInstance(entry)) {
        T t = (T) entry;
        subArrayList.add(t);
      }
    }
    return subArrayList;
  }

  public void addExercise(String exerciseName, float caloriesPerHour) throws ExerciseAlreadyExistsException {
    if (!exerciseExists(exerciseName)) {
      Exercise exercise = new Exercise(exerciseName, caloriesPerHour);
      exercises.put(exerciseName.toLowerCase(), exercise);
    } else {
      throw new ExerciseAlreadyExistsException();
    }
    notifySubscribers("updateExercise", null);
    // notifySubscribers("updateLog", null);
  }

  public HashMap<String, FoodItem> getFoodsMap() {
    HashMap<String, FoodItem> foodMap = new HashMap<String, FoodItem>();
    for (IFood food : recipes.values()) {
      if (food.getClass() == FoodItem.class) {
        foodMap.put(food.getName().toLowerCase(), (FoodItem) food);
      }
    }
    return foodMap;
  }

  public HashMap<String, Recipe> getRecipesMap() {
    HashMap<String, Recipe> recipeMap = new HashMap<String, Recipe>();
    for (IFood food : recipes.values()) {
      if (food.getClass() == Recipe.class) {
        recipeMap.put(food.getName().toLowerCase(), (Recipe) food);
      }
    }
    return recipeMap;
  }

  public HashMap<String, Exercise> getExerciseMap() {
    return exercises;
  }

  public Exercise getExercise(String exerciseName) {
    return exercises.get(exerciseName.toLowerCase());
  }

  public void removeExercise(String exerciseName) {
    exercises.remove(exerciseName.toLowerCase());
    notifySubscribers("updateExercise", "");
  }

 /**
   * remove food entry from log (foodName, Date)
   * @param ce2 ConsumptionEntry
   * @param date LocalDate
   */
  public void removeFoodEntry(ConsumptionEntry ce2, LocalDate date) {
    ArrayList<ConsumptionEntry> allEntries =
        entriesOnOrAfterDate(getLogEntriesOfType(ConsumptionEntry.class), LocalDate.now());
    for (ConsumptionEntry ce : allEntries) {
      // compare each entry and see if it matches the requested information
      if (ce.getFoodConsumed().equalsIgnoreCase(ce2.getFoodConsumed())
          && ce.getDate().equals(date)) {
        log.remove(ce);
        notifySubscribers("updateLog", null);
        return;
      }
    }
  }

  /**
   * remove exercise entry from log (exerciseName, Date)
   * @param ee ExerciseEntry
   * @param date LocalDate
   */
  public void removeExerciseEntry(ExerciseEntry ee, LocalDate date) {
    ArrayList<ExerciseEntry> allEntries =
        entriesOnOrAfterDate(getLogEntriesOfType(ExerciseEntry.class), LocalDate.now());
    for (ExerciseEntry e : allEntries) {
      // compare each entry and see if it matches the requested information
      if (e.getExerciseName().equalsIgnoreCase(ee.getExerciseName())
          && e.getDate().equals(date)) {
        log.remove(e);
        notifySubscribers("updateLog", null);
        return;
      }
    }
  }
    
}
