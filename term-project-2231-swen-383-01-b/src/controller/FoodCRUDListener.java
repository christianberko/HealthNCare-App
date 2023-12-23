package controller;

import java.util.ArrayList;

import exceptions.FoodAlreadyExistsException;
import model.FoodItem;
import model.HealthNCare;
import model.IFood;
import model.Recipe;
import model.RecipeItem;
import view.UserInterface;

public class FoodCRUDListener {
    private HealthNCare healthNCare;
    private UserInterface ui;

    public FoodCRUDListener(HealthNCare healthNCare, UserInterface ui) {
        this.healthNCare = healthNCare;
        this.ui = ui;
    }

    public void addRecipe(String recipeName, String[][] recipeRequirements) {
        ArrayList<RecipeItem> recipeReqs = new ArrayList<RecipeItem>();
        for (int i = 0; i < recipeRequirements.length; i++) {

            RecipeItem ri = new RecipeItem(
                    healthNCare.getFood(recipeRequirements[i][0].toLowerCase()),
                    Float.parseFloat(recipeRequirements[i][1]));
            recipeReqs.add(ri);
        }
        Recipe recipe = new Recipe(recipeName, recipeReqs);
        try {
            healthNCare.addFood(recipe);
        } catch (FoodAlreadyExistsException f) {
            ui.showErrorMessage(f.getMessage());
        }
    }

    /**
     * This method is called when the user wants to delete a food item from the
     * database.
     *
     * @param foodName Name of the food item to delete from the database.
     */
    public void deleteFood(String foodName) {
        IFood f = healthNCare.getFood(foodName);
        try {
            healthNCare.removeFood(f);
        } catch (Exception e) {
            ui.showErrorMessage(e.getMessage());
        }
    }

    /**
     * Updates the properties of a FoodItem based on the input fields.
     *
     * @param foodName    The name of the food item to update.
     * @param inputFields An array of strings representing the updated values.
     */
    public void updateFood(String foodName, String[] inputFields) {
        healthNCare.updateBasicFood(foodName, inputFields);
    }

    public void updateRecipe(String recipeName, ArrayList<String[]> recipeItems) {
        Recipe recipe = (Recipe) healthNCare.getFood(recipeName);
        ArrayList<RecipeItem> newRecipeItems = new ArrayList<RecipeItem>();
        for (String[] recipeItem : recipeItems) {
            IFood food = healthNCare.getFood(recipeItem[0]);
            RecipeItem ri = new RecipeItem(food, Float.parseFloat(recipeItem[1]));
            newRecipeItems.add(ri);
        }
        healthNCare.updateRecipe(recipeName, newRecipeItems);
    }

    /**
     * 
     * @param inputFields a array of strings to be parsed
     */
    public void addFoodItem(String[] inputFields) {
        FoodItem food = new FoodItem(
                inputFields[0],
                Float.parseFloat(inputFields[1]),
                Float.parseFloat(inputFields[2]),
                Float.parseFloat(inputFields[3]),
                Float.parseFloat(inputFields[4]),
                Float.parseFloat(inputFields[5]));
        try {
            healthNCare.addFood(food);
        } catch (FoodAlreadyExistsException faee) {
            ui.showErrorMessage(faee.getMessage());
        }
    }

    /**
     * @param foodName Includes the name of the food to be checked
     * @return true if the food exists in the database, false otherwise
     */
    public boolean checkFoodExists(String foodName) {
        return healthNCare.foodExists(foodName);
    }

    public IFood retrieveFood(String foodName) {
        return healthNCare.getFood(foodName);
    }

}
