package Design_tool_prototype1;

import javax.swing.event.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import java.awt.image.*;
import java.net.URI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.awt.geom.Rectangle2D;

import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.metal.MetalTreeUI;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.apache.xml.serialize.*;
import org.w3c.dom.Element;

import java.util.*;
import java.io.File;
import javax.imageio.*;

import org.apache.batik.dom.svg.SVGDOMImplementation;
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

import java.awt.Font;
import java.awt.GraphicsEnvironment;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import javax.swing.border.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
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

import org.apache.commons.lang.StringUtils;
import org.apache.batik.dom.util.DOMUtilities;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.batik.util.*;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.w3c.dom.*;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;


public class ribbonTest extends JRibbonFrame implements ChangeListener, ActionListener, KeyListener{
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
    SVGConjurer selectedSVGC;

    JTextArea text_type_area = new JTextArea(80, 20);

    DockController controller = new DockController();
    DockController default_controller = new DockController();
    SplitDockStation station = new SplitDockStation();
    DefaultDockable drawing_dock = new DefaultDockable();
    DefaultDockable color_dock = new DefaultDockable();
    DefaultDockable alter_dock = new DefaultDockable();
    DefaultDockable navigator_dock = new DefaultDockable();
    DefaultDockable control_dock = new DefaultDockable();
    DefaultDockable tool_dock = new DefaultDockable();

    JCommandToggleButton[] fabric_buttons;
    JCommandToggleButton[] button_buttons;
    JCommandToggleButton[] pattern_buttons;
    JFrame frame;
    JButton line_bt;
    paintBoard pb;
    SVGConjurer svgF;
    SVGConjurer svgR;
    alterStation alt;
    ControlPanel ctrlp;
    DrawingBoardFooter dbf;
    navigator nv = new navigator();
    toolbox tb;
    JColorChooser colorChooser;
    JRibbonGallery gallery;
    final static String GALLERY_NAME = "Gallery";
    final static String BUTTON_GALLERY_NAME = "Button Gallery";
    final static String PATTERN_GALLERY_NAME = "Pattern Gallery";

    JRibbonBand texture_gallery_band;
    JRibbonBand texture_controls_band;

    JRibbonBand dress_form_controls_band;

    JRibbonBand buttons_controls_band;
    JRibbonBand button_gallery_band;

    JRibbonBand pattern_controls_band;
    JRibbonBand pattern_gallery_band;

    JRibbonBand text_controls_band;
    JRibbonBand text_entry_band;
    JRibbonBand text_resize_band;

    private DrawingBoardRule columnView;
    private DrawingBoardRule rowView;

    private static int splashProgress = 0;

    Image image;
    java.util.List<Fabric> fabrics_list = new ArrayList<Fabric>();
    java.util.List<Fabric> buttons_list = new ArrayList<Fabric>();
    java.util.List<patternPackage> pattern_list = new ArrayList<patternPackage>();

    Container navigator_container;

    static SplashScreen mySplash;
    static Graphics2D splashGraphics;
    static Rectangle2D.Double splashTextArea;
    static Rectangle2D.Double splashProgressArea;
    static Font font;
    Font[] fnt;

    RibbonApplicationMenuEntryPrimary amEntryNew;
    RibbonApplicationMenuEntryPrimary amEntryOpen;
    RibbonApplicationMenuEntryPrimary amEntrySave;
    RibbonApplicationMenuEntryPrimary amEntryClose;

    Document empty_front;
    Document empty_rear;
    Dimension dim;

    projectObject project_obj;

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
        controller.setTheme(new NoStackTheme(new EclipseTheme()));
        SplitDockGrid grid = new SplitDockGrid();
        increaseSplash();

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.getAllFonts();
        fnt= ge.getAllFonts();

        /**---- Setting the Main Icon ----**/
        Image frame_image;
        try{
            File frame_icon = new File("curve_bt.gif");
            frame_image = ImageIO.read(frame_icon);
            frame.setIconImage(frame_image);
        }
        catch(Exception e){
            System.out.println("frame icon error: "+e);
        }

        /**---- Creating the Application Menu ----**/
        RibbonApplicationMenu applicationMenu = new RibbonApplicationMenu();

        amEntryNew = new RibbonApplicationMenuEntryPrimary(new EmptyResizableIcon(32), "New Project", this, CommandButtonKind.ACTION_ONLY);
        amEntryOpen = new RibbonApplicationMenuEntryPrimary(new EmptyResizableIcon(32), "Open Project", this, CommandButtonKind.ACTION_ONLY);
        amEntrySave = new RibbonApplicationMenuEntryPrimary(new EmptyResizableIcon(32), "Save Project", this, CommandButtonKind.ACTION_ONLY);
        amEntryClose = new RibbonApplicationMenuEntryPrimary(new EmptyResizableIcon(32), "Close Project", this, CommandButtonKind.ACTION_ONLY);
        amEntrySave.setEnabled(false);
        amEntryClose.setEnabled(false);
	applicationMenu.addMenuEntry(amEntryNew);
        applicationMenu.addMenuEntry(amEntryOpen);
        applicationMenu.addMenuEntry(amEntrySave);
        applicationMenu.addMenuEntry(amEntryClose);
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

