package model;

/**
 * This class is responsible for storing the recipe items.
 */
public class RecipeItem {
    private IFood food;
    private float quantity;

    /**
     * 
     * @param food The food item to be added to the recipe.
     * @param quantity The quantity of the food item to be added to the recipe.
     */
    public RecipeItem(IFood food, float quantity ) {
        this.food = food;
        this.quantity = quantity;
    }

    /**
     * 
     * @return The food item to be added to the recipe.
     */
    public IFood getFood() {
        return food;
    }

    /**
     * 
     * @return The quantity of the food item to be added to the recipe.
     */
    public float getQuantity() {
        return quantity;
    }
}
