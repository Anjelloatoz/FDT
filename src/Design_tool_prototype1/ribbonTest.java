package Design_tool_prototype1;

import javax.swing.event.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.awt.geom.Rectangle2D;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Toolkit;
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

import bibliothek.gui.DockController;
import bibliothek.gui.dock.DefaultDockable;
import bibliothek.gui.dock.SplitDockStation;
import bibliothek.gui.dock.station.split.SplitDockGrid;
import bibliothek.extension.gui.dock.theme.BubbleTheme;
import bibliothek.extension.gui.dock.theme.EclipseTheme;
import bibliothek.gui.dock.themes.NoStackTheme;

public class ribbonTest extends JRibbonFrame implements ChangeListener, ActionListener{
    JDesktopPane desk;
    JInternalFrame iframe;
    JInternalFrame toolframe;
    JInternalFrame color_frame;
    JInternalFrame alter_frame;
    JInternalFrame control_frame;
    JInternalFrame navigator_frame;

    JTabbedPane drawing_board_tabbs = new JTabbedPane();
    Container drawing_container;
    JPanel bordfooter;

    DockController controller = new DockController();
    DockController default_controller = new DockController();
    SplitDockStation station = new SplitDockStation();
    DefaultDockable drawing_dock = new DefaultDockable();
    DefaultDockable color_dock = new DefaultDockable();
    DefaultDockable alter_dock = new DefaultDockable();
    DefaultDockable navigator_dock = new DefaultDockable();
    DefaultDockable control_dock = new DefaultDockable();
    DefaultDockable tool_dock = new DefaultDockable();

    JCommandToggleButton[] buttons;
    JCommandToggleButton[] button_buttons;
    JFrame frame;
    JButton line_bt;
    paintBoard pb;
    SVGConjurer svgF;
    SVGConjurer svgR;
    alterStation alt;
    ControlPanel ctrlp;
    DrawingBoardFooter dbf;
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

    private DrawingBoardRule columnView;
    private DrawingBoardRule rowView;

    private static int splashProgress = 0;

    Image image;
    java.util.List<Fabric> fabrics_list = new ArrayList<Fabric>();
    java.util.List<button> buttons_list = new ArrayList<button>();

    Container navigator_container;

