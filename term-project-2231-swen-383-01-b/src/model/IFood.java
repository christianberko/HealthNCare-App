package model;

/**
 * Common interface for all food items. Whether the item is a basic food item or a recipe it should
 * be able to get the nutritional information from it.
 */
public interface IFood {
  /**
   * @return The name of the food item
   */
  public String getName();

  /**
   * @return The number of calories in the food item
   */
  public float getCalories();

  /**
   * @return The amount of protein in the food item
   */
  public float getProtein();

  /**
   * @return The amount of fat in the food item
   */
  public float getFat();

  /**
   * @return The amount of carbohydrates in the food item
   */
  public float getCarbohydrates();

  /**
   * @return The amount of sodium in the food item
   */
  public float getSodium();

  /**
   * @return The number of servings of the food item
   */
  public boolean dependsOn(IFood food);
}
