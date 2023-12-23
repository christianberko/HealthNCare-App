package view;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;

import controller.ItemListResponder;
import controller.SaveDataListener;
import controller.LogEntryCRUDListener;
import controller.DailyStatsResponder;
import controller.CheckCaloriesResponder;
import exceptions.PastEntryConstraintException;
import model.CalorieLimitEntry;
import model.ConsumptionEntry;
import model.HealthNCare;
import model.LogEntry;
import model.Observable;
import model.WeightEntry;

public class SwingUI extends JFrame implements UserInterface, Observer, Observable {
    private HealthNCare healthNCare;
    private CalorieCalculator calorieCalculator;
    private DailyLogTrees dailyLogTables;
    private NutritionBarChart nutritionBarChart;
    private SetLimits setLimits;
    private SettingsDialog settingsDialog;
    private DatePicker datePicker;
    private DatePickerSettings datePickerSettings = new DatePickerSettings();
    private DarkModeManager darkModeManager = new DarkModeManager();
    private GridBagConstraintDefaults constraintDefaults;
    private GridBagConstraints c;
    private LocalDate enteredDate;
    private String dateText;
    private JFrame parent = this;
    private ItemListResponder itemListResponder;
    private LogEntryCRUDListener logEntryCRUDListener;
    private DailyStatsResponder dailyStatsResponder;
    private CheckCaloriesResponder checkCaloriesResponder;
    private SaveDataListener saveDataListener;
    private JButton setCalorieLimitsButton;
    private JButton setWeightLimitsButton;
    private JLabel setCalorieLimitsLabel;
    private JLabel setWeightLimitsLabel;

    protected List<Observer> observers = new ArrayList<>();

    public SwingUI(HealthNCare healthNCare) {
        this.healthNCare = healthNCare;
        healthNCare.addSubscriber(this);
        itemListResponder = new ItemListResponder(healthNCare);
        logEntryCRUDListener = new LogEntryCRUDListener(healthNCare, this);
        saveDataListener = new SaveDataListener(healthNCare);
        dailyStatsResponder = new DailyStatsResponder(healthNCare);
        checkCaloriesResponder = new CheckCaloriesResponder(healthNCare);
        datePickerSettings.setAllowEmptyDates(false); //force the selected date to be the current date by default
        this.setMinimumSize(new Dimension(600,550)); // set minimum size of the window
        this.setTitle("Team B's HealthNCare App");
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(800,800));
        constraintDefaults = new GridBagConstraintDefaults();

        JButton settingsButton = new JButton("Settings");
        settingsButton.addActionListener(e -> openSettingsDialog());
        settingsButton.setPreferredSize(new Dimension(100, 30));

        c = constraintDefaults.getAppDefaults();
        c.ipady = 60;
        c.gridheight = 2;
        c.gridx = 0;
        c.gridy = 2;
        this.add(settingsButton, c);


        JPanel dateBox = new JPanel();
        dateBox.setLayout(new GridBagLayout());

        JLabel dateLabel = new JLabel("Date: ", SwingConstants.RIGHT);
        c = constraintDefaults.getAppDefaults();
        c.gridx = 0;
        c.gridy = 0;
        dateBox.add(dateLabel, c);

        datePicker = new DatePicker(datePickerSettings);
        datePicker.getComponentToggleCalendarButton().setText("Select Date");
        datePicker.addDateChangeListener(d -> dateChanged(d));
        c = constraintDefaults.getAppDefaults();
        c.gridx = 1;
        c.gridy = 0;
        dateBox.add(datePicker, c);
        formatDate();

        //0,1-2
        dailyLogTables = new DailyLogTrees(enteredDate, this, healthNCare);
        c = constraintDefaults.getAppDefaults();
        c.anchor = GridBagConstraints.PAGE_START;
        c.gridwidth = 2;
        c.ipady = 60;
        c.gridx = 0;
        c.gridy = 1;
        
