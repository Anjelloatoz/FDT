package Design_tool_prototype1;

import java.awt.event.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.io.ByteArrayOutputStream;
import org.apache.batik.util.Base64EncoderStream;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGMatrix;
import org.apache.batik.bridge.UpdateManagerListener;
import org.apache.batik.bridge.UpdateManagerEvent;

import org.apache.batik.ext.awt.image.codec.util.ImageEncoder;
import org.apache.batik.ext.awt.image.codec.png.PNGImageEncoder;
import org.w3c.dom.NodeList;

import java.awt.image.*;

import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.Text;


import org.apache.commons.lang.StringUtils;

import java.awt.geom.*;
import org.apache.batik.ext.awt.geom.PathLength;

import org.apache.batik.bridge.UpdateManager;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.svg.SVGLocatableSupport;
import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGRect;
import org.w3c.dom.Element;
import org.apache.batik.svggen.*;
import org.apache.batik.dom.util.DOMUtilities;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.awt.geom.AffineTransform;
import java.io.StringReader;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.undo.*;
import java.awt.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.CharArrayWriter;
import org.w3c.dom.DOMException;

public class SVGConjurer extends JFrame implements ChangeListener, MouseListener, MouseMotionListener, KeyListener, MouseWheelListener, EventListener, UpdateManagerListener {
	static final long serialVersionUID = 333333;
	// Namespace string, to be used throughout the class
	final String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        static final String XLINK_NAMESPACE_URI = "http://www.w3.org/1999/xlink";

	private int radius = 4;
        UndoManager manager = new UndoManager();
        int shape_type_number = 0;
        int tmp_shape_type_number = 0;
        Point2D last_location = null;
        java.util.ArrayList<Point2D> current_drawing_locations = new ArrayList();
        java.util.ArrayList<Point2D> whole_path_locations_list = new ArrayList();
        boolean drawing_in_progress = false;
        int drawing_number = 0;
	JSVGCanvas canvas;
	Document document;
	public Element selected_shape;
        patternObject associated_pattern;
        private Element selected_component;
        private Element selected_drag_point;
        private Element selected_point;
        private Element selected_button_box;
        private Element clipboard_element;
        Element other_side;
        Element this_side;
        Element resize_frame;
        Element selected_text;
        Element fill_defs = null;
        Color color = new Color(0,0,0);
        private int scl = 10;
        Point2D mouse_point = new Point2D.Double(0, 0);
        private boolean moveable = false;
        private boolean component_moveable = false;
        private boolean drag_point_moveable = false;
        boolean pathDragging = false;
        private boolean edit_point_moveable = false;
        private boolean selected_point_moveable = false;
        private boolean SE_resizeable = false;
        private Double shape_X;
        private Double shape_Y;
        private Double translate_X = 0.0;
        private Double translate_Y = 0.0;
        private Double shape_scale;
        private Point2D pressed_point = null;
        java.util.List<Element> location_list = new ArrayList();
        java.util.List<Element> location_coordinates_list = new ArrayList();
        Line2D axis_X = new Line2D.Double(0, 0, 100, 100);
        Line2D axis_Y = new Line2D.Double(0, 0, 100, 100);
        private Point2D mouse_location;
        private Point2D mouse_released;
        private Point2D selected_component_station_point;
        private Point2D selected_shape_station_point;
        private Point2D selected_component_dimensions;
        private Point2D selected_text_station_point;
        Point2D before_drag_edit_point;
        String before_drag_drawing = "";
        Point2D after_drag_edit_point;
        String after_drag_drawing = "";

        Point2D prev_point_coords;
        Cursor cross_hair = new Cursor(Cursor.CROSSHAIR_CURSOR);
        Cursor dflt_cursor = new Cursor(Cursor.DEFAULT_CURSOR);
        Cursor hand_cursor = new Cursor(Cursor.HAND_CURSOR);
        Cursor move_cursor = new Cursor(Cursor.MOVE_CURSOR);
        String natUri;
        String fillUri;
        BufferedImage fill_image;
        String left_pattern_coords = "";
        String right_pattern_coords = "";
        String prev_pattern_coords = "";
        alterStation as;
        ribbonTest rt;
        private boolean ctrl = false;
        private boolean alt = false;
        private boolean shift = false;
        private boolean delete = false;

        boolean textPath = false;
        boolean text_moveable = false;
        boolean symmetric = false;
        DrawingBoardFooter dbf;
        SVGMatrix matrix = null;
        AffineTransform mouse_transformer = null;
        Cursor current_cursor = Cursor.getDefaultCursor();

        UndoableAddDrawingLocation uadl;
        UndoableAddPathLocation uapl;
        UndoableAddLocationCoordinates ualc;
        UndoableAddLocation ual;
        UndoableAddElement uae;
        UndoableSetAttribute usa;
        UndoableClearDrawingLocations ucdl;
        UndoableSwitchDrawingInProgress usdip;
        CompoundEdit compound_edit;
        UndoableReplaceList url;
        UndoableArcReplaceList uarl;

        UndoableRemoveElement ure;
        UndoableDrawingNumberDeduct udnd;
        UndoableDrawingNumberIncrease udni;
        UndoableAppendChild uac;
        UndoableRemoveChild urc;
        UndoableClearList ucl;
        UndoableClearPointList ucpl;
        UndoableClearLocation uclc;
        UndoableClearLocationList ucll;

        UndoablePatternDrag upd;
        projectObject project_object;
        treeHandler th;

        UndoableAddPatternObject uapo;
        UndoableRemovePatternByElement urpbe;

        Element destination_container;

        UndoableRemoveChildList urcl;
        String IDENTIFIER = "";
        resizeHandler resize_handler = null;
        Shape selected_shape_boundry = null;
        boolean on_bounds = false;
        boolean split_operation = false;
        int split_points = 0;
        boolean updating = false;
        int thread_counter = 0;
        int thread_up = 0;
        int thread_down = 0;
        UpdateManager update_manager = null;
        Thread axis_thread;
        Graphics2D canvas_graphics_2D;
        Graphics canvas_graphics;
        Color axis_color = new Color(72, 251,47);

        public SVGConjurer(){

        }
	public SVGConjurer(Dimension dim, alterStation as, DrawingBoardFooter dbf, String Identifier){
            super("SVG Conjurer");
            this.as = as;
            this.dbf = dbf;
            this.IDENTIFIER = Identifier;
            dbf.manager = manager;
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Cursor paint_cursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
            JPanel panel = new JPanel();
            JPanel p = new JPanel();
            try{
                File nat_file = new File("FW.PNG");
                natUri = nat_file.toURI().toString();
            }
            catch(Exception e){
                p1("file Exception");
            }
            canvas = new LJSVGCanvas();
            canvas.setMySize(dim);
            canvas.addMouseListener(this);
            canvas.addMouseMotionListener(this);
            canvas.addMouseWheelListener(this);
            canvas.addKeyListener(this);
            canvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
            canvas.setCursor(paint_cursor);
            canvas.setRecenterOnResize(true);
            canvas.setDisableInteractions(true);
            panel.setLayout(new BorderLayout());
            panel.add("North", p);
            panel.add("Center", canvas);
            panel.setCursor(paint_cursor);
            this.setContentPane(panel);
            this.pack();
            this.setBounds(150, 150, this.getWidth(), this.getHeight());
            DOMImplementation dom = SVGDOMImplementation.getDOMImplementation();
            document = dom.createDocument(svgNS, "svg", null);
            canvas.setDocument(document);

            this_side = document.createElementNS(svgNS, "svg");
            this_side.setAttribute("id", "this_side");
            other_side = document.createElementNS(svgNS, "svg");
            other_side.setAttribute("id", "other_side");
            document.getDocumentElement().appendChild(other_side);
            
            setPlates();
            drawAxises();
            document.getDocumentElement().appendChild(this_side);
        }

	public void mouseClicked(MouseEvent e) {
            removeResizeHandler();
            if(!splitChecker()){
                return;
            }
            if((shape_type_number != 0) && ((!symmetric|e.getX()>222))){
                last_location = e.getPoint();
                compound_edit = new CompoundEdit();

                uadl = new UndoableAddDrawingLocation(this, last_location);
                compound_edit.addEdit(uadl);

                uapl = new UndoableAddPathLocation(this, last_location);
                compound_edit.addEdit(uapl);
                
                Element coord_X = createLocationCoordinateX();
                Element coord_Y = createLocationCoordinateY();

                ualc = new UndoableAddLocationCoordinates(this, coord_X, coord_Y);
                compound_edit.addEdit(ualc);

                Element location = drawLocation();

                ual = new UndoableAddLocation(this, location, location_list, document);
                compound_edit.addEdit(ual);

                if(current_drawing_locations.size() == shape_type_number){
                    drawSegment();
                }
                else{
                    compound_edit.end();
                    manager.addEdit(compound_edit);
                    dbf.manager = manager;
                    dbf.updateButtons();
                }
            }
	}

	public void mousePressed(MouseEvent e) {
            pressed_point = e.getPoint();
        }

	public void mouseReleased(MouseEvent e) {
            moveable = false;
            component_moveable = false;
            selected_point_moveable = false;
            mouse_released = e.getPoint();
            SE_resizeable = false;
        }

        public void mouseWheelMoved(MouseWheelEvent e){
            AffineTransform af = canvas.getRenderingTransform();
            double x = af.getScaleX();
            double y = af.getScaleY();
            double i = (double)e.getWheelRotation();
            af.setToScale(x+(i/10), y+(i/10));
            System.out.println(x+(i/100)+" "+y+(i/100));
            canvas.setRenderingTransform(af);
        }