    static SplashScreen mySplash;                   // instantiated by JVM we use it to get graphics
    static Graphics2D splashGraphics;               // graphics context for overlay of the splash image
    static Rectangle2D.Double splashTextArea;       // area where we draw the text
    static Rectangle2D.Double splashProgressArea;   // area where we draw the progress bar
    static Font font;

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
        increaseSplash();
splashText("Initializing GUI");
        controller.add(station);
        BubbleTheme bt = new BubbleTheme();
        controller.setTheme(new NoStackTheme(new EclipseTheme()));
        SplitDockGrid grid = new SplitDockGrid();
        increaseSplash();
        Image frame_image;
increaseSplash();
        try{
        File frame_icon = new File("curve_bt.gif");
            frame_image = ImageIO.read(frame_icon);
            frame.setIconImage(frame_image);
        }
        catch(Exception e){
        System.out.println("frame icon error: "+e);
        }
        increaseSplash();
increaseSplash();
        try{
            ObjectInputStream objIn = new ObjectInputStream(new BufferedInputStream(new FileInputStream("fabrics.dat")));
            fabrics_list = (ArrayList)(objIn.readObject());
            objIn.close();
        }
        catch(Exception e){
            System.out.println("Fabrics list import error "+e);
        }
        increaseSplash();
increaseSplash();
        try{
            ObjectInputStream objIn = new ObjectInputStream(new BufferedInputStream(new FileInputStream("buttons.dat")));
            buttons_list = (ArrayList)(objIn.readObject());
            objIn.close();
        }
        catch(Exception e){
            System.out.println("Buttons list import error "+e);
        }
increaseSplash();
        RibbonApplicationMenuEntryPrimary amEntryNew = new RibbonApplicationMenuEntryPrimary(new EmptyResizableIcon(32), "New", this/*new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                System.out.println("Invoked creating new document");
            }
        }*/, CommandButtonKind.ACTION_ONLY);
increaseSplash();
        RibbonApplicationMenu applicationMenu = new RibbonApplicationMenu();
	applicationMenu.addMenuEntry(amEntryNew);
increaseSplash();
        applicationMenu.setDefaultCallback(new RibbonApplicationMenuEntryPrimary.PrimaryRolloverCallback(){
            @Override
            public void menuEntryActivated(JPanel targetPanel){
                targetPanel.removeAll();
                JCommandButtonPanel openHistoryPanel = new JCommandButtonPanel(CommandButtonDisplayState.MEDIUM);
                String groupName = "Default Documents";
                openHistoryPanel.addButtonGroup(groupName);
increaseSplash();
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
increaseSplash();
        this.getRibbon().setApplicationMenu(applicationMenu);
        buttons = new JCommandToggleButton[fabrics_list.size()];
        button_buttons = new JCommandToggleButton[buttons_list.size()];
increaseSplash();
splashText("Populating fabrics library");
for(int j = 0; j < fabrics_list.size(); j++){
            try{
                File image_file = new File(fabrics_list.get(j).getFabricIcon());
                image = ImageIO.read(image_file);
            }
            
            catch(Exception e){
                System.out.println("RibbonTest class - RibbonTest()Constructor - Fabric ImageIO problem: "+e);
            }
increaseSplash();
            DecoratedResizableIcon dri = new DecoratedResizableIcon(new DisabledResizableIcon(RibbonElementPriority.TOP, 52, 52), new IconImageDecoration(image), new BottomRightDecoration(j));
            JCommandToggleButton jtb  = new JCommandToggleButton("Texture "  + j, dri);
            jtb.setName("fabric");
increaseSplash();
            jtb.addActionListener(this);
            this.buttons[j] = jtb;
        }
increaseSplash();
splashText("Populating buttons library");
        for(int j = 0; j < buttons_list.size(); j++){
            try{
                File image_file = new File(buttons_list.get(j).getbuttonIcon());
                image = ImageIO.read(image_file);
            }

            catch(Exception e){
                System.out.println("RibbonTest class - RibbonTest()Constructor - Button ImageIO problem: "+e);
//                System.out.println(buttons_list.get(j).getbuttonIcon());
            }
increaseSplash();
            java.awt.Image icon_image = image.getScaledInstance(55, 55, 10);
            DecoratedResizableIcon dri = new DecoratedResizableIcon(new DisabledResizableIcon(RibbonElementPriority.TOP, 52, 52), new IconImageDecoration(icon_image), new BottomRightDecoration(j));
            JCommandToggleButton button_tb  = new JCommandToggleButton("Button "  + j, dri);
increaseSplash();
            button_tb.setName("button");
            button_tb.addActionListener(this);
            this.button_buttons[j] = button_tb;
        }
increaseSplash();
        band = new JRibbonBand("Texture Gallery", new EmptyResizableIcon(32));
        button_band = new JRibbonBand("Import", new EmptyResizableIcon(32));
        dress_form_button_band = new JRibbonBand("Dress Form Controls", new EmptyResizableIcon(32));
        buttons_button_band = new JRibbonBand("Button Controls", new EmptyResizableIcon(32));
increaseSplash();
        button_gallery_band = new JRibbonBand("Button Gallery", new EmptyResizableIcon(32));
        button_button_band = new JRibbonBand("Buttons Control", new EmptyResizableIcon(32));
increaseSplash();
        Map<RibbonElementPriority, Integer> visibleButtonCounts = new HashMap<RibbonElementPriority, Integer>();
        visibleButtonCounts.put(RibbonElementPriority.LOW, 4);
	visibleButtonCounts.put(RibbonElementPriority.MEDIUM, 5);
	visibleButtonCounts.put(RibbonElementPriority.TOP, 6);
increaseSplash();
        java.util.List<StringValuePair<java.util.List<JCommandToggleButton>>> galleryButtons = new ArrayList<StringValuePair<java.util.List<JCommandToggleButton>>>();
	java.util.List<JCommandToggleButton> galleryButtonsList = new ArrayList<JCommandToggleButton>();
increaseSplash();
        for (int j = 0; j < fabrics_list.size(); j++){
            galleryButtonsList.add(this.buttons[j]);
        }
increaseSplash();
        java.util.List<StringValuePair<java.util.List<JCommandToggleButton>>> button_galleryButtons = new ArrayList<StringValuePair<java.util.List<JCommandToggleButton>>>();
	java.util.List<JCommandToggleButton> button_galleryButtonsList = new ArrayList<JCommandToggleButton>();
increaseSplash();
        for (int j = 0; j < buttons_list.size(); j++){
            button_galleryButtonsList.add(this.button_buttons[j]);
        }
increaseSplash();
        galleryButtons.add(new StringValuePair<java.util.List<JCommandToggleButton>>("Group " , galleryButtonsList));
        band.addRibbonGallery(GALLERY_NAME, galleryButtons, visibleButtonCounts, 6, 4, RibbonElementPriority.TOP);
increaseSplash();
        button_galleryButtons.add(new StringValuePair<java.util.List<JCommandToggleButton>>("Button Group" , button_galleryButtonsList));
        button_gallery_band.addRibbonGallery(BUTTON_GALLERY_NAME, button_galleryButtons, visibleButtonCounts, 6, 4, RibbonElementPriority.TOP);
increaseSplash();
        JCommandButton import_bt = new JCommandButton("Import");
        import_bt.addActionListener(this);
increaseSplash();
        JCommandButton print = new JCommandButton("Print");
        print.addActionListener(this);
increaseSplash();
        button_band.addCommandButton(print, RibbonElementPriority.MEDIUM);
increaseSplash();
        button_band.addCommandButton(import_bt, RibbonElementPriority.MEDIUM);
        button_band.addCommandButton(new JCommandButton("Fill", new EmptyResizableIcon(32)),
        RibbonElementPriority.MEDIUM);
increaseSplash();
        button_band.addCommandButton(new JCommandButton("Import", new EmptyResizableIcon(32)),
        RibbonElementPriority.MEDIUM);
        button_band.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesRestrictive(band));
increaseSplash();
        JCommandButton import_img = new JCommandButton("Import Guide Image", new EmptyResizableIcon(32));
        import_img.addActionListener(this);
increaseSplash();
        JCommandButton import_button = new JCommandButton("Import Button", new EmptyResizableIcon(32));
        import_button.addActionListener(this);
increaseSplash();
splashText("Loading dress form controls");
        dress_form_button_band.addCommandButton(import_img, RibbonElementPriority.MEDIUM);
        dress_form_button_band.addCommandButton(new JCommandButton("test2", new EmptyResizableIcon(32)),
        RibbonElementPriority.MEDIUM);
increaseSplash();
        dress_form_button_band.addCommandButton(new JCommandButton("test3", new EmptyResizableIcon(32)),
        RibbonElementPriority.MEDIUM);
increaseSplash();
        dress_form_button_band.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesRestrictive(band));
        buttons_button_band.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesRestrictive(band));
increaseSplash();
//        JCommandButton import_button = new JCommandButton("Import Button", new EmptyResizableIcon(32));
//        import_img.addActionListener(this);
        buttons_button_band.addCommandButton(import_button, RibbonElementPriority.MEDIUM);
increaseSplash();
        gallery = band.getControlPanel().getRibbonGallery(GALLERY_NAME);
        this.getRibbon().addTask(new RibbonTask("Texture", button_band, band));
increaseSplash();
        this.getRibbon().addTask(new RibbonTask("Dress Form", dress_form_button_band));
        this.getRibbon().addTask(new RibbonTask("Buttons", buttons_button_band, button_gallery_band));
increaseSplash();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        dbf = new DrawingBoardFooter(svgF);
        bordfooter = dbf.getDrawingBoardFooter();
increaseSplash();
        desk = new JDesktopPane();
        iframe = new JInternalFrame("Drawing Board", true, true, true, true);
        iframe.setIconifiable(false);
increaseSplash();
        iframe.setToolTipText("Your drawings go here");
        
