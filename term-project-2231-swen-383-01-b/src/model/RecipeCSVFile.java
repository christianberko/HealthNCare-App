package model;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

/** Concrete implementation of {@link IRecipeLog} for using a CSV file */
public class RecipeCSVFile implements IRecipeLog {
  // Basic food
  // b,Hot Dog,314.0,18.6,24.3,11.4,810
  // b,name,calories,fat,carb,protein,sodium

  // Recipe
  // r,Hot Dog-Bun-Mustard,Hot Dog,1.0,Hot Dog Bun,1.0,Mustard,1.5
  // r,name,f1name,f1count,f2name, f2count,...,fNname,fNcount

  private final String recipeFilePath = "foods.csv";

  /**
   * Reads recipes form the CSV file and parses them into IFood objects
   *
   * @returns {@code HashMap<String, IFood>} of IFood objects with the lowercase name as the key
   */
  public HashMap<String, IFood> readRecipes() {
    HashMap<String, IFood> foods = new HashMap<String, IFood>();
    try {
      CSVParser csvParser =
          CSVParser.parse(Files.newBufferedReader(Path.of(recipeFilePath)), CSVFormat.DEFAULT);
      for (CSVRecord line : csvParser) {
        IFood food;
        switch (line.get(0)) {
          case "b":
            food =
                new FoodItem(
                    line.get(1),
                    Float.parseFloat(line.get(2)),
                    Float.parseFloat(line.get(3)),
                    Float.parseFloat(line.get(4)),
                    Float.parseFloat(line.get(5)),
                    Float.parseFloat(line.get(6)));
            foods.put(line.get(1).toLowerCase(), food);
            break;
          case "r":
            ArrayList<RecipeItem> recipe = new ArrayList<RecipeItem>();
            String recipeName = line.get(1);
            for (int i = 2; i < line.size(); i = i + 2) {
              IFood itemNeeded = new Recipe();
              float quantity;
              String foodName = line.get(i);

              // example data contains empty attributes in some of the records
              // so this will check to make sure they are not empty before using them
              if (foodName.isEmpty()) {
                continue;
              }

              // retrive the IFood object with the same name
              // Note: in example data, food name case does not match
              // so we are always using lower case
              itemNeeded = foods.get(foodName.toLowerCase());
              quantity = Float.parseFloat(line.get(i + 1));

              RecipeItem ri = new RecipeItem(itemNeeded, quantity);
              recipe.add(ri);
            }
            food = new Recipe(recipeName, recipe);
            foods.put(food.getName().toLowerCase(), food);
            break;
          default:
            continue;
        }
      }
      csvParser.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return foods;
  }

  /**
   * Writes out the recipe to the CSV file. The file will be overwritten by the new recipes. The
   * recipes will be written to the file such that a recipe's dependencies will always come later in
   * the file and this includes any sub recipes. <i>Note: if the recipes contain circular
   * dependencies this method will run indefinitely</i>
   *
   * @param recipes in a hashmap with the name as the key
   */
  public void writeRecipes(HashMap<String, IFood> recipes) {
    try {
      FileWriter fw = new FileWriter(recipeFilePath);
      CSVPrinter csvPrinter = new CSVPrinter(fw, CSVFormat.DEFAULT);
      ArrayList<String> recipesToWrite =
          new ArrayList<>(); // keeps track of IFood objects that are recipes
      for (IFood food : recipes.values()) {

        // Print the basic food items first
        if (food.getClass() == FoodItem.class) {
          String[] foodRecord = {
            "b",
            food.getName(),
            String.valueOf(food.getCalories()),
            String.valueOf(food.getFat()),
            String.valueOf(food.getCarbohydrates()),
            String.valueOf(food.getProtein()),
            String.valueOf(food.getSodium())
          };
          csvPrinter.printRecord((Object[]) foodRecord);
        } else if (food.getClass() == Recipe.class) {
          // Add the recipe to the list of recipes to write
          // so they can be printed out later to ensure
          // there are no forward reference issues
          recipesToWrite.add(food.getName().toLowerCase());
        }
      }

      // Write out recipes
      while (!recipesToWrite.isEmpty()) {
        ArrayList<String> toRemove = new ArrayList<String>();
        for (String recipe : recipesToWrite) {
          // cast to Recipe since we know it will be a recipe object
          // and then can use recipe specific methods
          Recipe recipeObj = (Recipe) recipes.get(recipe);

          // If the recipe's dependencies are not found
          // in the list of recipes to still write
          boolean dependentNeeded = false;
          for (IFood dependent : recipeObj.getDependents()) {
            if (recipesToWrite.contains(dependent.getName().toLowerCase())) {
              // recipe has recipes that need to be written first
              dependentNeeded = true;
              break;
            }
          }

          // If all dependents are already included
          // then the recipe can now be written to the file
          if (!dependentNeeded) {
            ArrayList<String> record = new ArrayList<String>(); // holds record contents
            record.add("r");
            record.add(recipeObj.getName());
            for (RecipeItem item : recipeObj.getRecipeItems()) {
              // get name and quantity for each recipe ingredient
              record.add(item.getFood().getName());
              record.add(String.valueOf(item.getQuantity()));
            }
            csvPrinter.printRecord(record);
            toRemove.add(recipe); // mark this recipe to be removed
            // from the recipesToWrite collection
          }
        }
        recipesToWrite.removeAll(toRemove); // remove recipes that were written
      }

      csvPrinter.flush();
      csvPrinter.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
