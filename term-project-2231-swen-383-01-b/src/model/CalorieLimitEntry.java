package model;

import java.time.LocalDate;

/**
 * A concrete implementation of a {@link LogEntry} that tracks a person's calorie limit at a
 * particular point in time
 */
public class CalorieLimitEntry extends LogEntry {
  private float calorieLimit;

  public CalorieLimitEntry(LocalDate date, float calorieLimit) {
    super(date);
    this.calorieLimit = calorieLimit;
  }

  /**
   * Prepares the attributes of the class into an array to be processed into a record for the
   * storage medium
   *
   * @return {@code String[]} Each attribute in the array in the correct order
   */
  public String[] toRecord() {
    return new String[] {
      String.valueOf(date.getYear()),
      String.valueOf(date.getMonthValue()),
      String.valueOf(date.getDayOfMonth()),
      "c",
      String.valueOf(calorieLimit)
    };
  }

  /**
   * Retrieves the {@code calorieLimit} attribute
   *
   * @return {@code calorieLimit} The calorie limit at that point in time
   */
  public float getCalorieLimit() {
    return calorieLimit;
  }
}