        this.add(dailyLogTables, c);

        JPanel setLimitsBox = new JPanel();
        setLimitsBox.setLayout(new GridBagLayout());

        setCalorieLimitsLabel = new JLabel("<html><center>Current Calorie limit set to: " + checkCaloriesResponder.getCalorieLimit(enteredDate) + " cals</center></html>", SwingConstants.CENTER);
        c = constraintDefaults.getAppDefaults();
        c.ipady = 30;
        c.gridx = 0;
        c.gridy = 0;
        setLimitsBox.add(setCalorieLimitsLabel, c);

        setCalorieLimitsButton = new JButton("Set Calorie Limits for " + dateText);
        setCalorieLimitsButton.addActionListener(e -> setLimits("calories"));
        c = constraintDefaults.getAppDefaults();
        c.ipady = 20;
        c.gridx = 0;
        c.gridy = 1;
        setLimitsBox.add(setCalorieLimitsButton, c);

        setWeightLimitsLabel = new JLabel("<html><center>Current Weight is: " + dailyStatsResponder.getWeight(enteredDate) + " lbs</center></html>", SwingConstants.CENTER);
        c = constraintDefaults.getAppDefaults();
        c.ipady = 30;
        c.gridx = 1;
        c.gridy = 0;
        setLimitsBox.add(setWeightLimitsLabel, c);

        setWeightLimitsButton = new JButton("Set Weight for " + dateText);
        setWeightLimitsButton.addActionListener(e -> setLimits("weight"));
        c = constraintDefaults.getAppDefaults();
        c.ipady = 20;
        c.gridx = 1;
        c.gridy = 1;
        setLimitsBox.add(setWeightLimitsButton, c);

        c = constraintDefaults.getAppDefaults();
        c.gridx = 2;
        c.gridy = 0;
        dateBox.add(setLimitsBox, c);

