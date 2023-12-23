package controller;

import model.HealthNCare;
import view.UserInterface;

public class ExerciseCRUDListener {
    private HealthNCare hnc;
    UserInterface ui;

    public ExerciseCRUDListener(HealthNCare hnc, UserInterface ui) {
        this.hnc = hnc;
        this.ui = ui;
    }

    public void create(String exerciseName, float caloriesPerHour) {
        try {
            hnc.addExercise(exerciseName, caloriesPerHour);
        } catch (Exception e) {
            ui.showErrorMessage(e.getMessage());
        }
    }

    public void retrieve(String exerciseName) {
        hnc.getExercise(exerciseName);
    }

    public void update(String exerciseName, float newCaloriesPerHour) {
        hnc.getExercise(exerciseName).setCaloriesPerHour(newCaloriesPerHour);
    }

    public void delete(String exerciseName) {
        hnc.removeExercise(exerciseName);
    }
}
