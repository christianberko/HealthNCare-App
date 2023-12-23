package view;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import java.awt.event.MouseEvent;

import controller.ExerciseCRUDListener;
import controller.FoodCRUDListener;
import controller.ItemListResponder;
import controller.LogEntryCRUDListener;
import controller.RemoveEntryListener;
import model.ConsumptionEntry;
import model.Exercise;
import model.ExerciseEntry;
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
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;
import java.time.LocalDate;

public class DailyLogTrees extends JPanel implements Observer {
    private HealthNCare healthNCare;
    private UserInterface ui;
    private Recipe recipe;
    // private ExerciseList exerciseList;
    private GridBagConstraintDefaults constraintDefaults;
    private GridBagConstraints c;
    private ItemListResponder itemListResponder;
    private RemoveEntryListener removeEntryListener;
    private ExerciseCRUDListener exerciseCRUDListener;
    private LogEntryCRUDListener logEntryCRUDListener;
    private FoodCRUDListener foodCRUDListener;
    private LocalDate date;
    private JTree listFoods;
    private JTree listRecipes;
    private JTree listExercises;

    private JComboBox<String> addBasicFood;
    private JComboBox<String> addRecipe;
    private JComboBox<String> addExercise;

    public DailyLogTrees(LocalDate date, SwingUI gui, HealthNCare healthNCare) {
        this.healthNCare = healthNCare;
        this.date = date;
        this.ui = gui;
        healthNCare.addSubscriber(this);
        gui.addSubscriber(this);
        itemListResponder = new ItemListResponder(healthNCare);
        exerciseCRUDListener = new ExerciseCRUDListener(healthNCare, gui);
        removeEntryListener = new RemoveEntryListener(healthNCare, gui);
        logEntryCRUDListener = new LogEntryCRUDListener(healthNCare, gui);
        foodCRUDListener = new FoodCRUDListener(healthNCare, gui);
        recipe = new Recipe();
        // this.setMinimumSize(new Dimension(300, 500));
        // this.setAlwaysOnTop(true);
        this.setLayout(new GridBagLayout());
        constraintDefaults = new GridBagConstraintDefaults();

        JLabel dailyLog = new JLabel(
                "<html><font size=6>Daily Log: Track Your Food and Exercise Journey</strong></html>",
                SwingConstants.CENTER);
        c = constraintDefaults.getDefaults();
        c.anchor = GridBagConstraints.CENTER;
        c.gridwidth = 6;
        c.gridx = 0;
        c.gridy = 0;
        this.add(dailyLog, c);

        JLabel basicFoods = new JLabel("<html><font size=4>Basic Foods</font></html>", SwingConstants.CENTER);
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 1;
        this.add(basicFoods, c);

        addBasicFood = new JComboBox<String>();
        addBasicFood.addItem("");
        addBasicFood.addItem("---- Add New ----");
        addBasicFood.setSelectedIndex(0);
        for (FoodItem f : itemListResponder.getFoodsMap().values()) {
            addBasicFood.addItem(f.getName());
        }
        addBasicFood.addActionListener(x -> {
            if (addBasicFood.getSelectedItem() != null ? !addBasicFood.getSelectedItem().equals("") : false) {
                if (addBasicFood.getSelectedItem().equals("---- Add New ----")) {
                    addFood();
                }
                else {
                    createLogFoodDialog(String.valueOf(addBasicFood.getSelectedItem()), null, null);
                }
                addBasicFood.setSelectedIndex(0);
            }
        });
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 1;
        this.add(addBasicFood, c);

        createBasicFoodTree();

        JLabel recipes = new JLabel("<html><font size=4>Recipes</font></html>", SwingConstants.CENTER);
        c = constraintDefaults.getDefaults();
        c.gridx = 2;
        c.gridy = 1;
        this.add(recipes, c);

        addRecipe = new JComboBox<String>();
        addRecipe.addItem("");
        addRecipe.addItem("---- Add New ----");
        addRecipe.setSelectedIndex(0);
        for (Recipe r : itemListResponder.getRecipesMap().values()) {
            addRecipe.addItem(r.getName());
        }
        addRecipe.addActionListener(x -> {
            if (addRecipe.getSelectedItem() != null ? !addRecipe.getSelectedItem().equals("") : false) {
                if (addRecipe.getSelectedItem().equals("---- Add New ----")) {
                    addRecipe();
                } else {
                    createLogFoodDialog(String.valueOf(addRecipe.getSelectedItem()), null, null);
                }
                addRecipe.setSelectedIndex(0);
            }
        });
        c = constraintDefaults.getDefaults();
        c.gridx = 3;
        c.gridy = 1;
        this.add(addRecipe, c);

        createRecipeTree();

        JLabel exercises = new JLabel("<html><font size=4>Exercises</font></html>", SwingConstants.CENTER);
        c = constraintDefaults.getDefaults();
        c.gridx = 4;
        c.gridy = 1;
        this.add(exercises, c);

        addExercise = new JComboBox<String>();
        addExercise.addItem("");
        addExercise.addItem("---- Add New ----");
        addExercise.setSelectedIndex(0);
        for (Exercise e : itemListResponder.getExerciseMap().values()) {
            addExercise.addItem(e.getName());
        }
        addExercise.addActionListener(x -> {
            if (addExercise.getSelectedItem() != null ? !addExercise.getSelectedItem().equals("") : false) {
                if (addExercise.getSelectedItem().equals("---- Add New ----")) {
                    addExercise();
                } else {
                    String value = String.valueOf(addExercise.getSelectedItem());
                    createLogExerciseDialog(value, null, null);
                }
                addExercise.setSelectedIndex(0);
            }
        });
        c = constraintDefaults.getDefaults();
        c.gridx = 5;
        c.gridy = 1;
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

        // Rework to use main window buttons
        /*
         * JButton saveChangesButton = new JButton("Save Changes");
         * saveChangesButton.addActionListener(e -> saveData());
         * saveChangesButton.addActionListener(e -> actionPerformed());
         * c = constraintDefaults.getDefaults();
         * c.gridx = 0;
         * c.gridy = 6;
         * this.add(saveChangesButton, c);
         * 
         * JButton cancelButton = new JButton("Cancel");
         * cancelButton.addActionListener(e -> actionPerformed());
         * c = constraintDefaults.getDefaults();
         * c.gridx = 1;
         * c.gridy = 6;
         * this.add(cancelButton, c);
         */
    }

