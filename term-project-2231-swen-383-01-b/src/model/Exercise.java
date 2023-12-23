package model;

/** Class for represents a single exercise activity */
public class Exercise {
  String name;

  /**
   * Represents the calories burnt per hour for a 100 pound person, so make sure to adjust for
   * weight when doing calculation
   */
  float caloriesPerHour;

  /**
   * Contructor for Exercise object that takes a name and an amount of calories burned per hour for
   * a 100 pound person
   *
   * @param name Name of exercise
   * @param caloriesPerHour Calories burnt per hour for 100 pound person
   */
  public Exercise(String name, float caloriesPerHour) {
    this.name = name;
    this.caloriesPerHour = caloriesPerHour;
  }

  /**
   * Gets the name of the exercise
   *
   * @return String name
   */
  public String getName() {
    return name;
  }

  /** Gets the calories burnt by performing this exercise for one hour for a 100 pound person */
  public float getCaloriePerHour() {
    return caloriesPerHour;
  }

  public void setCaloriesPerHour(float caloriesPerHour) {
    this.caloriesPerHour = caloriesPerHour;
  }
}
