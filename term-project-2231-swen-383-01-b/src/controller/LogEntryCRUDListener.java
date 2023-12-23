package controller;

import java.time.LocalDate;
import java.util.ArrayList;

import model.CalorieLimitEntry;
import model.ConsumptionEntry;
import model.Exercise;
import model.ExerciseEntry;
import model.FoodItem;
import model.HealthNCare;
import model.IFood;
import model.LogEntry;
import model.Recipe;
import model.WeightEntry;
import view.UserInterface;

public class LogEntryCRUDListener {
    private HealthNCare healthNCare;
    private UserInterface ui;

    public LogEntryCRUDListener(HealthNCare healthNCare, UserInterface ui) {
        this.healthNCare = healthNCare;
        this.ui = ui;
    }

    public void createFoodLogEntry(LocalDate date, String[] foodEntry) {
        ConsumptionEntry ce = new ConsumptionEntry(date, foodEntry[0], Float.parseFloat(foodEntry[1]));
        try {
            healthNCare.addLogEntry(ce);
        } catch (Exception e) {
            ui.showErrorMessage(e.getMessage());
        }
    }

    public void createExerciseLogEntry(LocalDate date, String exerciseName, float minutesOfExercise) {
        ExerciseEntry entry = new ExerciseEntry(date, exerciseName, minutesOfExercise);
        try {
            healthNCare.addLogEntry(entry);
        } catch (Exception e) {
            ui.showErrorMessage(e.getMessage());
        }
    }

    /**
     * @param date      Date to add the food entry to
     * @param foodEntry Includes the name of the food and the number of servings
     */
    public void addConsumptionEntry(LocalDate date, String[] foodEntry) {
        ConsumptionEntry ce = new ConsumptionEntry(date, foodEntry[0], Float.parseFloat(foodEntry[1]));
        try {
            healthNCare.addLogEntry(ce);
        } catch (Exception e) {
            ui.showErrorMessage(e.getMessage());
        }
    }

    public void deleteFoodLogEntry(LocalDate date, String foodName, float quantity) {
        ArrayList<ConsumptionEntry> allEntries = healthNCare
                .entriesOnDate(healthNCare.getLogEntriesOfType(ConsumptionEntry.class), date);
        for (ConsumptionEntry ce : allEntries) {
            // compare each entry and see if it matches the requested information
            if (ce.getFoodConsumed().toLowerCase().equals(foodName.toLowerCase()) &&
                    ce.getAmountConsumed() == quantity) {
                healthNCare.removeEntry(ce);
            }
        }
    }

    // public void deleteExerciseLogEntry(LocalDate date, String exerciseName, float
    // minutesPerformed) {
    // ArrayList<ExerciseEntry> allEntries = healthNCare
    // .entriesOnDate(healthNCare.getLogEntriesOfType(ExerciseEntry.class), date);
    // for (ExerciseEntry ee : allEntries) {
    // // compare each entry and see if it matches the requested information
    // if (ee.getExerciseName().toLowerCase().equals(exerciseName.toLowerCase()) &&
    // ee.getMinutesOfExercise() == minutesPerformed) {
    // healthNCare.removeEntry(ee);
    // }
    // }
    // }

    public void removeLogEntry(LogEntry entry) {
        healthNCare.removeEntry(entry);
    }

    /**
     * @param date         Date chose by the user
     * @param calorieLimit Calorie Limit specified by the user This function
     *                     onAction will create a
     *                     new CalorieLimit entry and then add it to the Log
     */
    public void addCalorieLimit(LocalDate date, float calorieLimit) {
        CalorieLimitEntry cle = new CalorieLimitEntry(date, calorieLimit);
        try {
            healthNCare.addLogEntry(cle);
        } catch (Exception e) {
            ui.showErrorMessage(e.getMessage());
        }
    }

    /**
     * @param date   Date to add the weight entry to
     * @param weight Weight to add to the log
     */
    public void addWeightEntry(LocalDate date, float weight) {
        WeightEntry we = new WeightEntry(date, weight);
        try {
            healthNCare.addLogEntry(we);
        } catch (Exception e) {
            ui.showErrorMessage(e.getMessage());
        }
    }

    public ArrayList<ConsumptionEntry> getRecipesLogged(LocalDate date) {
        ArrayList<ConsumptionEntry> recipesConsumed = new ArrayList<ConsumptionEntry>();
        ArrayList<ConsumptionEntry> allFoodConsumed = healthNCare
                .entriesOnDate(healthNCare.getLogEntriesOfType(ConsumptionEntry.class), date);
        for (ConsumptionEntry ce : allFoodConsumed) {
            IFood food = healthNCare.getFood(ce.getFoodConsumed());
            if (food.getClass() == Recipe.class) {
                recipesConsumed.add((ConsumptionEntry) ce);
            }
        }
        return recipesConsumed;
    }

    public ArrayList<ExerciseEntry> getExercisesLogged(LocalDate date) {
        ArrayList<Exercise> exercisesPerformed = new ArrayList<Exercise>();
        ArrayList<ExerciseEntry> exerciseEntries = healthNCare
                .entriesOnDate(healthNCare.getLogEntriesOfType(ExerciseEntry.class), date);
        for (ExerciseEntry e : exerciseEntries) {
            Exercise exercise = healthNCare.getExercise(e.getExerciseName());
            exercisesPerformed.add(exercise);
        }
        return exerciseEntries;
    }

    public ArrayList<ConsumptionEntry> getBasicFoodsLogged(LocalDate date) {
        ArrayList<ConsumptionEntry> basicFoodsConsumed = new ArrayList<ConsumptionEntry>();
        ArrayList<ConsumptionEntry> allFoodConsumed = healthNCare
                .entriesOnDate(healthNCare.getLogEntriesOfType(ConsumptionEntry.class), date);
        for (ConsumptionEntry ce : allFoodConsumed) {
            IFood food = healthNCare.getFood(ce.getFoodConsumed());
            if (food.getClass() == FoodItem.class) {
                basicFoodsConsumed.add((ConsumptionEntry) ce);
            }
        }
        return basicFoodsConsumed;
    }
}
