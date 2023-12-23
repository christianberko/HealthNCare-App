package controller;

import model.HealthNCare;
import java.time.LocalDate;

public class CheckCaloriesResponder {
    private HealthNCare healthNCare;
    public CheckCaloriesResponder(HealthNCare health) {
        healthNCare = health;
    }

    public float getConsumedCalories(LocalDate date) {
        return healthNCare.getCaloriesConsumed(date);
    }

    public float getExpendedCalories(LocalDate date) {
        return healthNCare.getCaloriesExpendedFromExercise(date);
    }

    public float getCalorieLimit(LocalDate date) {
        return healthNCare.getCalorieLimit(date);
    }
}