        iframe.setBounds(0, 0, dim.width-400, dim.height-200);
        iframe.setVisible(true);
increaseSplash();
splashText("Loading the alter station");
                                
        alt = new alterStation();
splashText("Loading the tool board");
        toolframe = new JInternalFrame("Tool Board", true, true, true, true);
        toolframe.setToolTipText("Drawing tools");
increaseSplash();
        toolframe.setBounds(dim.width-350, dim.height-250, 350, 80);
        toolframe.setVisible(true);
increaseSplash();
splashText("Loading the paint board");
        pb = new paintBoard();
increaseSplash();
splashText("Initializing drawing canvases");
        svgF = new SVGConjurer(dim, alt, dbf);
        svgR = new SVGConjurer(dim, alt, dbf);
        dbf.svgc = svgF;
increaseSplash();
        svgF.rt = this;
        svgR.rt = this;
        alt.svgc = svgF;
increaseSplash();
        alt.rt = this;
        pb.alt = alt;
        pb.nv = nv;
increaseSplash();
        toolbox tb = new toolbox(svgF);
        toolframe.add(tb.getToolbox());
splashText("Loading the color palette");
        colorChooser = new JColorChooser();
increaseSplash();
        ColorSelectionModel model = colorChooser.getSelectionModel();
        model.addChangeListener(this);
increaseSplash();
        color_frame = new JInternalFrame("Color Pallete", true, true, true, true);
        color_frame.setToolTipText("Color Pallete");
        color_frame.setBounds(dim.width-400, 0, 400, 380);
        color_frame.add(colorChooser);
increaseSplash();
        color_frame.setVisible(true);
increaseSplash();
        alter_frame = new JInternalFrame("Alter Station", true, true, true, true);
increaseSplash();
        alter_frame.setToolTipText("Selected element altering tools");
        alter_frame.setBounds(dim.width-400, dim.height-380, 400, 200);
increaseSplash();
        alter_frame.add(alt.getAlterStation());
        alter_frame.setVisible(true);
increaseSplash();
splashText("Loading the control panel");
        ctrlp = new ControlPanel();
increaseSplash();
        control_frame = new JInternalFrame("Control Panel", true, true, true, true);
        control_frame.setToolTipText("Selected element altering tools");
        control_frame.setBounds(dim.width-400, dim.height-380, 400, 200);
increaseSplash();
        control_frame.add(ctrlp.getControlPanel());
        control_frame.setVisible(true);
        ctrlp.svgc = svgF;
increaseSplash();
splashText("Initializing the navigator");
        navigator_frame = new JInternalFrame("Shape Navigator", true, true, true, true);
        navigator_frame.setToolTipText("Created shapes are lined up here");
        navigator_frame.setBounds(0, dim.height-380, 200, 190);
increaseSplash();
        navigator_frame.add(nv.getNavigator());
        navigator_frame.setVisible(true);
increaseSplash();
        JScrollPane frontDrawingScrollPane = new JScrollPane(svgF.getBoard());
        JScrollPane rearDrawingScrollPane = new JScrollPane(svgR.getBoard());
increaseSplash();
splashText("Creating the column and row headers");
        columnView = new DrawingBoardRule(DrawingBoardRule.HORIZONTAL, true);
        rowView = new DrawingBoardRule(DrawingBoardRule.VERTICAL, true);
increaseSplash();
        DrawingBoardRule rearColumnView = new DrawingBoardRule(DrawingBoardRule.HORIZONTAL, true);
        DrawingBoardRule rearRowView = new DrawingBoardRule(DrawingBoardRule.VERTICAL, true);
        columnView.setPreferredWidth(svgF.getBoard().getWidth());
increaseSplash();
        rowView.setPreferredHeight(svgF.getBoard().getHeight());
        rearColumnView.setPreferredWidth(svgF.getBoard().getWidth());
        rearRowView.setPreferredHeight(svgF.getBoard().getHeight());
increaseSplash();
splashText("Setting the drawing board rulers");
        frontDrawingScrollPane.setColumnHeaderView(columnView);
        frontDrawingScrollPane.setRowHeaderView(rowView);
        rearDrawingScrollPane.setColumnHeaderView(rearColumnView);
        rearDrawingScrollPane.setRowHeaderView(rearRowView);
increaseSplash();
        //        iframe.add(drawingScrollPane);
        drawing_board_tabbs.addTab("Front View", frontDrawingScrollPane);
        drawing_board_tabbs.addTab("Rear View", rearDrawingScrollPane);
increaseSplash();
splashText("Loading the drawing board footer");
        iframe.add(bordfooter, BorderLayout.SOUTH);
increaseSplash();
        drawing_dock.setTitleText("Drawing Board");
        drawing_container = drawing_dock.getContentPane();
increaseSplash();
        color_dock.setTitleText("Colour Pallete");
        Container color_container = color_dock.getContentPane();
        color_container.add(new JScrollPane(colorChooser));
increaseSplash();
        
