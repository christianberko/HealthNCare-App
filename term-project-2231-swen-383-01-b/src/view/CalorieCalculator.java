package view;

import javax.swing.*;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import controller.CheckCaloriesResponder;
import model.HealthNCare;

import java.time.LocalDate;

/*
 * @author
 * @version 1.0
 * 
 * Displays the calories consumed, expended, and a net calorie total for the day
 */
public class CalorieCalculator extends JPanel implements Observer {
    private GridBagConstraintDefaults constraintDefaults;
    private GridBagConstraints c;
    private CheckCaloriesResponder checkCalories;
    private HealthNCare healthNCare;
    private LocalDate date;
    private String color;
    private String text;
    private String tooltip;
    private JLabel totalCaloriesLabel;
    private JLabel totalCalories;
    private JLabel totalExerciseCalories;
    private JLabel netCaloriesLabel;
    private JLabel netCalories;
    private SwingUI ui; // TODO: update this to use the interface

    /*
     * @param health The object to Observe
     * @param gui The object to Observe
     * 
     * Constructor for the CalorieCalculator class
     */
    public CalorieCalculator(HealthNCare health, SwingUI gui) {
        // The total calories consumed for the day.
        // The total calories expended in exercise for the day.
        // The net calories (consumed - expended) for the day (this may be negative).
        // The total number of calories consumed for the day and some indication of whether the net calories exceeds the set limit. 

        healthNCare = health;
        this.ui = gui;
        healthNCare.addSubscriber(this);
        gui.addSubscriber(this);

        this.setLayout(new GridBagLayout());
        constraintDefaults = new GridBagConstraintDefaults();
        checkCalories = new CheckCaloriesResponder(healthNCare);

        JLabel title = new JLabel("<html><font size=6><center>Calorie Calculator</font><br><font size=4>See your daily calorie intake, expenditure, and net balance</font></center></html>");
        c = constraintDefaults.getDefaults();
        c.gridwidth = 4;
        c.gridx = 0;
        c.gridy = 0;
        this.add(title, c);

        totalCaloriesLabel = new JLabel("<html><font color=green><b>Total calories consumed:    </b></font></html>",SwingConstants.RIGHT);
        totalCaloriesLabel.setToolTipText(tooltip);
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 1;
        this.add(totalCaloriesLabel, c);

        totalCalories = new JLabel("", SwingConstants.CENTER);
        totalCalories.setToolTipText(tooltip);
        c = constraintDefaults.getDefaults();
        c.gridx = 2;
        c.gridy = 1;
        this.add(totalCalories, c);

        JLabel totalExerciseCaloriesLabel = new JLabel("Total calories expended through exercise: ", SwingConstants.RIGHT);
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 2;
        this.add(totalExerciseCaloriesLabel, c);

        JLabel minus = new JLabel("<html><font size=8>-</font></html>");
        c = constraintDefaults.getDefaults();
        c.insets = new Insets(3,80,15,0);
        //c.ipadx = -50;
        c.gridx = 1;
        c.gridy = 2;
        this.add(minus, c);

        totalExerciseCalories = new JLabel("", SwingConstants.CENTER);
        //JLabel totalExerciseCalories = new JLabel("<html><font size=5>-</font> &nbsp &nbsp &nbsp &nbsp &nbsp 800</html>", SwingConstants.CENTER);
        c = constraintDefaults.getDefaults();
        c.gridx = 2;
        c.gridy = 2;
        this.add(totalExerciseCalories, c);

        JSeparator divider = new JSeparator();
        c = constraintDefaults.getDefaults();
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 3;
        this.add(divider, c);

        netCaloriesLabel = new JLabel("Net calories (consumed - expended):   ", SwingConstants.RIGHT);
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 4;
        this.add(netCaloriesLabel, c);

        JLabel equals = new JLabel("<html><font size=6>=</font></html>");
        c = constraintDefaults.getDefaults();
        c.insets = new Insets(3,80,5,0);
        //c.ipadx = -50;
        c.gridx = 1;
        c.gridy = 4;
        this.add(equals, c);

        netCalories = new JLabel("", SwingConstants.CENTER);
        c = constraintDefaults.getDefaults();
        c.gridx = 2;
        c.gridy = 4;
        this.add(netCalories, c);

        //updateValues();
    }

    /*
     * @param messageType The type of update that's being sent
     * @param message The contents of the update
     * 
     * Observer updates the class if the messageType is something that affects it
     */
    @Override
    public void update(String messageType, String message) {
        if (messageType.equals("updateLog")) {
            updateValues();
        } else if (messageType.equals("updateDate")) {
            date = LocalDate.parse(message);
            updateValues();
        }
        else if (messageType.equals("updateFood")) {
            updateValues();
        }
    }

    /*
     * Repaints the values if an update is detected
     */
    public void updateValues() {

        float consumed = checkCalories.getConsumedCalories(date);
        System.out.println("consumed"+consumed);
        float expended = checkCalories.getExpendedCalories(date);
        float limit = checkCalories.getCalorieLimit(date);

        float net = consumed-expended;

        //If over daily limit, make text red to indicate they're over the limit and update tooltip to reflect, otherwise keep it green to show they are safe
        if (consumed <= limit) {
            color = "green";
            text = "";
            tooltip = "Green text means you are within your Daily Calorie Limit.";
        } else {
            color = "red";
            text = "<br>(Exceeded Daily Limit)";
            tooltip = "Red text means you have exceeded your Daily Calorie Limit.";
        }

        totalCaloriesLabel.setText("<html><b><font color="+color+">Total calories consumed"+text+":    </font></b></html>");
        totalCaloriesLabel.setToolTipText(tooltip);
        totalCalories.setText("<html><b><font color="+color+">"+String.format("%.2f", consumed)+"</font></b></html>");
        totalCalories.setToolTipText(tooltip);
        totalExerciseCalories.setText(String.format("%.2f", expended));

        if (net <= limit) {
            color = "green";
            text = "";
            tooltip = "Green text means you are within your Daily Calorie Limit.";
        } else {
            color = "red";
            text = " (Exceeded Daily Limit)";
            tooltip = "Red text means you have exceeded your Daily Calorie Limit.";
        }
        netCaloriesLabel.setText("<html><b><font color="+color+">Net calories (consumed - expended)"+text+":    </font></b></html>");
        netCaloriesLabel.setToolTipText(tooltip);
        netCalories.setText("<html><b><font color="+color+">"+String.format("%.2f", net)+"</font></b></html>");
        netCalories.setToolTipText(tooltip);
    }
}
