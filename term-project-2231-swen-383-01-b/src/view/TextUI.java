package view;

import controller.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Scanner;
import model.HealthNCare;

/*
 * This class is responsible for the text user interface.
 * It is the main view class of the application.
 */
public class TextUI implements Observer, UserInterface {
  Scanner scanner = new Scanner(System.in);
  private HealthNCare health;
  private LocalDate enteredDate;

  private SaveDataListener saveDataListener;
  private ItemListResponder foodListResponder;
  private DailyStatsResponder dailyStatsResponder;
  private FoodCRUDListener foodCRUDListener;
  private LogEntryCRUDListener logEntryCRUDListener;
  private String divider = "------------------------------------------------------------";
  private String header = "=================================================";

  /**
   * Constructor for TextUI
   *
   * @param hnc The main model class
   */
  public TextUI(HealthNCare hnc) {
    this.health = hnc;
    health.addSubscriber(this);

    System.out.println(
        "Welcome to the Health And Care App\nDeveloped by Team B, Java Juggernauts\n");
    selectDate();

    // Create listeners
    saveDataListener = new SaveDataListener(health);
    foodListResponder = new ItemListResponder(health);
    dailyStatsResponder = new DailyStatsResponder(health);
    foodCRUDListener = new FoodCRUDListener(hnc, this);
    logEntryCRUDListener = new LogEntryCRUDListener(hnc, this);

    while (true) {
      clearScreen();
      printMainMenu();
      processUserInput();
    }
  }

  /** Clears the console screen */
  public void clearScreen() {
    System.out.print("\033[H\033[2J"); // clear screen
    System.out.flush(); // empty the buffer
  }

  /** Wait for input to continue execution */
  public void waitForInput() {
    // Wait for user input before clearing the screen
    System.out.println("\nPress Enter to continue...");
    scanner.nextLine(); // Wait for user to press Enter
  }

  /** Prints the main menu to the console */
  public void printMainMenu() {
    System.out.println("=================================================");
    System.out.println("HealthNCare App\t\tSelected Date: " + enteredDate);
    System.out.println("=================================================");
    System.out.println(
        "1: Set your calorie limit and weight for the day\n"
            + "2: Add a new food to use for meals\n"
            + "3: Add a new recipe\n"
            + "4: View your stats for the day\n"
            + "5: Log a meal\n"
            + "6: Delete a meal from the log\n"
            + "7: Save the current app data to storage\n"
            + "8: Change the date (Current date: "
            + enteredDate
            + ")\n"
            + "0: Exit the application (saves current app data to storage)");
    System.out.println(divider);
    System.out.print("Select what you want to do on this date (" + enteredDate + "): ");
  }

