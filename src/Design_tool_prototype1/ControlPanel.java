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
import javax.swing.border.*;
import java.util.HashMap;

public class ControlPanel implements ActionListener, ChangeListener{
    JPanel controlPanel = new JPanel();
    JTabbedPane image_pane;
    JPanel layers_panel;
    JPanel options_panel;
    java.util.List<org.w3c.dom.Element> image_elements= new java.util.ArrayList();
    SVGConjurer svgc;
    int img_count = 0;
    HashMap img_map = new HashMap();
    JSlider opaque_slide = new JSlider();

    ControlPanel(){
        TitledBorder guide_image_border = new TitledBorder("Guide Images");
        controlPanel.setBorder(guide_image_border);
        controlPanel.setLayout(new BorderLayout());
        JPanel image_contents_panel = new JPanel(new BorderLayout());
        image_pane = new JTabbedPane();
        layers_panel = new JPanel();
        layers_panel.setLayout(new BoxLayout(layers_panel, BoxLayout.Y_AXIS));
        image_pane.addTab("Layer", layers_panel);
        options_panel = new JPanel();
        image_pane.addTab("Properties", new JPanel());
        image_pane.addTab("Options", options_panel);
        image_contents_panel.add(image_pane, BorderLayout.CENTER);
        controlPanel.add(image_contents_panel, BorderLayout.NORTH);

        opaque_slide.setMajorTickSpacing(10);
        opaque_slide.setMinorTickSpacing(1);
        opaque_slide.setPaintTicks(true);
        opaque_slide.setMinimum(0);
        opaque_slide.setMaximum(100);
        opaque_slide.addChangeListener(this);
        options_panel.add(opaque_slide);
    }

    public JPanel getControlPanel(){
        return controlPanel;
    }

    public void addNewLayer(java.awt.Image icon_image, String image_name, org.w3c.dom.Element e){
        ImageIcon icn = new ImageIcon();
        icn.setImage(icon_image);
        JButton icon_button = new JButton(icn);
        icon_button.setName("Icon Button");
        icon_button.addActionListener(this);
        icon_button.setActionCommand("image_"+img_count++);
        JPanel new_layer = new JPanel();
        new_layer.setLayout(new BoxLayout(new_layer, BoxLayout.X_AXIS));
        new_layer.add(icon_button);
        new_layer.add(Box.createRigidArea(new Dimension(15,0)));
        JButton move = new JButton("Move Up");
        move.addActionListener(this);
        new_layer.add(move);
        new_layer.add(Box.createRigidArea(new Dimension(15,0)));
        JButton remove = new JButton("Delete");
        remove.setActionCommand("image_"+img_count++);
        remove.addActionListener(this);
        img_map.put(remove.getActionCommand(), e);
        new_layer.add(remove);
        new_layer.add(Box.createRigidArea(new Dimension(15,0)));
        new_layer.add(new JLabel(image_name));
        new_layer.setAlignmentX(Component.LEFT_ALIGNMENT);
        layers_panel.add(new_layer,0);
        layers_panel.add(new JSeparator(SwingConstants.HORIZONTAL),1);
        layers_panel.add(Box.createVerticalStrut(5));

        image_elements.add(e);
        controlPanel.repaint();
    }

    public void actionPerformed(ActionEvent ae){
        if(((JButton)ae.getSource()).getText().equals("Delete")){
            svgc.DeleteGuideImage((org.w3c.dom.Element)img_map.remove(ae.getActionCommand()));
            layers_panel.remove(((JButton)ae.getSource()).getParent());
            layers_panel.repaint();
            img_count--;
        }

        else if((((JButton)ae.getSource()).getName()).equals("Icon Button")){
            String opacity = svgc.getOpacity();
            opaque_slide.setValue((int)((Double.parseDouble(opacity))*100));
        }
    }
    public void stateChanged(ChangeEvent e) {
        JSlider slider=(JSlider)e.getSource();
//            element.setAttribute("pathLength", ""+slider.getValue());
//            System.out.println(slider.getValue());
//            svgc.resizer(slider.getValue());
            svgc.setOpacity(((double)slider.getValue())/100);
      }
}
