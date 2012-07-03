package Design_tool_prototype1;

import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import org.w3c.dom.Element;

class NavigatorTreeCellRenderer extends DefaultTreeCellRenderer {
    ImageIcon projectIcon;
    ImageIcon patternIcon;
    ImageIcon elementIcon;
    ImageIcon frontIcon;
    ImageIcon rearIcon;

    public NavigatorTreeCellRenderer() {
        projectIcon = new ImageIcon("project_icon.gif");
        patternIcon = new ImageIcon("pattern_object_icon.gif");
        elementIcon = new ImageIcon("element_icon.gif");
        frontIcon = new ImageIcon("front_view_icon.gif");
        rearIcon = new ImageIcon("rear_view_icon.gif");
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,boolean expanded,boolean leaf, int row, boolean hasFocus){
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        Object nodeObj = ((DefaultMutableTreeNode)value).getUserObject();        // check whatever you need to on the node user object

        projectObject project;
        patternObject pattern;
        Element element;
        try{
            project = (projectObject)nodeObj;
            setIcon(projectIcon);
            setText(project.project_name);
        }
        catch(Exception e){
            try{
                pattern = (patternObject)nodeObj;
                setIcon(patternIcon);
                setText(pattern.pattern_name);
            }
            catch(Exception e2){
                try{
                    element = (Element)nodeObj;
                    setIcon(elementIcon);
                    setText(element.getAttribute("id"));
                }
                catch(Exception e3){
                    if(nodeObj.equals("Front face")){
                        setIcon(frontIcon);
                        setText("Front Face");
                    }
                    else if(nodeObj.equals("Rear face")){
                        setIcon(rearIcon);
                        setText("Rear Face");
                    }
                }
            }
        }
        return this;
    }
}