        this.getRibbon().setApplicationMenu(applicationMenu);
        increaseSplash();/**---- Finished Creating the Application Menu ----**/

        /**----- Read in Gallery Files ----**/
        try{
            ObjectInputStream objIn = new ObjectInputStream(new BufferedInputStream(new FileInputStream("fabrics.dat")));
            fabrics_list = (ArrayList)(objIn.readObject());
            objIn.close();
        }
        catch(Exception e){
            System.out.println("Fabrics list import error "+e);
        }
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

        try{
            ObjectInputStream objIn = new ObjectInputStream(new BufferedInputStream(new FileInputStream("patterns.dat")));
            pattern_list = (ArrayList)(objIn.readObject());
            objIn.close();
        }
        catch(Exception e){
            System.out.println("Pattern list import error "+e);
        }
        increaseSplash();/**----- Finished Reading Gallery Files ----**/

        /**----- Populating the Fabrics Library ----**/
        fabric_buttons = new JCommandToggleButton[fabrics_list.size()];
        for(int j = 0; j < fabrics_list.size(); j++){
            try{
                image = fabrics_list.get(j).getFabricIcon();
            }

            catch(Exception e){
                System.out.println("RibbonTest class - RibbonTest()Constructor - Fabric ImageIO problem: "+e);
            }
            DecoratedResizableIcon dri = new DecoratedResizableIcon(new DisabledResizableIcon(RibbonElementPriority.TOP, 52, 52), new IconImageDecoration(image), new BottomRightDecoration(j));
            JCommandToggleButton jtb  = new JCommandToggleButton(fabrics_list.get(j).getFabricNameShort().substring(0, 10), dri);
            jtb.setName("fabric "+j);
            jtb.addActionListener(this);
            this.fabric_buttons[j] = jtb;
        }
        increaseSplash();/**----- Finished Populating the Fabrics Library ----**/

        /**----- Populating the Buttons Library ----**/
        button_buttons = new JCommandToggleButton[buttons_list.size()];
        for(int j = 0; j < buttons_list.size(); j++){
            try{
                image = buttons_list.get(j).getFabricIcon();
            }

            catch(Exception e){
                System.out.println("RibbonTest()Constructor - Button ImageIO problem: "+e);
                System.out.println(buttons_list.get(j).getFabricNameLong());
            }
            java.awt.Image icon_image = image.getScaledInstance(55, 55, 10);
            DecoratedResizableIcon dri = new DecoratedResizableIcon(new DisabledResizableIcon(RibbonElementPriority.TOP, 52, 52), new IconImageDecoration(icon_image), new BottomRightDecoration(j));
            JCommandToggleButton button_tb  = new JCommandToggleButton(buttons_list.get(j).getFabricNameShort().substring(0, 10), dri);
            button_tb.setName("button "+j);
            button_tb.addActionListener(this);
            this.button_buttons[j] = button_tb;
        }/**----- Finished Populating the Buttons Library ----**/

        /**----- Populating the Pattern Library ----**/
        pattern_buttons = new JCommandToggleButton[pattern_list.size()];
        for(int j = 0; j < pattern_list.size(); j++){
            try{
                image = pattern_list.get(j).getPatternFrontImage();
                image = image.getScaledInstance(80, 65, 10);
            }

            catch(Exception e){
                System.out.println("RibbonTest()Constructor - Pattern ImageIO problem: "+e);
            }

            DecoratedResizableIcon dri = new DecoratedResizableIcon(new DisabledResizableIcon(RibbonElementPriority.TOP, 52, 52), new IconImageDecoration(image), new BottomRightDecoration(j));
            JCommandToggleButton jtb  = new JCommandToggleButton(pattern_list.get(j).ptrn.pattern_name.substring(0, 10), dri);
            jtb.setName("pattern "+j);
            jtb.addActionListener(this);
            this.pattern_buttons[j] = jtb;
        }increaseSplash();/**----- Finished Populating the Pattern Library ----**/

        Map<RibbonElementPriority, Integer> visibleButtonCounts = new HashMap<RibbonElementPriority, Integer>();
        visibleButtonCounts.put(RibbonElementPriority.LOW, 4);
	visibleButtonCounts.put(RibbonElementPriority.MEDIUM, 5);
	visibleButtonCounts.put(RibbonElementPriority.TOP, 6);

        /**---- Assigning the Texture Controls ----**/
        texture_controls_band = new JRibbonBand("Import", new EmptyResizableIcon(32));
        JCommandButton import_bt = new JCommandButton("Import");
        import_bt.addActionListener(this);
        JCommandButton print = new JCommandButton("Print");
        print.addActionListener(this);
        texture_controls_band.addCommandButton(print, RibbonElementPriority.MEDIUM);
        texture_controls_band.addCommandButton(import_bt, RibbonElementPriority.MEDIUM);
        /**---- Finished Assigning the Texture Controls ----**/

