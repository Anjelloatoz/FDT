package Design_tool_prototype1;

/**
 *
 * @author Anjello
 */
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import org.w3c.dom.Element;

import java.util.*;

public class navigator extends JFrame implements ActionListener {
        JPanel navigatorPanel;
        paintBoard pb;
        JTable shape_table;
        int shape_count = 0;

    public navigator(){
        navigatorPanel = new JPanel();
        shape_table = new JTable(10,2);
        shape_table.setGridColor(Color.BLUE);


        navigatorPanel.setLayout(new BorderLayout());
        navigatorPanel.add(shape_table, BorderLayout.CENTER);
    }

    public JPanel getNavigator(){
        return navigatorPanel;
    }

    public void setElement(Element e){
        shape_count++;
        System.out.println(e.getAttribute("id"));
        shape_table.setValueAt("Shape "+shape_count,shape_count , 0);
        shape_table.setValueAt(e.getAttribute("fill"),shape_count , 1);
    }

    public void actionPerformed(ActionEvent ae){

    }
}
