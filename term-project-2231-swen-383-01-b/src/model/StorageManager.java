package model;

import java.util.ArrayList;
import java.util.HashMap;

/** Manages the storage of the log and recipes */
public class StorageManager {
  private ILog logStorage;
  private IRecipeLog recipeStorage;
  private IExerciseStorage exerciseStorage;

  private ArrayList<LogEntry> log;
  private HashMap<String, IFood> recipes;
  private HashMap<String, Exercise> exercises;

  /** Constructor for the StorageManager class */
  public StorageManager() {
    logStorage = new LogCSVFile();
    log = logStorage.readLog();

    recipeStorage = new RecipeCSVFile();
    recipes = recipeStorage.readRecipes();

    exerciseStorage = new ExerciseCSVFile();
    exercises = exerciseStorage.readExercises();
  }

  /**
   * Returns the log
   *
   * @return {@link ArrayList} of {@link LogEntry}
   */
  public ArrayList<LogEntry> getLog() {
    return log;
  }

  /**
   * Saves the log
   *
   * @return {@link HashMap} of {@link String} to {@link IFood}
   */
  public void saveLog(ArrayList<LogEntry> log) {
    logStorage.writeLog(log);
  }

  /**
   * Returns the recipes
   *
   * @return {@link HashMap} of {@link String} to {@link IFood}
   */
  public HashMap<String, IFood> getRecipes() {
    return recipes;
  }

  /**
   * Saves the recipes
   *
   * @param recipes The recipes to save
   */
  public void saveRecipes(HashMap<String, IFood> recipes) {
    recipeStorage.writeRecipes(recipes);
  }

  /**
   * Gets the exercises
   *
   * @return {@link HashMap} of {@link String} to {@link Exercise}
   */
  public HashMap<String, Exercise> getExercises() {
    return exercises;
  }

  /**
   * Saves the exercises
   *
   * @param exercises The exercises to be saved
   */
  public void saveExercises(HashMap<String, Exercise> exercises) {
    exerciseStorage.writeExercises(exercises);
  }
}