        //1,0
        c = constraintDefaults.getAppDefaults();
        c.gridwidth = 2;
        c.ipady = 10;
        c.gridx = 0;
        c.gridy = 0;
        dateBox.setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED, Color.gray, Color.black));
        this.add(dateBox, c);

        //1,1
        calorieCalculator = new CalorieCalculator(healthNCare, this);
        c = constraintDefaults.getAppDefaults();
        c.ipadx = 470;
        c.ipady = 230;
        //c.gridwidth = 2;
        c.gridx = 1;
        c.gridy = 4;
        calorieCalculator.setBorder(BorderFactory.createLineBorder(Color.black, 2, true));
        this.add(calorieCalculator, c);

        //1,2
        nutritionBarChart = new NutritionBarChart(healthNCare, this);
        c = constraintDefaults.getAppDefaults();
        c.insets = new Insets(-80,6,-50,6);
        c.anchor = GridBagConstraints.CENTER;
        c.ipadx = 470;
        c.ipady = 0;
        //c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 4;
        nutritionBarChart.setBorder(BorderFactory.createLineBorder(Color.black, 2, true));
        this.add(nutritionBarChart, c);

        //0-1,4
        JButton saveData = new JButton("Save Changes");
        saveData.addActionListener(x -> saveData());
        saveData.setPreferredSize(new Dimension(100, 30));
        c = constraintDefaults.getAppDefaults();
        c.gridheight = 2;
        c.ipady = 50;
        c.gridx = 1;
        c.gridy = 2;
        c.anchor = GridBagConstraints.NORTH;
        this.add(saveData, c);

        dateChanged(new DateChangeEvent(datePicker, enteredDate, enteredDate));

        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void update(String messageType, String message) {
        if (messageType == "updateFood") {
            //BasicFoodList.refreshTree();

        } else if (messageType == "updateLog") {
            setCalorieLimitsLabel.setText("<html><center>Current Calorie limit set to: " + checkCaloriesResponder.getCalorieLimit(enteredDate) + " cals</center></html>");
            setWeightLimitsLabel.setText("<html><center>Current Weight is: " + dailyStatsResponder.getWeight(enteredDate) + " lbs</center></html>");

        } else if (messageType == "updateExercise") {

        } else if (messageType == "error") {
            JOptionPane.showMessageDialog(parent, message,"Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void addSubscriber(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeSubscriber(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifySubscribers(String messageType, String message) {
        observers.forEach(observer -> observer.update(messageType, message));
    }

    private void dateChanged(DateChangeEvent d) {
        notifySubscribers("updateDate", d.getNewDate().toString());
        formatDate();
        setCalorieLimitsButton.setText("Set Calorie Limits for " + dateText);
        setCalorieLimitsLabel.setText("<html><center>Current Calorie limit set to: " + checkCaloriesResponder.getCalorieLimit(enteredDate) + " cals</center></html>");
        setWeightLimitsButton.setText("Set Weight for " + dateText);
        setWeightLimitsLabel.setText("<html><center>Current Weight is: " + dailyStatsResponder.getWeight(enteredDate) + " lbs</center></html>");
    }

    private void formatDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("LLL dd, YYYY");
        enteredDate = LocalDate.parse(datePicker.getDateStringOrEmptyString());
        dateText = enteredDate.format(formatter);
        System.out.println(dateText);
    }

    private void openSettingsDialog() {
        settingsDialog = new SettingsDialog(this, healthNCare);
        settingsDialog.setVisible(true);    
    }

    public void setLimits(String type) {
        try {
            if (isValidToEnter()) {
                setLimits = new SetLimits(healthNCare, this, this, dateText, enteredDate, type);
                setLimits.setVisible(true);  
            }
        } catch (Exception e) {
            showErrorMessage(e.getMessage());
        }
          
    }

    /**
   * Determines whether the desired entry is allowed to be entered. The rules are: A {@link
   * CalorieLimitEntry} or {@link WeightEntry} can't be created for a previous date or if a {@link
   * ConsumptionEntry} has been logged
   *
   * @param entry The entry to test
   */
  private boolean isValidToEnter() throws PastEntryConstraintException {
    if (!logEntryCRUDListener.getBasicFoodsLogged(enteredDate).isEmpty() && datePicker.getDate().isEqual(LocalDate.now())) {
        throw new PastEntryConstraintException();
    } else if (datePicker.getDate().isBefore(LocalDate.now())) {
        throw new PastEntryConstraintException();
    }
    return true;
  }

    // public String deleteFoodEntry() {
    // view.DailyLogTrees foodConsumed = new view.DailyLogTrees(healthNCare, this);
    // return null;
        
    // }

    // public String[] addFoodEntry(ArrayList<String> foodList) {
    // view.DailyLogTrees foodConsumed = new view.DailyLogTrees(healthNCare, this);
    // return null;
    // }

    // //not in the interface, since the functionality won't be backported to TextUI
    // atm
    // public String[] addExerciseEntry() {
    // view.DailyLogTrees exercise = new view.DailyLogTrees(healthNCare, this);
    // return null;
    // }

    public LocalDate selectDate() {
        return null;
    }

    public void viewDailyStats(
      ArrayList<String> foodConsumed,
      float caloriesConsumed,
      boolean overCalorieLimit,
      float weight,
      float[] nutrition) {
        view.CalorieCalculator calorieView = new view.CalorieCalculator(healthNCare, this);
        view.NutritionBarChart barChart = new view.NutritionBarChart(healthNCare, this);
    }

    public void saveData() {
        saveDataListener.onAction();
    }

    public String[] addFood() {
        view.SettingsDialog settings = new view.SettingsDialog(this, healthNCare);
        return null;
    }

    public void showErrorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(parent, errorMessage,"Error",JOptionPane.ERROR_MESSAGE);
    }

    public LocalDate getDate() {
        return enteredDate;
    }
}
