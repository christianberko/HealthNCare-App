package controller;

import java.util.ArrayList;
import java.util.HashMap;

import model.Exercise;
import model.FoodItem;
import model.HealthNCare;
import model.IFood;
import model.Recipe;

/*
 * This class is responsible for responding to requests for the list of foods.
 */
public class ItemListResponder {
    private HealthNCare healthNCare;

    /**
     * @param healthNCare The main model class of the application.
     */
    public ItemListResponder(HealthNCare health) {
        this.healthNCare = health;
    }

    /**
     * @return the list of foods recorded in foods.csv
     */
    public ArrayList<String> getFoods() {
        return healthNCare.getFoods();
    }

    public HashMap<String, FoodItem> getFoodsMap() {
        return healthNCare.getFoodsMap();
    }

    public HashMap<String, Recipe> getRecipesMap() {
        return healthNCare.getRecipesMap();
    }

    public HashMap<String, Exercise> getExerciseMap() {
        return healthNCare.getExerciseMap();
    }
}
