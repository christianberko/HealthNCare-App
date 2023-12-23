package view;

import javax.swing.*;

public class UpdateDailyLogDialog extends JDialog {
    private JTabbedPane selector;
    private JPanel basicFood;
    private JPanel recipe;
    private JPanel exercise;

    public UpdateDailyLogDialog() {
        selector = new JTabbedPane();
        basicFood = new JPanel();
        recipe = new JPanel();
        exercise = new JPanel();

        selector.addTab("Foods", basicFood);
        selector.addTab("Recipes", recipe);
        selector.addTab("Exercise", exercise);
    }

    public void addItems() {
        JLabel addFood = new JLabel("Add Food");
        basicFood.add(addFood);
        selector.setVisible(true);
    }

    public void removeItems() {

    }
}
