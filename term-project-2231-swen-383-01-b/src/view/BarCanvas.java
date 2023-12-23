package view;

import javax.swing.*;

import java.awt.Color;
import java.awt.Graphics;

/*
 * @author 
 * @version 1.0
 * 
 * Draws a bar in a bar chart.
 */
public class BarCanvas extends JPanel {

    private float percentage;
    private Color barColor;

    /*
     * @param percentage Percentage out of 100 that the bar should represent.
     * @param color Color of the bar
     * 
     * Constructor for the BarCanvas class
     */
    public BarCanvas(float percentage, Color color) {
        this.percentage = percentage;
        barColor = color;
    }

    /*
     * @param g A Graphics object
     * 
     * Draws the bar
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();

        int barWidth = 50;
        int barHeight = 200;

        int startX = (width - barWidth) / 2;

        // Adjust the y-coordinate to go bottom-up
        int adjustedY = height - barHeight;

        // Calculate the y-coordinate for the bottom of the colored part
        int bottomY = adjustedY + barHeight - (int) (barHeight * percentage / 100.0);

        g.setColor(barColor);
        g.fillRect(startX, bottomY, barWidth, (int) (barHeight * percentage / 100.0));

        g.setColor(Color.BLACK);
        g.drawRect(startX, adjustedY, barWidth, barHeight);
    }

    /*
     * @param percentage Percentage out of 100 that the bar should represent.
     * 
     * Updates the bar to reflect new data
     */
    public void updatePercentage(float percentage) {
        this.percentage = percentage;

        // Trigger a repaint to update the bar chart
        repaint();
    }
}