        /**---- Assigning the Texture Gallery ----**/
        texture_gallery_band = new JRibbonBand("Texture Gallery", new EmptyResizableIcon(32));
        java.util.List<StringValuePair<java.util.List<JCommandToggleButton>>> galleryButtons = new ArrayList<StringValuePair<java.util.List<JCommandToggleButton>>>();
	java.util.List<JCommandToggleButton> galleryButtonsList = new ArrayList<JCommandToggleButton>();
        for (int j = 0; j < fabrics_list.size(); j++){
            galleryButtonsList.add(this.fabric_buttons[j]);
        }
        galleryButtons.add(new StringValuePair<java.util.List<JCommandToggleButton>>("Group " , galleryButtonsList));
        texture_gallery_band.addRibbonGallery(GALLERY_NAME, galleryButtons, visibleButtonCounts, 6, 4, RibbonElementPriority.TOP);
        /**---- Finished Assigning the Texture Gallery ----**/

        /**---- Assigning the Dress Form controls ----**/
        dress_form_controls_band = new JRibbonBand("Dress Form Controls", new EmptyResizableIcon(32));
        JCommandButton import_img = new JCommandButton("Import Guide Image", new EmptyResizableIcon(32));
        import_img.addActionListener(this);
        dress_form_controls_band.addCommandButton(import_img, RibbonElementPriority.MEDIUM);
        dress_form_controls_band.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesRestrictive(texture_gallery_band));
        /**---- Finished Assigning the Dress Form controls ----**/

        /**---- Assigning the Button Band controls ----**/
        buttons_controls_band = new JRibbonBand("Button Controls", new EmptyResizableIcon(32));
        JCommandButton import_button = new JCommandButton("Import Button", new EmptyResizableIcon(32));
        import_button.addActionListener(this);
        buttons_controls_band.addCommandButton(import_button, RibbonElementPriority.MEDIUM);
        buttons_controls_band.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesRestrictive(texture_gallery_band));
        /**---- Finish Assigning the Button Band controls ----**/

        /**---- Assigning the Texture Controls ----**/
        text_controls_band = new JRibbonBand("Select Font", new EmptyResizableIcon(32));
        Object[] font_names = new Object[fnt.length];
        for(int x = 0; x < fnt.length; x++){
            font_names[x] = fnt[x].getFontName();
        }
        JComboBox fonts_box = new JComboBox(font_names);
        fonts_box.setName("fonts box");
        fonts_box.addActionListener(this);
        JRibbonComponent games = new JRibbonComponent(new EmptyResizableIcon(32), "Fonts", fonts_box);
        text_controls_band.addRibbonComponent(games);

        String[] font_sizes = new String[200];
        for(int i = 0; i < 200; i++){
            font_sizes[i] = i+"";
        }
        JComboBox fonts_size_box = new JComboBox(font_sizes);
        fonts_size_box.setName("font size");

        fonts_size_box.addActionListener(this);

        JButton edit_path = new JButton("Edit Path");
        edit_path.setName("edit path");
        edit_path.addActionListener(this);

        text_type_area.addKeyListener(this);
        text_type_area.setForeground(Color.CYAN);
        text_type_area.setBackground(Color.red);
        text_entry_band = new JRibbonBand("Enter the text", new EmptyResizableIcon(32));
        text_entry_band.addRibbonComponent(new JRibbonComponent(new EmptyResizableIcon(32), "", text_type_area));
        text_entry_band.addRibbonComponent(new JRibbonComponent(new EmptyResizableIcon(32), "", edit_path));
        
        text_entry_band.setMaximumSize(new Dimension(100, 30));

        text_resize_band = new JRibbonBand("Font Size", new EmptyResizableIcon(32));
        text_resize_band.addRibbonComponent(new JRibbonComponent(new EmptyResizableIcon(32), "", fonts_size_box));
        /**---- Finished Assigning the Texture Controls ----**/

        /**---- Assigning the Button Gallery ----**/
        button_gallery_band = new JRibbonBand("Button Gallery", new EmptyResizableIcon(32));
        java.util.List<StringValuePair<java.util.List<JCommandToggleButton>>> button_galleryButtons = new ArrayList<StringValuePair<java.util.List<JCommandToggleButton>>>();
	java.util.List<JCommandToggleButton> button_galleryButtonsList = new ArrayList<JCommandToggleButton>();
        for (int j = 0; j < buttons_list.size(); j++){
            button_galleryButtonsList.add(this.button_buttons[j]);
        }
        button_galleryButtons.add(new StringValuePair<java.util.List<JCommandToggleButton>>("Button Group" , button_galleryButtonsList));
        button_gallery_band.addRibbonGallery(BUTTON_GALLERY_NAME, button_galleryButtons, visibleButtonCounts, 6, 4, RibbonElementPriority.TOP);
        /**---- Finished Assigning the Button Gallery ----**/

        /**---- Assigning the Pattern Controls ----**/
        pattern_controls_band = new JRibbonBand("Pattern Controls", new EmptyResizableIcon(32));
        JCommandButton test_button = new JCommandButton("test", new EmptyResizableIcon(32));
        test_button.addActionListener(this);
        pattern_controls_band.addCommandButton(test_button, RibbonElementPriority.MEDIUM);
        pattern_controls_band.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesRestrictive(texture_gallery_band));
        /**---- Finished Assigning the Pattern Controls ----**/

        /**---- Assigning the Pattern Gallery ----**/
        pattern_gallery_band = new JRibbonBand("Pattern Gallery", new EmptyResizableIcon(32));
        java.util.List<StringValuePair<java.util.List<JCommandToggleButton>>> pattern_gallery_Buttons = new ArrayList<StringValuePair<java.util.List<JCommandToggleButton>>>();
	java.util.List<JCommandToggleButton> pattern_gallery_Buttons_List = new ArrayList<JCommandToggleButton>();
        for (int j = 0; j < pattern_list.size(); j++){
            pattern_gallery_Buttons_List.add(this.pattern_buttons[j]);
        }
        pattern_gallery_Buttons.add(new StringValuePair<java.util.List<JCommandToggleButton>>("Pattern Group" , pattern_gallery_Buttons_List));
        pattern_gallery_band.addRibbonGallery(PATTERN_GALLERY_NAME, pattern_gallery_Buttons, visibleButtonCounts, 6, 4, RibbonElementPriority.TOP);
        /**---- Finished Assigning the Pattern Gallery ----**/
        
        texture_controls_band.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesRestrictive(texture_gallery_band));
        gallery = texture_gallery_band.getControlPanel().getRibbonGallery(GALLERY_NAME);

        this.getRibbon().addTask(new RibbonTask("Texture", texture_controls_band, texture_gallery_band));
        this.getRibbon().addTask(new RibbonTask("Dress Form", dress_form_controls_band));
        this.getRibbon().addTask(new RibbonTask("Buttons", buttons_controls_band, button_gallery_band));
        this.getRibbon().addTask(new RibbonTask("Patterns", pattern_controls_band, pattern_gallery_band));
        this.getRibbon().addTask(new RibbonTask("Text", text_controls_band, text_entry_band, text_resize_band));

        dim = Toolkit.getDefaultToolkit().getScreenSize();
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
        svgF = new SVGConjurer(dim, alt, dbf, "front");
        svgR = new SVGConjurer(dim, alt, dbf, "rear");
        svgF.rt = this;
        svgR.rt = this;
        dbf.svgc = svgF;
        selectedSVGC = svgF;

