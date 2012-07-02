package Design_tool_prototype1;

import javax.swing.event.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.basic.BasicInternalFrameUI;

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

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.colorchooser.ColorSelectionModel;

public class App extends JRibbonFrame implements ChangeListener, ActionListener {

    JRibbon jr = new JRibbon();
    JCommandToggleButton[] buttons;
    JDesktopPane desk;
    JInternalFrame iframe;
    JInternalFrame toolframe;
    JInternalFrame color_frame;
    JInternalFrame alter_frame;
    JInternalFrame navigator_frame;
    JFrame frame;
    JButton line_bt;
    paintBoard pb;
    SVGConjurer svgc;
    alterStation alt;
    navigator nv = new navigator();
    JColorChooser colorChooser;
    JRibbonGallery gallery;
    final static String GALLERY_NAME = "Gallery";
    JRibbonBand band;
    JRibbonBand button_band;
    Image image;
    java.util.List<Fabric> fabrics_list = new ArrayList<Fabric>();

    private static class TopLeftDecoration implements
            DecoratedResizableIcon.IconDecorator {

        int number;

        public TopLeftDecoration(int number) {
            this.number = number;
        }

        @Override
        public void paintIconDecoration(Component c, Graphics g, int x, int y,
                int width, int height) {
            g.setColor(Color.blue.darker());
            g.drawString("" + number, x + 2, y
                    + g.getFontMetrics().getAscent());
        }
    }

    private static class BottomRightDecoration implements
            DecoratedResizableIcon.IconDecorator {

        int number;

        public BottomRightDecoration(int number) {
            this.number = number;
        }

        @Override
        public void paintIconDecoration(Component c, Graphics g, int x, int y,
                int width, int height) {
            /*
             * g.setColor(Color.red.darker()); g.drawString("" + number, x +
             * width - g.getFontMetrics().stringWidth("" + number) - 2, y +
             * height - g.getFontMetrics().getDescent());
             */
        }
    }

    private static class IconImageDecoration implements
            DecoratedResizableIcon.IconDecorator {

        Image icon_image;

        public IconImageDecoration(Image icon_image) {
            this.icon_image = icon_image;
        }

        @Override
        public void paintIconDecoration(Component c, Graphics g, int x, int y,
                int width, int height) {
            //g.setColor(Color.blue.darker());
//			g.drawImage(icon_image, 0, 0, null);
//                        g.drawImage(icon_image, 0, 0, null);
            g.drawImage(icon_image, 0, 0, null);
        }
    }

