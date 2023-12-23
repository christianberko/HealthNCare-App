package controller;

import java.time.LocalDate;
import model.HealthNCare;

public class NutritionChartUpdater {
    private HealthNCare healthNCare;
    private float fat;
    private float carbs;
    private float protein;
    private float sodium;
    private float fatScaled;
    private float carbsScaled;
    private float proteinScaled;
    private float sodiumConsumed;
    private LocalDate date;

    public NutritionChartUpdater(LocalDate date, HealthNCare healthNCare) {
        this.date = date;
        this.healthNCare = healthNCare;
        System.out.println(this.date);

        //get current values
        float fatConsumed = healthNCare.getFatConsumed(date);
        float carbsConsumed = healthNCare.getCarbsConsumed(date);
        float proteinConsumed = healthNCare.getProteinConsumed(date);
        sodiumConsumed = healthNCare.getSodiumConsumed(date);

        //calculate percentages
        //nutrients
        float sum = fatConsumed + carbsConsumed + proteinConsumed;
        fat = fatConsumed / sum;
        carbs = carbsConsumed / sum;
        protein = proteinConsumed / sum;

        //scale nutrients so largest value is 100
        // Find the maximum value among the three nutrients
        float maxNutrient = Math.max(Math.max(fat, carbs), protein);

        // Check if the maximum nutrient is already 100
        if (maxNutrient != 100.0f) {
            // Scale the nutrients proportionally
            float scaleFactor = 100.0f / maxNutrient;
            fatScaled = fat * scaleFactor;
            carbsScaled = carbs * scaleFactor;
            proteinScaled = protein * scaleFactor;
        }

        //sodium
        sodium = sodiumConsumed / 2300;
    }

    public float getFatBarPercentage() {
        System.out.println(fatScaled);
        return fatScaled;
    }

    public float getCarbsBarPercentage() {
        System.out.println(carbsScaled);
        return carbsScaled;
    }

    public float getProteinBarPercentage() {
        System.out.println(proteinScaled);
        return proteinScaled;
    }

    public float getSodiumBarPercentage() {
        System.out.println(sodium);
        float sodiumBar = 0.0f;
        if (sodium > 1) {
            sodiumBar = 100;
        } else {
            sodiumBar = sodium*100;
        }
        return sodiumBar;
    }

    public String getFat() {
        System.out.println(healthNCare.getFatConsumed(date));
        return String.format("%.2f", healthNCare.getFatConsumed(date));
    }

    public String getCarbs() {
        System.out.println(healthNCare.getCarbsConsumed(date));
        return String.format("%.2f", healthNCare.getCarbsConsumed(date));
    }

    public String getProtein() {
        System.out.println(healthNCare.getProteinConsumed(date));
        return String.format("%.2f", healthNCare.getProteinConsumed(date));
    }

    public String getSodium() {
        System.out.println(healthNCare.getSodiumConsumed(date));
        return String.format("%.2f", healthNCare.getSodiumConsumed(date));
    }

    public String getFatPercentage() {
        System.out.println(fat);
        return String.format("%.2f", fat*100);
    }

    public String getCarbsPercentage() {
        System.out.println(carbs);
        return String.format("%.2f", carbs*100);
    }

    public String getProteinPercentage() {
        System.out.println(protein);
        return String.format("%.2f", protein*100);
    }

    public String getSodiumPercentage() {
        System.out.println(sodium);
        return String.format("%.2f", sodium*100);
    }
}
