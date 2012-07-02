package Design_tool_prototype1;

import java.util.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.Toolkit;
import java.io.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.*;
import java.io.File;
import javax.imageio.*;

import org.jvnet.flamingo.ribbon.*;
import org.jvnet.flamingo.ribbon.RibbonTask;
import org.jvnet.flamingo.ribbon.resize.*;
import org.jvnet.flamingo.common.JCommandButton;
import org.jvnet.flamingo.ribbon.ui.JRibbonGallery;
import org.jvnet.flamingo.common.icon.DecoratedResizableIcon;
import org.jvnet.flamingo.common.icon.EmptyResizableIcon;
import org.jvnet.flamingo.common.JCommandToggleButton;
import org.jvnet.flamingo.common.StringValuePair;
import org.jvnet.flamingo.common.JCommandButton.CommandButtonKind;
import javax.swing.*;
import org.jvnet.flamingo.common.*;
import org.jvnet.flamingo.common.JCommandButton.CommandButtonKind;
import org.jvnet.flamingo.common.popup.*;
import org.jvnet.flamingo.ribbon.*;
import org.jvnet.flamingo.common.icon.EmptyResizableIcon;
import org.jvnet.flamingo.common.popup.JPopupPanel;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.imageio.*;
import java.io.*;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jvnet.flamingo.common.*;
import org.jvnet.flamingo.ribbon.*;
import org.jvnet.flamingo.common.icon.EmptyResizableIcon;
import org.jvnet.flamingo.common.JCommandButton.CommandButtonKind;
import org.jvnet.flamingo.ribbon.ui.JRibbonGallery;
import org.jvnet.flamingo.common.icon.DecoratedResizableIcon;

public class ribbonTest extends JRibbonFrame implements ChangeListener, ActionListener{
    JDesktopPane desk;
    JInternalFrame iframe;
    JInternalFrame toolframe;
    JInternalFrame color_frame;
    JInternalFrame alter_frame;
    JInternalFrame control_frame;
    JInternalFrame navigator_frame;
    JCommandToggleButton[] buttons;
    JCommandToggleButton[] button_buttons;
    JFrame frame;
    JButton line_bt;
    paintBoard pb;
    SVGConjurer svgc;
    alterStation alt;
    ControlPanel ctrlp;
    navigator nv = new navigator();
    JColorChooser colorChooser;
    JRibbonGallery gallery;
    final static String GALLERY_NAME = "Gallery";
    final static String BUTTON_GALLERY_NAME = "Button Gallery";
    JRibbonBand band;
    JRibbonBand button_gallery_band;
    JRibbonBand dress_form_band;
    JRibbonBand button_band;
    JRibbonBand button_button_band;
    JRibbonBand dress_form_button_band;
    JRibbonBand buttons_button_band;

    Image image;
    java.util.List<Fabric> fabrics_list = new ArrayList<Fabric>();
    java.util.List<button> buttons_list = new ArrayList<button>();

    private static class TopLeftDecoration implements DecoratedResizableIcon.IconDecorator {
        int number;

        public TopLeftDecoration(int number){
            this.number = number;
        }

	@Override
	public void paintIconDecoration(Component c, Graphics g, int x, int y, int width, int height){
            g.setColor(Color.blue.darker());g.drawString("" + number, x + 2, y + g.getFontMetrics().getAscent());
        }
    }

    private static class BottomRightDecoration implements DecoratedResizableIcon.IconDecorator {
        int number;
        
        public BottomRightDecoration(int number){
            this.number = number;
        }

        @Override
	public void paintIconDecoration(Component c, Graphics g, int x, int y, int width, int height){}
    }

    private static class IconImageDecoration implements DecoratedResizableIcon.IconDecorator {
        Image icon_image;

        public IconImageDecoration(Image icon_image){
            this.icon_image = icon_image;
        }

	@Override
	public void paintIconDecoration(Component c, Graphics g, int x, int y, int width, int height){
            g.drawImage(icon_image, 0, 0, null);
        }
    }