    private void createExerciseTree() {
        //if (exerciseList != null) {
            //this.remove(exerciseList);
       // }

        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        for (ExerciseEntry e : logEntryCRUDListener.getExercisesLogged(date)) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(e);
            root.add(node);
        }

        listExercises = new JTree(new DefaultTreeModel(root));
        listExercises.setRootVisible(false);
        listExercises.setVisibleRowCount(5);
        JScrollPane exercises = new JScrollPane(listExercises);
        exercises.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        listExercises.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) listExercises.getLastSelectedPathComponent();
                if (node == null) {
                    return;
                }
                ExerciseEntry ent = (ExerciseEntry) node.getUserObject();
                createLogExerciseDialog(ent.getExerciseName(), "update", ent);
            }
        });
        
        // Code to clear the selection after closing the dialog
        listExercises.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                JTree tree = (JTree) e.getSource();
                int row = tree.getRowForLocation(e.getX(), e.getY());
        
                // Left or Right click
                if (row != -1 && e.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(e) || SwingUtilities.isRightMouseButton(e)) {
                    // Clear the selection
                    tree.clearSelection();
                }
            }
        });
        c = constraintDefaults.getDefaults();
        c.gridwidth = 2;
        c.ipady = 150;
        c.gridx = 4;
        c.gridy = 2;
        this.add(exercises, c);
    }

    private void createRecipeTree() {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        for (ConsumptionEntry r : logEntryCRUDListener.getRecipesLogged(date)) {
            if (foodCRUDListener.retrieveFood(r.getFoodConsumed()).getClass() == recipe.getClass()) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(r);
                root.add(node);
            }
        }
        listRecipes = new JTree(new DefaultTreeModel(root));

        listRecipes.setVisibleRowCount(5);
        listRecipes.setRootVisible(false);
        JScrollPane recipes = new JScrollPane(listRecipes);
        recipes.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        listRecipes.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) listRecipes.getLastSelectedPathComponent();
                if (node == null) {
                    return;
                }
                ConsumptionEntry ent = (ConsumptionEntry) node.getUserObject();
                createLogFoodDialog(ent.getFoodConsumed(), "update", ent);
            }
        });
        listRecipes.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                JTree tree = (JTree) e.getSource();
                int row = tree.getRowForLocation(e.getX(), e.getY());
        
                // Left or Right click
                if (row != -1 && e.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(e) || SwingUtilities.isRightMouseButton(e)) {
                    // Clear the selection
                    tree.clearSelection();
                }
            }
        });
        c = constraintDefaults.getDefaults();
        c.gridwidth = 2;
        c.ipady = 150;
        c.gridx = 2;
        c.gridy = 2;
        this.add(recipes, c);
    }

    private void createBasicFoodTree() {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        itemListResponder.getFoodsMap();
        for (ConsumptionEntry f : logEntryCRUDListener.getBasicFoodsLogged(date)) {
            if (foodCRUDListener.retrieveFood(f.getFoodConsumed()).getClass() != recipe.getClass()) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(f);
                root.add(node);
            }
        }

        listFoods = new JTree(new DefaultTreeModel(root));
        listFoods.setRootVisible(false);
        listFoods.setVisibleRowCount(5);
        // listFoods.setBackground(new Color(230, 0, 230)); // pink
        JScrollPane foods = new JScrollPane(listFoods);
        foods.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        listFoods.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) listFoods.getLastSelectedPathComponent();
                if (node == null) {
                    return;
                }
                ConsumptionEntry ent = (ConsumptionEntry) node.getUserObject();
                createLogFoodDialog(ent.getFoodConsumed(), "update", ent);
            }
        });
        listFoods.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                JTree tree = (JTree) e.getSource();
                int row = tree.getRowForLocation(e.getX(), e.getY());
        
                // Left or Right click
                if (row != -1 && e.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(e) || SwingUtilities.isRightMouseButton(e)) {
                    // Clear the selection
                    tree.clearSelection();
                }
            }
        });
        c = constraintDefaults.getDefaults();
        c.gridwidth = 2;
        c.ipady = 150;
        c.gridx = 0;
        c.gridy = 2;
        // set background color red
        this.add(foods, c);
    }

    private void actionPerformed() {
        // this.dispose();
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
        // set background color green
        dialog.add(saveButton, c);

        JButton deleteButton = new JButton("Remove Entry");
        deleteButton.addActionListener(x -> {
            // Assuming removeEntryListener removes the entry correctly
            removeEntryListener.onFoodAction(date, new String[]{foodName, "1"});

            // Update the JTree model after removing the entry
            DefaultTreeModel treeModel = (DefaultTreeModel) listFoods.getModel();
            DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();

            // Find and remove the selected node
            DefaultMutableTreeNode selectedNode = findNodeByName(root, foodName);
            if (selectedNode != null) {
                treeModel.removeNodeFromParent(selectedNode);
            }

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

    // Helper method to find a node by name, used in createEditFoodDialog to delete
    private DefaultMutableTreeNode findNodeByName(DefaultMutableTreeNode root, String name) {
    Enumeration<?> e = root.depthFirstEnumeration();

    while (e.hasMoreElements()) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
        if (node.getUserObject().equals(name)) {
            return node;
        }
    }

    return null;
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

        // JButton deleteButton = new JButton("Delete Exercise");
        // deleteButton.addActionListener(x -> {
        //     ui.showErrorMessage("This feature is not implemented");
        //     // logEntryCRUDListener.deleteExerciseLogEntry(date, exerciseName,
        //     // Float.parseFloat());
        //     dialog.dispose();
        // });
        
        JButton deleteButton = new JButton("Remove Entry");
        deleteButton.addActionListener(x -> {
            // Assuming removeEntryListener removes the entry correctly
            removeEntryListener.onExerciseAction(date, new String[]{exerciseName, "1"});
            
            // Update the JTree model after removing the entry
            DefaultTreeModel treeModel = (DefaultTreeModel) listFoods.getModel();
            DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
            
            // Find and remove the selected node
            DefaultMutableTreeNode selectedNode = findNodeByName(root, exerciseName);
            if (selectedNode != null) {
                treeModel.removeNodeFromParent(selectedNode);
            }

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
            ArrayList<String[]> newRecipeItems = new ArrayList<String[]>();
            for (Vector<String> row : tableModel.getDataVector()) {
                if (row.elementAt(0) != "") {
                    newRecipeItems.add(new String[] {
                            row.elementAt(0),
                            row.elementAt(1)
                    });
                }
            }
            foodCRUDListener.updateRecipe(recipeName, newRecipeItems);
            dialog.dispose();
        });
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 0;
        bottomPanel.add(saveBtn, c);

        JButton deleteButton = new JButton("Remove Entry"); // TODO: Fix, it's not removing the entry
        deleteButton.addActionListener(x -> {
            // Assuming removeEntryListener removes the entry correctly
            removeEntryListener.onFoodAction(date, new String[] { recipeName, "1" });
            
            // Update the JTree model after removing the entry
            DefaultTreeModel treeModel = (DefaultTreeModel) listFoods.getModel();
            DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
            
            // Find and remove the selected node
            DefaultMutableTreeNode selectedNode = findNodeByName(root, recipeName);
            if (selectedNode != null) {
                treeModel.removeNodeFromParent(selectedNode);
            }

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

    @Override
    public void update(String messageType, String message) {
        switch (messageType) {
            case "updateFood":
                updateTree("food log");
                updateTree("recipe log");
                updateTree("exercise log");

                addBasicFood.removeAllItems();
                addBasicFood.addItem("");
                addBasicFood.addItem("---- Add New ----");
                for (FoodItem f : itemListResponder.getFoodsMap().values()) {
                    addBasicFood.addItem(f.getName());
                }
                addBasicFood.setSelectedIndex(0);
                // updateUI();
                addRecipe.removeAllItems();
                addRecipe.addItem("");
                addRecipe.addItem("---- Add New ----");
                for (Recipe r : itemListResponder.getRecipesMap().values()) {
                    addRecipe.addItem(r.getName());
                }
                addRecipe.setSelectedIndex(0);
                updateUI();
                break;
            case "updateExercise":
                updateTree("food log");
                updateTree("recipe log");
                updateTree("exercise log");

                addExercise.removeAllItems();
                addExercise.addItem("");
                addExercise.addItem("---- Add New ----");
                for (Exercise e : itemListResponder.getExerciseMap().values()) {
                    addExercise.addItem(e.getName());
                }
                updateUI();
                break;
            case "updateLog":
                updateTree("food log");
                updateTree("recipe log");
                updateTree("exercise log");
                updateUI();
                break;
            case "updateDate":
                date = LocalDate.parse(message);
                updateTree("food log");
                updateTree("recipe log");
                updateTree("exercise log");
                updateUI();
                break;
        }

    }

    private void updateTree(String tree) {
        DefaultMutableTreeNode root;
        switch (tree) {
            case "food log":
                root = new DefaultMutableTreeNode("food log");
                for (ConsumptionEntry fi : logEntryCRUDListener.getBasicFoodsLogged(date)) {
                    if (foodCRUDListener.retrieveFood(fi.getFoodConsumed()).getClass() != recipe.getClass()) {
                        DefaultMutableTreeNode node = new DefaultMutableTreeNode(fi);
                        root.add(node);
                    }
                }
                listFoods.setModel(new DefaultTreeModel(root));
                break;
            case "recipe log":
                root = new DefaultMutableTreeNode("recipe log");
                for (ConsumptionEntry rec : logEntryCRUDListener.getRecipesLogged(date)) {
                    if (foodCRUDListener.retrieveFood(rec.getFoodConsumed()).getClass() == recipe.getClass()) {
                        DefaultMutableTreeNode node = new DefaultMutableTreeNode(rec);
                        root.add(node);
                    }
                }
                listRecipes.setModel(new DefaultTreeModel(root));
                break;

            case "exercise log":
                root = new DefaultMutableTreeNode("exercise log");
                for (ExerciseEntry e : logEntryCRUDListener.getExercisesLogged(date)) {
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(e);
                    root.add(node);
                }
                listExercises.setModel(new DefaultTreeModel(root));
                break;
            default:
                break;
        }
    }

    private void createLogFoodDialog(String foodName, String status, ConsumptionEntry ent) {
        JDialog dialog = new JDialog();
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setModal(true);
        dialog.setLayout(new GridBagLayout());

        JLabel nameLbl = new JLabel("Name");
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 0;
        dialog.add(nameLbl, c);

        JLabel foodNameLbl = new JLabel(foodName);
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 0;
        dialog.add(foodNameLbl, c);

        JLabel quantityLabel = new JLabel("Number of servings consumed");
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 1;
        dialog.add(quantityLabel, c);

        JTextField quantityInput = new JTextField("1.0");
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 1;
        dialog.add(quantityInput, c);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(x -> {
            dialog.dispose();
        });
        c = constraintDefaults.getDefaults();
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 3;
        dialog.add(cancelButton, c);

        if (status == "update") {
            JButton logButton = new JButton("Log Food");
            logButton.addActionListener(x -> {
                String[] entry = new String[] { foodName, quantityInput.getText() };
                logEntryCRUDListener.addConsumptionEntry(date, entry);
                deleteFoodLog(ent);
                dialog.dispose();
            });
            c = constraintDefaults.getDefaults();
            c.ipadx = 100;
            c.gridx = 1;
            c.gridy = 2;
            dialog.add(logButton, c);

            JButton deleteButton = new JButton("Delete Food Entry");
            deleteButton.addActionListener(x -> {
                deleteFoodLog(ent);
                dialog.dispose();
            });
            deleteButton.setBackground(new Color(107, 1, 1));
            deleteButton.setForeground(new Color(230, 230, 230));

            c = constraintDefaults.getDefaults();
            c.ipadx = 100;
            c.gridx = 0;
            c.gridy = 2;
            dialog.add(deleteButton, c);
        } else {
            JButton logButton = new JButton("Log Food");
            logButton.addActionListener(x -> {
                String[] entry = new String[] { foodName, quantityInput.getText() };
                logEntryCRUDListener.addConsumptionEntry(date, entry);
                deleteFoodLog(ent);
                dialog.dispose();
            });
            c = constraintDefaults.getDefaults();
            c.ipadx = 100;
            c.gridwidth = 2;
            c.gridx = 0;
            c.gridy = 2;
            dialog.add(logButton, c);
        }

        dialog.setMinimumSize(new Dimension(350, 200));
        dialog.setVisible(true);
        updateUI();
    }

    public void createLogExerciseDialog(String exerciseName, String status, ExerciseEntry ent) {
        JDialog dialog = new JDialog();
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setModal(true);
        dialog.setLayout(new GridBagLayout());

        JLabel nameLbl = new JLabel("Name");
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 0;
        dialog.add(nameLbl, c);

        JLabel foodNameLbl = new JLabel(exerciseName);
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 0;
        dialog.add(foodNameLbl, c);

        JLabel quantityLabel = new JLabel("Minutes of exercise");
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 1;
        dialog.add(quantityLabel, c);

        JTextField quantityInput = new JTextField("30.0");
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 1;
        dialog.add(quantityInput, c);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(x -> {
            dialog.dispose();
        });
        c = constraintDefaults.getDefaults();
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 3;
        dialog.add(cancelButton, c);

        if (status == "update") {
            JButton logButton = new JButton("Update Exercise Entry");
            logButton.addActionListener(x -> {
                logEntryCRUDListener.createExerciseLogEntry(date, exerciseName, Float.parseFloat(quantityInput.getText()));
                deleteExerciseLog(ent);
                dialog.dispose();
            });

            c = constraintDefaults.getDefaults();
            c.gridx = 0;
            c.gridy = 2;
            dialog.add(logButton, c);

            JButton deleteButton = new JButton("Delete Exercise Entry");
            deleteButton.addActionListener(x -> {
                deleteExerciseLog(ent);
                dialog.dispose();
            });
            deleteButton.setBackground(new Color(107, 1, 1));
            deleteButton.setForeground(new Color(230, 230, 230));

            c = constraintDefaults.getDefaults();
            c.gridx = 1;
            c.gridy = 2;
            dialog.add(deleteButton, c);
        } else {
            JButton logButton = new JButton("Update Exercise Entry");
            logButton.addActionListener(x -> {
                logEntryCRUDListener.createExerciseLogEntry(date, exerciseName, Float.parseFloat(quantityInput.getText()));
                deleteExerciseLog(ent);
                dialog.dispose();
            });

            c = constraintDefaults.getDefaults();
            c.gridwidth = 2;
            c.gridx = 0;
            c.gridy = 2;
            dialog.add(logButton, c);
        }

        dialog.setMinimumSize(new Dimension(350, 200));
        dialog.setVisible(true);
        updateUI();
    }

    private void deleteExerciseLog(ExerciseEntry e) {
        logEntryCRUDListener.removeLogEntry(e);
        updateTree("exercise log");
    }

    private void deleteFoodLog(ConsumptionEntry e) {
        logEntryCRUDListener.removeLogEntry(e);
        updateTree("food log");
        updateTree("recipe log");
    }

    
}