        alter_dock.setTitleText("Alter Station");
        Container alter_container = alter_dock.getContentPane();
        alter_container.add(new JScrollPane(alt.getAlterStation()));
increaseSplash();
        
        navigator_dock.setTitleText("Navigator");
        Container navigator_container = navigator_dock.getContentPane();
//        navigator_container.add(new JScrollPane(nv.getNavigator()));
increaseSplash();
        
        control_dock.setTitleText("Control Panel");
        Container control_container = control_dock.getContentPane();
        control_container.add(new JScrollPane(ctrlp.getControlPanel()));
increaseSplash();
        tool_dock.setTitleText("Tool Board");
        Container tool_container = tool_dock.getContentPane();
        tool_container.add(new JScrollPane(tb.getToolbox()));
increaseSplash();
splashText("Finalizing the interface");
        grid.addDockable(0, 0, 0.1618929133858268, 1, navigator_dock);
        grid.addDockable(0.16582992125984253, 0, 0.4906661417322835, 1, drawing_dock);
        grid.addDockable(0.6604330708661418, 0, 0.3395669291338582, 0.3056872037914692, color_dock);
        grid.addDockable(0.6604330708661418, 0.31516587677725116, 0.3395669291338582, 0.2109004739336493, alter_dock);
increaseSplash();
        grid.addDockable(0.6604330708661418, 0.5355450236966826, 0.3395669291338582, 0.2772511848341231, control_dock);
        grid.addDockable(0.6604330708661418, 0.8222748815165877, 0.3395669291338582, 0.17772511848341238, tool_dock);
increaseSplash();
        station.dropTree(grid.toTree());
        default_controller = station.getController();
increaseSplash();
/*        desk.add(iframe);
        desk.add(color_frame);
        desk.add(alter_frame);
        desk.add(navigator_frame);
        desk.add(control_frame);
        desk.add(toolframe);
*/
//        this.add(desk);
        this.add(station.getComponent());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
increaseSplash();
splashText("Setting the screen........");
        this.setSize(dim);
        this.setVisible(true);
splashText("OK");

increaseSplash();
    }

    public void printLocations(){
/*        System.out.println("Navigator: "+station.getDockableLocationProperty(navigator_dock));
        System.out.println("Drawing: "+station.getDockableLocationProperty(drawing_dock));
        System.out.println("Color: "+station.getDockableLocationProperty(color_dock));
        System.out.println("Alter: "+station.getDockableLocationProperty(alter_dock));
        System.out.println("Control: "+station.getDockableLocationProperty(control_dock));
        System.out.println("Tool: "+station.getDockableLocationProperty(tool_dock));
*/        station.setController(default_controller);
    }

    public void stateChanged(ChangeEvent e) {
        svgF.color = colorChooser.getColor();
        svgR.color = colorChooser.getColor();
      }

    public void addFabric(String new_fabric[]){
//        System.out.println("addFabric(String new_fabric[]) Called");
        Fabric fabric = new Fabric(new_fabric[0], new_fabric[1], new_fabric[2], new_fabric[3], new_fabric[4]);
        addFabric(fabric);
    }

    public void addButton(String new_button[]){
//        System.out.println("addButton(String new_button[]) Called");
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
            org.w3c.dom.Element e = svgF.setGuideImage(f.toURI().toString(), w, h);
            java.awt.Image image = Toolkit.getDefaultToolkit().createImage(Image.getSource());
            java.awt.Image icon_image = image.getScaledInstance(35, 35, 10);
            ctrlp.addNewLayer(icon_image, f.getName(), e);
        }

        else if(jcb.getText().equals("Import Button")){
            String new_button[] = new String[5];
            ImportScreen imp_scr = new ImportScreen(this, "Button", new_button);
        }
        else if(jcb.getText().equals("New")){
//            patternObject po = new patternObject();
            setBoards();
            projectObject po = new projectObject(svgF.document, svgR.document);

            treeHandler th = new treeHandler(po, this);
            navigator_container = navigator_dock.getContentPane();
            navigator_container.add(new JScrollPane(th.getTree()));
            svgF.project_object = po;
            svgF.th = th;
        }
        else if(jcb.getText().equals("Print")){
            svgF.filePrinter();
            printLocations();
        }
        }
        catch(Exception e){
//            System.out.println("The Exception is "+e);
            JCommandToggleButton jtb = (JCommandToggleButton)ae.getSource();
//            System.out.println("The name is "+jtb.getName());
            String tmp[] = jtb.getText().split("\\s");

            if(jtb.getName().equals("fabric")){
                svgF.fillUri = fabrics_list.get(Integer.parseInt(tmp[1])).getFabricMainImage().toString();
                svgF.fillPattern();
//                System.out.println(svgF.fillUri.toString());
            }
            else if(jtb.getName().equals("button")){
                svgF.setButton(buttons_list.get(Integer.parseInt(tmp[1])).getbuttonMainImage());
            }
        }
    }

    private void setBoards(){
        drawing_container.add(drawing_board_tabbs);
        drawing_container.add(bordfooter, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    public void resetProject(treeHandler th){
//        System.out.println("Came here reset");
        navigator_container.removeAll();
        navigator_container.add(new JScrollPane(th.getTree()));
        this.setVisible(true);
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
//        System.out.println("addFabric(Fabric fabric) called");
        fabrics_list.add(fabric);
//        System.out.println(fabrics_list.size()+" fabrics in the list.");
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
//        System.out.println("addFabric(Fabric fabric) called");
        buttons_list.add(btn);
//        System.out.println(buttons_list.size()+" buttons in the list.");
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
    private static void splashInit()
    {
        System.out.println("Splash here");
        // the splash screen object is created by the JVM, if it is displaying a splash image

        mySplash = SplashScreen.getSplashScreen();
        // if there are any problems displaying the splash image
        // the call to getSplashScreen will returned null

        if (mySplash == null) {
            System.out.println("SplashScreen.getSplashScreen() returned null");
            return;
        }
        if (mySplash != null)
        {
            // get the size of the image now being displayed
            Dimension ssDim = mySplash.getSize();
            int height = ssDim.height;
            int width = ssDim.width;

            // stake out some area for our status information
            splashTextArea = new Rectangle2D.Double(15., height*0.35, width * .45, 32.);
            splashProgressArea = new Rectangle2D.Double(width * .55, height*.50, width*.4, 12 );

            // create the Graphics environment for drawing status info
            splashGraphics = mySplash.createGraphics();
            font = new Font("Dialog", Font.PLAIN, 12);
            splashGraphics.setFont(font);

            // initialize the status info
            splashText("Starting");
            splashProgress(0);
        }
    }
    private static void appInit()
    {
        System.out.println("Splash appInit");
        SwingUtilities.invokeLater(new Runnable() {
        public void run() {

        try{
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceRavenLookAndFeel");
        ribbonTest rt = new ribbonTest();
        }

        catch(Exception e){
            System.out.println("Look and feel error "+e);
        }
    }
    });
    splashGraphics.setPaint(Color.WHITE);
        splashText("Initializing session modules...");
                
    }

    public static void splashText(String str)
    {
        if (mySplash != null && mySplash.isVisible())
        {
            splashGraphics.setPaint(Color.black);
            splashGraphics.fill(splashTextArea);

            // draw the text
            splashGraphics.setPaint(Color.white);
            splashGraphics.drawString(str, (int)(splashTextArea.getX() + 10),(int)(splashTextArea.getY() + 15));

            // make sure it's displayed
/*            for(int i = 0; i < 20; i++){
                try{
                    str = str+".";
                    splashGraphics.drawString(str, (int)(splashTextArea.getX() + 10),(int)(splashTextArea.getY() + 15));
                    mySplash.update();
                }
                catch(Exception e){
                    System.out.println("Splash text exception");
                }
            }*/
            mySplash.update();
        }
    }
    private static void increaseSplash(){
        splashProgress(splashProgress);
        splashProgress++;
        System.out.println("Splash Progress is "+splashProgress);
    }

    public static void splashProgress(int pct)
    {
        if (mySplash != null && mySplash.isVisible())
        {

            // Note: 3 colors are used here to demonstrate steps
            // erase the old one
            splashGraphics.setPaint(Color.LIGHT_GRAY);
            splashGraphics.fill(splashProgressArea);

            // draw an outline
            splashGraphics.setPaint(Color.BLUE);
//            splashGraphics.draw(splashProgressArea);

            // Calculate the width corresponding to the correct percentage
            int x = (int) splashProgressArea.getMinX();
            int y = (int) splashProgressArea.getMinY();
            int wid = (int) splashProgressArea.getWidth();
            int hgt = (int) splashProgressArea.getHeight();

            int doneWidth = Math.round(pct*wid/100.f);
            doneWidth = Math.max(0, Math.min(doneWidth, wid-1));  // limit 0-width

            // fill the done part one pixel smaller than the outline
            splashGraphics.setPaint(Color.WHITE);
            splashGraphics.fillRect(x, y+1, doneWidth, hgt-1);

            // make sure it's displayed
            mySplash.update();
        }
    }

public static void main(String args[])throws Exception{

    JRibbonFrame.setDefaultLookAndFeelDecorated(true);
/*        SwingUtilities.invokeLater(new Runnable() {
        public void run() {

        try{
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceRavenLookAndFeel");
//        ribbonTest rt = new ribbonTest();
        }

        catch(Exception e){
            System.out.println("Look and feel error "+e);
        }
    }
    });*/

    splashInit();
    appInit();
}
}
