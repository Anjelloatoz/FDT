package Design_tool_prototype1;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

import java.util.*;

public class toolbox extends JFrame implements ActionListener {
        JPanel toolPanel;
        JButton cursor_bt, line_bt, curve_bt, bezier_bt, square_bt, circle, star, drag, finish, delete;
        SVGConjurer svgc;

    public toolbox(SVGConjurer svgc){
        this.svgc = svgc;
        toolPanel = new JPanel();
        cursor_bt = new JButton("", new ImageIcon("cursor_bt.gif"));
        line_bt = new JButton("", new ImageIcon("line_bt 2.gif"));
        curve_bt = new JButton("", new ImageIcon("curve_bt.gif"));
        bezier_bt = new JButton("", new ImageIcon("Bezier_bt.gif"));
        drag = new JButton("", new ImageIcon("drag_bt.gif"));
        finish = new JButton("", new ImageIcon("finish_bt.gif"));
        delete = new JButton("", new ImageIcon("delete_bt.gif"));

        toolPanel.setLayout(new FlowLayout());
        toolPanel.add(cursor_bt);
        toolPanel.add(line_bt);
        toolPanel.add(curve_bt);
        toolPanel.add(bezier_bt);
        toolPanel.add(drag);
        toolPanel.add(finish);
        toolPanel.add(delete);

        cursor_bt.addActionListener(this);
        line_bt.addActionListener(this);
        curve_bt.addActionListener(this);
        bezier_bt.addActionListener(this);
        drag.addActionListener(this);
        finish.addActionListener(this);
        delete.addActionListener(this);
    }

    public JPanel getToolbox(){
        return toolPanel;
    }

    public void actionPerformed(ActionEvent ae){
        if(ae.getSource().equals(cursor_bt))svgc.shape_type_number = 0;
        if(ae.getSource().equals(line_bt)) svgc.shape_type_number = 2;
        if(ae.getSource().equals(curve_bt)) svgc.shape_type_number = 4;
        if(ae.getSource().equals(bezier_bt)) svgc.patternSplitter();
        if(ae.getSource().equals(finish)) svgc.completionAgent();
        if(ae.getSource().equals(delete)) svgc.delete_drawing();
        if(ae.getSource().equals(drag)){
            svgc.showPoints();
            //svgc.editPattern();
        }
    }
}