    ribbonTest(){
        super("Anjelloatoz Clothing Fashion Designer");
        Image frame_image;

        try{
        File frame_icon = new File("curve_bt.gif");
            frame_image = ImageIO.read(frame_icon);
            frame.setIconImage(frame_image);
        }
        catch(Exception e){
        System.out.println("frame icon error: "+e);
        }

        try{
            ObjectInputStream objIn = new ObjectInputStream(new BufferedInputStream(new FileInputStream("fabrics.dat")));
            fabrics_list = (ArrayList)(objIn.readObject());
            objIn.close();
        }
        catch(Exception e){
            System.out.println("Fabrics list import error "+e);
        }

        try{
            ObjectInputStream objIn = new ObjectInputStream(new BufferedInputStream(new FileInputStream("buttons.dat")));
            buttons_list = (ArrayList)(objIn.readObject());
            objIn.close();
        }
        catch(Exception e){
            System.out.println("Buttons list import error "+e);
        }

        RibbonApplicationMenuEntryPrimary amEntryNew = new RibbonApplicationMenuEntryPrimary(new EmptyResizableIcon(32), "New", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                System.out.println("Invoked creating new document");
            }
        }, CommandButtonKind.ACTION_ONLY);

        RibbonApplicationMenu applicationMenu = new RibbonApplicationMenu();
	applicationMenu.addMenuEntry(amEntryNew);

        applicationMenu.setDefaultCallback(new RibbonApplicationMenuEntryPrimary.PrimaryRolloverCallback(){
            @Override
            public void menuEntryActivated(JPanel targetPanel){
                targetPanel.removeAll();
                JCommandButtonPanel openHistoryPanel = new JCommandButtonPanel(CommandButtonDisplayState.MEDIUM);
                String groupName = "Default Documents";
                openHistoryPanel.addButtonGroup(groupName);

                for (int i = 0; i < 5; i++) {
                    JCommandButton historyButton = new JCommandButton(i+ "    " + "default" + i + ".html", new EmptyResizableIcon(32));
                    historyButton.setHorizontalAlignment(SwingUtilities.LEFT);
                    openHistoryPanel.addButtonToLastGroup(historyButton);
                }
                
                openHistoryPanel.setMaxButtonColumns(1);
                targetPanel.setLayout(new BorderLayout());
                targetPanel.add(openHistoryPanel, BorderLayout.CENTER);
            }
        });

        this.getRibbon().setApplicationMenu(applicationMenu);
        buttons = new JCommandToggleButton[fabrics_list.size()];
        button_buttons = new JCommandToggleButton[buttons_list.size()];

        for(int j = 0; j < fabrics_list.size(); j++){
            try{
                File image_file = new File(fabrics_list.get(j).getFabricIcon());
                image = ImageIO.read(image_file);
            }
            
            catch(Exception e){
                System.out.println("RibbonTest class - RibbonTest()Constructor - Fabric ImageIO problem: "+e);
            }

            DecoratedResizableIcon dri = new DecoratedResizableIcon(new DisabledResizableIcon(RibbonElementPriority.TOP, 52, 52), new IconImageDecoration(image), new BottomRightDecoration(j));
            JCommandToggleButton jtb  = new JCommandToggleButton("Texture "  + j, dri);
            jtb.setName("fabric");
            jtb.addActionListener(this);
            this.buttons[j] = jtb;
        }

        for(int j = 0; j < buttons_list.size(); j++){
            try{
                File image_file = new File(buttons_list.get(j).getbuttonIcon());
                image = ImageIO.read(image_file);
            }

            catch(Exception e){
                System.out.println("RibbonTest class - RibbonTest()Constructor - Button ImageIO problem: "+e);
            }

            java.awt.Image icon_image = image.getScaledInstance(55, 55, 10);
            DecoratedResizableIcon dri = new DecoratedResizableIcon(new DisabledResizableIcon(RibbonElementPriority.TOP, 52, 52), new IconImageDecoration(icon_image), new BottomRightDecoration(j));
            JCommandToggleButton button_tb  = new JCommandToggleButton("Button "  + j, dri);
            button_tb.setName("button");
            button_tb.addActionListener(this);
            this.button_buttons[j] = button_tb;
        }

        band = new JRibbonBand("Texture Gallery", new EmptyResizableIcon(32));
        button_band = new JRibbonBand("Import", new EmptyResizableIcon(32));
        dress_form_button_band = new JRibbonBand("Dress Form Controls", new EmptyResizableIcon(32));
        buttons_button_band = new JRibbonBand("Button Controls", new EmptyResizableIcon(32));

        button_gallery_band = new JRibbonBand("Button Gallery", new EmptyResizableIcon(32));
        button_button_band = new JRibbonBand("Buttons Control", new EmptyResizableIcon(32));

        Map<RibbonElementPriority, Integer> visibleButtonCounts = new HashMap<RibbonElementPriority, Integer>();
        visibleButtonCounts.put(RibbonElementPriority.LOW, 4);
	visibleButtonCounts.put(RibbonElementPriority.MEDIUM, 5);
	visibleButtonCounts.put(RibbonElementPriority.TOP, 6);

        java.util.List<StringValuePair<java.util.List<JCommandToggleButton>>> galleryButtons = new ArrayList<StringValuePair<java.util.List<JCommandToggleButton>>>();
	java.util.List<JCommandToggleButton> galleryButtonsList = new ArrayList<JCommandToggleButton>();

        for (int j = 0; j < fabrics_list.size(); j++){
            galleryButtonsList.add(this.buttons[j]);
        }

        java.util.List<StringValuePair<java.util.List<JCommandToggleButton>>> button_galleryButtons = new ArrayList<StringValuePair<java.util.List<JCommandToggleButton>>>();
	java.util.List<JCommandToggleButton> button_galleryButtonsList = new ArrayList<JCommandToggleButton>();

        for (int j = 0; j < buttons_list.size(); j++){
            button_galleryButtonsList.add(this.button_buttons[j]);
        }

        galleryButtons.add(new StringValuePair<java.util.List<JCommandToggleButton>>("Group " , galleryButtonsList));
        band.addRibbonGallery(GALLERY_NAME, galleryButtons, visibleButtonCounts, 6, 4, RibbonElementPriority.TOP);

        button_galleryButtons.add(new StringValuePair<java.util.List<JCommandToggleButton>>("Button Group" , button_galleryButtonsList));
        button_gallery_band.addRibbonGallery(BUTTON_GALLERY_NAME, button_galleryButtons, visibleButtonCounts, 6, 4, RibbonElementPriority.TOP);

        JCommandButton import_bt = new JCommandButton("Import");
        import_bt.addActionListener(this);

        JCommandButton print = new JCommandButton("Print");
        print.addActionListener(this);

        button_band.addCommandButton(print, RibbonElementPriority.MEDIUM);

        button_band.addCommandButton(import_bt, RibbonElementPriority.MEDIUM);
        button_band.addCommandButton(new JCommandButton("Fill", new EmptyResizableIcon(32)),
        RibbonElementPriority.MEDIUM);
        button_band.addCommandButton(new JCommandButton("Import", new EmptyResizableIcon(32)),
        RibbonElementPriority.MEDIUM);
        button_band.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesRestrictive(band));

        JCommandButton import_img = new JCommandButton("Import Guide Image", new EmptyResizableIcon(32));
        import_img.addActionListener(this);

        JCommandButton import_button = new JCommandButton("Import Button", new EmptyResizableIcon(32));
        import_button.addActionListener(this);

        dress_form_button_band.addCommandButton(import_img, RibbonElementPriority.MEDIUM);
        dress_form_button_band.addCommandButton(new JCommandButton("test2", new EmptyResizableIcon(32)),
        RibbonElementPriority.MEDIUM);
        dress_form_button_band.addCommandButton(new JCommandButton("test3", new EmptyResizableIcon(32)),
        RibbonElementPriority.MEDIUM);
        dress_form_button_band.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesRestrictive(band));
        buttons_button_band.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesRestrictive(band));

