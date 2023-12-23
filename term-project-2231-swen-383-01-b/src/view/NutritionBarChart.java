package view;

import javax.swing.*;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;
import java.time.LocalDate;

import controller.NutritionChartUpdater;
import model.HealthNCare;

/*
 * @author
 * @version 1.0
 * 
 * Creates and updates a bar chart that visually shows the percentages of fat, carbohydrates, and protein out of the sum. 
 * Also creates and updates an additional bar chart that shows the percentage of sodium out of the daily limit as set by the FDA 
 * (2,300 milligrams)
 */
public class NutritionBarChart extends JPanel implements Observer {
    private GridBagConstraintDefaults constraintDefaults;
    private GridBagConstraints c;
    private BarCanvas fatCanvas;
    private BarCanvas carbCanvas;
    private BarCanvas proteinCanvas;
    private BarCanvas sodiumCanvas;
    private JLabel fatLabel;
    private JLabel carbsLabel;
    private JLabel proteinLabel;
    private JLabel sodiumLabel;
    private NutritionChartUpdater nutritionChartUpdater;
    private LocalDate date;
    private HealthNCare healthNCare;

    /*
     * @param health The object to Observe
     * @param gui The object to Observe
     * 
     * Constructor for the NutritionBarChart class
     */
    public NutritionBarChart(HealthNCare health, SwingUI gui) {
        
        healthNCare = health;
        healthNCare.addSubscriber(this);
        gui.addSubscriber(this);
        nutritionChartUpdater = new NutritionChartUpdater(date, healthNCare);
        this.setLayout(new GridBagLayout());
        //this.setPreferredSize(new Dimension(600, 600));
        constraintDefaults = new GridBagConstraintDefaults();

        JPanel nutritionPanel = new JPanel();
        nutritionPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        nutritionPanel.setLayout(new GridBagLayout());

        fatCanvas = new BarCanvas(nutritionChartUpdater.getFatBarPercentage(), Color.RED);
        c = constraintDefaults.getChartDefaults();
        //c.anchor = GridBagConstraints.PAGE_END;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(0,0,0,0);
        c.ipadx = 50;
        c.ipady = 200;
        c.gridwidth = 1;
        c.gridheight = 2;
        c.gridx = 1;
        c.gridy = 0;
        nutritionPanel.add(fatCanvas, c);

        carbCanvas = new BarCanvas(nutritionChartUpdater.getCarbsBarPercentage(), Color.GREEN);
        c = constraintDefaults.getChartDefaults();
        //c.anchor = GridBagConstraints.PAGE_END;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(0,0,0,0);
        c.ipadx = 50;
        c.ipady = 200;
        c.gridwidth = 1;
        c.gridheight = 2;
        c.gridx = 2;
        c.gridy = 0;
        nutritionPanel.add(carbCanvas, c);

        proteinCanvas = new BarCanvas(nutritionChartUpdater.getProteinBarPercentage(), Color.BLUE);
        c = constraintDefaults.getChartDefaults();
        //c.anchor = GridBagConstraints.PAGE_END;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(0,0,0,0);
        c.ipadx = 50;
        c.ipady = 200;
        c.gridwidth = 1;
        c.gridheight = 2;
        c.gridx = 3;
        c.gridy = 0;
        nutritionPanel.add(proteinCanvas, c);

        JLabel maxNutrition = new JLabel("100%", SwingConstants.RIGHT);
        c = constraintDefaults.getChartDefaults();
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        c.gridx = 0;
        c.gridy = 0;
        nutritionPanel.add(maxNutrition, c);

        JLabel minNutrition = new JLabel("0%", SwingConstants.RIGHT);
        c = constraintDefaults.getChartDefaults();
        c.anchor = GridBagConstraints.LAST_LINE_END;
        c.insets = new Insets(0,0,-5,0);
        c.gridx = 0;
        c.gridy = 1;
        nutritionPanel.add(minNutrition, c);

        fatLabel = new JLabel("<html>Fat<br>"+nutritionChartUpdater.getFat()+"g<br>"+nutritionChartUpdater.getFatPercentage()+"%</html>", SwingConstants.CENTER);
        c = constraintDefaults.getChartDefaults();
        c.gridx = 1;
        c.gridy = 2;
        nutritionPanel.add(fatLabel, c);

        carbsLabel = new JLabel("<html>Carbs<br>"+nutritionChartUpdater.getCarbs()+"g<br>"+nutritionChartUpdater.getCarbsPercentage()+"%</html>", SwingConstants.CENTER);
        c = constraintDefaults.getChartDefaults();
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 2;
        c.gridy = 2;
        nutritionPanel.add(carbsLabel, c);

        proteinLabel = new JLabel("<html>Protein<br>"+nutritionChartUpdater.getProtein()+"g<br>"+nutritionChartUpdater.getProteinPercentage()+"%</html>", SwingConstants.CENTER);
        c = constraintDefaults.getChartDefaults();
        c.anchor = GridBagConstraints.LINE_START;
        c.ipadx = 10;
        c.gridx = 3;
        c.gridy = 2;
        nutritionPanel.add(proteinLabel, c);

        //sodium
        JPanel sodiumPanel = new JPanel();
        sodiumPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        sodiumPanel.setLayout(new GridBagLayout());

        sodiumCanvas = new BarCanvas(nutritionChartUpdater.getSodiumBarPercentage(), Color.ORANGE);
        c = constraintDefaults.getChartDefaults();
        //c.anchor = GridBagConstraints.PAGE_END;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(0,0,0,0);
        c.ipadx = 50;
        c.ipady = 200;
        c.gridwidth = 1;
        c.gridheight = 2;
        c.gridx = 0;
        c.gridy = 0;
        sodiumPanel.add(sodiumCanvas, c);

        JLabel maxSodium = new JLabel("2,300 mg", SwingConstants.LEFT);
        c = constraintDefaults.getChartDefaults();
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.gridx = 1;
        c.gridy = 0;
        sodiumPanel.add(maxSodium, c);

        JLabel minSodium = new JLabel("0 mg", SwingConstants.LEFT);
        c = constraintDefaults.getChartDefaults();
        c.anchor = GridBagConstraints.LAST_LINE_START;
        c.insets = new Insets(0,0,-5,0);
        c.gridx = 1;
        c.gridy = 1;
        sodiumPanel.add(minSodium, c);

        sodiumLabel = new JLabel("<html>Sodium<br>"+nutritionChartUpdater.getSodium()+"mg<br>"+nutritionChartUpdater.getSodiumPercentage()+"%</html>", SwingConstants.CENTER);
        c = constraintDefaults.getChartDefaults();
        c.anchor = GridBagConstraints.PAGE_START;
        c.gridx = 0;
        c.gridy = 2;
        sodiumPanel.add(sodiumLabel, c);


        //main barchart panel
        JLabel nutrition = new JLabel("<html><font size=6><center>Nutrition Bar Charts</font><br><font size=4>See your daily nutrition values</font></center></font></html>", SwingConstants.CENTER);
        c = constraintDefaults.getChartDefaults();
        c.insets = new Insets(0,0,0,0);
        c.gridwidth = 6;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 0;
        this.add(nutrition, c);

        c = constraintDefaults.getChartDefaults();
        c.insets = new Insets(0,0,0,10);
        c.gridwidth = 4;
        c.gridx = 0;
        c.gridy = 1;
        this.add(nutritionPanel, c);

        c = constraintDefaults.getChartDefaults();
        c.insets = new Insets(0,10,0,0);
        c.gridwidth = 2;
        c.gridx = 4;
        c.gridy = 1;
        this.add(sodiumPanel, c);
    }

