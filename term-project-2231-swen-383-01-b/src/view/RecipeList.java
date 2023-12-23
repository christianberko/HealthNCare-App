package view;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import controller.ItemListResponder;
import model.HealthNCare;
import model.Recipe;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class RecipeList extends JPanel {
    private GridBagConstraintDefaults constraintDefaults;
    private GridBagConstraints c;
    private ItemListResponder itemListResponder;
    private JTree listRecipes;
    private HealthNCare healthNCare;

    public RecipeList(HealthNCare health) {
        
        healthNCare = health;
        
        itemListResponder = new ItemListResponder(health);
        this.setLayout(new GridBagLayout());
        constraintDefaults = new GridBagConstraintDefaults();

        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        for (Recipe r : itemListResponder.getRecipesMap().values()) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(r.getName());
            root.add(node);
        }
        listRecipes = new JTree(new DefaultTreeModel(root));

        listRecipes.setVisibleRowCount(5);
        listRecipes.setRootVisible(false);
        JScrollPane recipes = new JScrollPane(listRecipes);
        recipes.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        c = constraintDefaults.getDefaults();
        c.gridx = 0;
        c.gridy = 0;
        this.add(recipes, c);
    }

    public JTree getTree() {
        return listRecipes;
    }
}
