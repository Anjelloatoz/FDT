package Design_tool_prototype1;

import java.awt.event.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.*;
import javax.swing.JOptionPane;
import java.io.ByteArrayOutputStream;
import org.apache.batik.util.Base64EncoderStream;

//import org.apache.batik.ext.awt.image.codec.ImageEncoder;
//import org.apache.batik.ext.awt.image.codec.PNGImageEncoder;
import org.apache.batik.ext.awt.image.codec.util.ImageEncoder;
import org.apache.batik.ext.awt.image.codec.png.PNGImageEncoder;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;


import java.awt.*;

import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import org.apache.commons.lang.StringUtils;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.*;
//import java.awt.geom.CubicCurve2D.*;

import org.apache.batik.bridge.UpdateManager;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.svg.SVGLocatableSupport;
import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGRect;
import org.w3c.dom.Element;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGPath;
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
import java.awt.event.*;

public class SVGConjurer extends JFrame implements ChangeListener, MouseListener, MouseMotionListener, KeyListener {
	static final long serialVersionUID = 333333;
	// Namespace string, to be used throughout the class
	private final String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
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
	private Element selected_shape;
        private Element selected_component;
        private Element selected_drag_point;
        private Element selected_point;
        private Element selected_button_box;
        private Element clipboard_element;
        Element fill_defs = null;
        Color color = new Color(0,0,0);
        private int scl = 10;
        Point2D mouse_point = new Point2D.Double(0, 0);
        private boolean moveable = false;
        private boolean component_moveable = false;
        private boolean drag_point_moveable = false;
        private boolean edit_point_moveable = false;
        private boolean selected_point_moveable = false;
        private boolean SE_resizeable = false;
        private Double shape_X;
        private Double shape_Y;
        private Double shape_scale;
        private Point2D pressed_point = null;
        java.util.List<Element> location_list = new ArrayList();
        java.util.List<Element> location_coordinates_list = new ArrayList();
        Element axis_X;
        Element axis_Y;
        private Point2D mouse_location;
        private Point2D mouse_released;
        private Point2D selected_component_station_point;
        private Point2D selected_shape_station_point;
        private Point2D selected_component_dimensions;
        Point2D before_drag_edit_point;
        String before_drag_drawing = "";
        Point2D after_drag_edit_point;
        String after_drag_drawing = "";

        Point2D prev_point_coords;
        private SVGGraphics2D generator;
        boolean axises = false;
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

        boolean symmetric = false;
        DrawingBoardFooter dbf;

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

        public SVGConjurer(){

        }
	public SVGConjurer(Dimension dim, alterStation as, DrawingBoardFooter dbf) {
		super("SVG Conjurer");
                this.as = as;
                this.dbf = dbf;
                dbf.manager = manager;

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                p1("ऒ");
                Cursor paint_cursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
		JPanel panel = new JPanel();
		JPanel p = new JPanel();

                try{
                File nat_file = new File("FW.PNG");
                natUri = nat_file.toURI().toString();
//                p1(nat_file.toURI().toString());
                }
                catch(Exception e){
                    p1("file Exception");
                }
		canvas = new JSVGCanvas();
		canvas.setMySize(dim);
		canvas.addMouseListener(this);
                canvas.addMouseMotionListener(this);
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
                generator = new SVGGraphics2D (document);

		canvas.setDocument(document);
	}

