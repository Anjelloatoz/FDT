package Design_tool_prototype1;

/**
 *
 * @author Anjello
 */

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Robot;
import java.awt.MouseInfo;

import org.w3c.dom.Element;

public class alterStation implements ChangeListener, ActionListener{
    JPanel alterPanel = new JPanel();
    Element element;
    JSlider resize_slide;
    JLabel coordinates = new JLabel("X:___ Y:___");
    JTextArea x_coord = new JTextArea("00");
    JTextArea y_coord = new JTextArea("00");
    JButton ok = new JButton("OK");
    SVGConjurer svgc;
    ribbonTest rt;
    alterStation(){
        alterPanel.setLayout(new FlowLayout());
        Color cl = new Color(255,249,181);
//        alterPanel.setBackground(cl);
        resize_slide = new JSlider();
        resize_slide.setBackground(cl);
        resize_slide.setMajorTickSpacing(10);
        resize_slide.setMinorTickSpacing(1);
        resize_slide.setPaintTicks(true);

        resize_slide.addChangeListener(this);
        x_coord.setSize(40, 28);
        y_coord.setSize(40, 28);
        alterPanel.add(resize_slide);
        alterPanel.add(coordinates);
        alterPanel.add(x_coord);
        alterPanel.add(y_coord);
        alterPanel.add(ok);
        ok.addActionListener(this);
        alterPanel.setVisible(true);
    }

    public void setElement(Element e){
        System.out.println("in the setElement");
        this.element = e;
      //  PathLength pl = new PathLength(element);
        System.out.println("Path length is "+element.getAttribute("pathLength"));

    }

    public void setCoordinates(double x, double y){
        coordinates.setText("X:"+x+" Y:"+y);
    }

    public JPanel getAlterStation(){
        return alterPanel;
    }

    public void stateChanged(ChangeEvent e) {
        JSlider slider=(JSlider)e.getSource();
//            element.setAttribute("pathLength", ""+slider.getValue());
//            System.out.println(slider.getValue());
            svgc.resizer(slider.getValue());
      }
    public void actionPerformed(ActionEvent ae){
        if(ae.getSource().equals(ok)){
            int mouse_pointX = MouseInfo.getPointerInfo().getLocation().x;
            int mouse_pointY = MouseInfo.getPointerInfo().getLocation().y;
            try{
                Robot robot = new Robot();
                robot.mouseMove((int)rt.iframe.getContentPane().getLocationOnScreen().getX()+Integer.parseInt(x_coord.getText()),(int)rt.iframe.getContentPane().getLocationOnScreen().getY()+Integer.parseInt(y_coord.getText()));
                robot.mousePress(InputEvent.BUTTON1_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
                robot.mouseMove(mouse_pointX, mouse_pointY);
            }
            catch(Exception e){

            }
        }
    }

}