        public void mouseEntered(MouseEvent e) {
            try{
                rt.iframe.setSelected(true);
            }
            catch(Exception ex){
                p2("mouse entered and internal frame set selected error");
            }
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseDragged(MouseEvent e){
            as.setCoordinates(e.getX(), e.getY());
            shape_X = e.getX()- pressed_point.getX();
            shape_Y = e.getY() - pressed_point.getY();
            if(resize_handler!=null){
                resize_handler.drag(e.getX(), e.getY());
                return;
            }

            double x = mouse_point.getX();
            double y = mouse_point.getY();
            double tmp_x = e.getX()-translate_X;
            double tmp_y = e.getY()-translate_Y;
            translate_X = (double)e.getX();
            translate_Y = (double)e.getY();

            mouse_location = e.getPoint();

            if(ctrl){
                mouse_point = new Point2D.Double(x, e.getY());
            }
            else if(alt){
                mouse_point = new Point2D.Double(e.getX(), y);
            }
            else{
                mouse_point = e.getPoint();
            }
            
            mouse_location = e.getPoint();            
            axisAdjust();

            if (moveable && shift){
                relocate(e.getX(), e.getY());
            }
            if (text_moveable){
                text_relocate(tmp_x, tmp_y);
            }

            if(drag_point_moveable){
                patternDrag();
            }

            if(component_moveable||selected_point_moveable){
                componentRelocate(e.getX(), e.getY());
            }

            if(SE_resizeable){
                SEResizer(e.getX(), e.getY());
            }

            if((edit_point_moveable)){
                currentDrawingDrag();
            }
        }
        public void mouseMoved(MouseEvent e) {
            canvas.setCursor(current_cursor);
            double x = mouse_point.getX();
            double y = mouse_point.getY();
            mouse_location = e.getPoint();
            
            if(ctrl){
                mouse_point = new Point2D.Double(x, e.getY());
            }
            else if(alt){
                mouse_point = new Point2D.Double(e.getX(), y);
            }
            else{
                mouse_point = e.getPoint();
            }
            as.setCoordinates(e.getX(), e.getY());
            axisAdjust();

            if(last_location != null){
//                adjustPrediction();
            }
        }

	public void stateChanged(ChangeEvent e) {
	}

        public void testText(final String font_name){
            Runnable runnable = new Runnable() {

                public void run() {
                    try {
                        selected_text.setAttributeNS(null, "font-family", font_name + ", Arial, sans-serif");
                    } catch (Exception e) {
                        System.out.println("Exception occured in the set text method.");
                    }
                }
            };
            UpdateManager um = canvas.getUpdateManager();
            um.addUpdateManagerListener(this);
            try{
                System.out.println("Line 441");
                um.getUpdateRunnableQueue().invokeLater(runnable);
                System.out.println("Line 442");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 444: "+EX);
            }
        }

        public void testTextColor(final Color text_color){
            Runnable runnable = new Runnable() {

                public void run() {
                    try {
                        selected_text.setAttribute("fill", "rgb(" + text_color.getRed() + "," + text_color.getGreen() + "," + text_color.getBlue() + ")");
                    } catch (Exception ex) {
                        System.out.println("SVGC: not text operation: " + ex);
                    }
                }
            };
            UpdateManager um = canvas.getUpdateManager();
            um.addUpdateManagerListener(this);
            try{
                System.out.println("Line 464");
                um.getUpdateRunnableQueue().invokeLater(runnable);
                System.out.println("Line 466");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 465: "+EX);
            }
        }

        public void testSetText(final String text){
            Element e = document.getElementById(selected_text.getAttribute("id")+"_textPath");
            Shape s = null;
            try{
                s = org.apache.batik.parser.AWTPathProducer.createShape(new StringReader(e.getAttributeNS(null,"d")), GeneralPath.WIND_EVEN_ODD);
            }
            catch(Exception ex){
                System.out.println("Exception caught in the testsettext method.");
            }
            final PathLength pl = new PathLength(s);

            Runnable runnable = new Runnable() {
                public void run() {
                    ((Text) (selected_text.getFirstChild().getFirstChild())).setData(text);
                    selected_text.setAttribute("textLength", pl.lengthOfPath() + "");
                }
            };
            UpdateManager um = canvas.getUpdateManager();
            try{
                System.out.println("Line 492");
                um.getUpdateRunnableQueue().invokeLater(runnable);
                System.out.println("Line 494");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait491: "+EX);
            }
        }

        public void setTextSize(final int size){
            Element e = document.getElementById(selected_text.getAttribute("id")+"_textPath");
            Shape s = null;
            try{
                s = org.apache.batik.parser.AWTPathProducer.createShape(new StringReader(e.getAttributeNS(null,"d")), GeneralPath.WIND_EVEN_ODD);
            }
            catch(Exception ex){
                System.out.println("Exception caught in the testsettext method.");
            }
            final PathLength pl = new PathLength(s);
            Runnable runnable = new Runnable() {
                public void run() {
                    selected_text.setAttribute("font-size", size + "");
                    selected_text.setAttribute("textLength", pl.lengthOfPath() + "");
                }
            };
            UpdateManager um = canvas.getUpdateManager();
            um.addUpdateManagerListener(this);
            try{
                um.getUpdateRunnableQueue().invokeLater(runnable);
            }
            catch(Exception EX){
                System.out.println("Line 523");
                System.out.println("Invoke and Wait 517: "+EX);
                System.out.println("Line 525");
            }
        }

        public void editTextPath(final int size){
            Element e = document.getElementById(selected_text.getAttribute("id")+"_textPath");
            final Element tmp_path = (Element)e.cloneNode(true);
            tmp_path.setAttributeNS (null, "id", "tmp_textPath");
            tmp_path.setAttributeNS (null, "stroke", "black");
            tmp_path.setAttributeNS (null, "stroke-width", "1");
            tmp_path.setAttributeNS(null, "fill", "none");
            selected_shape = tmp_path;
            textPath = true;
            Runnable runnable = new Runnable() {
                public void run() {
                    selected_text.getParentNode().appendChild(tmp_path);
                }
            };
            UpdateManager um = canvas.getUpdateManager();
            um.addUpdateManagerListener(this);
            try{
                System.out.println("Line 546");
                um.getUpdateRunnableQueue().invokeLater(runnable);
                System.out.println("Line 548");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 541: "+EX);
            }
            showPoints();
        }

        private void drawAxises(){
            Line2D centreX = new Line2D.Double(0, canvas.getHeight()/2, canvas.getWidth(), canvas.getHeight()/2);
            Line2D centreY = new Line2D.Double(canvas.getWidth()/2, 0, canvas.getWidth()/2, canvas.getHeight());

            axis_X = new Line2D.Double();
            axis_Y = new Line2D.Double();

            Graphics graphics = canvas.getGraphics();
            Graphics2D graphics2d = (Graphics2D)graphics;
            graphics2d.setColor(Color.GRAY);
            graphics2d.draw(centreX);
            graphics2d.draw(centreY);
            graphics2d.setColor(Color.PINK);
            graphics2d.draw(axis_X);
            graphics2d.draw(axis_Y);

            graphics2d.dispose();
//            axisRunner();
        }
        public void setPlates(){
                    try {
                        document.getDocumentElement().removeChild(other_side);
                    } catch (DOMException domex) {
                        System.out.println("The OTHER_SIDE is not found");
                    }
                    catch (Exception gen_ex) {
                        System.out.println("SetPlates General Exception: "+gen_ex);
                    }
                    try {
                        document.getDocumentElement().removeChild(this_side);
                    }
                    catch (DOMException domex) {
                        System.out.println("The THIS_SIDE is not found");
                    }
                    catch (Exception gen_ex) {
                        System.out.println("SetPlates General Exception: "+gen_ex);
                    }
                    try {
                        document.getDocumentElement().removeChild(resize_frame);
                    }
                    catch (DOMException domex) {
                        System.out.println("The RESIZE_FRAME is not found");
                    }
                    catch (Exception gen_ex) {
                        System.out.println("SetPlates General Exception: "+gen_ex);
                    }

            other_side = document.createElementNS(svgNS, "svg");
            this_side = document.createElementNS(svgNS, "svg");
            resize_frame = document.createElementNS(svgNS, "svg");
            other_side.setAttribute("id", "other_side");
            this_side.setAttribute("id", "this_side");
            resize_frame.setAttribute("id", "resize_frame");
            document.getDocumentElement().appendChild(other_side);
            document.getDocumentElement().appendChild(this_side);
            document.getDocumentElement().appendChild(resize_frame);
        }

        private void drawPrediction(){
            final Element root = document.getDocumentElement();
            Element prediction = null;

            if(current_drawing_locations.size() == 1 ||current_drawing_locations.size() == 4){
                prediction = document.createElementNS(svgNS, "path");
                prediction.setAttribute("d", "M "+ last_location.getX()+" "+last_location.getY()+" L "+mouse_point.getX()+" "+mouse_point.getY());
            }

            else if(current_drawing_locations.size() == 2){
                if(shape_type_number == 2){
                    prediction = document.createElementNS(svgNS, "path");
                    prediction.setAttribute("d", "M "+ last_location.getX()+" "+last_location.getY()+" L "+mouse_point.getX()+" "+mouse_point.getY());
                }

                else{
                    prediction = document.createElementNS(svgNS, "path");
                    prediction.setAttribute("d", "M "+ current_drawing_locations.get(0).getX()+" "+current_drawing_locations.get(0).getY()+" Q "+current_drawing_locations.get(1).getX()+" "+current_drawing_locations.get(1).getY()+" "+mouse_point.getX()+" "+mouse_point.getY());
                }
            }
            else if(current_drawing_locations.size() == 3){
                prediction = document.createElementNS(svgNS, "path");
                prediction.setAttribute("d", "M "+ current_drawing_locations.get(0).getX()+" "+current_drawing_locations.get(0).getY()+" C "+current_drawing_locations.get(1).getX()+" "+current_drawing_locations.get(1).getY()+" "+current_drawing_locations.get(2).getX()+" "+current_drawing_locations.get(2).getY()+" "+mouse_point.getX()+" "+mouse_point.getY());
            }

            prediction.setAttribute("stroke", "orangered");
            prediction.setAttribute("stroke-width", "1");
            prediction.setAttribute("stroke-dasharray", "9, 5");
            prediction.setAttribute("id", "prediction");
            prediction.setAttribute("fill", "none");
            final Element prediction_duplicate = (Element)prediction.cloneNode(true);
            Runnable runnable = new Runnable() {
                public void run() {
                    root.appendChild(prediction_duplicate);
                }
            };
            UpdateManager um = canvas.getUpdateManager();
            um.addUpdateManagerListener(this);
            try{
                System.out.println("Line 634");
                um.getUpdateRunnableQueue().invokeLater(runnable);
                System.out.println("Line 636");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 627: "+EX);
            }
        }
        

        public void adjustPrediction(){
            final Element e = document.getElementById("prediction");
            String d = e.getAttribute("d");
            d = StringUtils.substringBeforeLast(d, " ");
            final String d_string = StringUtils.substringBeforeLast(d, " ");

            Runnable runnable = new Runnable() {
                public void run() {
                    e.setAttribute("d", d_string + " " + mouse_point.getX() + " " + mouse_point.getY());
                }
            };
            UpdateManager um = canvas.getUpdateManager();
            um.addUpdateManagerListener(this);
            try{
                System.out.println("Line 658");
                um.getUpdateRunnableQueue().invokeLater(runnable);
                System.out.println("Line 660");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 649: "+EX);
            }
        }

        private Element createLocationCoordinateX(){
            Element coord_X = document.createElementNS(svgNS, "line");
            coord_X.setAttributeNS(null, "id", "axis_X");
            coord_X.setAttributeNS(null, "stroke", "lightsteelblue");
            coord_X.setAttributeNS(null, "stroke-width", "1");

            coord_X.setAttribute("x1","0");
            coord_X.setAttribute("y1",""+last_location.getY());
            coord_X.setAttribute("x2",""+getWidth());
            coord_X.setAttribute("y2",""+last_location.getY());

            return coord_X;
        }

        private Element createLocationCoordinateY(){
            Element coord_Y = document.createElementNS(svgNS, "line");
            coord_Y.setAttributeNS(null, "id", "axis_Y");
            coord_Y.setAttributeNS(null, "stroke", "lightsteelblue");
            coord_Y.setAttributeNS(null, "stroke-width", "1");

            coord_Y.setAttribute("x1",""+last_location.getX());
            coord_Y.setAttribute("y1","0");
            coord_Y.setAttribute("x2",""+last_location.getX());
            coord_Y.setAttribute("y2",""+getHeight());

            return coord_Y;
        }

        private Element drawLocation(){
            Element location = document.createElementNS(svgNS, "circle");
            location.setAttributeNS(null, "id", "location");
            location.setAttributeNS(null, "stroke", "darkslateblue");
            if(on_bounds){
                location.setAttributeNS (null, "fill", "brown");
            }
            else{
                location.setAttributeNS (null, "fill", "lightsteelblue");
            }
            location.setAttributeNS(null, "stroke-width", "1");
            location.setAttributeNS(null, "r", "" + radius);
            location.setAttributeNS(null, "cx", "" + last_location.getX());
            location.setAttributeNS(null, "cy", "" + last_location.getY());
            return location;
        }

    /*private void drawLocationCoordinates(){
    Runnable r = new Runnable() {
    public void run() {
    Element root = document.getDocumentElement();
    Element coord_X = document.createElementNS(svgNS, "line");
    coord_X.setAttributeNS(null, "id", "axis_X");
    coord_X.setAttributeNS(null, "stroke", "lightsteelblue");
    coord_X.setAttributeNS(null, "stroke-width", "1");

    Element coord_Y = document.createElementNS(svgNS, "line");
    coord_Y.setAttributeNS(null, "id", "axis_Y");
    coord_Y.setAttributeNS(null, "stroke", "lightsteelblue");
    coord_Y.setAttributeNS(null, "stroke-width", "1");

    coord_X.setAttribute("x1","0");
    coord_X.setAttribute("y1",""+last_location.getY());
    coord_X.setAttribute("x2",""+getWidth());
    coord_X.setAttribute("y2",""+last_location.getY());

    coord_Y.setAttribute("x1",""+last_location.getX());
    coord_Y.setAttribute("y1","0");
    coord_Y.setAttribute("x2",""+last_location.getX());
    coord_Y.setAttribute("y2",""+getHeight());
    //	root.appendChild(coord_Y);
    //      root.appendChild(coord_X);
    axis_X.getParentNode().insertBefore(coord_X, axis_X);
    axis_X.getParentNode().insertBefore(coord_Y, axis_X);
    location_coordinates_list.add(coord_Y);
    location_coordinates_list.add(coord_X);
    }
    };
    UpdateManager um = canvas.getUpdateManager();
    um.getUpdateRunnableQueue().invokeLater(r);
    }*/

        private void drawEditPoint(final double x, final double y, final int rad, final String fill_color){
            final Element location = document.createElementNS(svgNS, "circle");
            location.setAttributeNS(null, "id", "location");
            location.setAttributeNS(null, "stroke", "red");
            location.setAttributeNS (null, "fill", fill_color);
	    location.setAttributeNS(null, "stroke-width", "1");
	    location.setAttributeNS(null, "r", ""+rad);
	    location.setAttributeNS(null, "cx", "" + x);
	    location.setAttributeNS(null, "cy", "" + y);
            location_list.add(location);
            dragPointListeners(location);

            Runnable runnable = new Runnable() {
                public void run() {
                    document.getDocumentElement().appendChild(location);
                }
            };
            UpdateManager um = canvas.getUpdateManager();
            um.addUpdateManagerListener(this);
            try{
                System.out.println("Line 767");
                um.getUpdateRunnableQueue().invokeLater(runnable);
                System.out.println("Line 769");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 756: "+EX);
            }
        }

        private void drawSegment(){
            if(shape_type_number == 2) {
                drawLine();
            }
            if(shape_type_number == 4) {
                drawArc();
            }
        }

        private void drawLine(){
            Element current_drawing;
            if(drawing_in_progress){
                current_drawing = document.getElementById("drawing_"+project_object.patterns.size());
            }
            else{
                current_drawing = document.createElementNS(svgNS, "path");
                current_drawing.setAttributeNS (null, "id", "drawing_"+project_object.patterns.size());
                current_drawing.setAttributeNS(null, "pathLength", "100");
                current_drawing.setAttributeNS (null, "stroke", "black");
                current_drawing.setAttributeNS (null, "stroke-width", "1");
                current_drawing.setAttributeNS(null, "fill", "none");
                current_drawing.setAttributeNS(null, "d", "M "+current_drawing_locations.get(0).getX()+" "+current_drawing_locations.get(0).getY());
                uae = new UndoableAddElement(this, current_drawing, location_list.get(1));
                compound_edit.addEdit(uae);
            }
            usa = new UndoableSetAttribute(this, current_drawing, "d", current_drawing.getAttributeNS(null, "d")+" L "+current_drawing_locations.get(1).getX()+" "+current_drawing_locations.get(1).getY());
            compound_edit.addEdit(usa);
            
            url = new UndoableReplaceList(this);
            compound_edit.addEdit(url);

            usdip = new UndoableSwitchDrawingInProgress(this, true);
            compound_edit.addEdit(usdip);
            compound_edit.end();
            manager.addEdit(compound_edit);
            dbf.manager = manager;
            dbf.updateButtons();
            if(textPath){
                setTextPath(current_drawing);
                return;
            }
        }

        private void drawArc(){
            Element current_drawing;
            if(drawing_in_progress){
                current_drawing = document.getElementById("drawing_"+project_object.patterns.size());
            }
            else{
                current_drawing = document.createElementNS(svgNS, "path");
                current_drawing.setAttributeNS (null, "id", "drawing_"+project_object.patterns.size());
                current_drawing.setAttributeNS(null, "pathLength", "100");
                current_drawing.setAttributeNS (null, "stroke", "black");
                current_drawing.setAttributeNS (null, "stroke-width", "1");
                current_drawing.setAttributeNS(null, "fill", "none");
                current_drawing.setAttributeNS(null, "d", "M "+current_drawing_locations.get(0).getX()+" "+current_drawing_locations.get(0).getY());
                uae = new UndoableAddElement(this, current_drawing, location_list.get(1));
                compound_edit.addEdit(uae);
            }
            usa = new UndoableSetAttribute(this, current_drawing, "d", current_drawing.getAttributeNS(null, "d")+" C"+current_drawing_locations.get(1).getX()+" "+current_drawing_locations.get(1).getY()+" "+current_drawing_locations.get(2).getX()+" "+current_drawing_locations.get(2).getY()+" "+current_drawing_locations.get(3).getX()+" "+current_drawing_locations.get(3).getY());
            compound_edit.addEdit(usa);
            uarl = new UndoableArcReplaceList(this);
            compound_edit.addEdit(uarl);
            usdip = new UndoableSwitchDrawingInProgress(this, true);
            compound_edit.addEdit(usdip);
            compound_edit.end();
            manager.addEdit(compound_edit);
            dbf.manager = manager;
            dbf.updateButtons();
            if(textPath){
                setTextPath(current_drawing);
                return;
            }
        }

        public void removePrediction(){
            final Element e = document.getElementById("prediction");
            Runnable runnable = new Runnable() {
                public void run() {
                    e.getParentNode().removeChild(e);
                }
            };
            UpdateManager um = canvas.getUpdateManager();
            um.addUpdateManagerListener(this);
            try{
                System.out.println("Line 861");
                um.getUpdateRunnableQueue().invokeLater(runnable);
                System.out.println("Line 863");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 848: "+EX);
            }
        }

        public void completionAgent(){
            p4("Came into the Completion agent");
            if(pathDragging==true){
                finishPathDragging();
                pathDragging = false;
                return;
            }
            
            Element current_drawing = document.getElementById("drawing_"+project_object.patterns.size());
            if(textPath){
                finishTextPath(current_drawing);
                return;
            }

            if(split_operation){
                document.getElementById("split_boundry").getParentNode().removeChild(document.getElementById("split_boundry"));
                patternSplitter();
                split_points = 0;
                split_operation = false;
                return;
            }
            compound_edit = new CompoundEdit();
            finishDrawing(null, current_drawing);
            compound_edit.end();
            manager.addEdit(compound_edit);
            dbf.manager = manager;
            dbf.updateButtons();
            
            Runnable runnable = new Runnable() {
                public void run() {
                    try{
                        Element e = document.getElementById("prediction");
                        e.getParentNode().removeChild(e);
                    }
                    catch(Exception ex) {
                        System.out.println("The prediction could not be found.");
                    }
                }
            };
            UpdateManager um = canvas.getUpdateManager();
            um.addUpdateManagerListener(this);
            try{
                um.getUpdateRunnableQueue().invokeLater(runnable);
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 897: "+EX);
            }
            textPath = false;
        }

        public void finishPathDragging(){
            System.out.println("In the finish path dragging");

            if(location_list.size()>0){
                urcl = new UndoableRemoveChildList(this, (Element)location_list.get(0).getParentNode(), location_list);
                compound_edit.addEdit(urcl);
            }
        }

        public void finishTextPath(Element current_drawing){
            if(current_drawing!=null){
                current_drawing.getParentNode().removeChild(current_drawing);
            }
            else{
                document.getElementById("tmp_textPath").getParentNode().removeChild(document.getElementById("tmp_textPath"));
            }

            if(location_list.size()>0){
                urcl = new UndoableRemoveChildList(this, (Element)location_list.get(0).getParentNode(), location_list);
                compound_edit.addEdit(urcl);
            }

            if(location_coordinates_list.size()>0){
                urcl = new UndoableRemoveChildList(this, (Element)location_coordinates_list.get(0).getParentNode(), location_coordinates_list);
                compound_edit.addEdit(urcl);
            }

            usdip = new UndoableSwitchDrawingInProgress(this, false);
            compound_edit.addEdit(usdip);

            ucpl = new UndoableClearPointList(current_drawing_locations);
            compound_edit.addEdit(ucpl);

            ucpl = new UndoableClearPointList(whole_path_locations_list);
            compound_edit.addEdit(ucpl);

            uclc = new UndoableClearLocation(last_location);
            compound_edit.addEdit(uclc);

            textPath = false;

            DefaultMutableTreeNode new_text_node = new DefaultMutableTreeNode(selected_text);
            ((DefaultTreeModel)th.getTree().getModel()).insertNodeInto(new_text_node, th.seekNodeByObject(selected_shape, (DefaultMutableTreeNode)th.getTree().getModel().getRoot()), 0);
            registerTextListeners(selected_text);
            System.out.println("The registered element is a "+selected_text.getLocalName());
        }

        public void finishDrawing(final Element destination, final Element current_drawing){
            try{
                usa = new UndoableSetAttribute(this, current_drawing, "d", current_drawing.getAttributeNS(null, "d")+" Z");
                compound_edit.addEdit(usa);
            }
            catch(Exception e){
                System.out.println("Returning in Finishing.");
                return;
            }

            final Element g_element = document.createElementNS(svgNS, "g");
            g_element.setAttribute("id", "g_"+current_drawing.getAttribute("id"));
            Element svg_element = document.createElementNS(svgNS, "svg");
            svg_element.setAttribute("id", "svg_"+current_drawing.getAttribute("id"));
            svg_element.setAttribute("x", ""+0);
            svg_element.setAttribute("y", ""+0);

            svg_element.appendChild(g_element);
            Runnable runnable = new Runnable(){
                public void run(){

                    g_element.appendChild(current_drawing);
                    System.out.println("Finish drawing method thread completed.");
                }
            };
            update_manager.getUpdateRunnableQueue().invokeLater(runnable);
//*                  uac = new UndoableAppendChild(this, svg_element, current_drawing);
//*                  compound_edit.addEdit(uac);
                  

            if(destination_container == null){
                patternObject po = new patternObject();
                po.front = rt.svgF.document.createElementNS(svgNS, "svg");
                po.front.setAttribute("id", "frontAnjelloatoz@gmail.com");
                po.rear = rt.svgR.document.createElementNS(svgNS, "svg");
                po.rear.setAttribute("id", "rearAnjelloatoz@gmail.com");
                rt.svgF.this_side.appendChild(po.front);
                rt.svgR.this_side.appendChild(po.rear);

                if(IDENTIFIER.equals("front")){
                    po.front.appendChild(svg_element);
                }

                else{
                    po.rear.appendChild(svg_element);
                }
/*                      uac = new UndoableAppendChild(this, root, po.front);
                      compound_edit.addEdit(uac);
                      uac = new UndoableAppendChild(this, root, po.rear);
                      compound_edit.addEdit(uac);*/


                uapo = new UndoableAddPatternObject(po, project_object, th, rt);
                compound_edit.addEdit(uapo);
                System.out.println("Before calling refresh tree 2");
//                th.refreshTree(project_object, rt);
                System.out.println("After calling refresh tree 2");
            }

            else{
                uac = new UndoableAppendChild(this, (Element)destination_container.getParentNode(), svg_element);
                compound_edit.addEdit(uac);
                System.out.println("Before calling refresh tree 1");
                
                System.out.println("Before calling refresh tree 1");
            }
                  
            registerPatternListeners(current_drawing);

            udni = new UndoableDrawingNumberIncrease(this);
            compound_edit.addEdit(udni);

/*            for(int i = 0; i < location_list.size(); i++){
                urc = new UndoableRemoveChild(this, (Element)location_list.get(i).getParentNode(), location_list.get(i));
                compound_edit.addEdit(urc);
            }*/

            if(location_list.size()>0){
                urcl = new UndoableRemoveChildList(this, (Element)location_list.get(0).getParentNode(), location_list);
                compound_edit.addEdit(urcl);
            }

/*            for(int i = 0; i < location_coordinates_list.size(); i++){
                urc = new UndoableRemoveChild(this, (Element)location_coordinates_list.get(i).getParentNode(), location_coordinates_list.get(i));
                compound_edit.addEdit(urc);
            }*/
            if(location_coordinates_list.size()>0){
                urcl = new UndoableRemoveChildList(this, (Element)location_coordinates_list.get(0).getParentNode(), location_coordinates_list);
                compound_edit.addEdit(urcl);
            }
            
/*            ucll = new UndoableClearLocationList(location_list);
            compound_edit.addEdit(ucll);
            
            ucll = new UndoableClearLocationList(location_coordinates_list);
            compound_edit.addEdit(ucll);*/

            usdip = new UndoableSwitchDrawingInProgress(this, false);
            compound_edit.addEdit(usdip);

            ucpl = new UndoableClearPointList(current_drawing_locations);
            compound_edit.addEdit(ucpl);

            ucpl = new UndoableClearPointList(whole_path_locations_list);
            compound_edit.addEdit(ucpl);

            uclc = new UndoableClearLocation(last_location);
            compound_edit.addEdit(uclc);
//            destination_container = null;

            Thread check_thread_1 = new Thread("Sync Thread"){
            @Override
                public void run(){
                    while(update_manager.getUpdateRunnableQueue().iterator().hasNext()){}
                    setSelectedDrawing(current_drawing);
                    return;
                }
            };

            check_thread_1.start();
        }

        public void addNewLayer(Element element){
            int number_of_svgs = this_side.getElementsByTagName("svg").getLength();
            final Element new_container = document.createElementNS(svgNS, "svg");
            new_container.setAttribute("id", "svg_drawing_"+number_of_svgs+1);
            new_container.appendChild(element);
            registerPatternListeners(element);

            Runnable runnable = new Runnable() {
                public void run() {
                    this_side.appendChild(new_container);
                }
            };
            UpdateManager um = canvas.getUpdateManager();
            um.addUpdateManagerListener(this);
            try{
                System.out.println("Line 1086");
                um.getUpdateRunnableQueue().invokeLater(runnable);
                System.out.println("Line 1088");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait1069: "+EX);
            }
            th.refreshTree(project_object, rt);
        }

        public void addNewPattern(){
            patternObject po = new patternObject();
            Element new_front = document.createElementNS(svgNS, "svg");
            Element new_rear = rt.svgR.document.createElementNS(svgNS, "svg");
            new_front.setAttribute("id", "rear");
            po.front = new_front;
            po.rear = new_rear;
            this_side.appendChild(po.front);
            rt.svgR.this_side.appendChild(po.rear);
            uapo = new UndoableAddPatternObject(po, project_object, th, rt);
            compound_edit.addEdit(uapo);
            th.refreshTree(project_object, rt);
        }

        public Element reverseMaker(Node root){
            NodeList nl = root.getChildNodes();
            for(int i = 0; i < nl.getLength(); i++){
                try{
                    if(nl.item(nl.getLength()-1).hasChildNodes()){
                        reverseMaker(nl.item(nl.getLength()-1));
                    }
                    if(!nl.item(nl.getLength()-1).isSameNode(nl.item(i)))
                    root.insertBefore(nl.item(nl.getLength()-1), nl.item(i));
                }
                catch(Exception ex){
                    System.out.println("SVGConjurer Could not insert the node: "+ex);
                    root.appendChild(nl.item(nl.getLength()-1));
                }
            }
            return (Element)root;
        }

        public void symmetricFinishDrawing(){
//            System.out.println("In the symmetricFinishDrawing");
            Element current_drawing = document.getElementById("drawing_"+project_object.patterns.size());

            java.awt.Shape tmp_shape = null;
            try{
                tmp_shape = org.apache.batik.parser.AWTPathProducer.createShape(new StringReader(current_drawing.getAttributeNS(null,"d")), GeneralPath.WIND_EVEN_ODD);
            }
            catch(Exception e){
                System.out.println("symmetricFinishDrawing Exception");
            }
            java.awt.geom.AffineTransform at = new java.awt.geom.AffineTransform();
            at.scale(-1, 1);
            at.translate(-444, 0);

            java.awt.Shape mirrored_shape = at.createTransformedShape(tmp_shape);
            SVGGeneratorContext sgc = SVGGeneratorContext.createDefault(document);
            SVGPath mirrored = new SVGPath(sgc);

            Element tmp_element = (Element)current_drawing.cloneNode(true);
            tmp_element.setAttribute("d", mirrored.toSVG(mirrored_shape).getAttribute("d"));
            final Element left_element  = pathReverse(tmp_element);
//            System.out.println("The right side is :"+current_drawing.getAttribute("d"));
            final Element root = document.getDocumentElement();

            Runnable r = new Runnable(){
                public void run(){
                    root.appendChild(left_element);
                }
            };
            UpdateManager um = canvas.getUpdateManager();
            um.addUpdateManagerListener(this);
	    try{
                System.out.println("Line 1161");
                um.getUpdateRunnableQueue().invokeLater(r);
                System.out.println("Line 1163");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait1142: "+EX);
            }
            
            String test_d = "";
            PathIterator pi = mirrored_shape.getPathIterator(null);
            double[] split_path_coords = new double[6];
            
            Point2D begining_point = null;
            Point2D ending_point = null;

            while(!pi.isDone()){
                int segment = pi.currentSegment(split_path_coords);
                if(segment == pi.SEG_MOVETO){
                    test_d = "M "+split_path_coords[0]+" "+split_path_coords[1];
                    if(ending_point == null){
                        ending_point = new Point2D.Double(split_path_coords[0], split_path_coords[1]);
                    }
                    begining_point = new Point2D.Double(split_path_coords[0], split_path_coords[1]);
                }

                if(segment == pi.SEG_LINETO){
                    test_d = test_d+" L "+split_path_coords[0]+" "+split_path_coords[1];
                    if(ending_point.equals(null)){
                        ending_point = new Point2D.Double(split_path_coords[0], split_path_coords[1]);
                    }
                    begining_point = new Point2D.Double(split_path_coords[0], split_path_coords[1]);
                }

                if(segment == pi.SEG_CUBICTO){
                    test_d = test_d+" C "+split_path_coords[0]+" "+split_path_coords[1]+" "+split_path_coords[2]+" "+split_path_coords[3]+" "+split_path_coords[4]+" "+split_path_coords[5];
                    if(ending_point.equals(null)){
                        ending_point = new Point2D.Double(split_path_coords[0], split_path_coords[1]);
                    }
                    begining_point = new Point2D.Double(split_path_coords[4], split_path_coords[5]);
                }
                pi.next();
            }
/*            System.out.println("The begining point is "+begining_point);
            System.out.println("The ending point is "+ending_point);
            System.out.println("The begining point is "+whole_path_locations_list.get(0));
            System.out.println("The ending point is "+whole_path_locations_list.get(whole_path_locations_list.size()-1));
*/
            if(begining_point.getX()==whole_path_locations_list.get(0).getX() && begining_point.getY()==whole_path_locations_list.get(0).getY()){
//                right_path.append(tmp_shape, true);
            }
            else{
                JFrame f1 = new JFrame("Creating the symmetric pattern");
            Object[] options = {"Line","Curve","Cancel"};
            int ans = JOptionPane.showOptionDialog(f1, "Please select how the right patterns end joins with left patterns begining.", "Joining the left and right patterns", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
            if(ans == 0){
                current_drawing.setAttribute("d", current_drawing.getAttribute("d")+" L "+StringUtils.stripStart(left_element.getAttribute("d"),"M"));
//                System.out.println("After joining path is: "+current_drawing.getAttribute("d"));
            }
            else if(ans == 1){
                int y = (int)whole_path_locations_list.get(whole_path_locations_list.size()-1).getY();
                int gap = (int)((whole_path_locations_list.get(whole_path_locations_list.size()-1).getX())-begining_point.getX())/3;
                current_drawing.setAttribute("d", current_drawing.getAttribute("d")+" C "+((whole_path_locations_list.get(whole_path_locations_list.size()-1).getX())-gap)+" "+y+" "+((whole_path_locations_list.get(whole_path_locations_list.size()-1).getX())-(gap*2))+" "+y+" "+StringUtils.stripStart(left_element.getAttribute("d"),"M"));
//                System.out.println("After joining path is: "+current_drawing.getAttribute("d"));
            }
            if(ending_point.getX()==whole_path_locations_list.get(0).getX() && ending_point.getY()==whole_path_locations_list.get(0).getY()){
//                right_path.append(tmp_shape, true);
            }
            else{
                f1 = new JFrame("Creating the symmetric pattern");
            ans = JOptionPane.showOptionDialog(f1, "Please select how the right patterns is closing.", "Joining the left and right patterns", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
            if(ans == 0){
                current_drawing.setAttribute("d", current_drawing.getAttribute("d")+" L "+whole_path_locations_list.get(0).getX()+" "+whole_path_locations_list.get(0).getY());
//                System.out.println("After joining path is: "+current_drawing.getAttribute("d"));
            }
            else if(ans == 1){
                int y = (int)whole_path_locations_list.get(0).getY();
                int gap = (int)((whole_path_locations_list.get(0).getX())-ending_point.getX())/3;
                current_drawing.setAttribute("d", current_drawing.getAttribute("d")+" C "+((ending_point.getX())+gap)+" "+y+" "+((ending_point.getX())+(gap*2))+" "+y+" "+ whole_path_locations_list.get(0).getX()+" "+ whole_path_locations_list.get(0).getY()+" ");
//                System.out.println("After joining path is: "+current_drawing.getAttribute("d"));
            }
            }
            root.removeChild(left_element);
            finishDrawing(null, current_drawing);
            }
        }        

        public Element pathReverse(Element subject_element){
            String forward_path;
            String backward_path;
            String chopped_backward_path;

            java.awt.Shape tmp_shape = null;
            backward_path = "";
            try{
                tmp_shape = org.apache.batik.parser.AWTPathProducer.createShape(new StringReader(subject_element.getAttribute("d")), GeneralPath.WIND_EVEN_ODD);
            }
            catch(Exception e){
                p1("Line 764: pathReverse Error");
            }

            forward_path = "";
            PathIterator pi = tmp_shape.getPathIterator(null);
            double[] split_path_coords = new double[6];

            while(!pi.isDone()){
                int segment = pi.currentSegment(split_path_coords);
                if(segment == pi.SEG_MOVETO){
                    forward_path = "M "+split_path_coords[0]+" "+split_path_coords[1];
                    backward_path = split_path_coords[0]+" "+split_path_coords[1];
                }
                if(segment == pi.SEG_LINETO){
                    forward_path = forward_path+" L "+split_path_coords[0]+" "+split_path_coords[1];
                    backward_path =split_path_coords[0]+" "+split_path_coords[1]+" L "+backward_path;
//                    short_backward_path =
                }
                if(segment == pi.SEG_CUBICTO){
                    forward_path = forward_path+" C "+split_path_coords[0]+" "+split_path_coords[1]+" "+split_path_coords[2]+" "+split_path_coords[3]+" "+split_path_coords[4]+" "+split_path_coords[5];
                    backward_path = split_path_coords[4]+" "+split_path_coords[5]+" C "+split_path_coords[2]+" "+split_path_coords[3]+" "+split_path_coords[0]+" "+split_path_coords[1]+" "+backward_path;
                }
                pi.next();
            }
            chopped_backward_path = backward_path;
            backward_path = "M "+backward_path;
//            System.out.println("The Forward Path is "+forward_path);
//            System.out.println("The Backward Path is "+backward_path);
            Element reversed_element = (Element)subject_element.cloneNode(false);
            reversed_element.setAttribute("d", backward_path);
            return reversed_element;
        }

        public Element setGuideImage(final String image_path, final int width, final int height){
            final Element root = document.getDocumentElement();

            final Element my_image = document.createElementNS(svgNS, "image");
            final Element defs = document.createElementNS(svgNS, "defs");
            defs.setAttribute("id", "guide image defs");
            my_image.setAttributeNS(XLINK_NAMESPACE_URI, "xlink:href", image_path);
            my_image.setAttributeNS(null, "id", "natasha_png");
            int location_x = canvas.getWidth()/2-width/2;
            int location_y = canvas.getHeight()/2-height/2;
            if (location_x < 0) location_x = 0;
            if (location_y < 0) location_y = 0;
            my_image.setAttributeNS(null, "x", location_x+"");
            my_image.setAttributeNS (null, "y", location_y+"");
            my_image.setAttributeNS(null, "width", width+"");
            my_image.setAttributeNS(null, "height", height+"");
            my_image.setAttributeNS(null, "opacity", "0.7");
            Element pattern = document.createElementNS(svgNS, "pattern");
            pattern.setAttributeNS(null, "id", "pattern"+project_object.patterns.size());
            pattern.setAttributeNS(null, "patternUnits", "userSpaceOnUse");
            pattern.setAttributeNS(null, "width", "0.5in");
            pattern.setAttributeNS(null, "height", "0.5in");
            pattern.setAttributeNS(null, "x", "0.5in");
            pattern.setAttributeNS(null, "y", "0.5in");
            pattern.appendChild(my_image);
            defs.appendChild(pattern);

            Runnable runnable = new Runnable() {
                public void run() {
                    document.getDocumentElement().insertBefore(my_image, this_side);
                    root.appendChild(defs);
                }
            };
            UpdateManager um = canvas.getUpdateManager();
            um.addUpdateManagerListener(this);
            try{
                System.out.println("Line 1327");
                um.getUpdateRunnableQueue().invokeLater(runnable);
                System.out.println("Line 1329");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 1306: "+EX);
            }
            return my_image;
        }

        public void DeleteGuideImage(final Element e){
            final Element root = document.getDocumentElement();
            Runnable runnable = new Runnable() {
                public void run() {
                    root.removeChild(e);
                }
            };
            UpdateManager um = canvas.getUpdateManager();
            um.addUpdateManagerListener(this);
            try{
                System.out.println("Line 1347");
                um.getUpdateRunnableQueue().invokeLater(runnable);
                System.out.println("Line 1349");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 1324: "+EX);
            }
        }

        public String getOpacity(){
            Element image = document.getElementById("natasha_png");
            p1(image.getAttributeNS(null, "opacity"));
            return image.getAttributeNS(null, "opacity");
        }

        public void setOpacity(final double opacity){
            final Element image = document.getElementById("natasha_png");
            Runnable runnable = new Runnable() {
                public void run() {
                    image.setAttributeNS(null, "opacity", "" + opacity);
                }
            };
            UpdateManager um = canvas.getUpdateManager();
            um.addUpdateManagerListener(this);
            try{
                System.out.println("Line 1372");
                um.getUpdateRunnableQueue().invokeLater(runnable);
                System.out.println("Line 1374");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 1347: "+EX);
            }
        }

        public JSVGCanvas getBoard(){
            return canvas;
        }

        public void patternSplitter(){
            Element spath = document.getElementById("drawing_"+project_object.patterns.size());
            for(int i = 0; i< 8; i++){
                p1("");
            }
            p2("*********************************************************************************");
            p2("The Split path is "+spath.getAttribute("d"));
            p2("SELECTED SHAPE IS "+selected_shape.getAttributeNS(null,"d"));
            p2("*********************************************************************************");
                    java.awt.Shape s = null;

                    boolean splitable = true;
                    Point2D split_path_begining = whole_path_locations_list.get(0);
                    Point2D split_path_ending = whole_path_locations_list.get(whole_path_locations_list.size()-1);

                    try{
                        s = org.apache.batik.parser.AWTPathProducer.createShape(new StringReader(selected_shape.getAttributeNS(null,"d")), GeneralPath.WIND_EVEN_ODD);
                    }
                    catch(Exception e){
                        p1("Line 570: Exception caught at test Splitter");
                    }
                    for(int i = 1; i < whole_path_locations_list.size()-1; i++){
                        if(!s.contains(whole_path_locations_list.get(i))){
                            splitable = false;
                        }
                    }

                    if(splitable){
                        boolean begining_point_on_line = false;
                        boolean ending_point_on_line = false;
                        String new_shape = "";
                        Point2D last_point = null;
                        while(!begining_point_on_line || !ending_point_on_line){
                            double[] coords = new double[6];
                            PathIterator pi = s.getPathIterator(null);
                            int line_586_loop = 0;
                            while(!pi.isDone()){ p1("LOG LINE 587: line_586_loop = "+line_586_loop);
                                int segment = pi.currentSegment(coords);
                                if(segment == pi.SEG_MOVETO){
                                    new_shape = "M "+coords[0]+" "+coords[1];
                                    last_point = new Point2D.Double(coords[0], coords[1]);

                                    p1("LOG LINE 593: new_shape = "+new_shape);
                                    p1("LOG LINE 594: last_point = "+last_point);
                                }
                                else if(segment == pi.SEG_LINETO){

                                    if(((last_point.distance(split_path_begining)+split_path_begining.distance(coords[0], coords[1]))-last_point.distance(coords[0], coords[1]))<1.0 && !begining_point_on_line){
                                        if(last_point.getX()==split_path_begining.getX()&&last_point.getY()==split_path_begining.getY()){
                                            new_shape = new_shape +" L "+coords[0]+" "+coords[1];
                                            p1("LOG LINE 601: new_shape = "+new_shape);
                                        }
                                        else{
                                            new_shape = new_shape+" L "+split_path_begining.getX()+" "+split_path_begining.getY()+" L "+coords[0]+" "+coords[1];
                                            p1("LOG LINE 605: new_shape = "+new_shape);
                                        }
                                        begining_point_on_line = true;
                                        p1("LOG LINE 608: begining_point_on_line = "+begining_point_on_line);
                                    }
                                    else if(((last_point.distance(split_path_ending)+split_path_ending.distance(coords[0], coords[1]))-last_point.distance(coords[0], coords[1]))<1.0 && !ending_point_on_line){
                                        if(last_point.getX()==split_path_ending.getX()&&last_point.getY()==split_path_ending.getY()){
                                            new_shape = new_shape +" L "+coords[0]+" "+coords[1];
                                            p1("LOG LINE 613: new_shape = "+new_shape);
                                        }
                                        else{
                                            new_shape = new_shape+" L "+split_path_ending.getX()+" "+split_path_ending.getY()+" L "+coords[0]+" "+coords[1];
                                            p1("LOG LINE 617: new_shape = "+new_shape);
                                        }
                                        ending_point_on_line = true;
                                        p1("LOG LINE 620: ending_point_on_line = "+ending_point_on_line);
                                    }
                                    else{
                                        new_shape = new_shape+" L "+coords[0]+" "+coords[1]; p1("LOG LINE 622: new_shape = "+new_shape);
                                    }
                                    
                                    last_point = new Point2D.Double(coords[0], coords[1]);
                                    p1("LOG LINE 627: new_shape = "+new_shape);
                                    p1("LOG LINE 628: last_point = "+last_point);
                                }
                                else if(segment == pi.SEG_CUBICTO){
                                    CubicCurve2D tmp_curve = new CubicCurve2D.Double(last_point.getX(), last_point.getY(), coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);

                                    PathIterator curve_path_iterator = tmp_curve.getPathIterator(null, 0.01);
                                    double[] curve_coords = new double[2];
                                    double distance = 1.2;
                                    int segment_counter = 0;
                                    double percenter = 0;
                                    double total_length = 0;
                                    Point2D break_point = null;
                                    Point2D first_point = last_point;
                                    boolean one_point_found = false;

                                    int line_638_loop = 0;
                                    while(!curve_path_iterator.isDone()){
                                        p1("LOG LINE 644 Loop:= "+line_638_loop);
                                        int curve_segment = curve_path_iterator.currentSegment(curve_coords);
                                        if(curve_segment == curve_path_iterator.SEG_MOVETO){
                                            first_point = new Point2D.Double(curve_coords[0], curve_coords[1]);
                                            if(first_point.distance(split_path_begining)<1.0){
                                                percenter = segment_counter;
                                                break_point = split_path_begining;
                                                begining_point_on_line= true;
                                                one_point_found = true;

                                                p1("LOG LINE 654: percenter = "+percenter);
                                                p1("LOG LINE 655: break_point = "+break_point);
                                                p1("LOG LINE 656: begining_point_on_line = "+begining_point_on_line);
                                                p1("LOG LINE 657: one_point_found = "+one_point_found);
                                            }

                                            else if(first_point.distance(split_path_ending)<1.0){
                                                percenter = segment_counter;
                                                break_point = split_path_ending;
                                                ending_point_on_line= true;
                                                one_point_found = true;

                                                p1("LOG LINE 666: percenter = "+percenter);
                                                p1("LOG LINE 667: break_point = "+break_point);
                                                p1("LOG LINE 668: begining_point_on_line = "+begining_point_on_line);
                                                p1("LOG LINE 669: one_point_found = "+one_point_found);
                                            }
                                        }

                                        if(curve_segment == curve_path_iterator.SEG_LINETO){ lineSplitter(first_point, new Point2D.Double(curve_coords[0], curve_coords[1]), split_path_begining); lineSplitter(first_point, new Point2D.Double(curve_coords[0], curve_coords[1]), split_path_ending);
                                            Line2D line_segment = new Line2D.Double(first_point.getX(), first_point.getY(), curve_coords[0], curve_coords[1]);
                                            if(lineSplitter(first_point, new Point2D.Double(curve_coords[0], curve_coords[1]), split_path_begining)&& !begining_point_on_line && !one_point_found){
                                                distance = line_segment.ptSegDist(split_path_begining);
                                                percenter = total_length+first_point.distance(split_path_begining);
                                                break_point = split_path_begining;
                                                begining_point_on_line = true;
                                                one_point_found = true;

                                                p1("LOG LINE 682: distance = "+distance);
                                                p1("LOG LINE 683: percenter = "+percenter);
                                                p1("LOG LINE 684: break_point = "+break_point);
                                                p1("LOG LINE 685: begining_point_on_line = "+begining_point_on_line);
                                                p1("LOG LINE 686: one_point_found = "+one_point_found);
                                            }

                                            else if(lineSplitter(first_point, new Point2D.Double(curve_coords[0], curve_coords[1]), split_path_ending)&& !ending_point_on_line && !one_point_found){
                                                distance = line_segment.ptLineDist(split_path_ending);
                                                percenter = total_length+first_point.distance(split_path_ending);
                                                break_point = split_path_ending;
                                                ending_point_on_line = true;
                                                one_point_found = true;

                                                p1("LOG LINE 696: distance = "+distance);
                                                p1("LOG LINE 697: percenter = "+percenter);
                                                p1("LOG LINE 698: break_point = "+break_point);
                                                p1("LOG LINE 699: begining_point_on_line = "+begining_point_on_line);
                                                p1("LOG LINE 700: one_point_found = "+one_point_found);
                                            }

                                            total_length = total_length+first_point.distance(curve_coords[0], curve_coords[1]);
                                            first_point = new Point2D.Double(curve_coords[0], curve_coords[1]);
                                            p1("LOG LINE 703: first_point = "+first_point);
                                        }
                                        segment_counter++;
                                        curve_path_iterator.next();
                                        p1("LOG LINE 707: segment_counter = "+segment_counter);
                                        line_638_loop++;
                                    }
                                    p1("************************* OUT OF THE LINE_638_LOOP *********************************************");

                                    percenter = (percenter/total_length)*100;
                                    one_point_found = false;

                                    p1("LOG LINE 715: percenter = "+percenter);
                                    p1("LOG LINE 716: one_point_found = "+one_point_found);

                                    distance = 1.0;
                                    Point2D A = new Point2D.Double(last_point.getX()+((coords[0]-last_point.getX())/100)*percenter, last_point.getY()+((coords[1]-last_point.getY())/100)*percenter);
                                    Point2D B = new Point2D.Double(coords[0]+((coords[2]-coords[0])/100)*percenter, coords[1]+((coords[3]-coords[1])/100)*percenter);
                                    Point2D C = new Point2D.Double(coords[2]+((coords[4]-coords[2])/100)*percenter, coords[3]+((coords[5]-coords[3])/100)*percenter);
                                    Point2D M = new Point2D.Double(A.getX()+((B.getX()-A.getX())/100)*percenter, A.getY()+((B.getY()-A.getY())/100)*percenter);
                                    Point2D N = new Point2D.Double(B.getX()+((C.getX()-B.getX())/100)*percenter, B.getY()+((C.getY()-B.getY())/100)*percenter);

                                    if(break_point == split_path_begining){
                                        new_shape = new_shape+" C "+A.getX()+" "+A.getY()+" "+M.getX()+" "+M.getY()+" "+break_point.getX()+" "+break_point.getY()+" C "+N.getX()+" "+N.getY()+" "+C.getX()+" "+C.getY()+" "+coords[4]+" "+coords[5];
                                        p1("LOG LINE 727: new_shape = "+new_shape);
                                    }

                                    else if(break_point == split_path_ending){
                                        new_shape = new_shape+" C "+A.getX()+" "+A.getY()+" "+M.getX()+" "+M.getY()+" "+break_point.getX()+" "+break_point.getY()+" C "+N.getX()+" "+N.getY()+" "+C.getX()+" "+C.getY()+" "+coords[4]+" "+coords[5];
                                        p1("LOG LINE 732: new_shape = "+new_shape);
                                    }
                                    else{
                                        new_shape = new_shape+" C "+coords[0]+" "+coords[1]+" "+coords[2]+" "+coords[3]+" "+coords[4]+" "+coords[5];
                                        p1("LOG LINE 736: new_shape = "+new_shape);
                                    }
                                    last_point = new Point2D.Double(coords[4], coords[5]);
                                    p1("LOG LINE 739: last_point = "+last_point);
                                }
                                pi.next();
                                line_586_loop++;
                            }

                            p1("************************************************ OUT OF THE LINE 586 LOOP ********************************************************************");
                            p1("````````````````````````````NEW SHAPE is "+new_shape);

                            try{
                                s = org.apache.batik.parser.AWTPathProducer.createShape(new StringReader(new_shape), GeneralPath.WIND_EVEN_ODD);
                            }
                            catch(Exception e){
                                p1("Line 751: Exception caught at test Splitter");
                            }
                        }
                        String forward_path;
                        String backward_path;

                        Element tmp = document.getElementById("drawing_"+project_object.patterns.size());
                        java.awt.Shape original_split_path = null;
                        backward_path = "";
                        try{
                            original_split_path = org.apache.batik.parser.AWTPathProducer.createShape(new StringReader(tmp.getAttribute("d")), GeneralPath.WIND_EVEN_ODD);
                        }
                        catch(Exception e){
                            p1("Line 764: splitPathConverter Error");
                        }

                        forward_path = "";
                        PathIterator pi = original_split_path.getPathIterator(null);
                        double[] split_path_coords = new double[6];

                        int line_767_loop = 0;
                        while(!pi.isDone()){
                            int segment = pi.currentSegment(split_path_coords);
                            if(segment == pi.SEG_MOVETO){
                                forward_path = "L "+split_path_coords[0]+" "+split_path_coords[1];
                                backward_path = split_path_coords[0]+" "+split_path_coords[1];
                            }
                            if(segment == pi.SEG_LINETO){
                                forward_path = forward_path+" L "+split_path_coords[0]+" "+split_path_coords[1];
                                backward_path =split_path_coords[0]+" "+split_path_coords[1]+" L "+backward_path;
                            }
                            if(segment == pi.SEG_CUBICTO){
                                forward_path = forward_path+" C "+split_path_coords[0]+" "+split_path_coords[1]+" "+split_path_coords[2]+" "+split_path_coords[3]+" "+split_path_coords[4]+" "+split_path_coords[5];
                                backward_path = split_path_coords[4]+" "+split_path_coords[5]+" C "+split_path_coords[2]+" "+split_path_coords[3]+" "+split_path_coords[0]+" "+split_path_coords[1]+" "+backward_path;

                                p1("LOG LINE 793: forward_path = "+forward_path);
                                p1("LOG LINE 794: backward_path = "+backward_path);
                            }
                            pi.next();
                            line_767_loop++;
                        }

                        p1("********************************************* OUT OF THE LINE_767_LOOP ************************************************");

                        backward_path = "L "+backward_path;
                        String element1 = "";
                        String element2 = "";

                        p1("LOG LINE 806: backward_path = "+backward_path);

                        PathIterator split_iterator = s.getPathIterator(null);
                        boolean first_element = true;
                        boolean begining_found = false;
                        boolean ending_found = false;

                        int line_807_loop = 0;
                        while(!split_iterator.isDone()){
                            p1("LOG LINE 813: line_809_loop = "+line_807_loop);
                            int segment = split_iterator.currentSegment(split_path_coords);
                            if(segment==split_iterator.SEG_MOVETO){
                                element1 = element1+"M "+split_path_coords[0]+" "+split_path_coords[1];
                                p1("LOG LINE 817: element1 = "+element1);
                            }

                            if(first_element){
                                if(segment == split_iterator.SEG_LINETO){
                                    element1 = element1+" L "+split_path_coords[0]+" "+split_path_coords[1]; p1("LOG LINE 818: element1 = "+element1);
                                    if((split_path_begining.getX()==split_path_coords[0]) && (split_path_begining.getY()==split_path_coords[1]) && !begining_found){
                                        element1 = element1+" "+forward_path;
                                        element2 = element2+"M "+split_path_coords[0]+" "+split_path_coords[1];
                                        first_element = false;

                                        p1("LOG LINE 828: element1 = "+element1);
                                        p1("LOG LINE 829: element2 = "+element2);
                                        p1("LOG LINE 830: first_element = "+first_element);
                                        begining_found = true;
                                    }
                                    else if((split_path_ending.getX()==split_path_coords[0]) && (split_path_ending.getY()==split_path_coords[1]) && !ending_found){
                                        element1 = element1+" "+backward_path;
                                        element2 = element2+"M "+split_path_coords[0]+" "+split_path_coords[1];
                                        first_element = false;

                                        p1("LOG LINE 837: element1 = "+element1);
                                        p1("LOG LINE 838: element2 = "+element2);
                                        p1("LOG LINE 839: first_element = "+first_element);
                                        ending_found = true;
                                    }
                                }

                                if(segment == split_iterator.SEG_CUBICTO){
                                    element1 = element1+" C "+split_path_coords[0]+" "+split_path_coords[1]+" "+split_path_coords[2]+" "+split_path_coords[3]+" "+split_path_coords[4]+" "+split_path_coords[5];
                                    p1("LOG LINE 841: element1 = "+element1);
                                    if((split_path_begining.getX()==split_path_coords[4]) && (split_path_begining.getY()==split_path_coords[5]) && !begining_found){
                                        element1 = element1+" "+forward_path;
                                        element2 = "M "+split_path_coords[4]+" "+split_path_coords[5];
                                        first_element = false;

                                        p1("LOG LINE 851: element1 = "+element1);
                                        p1("LOG LINE 852: element2 = "+element2);
                                        p1("LOG LINE 853: first_element = "+first_element);
                                        begining_found = true;
                                    }
                                    else if((split_path_ending.getX()==split_path_coords[4]) && (split_path_ending.getY()==split_path_coords[5]) && !ending_found){
                                        element1 = element1+" "+backward_path;
                                        element2 = element2+"M "+split_path_coords[4]+" "+split_path_coords[5];
                                        first_element = false;

                                        p1("LOG LINE 860: element1 = "+element1);
                                        p1("LOG LINE 861: element2 = "+element2);
                                        p1("LOG LINE 862: first_element = "+first_element);
                                        ending_found = true;
                                    }
                                }
                            }
                            else{
                                if(segment == split_iterator.SEG_LINETO){
                                    element2 = element2+" L "+split_path_coords[0]+" "+split_path_coords[1];
                                    p1("LOG LINE 869: element2 = "+element2);
                                    if((split_path_begining.getX()==split_path_coords[0]) && (split_path_begining.getY()==split_path_coords[1]) && !begining_found){
                                        element2 = element2+" "+forward_path;
                                        first_element = true;
                                        p1("LOG LINE 873: element2 = "+element2);
                                        p1("LOG LINE 874: first_element = "+first_element);
                                        begining_found = true;
                                    }
                                    else if((split_path_ending.getX()==split_path_coords[0]) && (split_path_ending.getY()==split_path_coords[1]) && !ending_found){
                                        element2 = element2+" "+backward_path;
                                        first_element = true;
                                        p1("LOG LINE 879: element2 = "+element2);
                                        p1("LOG LINE 880: first_element = "+first_element);
                                        ending_found = true;
                                    }
                                }
                                if(segment == split_iterator.SEG_CUBICTO){
                                    element2 = element2+" C "+split_path_coords[0]+" "+split_path_coords[1]+" "+split_path_coords[2]+" "+split_path_coords[3]+" "+split_path_coords[4]+" "+split_path_coords[5]; p1("LOG LINE 880: element2 = "+element2);
                                    if((split_path_begining.getX()==split_path_coords[4]) && (split_path_begining.getY()==split_path_coords[5]) && !begining_found){
                                        element2 = element2+" "+forward_path;
                                        first_element = true;
                                        p1("LOG LINE 888: element2 = "+element2);
                                        p1("LOG LINE 889: first_element = "+first_element);
                                        begining_found = true;
                                    }
                                    else if((split_path_ending.getX()==split_path_coords[4]) && (split_path_ending.getY()==split_path_coords[5]) && !ending_found){
                                        element2 = element2+" "+backward_path;
                                        first_element = true;
                                        p1("LOG LINE 894: element2 = "+element2);
                                        p1("LOG LINE 895: first_element = "+first_element);
                                        ending_found = true;
                                    }
                                }
                            }
                            split_iterator.next();
                            line_807_loop++;
                        }

                        p1("******************************************** OUT OF THE LINE_807_LOOP ***********************************************");

                        compound_edit = new CompoundEdit();
                        Element root = document.getDocumentElement();
                        Element del = selected_shape;
//                        Element del = document.getElementById("svg_"+selected_shape.getAttribute("id"));
//                        root.removeChild(del);
                        destination_container = (Element)selected_shape.getParentNode();
                        urc = new UndoableRemoveChild(this, document.getElementById("svg_"+selected_shape.getAttribute("id")), selected_shape);
                        compound_edit.addEdit(urc);

//                        root.removeChild(document.getElementById("drawing_"+drawing_number));
                        ure = new UndoableRemoveElement(this, document.getElementById("drawing_"+project_object.patterns.size()));
                        compound_edit.addEdit(ure);

                        urpbe = new UndoableRemovePatternByElement(project_object, del, th, rt);
                        compound_edit.addEdit(urpbe);

                        project_object.addHistoryElement(del);
//                        project_object.removePatternByElement(del);
//                        th.refreshTree(project_object, rt);

//                        drawing_number = drawing_number-1;
                        udnd = new UndoableDrawingNumberDeduct(this);
                        compound_edit.addEdit(udnd);

                        String first_element_name = selected_shape.getAttribute("id")+"_01";

                        Element first_drawing = document.createElementNS(svgNS, "path");
                        first_drawing.setAttributeNS (null, "id", first_element_name);
                        first_drawing.setAttributeNS(null, "pathLength", "100");
                        first_drawing.setAttributeNS (null, "stroke", "black");
                        first_drawing.setAttributeNS (null, "stroke-width", "1");
                        first_drawing.setAttributeNS(null, "fill", "none");
                        //selected_shape.getParentNode().appendChild(first_drawing);
                        first_drawing.setAttributeNS(null, "d", element1);
//                        finishDrawing(first_drawing);
/*                        uac = new UndoableAppendChild(this, destination_container, first_drawing);
                        compound_edit.addEdit(uac);*/
                        finishDrawing(destination_container, first_drawing);
                        
//                        drawing_number++;
                        udni = new UndoableDrawingNumberIncrease(this);
                        compound_edit.addEdit(udni);

                        Element second_drawing = document.createElementNS(svgNS, "path");
                        second_drawing.setAttributeNS (null, "id", selected_shape.getAttribute("id")+"_02");
                        second_drawing.setAttributeNS(null, "pathLength", "100");
                        second_drawing.setAttributeNS (null, "stroke", "black");
                        second_drawing.setAttributeNS (null, "stroke-width", "1");
                        second_drawing.setAttributeNS(null, "fill", "none");
                        second_drawing.setAttributeNS(null, "d", element2);
//                        finishDrawing(second_drawing);
/*                        uac = new UndoableAppendChild(this, destination_container, second_drawing);
                        compound_edit.addEdit(uac);*/
                        finishDrawing(destination_container, second_drawing);

/*                        for(int i = 0; i < location_list.size(); i++){
                            root.removeChild(location_list.get(i));
                        }*/

                        for(int i = 0; i < location_list.size(); i++){
                            urc = new UndoableRemoveChild(this, (Element)location_list.get(i).getParentNode(), location_list.get(i));
                            compound_edit.addEdit(urc);
                        }

/*                        for(int i = 0; i < location_coordinates_list.size(); i++){
                            root.removeChild(location_coordinates_list.get(i));
                        }*/
                        for(int i = 0; i < location_coordinates_list.size(); i++){
                            urc = new UndoableRemoveChild(this, (Element)location_coordinates_list.get(i).getParentNode(), location_coordinates_list.get(i));
                            compound_edit.addEdit(urc);
                        }

//                        location_list.clear();
                        ucll = new UndoableClearLocationList(location_list);
                        compound_edit.addEdit(ucll);


//                        location_coordinates_list.clear();
                        ucll = new UndoableClearLocationList(location_coordinates_list);
                        compound_edit.addEdit(ucll);

//                        drawing_number++;
                        udni = new UndoableDrawingNumberIncrease(this);
                        compound_edit.addEdit(udni);

                        registerPatternListeners(first_drawing);
                        registerPatternListeners(second_drawing);

//                        drawing_in_progress = false;
                        usdip = new UndoableSwitchDrawingInProgress(this, false);
                        compound_edit.addEdit(usdip);

//                        current_drawing_locations.clear();
//                        whole_path_locations_list.clear();
//                        last_location = null;

                        ucpl = new UndoableClearPointList(current_drawing_locations);
                        compound_edit.addEdit(ucpl);

                        ucpl = new UndoableClearPointList(whole_path_locations_list);
                        compound_edit.addEdit(ucpl);

                        uclc = new UndoableClearLocation(last_location);
                        compound_edit.addEdit(uclc);

                        compound_edit.end();
                        manager.addEdit(compound_edit);
                        dbf.manager = manager;
                        dbf.updateButtons();
                        for(int i = 0; i < project_object.patterns.size(); i++){
                            System.out.println("New Patterns: "+project_object.patterns.get(i).front.getAttribute("id"));
                        }
                    }
        }

        private void removeElement(final Element e){
            Runnable r = new Runnable() {
			public void run() {
                            try{
                                e.getParentNode().removeChild(e);
                            }
                            catch(Exception e){
                                return;
                            }
			}
		};
		UpdateManager um = canvas.getUpdateManager();
                um.addUpdateManagerListener(this);
		try{
                    System.out.println("Line 1875");
                um.getUpdateRunnableQueue().invokeLater(r);
                System.out.println("Line 1877");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 1848: "+EX);
            }
        }

        public void splitAdjuster(){
            java.awt.Shape original_shape = null;
            try{
                original_shape = org.apache.batik.parser.AWTPathProducer.createShape(new StringReader(selected_shape.getAttributeNS(null,"d")), GeneralPath.WIND_EVEN_ODD);
            }
            catch(Exception e){
                p1("EXCEPTION AT LINE 961: "+e);
            }

            PathIterator pi = original_shape.getPathIterator(null);
            double coords[] = new double[6];

            while(!pi.isDone()){
                int segment = pi.currentSegment(coords);

                if(segment == pi.SEG_MOVETO){
                    if(new Point2D.Double(coords[0], coords[1]).distance(whole_path_locations_list.get(0))<2){
                        
                    }
                }
                pi.next();
            }
        }

        public boolean lineSplitter(Point2D p1, Point2D p2, Point2D break_point){
            if(Math.abs((p1.distance(break_point)+break_point.distance(p2))-p1.distance(p2))<2){
                return true;
            }
            else return false;
        }

        public void fillPattern(){
            final String DATA_PROTOCOL_PNG_PREFIX = "data:image/png;base64,";
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            Base64EncoderStream b64Encoder = new Base64EncoderStream(os);

            ImageEncoder encoder = new PNGImageEncoder(b64Encoder, null);

            try{
                encoder.encode(fill_image);
            }
            catch(Exception ec){
                System.out.println("Line 1529: Create Buffered Image error: "+ec);
            }
            try{
                b64Encoder.close();
            }
            catch(Exception e){
                System.out.println("Line 1603: b64Encoder closing exception"+e);
            }
            Element root = document.getDocumentElement();
            if(fill_defs == null){
                fill_defs = document.createElementNS(svgNS, "defs");
                fill_defs.setAttribute("id", "pattern_defs");
                root.appendChild(fill_defs);
            }

            if(!selected_shape.getAttribute("fill").equals("none")){
                selected_shape.setAttribute("fill", "none");
                fill_defs.removeChild(document.getElementById("pattern_"+selected_shape.getAttribute("id")));
            }

            Element fill_image = document.createElementNS(svgNS, "image");
            fill_image.setAttributeNS(XLINK_NAMESPACE_URI, "xlink:href", DATA_PROTOCOL_PNG_PREFIX+os.toString());
            fill_image.setAttributeNS(null, "id", "fill_image_"+selected_shape.getAttribute("id"));
            fill_image.setAttributeNS(null, "x", "0");
            fill_image.setAttributeNS (null, "y", "0");
            fill_image.setAttributeNS(null, "width", "1.0in");
       	  fill_image.setAttributeNS(null, "height", "1.0in");

             Element pattern = document.createElementNS(svgNS, "pattern");
             pattern.setAttributeNS(null, "id", "pattern_"+selected_shape.getAttribute("id"));
             pattern.setAttributeNS(null, "patternUnits", "userSpaceOnUse");
             pattern.setAttributeNS(null, "width", "1.0in");
             pattern.setAttributeNS(null, "height", "1.0in");
             pattern.setAttributeNS(null, "x", "1.0in");
             pattern.setAttributeNS(null, "y", "1.0in");

             pattern.appendChild(fill_image);
             fill_defs.appendChild(pattern);
             selected_shape.setAttribute("fill", "url(#pattern_"+selected_shape.getAttribute("id")+")");
             refresh();
        }

        public void fillPatternByURI(String image_name){
            Element fill_image = document.createElementNS(svgNS, "image");
            fill_image.setAttributeNS(XLINK_NAMESPACE_URI, "xlink:href", fillUri);
            fill_image.setAttributeNS(null, "id", image_name);
            fill_image.setAttributeNS(null, "x", "0");
            fill_image.setAttributeNS (null, "y", "0");
            fill_image.setAttributeNS(null, "width", "1.0in");
            fill_image.setAttributeNS(null, "height", "1.0in");
            final Element pattern = document.createElementNS(svgNS, "pattern");
            pattern.setAttributeNS(null, "id", "pattern_"+selected_shape.getAttribute("id"));
            pattern.setAttributeNS(null, "patternUnits", "userSpaceOnUse");
            pattern.setAttributeNS(null, "width", "1.0in");
            pattern.setAttributeNS(null, "height", "1.0in");
            pattern.setAttributeNS(null, "x", "1.0in");
            pattern.setAttributeNS(null, "y", "1.0in");
            pattern.appendChild(fill_image);
            Runnable runnable = new Runnable() {
                public void run() {
                    selected_shape.getParentNode().getParentNode().appendChild(pattern);
                    selected_shape.setAttribute("fill", "url(#pattern_" + selected_shape.getAttribute("id") + ")");
                }
            };
            UpdateManager um = canvas.getUpdateManager();
            um.addUpdateManagerListener(this);
            try{
                System.out.println("Line 1993");
                um.getUpdateRunnableQueue().invokeLater(runnable);
                System.out.println("Line 1995");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 1964: "+EX);
            }
        }

        public void elementIterator(Element e){
            if(e.getLocalName().equals("path")){
                registerPatternListeners(e);
            }
            if(e.getLocalName().equals("image")){
                registerButtonListeners(e);
            }
            if(e.hasChildNodes()){
            org.w3c.dom.NodeList node_list = e.getChildNodes();
            for(int i = 0; i < node_list.getLength();i++){                
                if(!node_list.item(i).getLocalName().equals(null)){
                    System.out.println("In the element iterator the node is: "+node_list.item(i).getLocalName());
                    try{
                        elementIterator((Element)node_list.item(i));
                    }
                    catch(Exception ex){
                        System.out.println("Exception in the element iterator: "+ex);
                    }                    
                }
            }
            return;
        }
    }

        public BufferedImage createBufferedImage(){
            ImageIcon icon = new ImageIcon("test_image.PNG");
            Image image = icon.getImage();

            BufferedImage buffImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics g = buffImage.getGraphics();
            g.drawImage(image, 0, 0, null);
            return buffImage;
        }

        public void setButton(final BufferedImage sent_image){
            System.out.println("In the setButton");
            final String DATA_PROTOCOL_PNG_PREFIX = "data:image/png;base64,";
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            Base64EncoderStream b64Encoder = new Base64EncoderStream(os);
            ImageEncoder encoder = new PNGImageEncoder(b64Encoder, null);

            try{
                encoder.encode(sent_image);
            }
            catch(Exception ec){
                System.out.println("Line 1596: Create Buffered Image error: "+ec);
            }
            try{
                b64Encoder.close();
            }
            catch(Exception e){
                System.out.println("Line 1603: b64Encoder closing exception"+e);
            }
            
            SVGLocatableSupport ls = new SVGLocatableSupport();
            SVGRect rect = SVGLocatableSupport.getBBox(selected_shape);
            float x = rect.getX();
            float y = rect.getY();
            float width = rect.getWidth();
            float height = rect.getHeight();
            int button_count = selected_shape.getParentNode().getChildNodes().getLength();
            Element my_image = document.createElementNS(svgNS, "image");
            my_image.setAttributeNS(XLINK_NAMESPACE_URI, "xlink:href", DATA_PROTOCOL_PNG_PREFIX+os.toString());
            my_image.setAttributeNS(null, "id", "button_"+button_count);
            my_image.setAttributeNS(null, "x", ""+x);
            my_image.setAttributeNS (null, "y", ""+y);
            my_image.setAttributeNS(null, "width", ""+width/2);
            my_image.setAttributeNS(null, "height", ""+height/2);
            selected_shape.getParentNode().appendChild(my_image);
            registerButtonListeners(my_image);
            refresh();
            th.refreshTree(project_object, rt);
        }

        public void showPoints(){

            java.awt.Shape s = null;
            try{
                s = org.apache.batik.parser.AWTPathProducer.createShape(new StringReader(selected_shape.getAttributeNS(null,"d")), GeneralPath.WIND_EVEN_ODD);
            }
            catch(Exception e){
                p1("Exception caught at test Splitter");
            }
            double[] coords = new double[6];
            PathIterator pi = s.getPathIterator(null);

            while(!pi.isDone()){
                int segment = pi.currentSegment(coords);

                if(segment == pi.SEG_MOVETO){
                    drawEditPoint(coords[0], coords[1], 3, "white");
                }

                else if(segment == pi.SEG_LINETO){
                    drawEditPoint(coords[0], coords[1], 3, "white");
                }

                else if(segment == pi.SEG_CUBICTO){
                    drawEditPoint(coords[0], coords[1], 3, "chartreuse ");
                    drawEditPoint(coords[2], coords[3], 3, "chartreuse ");
                    drawEditPoint(coords[4], coords[5], 3, "white");
                }
                pi.next();
            }
        }

        public void delete_drawing(){
            DefaultMutableTreeNode dmtn = null;
            JTree t = th.getTree();
            
            dmtn = th.seekNodeByObject(selected_shape, (DefaultMutableTreeNode)t.getModel().getRoot());
            ((DefaultTreeModel)t.getModel()).removeNodeFromParent(dmtn);
            th.getTree().repaint();
            Runnable runnable = new Runnable() {
                public void run() {
                    selected_shape.getParentNode().getParentNode().getParentNode().removeChild(selected_shape.getParentNode().getParentNode());
                }
            };
            UpdateManager um = canvas.getUpdateManager();
            um.addUpdateManagerListener(this);
            try{
                System.out.println("Line 2123");
                um.getUpdateRunnableQueue().invokeLater(runnable);
                System.out.println("Line 2125");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 2092: "+EX);
            }
        }

        public void removeResizeHandler(){
            System.out.println("Remove resize handler called");
                Runnable runnable = new Runnable() {
                    public void run(){
                        NodeList list = resize_frame.getChildNodes();
                        for(int x = 0; x < list.getLength(); x++){
                            resize_frame.removeChild(list.item(0));
                        }
                        System.out.println("Resize handler removed");
                    }
                };
                update_manager.getUpdateRunnableQueue().invokeLater(runnable);
                resize_handler = null;
        }

        public void setSelectedDrawing(final Element e){
            System.out.println("Selected drawing is: "+e.getAttribute("id")+" - "+e.getLocalName());
            System.out.println("------------- setSelectedDrawing started ----------------------");

            Runnable runnable = new Runnable(){
                public void run(){
                    try{
                        document.getElementById("resizer_holder").getParentNode().removeChild(document.getElementById("resizer_holder"));
                    }
                    catch(Exception e){
                        System.out.println("Resize holder is not there.");
                    }
                    textPath = false;
                    if(!e.getLocalName().equals("path")) return;
                    if(selected_shape !=null){
                        System.out.println("selected_shape !=null");
                        System.out.println("setSelectedDrawing Tread A started");
                        selected_shape.setAttribute("stroke", "black");
                        System.out.println("Previously selected shape turned black");
                    }
                    NodeList all_nodes = document.getElementsByTagName("path");
                    System.out.println("path Nodelist has "+all_nodes.getLength()+" nodes.");
                    for(int i = 0; i < all_nodes.getLength(); i++){
                        if(!((Element)all_nodes.item(i)).getAttribute("id").equals(e.getAttribute("id"))){
                            final Element old_element = ((Element)all_nodes.item(i));
                            removePatternListeners(((Element)all_nodes.item(i)));
                            System.out.println("setSelectedDrawing Tread B started");
                            old_element.setAttribute("stroke", "grey");
                            System.out.println("Old_element turned grey");
                        }
                    }
                    selected_shape = e;
                    System.out.println("setSelectedDrawing Thread C started");
                    e.setAttribute("stroke", "red");
                    System.out.println("Sent element became the selected_shape");
                    try{
                        selected_shape_boundry = org.apache.batik.parser.AWTPathProducer.createShape(new StringReader(selected_shape.getAttributeNS(null,"d")), GeneralPath.WIND_EVEN_ODD);
                    }
                    catch(Exception ex){
                        p1("Exception caught at setSelectedDrawing");
                    }
                    System.out.println("setSelectedDrawing Thread D started");
                    selected_shape.getParentNode().getParentNode().getParentNode().appendChild(selected_shape.getParentNode().getParentNode());
                    System.out.println("selected_shape added to the upper layer");
                    matrix = SVGLocatableSupport.getScreenCTM(selected_shape);
                    AffineTransform at = new AffineTransform(matrix.getA(), matrix.getB(), matrix.getC(), matrix.getD(), matrix.getE(), matrix.getF());
                    System.out.println("");
                    try{
                        mouse_transformer = at.createInverse();
                    }
                    catch(Exception ex){
                        System.out.println("matrix to mouse transformer exception: "+ex);
                    }
                }
            };
            try{
                System.out.println("Before the setSelectedDrawing first thread");
                update_manager.getUpdateRunnableQueue().invokeAndWait(runnable);
                System.out.println("After the setSelectedDrawing first thread");                
            }
            catch(Exception ex){
                System.out.println("setSelectedDrawing first thread Exception: "+ex);
            }
            Runnable runnable2 = new Runnable(){   
                public void run(){
                    try{
                        NodeList list = resize_frame.getChildNodes();
                        for(int x = 0; x < list.getLength(); x++){
                            resize_frame.removeChild(list.item(0));
                        }
                        System.out.println("Resize handler removed");
                        resize_handler = null;
                    }
                    catch(Exception e2){
                        System.out.println("Could not remove the resize_handler!!");
                    }
                }
            };
            try{
                System.out.println("Before the setSelectedDrawing second thread");
                update_manager.getUpdateRunnableQueue().invokeAndWait(runnable2);
                System.out.println("Before the setSelectedDrawing second thread");
                System.out.println("");
            }
            catch(Exception exc){
                System.out.println("Second Exception: "+exc);
            }
            resizer();
        }

        public void appendElement(final Element parent, final Element child){
            Runnable runnable = new Runnable(){
                public void run(){
                    parent.appendChild(child);
                }
            };
            update_manager.getUpdateRunnableQueue().invokeLater(runnable);
        }

        public void splitHandler(){
            final Element split_boundry = document.createElementNS(svgNS, "path");
            split_boundry.setAttribute("d", selected_shape.getAttribute("d"));
            split_boundry.setAttribute("id", "split_boundry");
            split_boundry.setAttribute("stroke", "red");
            split_boundry.setAttribute("stroke-width", "1");
            split_boundry.setAttribute("fill", "none");

//            document.getDocumentElement().appendChild(split_boundry);
            Runnable runnable = new Runnable() {
                public void run() {
                    System.out.println("Thread call: Appending the split boundry");
                    selected_shape.getParentNode().appendChild(split_boundry);
                }
            };
            try{
                update_manager.getUpdateRunnableQueue().invokeLater(runnable);
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait2242: "+EX);
            }
            splitHandlerListeners(split_boundry);
            split_operation = true;
        }

        public boolean splitChecker(){
            if(split_operation){
                if(on_bounds && split_points<2){
                    split_points++;
                    return true;
                }
                else if(0 < split_points && 2 > split_points){
                    return true;
                }
                else return false;
            }
            else return true;
        }

        public void splitHandlerListeners(Element element){
            EventTarget t1 = (EventTarget)element;
            t1.addEventListener ("mouseover", new MouseOverSplitHandler(), false);
            t1.addEventListener ("mouseout", new MouseOutSplitHandler(), false);
        }

        public class MouseOverSplitHandler implements EventListener {
        public void handleEvent (Event evt) {
//            System.out.println("In the drag point listeners");
         EventTarget tt = evt.getTarget();
         final Element e1 = (Element)tt;
            Runnable runnable = new Runnable() {

                public void run() {
                    e1.setAttribute("stroke", "lime");
                }
            };
            try{
                update_manager.getUpdateRunnableQueue().invokeLater(runnable);
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 2285: "+EX);
            }
         Toolkit toolkit = Toolkit.getDefaultToolkit();
         Image cut_cursor_image = toolkit.createImage("cut_icon.gif");
         Point cut_cursor_hotspot = new Point(0,15);
         Cursor cut_cursor = toolkit.createCustomCursor(cut_cursor_image, cut_cursor_hotspot, "Pattern split");
         canvas.setCursor(cut_cursor);
         on_bounds = true;
        }
      }

        public class MouseOutSplitHandler implements EventListener {
        public void handleEvent (Event evt) {
//            System.out.println("In the drag point listeners");
         EventTarget tt = evt.getTarget();
         final Element e1 = (Element)tt;
            Runnable runnable = new Runnable() {
                public void run() {
                    e1.setAttribute("stroke", "black");
                }
            };
            try{
                update_manager.getUpdateRunnableQueue().invokeLater(runnable);
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 2312: "+EX);
            }
         canvas.setCursor(Cursor.getDefaultCursor());
         on_bounds = false;
        }
      }


/////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
//////////////////// REGISTERING THE LISTENERS \\\\\\\\\\\\\\\\\\\\\\\\\\\\
////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

        public void registerEditPointListeners(Element element){
        EventTarget t1 = (EventTarget)element;

/*        t1.addEventListener ("mouseover", new OnMouseOverEditPointAction(), false);
        t1.addEventListener ("mouseout", new OnMouseOutEditPoint(), false);
        t1.addEventListener ("cslick", new OnEditPointClickAction(), false);
        t1.addEventListener ("mousedown", new OnEditPointMouseDownAction(), false);
        t1.addEventListener ("mouseup", new OnEditPointMouseUpAction(), false);*/

        t1.addEventListener ("mouseover", this, false);
        t1.addEventListener ("mouseout", this, false);
        t1.addEventListener ("cslick", this, false);
        t1.addEventListener ("mousedown", this, false);
        t1.addEventListener ("mouseup", this, false);
    }
    
        

        public void registerPatternListeners(Element element){
        EventTarget t1 = (EventTarget)element; //document.getElementById (element.getAttribute("id"));
//        System.out.println("Registering element is "+element.getAttribute("id"));

        t1.addEventListener ("mouseover", new PATTERN_MouseOverAction(), false);
        t1.addEventListener ("mouseout", new PATTERN_MouseOutAction(), false);
        t1.addEventListener ("click", new PATTERN_OnClickAction(), false);
        t1.addEventListener ("mousedown", new PATTERN_MouseDownAction(), false);
        t1.addEventListener ("mouseup", new PATTERN_MouseUpAction(), false);
//        t1.addEventListener ("mousemove", new PATTERN_MouseMoveAction(), false);
    }
        public void removePatternListeners(Element e){
            EventTarget et = (EventTarget)e;
            et.removeEventListener("mouseover", this, false);
            System.out.println("Suppose to remove the pattern listener.");
        }

        public void handleEvent(Event evt){
            Element element = (Element)evt.getTarget();
//            System.out.println("This element is: "+element.getTagName());
//            System.out.println("This element ID is: "+element.getAttribute("id"));
            if(element.getAttribute("id").equals("location")){
                if(evt.getType().equals("mouseover")){
                    MouseOverEditPoint(element);
                }
                else if(evt.getType().equals("mouseout")){
                    mouseOutEditPoint(element);
                }
                else if(evt.getType().equals("mousedown")){
                    mouseDownEditPoint(element);
                }
                else if(evt.getType().equals("mouseup")){
                    mouseUpEditPoint(element);
                }
            }
        }

        public void MouseOverEditPoint(final Element element){
            Runnable runnable = new Runnable() {
                public void run() {
                    element.setAttribute("fill", "red");
                }
            };
            update_manager.getUpdateRunnableQueue().invokeLater(runnable);
            tmp_shape_type_number = shape_type_number;
            shape_type_number = 0;
        }

        public void mouseOutEditPoint(final Element element){
            Runnable runnable = new Runnable(){
                public void run(){
                    element.setAttribute("fill", "lightsteelblue");
                }
            };
            update_manager.getUpdateRunnableQueue().invokeLater(runnable);
            shape_type_number = tmp_shape_type_number;
        }

        public void mouseDownEditPoint(final Element element){
            selected_drag_point = element;
            double x = Integer.parseInt(StringUtils.substringBefore(element.getAttribute("cx"),"."));
            double y = Integer.parseInt(StringUtils.substringBefore(element.getAttribute("cy"),"."));
            before_drag_edit_point = new Point2D.Double(x, y);
            x = Double.parseDouble(element.getAttribute("cx"));
            y = Double.parseDouble(element.getAttribute("cy"));
            prev_point_coords = new Point2D.Double(x, y);
            Element current_drawing = document.getElementById("drawing_"+project_object.patterns.size());
            before_drag_drawing = current_drawing.getAttribute("d");
            prev_pattern_coords = current_drawing.getAttribute("d");
            left_pattern_coords = StringUtils.substringBefore(current_drawing.getAttribute("d"), x+" "+y);
            right_pattern_coords = StringUtils.substringAfter(current_drawing.getAttribute("d"), x+" "+y+" ");
            edit_point_moveable = true;
            canvas.setCursor(dflt_cursor);
        }

        public void mouseUpEditPoint(Element element){
            Element current_drawing = document.getElementById("drawing_"+project_object.patterns.size());
            double x = Integer.parseInt(StringUtils.substringBefore(element.getAttribute("cx"),"."));
            double y = Integer.parseInt(StringUtils.substringBefore(element.getAttribute("cy"),"."));
            after_drag_edit_point = new Point2D.Double(x, y);
            after_drag_drawing = current_drawing.getAttribute("d");
            edit_point_moveable = false;
            UndoablePatternDragCaller(element);
        }

        public void UndoablePatternDragCaller(Element e1){
            Element current_drawing = document.getElementById("drawing_"+project_object.patterns.size());
            upd = new UndoablePatternDrag(this, before_drag_edit_point, after_drag_edit_point, before_drag_drawing, after_drag_drawing, current_drawing, selected_drag_point);
            compound_edit = new CompoundEdit();
            compound_edit.addEdit(upd);
            compound_edit.end();
            manager.addEdit(compound_edit);
            dbf.manager = manager;
            dbf.updateButtons();
        }

        public class PATTERN_MouseOverAction implements EventListener {
        public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         final Element e1 = (Element)tt;
            Runnable runnable = new Runnable() {
                public void run() {
                    e1.setAttribute("stroke", "red");
                }
            };
            UpdateManager um = canvas.getUpdateManager();
//            um.addUpdateManagerListener(this);
            try{
                System.out.println("Line 2511");
                um.getUpdateRunnableQueue().invokeLater(runnable);
                System.out.println("Line 2513");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 2470: "+EX);
            }
         try{
             canvas.setCursor(hand_cursor);
         }
         catch(Exception excep){
             System.out.println("Line 2321 Exception caught: "+excep);
         }
        }
      }

        public class PATTERN_MouseOutAction implements EventListener {
     public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         final Element e1 = (Element)tt;
         Runnable runnable = new Runnable() {
             public void run() {
                 e1.setAttribute("stroke", "black");
                 e1.setAttributeNS(null, "stroke-width", "1");
             }
         };
         UpdateManager um = canvas.getUpdateManager();
//         um.addUpdateManagerListener(this);
         try{
             System.out.println("Line 2540");
                um.getUpdateRunnableQueue().invokeLater(runnable);
                System.out.println("Line 2542");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 2497: "+EX);
            }
         canvas.setCursor(dflt_cursor);
        }
      }

        public class PATTERN_OnClickAction implements EventListener {
     public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();

         Element e1 = (Element)tt;
         selected_shape = e1;
         final Element child = (Element)e1.getParentNode();
         final Element parent = (Element)e1.getParentNode().getParentNode();
         Runnable runnable = new Runnable() {
             public void run() {
                 parent.removeChild(child);
                 parent.appendChild(child);
             }
         };
         UpdateManager um = canvas.getUpdateManager();
//         um.addUpdateManagerListener(this);
         try{
             System.out.println("Line 2568");
                um.getUpdateRunnableQueue().invokeLater(runnable);
                System.out.println("Line 2570");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 2523: "+EX);
            }
         setSelectedDrawing(e1);
        }
      }