	public void mouseClicked(MouseEvent e) {
            if((shape_type_number != 0) && ((!symmetric|e.getX()>222))){
                last_location = e.getPoint();
                compound_edit = new CompoundEdit();


                p3("1. current_drawing_locations: "+current_drawing_locations.size());
                uadl = new UndoableAddDrawingLocation(this, last_location);
                compound_edit.addEdit(uadl);

                p3("3. current_drawing_locations: "+current_drawing_locations.size());
                uapl = new UndoableAddPathLocation(this, last_location);
                compound_edit.addEdit(uapl);
                
                Element coord_X = createLocationCoordinateX();
                Element coord_Y = createLocationCoordinateY();

                p3("5. current_drawing_locations: "+current_drawing_locations.size());
                ualc = new UndoableAddLocationCoordinates(this, coord_X, coord_Y);
                compound_edit.addEdit(ualc);

//                p3("6. current_drawing_locations: "+current_drawing_locations.size());

                Element location = drawLocation();

                p3("7. current_drawing_locations: "+current_drawing_locations.size());
                ual = new UndoableAddLocation(this, location, location_list, document);
                compound_edit.addEdit(ual);
//                System.out.println("1. Location list: "+location_list.size());

//                p3("8. current_drawing_locations: "+current_drawing_locations.size());

                if(current_drawing_locations.size() == shape_type_number){
                    drawSegment();
/*                    compound_edit.end();
                    manager.addEdit(compound_edit);
                    dbf.manager = manager;
                    dbf.updateButtons();
                    p2("At the end current_drawing_locations.size():"+current_drawing_locations.size());
                    p2("");*/
                }
                else{
                    compound_edit.end();
                    manager.addEdit(compound_edit);
                    dbf.manager = manager;
                    dbf.updateButtons();
//                    p2("At the end current_drawing_locations.size():"+current_drawing_locations.size());
                    p2("");
                }

//                removeElement(document.getElementById("prediction"));
//                drawPrediction();
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
//        locationSet();
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
        public void mouseDragged(MouseEvent e) {

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
            
            mouse_location = e.getPoint();
            shape_X = e.getX()- pressed_point.getX();
            shape_Y = e.getY() - pressed_point.getY();
            if(!axises)drawAxises();
            axisAdjust();
            if (moveable && shift){
                relocate(e.getX(), e.getY());
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
//                System.out.println(e.getX());
                currentDrawingDrag();
            }
        }
        public void mouseMoved(MouseEvent e) {
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
            if(!axises)drawAxises();
            as.setCoordinates(e.getX(), e.getY());
            axisAdjust();

            if(last_location != null){
//                adjustPrediction();
            }
        }

	// Changing the coordinates and the radius of the most recent ball
	public void stateChanged(ChangeEvent e) {
	}

        private void drawAxises(){
            axises = true;
            Runnable r = new Runnable() {
			public void run() {
				Element root = document.getDocumentElement();
				axis_X = document.createElementNS(svgNS, "line");
                                axis_X.setAttributeNS(null, "id", "axis_X");
				axis_X.setAttributeNS(null, "stroke", "lime");
				axis_X.setAttributeNS(null, "stroke-width", "1");
                                axis_Y = document.createElementNS(svgNS, "line");
                                axis_Y.setAttributeNS(null, "id", "axis_Y");
				axis_Y.setAttributeNS(null, "stroke", "lime");
				axis_Y.setAttributeNS(null, "stroke-width", "1");
				root.appendChild(axis_X);
                                root.appendChild(axis_Y);
			}
		};
		UpdateManager um = canvas.getUpdateManager();
		um.getUpdateRunnableQueue().invokeLater(r);
        }

        private void drawPrediction(){
            Runnable r = new Runnable() {
                public void run(){
                    Element root = document.getDocumentElement();
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
                    root.appendChild(prediction);
                }
            };
            UpdateManager um = canvas.getUpdateManager();
            um.getUpdateRunnableQueue().invokeLater(r);
        }

        public void adjustPrediction(){
            Runnable r = new Runnable(){
                public void run(){
                    Element e = document.getElementById("prediction");
                    String d = e.getAttribute("d");
                    d = StringUtils.substringBeforeLast(d, " ");
                    d = StringUtils.substringBeforeLast(d, " ");
                    e.setAttribute("d", d + " "+ mouse_point.getX()+" "+mouse_point.getY());
                }
            };
            UpdateManager um = canvas.getUpdateManager();
            um.getUpdateRunnableQueue().invokeLater(r);
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
        	Element root = document.getDocumentElement();
		Element location = document.createElementNS(svgNS, "circle");
                location.setAttributeNS(null, "id", "location");
		location.setAttributeNS(null, "stroke", "darkslateblue");
                location.setAttributeNS (null, "fill", "lightsteelblue");
		location.setAttributeNS(null, "stroke-width", "1");
		location.setAttributeNS(null, "r", "" + radius);
		location.setAttributeNS(null, "cx", "" + last_location.getX());
		location.setAttributeNS(null, "cy", "" + last_location.getY());

                return location;
        }

        private void drawLocationCoordinates(){
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
        }

        private void drawEditPoint(final double x, final double y, final int rad, final String fill_color){
            Runnable r = new Runnable() {
			public void run() {
				Element root = document.getDocumentElement();
				Element location = document.createElementNS(svgNS, "circle");
                                location.setAttributeNS(null, "id", "location");
				location.setAttributeNS(null, "stroke", "red");
                                location.setAttributeNS (null, "fill", fill_color);
				location.setAttributeNS(null, "stroke-width", "1");
				location.setAttributeNS(null, "r", ""+rad);
				location.setAttributeNS(null, "cx", "" + x);
				location.setAttributeNS(null, "cy", "" + y);
//                                p1("Location :"+last_location);
				root.appendChild(location);
//                                selected_shape.getParentNode().appendChild(location);
                                location_list.add(location);
                                dragPointListeners(location);

			}
		};
		UpdateManager um = canvas.getUpdateManager();
		um.getUpdateRunnableQueue().invokeLater(r);
        }

        private void drawSegment(){
//            drawImage();
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
                p3("9. current_drawing_locations: "+current_drawing_locations.size());
//                System.out.println("Location List: "+location_list.size());
                uae = new UndoableAddElement(this, current_drawing, location_list.get(1));
                compound_edit.addEdit(uae);
                p3("10. current_drawing_locations: "+current_drawing_locations.size());
            }
            p3("12. current_drawing_locations: "+current_drawing_locations.size());
            usa = new UndoableSetAttribute(this, current_drawing, "d", current_drawing.getAttributeNS(null, "d")+" L "+current_drawing_locations.get(1).getX()+" "+current_drawing_locations.get(1).getY());
            compound_edit.addEdit(usa);
            
            p3("17. current_drawing_locations: "+current_drawing_locations.size());
            url = new UndoableReplaceList(this);
            compound_edit.addEdit(url);

            usdip = new UndoableSwitchDrawingInProgress(this, true);
            compound_edit.addEdit(usdip);
            compound_edit.end();
            manager.addEdit(compound_edit);
            dbf.manager = manager;
            dbf.updateButtons();
            p3("18. current_drawing_locations: "+current_drawing_locations.size());
        }

        private void drawArc(){
            Element root = document.getDocumentElement();
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
//            System.out.println("End of the arc: "+current_drawing_locations.size());
        }

        public void removePrediction(){
            Element e = document.getElementById("prediction");
            e.getParentNode().removeChild(e);
        }

        public void completionAgent(){
            p4("Came into the Completion agent");
            Element current_drawing = document.getElementById("drawing_"+project_object.patterns.size());
            compound_edit = new CompoundEdit();
            finishDrawing(null, current_drawing);
            compound_edit.end();
            manager.addEdit(compound_edit);
            dbf.manager = manager;
            dbf.updateButtons();
            
            try{
                Element e = document.getElementById("prediction");
                e.getParentNode().removeChild(e);
            }
            catch(Exception ex){
                System.out.println("The prediction could not be found.");
            }            
        }

        public void finishDrawing(final Element destination, final Element current_drawing){
            java.awt.Shape test_shape = null;

            try{
                test_shape = org.apache.batik.parser.AWTPathProducer.createShape(new StringReader(current_drawing.getAttributeNS(null,"d")), new GeneralPath().WIND_EVEN_ODD);
            }
            catch(Exception e){
                System.out.println("Test Exception");
            }
            java.awt.geom.AffineTransform at = new java.awt.geom.AffineTransform();
            at.scale(-1, 1);
            java.awt.Shape mirrored_shape = at.createTransformedShape(test_shape);

            String test_d = "";
            PathIterator pi = mirrored_shape.getPathIterator(null);
                        double[] split_path_coords = new double[6];

                        while(!pi.isDone()){
                            int segment = pi.currentSegment(split_path_coords);
                            if(segment == pi.SEG_MOVETO){
                                test_d = "M "+split_path_coords[0]+" "+split_path_coords[1];
                            }
                            if(segment == pi.SEG_LINETO){
                                test_d = test_d+" L "+split_path_coords[0]+" "+split_path_coords[1];
                            }
                            if(segment == pi.SEG_CUBICTO){
                                test_d = test_d+" C "+split_path_coords[0]+" "+split_path_coords[1]+" "+split_path_coords[2]+" "+split_path_coords[3]+" "+split_path_coords[4]+" "+split_path_coords[5];
                            }
                            pi.next();
                        }
//                        System.out.println("The Mirrored shape is : "+test_d);


                  Element root = document.getDocumentElement();
                  try{
                      usa = new UndoableSetAttribute(this, current_drawing, "d", current_drawing.getAttributeNS(null, "d")+" Z");
                      compound_edit.addEdit(usa);
                  }

                  catch(Exception e){
                      return;
                  }

                  Element svg_element = document.createElementNS(svgNS, "svg");
                  svg_element.setAttribute("id", "svg_"+current_drawing.getAttribute("id"));
                  svg_element.setAttribute("x", ""+0);
                  svg_element.setAttribute("y", ""+0);

                  uac = new UndoableAppendChild(this, svg_element, current_drawing);
                  compound_edit.addEdit(uac);

                  if(destination_container == null){
                      patternObject po = new patternObject();
                      po.front = document.createElementNS(svgNS, "svg");
                      po.front.appendChild(svg_element);
                      uac = new UndoableAppendChild(this, root, po.front);
                      compound_edit.addEdit(uac);

                      uapo = new UndoableAddPatternObject(po, project_object, th, rt);
                      compound_edit.addEdit(uapo);
                      th.refreshTree(project_object, rt);
                  }

                  else{
                      uac = new UndoableAppendChild(this, destination_container, svg_element);
                      compound_edit.addEdit(uac);
                      th.refreshTree(project_object, rt);
                  }
                  
                  registerPatternListeners(current_drawing);
                  udni = new UndoableDrawingNumberIncrease(this);
                  compound_edit.addEdit(udni);

            p3("In the finish drawing location_list: "+location_list.size());
            for(int i = 0; i < location_list.size(); i++){
                urc = new UndoableRemoveChild(this, (Element)location_list.get(i).getParentNode(), location_list.get(i));
                compound_edit.addEdit(urc);
            }

            p3("In the finish drawing location_coordinates_list: "+location_coordinates_list.size());
            for(int i = 0; i < location_coordinates_list.size(); i++){
                urc = new UndoableRemoveChild(this, (Element)location_coordinates_list.get(i).getParentNode(), location_coordinates_list.get(i));
                compound_edit.addEdit(urc);
            }
            ucll = new UndoableClearLocationList(location_list);
            compound_edit.addEdit(ucll);
            
            ucll = new UndoableClearLocationList(location_coordinates_list);
            compound_edit.addEdit(ucll);

            usdip = new UndoableSwitchDrawingInProgress(this, false);
            compound_edit.addEdit(usdip);

            ucpl = new UndoableClearPointList(current_drawing_locations);
            compound_edit.addEdit(ucpl);

            ucpl = new UndoableClearPointList(whole_path_locations_list);
            compound_edit.addEdit(ucpl);

            uclc = new UndoableClearLocation(last_location);
            compound_edit.addEdit(uclc);
//            destination_container = null;

//            System.out.println("Current drawing parent is "+current_drawing.getParentNode().getLocalName()+" "+((Element)current_drawing.getParentNode()).getAttribute("id"));
        }

        public void addNewLayer(Element element){
            Element root = document.getDocumentElement();
            int number_of_svgs = root.getElementsByTagName("svg").getLength();
            Element new_container = document.createElementNS(svgNS, "svg");
            new_container.setAttribute("id", "svg_drawing_"+number_of_svgs+1);
            new_container.appendChild(element);
            registerPatternListeners(element);
            root.appendChild(new_container);
            refresh();
            th.refreshTree(project_object, rt);
        }

        public void addNewPattern(){
            patternObject po = new patternObject();
            po.front = document.createElement("svg");
            Element root = document.getDocumentElement();
            root.appendChild(po.front);
            uapo = new UndoableAddPatternObject(po, project_object, th, rt);
            compound_edit.addEdit(uapo);
            th.refreshTree(project_object, rt);
        }

        public void symmetricFinishDrawing(){
//            System.out.println("In the symmetricFinishDrawing");
            Element current_drawing = document.getElementById("drawing_"+project_object.patterns.size());

            java.awt.Shape tmp_shape = null;
            try{
                tmp_shape = org.apache.batik.parser.AWTPathProducer.createShape(new StringReader(current_drawing.getAttributeNS(null,"d")), new GeneralPath().WIND_EVEN_ODD);
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
	    um.getUpdateRunnableQueue().invokeLater(r);
            
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
            String short_backward_path;

            java.awt.Shape tmp_shape = null;
            backward_path = "";
            try{
                tmp_shape = org.apache.batik.parser.AWTPathProducer.createShape(new StringReader(subject_element.getAttribute("d")), new GeneralPath().WIND_EVEN_ODD);
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
            chopped_backward_path = backward_path;;
            backward_path = "M "+backward_path;
//            System.out.println("The Forward Path is "+forward_path);
//            System.out.println("The Backward Path is "+backward_path);
            Element reversed_element = (Element)subject_element.cloneNode(false);
            reversed_element.setAttribute("d", backward_path);
            return reversed_element;
        }

        public Element setGuideImage(final String image_path, final int width, final int height){
            final Element my_image = document.createElementNS(svgNS, "image");
            Runnable r = new Runnable(){
                public void run(){
                    Element defs = document.createElementNS(svgNS, "defs");
                    defs.setAttribute("id", "guide image defs");
                    Element root = document.getDocumentElement();
                    root.appendChild(defs);

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

                  root.appendChild(my_image);
                }
            };
            UpdateManager um = canvas.getUpdateManager();
	    um.getUpdateRunnableQueue().invokeLater(r);
            return my_image;
        }

        public void DeleteGuideImage(final Element e){
            Runnable r = new Runnable(){
                public void run(){
                    Element root = document.getDocumentElement();
                    root.removeChild(e);
                }
            };
            UpdateManager um = canvas.getUpdateManager();
	    um.getUpdateRunnableQueue().invokeLater(r);
        }

        public String getOpacity(){
            Element image = document.getElementById("natasha_png");
            p1(image.getAttributeNS(null, "opacity"));
            return image.getAttributeNS(null, "opacity");
        }

        public void setOpacity(final double opacity){
            final Element image = document.getElementById("natasha_png");
            Runnable r = new Runnable(){
            public void run(){
                image.setAttributeNS(null, "opacity", ""+opacity);
            }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
        }

        public JSVGCanvas getBoard(){
        return canvas;
        }

        public void patternSplitter(){

//            removePrediction();
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
                        s = org.apache.batik.parser.AWTPathProducer.createShape(new StringReader(selected_shape.getAttributeNS(null,"d")), new GeneralPath().WIND_EVEN_ODD);
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
                                s = org.apache.batik.parser.AWTPathProducer.createShape(new StringReader(new_shape), new GeneralPath().WIND_EVEN_ODD);
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
                            original_split_path = org.apache.batik.parser.AWTPathProducer.createShape(new StringReader(tmp.getAttribute("d")), new GeneralPath().WIND_EVEN_ODD);
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
		um.getUpdateRunnableQueue().invokeLater(r);
        }

        public void splitAdjuster(){
            java.awt.Shape original_shape = null;
            try{
                original_shape = org.apache.batik.parser.AWTPathProducer.createShape(new StringReader(selected_shape.getAttributeNS(null,"d")), new GeneralPath().WIND_EVEN_ODD);
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
            p1(selected_shape.getAttribute("fill"));
            final String DATA_PROTOCOL_PNG_PREFIX = "data:image/png;base64,";
            /********************/

            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            Base64EncoderStream b64Encoder = new Base64EncoderStream(os);


            ImageEncoder encoder = new PNGImageEncoder(b64Encoder, null);

            try{
                encoder.encode(fill_image);
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
            Runnable r = new Runnable(){
                public void run(){

                  Element root = document.getDocumentElement();
                  if(fill_defs == null){
                      fill_defs = document.createElementNS(svgNS, "defs");
                      fill_defs.setAttribute("id", "pattern_defs");
                      root.appendChild(fill_defs);
                      p1("Fill defs didnt have children");
                  }

                  if(!selected_shape.getAttribute("fill").equals("none")){
                       p1("in here!!");
                       selected_shape.setAttribute("fill", "none");
                       fill_defs.removeChild(document.getElementById("pattern_"+selected_shape.getAttribute("id")));
                  }

                  Element fill_image = document.createElementNS(svgNS, "image");
                  fill_image.setAttributeNS(XLINK_NAMESPACE_URI, "xlink:href", DATA_PROTOCOL_PNG_PREFIX+os.toString());
                  fill_image.setAttributeNS(null, "id", "fill_image_"+selected_shape.getAttribute("id"));
                  p1(fill_image.getAttribute("id"));
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
                }
            };
            UpdateManager um = canvas.getUpdateManager();
	    um.getUpdateRunnableQueue().invokeLater(r);
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
                elementIterator((Element)node_list.item(i));
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

            /********************/
            final String DATA_PROTOCOL_PNG_PREFIX = "data:image/png;base64,";
            /********************/

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
            
                  Element root = document.getDocumentElement();
                  SVGLocatableSupport ls = new SVGLocatableSupport();
                  SVGRect rect = ls.getBBox(selected_shape);
                  float x = rect.getX();
                  float y = rect.getY();
                  float width = rect.getWidth();
                  float height = rect.getHeight();

                  int button_count = selected_shape.getParentNode().getChildNodes().getLength();

                  Element my_image = document.createElementNS(svgNS, "image");
//                  my_image.setAttributeNS(XLINK_NAMESPACE_URI, "xlink:href", button_path.toString());
                  my_image.setAttributeNS(XLINK_NAMESPACE_URI, "xlink:href", DATA_PROTOCOL_PNG_PREFIX+os.toString());
                  my_image.setAttributeNS(null, "id", "button_"+button_count);
                  my_image.setAttributeNS(null, "x", ""+x);
                  my_image.setAttributeNS (null, "y", ""+y);
		  my_image.setAttributeNS(null, "width", ""+width/2);
        	  my_image.setAttributeNS(null, "height", ""+height/2);
                  selected_shape.getParentNode().appendChild(my_image);
//                  selected_shape.setAttribute("viewBox", "0 0 90 90");
//                  root.appendChild(svg_element);
                  registerButtonListeners(my_image);
                  //root.appendChild(selected_shape);
                  Runnable r = new Runnable(){
                public void run(){
                }
            };
            UpdateManager um = canvas.getUpdateManager();
	    um.getUpdateRunnableQueue().invokeLater(r);
            th.refreshTree(project_object, rt);
        }

        public void showPoints(){
            java.awt.Shape s = null;
            try{
                        s = org.apache.batik.parser.AWTPathProducer.createShape(new StringReader(selected_shape.getAttributeNS(null,"d")), new GeneralPath().WIND_EVEN_ODD);
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
            Runnable r = new Runnable(){
                public void run(){
                    Element root = document.getDocumentElement();
                    root.removeChild(selected_shape);
                }
            };
            UpdateManager um = canvas.getUpdateManager();
	    um.getUpdateRunnableQueue().invokeLater(r);
        }


/////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
//////////////////// REGISTERING THE LISTENERS \\\\\\\\\\\\\\\\\\\\\\\\\\\\
////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

        public void removeListeners(Element element){
        EventTarget t1 = (EventTarget)element;


        t1.removeEventListener("mouseover", new OnMouseMuteAction(), false);
        t1.removeEventListener("mouseout", new OnMouseMuteAction(), false);
        t1.removeEventListener("click", new OnMouseMuteAction(), false);
        t1.removeEventListener("mousedown", new OnMouseMuteAction(), false);
        t1.removeEventListener("mouseup", new OnMouseMuteAction(), false);
    }

        public class OnMouseMuteAction implements EventListener {
        public void handleEvent (Event evt) {
            System.out.println("OnMouseMuteAction");
        }
      }
    public void registerEditPointListeners(Element element){
        EventTarget t1 = (EventTarget)element;

        t1.addEventListener ("mouseover", new OnMouseOverEditPointAction(), false);
        t1.addEventListener ("mouseout", new OnMouseOutEditPoint(), false);
        t1.addEventListener ("click", new OnEditPointClickAction(), false);
        t1.addEventListener ("mousedown", new OnEditPointMouseDownAction(), false);
        t1.addEventListener ("mouseup", new OnEditPointMouseUpAction(), false);
    }
    
    public class OnMouseOverEditPointAction implements EventListener {
        public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         e1.setAttribute("fill", "red");
         tmp_shape_type_number = shape_type_number;
         shape_type_number = 0;
        }
      }
    
    public class OnMouseOutEditPoint implements EventListener {
     public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         e1.setAttribute("fill", "lightsteelblue");
         shape_type_number = tmp_shape_type_number;
        }
      }
    
    public class OnEditPointClickAction implements EventListener {
     public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
        }
      }
    
    public class OnEditPointMouseDownAction implements EventListener {
     public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         selected_drag_point = e1;
         e1.setAttribute ("fill", "red");
         double x = Integer.parseInt(StringUtils.substringBefore(e1.getAttribute("cx"),"."));
         double y = Integer.parseInt(StringUtils.substringBefore(e1.getAttribute("cy"),"."));
         before_drag_edit_point = new Point2D.Double(x, y);

         x = Double.parseDouble(e1.getAttribute("cx"));
         y = Double.parseDouble(e1.getAttribute("cy"));
         prev_point_coords = new Point2D.Double(x, y);
         Element current_drawing = document.getElementById("drawing_"+project_object.patterns.size());
         p2("Shape coords are: "+current_drawing.getAttribute("d"));
         before_drag_drawing = current_drawing.getAttribute("d");
         prev_pattern_coords = current_drawing.getAttribute("d");
//         System.out.println("Selected drag point: "+x+" "+y);
         p2("Selected drag point untruncated: "+e1.getAttribute("cx")+" "+e1.getAttribute("cy"));
         left_pattern_coords = StringUtils.substringBefore(current_drawing.getAttribute("d"), x+" "+y);
         p2("left_pattern_coords: "+left_pattern_coords);
         right_pattern_coords = StringUtils.substringAfter(current_drawing.getAttribute("d"), x+" "+y+" ");
         p2("right_pattern_coords: "+right_pattern_coords);
         edit_point_moveable = true;
         canvas.setCursor(dflt_cursor);
        }
      }

        public class OnEditPointMouseUpAction implements EventListener {
     public void handleEvent (Event evt){
         p2("In the mouse up");
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         Element current_drawing = document.getElementById("drawing_"+project_object.patterns.size());

         double x = Integer.parseInt(StringUtils.substringBefore(e1.getAttribute("cx"),"."));
         double y = Integer.parseInt(StringUtils.substringBefore(e1.getAttribute("cy"),"."));
         after_drag_edit_point = new Point2D.Double(x, y);
         after_drag_drawing = current_drawing.getAttribute("d");
         edit_point_moveable = false;
         UndoablePatternDragCaller(e1);
     }
      }

        public void UndoablePatternDragCaller(Element e1){
//            System.out.println("UndoablePatternDragCaller called");
            Element current_drawing = document.getElementById("drawing_"+project_object.patterns.size());
            upd = new UndoablePatternDrag(this, before_drag_edit_point, after_drag_edit_point, before_drag_drawing, after_drag_drawing, current_drawing, selected_drag_point);
            compound_edit = new CompoundEdit();
            compound_edit.addEdit(upd);
            compound_edit.end();
            manager.addEdit(compound_edit);
            dbf.manager = manager;
            dbf.updateButtons();
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

        public class PATTERN_MouseOverAction implements EventListener {
        public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         SVGLocatableSupport ls = new SVGLocatableSupport();
         SVGRect rect = ls.getBBox(e1);
         e1.setAttribute ("stroke", "red");
         e1.setAttributeNS(null, "pathLength", "200");
         canvas.setCursor(hand_cursor);
        }
      }

        public class PATTERN_MouseOutAction implements EventListener {
     public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         e1.setAttribute ("stroke", "black");
         e1.setAttributeNS (null, "stroke-width", "1");
         canvas.setCursor(dflt_cursor);
        }
      }

        public class PATTERN_OnClickAction implements EventListener {
     public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         selected_shape = e1;
         Element child = (Element)e1.getParentNode();
         Element parent = (Element)e1.getParentNode().getParentNode();
         parent.removeChild(child);
         parent.appendChild(child);
        }
      }

        public class PATTERN_MouseDownAction implements EventListener {
     public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         selected_shape = e1;
         moveable = true;
         try{
         selected_shape_station_point = new Point2D.Double(Double.parseDouble(((Element)selected_shape.getParentNode()).getAttribute("x")),Double.parseDouble(((Element)selected_shape.getParentNode()).getAttribute("y")));
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
         p1("OnMouseOverNWPointAction Called");
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         e1.setAttribute ("fill", "hotpink");
         p1("The elementID is "+e1.getAttribute("id"));
         canvas.setCursor(move_cursor);
        }
      }

        public class COMPONENT_NW_POINT_MouseOutAction implements EventListener {
        public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         e1.setAttribute ("fill", "hotpink");
         canvas.setCursor(dflt_cursor);
        }
      }
        
