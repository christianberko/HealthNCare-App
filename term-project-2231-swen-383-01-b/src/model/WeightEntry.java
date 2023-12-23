package model;

import java.time.LocalDate;

/** Concrete implementation of the {@link LogEntry} that tracks weight over time */
public class WeightEntry extends LogEntry {
  private float weight;

  /**
   * Constructor for the WeightEntry class
   *
   * @param date The date the weight was recorded
   * @param weight The weight recorded
   */
  public WeightEntry(LocalDate date, float weight) {
    super(date);
    this.weight = weight;
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
      "w",
      String.valueOf(weight)
    };
  }

  /**
   * Returns the weight at the time of this record
   *
   * @return {@code float} Weight in pounds
   */
  public float getWeight() {
    return weight;
  }
}
