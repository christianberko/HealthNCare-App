package model;

import java.util.HashMap;

/**
 * Interface definition for any Recipe storage object. It should be able to read all the items and
 * write the items back out to the storage medium
 */
public interface IRecipeLog {
  /**
   * Reads the recipes from the storage media
   *
   * @return recipes as a HashMap
   */
  public HashMap<String, IFood> readRecipes();

  /**
   * Write the recipes to the storage medium
   *
   * @param recipes The recipes to write
   */
  public void writeRecipes(HashMap<String, IFood> recipes);
}
