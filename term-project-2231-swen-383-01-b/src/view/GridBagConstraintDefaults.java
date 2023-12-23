package view;

import java.awt.GridBagConstraints;
import java.awt.Insets;

/*
 * @author
 * @version 1.0
 * 
 * Sets defaults for UI GridBagLayout constraints across the app
 */
public class GridBagConstraintDefaults extends GridBagConstraints {
    GridBagConstraints c;
    public GridBagConstraintDefaults() {
    }

    /*
     * Sets defaults for the main SwingUI app window
     */
    public GridBagConstraints getAppDefaults() {
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.insets = new Insets(6,6,6,6);
        return c;
    }

    /*
     * Sets defaults for the rest of the app by default
     */
    public GridBagConstraints getDefaults() {
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.insets = new Insets(3,3,3,3);
        return c;
    }

    /*
     * Sets defaults for the NutritionBarChart
     */
    public GridBagConstraints getChartDefaults() {
        c = new GridBagConstraints();
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(3,3,3,3);
        return c;
    }
}
