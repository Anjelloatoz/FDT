package Design_tool_prototype1;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

import java.util.*;
import javax.swing.undo.*;

public class DrawingBoardFooter extends JFrame implements ActionListener {
        JPanel drawingBoardFooterPanel;
        SVGConjurer svgc;
        UndoManager manager;
        JLabel pointer_x = new JLabel("Length");
        JLabel pointer_y = new JLabel("Height");
        JLabel pointer_x_value = new JLabel("00");
        JLabel pointer_y_value = new JLabel("00");
        JButton undo, redo;
        JToggleButton symatric;

    public DrawingBoardFooter(SVGConjurer svgc){
        this.svgc = svgc;
        
        drawingBoardFooterPanel = new JPanel();
        drawingBoardFooterPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        BevelBorder lowered_border = new BevelBorder(BevelBorder.LOWERED);

        pointer_x.setMaximumSize(new Dimension(30,27));
        pointer_x.setForeground(Color.green);

        pointer_x_value.setMaximumSize(new Dimension(100,27));
        pointer_x_value.setBorder(lowered_border);

        pointer_y.setMaximumSize(new Dimension(30,27));
        pointer_y.setForeground(Color.green);

        pointer_y_value.setMaximumSize(new Dimension(100,27));
        pointer_y_value.setBorder(lowered_border);

        undo = new JButton("", new ImageIcon("small-undo-icon.gif"));
        redo = new JButton("", new ImageIcon("small-redo-icon.gif"));
        symatric = new JToggleButton("", new ImageIcon("symatric-icon.gif"));
        undo.setEnabled(false);
        redo.setEnabled(false);
        symatric.addActionListener(this);

        undo.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent ae){
                try{
                    System.out.println("*****Undo called*****");
                    manager.undo();

                    System.out.println("*********************************");
                    System.out.println("");
                } catch(javax.swing.undo.CannotUndoException ex){ex.printStackTrace();}

                finally{
                    updateButtons();
                }
            }
        });

        redo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                try{
                    System.out.println("*****Redo called*****");
                    manager.redo();
                    System.out.println("*********************************");
                    System.out.println("");
                } catch(javax.swing.undo.CannotRedoException ex){ex.printStackTrace();}

                finally{
                    updateButtons();
                }
            }
        });

        drawingBoardFooterPanel.add(pointer_x, FlowLayout.LEFT);
        drawingBoardFooterPanel.add(pointer_x_value, FlowLayout.LEFT);
        drawingBoardFooterPanel.add(pointer_y, FlowLayout.LEFT);
        drawingBoardFooterPanel.add(pointer_y_value, FlowLayout.LEFT);

        drawingBoardFooterPanel.add(undo);
        drawingBoardFooterPanel.add(redo);
        drawingBoardFooterPanel.add(symatric);
    }

    void updateButtons(){
        undo.setEnabled(manager.canUndo());
        redo.setEnabled(manager.canRedo());
    }

    public JPanel getDrawingBoardFooter(){
        return drawingBoardFooterPanel;
    }

    public void actionPerformed(ActionEvent ae){
        AbstractButton abstractButton = (AbstractButton) ae.getSource();
        boolean selected = abstractButton.getModel().isSelected();
        if(selected){
            System.out.println("Selected");
            svgc.SymatricOn();
        }
        else{
            System.out.println("Not Selected");
            svgc.SymatricOff();
        }
    }
}