        public class COMPONENT_NW_POINT_MouseDownAction implements EventListener {
     public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         selected_point = e1;
         e1.setAttribute ("fill", "red");
         selected_component_station_point = new Point2D.Double(Double.parseDouble(selected_component.getAttribute("x")),Double.parseDouble(selected_component.getAttribute("y")));
         selected_point_moveable = true;
         canvas.setCursor(move_cursor);
        }
      }

        public class COMPONENT_NW_POINT_MouseUpAction implements EventListener {
     public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         selected_point = e1;
         e1.setAttribute ("fill", "red");
         selected_point_moveable = false;
         canvas.setCursor(dflt_cursor);
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
         Element e1 = (Element)tt;
         SVGLocatableSupport ls = new SVGLocatableSupport();
         SVGRect rect = ls.getBBox(e1);
         float x = rect.getX();
         float y = rect.getY();
         float width = rect.getWidth();
         float height = rect.getHeight();
         Element button_box = document.createElementNS(svgNS, "rect");
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
         e1.getParentNode().appendChild(button_box);
         canvas.setCursor(hand_cursor);
        }
      }
        
        public class BUTTON_MouseOutAction implements EventListener {
        public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         p1("Element to be removed is "+document.getElementById("button_box-"+e1.getAttribute("id")));
         e1.getParentNode().removeChild(document.getElementById("button_box-"+e1.getAttribute("id")));
//         e1.setAttribute ("fill", "white");
         canvas.setCursor(dflt_cursor);
        }
      }

        public class BUTTON_ClickAction implements EventListener {
        public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         System.out.append("Button Clicked");

         SVGLocatableSupport ls = new SVGLocatableSupport();
         SVGRect rect = ls.getBBox(e1);
         float fx = rect.getX();
         float fy = rect.getY();
         float fwidth = rect.getWidth();
         float fheight = rect.getHeight();

         double x = Double.parseDouble(e1.getAttribute("x"));
         double y = Double.parseDouble(e1.getAttribute("y"));
         double width = Double.parseDouble(e1.getAttribute("width"));
         double height = Double.parseDouble(e1.getAttribute("height"));
         selected_button_box = document.createElementNS(svgNS, "rect");
         selected_button_box.setAttribute("id", "selected_button_box-"+e1.getAttribute("id"));
         p1("fx is "+fx);
         p1("fy is "+fy);
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
         Element e1 = (Element)tt;
         e1.setAttribute ("fill", "hotpink");
         Cursor my_cursor = new Cursor(Cursor.E_RESIZE_CURSOR);
         canvas.setCursor(hand_cursor);
        }
      }

        public class OnMouseOutDragPoint implements EventListener {
        public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         e1.setAttribute ("fill", "white");
         Cursor n_cursor = new Cursor(Cursor.DEFAULT_CURSOR);
         //setCursor(n_cursor);
         canvas.setCursor(n_cursor);
        }
      } 

        public class OnDragPointClickAction implements EventListener {
        public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         e1.setAttribute ("fill", "red");
         selected_drag_point = e1;
         p1("The break point: "+e1.getAttribute("cx")+" "+e1.getAttribute("cy"));
         left_pattern_coords = StringUtils.substringBefore(selected_shape.getAttribute("d"), e1.getAttribute("cx")+".0 "+e1.getAttribute("cy")+".0");
         right_pattern_coords = StringUtils.substringAfter(selected_shape.getAttribute("d"), e1.getAttribute("cx")+".0 "+e1.getAttribute("cy")+".0");
         p1("************************** "+left_pattern_coords);
         p1(right_pattern_coords);
        }
      }

        public class OnDragPointMouseDownAction implements EventListener {
     public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         selected_drag_point = e1;
         e1.setAttribute ("fill", "red");
         double x = Integer.parseInt(StringUtils.substringBefore(e1.getAttribute("cx"),"."));
         double y = Integer.parseInt(StringUtils.substringBefore(e1.getAttribute("cy"),"."));

         x = Double.parseDouble(e1.getAttribute("cx"));
         y = Double.parseDouble(e1.getAttribute("cy"));
         p2("Shape coords are: "+selected_shape.getAttribute("d"));
//         System.out.println("Selected drag point: "+x+" "+y);
         p2("Selected drag point untruncated: "+e1.getAttribute("cx")+" "+e1.getAttribute("cy"));
         left_pattern_coords = StringUtils.substringBefore(selected_shape.getAttribute("d"), x+" "+y+" ");
         right_pattern_coords = StringUtils.substringAfter(selected_shape.getAttribute("d"), x+" "+y+" ");
         drag_point_moveable = true;
         canvas.setCursor(dflt_cursor);
        }
      }

        public class OnDragPointMouseUpAction implements EventListener {
     public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         selected_drag_point = null;
         drag_point_moveable = false;
         canvas.setCursor(dflt_cursor);
        }
      }

    public void relocate(final int x, final int y){
        Runnable r = new Runnable(){
            public void run(){
                //selected_shape.setAttribute("transform", "translate("+shape_X+" "+shape_Y+")");
                ((Element)selected_shape.getParentNode()).setAttribute("x", ""+(selected_shape_station_point.getX()+shape_X));
                ((Element)selected_shape.getParentNode()).setAttribute("y", ""+(selected_shape_station_point.getY()+shape_Y));
//                ((Element)selected_shape.getParentNode()).setAttribute("x", ""+shape_X);
            }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }

    public void componentRelocate(final int x, final int y){
//        p1(shape_X);
//        p1(shape_Y);
        Runnable r = new Runnable(){
            public void run(){
                //selected_shape.setAttribute("transform", "translate("+shape_X+" "+shape_Y+")");

                selected_component.setAttribute("x", ""+(((selected_component_station_point.getX()+shape_X))));
                selected_component.setAttribute("y", ""+(((selected_component_station_point.getY()+shape_Y))));
                if(selected_point_moveable){
                    selected_point.setAttribute("x", selected_component.getAttribute("x"));
                    selected_point.setAttribute("y", selected_component.getAttribute("y"));
                }
            }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }

    public void SEResizer(final int x, final int y){
//        p1("Component relocate called");
//        p1(shape_X);
//        p1(shape_Y);
        Runnable r = new Runnable(){
            public void run(){
                //selected_shape.setAttribute("transform", "translate("+shape_X+" "+shape_Y+")");

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

            }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }

    public void componentBounds(final double x, final double y, final int position){
        p1("ComponentBounds called");
        Runnable r = new Runnable(){
            public void run(){
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
            //        NRegistrar(rect);
                }
                else if(position == 3){
          //          NERegistrar(rect);
                }
                else if(position == 4){
        //            ERegistrar(rect);
                }
                else if(position == 5){
                    rect.setAttribute("id", "SE_point");
                    RegisterComponentSEPointListeners(rect);
                }
                else if(position == 6){
    //                SRegistrar(rect);
                }
                else if(position == 7){
  //                  SWRegistrar(rect);
                }
                else if(position == 8){
//                    WRegistrar(rect);
                }
            }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);        
    }

    public void clearComponentBounds(){
        Runnable r = new Runnable(){
            public void run(){
                selected_component.getParentNode().removeChild(selected_button_box);
                selected_component.getParentNode().removeChild(document.getElementById("SE_point"));
                selected_component.getParentNode().removeChild(document.getElementById("NW_point"));
            }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }

    public void copyElement(){
        Runnable r = new Runnable(){
            public void run(){
                Element new_element = clipboard_element;
                new_element.setAttribute("x", Double.parseDouble(clipboard_element.getAttribute("x"))+10+"");
                new_element.setAttribute("y", Double.parseDouble(clipboard_element.getAttribute("y"))+10+"");
                selected_component.getParentNode().appendChild(new_element);
                registerButtonListeners(new_element);
            }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }

    public void patternDrag(){
        Runnable r = new Runnable(){
            public void run(){
                selected_drag_point.setAttribute("cx",mouse_point.getX()+"");
                selected_drag_point.setAttribute("cy",mouse_point.getY()+"");
//                p1("Left pattern coords are: "+left_pattern_coords);
//                p1("Mouse coords are: "+mouse_point.getX()+" "+mouse_point.getY());
//                p1("Right pattern coords are: "+right_pattern_coords);
//                p1("pattern D is : "+left_pattern_coords+" "+mouse_point.getX()+" "+mouse_point.getY()+" "+right_pattern_coords);
                System.out.println("Dragged code is: "+left_pattern_coords+" "+mouse_point.getX()+" "+mouse_point.getY()+" "+right_pattern_coords);
                selected_shape.setAttribute("d", left_pattern_coords+" "+mouse_point.getX()+" "+mouse_point.getY()+" "+right_pattern_coords);
            }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }

    public void currentDrawingDrag(){
//        p2("CurrentDrawingDrag called");
        Runnable r = new Runnable(){
            public void run(){
                int x = (int)mouse_point.getX();
                int y = (int)mouse_point.getY();
                if(((!symmetric|x>222))){
                selected_drag_point.setAttribute("cx",x+"");
                selected_drag_point.setAttribute("cy",y+"");
                Element current_drawing = document.getElementById("drawing_"+project_object.patterns.size());
//                p2("Left pattern coords are: "+left_pattern_coords);
//                p2("Mouse coords are: "+mouse_point.getX()+" "+mouse_point.getY());
//                p2("Right pattern coords are: "+right_pattern_coords);
//                p2("pattern D is : "+left_pattern_coords+" "+mouse_point.getX()+" "+mouse_point.getY()+" "+right_pattern_coords);
                current_drawing.setAttribute("d", left_pattern_coords+" "+x+" "+y+" "+right_pattern_coords);
                }
            }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }

    public void resizer(int scale){
        shape_scale = 50.0/(50+(50-scale));
        resize();
    }

    public void resize(){
        Runnable r = new Runnable(){
            public void run(){
                selected_shape.setAttribute("transform", "scale("+shape_scale+")");
            }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }

    public void axisAdjust(){

        dbf.pointer_x_value.setText(Round(mouse_location.getX()/37,2)+"");
        dbf.pointer_y_value.setText(Round(mouse_location.getY()/37,2)+"");
        Runnable r = new Runnable(){
            public void run(){
                axis_X.setAttribute("x1","0");
                if(!alt){
                    axis_X.setAttribute("y1",""+mouse_location.getY());
                }
                axis_X.setAttribute("x2",""+getWidth());
                if(!alt){
                    axis_X.setAttribute("y2",""+mouse_location.getY());
                }

                if(!ctrl){
                    axis_Y.setAttribute("x1",""+mouse_location.getX());
                }
                axis_Y.setAttribute("y1","0");
                if(!ctrl){
                    axis_Y.setAttribute("x2",""+mouse_location.getX());
                }
                axis_Y.setAttribute("y2",""+getHeight());
            }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }

    public static double Round(double Rval, int Rpl) {
  double p = (double)Math.pow(10,Rpl);
  Rval = Rval * p;
  float tmp = Math.round(Rval);
  return (double)tmp/p;
    }


    public void filePrinter(){
        try{
            Element root = document.getDocumentElement();

            FileWriter file = new FileWriter ("The Document.svg");
            PrintWriter writer = new PrintWriter (file);
            SVGDocument svgDoc = canvas.getSVGDocument();
            DOMUtilities.writeDocument (document, writer);
            writer.close();
        }
        catch(Exception e){
            System.err.println ("IO problem: " + e.toString () );
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
        um.getUpdateRunnableQueue().invokeLater(r);
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
        um.getUpdateRunnableQueue().invokeLater(r);
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
            clearComponentBounds();
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
//        displayInfo(e, "KEY TYPED: ");
        //p1("Key Typed"+e.getKeyChar());
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


        Element location = drawLocation();


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

    public void refresh(){
        Runnable r = new Runnable(){
          public void run(){}
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }
}
