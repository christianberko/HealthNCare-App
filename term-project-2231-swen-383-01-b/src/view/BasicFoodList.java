package view;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import controller.ItemListResponder;
import model.FoodItem;
import model.HealthNCare;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

/*
 * @author
 * @version 1.0
 * 
 * A list of basic foods available to modify
 */
public class BasicFoodList extends JPanel {
    private GridBagConstraintDefaults constraintDefaults;
    private GridBagConstraints c;
    private ItemListResponder itemListResponder;
    private DefaultMutableTreeNode root;
    private HealthNCare healthNCare;
    public JTree listFoods;

    /*
     * @param health The object to Observe
     * 
     * Constructor for the BasicFoodList class
     */
    public BasicFoodList(HealthNCare health) {
        healthNCare = health;
        itemListResponder = new ItemListResponder(healthNCare);
        this.setLayout(new GridBagLayout());
        constraintDefaults = new GridBagConstraintDefaults();

        root = new DefaultMutableTreeNode();
        itemListResponder.getFoodsMap();
        for (FoodItem f : itemListResponder.getFoodsMap().values()) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(f.getName());
            root.add(node);
        }

        listFoods = new JTree(new DefaultTreeModel(root));
        listFoods.setRootVisible(false);
        listFoods.setVisibleRowCount(5);
        JScrollPane foods = new JScrollPane(listFoods);
        foods.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 0;
        this.add(foods, c);
    }
}