increaseSplash();

        alt.svgc = svgF;
increaseSplash();
        alt.rt = this;
        pb.alt = alt;
        pb.nv = nv;
increaseSplash();
        tb = new toolbox(svgF, this);
        toolframe.add(tb.getToolbox());
splashText("Loading the color palette");
        colorChooser = new JColorChooser();
increaseSplash();
        ColorSelectionModel model = colorChooser.getSelectionModel();
        model.addChangeListener(this);
increaseSplash();
        color_frame = new JInternalFrame("Color Palete", true, true, true, true);
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
        drawing_board_tabbs.addChangeListener(this);
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
        this.add(station.getComponent());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
increaseSplash();
splashText("Setting the screen........");
        this.setSize(dim);
        this.setVisible(true);
        File e = new File("Temp");
        e.mkdir();
splashText("OK");

increaseSplash();
    }

    public void printLocations(){
        station.setController(default_controller);
    }

    public void stateChanged(ChangeEvent e) {
        try{
            JTabbedPane jtp = (JTabbedPane)e.getSource();
            if(jtp.getSelectedIndex()==1){
                tb.svgc = svgR;
                ctrlp.svgc = svgR;
                selectedSVGC = svgR;
                ElementLocalizer el = new ElementLocalizer(svgR.other_side, svgF.getRearView(), svgF, svgR);
                try{
                    svgR.other_side.removeChild(svgR.other_side.getFirstChild());
                }
                catch(Exception exc){
                    System.out.println("could not remove the other side: 670 "+exc);
                }
                svgR.other_side.appendChild(el.container.getFirstChild());
            }
            else{
                tb.svgc = svgF;
                ctrlp.svgc = svgF;
                selectedSVGC = svgF;
                ElementLocalizer el = new ElementLocalizer(svgF.other_side, svgR.getRearView(), svgF, svgR);
                try{
                    svgF.other_side.removeChild(svgF.other_side.getFirstChild());
                }
                catch(Exception exc){
                    System.out.println("Could not remove the 680: "+exc);
                }
                System.out.println("Line 682");
                svgF.other_side.appendChild(el.container.getFirstChild());
            }
        }
        catch(Exception ex1){
            System.out.println("In the Ribbon Test: "+ex1);
        }
        selectedSVGC.testTextColor(colorChooser.getColor());
      }
    public void actionPerformed(ActionEvent ae){

        try{
            if(ae.getActionCommand().equals("comboBoxChanged")){
                JComboBox jcb = (JComboBox)ae.getSource();
                if(jcb.getName().equals("fonts box")){
                    selectedSVGC.testText(jcb.getSelectedItem().toString());
                    Font f = fnt[jcb.getSelectedIndex()];
                    return;
                }
                else if(jcb.getName().equals("font size")){
                    selectedSVGC.setTextSize(jcb.getSelectedIndex());
                    return;
                }
                
            }
        }
        catch(Exception ee){
            System.out.print("ribbon test ActionPerformed Exception");
        }

        try{
            JButton jb = (JButton)ae.getSource();
            if(jb.getName().equals("edit path")){
                selectedSVGC.editTextPath(WIDTH);
                return;
            }
        }
        catch(Exception ex){

        }
        
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

        else if(jcb.getText().equals("Import Pattern")){
            String new_button[] = new String[5];
            ImportScreen imp_scr = new ImportScreen(this, "Button", new_button);
        }
        else if(jcb.getText().equals("New Project")){
//            patternObject po = new patternObject();
            try{
                closeProject();
            }
            catch(Exception ex){
                System.out.println("Project close Exception");
            }

            setBoards();
            project_obj = new projectObject(svgF.document, svgR.document);

            treeHandler th = new treeHandler(project_obj, this, svgF);
            navigator_container = navigator_dock.getContentPane();
            navigator_container.add(new JScrollPane(th.getTree()));
            svgF.project_object = project_obj;
            svgR.project_object = project_obj;
            svgF.th = th;
            svgR.th = th;
            amEntrySave.setEnabled(true);
            amEntryClose.setEnabled(true);
        }
        else if(jcb.getText().equals("Save Project")){
            try{
                projectWriter(svgF.project_object);
            }
            catch(Exception e){
                System.out.println("Project save exception: "+e);
            }
        }

        else if(jcb.getText().equals("Open Project")){
            try{
                closeProject();
            }
            catch(Exception ex){
                System.out.println("Project close Exception");
            }
            projectReader();
            amEntrySave.setEnabled(true);
            amEntryClose.setEnabled(true);
        }
        else if(jcb.getText().equals("Close Project")){
            System.out.println("Test: "+svgF.document.getFirstChild().getChildNodes().getLength());

            closeProject();
        }
        else if(jcb.getText().equals("Print")){
            svgF.filePrinter();
            printLocations();
        }
        }
        catch(Exception e){
//            System.out.println("The Exception is "+e);
            JCommandToggleButton jtb = (JCommandToggleButton)ae.getSource();
            System.out.println("The name is "+jtb.getName());
            String tmp[] = jtb.getText().split("\\s");

            if(jtb.getName().contains("fabric")){
                String[] bt_txt = jtb.getName().split(" ");
                int bt_num = Integer.parseInt(bt_txt[bt_txt.length-1]);

                ImageRelations2 ir = new ImageRelations2(fabrics_list.get(bt_num).getFabricMainImage(), fabrics_list.get(jtb.getParent().getComponentZOrder(jtb)).getFabricNameLong());
                selectedSVGC.fillUri = ir.getUri();
                selectedSVGC.fillPatternByURI();
            }
            else if(jtb.getName().contains("button")){
                String[] bt_txt = jtb.getName().split(" ");
                int bt_num = Integer.parseInt(bt_txt[bt_txt.length-1]);

                svgF.setButton(buttons_list.get(bt_num).getFabricMainImage());
            }
            else if(jtb.getName().contains("pattern")){
                System.out.println("Pattern pressed");
                String[] bt_txt = jtb.getName().split(" ");
                int bt_num = Integer.parseInt(bt_txt[bt_txt.length-1]);

                readPattern(pattern_list.get(bt_num).ptrn);
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        if(text_type_area.hasFocus()){
            selectedSVGC.testSetText(text_type_area.getText());
        }
    }
    public void keyPressed(KeyEvent e) {
        if(text_type_area.hasFocus()){
            selectedSVGC.testSetText(text_type_area.getText());
        }
    }
    public void keyTyped(KeyEvent e) {
        if(text_type_area.hasFocus()){
            selectedSVGC.testSetText(text_type_area.getText());
        }
    }

    private void setBoards(){
        drawing_container.add(drawing_board_tabbs);
        drawing_container.add(bordfooter, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    private void closeProject(){
        svgF.setPlates();
        svgR.setPlates();
            navigator_container.removeAll();
            svgF.project_object = null;
            svgR.project_object = null;
            svgF.th = null;
            svgR.th = null;
            svgF.refresh();
            svgR.refresh();
            setVisible(true);
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
        fabrics_list.add(fabric);
        fileWriter("fabric");

        try{
            image = fabric.getFabricIcon();
        }

        catch(Exception e){
            System.out.println("RibbonTest class - addFabric(Fabric fabric) Creating image from fabric file error: "+e);
        }

        DecoratedResizableIcon dri = new DecoratedResizableIcon(new DisabledResizableIcon(RibbonElementPriority.TOP, 52, 52), new IconImageDecoration(image), new BottomRightDecoration(3));
        JCommandToggleButton jtb  = new JCommandToggleButton("Texture "  + fabrics_list.size(), dri);
        jtb.setName("fabric");
        jtb.addActionListener(this);
        texture_gallery_band.addRibbonGalleryButtons(GALLERY_NAME, "Group ", jtb);
    }

    public void addButton(Fabric button){
//        System.out.println("addFabric(Fabric fabric) called");
        buttons_list.add(button);
//        System.out.println(buttons_list.size()+" buttons in the list.");
        fileWriter("button");

        try{
//            File image_file = new File(button.getbuttonIcon());
  //          image = ImageIO.read(image_file);
            image = button.getFabricIcon();
        }

        catch(Exception e){
            System.out.println("RibbonTest class - addButton(button Btn) Creating image from fabric file error: "+e);
        }

        DecoratedResizableIcon dri = new DecoratedResizableIcon(new DisabledResizableIcon(RibbonElementPriority.TOP, 52, 52), new IconImageDecoration(image), new BottomRightDecoration(3));
        JCommandToggleButton jtb  = new JCommandToggleButton("Button "  + 3, dri);
        jtb.addActionListener(this);
        button_gallery_band.addRibbonGalleryButtons(BUTTON_GALLERY_NAME, "Button Group", jtb);
    }

    public void addPattern(patternPackage pp){
        pattern_list.add(pp);
        fileWriter("pattern");

        try{
            image = pp.getPatternFrontImage();
        }

        catch(Exception e){
            System.out.println("RibbonTest class addPattern(pp) Creating image from pp error: "+e);
        }

        DecoratedResizableIcon dri = new DecoratedResizableIcon(new DisabledResizableIcon(RibbonElementPriority.TOP, 52, 52), new IconImageDecoration(image), new BottomRightDecoration(3));
        JCommandToggleButton jtb  = new JCommandToggleButton("pattern "  + pattern_list.size(), dri);
        jtb.setName("pattern");
        jtb.addActionListener(this);
        pattern_gallery_band.addRibbonGalleryButtons(GALLERY_NAME, "Group ", jtb);
        System.out.println("Pattern added to the gallery");
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

        else if(file.equals("pattern")){
            ObjectOutputStream objOut = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("patterns.dat")));
            objOut.writeObject(pattern_list);
            objOut.close();
            System.out.println("Pattern written");
        }
    }

    catch(Exception e){
        System.out.println("Line 486: Exception "+e);
    }
}

