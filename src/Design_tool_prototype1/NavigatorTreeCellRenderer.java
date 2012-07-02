package Design_tool_prototype1;

import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;


class NavigatorTreeCellRenderer extends DefaultTreeCellRenderer {
    ImageIcon defaultIcon;
    ImageIcon specialIcon;

    public NavigatorTreeCellRenderer() {
        defaultIcon = new ImageIcon(NavigatorTreeCellRenderer.class.getResource("/images/defaultIcon.gif"));
        specialIcon = new ImageIcon(NavigatorTreeCellRenderer.class.getResource("/images/specialIcon.gif"));
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,boolean expanded,boolean leaf, int row, boolean hasFocus){
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        Object nodeObj = ((DefaultMutableTreeNode)value).getUserObject();        // check whatever you need to on the node user object

        projectObject project;
        try{
            project = (projectObject)nodeObj;
            setIcon(defaultIcon);
        }
        catch(Exception e){
            System.out.println("Class could not be casted");
        }
        return this;
    }
}