        public class PATTERN_MouseDownAction implements EventListener {

     public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         selected_shape = e1;
         moveable = true;
         try{
         selected_shape_station_point = new Point2D.Double(Double.parseDouble(((Element)(selected_shape.getParentNode().getParentNode())).getAttribute("x")),Double.parseDouble(((Element)(selected_shape.getParentNode().getParentNode())).getAttribute("y")));
         canvas.setCursor(move_cursor);
         }
         catch(Exception e){
             p1("Empty String");
         }
        }
      }

        public class PATTERN_MouseUpAction implements EventListener {
     public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         moveable = false;
         canvas.setCursor(dflt_cursor);
        }
      }

        public class PATTERN_MouseMoveAction implements EventListener {
        public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
//         if(moveable) resizer();
         canvas.setCursor(dflt_cursor);
        }
      }

        public void RegisterComponentNWPointListeners(Element element){
            p1("In the NWRegistrar the element ID is "+element.getAttribute("id"));

        EventTarget t1 = (EventTarget) document.getElementById (element.getAttribute("id"));

        t1.addEventListener ("mouseover", new COMPONENT_NW_POINT_MouseOverAction(), false);
        t1.addEventListener ("mouseout", new COMPONENT_NW_POINT_MouseOutAction(), false);
//        t1.addEventListener ("click", new OnClickAction(), false);
        t1.addEventListener ("mousedown", new COMPONENT_NW_POINT_MouseDownAction(), false);
        t1.addEventListener ("mouseup", new COMPONENT_NW_POINT_MouseUpAction(), false);
//        t1.addEventListener ("mousemove", new COMPONENT_NW_POINT_MouseMoveAction (), false);
    }
        
       public class COMPONENT_NW_POINT_MouseOverAction implements EventListener {

        public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         final Element e1 = (Element)tt;
            Runnable runnable = new Runnable() {
                public void run() {
                    e1.setAttribute("fill", "hotpink");
                }
            };
            UpdateManager um = canvas.getUpdateManager();
//            um.addUpdateManagerListener(this);
            try{
                System.out.println("Line 2640");
                um.getUpdateRunnableQueue().invokeLater(runnable);
                System.out.println("Line 2642");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 2593: "+EX);
            }
         canvas.setCursor(move_cursor);
        }
      }

        public class COMPONENT_NW_POINT_MouseOutAction implements EventListener {
        public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         final Element e1 = (Element)tt;
            Runnable runnable = new Runnable() {
                public void run() {
                    e1.setAttribute("fill", "hotpink");
                }
            };
            UpdateManager um = canvas.getUpdateManager();
//            um.addUpdateManagerListener(this);
            try{
                System.out.println("Line 2663");
                um.getUpdateRunnableQueue().invokeLater(runnable);
                System.out.println("Line 2665");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 2614: "+EX);
            }
         canvas.setCursor(dflt_cursor);
        }
      }
        
        public class COMPONENT_NW_POINT_MouseDownAction implements EventListener {
     public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         final Element e1 = (Element)tt;
         selected_point = e1;
         Runnable runnable = new Runnable() {
             public void run() {
                 e1.setAttribute("fill", "red");
             }
         };
         UpdateManager um = canvas.getUpdateManager();
//         um.addUpdateManagerListener(this);
         try{
             System.out.println("Line 2687");
                um.getUpdateRunnableQueue().invokeLater(runnable);
                System.out.println("Line 2689");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 2636: "+EX);
            }
         selected_component_station_point = new Point2D.Double(Double.parseDouble(selected_component.getAttribute("x")),Double.parseDouble(selected_component.getAttribute("y")));
         selected_point_moveable = true;
         canvas.setCursor(move_cursor);
        }
      }

        public class COMPONENT_NW_POINT_MouseUpAction implements EventListener {
     public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         final Element e1 = (Element)tt;
         selected_point = e1;
         Runnable runnable = new Runnable() {
             public void run() {
                 e1.setAttribute("fill", "red");
             }
         };
         UpdateManager um = canvas.getUpdateManager();
//         um.addUpdateManagerListener(this);
         try{
             System.out.println("Line 2713");
                um.getUpdateRunnableQueue().invokeLater(runnable);
                System.out.println("Line 2715");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 2660: "+EX);
            }
         selected_point_moveable = false;
         canvas.setCursor(dflt_cursor);
        }
      }

        public void registerTextListeners(Element element){

        EventTarget t1 = (EventTarget) document.getElementById (element.getAttribute("id"));

        t1.addEventListener ("mouseover", new TEXT_MouseOverAction(), false);
        t1.addEventListener ("mouseout", new TEXT_MouseOutAction(), false);
//        t1.addEventListener ("click", new TEXT_ClickAction(), false);
        t1.addEventListener ("mousedown", new TEXT_MouseDownAction(), false);
//        t1.addEventListener ("mouseup", new TEXT_MouseUpAction(), false);
    }
        public class TEXT_MouseOverAction implements EventListener {
        public void handleEvent (Event evt) {
            System.out.println(((Element)((Element)document.getElementById(selected_text.getAttribute("id")+"_textPath")).getParentNode()).getAttribute("x"));
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         System.out.println("The element is: "+e1.getAttribute("id")+"; "+e1.getLocalName());
         canvas.setCursor(hand_cursor);
        }
      }

        public class TEXT_MouseOutAction implements EventListener {
        public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         canvas.setCursor(dflt_cursor);
        }
      }

        public class TEXT_MouseDownAction implements EventListener {
     public void handleEvent (Event evt) {
         System.out.println("On text mouse down.");
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         selected_text = (Element)e1.getParentNode();
//         System.out.println("selected_text: "+selected_text.getParentNode().getLocalName());
         text_moveable = true;
//         System.out.println("That parents type is: "+document.getElementById(selected_text.getAttribute("id")+"_textPath").getLocalName());
//         selected_text_station_point = new Point2D.Double(Double.parseDouble(document.getElementById(selected_text.getAttribute("id")+"_textPath").getParentNode().getAttribute("x"),Double.parseDouble(((Element)document.getElementById(((Element)selected_text.getAttribute("id")+"_textPath").getParentNode()).getAttribute("y")))));
         selected_text_station_point = new Point2D.Double(Double.parseDouble(((Element)document.getElementById(selected_text.getAttribute("id")+"_textPath").getParentNode()).getAttribute("x")), Double.parseDouble(((Element)document.getElementById(selected_text.getAttribute("id")+"_textPath").getParentNode()).getAttribute("y")));
         canvas.setCursor(move_cursor);
        }
      }

        public void registerButtonListeners(Element element){

        EventTarget t1 = (EventTarget) document.getElementById (element.getAttribute("id"));

        t1.addEventListener ("mouseover", new BUTTON_MouseOverAction(), false);
        t1.addEventListener ("mouseout", new BUTTON_MouseOutAction(), false);
        t1.addEventListener ("click", new BUTTON_ClickAction(), false);
        t1.addEventListener ("mousedown", new BUTTON_MouseDownAction(), false);
        t1.addEventListener ("mouseup", new BUTTON_MouseUpAction(), false);
    }

        public class BUTTON_MouseOverAction implements EventListener {
        public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         final Element e1 = (Element)tt;
         SVGRect rect = SVGLocatableSupport.getBBox(e1);
         float x = rect.getX();
         float y = rect.getY();
         float width = rect.getWidth();
         float height = rect.getHeight();
         final Element button_box = document.createElementNS(svgNS, "rect");
         button_box.setAttribute("id", "button_box-"+e1.getAttribute("id"));
         button_box.setAttribute("x", ""+x);
         button_box.setAttribute("y", ""+y);
         button_box.setAttribute("width", ""+width);
         button_box.setAttribute("height", ""+height);
         button_box.setAttribute("rx", ""+x/15);
         button_box.setAttribute("ry", ""+y/15);
         button_box.setAttribute("stroke", "#00cc00");
         button_box.setAttribute("stroke-width", ""+4);
         button_box.setAttribute("fill", "none");
            Runnable runnable = new Runnable() {
                public void run() {
                    e1.getParentNode().appendChild(button_box);
                }
            };
            UpdateManager um = canvas.getUpdateManager();
//            um.addUpdateManagerListener(this);
            try{
                System.out.println("Line 2807");
                um.getUpdateRunnableQueue().invokeLater(runnable);
                System.out.println("Line 2809");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 2752: "+EX);
            }
         canvas.setCursor(hand_cursor);
        }
      }
        
        public class BUTTON_MouseOutAction implements EventListener {
        public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         final Element e1 = (Element)tt;
            Runnable runnable = new Runnable() {
                public void run() {
                    e1.getParentNode().removeChild(document.getElementById("button_box-" + e1.getAttribute("id")));
                }
            };
            UpdateManager um = canvas.getUpdateManager();
//            um.addUpdateManagerListener(this);
            try{
                System.out.println("Line 2830");
                um.getUpdateRunnableQueue().invokeLater(runnable);
                System.out.println("Line 2832");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 2773: "+EX);
            }
         canvas.setCursor(dflt_cursor);
        }
      }

        public class BUTTON_ClickAction implements EventListener {
        public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         System.out.append("Button Clicked");

         SVGRect rect = SVGLocatableSupport.getBBox(e1);
         float fx = rect.getX();
         float fy = rect.getY();
         float fwidth = rect.getWidth();
         float fheight = rect.getHeight();

         selected_button_box = document.createElementNS(svgNS, "rect");
         selected_button_box.setAttribute("id", "selected_button_box-"+e1.getAttribute("id"));
         selected_button_box.setAttribute("x", ""+fx);
         selected_button_box.setAttribute("y", ""+fy);
         selected_button_box.setAttribute("width", ""+fwidth);
         selected_button_box.setAttribute("height", ""+fheight);
         selected_button_box.setAttribute("rx", ""+fx/15);
         selected_button_box.setAttribute("ry", ""+fx/15);
         selected_button_box.setAttribute("stroke", "red");
         selected_button_box.setAttribute("stroke-width", ""+4);
         selected_button_box.setAttribute("fill", "none");
         e1.getParentNode().appendChild(selected_button_box);
         componentBounds(fx, fy, 1);
         componentBounds(fx+fwidth-2, fy+fheight-2, 5);
         p1("fx+fwidth-2 is "+(fx+fwidth-2));
         p1("fy+fheight-2 is "+(fy+fheight-2));
        }
      }
        
        public class BUTTON_MouseDownAction implements EventListener {
     public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         selected_component = e1;
         component_moveable = true;
         selected_component_station_point = new Point2D.Double(Double.parseDouble(selected_component.getAttribute("x")),Double.parseDouble(selected_component.getAttribute("y")));
         canvas.setCursor(move_cursor);
        }
      }

        public class BUTTON_MouseUpAction implements EventListener {
     public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         selected_component = e1;
         component_moveable = false;
         p1("Button mouse up called");
        }
      }

        public void RegisterComponentSEPointListeners(Element element){
            p1("In the NWRegistrar the element ID is "+element.getAttribute("id"));

        EventTarget t1 = (EventTarget) document.getElementById (element.getAttribute("id"));

        t1.addEventListener ("mouseover", new COMPONENT_SE_POINT_MouseOverAction(), false);
        t1.addEventListener ("mouseout", new COMPONENT_SE_POINT_MouseOutAction(), false);
//        t1.addEventListener ("click", new OnClickAction (), false);
        t1.addEventListener ("mousedown", new COMPONENT_SE_POINT_MouseDownAction(), false);
        t1.addEventListener ("mouseup", new COMPONENT_SE_POINT_MouseUpAction(), false);
//        t1.addEventListener ("mousemove", new OnMouseMoveAction (), false);
    }

        public class COMPONENT_SE_POINT_MouseOverAction implements EventListener {

        public void handleEvent (Event evt) {
         p1("OnMouseOverSEPointAction Called");
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         e1.setAttribute ("fill", "hotpink");
         p1("The elementID is "+e1.getAttribute("id"));
         Cursor new_cursor = new Cursor(Cursor.HAND_CURSOR);
         canvas.setCursor(new_cursor);
        }
      }
        
        public class COMPONENT_SE_POINT_MouseOutAction implements EventListener {
        public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         e1.setAttribute ("fill", "hotpink");
         canvas.setCursor(dflt_cursor);
        }
      }

        public class COMPONENT_SE_POINT_MouseDownAction implements EventListener {
     public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         selected_point = e1;
         component_moveable = false;
         e1.setAttribute ("fill", "red");
         selected_component_dimensions = new Point2D.Double(Double.parseDouble(selected_component.getAttribute("width")),Double.parseDouble(selected_component.getAttribute("height")));
         SE_resizeable = true;
         Cursor new_cursor = new Cursor(Cursor.SE_RESIZE_CURSOR);
         canvas.setCursor(new_cursor);
        }
      }
        
        public class COMPONENT_SE_POINT_MouseUpAction implements EventListener {
     public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         selected_point = e1;
         SE_resizeable = false;
         e1.setAttribute ("fill", "red");
         selected_point_moveable = false;
         canvas.setCursor(dflt_cursor);
        }
      }

        public void dragPointListeners(Element element){
        EventTarget t1 = (EventTarget)element;

        t1.addEventListener ("mouseover", new OnMouseOverDragPointAction (), false);
        t1.addEventListener ("mouseout", new OnMouseOutDragPoint (), false);
        t1.addEventListener ("click", new OnDragPointClickAction (), false);
        t1.addEventListener ("mousedown", new OnDragPointMouseDownAction (), false);
        t1.addEventListener ("mouseup", new OnDragPointMouseUpAction (), false);
    }

        public class OnMouseOverDragPointAction implements EventListener {
        public void handleEvent (Event evt) {
//            System.out.println("In the drag point listeners");
         EventTarget tt = evt.getTarget();
         final Element e1 = (Element)tt;
            Runnable runnable = new Runnable() {
                public void run() {
                    e1.setAttribute("fill", "hotpink");
                }
            };
            UpdateManager um = canvas.getUpdateManager();
//            um.addUpdateManagerListener(this);
            try{
                System.out.println("Line 2977");
                um.getUpdateRunnableQueue().invokeLater(runnable);
                System.out.println("Line 2979");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 2918: "+EX);
            }
         canvas.setCursor(hand_cursor);
        }
      }

        public class OnMouseOutDragPoint implements EventListener {
        public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         final Element e1 = (Element)tt;
            Runnable runnable = new Runnable() {
                public void run() {
                    e1.setAttribute("fill", "white");
                }
            };
            UpdateManager um = canvas.getUpdateManager();
//            um.addUpdateManagerListener(this);
            try{
                System.out.println("Line 3000");
                um.getUpdateRunnableQueue().invokeLater(runnable);
                System.out.println("Line 3002");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 2939: "+EX);
            }
         Cursor n_cursor = new Cursor(Cursor.DEFAULT_CURSOR);
         //setCursor(n_cursor);
         canvas.setCursor(n_cursor);
        }
      } 

        public class OnDragPointClickAction implements EventListener {
        public void handleEvent (Event evt) {
            System.out.println("In the OnDragPointClickAction");
         EventTarget tt = evt.getTarget();
         final Element e1 = (Element)tt;
            Runnable runnable = new Runnable() {
                public void run() {
                    e1.setAttribute("fill", "red");
                }
            };
            UpdateManager um = canvas.getUpdateManager();
//            um.addUpdateManagerListener(this);
            try{
                System.out.println("Line 3026");
                um.getUpdateRunnableQueue().invokeLater(runnable);
                System.out.println("Line 3028");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 2963: "+EX);
            }
         System.out.println("Path is: "+selected_shape.getAttribute("d"));
         System.out.println("break point is: "+e1.getAttribute("cx")+e1.getAttribute("cy"));
//         selected_drag_point = e1;
//         left_pattern_coords = StringUtils.substringBefore(selected_shape.getAttribute("d"), e1.getAttribute("cx")+" "+e1.getAttribute("cy"));
//         right_pattern_coords = StringUtils.substringAfter(selected_shape.getAttribute("d"), e1.getAttribute("cx")+" "+e1.getAttribute("cy"));
         System.out.println("2529 left_pattern_coords: "+left_pattern_coords);
         System.out.println("right_pattern_coords: "+right_pattern_coords);
        }
      }

        public class OnDragPointMouseDownAction implements EventListener {
     public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         final Element e1 = (Element)tt;
         selected_drag_point = e1;
         Runnable runnable = new Runnable() {
             public void run() {
                 e1.setAttribute("fill", "red");
             }
         };
         UpdateManager um = canvas.getUpdateManager();
//         um.addUpdateManagerListener(this);
         try{
             System.out.println("Line 3056");
                um.getUpdateRunnableQueue().invokeLater(runnable);
                System.out.println("Line 3058");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 2991: "+EX);
            }
         double x = Integer.parseInt(StringUtils.substringBefore(e1.getAttribute("cx"),"."));
         double y = Integer.parseInt(StringUtils.substringBefore(e1.getAttribute("cy"),"."));

         x = Double.parseDouble(e1.getAttribute("cx"));
         y = Double.parseDouble(e1.getAttribute("cy"));
         p2("Shape coords are: "+selected_shape.getAttribute("d"));
//         System.out.println("Selected drag point: "+x+" "+y);
         p2("Selected drag point untruncated: "+e1.getAttribute("cx")+" "+e1.getAttribute("cy"));
String tmp = x+" "+y;
         left_pattern_coords = StringUtils.substringBefore(selected_shape.getAttribute("d"), tmp);
         System.out.println("2550 left_pattern_coords: "+left_pattern_coords);
         right_pattern_coords = StringUtils.substringAfter(selected_shape.getAttribute("d"), tmp);
         drag_point_moveable = true;
         canvas.setCursor(dflt_cursor);
        }
      }

        public class OnDragPointMouseUpAction implements EventListener {
     public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         selected_drag_point = null;
         drag_point_moveable = false;
         canvas.setCursor(dflt_cursor);
        }
      }

    public void relocate(final int x, final int y){
        Runnable runnable = new Runnable() {
            public void run() {
                ((Element) (selected_shape.getParentNode().getParentNode())).setAttribute("x", "" + (selected_shape_station_point.getX() + shape_X));
                ((Element) (selected_shape.getParentNode().getParentNode())).setAttribute("y", "" + (selected_shape_station_point.getY() + shape_Y));
            }
        };
        UpdateManager um = canvas.getUpdateManager();
        um.addUpdateManagerListener(this);
        try{
            System.out.println("Line 3099");
                um.getUpdateRunnableQueue().invokeLater(runnable);
                System.out.println("Line 3101");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 3032: "+EX);
            }
    }

    public void text_relocate(final double x, final double y){
        System.out.println("Text Relocate called.");
        Element tmp_textPath = document.getElementById(selected_text.getAttribute("id")+"_textPath");
        java.awt.Shape tmp_shape = null;
        try{
            tmp_shape = org.apache.batik.parser.AWTPathProducer.createShape(new StringReader(tmp_textPath.getAttributeNS(null,"d")), GeneralPath.WIND_EVEN_ODD);
        }
        catch(Exception e){
            System.out.println("symmetricFinishDrawing Exception");
        }

        AffineTransform at = new AffineTransform();
        at.translate(x, y);
        java.awt.Shape moved_shape = at.createTransformedShape(tmp_shape);
        SVGGeneratorContext sgc = SVGGeneratorContext.createDefault(document);
        SVGPath moved = new SVGPath(sgc);
        try{
            tmp_textPath.setAttribute("d", moved.toSVG(moved_shape).getAttribute("d"));
        }
        catch(Exception e){
            System.out.println("A mad CSS exception.");
        }
        testSetText(((Text)(selected_text.getFirstChild().getFirstChild())).getTextContent());
    }

    public void componentRelocate(final int x, final int y){
        selected_component.setAttribute("x", ""+(((selected_component_station_point.getX()+shape_X))));
        selected_component.setAttribute("y", ""+(((selected_component_station_point.getY()+shape_Y))));
        if(selected_point_moveable){
            selected_point.setAttribute("x", selected_component.getAttribute("x"));
            selected_point.setAttribute("y", selected_component.getAttribute("y"));
        }
    }

    public void SEResizer(final int x, final int y){
        if(shape_X > shape_Y){
            shape_Y = shape_X;
        }

        else if(shape_Y > shape_X){
            shape_X = shape_Y;
        }
        selected_component.setAttribute("width", ""+(((selected_component_dimensions.getX()+shape_X))));
        selected_component.setAttribute("height", ""+(((selected_component_dimensions.getY()+shape_Y))));
        selected_point.setAttribute("x", Double.parseDouble(selected_component.getAttribute("x"))+Double.parseDouble(selected_component.getAttribute("width"))+"");
        selected_point.setAttribute("y", Double.parseDouble(selected_component.getAttribute("y"))+Double.parseDouble(selected_component.getAttribute("height"))+"");
        selected_button_box.setAttribute("width", selected_component.getAttribute("width"));
        selected_button_box.setAttribute("height", selected_component.getAttribute("height"));
        refresh();
    }

    public void componentBounds(final double x, final double y, final int position){
        p1("ComponentBounds called");
        Element rect = document.createElementNS(svgNS, "rect");
        rect.setAttribute("x", ""+x);
        rect.setAttribute("y", ""+y);
        rect.setAttribute("height", ""+5);
        rect.setAttribute("width", ""+5);
        rect.setAttribute("stroke", "red");
        rect.setAttribute("fill", "pink");
        rect.setAttribute("stroke-width", "1");
        selected_component.getParentNode().appendChild(rect);
        if(position == 1){
            rect.setAttribute("id", "NW_point");
            RegisterComponentNWPointListeners(rect);
        }
        else if(position == 2){
        }
        else if(position == 3){
        }
        else if(position == 4){
        }
        else if(position == 5){
            rect.setAttribute("id", "SE_point");
            RegisterComponentSEPointListeners(rect);
        }
        else if(position == 6){
        }
        else if(position == 7){
        }
                else if(position == 8){
//                    WRegistrar(rect);
                }
        refresh();
    }

    public void clearComponentBounds(){
        selected_component.getParentNode().removeChild(selected_button_box);
        selected_component.getParentNode().removeChild(document.getElementById("SE_point"));
        selected_component.getParentNode().removeChild(document.getElementById("NW_point"));
        refresh();
    }

    public void copyElement(){
        Element new_element = clipboard_element;
        new_element.setAttribute("x", Double.parseDouble(clipboard_element.getAttribute("x"))+10+"");
        new_element.setAttribute("y", Double.parseDouble(clipboard_element.getAttribute("y"))+10+"");
        selected_component.getParentNode().appendChild(new_element);
        registerButtonListeners(new_element);
        refresh();
    }

    public void patternDrag(){
        System.out.println("Pattern drag called");
        Point p = new Point(0,0);
        mouse_transformer.transform(mouse_point, p);
        final Point P = p;
        Runnable runnable = new Runnable() {
            public void run() {

                selected_drag_point.setAttribute("cx", mouse_point.getX() + "");
                selected_drag_point.setAttribute("cy", mouse_point.getY() + "");
                selected_shape.setAttribute("d", left_pattern_coords + " " + P.getX() + " " + P.getY() + " " + right_pattern_coords);
                if (textPath) {
                    document.getElementById(selected_text.getAttribute("id") + "_textPath").setAttribute("d", left_pattern_coords + " " + mouse_point.getX() + " " + mouse_point.getY() + " " + right_pattern_coords);
                }
                if (textPath) {
                    testSetText(((Text) (selected_text.getFirstChild().getFirstChild())).getTextContent());
                }
            }
        };
        UpdateManager um = canvas.getUpdateManager();
        um.addUpdateManagerListener(this);
        try{
            System.out.println("Line 3232");
                um.getUpdateRunnableQueue().invokeLater(runnable);
                System.out.println("Line 3234");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 3163: "+EX);
            }
    }

    public void currentDrawingDrag(){
        final double x = mouse_point.getX();
        final double y = mouse_point.getY();
        if(((!symmetric|x>222))){
            
            final Element current_drawing = document.getElementById("drawing_"+project_object.patterns.size());
            Runnable runnable = new Runnable(){
                public void run(){
                    selected_drag_point.setAttribute("cx",x+"");
                    selected_drag_point.setAttribute("cy",y+"");
                    current_drawing.setAttribute("d", left_pattern_coords+" "+x+" "+y+" "+right_pattern_coords);
                    if(textPath){
                        document.getElementById(selected_text.getAttribute("id")+"_textPath").setAttribute("d", left_pattern_coords+" "+x+" "+y+" "+right_pattern_coords);
                    }
                }
            };
            update_manager.getUpdateRunnableQueue().invokeLater(runnable);
            
        }
        if(textPath){
            Runnable runnable = new Runnable(){
                public void run(){
                    testSetText(((Text)(selected_text.getFirstChild().getFirstChild())).getTextContent());
                }
            };
            update_manager.getUpdateRunnableQueue().invokeLater(runnable);
        }
    }

    public void resizer(int scale){
        shape_scale = 50.0/(50+(50-scale));
        resize();
    }

    public void resize(){
        selected_shape.setAttribute("transform", "scale("+shape_scale+")");
        refresh();
    }

    public void axisAdjust(){
        if(update_manager == null){
            update_manager = canvas.getUpdateManager();
            update_manager.addUpdateManagerListener(this);
        }
        dbf.pointer_x_value.setText(Round(mouse_location.getX()/37,2)+"");
        dbf.pointer_y_value.setText(Round(mouse_location.getY()/37,2)+"");

        axis_X.setLine(0, mouse_location.getY(), getWidth(), mouse_location.getY());
        axis_Y.setLine(mouse_location.getX(), 0, mouse_location.getX(), getHeight());

        canvas_graphics = canvas.getGraphics();

//        Graphics2D myGraphics = canvas_graphics.create();

        canvas_graphics_2D = (Graphics2D)canvas_graphics;
        Line2D line = new Line2D.Double(20, 20, 100, 100);
        canvas.repaint();
//        axisRunner();
        
/*        graphics2d.setColor(Color.pink);
        graphics2d.draw(centreX);
        graphics2d.draw(centreY);
        graphics2d.setColor(Color.PINK);
        graphics2d.draw(centreX);
        graphics2d.draw(centreY);
*/
/*
        Runnable r = new Runnable(){
            public void run(){
                axis_X.setAttribute("x1","0");
        if(!alt){
            axis_X.setAttribute("y1",""+(mouse_location.getY()));
        }
        axis_X.setAttribute("x2",""+getWidth());
        if(!alt){
            axis_X.setAttribute("y2",""+(mouse_location.getY()));
        }
        if(!ctrl){
            axis_Y.setAttribute("x1",""+(mouse_location.getX()));
        }
        axis_Y.setAttribute("y1","0");
        if(!ctrl){
            axis_Y.setAttribute("x2",""+(mouse_location.getX()));
        }
        axis_Y.setAttribute("y2",""+getHeight());
            }
        };
        if(thread_counter < 5){
            UpdateManager um = canvas.getUpdateManager();
            um.addUpdateManagerListener(this);
            um.getUpdateRunnableQueue().invokeLater(r);
        }*/
    }

    public void axisRunner(){
        axis_thread = new Thread(){
            public void run(){
                while(true){
                    if(canvas_graphics_2D!=null){
                        canvas_graphics_2D.setColor(axis_color);
                        canvas_graphics_2D.draw(axis_X);
                        canvas_graphics_2D.draw(axis_Y);                        
                    }
                }
            }
        };
        if((!axis_thread.isAlive()) && (canvas_graphics_2D!=null)){
            axis_thread.setPriority(2);
//            axis_thread.start();
        }
    }

    public void refreshTree(){
        th.refreshTree(project_object, rt);
    }

    public static double Round(double Rval, int Rpl){
        double p = (double)Math.pow(10,Rpl);
        Rval = Rval * p;
        float tmp = Math.round(Rval);
        return (double)tmp/p;
    }


    public void filePrinter(){
        try{
            FileWriter file = new FileWriter ("The Document.svg");
            PrintWriter writer = new PrintWriter (file);
            SVGDocument svgDoc = canvas.getSVGDocument();
            DOMUtilities.writeDocument (document, writer);
            writer.close();
            System.out.println("File written");
        }
        catch(Exception e){
            System.err.println ("IO problem: " + e.toString () );
        }
    }

    public Element getRearView(){
        Element reverse_element = null;
        try{
            Element new_root = (Element)document.getElementById("this_side").cloneNode(true);
            Rectangle rear_rect = this.BoundryFinder(this_side, null);
            Element group = document.createElementNS(svgNS, "g");
            group.setAttribute("id", "group");

            System.out.print("rear_rect X: "+rear_rect.x);
            System.out.print("rear_rect Width: "+rear_rect.width);

            group.appendChild(new_root);

            group.setAttribute("transform", "translate("+canvas.getWidth()+") scale(-1,1)");
            new_root.setAttribute("opacity", "0.5");
            reverse_element = reverseMaker(group);
            rearFilePrinter(reverse_element);            
        }
        catch(Exception exc){
            System.out.println("Line 3053: "+exc);
        }
        return reverse_element;
    }

    public void rearFilePrinter(Element rear_root){
        try{
            FileWriter file = new FileWriter ("The RearDocument.svg");
            PrintWriter writer = new PrintWriter (file);
//            SVGDocument svgDoc = rear_root;
            DOMUtilities.writeNode(rear_root, writer);
            writer.close();
            System.out.println("File written");
        }
        catch(Exception e){
            System.err.println ("IO problem: " + e.toString () );
        }
    }

    public void setTextElement(){
        final Element textElement = document.createElementNS (svgNS,"text");
        
        NodeList nl = ((Element)selected_shape.getParentNode()).getElementsByTagName("text");
        System.out.println("The "+selected_shape.getAttribute("id")+" has "+nl.getLength()+" text elements.");

        textElement.setAttribute("id", "text_"+nl.getLength());
        textElement.setAttributeNS (null, "font-size", "40");
        textElement.setAttributeNS (null, "fill", "slateblue");
        textElement.setAttributeNS (null, "font-family", "Arial"+", Arial, sans-serif");
        selected_shape.getParentNode().appendChild(textElement);
        selected_text = textElement;
        refresh();
    }

    public void setTextPath(Element element){
        Element text_defs = null;
        try{
            text_defs = document.getElementById("text_defs");
            if(text_defs == null){
                text_defs = document.createElementNS(svgNS, "defs");
                text_defs.setAttributeNS(svgNS, "id", "text_defs");
                selected_text.getParentNode().appendChild(text_defs);
            }
        }
        catch(Exception e){
            System.out.println("Came into the catch");
            text_defs = document.createElementNS(svgNS, "defs");
            text_defs.setAttributeNS(svgNS, "id", "text_defs");
            document.getDocumentElement().appendChild(text_defs);
            System.out.println("text_defs appended");
        }
//        element.setAttribute("id", selected_text.getAttribute("id")+"_textPath");
        if(selected_text.getFirstChild()==null){
            Element path_element = (Element)element.cloneNode(true);
            path_element.setAttribute("id", selected_text.getAttribute("id")+"_textPath");
            Element path_container = document.createElementNS(svgNS, "svg");
            path_container.appendChild(path_element);
            path_container.setAttribute("x", 0+"");
            path_container.setAttribute("y", 0+"");
            System.out.println("X of container is: "+path_container.getAttribute("x"));
            System.out.println("Y of container is: "+path_container.getAttribute("y"));
            text_defs.appendChild(path_container);
            Element localtextPath = document.createElementNS(svgNS, "textPath");
            localtextPath.setAttributeNS(XLINK_NAMESPACE_URI, "xlink:href", "#"+path_element.getAttribute("id"));
            Text text_element = document.createTextNode("Sample Text");
            localtextPath.appendChild(text_element);
            selected_text.appendChild(localtextPath);
        }
        else{
            document.getElementById(selected_text.getAttribute("id")+"_textPath").setAttribute("d", element.getAttribute("d"));
        }
    }

    public Rectangle BoundryFinder(Element element, Rectangle rect){
        if(rect == null){
            SVGRect svg_rect = SVGLocatableSupport.getBBox(element);
            rect = new Rectangle((int)svg_rect.getX(), (int)svg_rect.getY(), (int)svg_rect.getWidth(), (int)svg_rect.getHeight());
        }
        if(element.getLocalName().equals("svg")){
            for(int i = 0; i < element.getChildNodes().getLength(); i++){
                rect = BoundryFinder((Element)element.getChildNodes().item(i), rect);
            }
        }
        else if(element.getLocalName().equals("g")){
            for(int i = 0; i < element.getChildNodes().getLength(); i++){
                rect = BoundryFinder((Element)element.getChildNodes().item(i), rect);
            }
        }
        else if(element.getLocalName().equals("path")){
            SVGRect local_rect = SVGLocatableSupport.getBBox(element);
            float local_x1 = local_rect.getX();
            float local_y1 = local_rect.getY();
            float local_x2 = local_rect.getX()+local_rect.getWidth();
            float local_y2 = local_rect.getY()+local_rect.getHeight();
            float rect_x1 = (float)rect.getX();
            float rect_y1 = (float)rect.getY();
            float rect_x2 = (float)rect.getX()+(float)rect.getWidth();
            float rect_y2 = (float)rect.getY()+(float)rect.getHeight();
            if(local_x1 < rect_x1){
                rect.x = (int)local_x1;
            }
            if(local_y1 < rect_y1){
                rect.y = (int)rect_y1;
            }
            if(local_x2 > rect_x2){
                rect.width = (int)(local_x2-rect_x1);
            }
            if(local_y2 > rect_y2){
                rect.height = (int)(local_y2-rect_y1);
            }
        }
        return rect;
    }

    public SVGRect BoundryFinder4Resize(Element element, SVGRect rect){
        if(rect == null){
            rect = SVGLocatableSupport.getBBox(element);
        }
        if(element.getLocalName().equals("svg")||element.getLocalName().equals("g")){
            for(int i = 0; i < element.getChildNodes().getLength(); i++){
                rect = BoundryFinder4Resize((Element)element.getChildNodes().item(i), rect);
            }
        }
        else{
            System.out.println("Subject element is: "+element.getAttribute("id"));

            SVGRect local_rect = SVGLocatableSupport.getBBox(element);

            float local_x1 = local_rect.getX();
            float local_y1 = local_rect.getY();
            float local_x2 = local_rect.getX()+local_rect.getWidth();
            float local_y2 = local_rect.getY()+local_rect.getHeight();

            float rect_x1 = rect.getX();
            float rect_y1 = rect.getY();
            float rect_x2 = rect.getX()+rect.getWidth();
            float rect_y2 = rect.getY()+rect.getHeight();

            if(local_x1 < rect_x1){
                rect.setX(local_x1);
            }
            if(local_y1 < rect_y1){
                rect.setY(local_y1);
            }
            if(local_x2 > rect_x2){
                rect.setWidth(local_x2-rect_x1);
            }
            if(local_y2 > rect_y2){
                rect.setHeight(local_y2-rect_y1);
            }
        }
        return rect;
    }

    public void getStringPresentation(Element e){
        CharArrayWriter tmp_character_array = new CharArrayWriter();
        String presentation = "";
        try{
            DOMUtilities.writeNode(e, tmp_character_array);
            presentation = tmp_character_array.toString();
            System.out.println(e.getAttribute("id")+": "+presentation);
        }

        catch(Exception ex){
            System.out.println("patterns creation exception: "+e);
        }
    }

    public void SymatricOn(){
        symmetric = true;
        Runnable r = new Runnable(){
            public void run(){
                Element root = document.getDocumentElement();
                Element symmetric_line = document.createElementNS(svgNS, "line");
                symmetric_line.setAttributeNS(null, "id", "symmetric_line");
                symmetric_line.setAttributeNS(null, "stroke", "black");
                symmetric_line.setAttributeNS(null, "stroke-width", "1");
                symmetric_line.setAttribute("x1","222");
                symmetric_line.setAttribute("y1",""+0);
                symmetric_line.setAttribute("x2",""+222);
                symmetric_line.setAttribute("y2",""+getWidth());
                root.appendChild(symmetric_line);
            }
        };
        UpdateManager um = canvas.getUpdateManager();
        um.addUpdateManagerListener(this);
        try{
            System.out.println("Line 3547");
                um.getUpdateRunnableQueue().invokeLater(r);
                System.out.println("Line 3549");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 3476: "+EX);
            }
    }

    public void SymatricOff(){
        symmetric = false;
        Runnable r = new Runnable(){
            public void run(){
                Element root = document.getDocumentElement();
                root.removeChild(document.getElementById("symmetric_line"));
            }
        };
        UpdateManager um = canvas.getUpdateManager();
        um.addUpdateManagerListener(this);
        try{
            System.out.println("Line 3567");
                um.getUpdateRunnableQueue().invokeLater(r);
                System.out.println("Line 3569");
            }
            catch(Exception EX){
                System.out.println("Invoke and Wait 3494: "+EX);
            }
    }

    public void keyReleased(KeyEvent e) {
//        displayInfo(e, "KEY RELEASED: ");
        //p1("Key Released"+e.getKeyChar());
        if(e.getKeyCode()==17){
            ctrl = false;
            dbf.pointer_y.setForeground(Color.GREEN);
        }
        else if(e.getKeyCode()==18){
            alt = false;
            dbf.pointer_x.setForeground(Color.GREEN);
        }
        else if(e.getKeyCode()==16){
            shift = false;
        }
    }

    public void keyPressed(KeyEvent e) {
        p3("Key Pressed"+e.getKeyChar());
        p3("Key Code is "+e.getKeyCode());
        if(e.getKeyCode()==10){
            completionAgent();
        }
        else if(e.getKeyCode()==0){
            delete_drawing();
        }
        else if(e.getKeyCode()==17){
            ctrl = true;
            dbf.pointer_y.setForeground(Color.RED);
/*            System.out.println("At CTRL current_drawing_locations: "+current_drawing_locations.size());
            System.out.println("At CTRL whole_path_locations_list: "+whole_path_locations_list.size());
            System.out.println("At CTRL location_coordinates_list: "+location_coordinates_list.size());
            System.out.println("At CTRL location_list: "+location_list.size());
*/        }
        else if(e.getKeyCode()==18){
            alt = true;
            dbf.pointer_x.setForeground(Color.RED);
        }
        else if(e.getKeyCode()==16){
            shift = true;
        }
        else if(e.getKeyCode()==67){
            clipboard_element = selected_component;
        }
        else if(e.getKeyCode()==86){
            copyElement();
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    private void p1(String s){
//        System.out.println(s);
    }
    private void p2(String s){
//        System.out.println(s);
    }
    private void p3(String s){
//        System.out.println(s);
    }
    private void p4(String s){
//        System.out.println(s);
    }

    private void addPoint(){
        CompoundEdit ce = new CompoundEdit();
        ce.end();

        manager.addEdit(ce);
        dbf.manager = manager;
        dbf.updateButtons();
    }

    public void addLine(){
//        Element line = drawLine();
        dbf.manager = manager;
        dbf.updateButtons();
    }

    public void printlocations(String msg){
//        System.out.println(msg+" current_drawing_locations: "+current_drawing_locations.size());
    }
    public void registerAllPaths(){
        org.w3c.dom.NodeList nl = document.getElementsByTagName("path");
        for(int i = 0; i < nl.getLength(); i++){
            registerPatternListeners((Element)nl.item(i));
        }
    }

    public void refresh(){
        Runnable r = new Runnable(){
            public void run(){
                System.out.println("Refresh called");
            }
        };
        try{
       	    update_manager.getUpdateRunnableQueue().invokeLater(r);
        }
        catch(Exception exception){
            System.out.println("Refresh() Exception 3603: "+exception);
        }
        
    }

    public void resizer(){
        System.out.println("resizer called");
        Element root = document.getDocumentElement();
        resize_handler = new resizeHandler(this, document, root, selected_shape, selected_point, svgNS, canvas);
        resize_handler.tester(document, root, selected_shape, selected_point, svgNS, canvas);
    }

    public void updateFailed(UpdateManagerEvent ume){
        System.out.println("updateFailed");
    }

    public void updateCompleted(UpdateManagerEvent ume){}

    public void updateStarted(UpdateManagerEvent ume){}

    public void managerSuspended(UpdateManagerEvent ume){
        System.out.println("managerSuspended: "+ume.getSource());
    }

    public void managerStopped(UpdateManagerEvent ume){
        System.out.println("managerStopped: "+ume.getSource());
    }

    public void managerStarted(UpdateManagerEvent ume){
        System.out.println("managerStarted: "+ume.getSource());
    }

    public void managerResumed(UpdateManagerEvent ume){
        System.out.println("managerResumed: "+ume.getSource());
    }
}