public void projectReader(){
    project p = null;
    project_obj = new projectObject();
/************************/
    FileFilter projectfilter = new FileNameExtensionFilter("Temporary project format (.lld)", "lld");
    JFileChooser jfc = new JFileChooser();
    jfc.addChoosableFileFilter(projectfilter);
    jfc.setDialogTitle("Open Project File");
    File file = jfc.getCurrentDirectory();
//    System.out.println("The path is: "+file.getPath());
    int result = jfc.showOpenDialog(this);

    if(result == JFileChooser.CANCEL_OPTION) return;
    String[] path = StringUtils.split(jfc.getCurrentDirectory().getPath(), "\\");

    File selected_file = jfc.getSelectedFile();
    String qualified_path = path[0];
    for(int i = 1; i < path.length; i++){
        qualified_path = qualified_path+"//" + path[i];
    }

/************************/
    try{
        ObjectInputStream objIn = new ObjectInputStream(new BufferedInputStream(new FileInputStream(qualified_path+"//"+selected_file.getName())));
        p = (project)(objIn.readObject());
        objIn.close();
    }

    catch(Exception e){
        System.out.println("Project read Exception: "+ e);
    }

    Element root = svgF.document.getDocumentElement();
    Node defs = null;
    Node defs2 = null;
    try {
      String parser = XMLResourceDescriptor.getXMLParserClassName();
      SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
//      System.out.println("p.defs is: "+p.defs);
      defs = DOMUtilities.parseXML(p.defs, svgF.document,svgF.canvas.getURI(), null, null,f);
      defs2 = DOMUtilities.parseXML(p.defs, svgR.document,svgR.canvas.getURI(), null, null,f);
    }
    catch( Exception ex ){
        ex.printStackTrace();
    }

    try{
//        Element e = svgF.document.createElement("svg");
//        Element e2 = svgR.document.createElement("svg");
//        e.appendChild(defs);
        root.appendChild(defs.getFirstChild());
        svgR.document.getDocumentElement().appendChild(defs2.getFirstChild());
    }
    catch(Exception ee){
        System.out.append("ee error: "+ee);
    }

    for(int i = 0; i < p.patterns.size(); i++){
    for(int j = 0; j < p.patterns.get(i).fills.size(); j++){
        inner: for(int x = 0; x < fabrics_list.size(); x++){
            if(fabrics_list.get(x).getFabricNameLong().equals(p.patterns.get(i).fills.get(j))){
                ImageRelations2 ir = new ImageRelations2(fabrics_list.get(x).getFabricMainImage(), fabrics_list.get(x).getFabricNameLong());
                break inner;
            }
        }
    }
}
    for(int i = 0; i < p.history_elements.size(); i++){
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
        try{
            Node ef = null;
            try{
                System.out.println("Creating node is "+p.history_elements.get(i));
                ef = DOMUtilities.parseXML(p.history_elements.get(i), svgF.document,svgF.canvas.getURI(), null, null,f);
            }
            catch(Exception ee){
                System.out.println("History element Read exception is: "+ee);
            }
            Element el = svgF.document.createElement("svg");
            el.appendChild(ef);
            project_obj.history_elements.add((Element)el.getFirstChild().getFirstChild().cloneNode(true));
            System.out.println("I think the element is successfully added.");
        }
        catch(Exception ex){
            System.out.println("History elements reading in error: "+ex);
        }
    }

    for(int i = 0; i < p.patterns.size(); i++){
        patternObject ptrn = new patternObject();
        ptrn.pattern_name = p.patterns.get(i).pattern_name;
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);

        try {
            Node ef = null;
            Node er = null;
            try{
                System.out.println("The mentioning string is in: "+p.patterns.get(i).front);
                ef = DOMUtilities.parseXML(p.patterns.get(i).front, svgF.document,svgF.canvas.getURI(), null, null,f);
                er = DOMUtilities.parseXML(p.patterns.get(i).rear, svgR.document,svgR.canvas.getURI(), null, null,f);
            }
            catch(Exception ee){
                System.out.println("Read exception is: "+ee);
            }
            Element el = svgF.document.createElement("svg");
            el.appendChild(ef);
            Element em = svgR.document.createElement("svg");
            em.appendChild(er);

            ptrn.front = el;
            ptrn.rear = em;
            Element element1 = (Element)ptrn.front.getFirstChild();
            Element element2 = (Element)ptrn.rear.getFirstChild();
            ptrn.front = null;
            ptrn.rear = null;
            ptrn.front = element1;
            ptrn.rear = element2;

            try{
                root.appendChild(ptrn.front);
            }
            catch(Exception ex1){
                System.out.println("front appending error: "+ex1);
            }
            
            svgR.document.getDocumentElement().appendChild(ptrn.rear);
            svgF.elementIterator(ptrn.front);
            svgR.elementIterator(ptrn.rear);
        }

        catch(Exception ex){
            System.out.println("Project reader pattern creation error: "+ex);
        }

        try{
            project_obj.addPatternObject(ptrn);
        }
        catch(Exception e){
            System.out.println("The exception: "+e);
        }
    }
    treeHandler th = new treeHandler(project_obj, this, svgF);

    navigator_container = navigator_dock.getContentPane();
    navigator_container.add(new JScrollPane(th.getTree()));
    svgF.project_object = project_obj;
    svgF.th = th;
    setVisible(true);
    setBoards();
    svgF.refresh();
    svgR.refresh();
}

