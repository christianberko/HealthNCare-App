package model;

import java.time.LocalDate;

/** Concrete implementation of {@link LogEntry} that logs a food consumed and a specific quantity */
public class ConsumptionEntry extends LogEntry {
  private String foodConsumedName;
  private float amountConsumed;

  /**
   * Constructor for the ConsumptionEntry class
   *
   * @param date The date the food was consumed
   * @param foodConsumedName The name of the food consumed
   * @param amountConsumed The amount of the food consumed
   */
  public ConsumptionEntry(LocalDate date, String foodConsumedName, float amountConsumed) {
    super(date);
    this.foodConsumedName = foodConsumedName;
    this.amountConsumed = amountConsumed;
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
      "f",
      foodConsumedName,
      String.valueOf(amountConsumed)
    };
  }

  /**
   * Get the name of the food consumed
   *
   * @return {@code String} the name of the food
   */
  public String getFoodConsumed() {
    return foodConsumedName;
  }

  /**
   * Get the amount of the food consumed
   *
   * @return {@code float} The amount of servings consumed
   */
  public float getAmountConsumed() {
    return amountConsumed;
  }

  @Override
  public String toString() {
    return foodConsumedName+" | Number of Servings: "+String.valueOf(amountConsumed);
  }
}