//        JCommandButton import_button = new JCommandButton("Import Button", new EmptyResizableIcon(32));
//        import_img.addActionListener(this);
        buttons_button_band.addCommandButton(import_button, RibbonElementPriority.MEDIUM);

        gallery = band.getControlPanel().getRibbonGallery(GALLERY_NAME);
        this.getRibbon().addTask(new RibbonTask("Texture", button_band, band));
        this.getRibbon().addTask(new RibbonTask("Dress Form", dress_form_button_band));
        this.getRibbon().addTask(new RibbonTask("Buttons", buttons_button_band, button_gallery_band));

        desk = new JDesktopPane();
        iframe = new JInternalFrame("Drawing Board", true, true, true, true);
        iframe.setIconifiable(false);
        iframe.setToolTipText("Your drawings go here");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        iframe.setBounds(0, 0, dim.width-400, dim.height-100);
        iframe.setVisible(true);
                                
        alt = new alterStation();
        toolframe = new JInternalFrame("Tool Board", true, true, true, true);
        toolframe.setToolTipText("Drawing tools");
        toolframe.setBounds(dim.width-350, dim.height-250, 350, 80);
        toolframe.setVisible(true);

        pb = new paintBoard();
        svgc = new SVGConjurer(dim, alt);
        svgc.rt = this;
        alt.svgc = svgc;
        alt.rt = this;
        pb.alt = alt;
        pb.nv = nv;
        toolbox tb = new toolbox(svgc);
        toolframe.add(tb.getToolbox());
        colorChooser = new JColorChooser();
        ColorSelectionModel model = colorChooser.getSelectionModel();
        model.addChangeListener(this);

        color_frame = new JInternalFrame("Color Pallete", true, true, true, true);
        color_frame.setToolTipText("Color Pallete");
        color_frame.setBounds(dim.width-400, 0, 400, 380);
        color_frame.add(colorChooser);
        color_frame.setVisible(true);

        alter_frame = new JInternalFrame("Alter Station", true, true, true, true);
        alter_frame.setToolTipText("Selected element altering tools");
        alter_frame.setBounds(dim.width-400, dim.height-380, 400, 200);
        alter_frame.add(alt.getAlterStation());
        alter_frame.setVisible(true);

        ctrlp = new ControlPanel();
        control_frame = new JInternalFrame("Control Panel", true, true, true, true);
        control_frame.setToolTipText("Selected element altering tools");
        control_frame.setBounds(dim.width-400, dim.height-380, 400, 200);
        control_frame.add(ctrlp.getControlPanel());
        control_frame.setVisible(true);
        ctrlp.svgc = svgc;

        navigator_frame = new JInternalFrame("Shape Navigator", true, true, true, true);
        navigator_frame.setToolTipText("Created shapes are lined up here");
        navigator_frame.setBounds(0, dim.height-380, 200, 190);
        navigator_frame.add(nv.getNavigator());
        navigator_frame.setVisible(true);
        iframe.add(svgc.getBoard());

        desk.add(iframe);
        desk.add(color_frame);
        desk.add(alter_frame);
        desk.add(navigator_frame);
        desk.add(control_frame);
        desk.add(toolframe);

        this.add(desk);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setSize(dim);
        this.setVisible(true);
    }

    public void stateChanged(ChangeEvent e) {
        svgc.color = colorChooser.getColor();
      }

    public void addFabric(String new_fabric[]){
        System.out.println("addFabric(String new_fabric[]) Called");
        Fabric fabric = new Fabric(new_fabric[0], new_fabric[1], new_fabric[2], new_fabric[3], new_fabric[4]);
        addFabric(fabric);
    }

    public void addButton(String new_button[]){
        System.out.println("addButton(String new_button[]) Called");
        button btn = new button(new_button[0], new_button[1], new_button[2], new_button[3], new_button[4]);
        addButton(btn);
    }

    public void actionPerformed(ActionEvent ae){
        try{
        JCommandButton jcb = (JCommandButton)ae.getSource();

        if(jcb.getText().equals("Import")){
            String new_fabric[] = new String[5];
            ImportScreen imp_scr = new ImportScreen(this, "Fabric", new_fabric);
        }

        else if(jcb.getText().equals("Import Guide Image")){
            File f = openFile("");
            java.awt.image.BufferedImage Image = javax.imageio.ImageIO.read(f);
            int h = Image.getHeight();
            int w = Image.getWidth();
            org.w3c.dom.Element e = svgc.setGuideImage(f.toURI().toString(), w, h);
            java.awt.Image image = Toolkit.getDefaultToolkit().createImage(Image.getSource());
            java.awt.Image icon_image = image.getScaledInstance(35, 35, 10);
            ctrlp.addNewLayer(icon_image, f.getName(), e);
        }

        else if(jcb.getText().equals("Import Button")){
            String new_button[] = new String[5];
            ImportScreen imp_scr = new ImportScreen(this, "Button", new_button);
        }
        else if(jcb.getText().equals("Print")){
            svgc.filePrinter();
        }
        }
        catch(Exception e){
            JCommandToggleButton jtb = (JCommandToggleButton)ae.getSource();
            System.out.println("The name is "+jtb.getName());
            String tmp[] = jtb.getText().split("\\s");

            if(jtb.getName().equals("fabric")){
                svgc.fillUri = fabrics_list.get(Integer.parseInt(tmp[1])).getFabricMainImage().toString();
                svgc.fillPattern();
                System.out.println(svgc.fillUri.toString());
            }
            else if(jtb.getName().equals("button")){
                svgc.setButton(buttons_list.get(Integer.parseInt(tmp[1])).getbuttonMainImage());
            }
        }
    }

    private File openFile(String extension) {
    FileFilter filter = new FileNameExtensionFilter("Image Files", "JPEG", "JPG", "bmp", "gif", "png");
      JFileChooser jfc = new JFileChooser();
      jfc.addChoosableFileFilter(filter);

      int result = jfc.showOpenDialog(this);
      if(result == JFileChooser.CANCEL_OPTION) return null;
      try {
          return(jfc.getSelectedFile());
      }

      catch (Exception e) {
         JOptionPane.showMessageDialog(this,e.getMessage(),
         "File error",JOptionPane.ERROR_MESSAGE);
      return null;}
   }

    public void addFabric(Fabric fabric){
        System.out.println("addFabric(Fabric fabric) called");
        fabrics_list.add(fabric);
        System.out.println(fabrics_list.size()+" fabrics in the list.");
        fileWriter("fabric");

        try{
            File image_file = new File(fabric.getFabricIcon());
            image = ImageIO.read(image_file);
        }

        catch(Exception e){
            System.out.println("RibbonTest class - addFabric(Fabric fabric) Creating image from fabric file error: "+e);
        }

        DecoratedResizableIcon dri = new DecoratedResizableIcon(new DisabledResizableIcon(RibbonElementPriority.TOP, 52, 52), new IconImageDecoration(image), new BottomRightDecoration(3));
        JCommandToggleButton jtb  = new JCommandToggleButton("Texture "  + 3, dri);
        jtb.addActionListener(this);
        band.addRibbonGalleryButtons(GALLERY_NAME, "Group ", jtb);
    }

    public void addButton(button btn){
        System.out.println("addFabric(Fabric fabric) called");
        buttons_list.add(btn);
        System.out.println(buttons_list.size()+" buttons in the list.");
        fileWriter("button");

        try{
            File image_file = new File(btn.getbuttonIcon());
            image = ImageIO.read(image_file);
        }

        catch(Exception e){
            System.out.println("RibbonTest class - addButton(button Btn) Creating image from fabric file error: "+e);
        }

        DecoratedResizableIcon dri = new DecoratedResizableIcon(new DisabledResizableIcon(RibbonElementPriority.TOP, 52, 52), new IconImageDecoration(image), new BottomRightDecoration(3));
        JCommandToggleButton jtb  = new JCommandToggleButton("Button "  + 3, dri);
        jtb.addActionListener(this);
        button_gallery_band.addRibbonGalleryButtons(BUTTON_GALLERY_NAME, "Button Group", jtb);
    }


    public void fileWriter(String file){
    try{
        if(file.equals("fabric")){
            ObjectOutputStream objOut = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("fabrics.dat")));
            objOut.writeObject(fabrics_list);
            objOut.close();
        }

        else if(file.equals("button")){
            ObjectOutputStream objOut = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("buttons.dat")));
            objOut.writeObject(buttons_list);
            objOut.close();
        }
    }

    catch(Exception e){
        System.out.println("Line 486: Exception "+e);
    }
}
public static void main(String args[])throws Exception{
        JRibbonFrame.setDefaultLookAndFeelDecorated(true);
        SwingUtilities.invokeLater(new Runnable() {
        public void run() {

        try{
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceRavenLookAndFeel");
        ribbonTest rt = new ribbonTest();
        }

        catch(Exception e){
            System.out.println("Look and feel error "+e);
        }
    }
    });
}
}