private void readPattern(pattern p){
    patternObject ptrn = new patternObject();
    ptrn.pattern_name = p.pattern_name;
    String parser = XMLResourceDescriptor.getXMLParserClassName();
    SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);

    try{
        Node ef = null;
        Node er = null;
        try{
            ef = DOMUtilities.parseXML(p.front, svgF.document,svgF.canvas.getURI(), null, null,f);
            er = DOMUtilities.parseXML(p.rear, svgR.document,svgR.canvas.getURI(), null, null,f);
        }
        catch(Exception ee){
            System.out.println("1034 Read exception is: "+ee);
        }

        Element el = svgF.document.createElement("svg");
        el.appendChild(ef);
        Element em = svgR.document.createElement("svg");
        em.appendChild(er);
        ptrn.front = el;
        ptrn.rear = em;
        Element element1 = (Element)ptrn.front.getFirstChild();
        Element element2 = (Element)ptrn.rear.getFirstChild();
        ptrn.front = null;
        ptrn.front = element1;
        ptrn.rear = null;
        ptrn.rear = element2;
        Element root = svgF.document.getDocumentElement();
        root.appendChild(ptrn.front);
        svgR.document.getDocumentElement().appendChild(ptrn.rear);
        svgF.elementIterator(ptrn.front);
        svgR.elementIterator(ptrn.rear);
    }

    catch(Exception ex){
        System.out.println("982 Project reader pattern creation error: "+ex);
    }
    try{
        project_obj.addPatternObject(ptrn);
    }
    catch(Exception e){
        System.out.println("The exception: "+e);
    }

    treeHandler th = new treeHandler(project_obj, this, svgF);

    navigator_container = navigator_dock.getContentPane();
    navigator_container.add(new JScrollPane(th.getTree()));
    svgF.project_object = project_obj;
    svgF.project_object = project_obj;
    svgF.th = th;
    svgR.th = th;
    setVisible(true);
    setBoards();
    svgF.refresh();
    svgR.refresh();
}

