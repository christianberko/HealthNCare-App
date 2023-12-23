package view;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * This interface is responsible for defining the methods that the user interface must implement.
 */
public interface UserInterface {
  /**
   * @return LocalDate object to use when logging information
   */
  public LocalDate selectDate();

  public void setLimits(String type);

  /**
   * @return Array of Strings containing the food and nutritional values to add to the database
   */
  public String[] addFood();

  /**
   * @param foodConsumed ArrayList of food consumed that day
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
      float[] nutrition);

  // /**
  // * @return Array of Strings containing the name of the food/recipe eaten and
  // the number of
  // * servings consumed
  // */
  // public String[] addFoodEntry(ArrayList<String> foodList);

  // /**
  // * @return name of the food to delete from the database
  // */
  // public String deleteFoodEntry();

  /** Save the current data */
  public void saveData();

  public void showErrorMessage(String errorMessage);

  public LocalDate getDate();
}