    public App() {
        frame = new JFrame("Clothing Fashion Designer");
        Image frame_image;
        try {
            File frame_icon = new File("curve_bt.gif");
            frame_image = ImageIO.read(frame_icon);
            frame.setIconImage(frame_image);
        } catch (Exception e) {
            System.out.println("frame icon error: " + e);
        }


        try {
            ObjectInputStream objIn = new ObjectInputStream(new BufferedInputStream(new FileInputStream("fabrics.dat")));
            fabrics_list = (ArrayList) (objIn.readObject());
            System.out.println(fabrics_list.size() + " fabrics in the list at the begining.");
            objIn.close();
        } catch (Exception e) {
            System.out.println("list error");
        }

        frame.setLayout(new BorderLayout());

        buttons = new JCommandToggleButton[fabrics_list.size()];
        for (int j = 0; j < fabrics_list.size(); j++) {

            try {
                File image_file = new File(fabrics_list.get(j).getFabricIcon());
                image = ImageIO.read(image_file);
            } catch (Exception e) {
                System.out.println("file error");
            }

            DecoratedResizableIcon dri = new DecoratedResizableIcon(
                    new DisabledResizableIcon(RibbonElementPriority.TOP, 52, 52), new IconImageDecoration(image),
                    new BottomRightDecoration(j));
            JCommandToggleButton jtb = new JCommandToggleButton("Texture " + j, dri);
            jtb.addActionListener(this);

            this.buttons[j] = jtb;
        }

        JRibbonFrame ribbonframe = new JRibbonFrame();

        JRibbon ribbon = new JRibbon();
        RibbonApplicationMenu ram = new RibbonApplicationMenu();

        //RibbonApplicationMenuEntryPrimary save_pattern = new RibbonApplicationMenuEntryPrimary(new EmptyResizableIcon(32), "Save selected Pattern", this, CommandButtonKind.ACTION_ONLY);

        RibbonApplicationMenuEntryPrimary amEntryNew = new RibbonApplicationMenuEntryPrimary(
                new EmptyResizableIcon(32), "New", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Invoked creating new document");
            }
        }, CommandButtonKind.ACTION_ONLY);
        ram.addMenuEntry(amEntryNew);



        ram.setDefaultCallback(new RibbonApplicationMenuEntryPrimary.PrimaryRolloverCallback() {

            @Override
            public void menuEntryActivated(JPanel targetPanel) {
                targetPanel.removeAll();
                JCommandButtonPanel openHistoryPanel = new JCommandButtonPanel(
                        CommandButtonDisplayState.MEDIUM);
                String groupName = "Default Documents";
                openHistoryPanel.addButtonGroup(groupName);
                for (int i = 0; i < 5; i++) {
                    JCommandButton historyButton = new JCommandButton(i
                            + "    " + "default" + i + ".html",
                            new EmptyResizableIcon(32));
                    historyButton.setHorizontalAlignment(SwingUtilities.LEFT);
                    openHistoryPanel.addButtonToLastGroup(historyButton);
                }
                openHistoryPanel.setMaxButtonColumns(1);
                targetPanel.setLayout(new BorderLayout());
                targetPanel.add(openHistoryPanel, BorderLayout.CENTER);
            }
        });

        ribbon.setApplicationMenu(ram);
        //this.getRibbon().setApplicationMenu(ram);

        band = new JRibbonBand("Texture Gallery", new EmptyResizableIcon(32));
        button_band = new JRibbonBand("Import", new EmptyResizableIcon(32));

        Map<RibbonElementPriority, Integer> visibleButtonCounts = new HashMap<RibbonElementPriority, Integer>();
        visibleButtonCounts.put(RibbonElementPriority.LOW, 4);
        visibleButtonCounts.put(RibbonElementPriority.MEDIUM, 5);
        visibleButtonCounts.put(RibbonElementPriority.TOP, 6);

        java.util.List<StringValuePair<java.util.List<JCommandToggleButton>>> galleryButtons = new ArrayList<StringValuePair<java.util.List<JCommandToggleButton>>>();
//		for (int i = 0; i < 2; i++) {
        java.util.List<JCommandToggleButton> galleryButtonsList = new ArrayList<JCommandToggleButton>();
        for (int j = 0; j < fabrics_list.size(); j++) {
            galleryButtonsList.add(this.buttons[j]);
        }
        galleryButtons.add(new StringValuePair<java.util.List<JCommandToggleButton>>(
                "Group ", galleryButtonsList));
//		}

        band.addRibbonGallery(GALLERY_NAME, galleryButtons,
                visibleButtonCounts, 6, 4, RibbonElementPriority.TOP);

        JCommandButton import_bt = new JCommandButton("Import");
        import_bt.addActionListener(this);

        button_band.addCommandButton(import_bt, RibbonElementPriority.MEDIUM);
        button_band.addCommandButton(new JCommandButton("Fill", new EmptyResizableIcon(32)),
                RibbonElementPriority.MEDIUM);
        button_band.addCommandButton(new JCommandButton("Import", new EmptyResizableIcon(32)),
                RibbonElementPriority.MEDIUM);
        button_band.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesRestrictive(band));

        gallery = band.getControlPanel().getRibbonGallery(
                GALLERY_NAME);
        ribbon.addTask(new RibbonTask("Texture", button_band, band));



        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(dim);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        desk = new JDesktopPane();

        alt = new alterStation();

        iframe = new JInternalFrame("Drawing Board", true, true, true, true);
        iframe.setIconifiable(false);
        iframe.setToolTipText("Your drawings go here");
        Cursor paint_cursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
        iframe.setCursor(paint_cursor);

        JComponent c = (BasicInternalFrameTitlePane) ((BasicInternalFrameUI) iframe.getUI()).getNorthPane();
        //c.setSize(c.getWidth(), 50);
        c.setPreferredSize(new Dimension(c.getPreferredSize().width, 50));

        iframe.setBounds(0, 0, dim.width - 400, dim.height - 100);
        iframe.setVisible(true);
        desk.add(iframe);