public void projectWriter(projectObject po){
    project p = new project();

    for(int i = 0; i < po.patterns.size(); i++){
        pattern pt = new pattern();

        for(int j = 0; j < po.patterns.get(i).associated_fabrics.size(); j++){
        try{
//            System.out.println(po.associated_fabrics.get(i).fill_uri);
            File f = new File(new URI(po.patterns.get(i).associated_fabrics.get(j).fill_uri));

            for(int x = 0; x < fabrics_list.size(); x++){
                if(fabrics_list.get(x).getFabricNameLong().equals(f.getAbsoluteFile().getName())){
                    System.out.println("Relevant fabric found at index: "+x);
                    pt.fills.add(fabrics_list.get(x).getFabricNameLong());
                }
            }
        }
        catch(Exception e3){
            System.out.println("The exception in ribbon test");
        }
    }
        CharArrayWriter tmp_character_array = new CharArrayWriter();
        CharArrayWriter tmp_character_array2 = new CharArrayWriter();
        pt.pattern_name = po.patterns.get(i).pattern_name;
        try{
            DOMUtilities.writeNode(po.patterns.get(i).front, tmp_character_array);
            pt.front = tmp_character_array.toString();
            DOMUtilities.writeNode(po.patterns.get(i).rear, tmp_character_array2);
            pt.rear = tmp_character_array2.toString();
        }

        catch(Exception e){
            System.out.println("patterns creation exception: "+e);
        }
        p.patterns.add(pt);
    }

    System.out.println("2");

    CharArrayWriter defs = new CharArrayWriter();
    Element defs_wrapper = svgF.document.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg");
//    defs_wrapper.appendChild(svgF.document.getDocumentElement().getElementsByTagName("defs").item(0));
    NodeList nl = svgF.document.getElementsByTagName("defs");
    Element pattern_defs = null;
System.out.println("3");
    for(int i = 0; i < nl.getLength(); i++){
        if(((Element)nl.item(i)).getAttribute("id").equals("pattern_defs")){
            System.out.println("Pattern defs found in the project writer");
            pattern_defs = (Element)nl.item(i);
        }
    }
    defs_wrapper.appendChild(pattern_defs.cloneNode(true));
System.out.println("3");
    try{
        DOMUtilities.writeNode(defs_wrapper, defs);
    }

    catch(Exception e){
        System.out.println("Defs creation exception: "+e);
    }
System.out.println("4");
    p.defs = defs.toString();
    CharArrayWriter out = new CharArrayWriter();
System.out.println("5");
    try{
        DOMUtilities.writeDocument(svgF.document, out);
    }

    catch(Exception e){
        System.out.println("projectWriter exception: "+e);
    }
    System.out.println("Before history repetition");
    CharArrayWriter history_string = new CharArrayWriter();
    for(int x = 0; x < po.history_elements.size(); x++){
        try{
            Element wrap = svgF.document.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg");
            wrap.appendChild(po.history_elements.get(x));
            DOMUtilities.writeNode(wrap, history_string);
            p.history_elements.add(history_string.toString());
        }
        catch(Exception e){
            System.out.println("projectWriter exception: "+e);
        }
    }
    System.out.println("After history repetition");
    FileFilter projectfilter = new FileNameExtensionFilter("Temporary project format (.lld)", "lld");
    JFileChooser jfc = new JFileChooser();
    jfc.addChoosableFileFilter(projectfilter);
    jfc.setDialogTitle("Save Project File");
    File file = jfc.getCurrentDirectory();
    System.out.println("The path is: "+file.getPath());
    File name_file = new File(po.project_name);
    jfc.setSelectedFile(name_file);
    int result = jfc.showSaveDialog(this);
    System.out.println(jfc.getSelectedFile().getName());
    if(result == JFileChooser.CANCEL_OPTION) return;
    String[] path = StringUtils.split(jfc.getCurrentDirectory().getPath(), "\\");
    String qualified_path = path[0];
    for(int i = 1; i < path.length; i++){
        qualified_path = qualified_path+"//" + path[i];
    }
    System.out.println("Written path is"+qualified_path+"//"+jfc.getSelectedFile().getName());
    try{
        ObjectOutputStream objOut = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(qualified_path+"///"+jfc.getSelectedFile().getName()+".lld")));
        objOut.writeObject(p);
        objOut.close();
        System.out.println("project written");
    }

    catch(Exception e){
        System.out.println("Line 701: Exception "+e);
    }
}