  /** Processes the user input and calls the appropriate method */
  private void processUserInput() {
    String choice = scanner.nextLine().trim();
    switch (choice) {
      case "1":
        setLimits(choice);
        break;
      case "2":
        foodCRUDListener.addFoodItem(addFood());
        break;
      case "3":
        addRecipe();
        break;
      case "4":
        viewDailyStats(
            dailyStatsResponder.getFoodConsumed(enteredDate),
            dailyStatsResponder.getCaloriesConsumed(enteredDate),
            dailyStatsResponder.getOverCalorieLimit(enteredDate),
            dailyStatsResponder.getWeight(enteredDate),
            dailyStatsResponder.getNutritionConsumed(enteredDate));
        break;
      case "5":
        logEntryCRUDListener.addConsumptionEntry(enteredDate, addFoodEntry(foodListResponder.getFoods()));
        break;
      case "6":
        foodCRUDListener.deleteFood(deleteFoodEntry());
        break;
      case "7":
        saveData();
        break;
      case "8":
        selectDate();
        break;
      case "0":
        saveData();
        System.exit(0);
        break;
      default:
        System.out.println(
            "Invalid input: Enter a number corresponding to the option you wish to activate when"
                + " the menu returns.");
        System.out.println("Please press Enter to confirm and try again.");
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
          break;
        }

        break;
    }
    clearScreen();
  }

  /**
   * @return LocalDate used for getting and setting log entries
   */
  public LocalDate selectDate() {
    System.out.println(header);
    System.out.println("SELECT DATE");
    System.out.println(divider);
    System.out.println(
        "Choose a date to view and edit; if you want to use the current date just leave each option"
            + " blank");
    System.out.println(divider);

    int year;
    do {
      System.out.print("Enter the year (or press Enter to use the current year): ");
      String yearInput = scanner.nextLine().trim();

      if (yearInput.isEmpty()) {
        year = LocalDate.now().getYear();
        break;
      }

      try {
        year = Integer.parseInt(yearInput);
        break;
      } catch (NumberFormatException e) {
        System.err.println("Invalid input. Please enter a valid year.");
      }
    } while (true);

    int month;
    do {
      System.out.print("Enter the month (Use numbers 1-12): ");
      String monthInput = scanner.nextLine().trim();

      if (monthInput.isEmpty()) {
        month = LocalDate.now().getMonthValue();
        break;
      }

      try {
        month = Integer.parseInt(monthInput);
        if (month >= 1 && month <= 12) {
          break;
        } else {
          throw new NumberFormatException();
        }
      } catch (NumberFormatException e) {
        System.err.println("Invalid input. Please enter a valid month (1-12).");
      }
    } while (true);

    int day;
    do {
      System.out.print("Enter the day (Use numbers 1-31): ");
      String dayInput = scanner.nextLine().trim();

      if (dayInput.isEmpty()) {
        day = LocalDate.now().getDayOfMonth();
        break;
      }

      try {
        day = Integer.parseInt(dayInput);
        YearMonth yearMonth = YearMonth.of(year, month);
        if (day >= 1 && day <= yearMonth.lengthOfMonth()) {
          break;
        } else {
          throw new NumberFormatException();
        }
      } catch (NumberFormatException e) {
        System.err.println("Invalid input. Please enter a valid day.");
      }
    } while (true);

    enteredDate = LocalDate.of(year, month, day);
    waitForInput();
    return enteredDate;
  }

  /**
   * @return Array of Strings containing the calorie limit and weight to log for the day.
   */
  public void setLimits(String type) {
    System.out.println(header);
    System.out.println("SET CALORIE LIMIT AND WEIGHT FOR " + enteredDate);
    System.out.println(divider);
    System.out.println("Enter the calorie limit for " + enteredDate + ":");
    String calorieLimit = scanner.next();
    System.out.println("Enter your weight (lbs) for " + enteredDate + ":");
    String weight = scanner.next();
    waitForInput();

    logEntryCRUDListener.addWeightEntry(enteredDate, Float.parseFloat(weight));
    logEntryCRUDListener.addCalorieLimit(enteredDate, Float.parseFloat(calorieLimit));
  }

  /**
   * @return Array of Strings containing the food and nutritional values to add to the database
   */
  public String[] addFood() {
    System.out.println(header);
    System.out.println("ADD A FOOD TO USE IN RECIPES AND MEAL LOGS");
    System.out.println(divider);
    System.out.println("Enter the name of the food item: ");
    scanner.nextLine();
    String food = scanner.nextLine();
    // Check to make sure this food doesn't already exist
    while (foodCRUDListener.checkFoodExists(food)) {
      System.out.println("That food name already exists, try again: ");
      food = scanner.nextLine();
    }

    System.out.println("Enter the number of calories this food provides: ");
    String calories = scanner.next();
    System.out.println("Enter the amount of fat (g) this food provides: ");
    String fat = scanner.next();
    System.out.println("Enter the amount of carbohydrates (g) this food provides: ");
    String carbs = scanner.next();
    System.out.println("Enter the amount of protein (g) this food provides: ");
    String protein = scanner.next();
    System.out.println("Enter the number of sodium (mg) this food provides: ");
    String sodium = scanner.next();
    return new String[] {food, calories, fat, carbs, protein, sodium};
  }

  /** Prompts the user for necessary input to create a new recipe */
  public void addRecipe() {
    System.out.println(header);
    System.out.println("ADD A RECIPE TO USE IN MEAL LOGS");
    System.out.println(divider);
    System.out.println("What is the name of the new recipe?: ");
    String name = scanner.nextLine();
    System.out.println("How many ingredients are required?: ");
    int count = scanner.nextInt();
    String[][] recipe = new String[count + 1][2];
    // recipe[0][0] = name;
    for (int i = 0; i <= count; i++) {
      System.out.println("What is the name of item " + String.valueOf(i + 1) + ": ");
      scanner.nextLine();
      String itemName = scanner.nextLine();
      while (!foodCRUDListener.checkFoodExists(itemName)) {
        System.out.println("Food couldn't be found. Please try again: ");
        itemName = scanner.nextLine();
      }
      recipe[i][0] = itemName;
      System.out.println("How many are required: ");
      String amount = scanner.next();
      recipe[i][1] = amount;
    }
    foodCRUDListener.addRecipe(name, recipe);
  }

  /**
   * Shows the user their stats for the selected day
   *
   * @param foodConsumed ArrayList of food consumed that day with servings (Food, Servings)
   * @param caloriesConsumed total number of calories consumed that day
   * @param overCalorieLimit Was the calorie limit for the day exceeded?
   * @param weight weight recorded for that day
   * @param nutrition Total nutritional values consumed that day (Fats, Carbs, Proteins).
   */
  public void viewDailyStats(
      ArrayList<String> foodConsumed,
      float caloriesConsumed,
      boolean overCalorieLimit,
      float weight,
      float[] nutrition) {
    // Display the stats before clearing the screen
    System.out.println(header);
    System.out.println("YOUR DAILY STATS FOR " + enteredDate);
    System.out.println(divider);
    System.out.println("Your weight is " + weight + " lbs");
    System.out.println("Here is your summary for " + enteredDate + ":\n");
    System.out.println("List of food eaten and number of servings for each meal: ");
    for (String food : foodConsumed) {
      System.out.println(" - " + food);
    }
    System.out.println();
    System.out.println("Number of calories consumed: " + caloriesConsumed);
    if (overCalorieLimit) {
      System.out.println("You are over your daily limit of calories for the day.");
    } else {
      System.out.println("You are within your daily limit of calories for the day!");
    }
    System.out.println();
    System.out.println("Here are your nutrition values in percentages:");
    String fat = "";
    String carbs = "";
    String protein = "";
    String sodium = "";
    for ( int num = 0; num < nutrition[0]; num++) {
      fat += "*";
    }
    for ( int num = 0; num < nutrition[1]; num++) {
      carbs += "*";
    }
    for ( int num = 0; num < nutrition[2]; num++) {
      protein += "*";
    }
    for ( int num = 0; num < nutrition[3]; num++) {
      sodium += "*";
    }
    System.out.printf("%-29s %-2.0f%%  %-100s","Total milligrams of FATS:", nutrition[0], fat);
    System.out.printf("\n%-29s %-2.0f%%  %-100s","Total milligrams of CARBS:", nutrition[1], carbs);
    System.out.printf("\n%-29s %-2.0f%%  %-100s","Total milligrams of PROTEIN:", nutrition[2], protein);
    System.out.printf("\n%-29s %-2.0f%%  %-100s","Total milligrams of SODIUM:", nutrition[3], sodium);

    waitForInput();
    // clearScreen();
  }

  /**
   * Allows the user add a consumption entry to the log
   *
   * @return Array of Strings containing the name of the food/recipe eaten and the number of
   *     servings consumed
   */
  public String[] addFoodEntry(ArrayList<String> foodList) {
    System.out.println(header);
    System.out.println("ADD A MEAL TO YOUR DAILY LOG FOR " + enteredDate);
    System.out.println(divider);
    System.out.println("Choose a food to add to the Daily Log: ");
    System.out.println("Type \"/foods\" to show the available foods or enter it by name");
    System.out.print("Food to log: ");
    String input = scanner.nextLine();
    if (input.equals("/foods")) {
      listFoods();
      System.out.print("Food to log: ");
      input = scanner.nextLine();
    }
    System.out.print("How many servings did you consume? ");
    String servings = scanner.next();
    waitForInput();
    return new String[] {input, servings};
  }

  /**
   * Removes a food from the list of possible foods to use
   *
   * @return The name of the food to delete
   */
  public String deleteFood() {
    System.out.println(header);
    System.out.println("REMOVE A FOOD FROM THE DATABASE");
    System.out.println(divider);
    System.out.println("Choose a food to remove");
    System.out.print("Type the name of the food or \"/foods\" to show the available options: ");
    scanner.nextLine();
    String foodName = scanner.nextLine();
    if (foodName.equals("/foods")) {
      listFoods();
      System.out.print("Food to remove: ");
      scanner.nextLine();
      foodName = scanner.nextLine();
    }
    while (!foodCRUDListener.checkFoodExists(foodName)) {
      System.out.println("Food does not exist. Please try again: ");
      foodName = scanner.nextLine();
    }
    waitForInput();
    return foodName;
  }

  /**
   * Delete a food consumption log entry
   *
   * @return name of the food to delete from the database
   */
  public String deleteFoodEntry() {
    System.out.println(header);
    System.out.println("REMOVE A FOOD ENTRY FROM YOUR DAILY LOG FOR " + enteredDate);
    System.out.println(divider);
    ArrayList<String> foodList = dailyStatsResponder.getFoodConsumed(enteredDate);
    System.out.println("Foods consumed:");
    for (String food : foodList) {
      System.out.println(" - " + food);
    }
    System.out.println("Choose a food to remove from the Daily Log: ");
    scanner.nextLine();
    String foodName = scanner.nextLine();
    while (!foodCRUDListener.checkFoodExists(foodName)) {
      System.out.println("Food does not exist. Please try again: ");
      foodName = scanner.nextLine();
    }
    waitForInput();
    return foodName;
  }

  /** Save the current data */
  public void saveData() {
    clearScreen();
    System.out.println(header);
    System.out.println("*** DO NOT CLOSE THE APP - DATA IS BEING SAVED ***");
    saveDataListener.onAction();
    System.out.println("Data has been successfully saved!");
    waitForInput();
  }

  /** Update method for Observer interface */
  public void update(String messageType, String message) {}

  /**
   * Prints the list of foods to the console
   *
   * @param foodList ArrayList of foods to print
   */
  private void listFoods() {
    ArrayList<String> foodList = foodListResponder.getFoods();
    for (String food : foodList) {
      System.out.println(" - " + food);
    }
  }

  public void showErrorMessage(String errorMessage) {
    System.out.println(errorMessage);
  }

  public LocalDate getDate() {
    return enteredDate;
  }
}
