package view;

import javax.swing.*;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.time.LocalDate;

import controller.LogEntryCRUDListener;
import model.HealthNCare;

public class SetLimits extends JDialog {
    private HealthNCare hnc;
    private UserInterface ui;
    private LocalDate enteredDate;
    private String dateText;
    private GridBagConstraintDefaults constraintDefaults;
    private LogEntryCRUDListener logEntryCRUDListener;
    private GridBagConstraints c;
    private JTextField weightInput;
    private JTextField calorieInput;
    private JDialog parent = this;

    public SetLimits(HealthNCare hnc, UserInterface ui, JFrame parent, String dateString, LocalDate date, String type) {
        this.hnc = hnc;
        this.ui = ui;
        enteredDate = date;
        dateText = dateString;
        this.setTitle("Set your " + type + " limit for " + dateText);
        this.setLocationRelativeTo(parent);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setModal(true);
        this.setLayout(new GridBagLayout());
        constraintDefaults = new GridBagConstraintDefaults();
        logEntryCRUDListener = new LogEntryCRUDListener(hnc, ui);
        if (type == "calories") {
            JLabel calories = new JLabel("What is your maximum calories for the day? ", SwingConstants.RIGHT);
            c = constraintDefaults.getDefaults();
            c.gridx = 0;
            c.gridy = 0;
            this.add(calories, c);

            calorieInput = new JTextField("2000.0");
            c = constraintDefaults.getDefaults();
            c.gridx = 1;
            c.gridy = 0;
            this.add(calorieInput, c);
        } else if (type == "weight") {
            JLabel weight = new JLabel("What's your weight today? (pounds)", SwingConstants.RIGHT);
            c = constraintDefaults.getDefaults();
            c.gridx = 0;
            c.gridy = 1;
            this.add(weight, c);

            weightInput = new JTextField("150.0");
            c = constraintDefaults.getDefaults();
            c.gridx = 1;
            c.gridy = 1;
            this.add(weightInput, c);
        }

        JButton saveChangesButton = new JButton("Save Changes");
        saveChangesButton.addActionListener(e -> saveData(type));
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 2;
        this.add(saveChangesButton, c);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> actionPerformed());
        c = constraintDefaults.getDefaults();
        c.gridx = 1;
        c.gridy = 2;
        this.add(cancelButton, c);

        this.pack();
    }

    private void actionPerformed() {
        this.dispose();
    }

    private void saveData(String type) {
        //input validation
        try {
            if (type == "weight") {
                float fWeight = Float.parseFloat(weightInput.getText());
                logEntryCRUDListener.addWeightEntry(enteredDate, fWeight);
            } else if (type == "calories") {
                float fCalorie = Float.parseFloat(calorieInput.getText());
                logEntryCRUDListener.addCalorieLimit(enteredDate, fCalorie);
            }
            actionPerformed();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parent, "You entered an invalid number. Please make sure it is a number, with a period for the decimal symbol.","Input error",JOptionPane.ERROR_MESSAGE);
        }
    }
}