public int[] imageToArray(Image image){
    int width = image.getWidth(null);
        int height = image.getHeight(null);
        int[] fabric_array = new int[width*height];

        PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height, fabric_array, 0, width);

        try{
            pg.grabPixels();
        }
        catch(Exception e){
            System.out.println("Fabric.java pixelgrabber error: "+e);
        }
        return fabric_array;
}

public void imageWriterTester(int[] p){
    try{
        ObjectOutputStream objOut = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("Image "+".test")));
        objOut.writeObject(p);
        objOut.close();
        System.out.println("project written");
    }

    catch(Exception e){
        System.out.println("BufferdImage writing problem: "+e);
    }
}

    private static void splashInit()
    {
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
            splashTextArea = new Rectangle2D.Double(15., height*0.35, width * .45, 20.);
            splashProgressArea = new Rectangle2D.Double(width * .55, height*.60, width*.4, 12 );

            // create the Graphics environment for drawing status info
            splashGraphics = mySplash.createGraphics();
            font = new Font("Dialog", Font.PLAIN, 12);
            splashGraphics.setFont(font);

            // initialize the status info
            splashText("Starting");
            splashProgress(0);
        }
    }
    private static void appInit(){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                try{
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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
            splashGraphics.setPaint(Color.BLACK);
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
//        System.out.println("Splash Progress is "+splashProgress);
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
//    System.out.println("Sent parameter: "+args[0]);

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
