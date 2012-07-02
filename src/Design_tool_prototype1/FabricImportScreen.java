package Design_tool_prototype1;

import javax.imageio.*;

import java.awt.*;
import java.awt.Toolkit;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;


import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FabricImportScreen extends JFrame implements ActionListener{
    JLabel f_name_short = new JLabel("Fabric Name Short: ");
    JLabel f_name_long = new JLabel("Fabric Name Long: ");
    JLabel f_type = new JLabel("Fabric Type: ");
    JLabel f_icon = new JLabel("Fabric Icon");
    JLabel f_main_image = new JLabel("Fabric Main Image");

    //App app;
    ribbonTest rt;

    JTextField f_name_short_bx = new JTextField();
    JTextField f_name_long_bx = new JTextField();
    JTextField f_type_bx = new JTextField();

    JButton browse_icon = new JButton("Browse Icon");
    JButton browse_image = new JButton("Browse Image");
    JButton ok = new JButton("OK");
    JButton cancel = new JButton("Cancel");

    JPanel icon_bx = new JPanel();
    JPanel image_bx = new JPanel();

    JLabel icon_label = new JLabel();
    JLabel image_label = new JLabel();

    TitledBorder jt = new TitledBorder("Fabric Details");

    Image icon;
    Image sample;
    File image_file;
    File icon_file;

    Graphics gh;

    FabricImportScreen(ribbonTest rt){
        super("Fabric Import Screen");

        this.rt = rt;

        Dimension icon_bx_dm = new Dimension(50,50);
        Dimension image_bx_dm = new Dimension(300,300);

        this.setLayout(new BorderLayout());
        icon_bx.setBounds(0, 0, 50, 50);
        icon_bx.setMinimumSize(icon_bx_dm);
        icon_bx.setMaximumSize(icon_bx_dm);
        icon_bx.setBackground(Color.lightGray);
        image_bx.setBounds(0, 0, 800, 500);
        image_bx.setBackground(Color.lightGray);
        icon_label.setSize(50, 50);
        icon_label.setMaximumSize(icon_bx_dm);
        icon_label.setPreferredSize(icon_bx_dm);
        image_label.setSize(300, 300);
        image_label.setMaximumSize(image_bx_dm);
        icon_bx.add(icon_label);
        image_bx.add(image_label);


        image_bx.setMinimumSize(image_bx_dm);
        image_bx.setMaximumSize(image_bx_dm);
        gh = image_bx.getGraphics();

        JPanel detail_panel = new JPanel(new GridLayout(3,2,15,10));
        detail_panel.add(f_name_short);
        detail_panel.add(f_name_short_bx);
        detail_panel.add(f_name_long);
        detail_panel.add(f_name_long_bx);
        detail_panel.add(f_type);
        detail_panel.add(f_type_bx);

        ok.addActionListener(this);
        cancel.addActionListener(this);
        browse_icon.addActionListener(this);
        browse_image.addActionListener(this);


        TitledBorder detail_border = new TitledBorder("Fabric Details");
        JPanel tmp_panel = new JPanel(new BorderLayout());
        tmp_panel.add(detail_panel, BorderLayout.NORTH);
        detail_panel.setBorder(detail_border);

        Box icon_box = new Box(BoxLayout.Y_AXIS);

        JPanel icon_panel = new JPanel(new BorderLayout());
        icon_box.add(f_icon);
        icon_box.add(Box.createVerticalStrut(10));
        icon_box.add(icon_bx);
        icon_box.add(Box.createVerticalStrut(10));
        icon_box.add(browse_icon);
        TitledBorder icon_border = new TitledBorder("Icon Preview");
        icon_box.setBorder(icon_border);
        icon_box.setMaximumSize(new Dimension(100,100));
//        icon_panel.add(icon_box);
        tmp_panel.add(icon_box, BorderLayout.CENTER);
//        tmp_panel.add(Box.createVerticalStrut(60),BorderLayout.SOUTH);

        JPanel button_panel = new JPanel(new FlowLayout());
        button_panel.add(ok);
        button_panel.add(cancel);
        tmp_panel.add(button_panel, BorderLayout.SOUTH);

        JPanel image_main_panel = new JPanel(new FlowLayout());
//        image_main_panel.add();
        image_main_panel.add(f_main_image);
        image_main_panel.add(browse_image);
        JPanel tmp_panel2 = new JPanel(new BorderLayout());
        tmp_panel2.add(image_main_panel, BorderLayout.NORTH);
        TitledBorder image_bx_border = new TitledBorder("Sample Preview");
        image_bx.setBorder(image_bx_border);
        tmp_panel2.add(image_bx, BorderLayout.CENTER);

        this.add(tmp_panel, BorderLayout.WEST);
        this.add(tmp_panel2, BorderLayout.CENTER);
        this.setBounds((int)((Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2)-300), (int)((Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2)-175), 600, 350);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent ae){
        if(ae.getActionCommand().equals("Cancel")){
            this.setVisible(false);
        }

        if(ae.getActionCommand().equals("Browse Icon")){
            FileFilter filter = new FileNameExtensionFilter("Portable Network Graphics image(*.PNG)", "png");
      JFileChooser jfc = new JFileChooser();
      jfc.addChoosableFileFilter(filter);

      int result = jfc.showOpenDialog(this);
      if(result == JFileChooser.CANCEL_OPTION) return;
      try {
          //return(jfc.getSelectedFile());
          icon_file = jfc.getSelectedFile();
          icon = ImageIO.read(icon_file);
          ImageIcon img_small = new ImageIcon(icon);

          icon_label.setIcon(img_small);
      }

      catch (Exception e) {
         JOptionPane.showMessageDialog(this,e.getMessage(),
         "File error",JOptionPane.ERROR_MESSAGE);
      return;}
        }

        if(ae.getActionCommand().equals("Browse Image")){
            FileFilter filter = new FileNameExtensionFilter("Portable Network Graphics image(*.PNG)", "png");
      JFileChooser jfc = new JFileChooser();
      jfc.addChoosableFileFilter(filter);

      int result = jfc.showOpenDialog(this);
      if(result == JFileChooser.CANCEL_OPTION) return;
      try {
          image_file = jfc.getSelectedFile();
          sample = ImageIO.read(image_file);
          ImageIcon img_large = new ImageIcon(sample);
          image_label.setIconTextGap(0);
          image_label.setIcon(img_large);
          icon_label.setIcon(img_large);
          String tmp[] = image_file.getName().split("\\.");
//          System.out.println(tmp[0]);
          f_name_short_bx.setText(tmp[0]);
          f_name_long_bx.setText(tmp[0]);
          if(icon_file==null)icon_file = image_file;
          
      }

      catch (Exception e) {
         JOptionPane.showMessageDialog(this,e.getMessage(),
         "File error",JOptionPane.ERROR_MESSAGE);
      return;}
        }

        if(ae.getActionCommand().equals("OK")){
//*            Fabric fabric = new Fabric(f_name_short_bx.getText(), f_name_long_bx.getText(), f_type_bx.getText(), icon_file.toURI(), image_file.toURI());
//*            rt.addFabric(fabric);
            icon_file = null;
        }
    }

    public static void main(String args[]){
//        FabricImportScreen fis = new FabricImportScreen();
    }
}