package Design_tool_prototype1;

import javax.swing.*;
import javax.swing.ButtonGroup;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Image;

import java.util.*;

public class toolbox extends JFrame implements ActionListener {
        JPanel toolPanel;
        JToggleButton cursor_bt, line_bt, curve_bt, text_bt, square_bt, circle, star, drag;
        JButton bezier_bt, finish, delete;
        ButtonGroup canvas_group;
        SVGConjurer svgc;
        ribbonTest rt;
        Cursor line_cursor;
        Cursor curve_cursor;
        Cursor line_cut_cursor;
        Cursor curve_cut_cursor;
        Cursor text_cursor;
        Cursor drag_cursor;

    public toolbox(SVGConjurer svgc, ribbonTest rt){
        this.svgc = svgc;
        this.rt = rt;
        toolPanel = new JPanel();
        cursor_bt = new JToggleButton("", new ImageIcon("cursor_bt.gif"));
        line_bt = new JToggleButton("", new ImageIcon("line_bt 2.gif"));
        curve_bt = new JToggleButton("", new ImageIcon("curve_bt.gif"));
        drag = new JToggleButton("", new ImageIcon("drag_bt.gif"));
        text_bt = new JToggleButton("", new ImageIcon("text_bt.gif"));

        bezier_bt = new JButton("", new ImageIcon("cut_icon.gif"));
        finish = new JButton("", new ImageIcon("finish_bt.gif"));
        delete = new JButton("", new ImageIcon("delete_bt.gif"));

        canvas_group = new ButtonGroup();
        canvas_group.add(cursor_bt);
        canvas_group.add(line_bt);
        canvas_group.add(curve_bt);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image line_cursor_image = toolkit.getImage("line_cursor.png");
        Point line_cursor_hotspot = new Point(0,0);
        Image curve_cursor_image = toolkit.getImage("curve_cursor.png");
        Point curve_cursor_hotspot = new Point(0,0);
        Image line_cut_cursor_image = toolkit.getImage("line split cursor.png");
        Point line_cut_cursor_hotspot = new Point(0,0);
        Image curve_cut_cursor_image = toolkit.getImage("curve split cursor.png");
        Point curve_cut_cursor_hotspot = new Point(0,0);
        /*Image text_cursor_image = toolkit.getImage("text_cursor.gif");
        Point text_cursor_hotspot = new Point(0,0);
        Image drag_cursor_image = toolkit.getImage("text_cursor.gif");
        Point drag_cursor_hotspot = new Point(0,0);*/

        line_cursor = toolkit.createCustomCursor(line_cursor_image, line_cursor_hotspot, "Line");
        curve_cursor = toolkit.createCustomCursor(curve_cursor_image, curve_cursor_hotspot, "Curve");
        line_cut_cursor = toolkit.createCustomCursor(line_cut_cursor_image, line_cut_cursor_hotspot, "Pattern split");
        curve_cut_cursor = toolkit.createCustomCursor(curve_cut_cursor_image, line_cut_cursor_hotspot, "Pattern split");
        text_cursor = toolkit.createCustomCursor(line_cursor_image, line_cursor_hotspot, "Text");
        drag_cursor = toolkit.createCustomCursor(line_cursor_image, line_cursor_hotspot, "Drag");

        toolPanel.setLayout(new FlowLayout());
        toolPanel.add(cursor_bt);
        toolPanel.add(line_bt);
        toolPanel.add(curve_bt);
        toolPanel.add(text_bt);
        toolPanel.add(drag);
        toolPanel.add(bezier_bt);
        toolPanel.add(finish);
        toolPanel.add(delete);

        cursor_bt.addActionListener(this);
        line_bt.addActionListener(this);
        curve_bt.addActionListener(this);
        bezier_bt.addActionListener(this);
        drag.addActionListener(this);
        finish.addActionListener(this);
        delete.addActionListener(this);
        text_bt.addActionListener(this);
    }

    public JPanel getToolbox(){
        return toolPanel;
    }

    public void actionPerformed(ActionEvent ae){
        if(ae.getSource().equals(cursor_bt)){
            svgc.shape_type_number = 0;
            svgc.current_cursor = Cursor.getDefaultCursor();
            svgc.refreshTree();
        }
        if(ae.getSource().equals(line_bt)){
            svgc.shape_type_number = 2;
            svgc.canvas.setCursor(line_cursor);
//            svgc.current_cursor = this.line_cursor;
        }
        if(ae.getSource().equals(curve_bt)){
            svgc.shape_type_number = 4;
            svgc.canvas.setCursor(curve_cursor);
//            svgc.current_cursor = this.curve_cursor;
        }
        if(ae.getSource().equals(bezier_bt)){
            svgc.splitHandler();
            if(svgc.shape_type_number==2){
                svgc.current_cursor = this.line_cut_cursor;
            }
            else if(svgc.shape_type_number==4){
                svgc.current_cursor = this.curve_cut_cursor;
            }
        }
        if(ae.getSource().equals(text_bt)){
            if(rt.selectedSVGC.selected_shape == null){
                JOptionPane.showMessageDialog(null, "Please select a dress piece to add a text to.", "A dress item not selected", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            rt.selectedSVGC.setTextElement();
            rt.getRibbon().setSelectedTask(rt.getRibbon().getTask(4));
            rt.text_type_area.setText("Type your text here");
            rt.text_type_area.setSelectionStart(0);
            rt.text_type_area.setSelectionEnd(rt.text_type_area.getText().length());
            rt.text_type_area.requestFocus();
            rt.selectedSVGC.textPath = true;
            rt.setCursor(text_cursor);
        }
        if(ae.getSource().equals(finish)){
//            rt.setCursor(Cursor.DEFAULT_CURSOR);
            if(svgc.symmetric){
                svgc.symmetricFinishDrawing();
            }
            else
                svgc.completionAgent();
        }
        if(ae.getSource().equals(delete)){
            svgc.delete_drawing();
            svgc.removeResizeHandler();
        }
        if(ae.getSource().equals(drag)){
            svgc.pathDragging = true;
            svgc.showPoints();
//            rt.setCursor(drag_cursor);
        }
    }
}
