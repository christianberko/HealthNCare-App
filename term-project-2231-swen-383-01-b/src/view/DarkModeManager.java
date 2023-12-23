package view;

import javax.swing.*;

import java.awt.*;

public class DarkModeManager {
    private boolean isDarkMode = false;

    public void toggleDarkMode(JFrame frame) {
        isDarkMode = !isDarkMode;

        if (isDarkMode) {
            setDarkMode(frame);
        } else {
            setLightMode(frame);
        }
    }

    private void setDarkMode(JFrame frame) {
        try {
            javax.swing.UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
            com.formdev.flatlaf.FlatLaf.updateUI();
            // Now do other necessary stuff to make sure the UI get updated properly.
        } catch (UnsupportedLookAndFeelException updateFlatLafThemeException) {
            
        }
    }

    private void setLightMode(JFrame frame) { // This looks good, just need to set it as default, and the JTree background color, and BarChart background color
        try {
            javax.swing.UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
            com.formdev.flatlaf.FlatLaf.updateUI();
            // Now do other necessary stuff to make sure the UI get updated properly.
        } catch (UnsupportedLookAndFeelException updateFlatLafThemeException) {
            
        }
    }

    private void setAppColors(JFrame frame, Color backgroundColor, Color foregroundColor) {
        frame.getContentPane().setBackground(backgroundColor);
        frame.setBackground(backgroundColor);
        frame.setForeground(foregroundColor);
        frame.getContentPane().setForeground(foregroundColor);

        Component[] components = frame.getContentPane().getComponents();
        for (Component component : components) {
            component.setForeground(foregroundColor);
            component.setBackground(backgroundColor);
            
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                Component[] panelComponents = panel.getComponents();
                for (Component panelComponent : panelComponents) {
                    panelComponent.setForeground(foregroundColor);
                    panelComponent.setBackground(backgroundColor);
                }
            }

            if (component instanceof JTree) {
                JTree tree = (JTree) component;
                tree.setBackground(backgroundColor);
                tree.setForeground(foregroundColor);
            }

        }
    }

}
