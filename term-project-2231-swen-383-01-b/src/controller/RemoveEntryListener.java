package controller;

import java.time.LocalDate;

import model.ConsumptionEntry;
import model.ExerciseEntry;
import model.HealthNCare;
import view.UserInterface;

/**
 * This class is responsible for handling the removal of a food item from the
 * log entry
 */
public class RemoveEntryListener {
    private HealthNCare healthNCare = new HealthNCare();
    private UserInterface ui;
    private SaveDataListener saveDataListener = new SaveDataListener(healthNCare);

    public RemoveEntryListener(HealthNCare healthNCare, UserInterface ui) {
        this.healthNCare = healthNCare;
        this.ui = ui;
    }

    public void onFoodAction(LocalDate date, String[] foodEntry) {
        ConsumptionEntry ce = new ConsumptionEntry(date, foodEntry[0], Float.parseFloat(foodEntry[1]));

        try {
            // Remove the entry from the log
            healthNCare.removeEntry(ce);
            healthNCare.removeFoodEntry(ce, date);

            // Save the data using the saveDataListener instance
            saveDataListener.onAction();
            System.out.println("Removed " + foodEntry[0] + " from log entry");

        } catch (Exception e) {
            ui.showErrorMessage(e.getMessage());
        }
    }

    public void onExerciseAction(LocalDate date, String[] exerciseEntry) {
        ExerciseEntry ee = new ExerciseEntry(date, exerciseEntry[0], Float.parseFloat(exerciseEntry[1]));

        try {
            // Remove the entry from the log
            healthNCare.removeEntry(ee);
            healthNCare.removeExerciseEntry(ee, date);

            // Save the data using the saveDataListener instance
            saveDataListener.onAction();
            System.out.println("Removed " + exerciseEntry[0] + " from log entry");

        } catch (Exception e) {
            ui.showErrorMessage(e.getMessage());
        }
    }
}
