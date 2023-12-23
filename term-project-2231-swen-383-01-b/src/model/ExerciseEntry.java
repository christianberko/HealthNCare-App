package model;

import java.time.LocalDate;

/**
 * Concrete implementation of {@link LogEntry} that logs when a user engages in exercise and for how
 * long
 */
public class ExerciseEntry extends LogEntry {
  private String exerciseName;
  private float minutesOfExercise;

  /**
   * Constructor of ExerciseEntry class
   *
   * @param date the date the exercise was completed
   * @param exerciseName the name of exercise completed
   * @param minutesOfExercise number of minutes of exercise
   */
  public ExerciseEntry(LocalDate date, String exerciseName, float minutesOfExercise) {
    super(date);
    this.exerciseName = exerciseName;
    this.minutesOfExercise = minutesOfExercise;
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
      "e",
      exerciseName,
      String.valueOf(minutesOfExercise)
    };
  }

  /**
   * Gets the name of the exercise
   *
   * @return name of the exercise
   */
  public String getExerciseName() {
    return exerciseName;
  }

  /**
   * Gets the number of minutes the exercise was completed for
   *
   * @return number of minutes of exercise
   */
  public float getMinutesOfExercise() {
    return minutesOfExercise;
  }

  @Override
  public String toString() {
    return exerciseName+" | Minutes spent: "+String.valueOf(minutesOfExercise);
  }
}
