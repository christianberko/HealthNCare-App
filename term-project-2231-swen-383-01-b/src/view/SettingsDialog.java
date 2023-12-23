package view;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;

import controller.ExerciseCRUDListener;
import controller.FoodCRUDListener;
import controller.ItemListResponder;
import model.Exercise;
import model.FoodItem;
import model.HealthNCare;
import model.Recipe;
import model.RecipeItem;
import model.IFood;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Vector;

public class SettingsDialog extends JDialog implements Observer {
    private HealthNCare healthNCare;
    private BasicFoodList basicFoodList;
    private RecipeList recipeList;
    private ExerciseList exerciseList;
    private GridBagConstraintDefaults constraintDefaults;
    private GridBagConstraints c;
    private ItemListResponder itemListResponder;
    private ExerciseCRUDListener exerciseCRUDListener;
    private FoodCRUDListener foodCRUDListener;

    public SettingsDialog(JFrame parent, HealthNCare healthNCare) {
        this.healthNCare = healthNCare;
        healthNCare.addSubscriber(this);
        itemListResponder = new ItemListResponder(healthNCare);
        DarkModeManager darkModeManager = new DarkModeManager();
        exerciseCRUDListener = new ExerciseCRUDListener(healthNCare, (UserInterface) parent);
        foodCRUDListener = new FoodCRUDListener(healthNCare, (UserInterface) parent);
        this.setTitle("HealthNCare Settings");
        this.setLocationRelativeTo(parent);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setMinimumSize(new Dimension(300, 500));
        // this.setAlwaysOnTop(true);
        this.setLayout(new GridBagLayout());
        constraintDefaults = new GridBagConstraintDefaults();

        JLabel title = new JLabel("<html><center><font size=6>Settings:</font><br><font size=4>Add new Foods, Recipes, and Exercises to use in your journey to a healthier life</font></center></html>", SwingConstants.CENTER);
        c = constraintDefaults.getDefaults();
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        this.add(title, c);

        JButton themeButton = new JButton("Toggle Dark Mode");
        themeButton.addActionListener(e -> darkModeManager.toggleDarkMode(parent));
        themeButton.setPreferredSize(new Dimension(45, 15));

        c.anchor = GridBagConstraints.PAGE_START;
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 1;
        this.add(themeButton, c);

        JSeparator food = new JSeparator();
        c = constraintDefaults.getDefaults();
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 2;
        this.add(food, c);

        JLabel basicFoods = new JLabel("<html><font size=5>Basic Foods</font></html>", SwingConstants.CENTER);
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 3;
        this.add(basicFoods, c);

        JButton addBasicFood = new JButton("Add");
        addBasicFood.addActionListener(x -> addFood());
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 3;
        this.add(addBasicFood, c);

        createBasicFoodTree();

        JSeparator recipe = new JSeparator();
        c = constraintDefaults.getDefaults();
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 5;
        this.add(recipe, c);

        JLabel recipes = new JLabel("<html><font size=5>Recipes</font></html>", SwingConstants.CENTER);
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 6;
        this.add(recipes, c);

        JButton addRecipe = new JButton("Add");
        addRecipe.addActionListener(x -> addRecipe());
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 6;
        this.add(addRecipe, c);

        createRecipeTree();

        JSeparator exercise = new JSeparator();
        c = constraintDefaults.getDefaults();
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 8;
        this.add(exercise, c);

        JLabel exercises = new JLabel("<html><font size=5>Exercises</font></html>", SwingConstants.CENTER);
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 9;
        this.add(exercises, c);

        JButton addExercise = new JButton("Add");
        addExercise.addActionListener(x -> addExercise());
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 9;
        this.add(addExercise, c);

        createExerciseTree();

        // JLabel exerciseSettings = new JLabel("Calories per hour per 100lbs",
        // SwingConstants.RIGHT);
        // c = constraintDefaults.getDefaults();
        // c.gridx = 0;
        // c.gridy = 5;
        // this.add(exerciseSettings, c);

        // JTextField exerciseTextField = new JTextField();
        // c = constraintDefaults.getDefaults();
        // c.gridx = 1;
        // c.gridy = 5;
        // this.add(exerciseTextField, c);

        JSeparator save = new JSeparator();
        c = constraintDefaults.getDefaults();
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 10;
        this.add(save, c);

        JButton saveChangesButton = new JButton("Save Changes");
        saveChangesButton.addActionListener(e -> saveData());
        saveChangesButton.addActionListener(e -> actionPerformed());
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 11;
        this.add(saveChangesButton, c);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> actionPerformed());
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 11;
        this.add(cancelButton, c);