//        line_bt = new JButton("",new ImageIcon("line_bt 2.gif"));
//        line_bt.setToolTipText("Line drawing tool");

        toolframe = new JInternalFrame("Tool Board", true, true, true, true);
        toolframe.setToolTipText("Drawing tools");
        toolframe.setBounds(dim.width - 350, dim.height - 250, 350, 80);
        toolframe.setVisible(true);
        pb = new paintBoard();
        svgc = new SVGConjurer(dim, alt);
        alt.svgc = svgc;
        pb.alt = alt;
        pb.nv = nv;
        toolbox tb = new toolbox(svgc);
        toolframe.add(tb.getToolbox());
        desk.add(toolframe);

        colorChooser = new JColorChooser();
        ColorSelectionModel model = colorChooser.getSelectionModel();
        model.addChangeListener(this);

        color_frame = new JInternalFrame("Color Pallete", true, true, true, true);
        color_frame.setToolTipText("Color Pallete");
        color_frame.setBounds(dim.width - 400, 0, 400, 380);
        color_frame.add(colorChooser);
        color_frame.setVisible(true);
        desk.add(color_frame);

        alter_frame = new JInternalFrame("Alter Station", true, true, true, true);
        alter_frame.setToolTipText("Selected element altering tools");
        alter_frame.setBounds(dim.width - 400, dim.height - 380, 400, 200);
        alter_frame.add(alt.getAlterStation());
        alter_frame.setVisible(true);
        desk.add(alter_frame);

        navigator_frame = new JInternalFrame("Shape Navigator", true, true, true, true);
        navigator_frame.setToolTipText("Created shapes are lined up here");
        navigator_frame.setBounds(0, dim.height - 380, 200, 190);
        navigator_frame.add(nv.getNavigator());
        navigator_frame.setVisible(true);
        desk.add(navigator_frame);

        desk.setBackground(Color.lightGray);

        iframe.add(svgc.getBoard());
        System.out.println("Checkpoint");
        frame.add(desk, BorderLayout.CENTER);
        frame.add(ribbon, BorderLayout.NORTH);
//        this.add(frame);
        frame.setVisible(true);
//        this.setVisible(true);
        
    }

    public void stateChanged(ChangeEvent e) {
        svgc.color = colorChooser.getColor();
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            JCommandButton jcb = (JCommandButton) ae.getSource();
            if (jcb.getText().equals("Import")) {
//*            FabricImportScreen fis = new FabricImportScreen(this);
            }
        } catch (Exception e) {
            JCommandToggleButton jtb = (JCommandToggleButton) ae.getSource();
            System.out.println(jtb.getText());
            String tmp[] = jtb.getText().split("\\s");
            svgc.natUri = fabrics_list.get(Integer.parseInt(tmp[1])).getFabricMainImage().toString();
            System.out.println(svgc.natUri.toString());
        }
    }

    public void addFabric(Fabric fabric) {
        fabrics_list.add(fabric);
        System.out.println(fabrics_list.size() + " fabrics in the list.");
        fileWriter();
    }

    public void fileWriter() {
        try {
            ObjectOutputStream objOut = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("fabrics.dat")));
            objOut.writeObject(fabrics_list);
            objOut.close();
        } catch (Exception e) {
            System.out.println("Line 407: Exception " + e);
        }
    }

    public static void main(String args[]) throws Exception {
        JRibbonFrame.setDefaultLookAndFeelDecorated(true);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    //UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceRavenLookAndFeel");
                    App app = new App();
                } catch (Exception e) {
                    System.out.println("Look and feel error " + e);
                }
            }
        });
    }
}
