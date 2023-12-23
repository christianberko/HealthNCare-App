package model;

import java.util.HashMap;

/**
 * Interface for storing exercise objects. The concrete implementations should have the ability to
 * read and write to the storage medium
 */
public interface IExerciseStorage {
  /**
   * Read the exercises from the storage medium
   *
   * @return HashMap of exercises with name as the key
   */
  public HashMap<String, Exercise> readExercises();

  /**
   * Writes the exercises given to the persistent medium
   *
   * @param exercises HashMap of exercises with name as the key
   */
  public void writeExercises(HashMap<String, Exercise> exercises);
}