        this.pack();
    }

    private void createExerciseTree() {
        if (exerciseList != null) {
            this.remove(exerciseList);
        }

        exerciseList = new ExerciseList(itemListResponder, healthNCare);
        exerciseList.getTree().addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                createEditExerciseDialog(e.getPath().getLastPathComponent().toString());
            }
        });
        c = constraintDefaults.getDefaults();
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 10;
        this.add(exerciseList, c);
    }

    private void createRecipeTree() {
        if (recipeList != null) {
            this.remove(recipeList);
        }

        recipeList = new RecipeList(healthNCare);
        recipeList.getTree().addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                createEditRecipeDialog(e.getPath().getLastPathComponent().toString());
            }
        });
        c = constraintDefaults.getDefaults();
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 7;
        this.add(recipeList, c);
    }

    private void createBasicFoodTree() {
        if (basicFoodList != null) {
            this.remove(basicFoodList);
        }

        basicFoodList = new BasicFoodList(healthNCare);
        basicFoodList.listFoods.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                createEditFoodDialog(e.getPath().getLastPathComponent().toString());
            }
        });

        c = constraintDefaults.getDefaults();
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 4;
        this.add(basicFoodList, c);
    }

    private void actionPerformed() {
        this.dispose();
    }

    private void saveData() {
        // addCalorieLimit = new AddCalorieLimitListener(null);
        // addWeightEntry = new AddWeightEntry(null);
        // addWeightEntry.onAction(LocalDate.parse(enteredDate),
        // Float.parseFloat(weight));
        // addCalorieLimit.onAction(LocalDate.parse(enteredDate),
        // Float.parseFloat(calorie));
    }

    private void addFood() {
        JDialog dialog = createCreationalDialog();
        dialog.setTitle("Add a new food");

        // Food name
        JLabel foodName = new JLabel("What is the name of the food ", SwingConstants.RIGHT);
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 0;
        dialog.add(foodName, c);

        JTextField foodNameInput = new JTextField();
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 0;
        dialog.add(foodNameInput, c);

        // Calories
        JLabel calories = new JLabel("Number of calories", SwingConstants.RIGHT);
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 1;
        dialog.add(calories, c);

        JTextField caloriesInput = new JTextField();
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 1;
        dialog.add(caloriesInput, c);

        // Fat
        JLabel fat = new JLabel("Grams of fat", SwingConstants.RIGHT);
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 2;
        dialog.add(fat, c);

        JTextField fatInput = new JTextField();
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 2;
        dialog.add(fatInput, c);

        // Carbs
        JLabel carbs = new JLabel("Grams of carbs", SwingConstants.RIGHT);
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 3;
        dialog.add(carbs, c);

        JTextField carbsInput = new JTextField();
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 3;
        dialog.add(carbsInput, c);

        // Protein
        JLabel protein = new JLabel("Grams of protein", SwingConstants.RIGHT);
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 4;
        dialog.add(protein, c);

        JTextField proteinInput = new JTextField();
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 4;
        dialog.add(proteinInput, c);

        // Sodium
        JLabel sodium = new JLabel("Milligrams of sodium", SwingConstants.RIGHT);
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 5;
        dialog.add(sodium, c);

        JTextField sodiumInput = new JTextField();
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 5;
        dialog.add(sodiumInput, c);

        // Create button
        JButton createButton = new JButton("Create");
        createButton.addActionListener(x -> {
            String[] fields = {
                    foodNameInput.getText(),
                    caloriesInput.getText(),
                    fatInput.getText(),
                    carbsInput.getText(),
                    proteinInput.getText(),
                    sodiumInput.getText()
            };
            foodCRUDListener.addFoodItem(fields);
            dialog.dispose();
        });
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 6;
        dialog.add(createButton, c);

        dialog.setMinimumSize(new Dimension(500, 250));
        dialog.setVisible(true);
    }

    private void addRecipe() {
        JDialog dialog = new JDialog();
        dialog.setLocationRelativeTo(this);

        JPanel topInputs = new JPanel();
        topInputs.setLayout(new GridBagLayout());

        JLabel nameLabel = new JLabel();
        nameLabel.setText("Name:");
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        topInputs.add(nameLabel, c);

        JTextField nameInput = new JTextField("name of recipe");
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.CENTER;
        topInputs.add(nameInput, c);

        dialog.add(topInputs, BorderLayout.NORTH);

        // Table
        String[][] tableData = new String[][] { new String[] { "", "" } };
        String[] columns = { "Name", "Quantity" };
        DefaultTableModel tableModel = new DefaultTableModel(tableData, columns);
        JTable table = new JTable(tableModel);

        ArrayList<String> foods = itemListResponder.getFoods();
        JComboBox<String> comboBox = new JComboBox<String>(foods.toArray(new String[foods.size()]));
        table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(comboBox));

        dialog.add(table, BorderLayout.CENTER);

        // Bottom buttons
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridBagLayout());

        JButton addRowBtn = new JButton("Add new item");
        addRowBtn.addActionListener(x -> {
            tableModel.addRow(new String[] { "", "" });
            dialog.repaint();
        });
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 0;
        bottomPanel.add(addRowBtn, c);

        JButton createBtn = new JButton("Create Recipe");
        createBtn.addActionListener(x -> {
            // save
            ArrayList<String[]> newRecipeItems = new ArrayList<String[]>();
            for (Vector<String> row : tableModel.getDataVector()) {
                if (!row.elementAt(0).isEmpty() && !row.elementAt(1).isEmpty()) {
                    newRecipeItems.add(new String[] { row.elementAt(0), row.elementAt(1) });
                }
            }

            foodCRUDListener.addRecipe(nameInput.getText(),
                    newRecipeItems.toArray(new String[newRecipeItems.size()][]));
            dialog.dispose();
        });
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 0;
        bottomPanel.add(createBtn, c);

        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setMinimumSize(new Dimension(350, 200));
        dialog.setVisible(true);
    }

    private void addExercise() {
        JDialog dialog = createCreationalDialog();
        dialog.setTitle("Add a new exercise");

        // Food name
        JLabel exerciseName = new JLabel("What is the name of the new exercise", SwingConstants.RIGHT);
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 0;
        dialog.add(exerciseName, c);

        JTextField exerciseNameInput = new JTextField();
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 0;
        dialog.add(exerciseNameInput, c);

        // Calories
        JLabel calories = new JLabel("Number of calories per hour for a 100lb person", SwingConstants.RIGHT);
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 1;
        dialog.add(calories, c);

        JTextField caloriesInput = new JTextField();
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 1;
        dialog.add(caloriesInput, c);

        // Create button
        JButton createButton = new JButton("Create");
        createButton.addActionListener(x -> {
            exerciseCRUDListener.create(exerciseNameInput.getText(), Float.parseFloat(caloriesInput.getText()));
            dialog.dispose();
        });
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 6;
        dialog.add(createButton, c);

        dialog.setMinimumSize(new Dimension(500, 200));
        dialog.setVisible(true);
    }

    private JDialog createCreationalDialog() {
        JDialog dialog = new JDialog();
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setModal(true);
        dialog.setLayout(new GridBagLayout());
        return dialog;
    }

    private void createEditFoodDialog(String foodName) {

        // TODO - use a controller
        FoodItem food = (FoodItem) healthNCare.getFood(foodName);

        JDialog dialog = new JDialog();
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setModal(true);
        dialog.setLayout(new GridBagLayout());

        JLabel nameLabel = new JLabel("Name:", SwingConstants.LEFT);
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 0;
        dialog.add(nameLabel, c);

        JLabel name = new JLabel(food.getName(), SwingConstants.RIGHT);
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 0;
        dialog.add(name, c);

        JLabel caloriesLabel = new JLabel("Calories:", SwingConstants.LEFT);
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 1;
        dialog.add(caloriesLabel, c);

        JTextField caloriesInput = new JTextField(String.valueOf(food.getCalories()),
                SwingConstants.RIGHT);
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 1;
        dialog.add(caloriesInput, c);

        JLabel fatLabel = new JLabel("Fat (g):", SwingConstants.LEFT);
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 2;
        dialog.add(fatLabel, c);

        JTextField fatInput = new JTextField(String.valueOf(food.getFat()),
                SwingConstants.RIGHT);
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 2;
        dialog.add(fatInput, c);

        JLabel carbsLabel = new JLabel("Carbs (g):", SwingConstants.LEFT);
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 3;
        dialog.add(carbsLabel, c);

        JTextField carbsInput = new JTextField(String.valueOf(food.getCarbohydrates()), SwingConstants.RIGHT);
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 3;
        dialog.add(carbsInput, c);

        JLabel proteinLabel = new JLabel("Protein (g):", SwingConstants.LEFT);
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 4;
        dialog.add(proteinLabel, c);

        JTextField proteinInput = new JTextField(String.valueOf(food.getProtein()),
                SwingConstants.RIGHT);
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 4;
        dialog.add(proteinInput, c);

        JLabel sodiumLabel = new JLabel("Sodium (mg):", SwingConstants.LEFT);
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 5;
        dialog.add(sodiumLabel, c);

        JTextField sodiumInput = new JTextField(String.valueOf(food.getSodium()),
                SwingConstants.RIGHT);
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 5;
        dialog.add(sodiumInput, c);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(x -> {
            // name, calories, fat, carbs, protein, sodium
            String[] foodItem = {
                    caloriesInput.getText(),
                    fatInput.getText(),
                    carbsInput.getText(),
                    proteinInput.getText(),
                    sodiumInput.getText()
            };
            foodCRUDListener.updateFood(name.getText(), foodItem);
            dialog.dispose();
        });
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 6;
        dialog.add(saveButton, c);

        JButton deleteButton = new JButton("Delete Food");
        deleteButton.addActionListener(x -> {
            foodCRUDListener.deleteFood(foodName);
            dialog.dispose();
        });
        deleteButton.setBackground(new Color(190, 0, 0));
        deleteButton.setForeground(new Color(230, 230, 230));
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 6;
        dialog.add(deleteButton, c);

        dialog.setMinimumSize(new Dimension(300, 250));
        dialog.setVisible(true);
    }

    private void createEditExerciseDialog(String exerciseName) {
        JDialog dialog = new JDialog();
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setModal(true);
        dialog.setLayout(new GridBagLayout());

        // TODO - use a controller
        Exercise exercise = healthNCare.getExercise(exerciseName);

        JLabel nameLabel = new JLabel("Name:", SwingConstants.LEFT);
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 0;
        dialog.add(nameLabel, c);

        JLabel name = new JLabel(exerciseName, SwingConstants.RIGHT);
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 0;
        dialog.add(name, c);

        JLabel caloriesLabel = new JLabel("Calories per hour for 100lb person:", SwingConstants.LEFT);
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 1;
        dialog.add(caloriesLabel, c);

        JTextField caloriesInput = new JTextField(String.valueOf(exercise.getCaloriePerHour()),
                SwingConstants.RIGHT);
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 1;
        dialog.add(caloriesInput, c);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(x -> {
            exerciseCRUDListener.update(name.getText(), Float.parseFloat(caloriesInput.getText()));
            dialog.dispose();
        });
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 6;
        dialog.add(saveButton, c);

        JButton deleteButton = new JButton("Delete Exercise");
        deleteButton.addActionListener(x -> {
            exerciseCRUDListener.delete(exerciseName);
            dialog.dispose();
        });
        deleteButton.setBackground(new Color(190, 0, 0));
        deleteButton.setForeground(new Color(230, 230, 230));
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 6;
        dialog.add(deleteButton, c);

        dialog.setMinimumSize(new Dimension(350, 200));
        dialog.setVisible(true);
    }

    private void createEditRecipeDialog(String recipeName) {
        JDialog dialog = new JDialog();
        dialog.setLocationRelativeTo(this);

        JPanel topInputs = new JPanel();
        topInputs.setLayout(new GridBagLayout());
        // ((GridBagLayout) panel.getLayout()).rowHeights = new int[] { 0, 0, 0 };
        // ((GridBagLayout) panel.getLayout()).rowWeights = new double[] { 0.0, 0.0,
        // 1.0E-4 };

        JLabel nameLabel = new JLabel();
        nameLabel.setText("Name:");
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        topInputs.add(nameLabel, c);

        JTextField nameInput = new JTextField(recipeName);
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.CENTER;
        topInputs.add(nameInput, c);

        dialog.add(topInputs, BorderLayout.NORTH);

        // Table
        // TODO: move to controller
        Recipe recipe = (Recipe) healthNCare.getFood(recipeName);
        ArrayList<RecipeItem> recipeItems = recipe.getRecipeItems();
        String[][] tableData = new String[recipeItems.size()][2];
        for (int i = 0; i < recipeItems.size(); i++) {
            RecipeItem item = recipeItems.get(i);
            tableData[i] = new String[] { item.getFood().getName(), String.valueOf(item.getQuantity()) };
        }
        String[] columns = { "Name", "Quantity" };
        DefaultTableModel tableModel = new DefaultTableModel(tableData, columns);
        JTable table = new JTable(tableModel);

        ArrayList<String> foods = itemListResponder.getFoods();
        JComboBox<String> comboBox = new JComboBox<String>(foods.toArray(new String[foods.size()]));
        table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(comboBox));

        dialog.add(table, BorderLayout.CENTER);

        // Bottom buttons
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridBagLayout());

        JButton addRowBtn = new JButton("Add new item");
        addRowBtn.addActionListener(x -> {
            tableModel.addRow(new String[] { "", "" });
            dialog.repaint();
        });
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 0;
        bottomPanel.add(addRowBtn, c);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(x -> {
            // save
            ArrayList<RecipeItem> newRecipeItems = new ArrayList<RecipeItem>();
            for (Vector<String> row : tableModel.getDataVector()) {
                if (row.elementAt(0) != "") {
                    IFood food = healthNCare.getFood(row.elementAt(0)); // TODO: extract to controller
                    RecipeItem ri = new RecipeItem(food, Float.parseFloat(row.elementAt(1)));
                    newRecipeItems.add(ri);
                }
            }
            recipe.setRecipeItems(newRecipeItems);
            dialog.dispose();
        });
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 0;
        bottomPanel.add(saveBtn, c);

        JButton deleteButton = new JButton("Delete Food");
        deleteButton.addActionListener(x -> {
            foodCRUDListener.deleteFood(recipeName);
            dialog.dispose();
        });
        deleteButton.setBackground(new Color(190, 0, 0));
        deleteButton.setForeground(new Color(230, 230, 230));
        c = constraintDefaults.getDefaults();
        c.gridx = 2;
        c.gridy = 0;
        bottomPanel.add(deleteButton, c);

        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setMinimumSize(new Dimension(350, 200));
        dialog.setVisible(true);
    }

    public void update(String messageType, String message) {
        switch (messageType) {
            case "updateFood":
                createBasicFoodTree();
                createRecipeTree();
                this.revalidate();
                this.repaint();
                break;
            case "updateExercise":
                createExerciseTree();
                this.revalidate();
                this.repaint();
                break;
        }

    }
}
