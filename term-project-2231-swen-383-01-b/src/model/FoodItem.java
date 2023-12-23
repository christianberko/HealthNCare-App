package model;

/**
 * Implementaion of the {@link IFood} interface that specifically handles basic food items and the
 * attributes of a single serving
 */
public class FoodItem implements IFood {
  private String name;
  private float calories;
  private float fat;
  private float carbohydrates;
  private float protein;
  private float sodium;

  /**
   * Constructor for the FoodItem class
   *
   * @param name name of the food
   * @param calories calories in the food
   * @param fat fat in the food
   * @param carbohydrates carbohydrates in the food
   * @param protein protein in the food
   * @param sodium sodium in the food
   */
  public FoodItem(
      String name, float calories, float fat, float carbohydrates, float protein, float sodium) {
    this.name = name;
    this.calories = calories;
    this.fat = fat;
    this.carbohydrates = carbohydrates;
    this.protein = protein;
    this.sodium = sodium;
  }

  /**
   * Get the name of the food
   *
   * @return {@code String} name of the food
   */
  public String getName() {
    return name;
  }

  /**
   * Get the calories in the food
   *
   * @return {@code float} number of calories in the food
   */
  public float getCalories() {
    return calories;
  }

  /**
   * Get the amount of protein in the food
   *
   * @return {@code float} grams of protein in the food
   */
  public float getProtein() {
    return protein;
  }

  /**
   * Get the amount of fat in the food
   *
   * @return {@code float} grams of fat in the food
   */
  public float getFat() {
    return fat;
  }

  /**
   * Get the amount of carbohydrates in the food
   *
   * @return {@code float} grams of carbs in the food
   */
  public float getCarbohydrates() {
    return carbohydrates;
  }

  /**
   * Get the amount of sodium in the food
   *
   * @return {@code float} milligrams of sodium in the food
   */
  public float getSodium() {
    return sodium;
  }

  /**
   * Get the number of servings of the food
   *
   * @return {@code float} number of servings of the food
   */
  public boolean dependsOn(IFood food) {
    // since it is just a basic food item
    // it will never depend on other foods
    return false;
  }

  public void setCalories(float calories) {
    this.calories = calories;
  }

  public void setCarbohydrates(float carbohydrates) {
    this.carbohydrates = carbohydrates;
  }

  public void setFat(float fat) {
    this.fat = fat;
  }

  // needs modification in order to be implemented
  // you need to make sure that the hashmap key is also
  // updated and not just this field
  // public void setName(String name) {
  // this.name = name;
  // }

  public void setProtein(float protein) {
    this.protein = protein;
  }

  public void setSodium(float sodium) {
    this.sodium = sodium;
  }
}