    /*
     * Redraws the bars and updates the labels when a change in data is detected
     */
    public void updateBars() {
        nutritionChartUpdater = new NutritionChartUpdater(date, healthNCare);
        
        fatCanvas.updatePercentage(nutritionChartUpdater.getFatBarPercentage());
        carbCanvas.updatePercentage(nutritionChartUpdater.getCarbsBarPercentage());
        proteinCanvas.updatePercentage(nutritionChartUpdater.getProteinBarPercentage());
        sodiumCanvas.updatePercentage(nutritionChartUpdater.getSodiumBarPercentage());

        fatLabel.setText("<html>Fat<br>"+nutritionChartUpdater.getFat()+"mg<br>"+nutritionChartUpdater.getFatPercentage()+"%</html>");
        carbsLabel.setText("<html>Carbs<br>"+nutritionChartUpdater.getCarbs()+"g<br>"+nutritionChartUpdater.getCarbsPercentage()+"%</html>");
        proteinLabel.setText("<html>Protein<br>"+nutritionChartUpdater.getProtein()+"g<br>"+nutritionChartUpdater.getProteinPercentage()+"%</html>");
        sodiumLabel.setText("<html>Sodium<br>"+nutritionChartUpdater.getSodium()+"mg<br>"+nutritionChartUpdater.getSodiumPercentage()+"%</html>");
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
            updateBars();
        } else if (messageType.equals("updateDate")) {
            date = LocalDate.parse(message);
            System.out.println(date);
            updateBars();
        }
        else if (messageType.equals("updateFood")) {
            updateBars();
        }
    }
}
