package Design_tool_prototype1;

import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import org.w3c.dom.Element;

import java.io.StringReader;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;

class NavigatorTreeCellRenderer extends DefaultTreeCellRenderer {
    ImageIcon projectIcon;
    ImageIcon patternIcon;
    ImageIcon elementIcon;
    ImageIcon frontIcon;
    ImageIcon rearIcon;
    ImageIcon containerIcon;
    ImageIcon buttonIcon;
    ImageIcon patternHistoryIcon;

    public NavigatorTreeCellRenderer() {
        projectIcon = new ImageIcon("project_icon.gif");
        patternIcon = new ImageIcon("pattern_object_icon.gif");
        elementIcon = new ImageIcon("element_icon.gif");
        frontIcon = new ImageIcon("front_view_icon.gif");
        rearIcon = new ImageIcon("rear_view_icon.gif");
        containerIcon = new ImageIcon("container_icon.gif");
        patternHistoryIcon = new ImageIcon("pattern_history_icon.gif");
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
                    if(element.getNodeName().equals("svg")){
                        setIcon(containerIcon);
                        setText(element.getAttribute("id"));
                    }
                    else if(element.getNodeName().equals("path")){
                        setIcon(getElementIcon(element));
                        setText(element.getAttribute("id"));
                    }
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
                    else if(nodeObj.equals("Pattern History")){
                        setIcon(patternHistoryIcon);
                    }
                }
            }
        }
        return this;
    }

    private ImageIcon getElementIcon(Element element){
        java.awt.Shape s = null;
        try{
            s = org.apache.batik.parser.AWTPathProducer.createShape(new StringReader(element.getAttributeNS(null,"d")), new GeneralPath().WIND_EVEN_ODD);
        }
        catch(Exception e){
            System.out.println("Line 570: Exception caught at test Splitter"+e);
        }
        double x = s.getBounds().getWidth();
        double y = s.getBounds().getHeight();

        double x_ratio = x/45;
        double y_ratio = y/45;
        double general_ratio;
        
        if(x_ratio>y_ratio){
            general_ratio = x_ratio;
        }
        else{
            general_ratio = y_ratio;
        }

        java.awt.geom.AffineTransform element_transform = new java.awt.geom.AffineTransform();
        element_transform.scale(1/general_ratio, 1/general_ratio);
        element_transform.translate(-s.getBounds().x, -s.getBounds().y);
        java.awt.Shape transformed_shape = element_transform.createTransformedShape(s);

        BufferedImage image = new BufferedImage(50,50,BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();
        Graphics2D graphics2d = (Graphics2D)graphics;
        graphics2d.setColor(Color.WHITE);
        graphics2d.draw(transformed_shape);

        graphics2d.dispose();
        return new ImageIcon(image);
    }
}