package model;

import java.util.ArrayList;

/**
 * Implementation of {@link IFood} that is specifically for Recipes which can have multiple sub
 * IFood items
 */
public class Recipe implements IFood {
  private String name;
  private ArrayList<RecipeItem> recipeItems;

  /**
   * Recipe class constructor
   *
   * @param name of recipe
   * @param recipeItems List of {@link RecipeItem} objects that the recipe is made of
   */
  public Recipe(String name, ArrayList<RecipeItem> recipeItems) {
    this.name = name;
    this.recipeItems = recipeItems;
  }

  /** Constructor to create empty objects */
  public Recipe() {}

  /**
   * Get the name of the recipe
   *
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * Finds all the dependent {@link IFood} objects of a recipe recursively and returns them as a
   * flattened ArrayList
   *
   * @return {@code ArrayList<IFood>} one dimensional list of dependents
   */
  public ArrayList<IFood> getDependents() {
    ArrayList<IFood> dependents = new ArrayList<IFood>();
    for (RecipeItem recipeItem : recipeItems) {
      IFood food = recipeItem.getFood();
      if (food.getClass() == Recipe.class) { // if it is a recipe
        Recipe recipe = (Recipe) food;
        dependents.add(recipe); // add the recipe to the list
        dependents.addAll(recipe.getDependents()); // add it's children recursively
      } else {
        dependents.add(food); // add the basic food
      }
    }
    return dependents;
  }

  /**
   * Determines whether a food is dependent on another IFood
   *
   * @return boolean
   */
  public boolean dependsOn(IFood food) {
    ArrayList<IFood> dependents = getDependents();
    if (dependents.contains(food)) {
      return true;
    }
    return false;
  }

  /**
   * Retrieves the {@link RecipeItem} objects that make up the recipe
   *
   * @return
   */
  public ArrayList<RecipeItem> getRecipeItems() {
    return recipeItems;
  }

  /**
   * Gets the calories of the recipe by recursively adding up the calories of its dependents
   *
   * @return {@code float} number of calories
   */
  public float getCalories() {
    float calories = 0;
    for (RecipeItem ri : recipeItems) {
      calories += ri.getFood().getCalories();
    }
    return calories;
  }

  /**
   * Gets the protein of the recipe by recursively adding up the protein of its dependents
   *
   * @return {@code float} grams of protein
   */
  public float getProtein() {
    float protein = 0;
    for (RecipeItem ri : recipeItems) {
      protein += ri.getFood().getProtein();
    }
    return protein;
  }

  /**
   * Gets the dat of the recipe by recursively adding up the fat of its dependents
   *
   * @return {@code float} grams of fat
   */
  public float getFat() {
    float fat = 0;
    for (RecipeItem ri : recipeItems) {
      fat += ri.getFood().getFat();
    }
    return fat;
  }

  /**
   * Gets the carbohydrates of the recipe by recursively adding up the carbs of its dependents
   *
   * @return {@code float} grams of carbohydrates
   */
  public float getCarbohydrates() {
    float carbohydrates = 0;
    for (RecipeItem ri : recipeItems) {
      carbohydrates += ri.getFood().getCarbohydrates();
    }
    return carbohydrates;
  }

  /**
   * Gets the sodium of the recipe by recursively adding up the sodium of its dependents
   *
   * @return {@code float} milligrams of sodium
   */
  public float getSodium() {
    float sodium = 0;
    for (RecipeItem ri : recipeItems) {
      sodium += ri.getFood().getSodium();
    }
    return sodium;
  }

  public void setRecipeItems(ArrayList<RecipeItem> recipeItems) {
    this.recipeItems = recipeItems;
  }
}
