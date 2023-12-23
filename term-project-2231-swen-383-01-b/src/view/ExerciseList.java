package view;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import controller.ItemListResponder;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import model.Exercise;
import model.HealthNCare;

/*
 * @author
 * @version 1.0
 * 
 * Lists the exercises available to modify
 */
public class ExerciseList extends JPanel {
    private GridBagConstraintDefaults constraintDefaults;
    private GridBagConstraints c;
    private HealthNCare healthNCare;
    private JTree listExercises;

    /*
     * @param ItemListResponder Returns the data to populate the list
     * 
     * @param health The object to Observe
     * 
     * Constructor for the ExerciseList class
     */
    public ExerciseList(ItemListResponder itemListResponder, HealthNCare health) {

        healthNCare = health;

        this.setLayout(new GridBagLayout());
        constraintDefaults = new GridBagConstraintDefaults();

        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        for (Exercise e : itemListResponder.getExerciseMap().values()) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(e.getName());
            root.add(node);
        }

        listExercises = new JTree(new DefaultTreeModel(root));
        listExercises.setRootVisible(false);
        listExercises.setVisibleRowCount(5);
        JScrollPane foods = new JScrollPane(listExercises);
        foods.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 0;
        this.add(foods, c);
    }

    /*
     * @return list of exercises
     */
    public JTree getTree() {
        return listExercises;
    }
}
