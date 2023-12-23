package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import model.HealthNCare;

/**
 * This class is responsible for responding to requests for daily statistics.
 */
public class DailyStatsResponder {
  private HealthNCare healthNCare;

  /**
   * Constructor for the DailyStatsResponder class.
   *
   * @param health HealthNCare object to use when responding to requests for daily statistics.
   */
  public DailyStatsResponder(HealthNCare health) {
    this.healthNCare = health;
  }

  /**
   * @return ArrayList of food consumed that day with servings (Food, Servings)
   * @param date  Date to get data for
   */
  public ArrayList<String> getFoodConsumed(LocalDate date) {
    return healthNCare.getFoodConsumed(date);
  }

  /**
   * @return Total calories consumed that day
   * @param date Date to get data for
   */
  public float getCaloriesConsumed(LocalDate date) {
    return healthNCare.getCaloriesConsumed(date);
  }

  /**
   * @return Total value of excess calories consumed that day
   * @param date Date to get data for
   */
  public boolean getOverCalorieLimit(LocalDate date) {
    return healthNCare.isCalorieLimitExceeded(date);
  }

  /**
   * @return Total weight that day
   * @param date Date to get data for
   */
  public float getWeight(LocalDate date) {
    return healthNCare.getWeight(date);
  }

  /**
   * @return Total calories burned that day
   * @param date Date to get data for
   */
  public float getPercentCaloriesConsumed(LocalDate date) {
    return healthNCare.calculatePercentCalorieIntake(date);
  }

  /**
   * @return Total nutritional values consumed that day (Fats, Carbs, Proteins, Sodium).
   * @param date Date to get data for
   */
  public float[] getNutritionConsumed(LocalDate date) {
    float carbs = healthNCare.getCarbsConsumed(date);
    float fat = healthNCare.getFatConsumed(date);
    float protein = healthNCare.getProteinConsumed(date);
    float sodium = healthNCare.getSodiumConsumed(date);
    float total = carbs + fat + (protein / 100);
    float[] nutrition = {(fat / total) * 100, (carbs / total) * 100, (protein / total) * 100, (sodium / total)};
    return nutrition;
  }
}
