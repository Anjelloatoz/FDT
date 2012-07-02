package Design_tool_prototype1;

import java.awt.event.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.*;

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
import org.apache.batik.dom.util.DOMUtilities;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.awt.geom.AffineTransform;
import java.io.StringReader;


public class SVGConjurer extends JFrame implements ChangeListener, MouseListener, MouseMotionListener, KeyListener {
	static final long serialVersionUID = 333333;
	// Namespace string, to be used throughout the class
	private final String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        static final String XLINK_NAMESPACE_URI = "http://www.w3.org/1999/xlink";

	// Center coordinates and radius of the most recent ball
	private int radius = 4;
        int shape_type_number = 0;
        private Point2D last_location = null;
        private java.util.List<Point2D> current_drawing_locations = new ArrayList();
        private java.util.List<Point2D> whole_path_locations_list = new ArrayList();
        private boolean drawing_in_progress = false;
        private int drawing_number = 0;
	private JSVGCanvas canvas;
	private Document document;
	private Element selected_shape;
        private Element selected_component;
        private Element selected_drag_point;
        private Element selected_point;
        private Element selected_button_box;
        private Element clipboard_element;
        Element fill_defs = null;
        Color color = new Color(0,0,0);
        private int scl = 10;
        private Point2D mouse_point = null;
        private boolean moveable = false;
        private boolean component_moveable = false;
        private boolean drag_point_moveable = false;
        private boolean selected_point_moveable = false;
        private boolean SE_resizeable = false;
        private Double shape_X;
        private Double shape_Y;
        private Double shape_scale;
        private Point2D pressed_point = null;
        private java.util.List<Element> location_list = new ArrayList();
        private java.util.List<Element> location_coordinates_list = new ArrayList();
        private Element axis_X;
        private Element axis_Y;
        private Point2D mouse_location;
        private Point2D mouse_released;
        private Point2D selected_component_station_point;
        private Point2D selected_shape_station_point;
        private Point2D selected_component_dimensions;
        private SVGGraphics2D generator;
        boolean axises = false;
        Cursor cross_hair = new Cursor(Cursor.CROSSHAIR_CURSOR);
        Cursor dflt_cursor = new Cursor(Cursor.DEFAULT_CURSOR);
        Cursor hand_cursor = new Cursor(Cursor.HAND_CURSOR);
        Cursor move_cursor = new Cursor(Cursor.MOVE_CURSOR);
        String natUri;
        String fillUri;
        String left_pattern_coords = "";
        String right_pattern_coords = "";
        alterStation as;
        ribbonTest rt;
        private boolean ctrl = false;
        private boolean alt = false;
        private boolean shift = false;
        private boolean delete = false;

	public SVGConjurer(Dimension dim, alterStation as) {
		super("SVG Conjurer");
                this.as = as;
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


		// Finally, the document is associated with the canvas
		canvas.setDocument(document);
	}

	public void mouseClicked(MouseEvent e) {
 //           if(!(selected_button_box==null)) clearComponentBounds();
            if(shape_type_number != 0){
                last_location = e.getPoint();
                current_drawing_locations.add(last_location);
                whole_path_locations_list.add(last_location);
                drawLocationCoordinates();
                drawLocation();
                if(current_drawing_locations.size() == shape_type_number){
                    drawSegment();
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
//        locationSet();
        }
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
        public void mouseDragged(MouseEvent e) {
            mouse_point = e.getPoint();
            shape_X = e.getX()- pressed_point.getX();
            shape_Y = e.getY() - pressed_point.getY();
            if(!axises)drawAxises();
            axisAdjust();
            if (moveable){
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
        }
        public void mouseMoved   (MouseEvent e) {
            mouse_location = e.getPoint();
            if(!axises)drawAxises();
            as.setCoordinates(e.getX(), e.getY());
            axisAdjust();
        }

	// Changing the coordinates and the radius of the most recent ball
	public void stateChanged(ChangeEvent e) {
	}

/*        private void drawImage(){
            Runnable r = new Runnable() {
			public void run() {
				Element root = document.getDocumentElement();

                                Element defs = document.createElementNS(svgNS, "defs");
                                root.appendChild(defs);

				Element my_image = document.createElementNS(svgNS, "image");
                                my_image.setAttributeNS(XLINK_NAMESPACE_URI, "xlink:href", natUri);
                                my_image.setAttributeNS(null, "id", "natasha_png");
				my_image.setAttributeNS(null, "x", "0");
                                my_image.setAttributeNS (null, "y", "0");
				my_image.setAttributeNS(null, "width", "0.5in");
				my_image.setAttributeNS(null, "height", "0.5in");

                                Element pat = document.createElementNS(svgNS, "pattern");
                                pat.setAttributeNS(null, "id", "fpatid");
                                pat.setAttributeNS(null, "patternUnits", "userSpaceOnUse");
                                pat.setAttributeNS(null, "width", "0.5in");
                                pat.setAttributeNS(null, "height", "0.5in");
                                pat.setAttributeNS(null, "x", "0.5in");
                                pat.setAttributeNS(null, "y", "0.5in");

                                pat.appendChild(my_image);

				defs.appendChild(pat);
//                                location_list.add(location);
			}
		};
		UpdateManager um = canvas.getUpdateManager();
		um.getUpdateRunnableQueue().invokeLater(r);
        }
*/
        private void drawAxises(){
            axises = true;
            Runnable r = new Runnable() {
			public void run() {
				Element root = document.getDocumentElement();
				axis_X = document.createElementNS(svgNS, "line");
                                axis_X.setAttributeNS(null, "id", "axis_X");
				axis_X.setAttributeNS(null, "stroke", "green");
				axis_X.setAttributeNS(null, "stroke-width", "1");
                                axis_Y = document.createElementNS(svgNS, "line");
                                axis_Y.setAttributeNS(null, "id", "axis_Y");
				axis_Y.setAttributeNS(null, "stroke", "green");
				axis_Y.setAttributeNS(null, "stroke-width", "1");
				root.appendChild(axis_X);
                                root.appendChild(axis_Y);
			}
		};
		UpdateManager um = canvas.getUpdateManager();
		um.getUpdateRunnableQueue().invokeLater(r);
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
				root.appendChild(coord_Y);
                                root.appendChild(coord_X);
                                location_coordinates_list.add(coord_Y);
                                location_coordinates_list.add(coord_X);
			}
		};
		UpdateManager um = canvas.getUpdateManager();
		um.getUpdateRunnableQueue().invokeLater(r);
        }

        private void drawLocation(){
            Runnable r = new Runnable() {
			public void run() {
				Element root = document.getDocumentElement();
				Element location = document.createElementNS(svgNS, "circle");
                                location.setAttributeNS(null, "id", "location");
				location.setAttributeNS(null, "stroke", "darkslateblue");
                                location.setAttributeNS (null, "fill", "lightsteelblue");
				location.setAttributeNS(null, "stroke-width", "1");
				location.setAttributeNS(null, "r", "" + radius);
				location.setAttributeNS(null, "cx", "" + last_location.getX());
				location.setAttributeNS(null, "cy", "" + last_location.getY());
//                                p1("Location :"+last_location);
				root.appendChild(location);
                                location_list.add(location);
			}
		};
		UpdateManager um = canvas.getUpdateManager();
		um.getUpdateRunnableQueue().invokeLater(r);
        }

        private void drawEditPoint(final int x, final int y, final int rad, final String fill_color){
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
            if(shape_type_number == 2) drawLine();
            if(shape_type_number == 4) drawArc();
        }

        private void drawLine(){
            Runnable r = new Runnable() {
			public void run() {
				Element root = document.getDocumentElement();
                                Element current_drawing;
                                if(drawing_in_progress){
                                    current_drawing = document.getElementById("drawing_"+drawing_number);
                                }
                                else{
                                    current_drawing = document.createElementNS(svgNS, "path");
                                    current_drawing.setAttributeNS (null, "id", "drawing_"+drawing_number);
                                    current_drawing.setAttributeNS(null, "pathLength", "100");
                                    current_drawing.setAttributeNS (null, "stroke", "black");
                                    current_drawing.setAttributeNS (null, "stroke-width", "1");
                                    current_drawing.setAttributeNS(null, "fill", "none");
                                    current_drawing.setAttributeNS(null, "d", "M "+current_drawing_locations.get(0).getX()+" "+current_drawing_locations.get(0).getY());
                                    root.appendChild(current_drawing);                                    
                                }
                                current_drawing.setAttributeNS(null, "d", current_drawing.getAttributeNS(null, "d")+" L "+current_drawing_locations.get(1).getX()+" "+current_drawing_locations.get(1).getY());
                                current_drawing_locations.clear();
                                current_drawing_locations.add(last_location);
                                drawing_in_progress = true;
			}
		};

		UpdateManager um = canvas.getUpdateManager();
		um.getUpdateRunnableQueue().invokeLater(r);
        }

        private void drawArc(){
            Runnable r = new Runnable() {
			public void run() {
				Element root = document.getDocumentElement();
                                Element current_drawing;
                                if(drawing_in_progress){
                                    current_drawing = document.getElementById("drawing_"+drawing_number);
                                }
                                else{
                                    current_drawing = document.createElementNS(svgNS, "path");
                                    current_drawing.setAttributeNS (null, "id", "drawing_"+drawing_number);
                                    current_drawing.setAttributeNS(null, "pathLength", "100");
                                    current_drawing.setAttributeNS (null, "stroke", "black");
                                    current_drawing.setAttributeNS (null, "stroke-width", "1");
                                    current_drawing.setAttributeNS(null, "fill", "none");
                                    current_drawing.setAttributeNS(null, "d", "M "+current_drawing_locations.get(0).getX()+" "+current_drawing_locations.get(0).getY());
                                    root.appendChild(current_drawing);                                    
                                }
                                current_drawing.setAttributeNS(null, "d", current_drawing.getAttributeNS(null, "d")+" C"+current_drawing_locations.get(1).getX()+" "+current_drawing_locations.get(1).getY()+" "+current_drawing_locations.get(2).getX()+" "+current_drawing_locations.get(2).getY()+" "+current_drawing_locations.get(3).getX()+" "+current_drawing_locations.get(3).getY());
                                current_drawing_locations.clear();
                                current_drawing_locations.add(last_location);
                                drawing_in_progress = true;
			}
		};

		UpdateManager um = canvas.getUpdateManager();
		um.getUpdateRunnableQueue().invokeLater(r);
        }

        public void completionAgent(){
            Element current_drawing = document.getElementById("drawing_"+drawing_number);
            finishDrawing(current_drawing);
        }

        public void finishDrawing(final Element current_drawing){
            Runnable r = new Runnable(){
              public void run(){                  
                  Element root = document.getDocumentElement();

                  try{
                      current_drawing.setAttributeNS(null, "d", current_drawing.getAttributeNS(null, "d")+" Z");
                  }

                  catch(Exception e){
                      return;
                  }

/*                  SVGLocatableSupport ls = new SVGLocatableSupport();
                  SVGRect rect = ls.getBBox(current_drawing);
                  float x = rect.getX();
                  float y = rect.getY();
                  float width = rect.getWidth();
                  float height = rect.getHeight();
                  root.removeChild(current_drawing);
  */

                  Element svg_element = document.createElementNS(svgNS, "svg");
                  svg_element.setAttribute("id", "svg_"+current_drawing.getAttribute("id"));
                  svg_element.setAttribute("x", ""+0);
                  svg_element.setAttribute("y", ""+0);
//                  svg_element.setAttribute("viewBox", "0 0 100 100");

                  svg_element.appendChild(current_drawing);
                  root.appendChild(svg_element);

                  org.w3c.dom.NodeList nl = document.getElementsByTagName("svg");
                  Element el = (Element)nl.item(1);

//                  current_drawing.setAttribute("fill", "rgb("+color.getRed()+","+color.getGreen()+","+color.getBlue()+")");
                  //current_drawing.setAttribute("fill", "url(#pattern"+drawing_number+")");

//            canvas.setRenderingTransform(at);
                  registerPatternListeners(current_drawing);
                  p1(current_drawing.getAttribute("d"));
                  drawing_number++;
              }
            };
            Element root = document.getDocumentElement();
            for(int i = 0; i < location_list.size(); i++){
                root.removeChild(location_list.get(i));
            }

            for(int i = 0; i < location_coordinates_list.size(); i++){
                root.removeChild(location_coordinates_list.get(i));
            }
            location_list.clear();
            location_coordinates_list.clear();

            UpdateManager um = canvas.getUpdateManager();
	    um.getUpdateRunnableQueue().invokeLater(r);
            drawing_in_progress = false;
            current_drawing_locations.clear();
            whole_path_locations_list.clear();
            last_location = null;            
        }

        public Element setGuideImage(final String image_path, final int width, final int height){
            final Element my_image = document.createElementNS(svgNS, "image");
            Runnable r = new Runnable(){
                public void run(){
                    Element defs = document.createElementNS(svgNS, "defs");
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
                  pattern.setAttributeNS(null, "id", "pattern"+drawing_number);
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
            Element spath = document.getElementById("drawing_"+drawing_number);
            for(int i = 0; i< 8; i++){
                p1("");
            }
            p2("*********************************************************************************");
            p2("The Split path is "+spath.getAttribute("d"));
            p2("SELECTED SHAPE IS "+selected_shape.getAttributeNS(null,"d"));
            p2("*********************************************************************************");
            Runnable r = new Runnable(){
                public void run(){
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
                                                percenter = segment_counter;
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
                                                percenter = segment_counter;
                                                break_point = split_path_ending;
                                                ending_point_on_line = true;
                                                one_point_found = true;

                                                p1("LOG LINE 696: distance = "+distance);
                                                p1("LOG LINE 697: percenter = "+percenter);
                                                p1("LOG LINE 698: break_point = "+break_point);
                                                p1("LOG LINE 699: begining_point_on_line = "+begining_point_on_line);
                                                p1("LOG LINE 700: one_point_found = "+one_point_found);
                                            }
                                            first_point = new Point2D.Double(curve_coords[0], curve_coords[1]);
                                            p1("LOG LINE 703: first_point = "+first_point);
                                        }
                                        segment_counter++;
                                        curve_path_iterator.next();
                                        p1("LOG LINE 707: segment_counter = "+segment_counter);
                                        line_638_loop++;
                                    }
                                    p1("************************* OUT OF THE LINE_638_LOOP *********************************************");

                                    percenter = (percenter/segment_counter)*100;
                                    one_point_found = false;

                                    p1("LOG LINE 715: percenter = "+percenter);
                                    p1("LOG LINE 716: one_point_found = "+one_point_found);

                                    distance = 1.0;
                                    Point2D A = new Point2D.Double(last_point.getX()+((coords[0]-last_point.getX())/100)*percenter, last_point.getY()+((coords[1]-last_point.getY())/100)*percenter);
                                    Point2D B = new Point2D.Double(coords[0]+((coords[2]-coords[0])/100)*percenter, coords[1]+((coords[3]-coords[1])/100)*percenter);
                                    Point2D C = new Point2D.Double(coords[2]+((coords[4]-coords[2])/100)*percenter, coords[3]+((coords[5]-coords[3])/100)*percenter);
                                    Point2D M = new Point2D.Double(A.getX()+((B.getX()-A.getX())/100)*percenter, A.getY()+((B.getY()-A.getY())/100)*percenter);
                                    Point2D N = new Point2D.Double(B.getX()+((C.getX()-B.getX())/100)*percenter, B.getY()+((C.getY()-B.getY())/100)*percenter);
                                    p2("POINTA"+A);
                                    p2("POINTB"+B);
                                    p2("POINTC"+C);
                                    p2("POINTM"+M);
                                    p2("POINTN"+N);

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

                        Element tmp = document.getElementById("drawing_"+drawing_number);
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
                            System.err.println("LOG LINE 773: line_767_loop = "+line_767_loop);
                            int segment = pi.currentSegment(split_path_coords);
                            if(segment == pi.SEG_MOVETO){
                                forward_path = "L "+split_path_coords[0]+" "+split_path_coords[1];
                                backward_path = split_path_coords[0]+" "+split_path_coords[1];

                                p1("LOG LINE 779: forward_path = "+forward_path);
                                p1("LOG LINE 780: backward_path = "+backward_path);
                            }
                            if(segment == pi.SEG_LINETO){
                                forward_path = forward_path+" L "+split_path_coords[0]+" "+split_path_coords[1];
                                backward_path =split_path_coords[0]+" "+split_path_coords[1]+" L "+backward_path;

                                p1("LOG LINE 786: forward_path = "+forward_path);
                                p1("LOG LINE 787: backward_path = "+backward_path);
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

                        Element root = document.getDocumentElement();
                        Element del = document.getElementById("svg_"+selected_shape.getAttribute("id"));
                        root.removeChild(del);

                        root.removeChild(document.getElementById("drawing_"+drawing_number));
                        drawing_number = drawing_number-1;
                        String first_element_name = selected_shape.getAttribute("id");

                        Element first_drawing = document.createElementNS(svgNS, "path");
                        first_drawing.setAttributeNS (null, "id", first_element_name);
                        first_drawing.setAttributeNS(null, "pathLength", "100");
                        first_drawing.setAttributeNS (null, "stroke", "black");
                        first_drawing.setAttributeNS (null, "stroke-width", "1");
                        first_drawing.setAttributeNS(null, "fill", "none");
                        //selected_shape.getParentNode().appendChild(first_drawing);
                        finishDrawing(first_drawing);
                        first_drawing.setAttributeNS(null, "d", element1);
                        drawing_number++;

                        Element second_drawing = document.createElementNS(svgNS, "path");
                        second_drawing.setAttributeNS (null, "id", "drawing_"+drawing_number);
                        second_drawing.setAttributeNS(null, "pathLength", "100");
                        second_drawing.setAttributeNS (null, "stroke", "black");
                        second_drawing.setAttributeNS (null, "stroke-width", "1");
                        second_drawing.setAttributeNS(null, "fill", "none");
                        second_drawing.setAttributeNS(null, "d", element2);
                        finishDrawing(second_drawing);

                        for(int i = 0; i < location_list.size(); i++){
                            root.removeChild(location_list.get(i));
                        }

                        for(int i = 0; i < location_coordinates_list.size(); i++){
                            root.removeChild(location_coordinates_list.get(i));
                        }
                        location_list.clear();
                        location_coordinates_list.clear();
                        drawing_number++;
                        registerPatternListeners(first_drawing);
                        registerPatternListeners(second_drawing);
                        drawing_in_progress = false;
                        current_drawing_locations.clear();
                        whole_path_locations_list.clear();
                        last_location = null;
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

        public void beginEndPointCheck(){

        }

        public boolean lineSplitter(Point2D p1, Point2D p2, Point2D break_point){
            if(Math.abs((p1.distance(break_point)+break_point.distance(p2))-p1.distance(p2))<2){
                return true;
            }
            else return false;
        }

        public void fillPattern(){
            p1(selected_shape.getAttribute("fill"));
            Runnable r = new Runnable(){
                public void run(){

                  Element root = document.getDocumentElement();
                  if(fill_defs == null){
                      fill_defs = document.createElementNS(svgNS, "defs");
                      root.appendChild(fill_defs);
                      p1("Fill defs didnt have children");
                  }

                  if(!selected_shape.getAttribute("fill").equals("none")){
                       p1("in here!!");
                       //document.removeChild(document.getElementById("fill_image_"+selected_shape.getAttribute("id")));
                       //document.removeChild(document.getElementById("pattern_"+selected_shape.getAttribute("id")));
                       selected_shape.setAttribute("fill", "none");
                       fill_defs.removeChild(document.getElementById("pattern_"+selected_shape.getAttribute("id")));
                  }

                  Element fill_image = document.createElementNS(svgNS, "image");
                  fill_image.setAttributeNS(XLINK_NAMESPACE_URI, "xlink:href", fillUri);
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


//                  current_drawing.setAttribute("fill", "rgb("+color.getRed()+","+color.getGreen()+","+color.getBlue()+")");
                  selected_shape.setAttribute("fill", "url(#pattern_"+selected_shape.getAttribute("id")+")");

                }
            };
            UpdateManager um = canvas.getUpdateManager();
	    um.getUpdateRunnableQueue().invokeLater(r);
        }

        public void setButton(final java.net.URI button_path){
            p1("In the setButton");
            Runnable r = new Runnable(){
                public void run(){

                  Element root = document.getDocumentElement();
                  SVGLocatableSupport ls = new SVGLocatableSupport();
                  SVGRect rect = ls.getBBox(selected_shape);
                  float x = rect.getX();
                  float y = rect.getY();
                  float width = rect.getWidth();
                  float height = rect.getHeight();
//                  root.removeChild(selected_shape);

//                  Element svg_element = document.createElementNS(svgNS, "svg");
//                  svg_element.setAttribute("id", "svg_"+selected_shape.getAttribute("id"));
//                  svg_element.setAttribute("x", ""+0);
//                  svg_element.setAttribute("y", ""+0);
//                  svg_element.setAttribute("viewBox", "0 0 100 100");

//                  svg_element.appendChild(selected_shape);
                  Element my_image = document.createElementNS(svgNS, "image");
                  my_image.setAttributeNS(XLINK_NAMESPACE_URI, "xlink:href", button_path.toString());
                  my_image.setAttributeNS(null, "id", "a_button");
                  my_image.setAttributeNS(null, "x", ""+x);
                  my_image.setAttributeNS (null, "y", ""+y);
		  my_image.setAttributeNS(null, "width", ""+width/2);
        	  my_image.setAttributeNS(null, "height", ""+height/2);
                  selected_shape.getParentNode().appendChild(my_image);
//                  selected_shape.setAttribute("viewBox", "0 0 90 90");
//                  root.appendChild(svg_element);
                  registerButtonListeners(my_image);
                  //root.appendChild(selected_shape);
                }
            };
            UpdateManager um = canvas.getUpdateManager();
	    um.getUpdateRunnableQueue().invokeLater(r);
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
                    drawEditPoint((int)coords[0], (int)coords[1], 3, "white");
                }

                else if(segment == pi.SEG_LINETO){
                    drawEditPoint((int)coords[0], (int)coords[1], 3, "white");
                }

                else if(segment == pi.SEG_CUBICTO){
                    drawEditPoint((int)coords[0], (int)coords[1], 3, "chartreuse ");
                    drawEditPoint((int)coords[2], (int)coords[3], 3, "chartreuse ");
                    drawEditPoint((int)coords[4], (int)coords[5], 3, "white");
                }
                pi.next();
            }
        }

        public void editPattern(){

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


        public void registerPatternListeners(Element element){

        EventTarget t1 = (EventTarget) document.getElementById (element.getAttribute("id"));
//        p1("Registering element is "+element.getAttribute("id"));

        t1.addEventListener ("mouseover", new PATTERN_MouseOverAction(), false);
        t1.addEventListener ("mouseout", new PATTERN_MouseOutAction(), false);
        t1.addEventListener ("click", new PATTERN_OnClickAction(), false);
        t1.addEventListener ("mousedown", new PATTERN_MouseDownAction(), false);
        t1.addEventListener ("mouseup", new PATTERN_MouseUpAction(), false);
        t1.addEventListener ("mousemove", new PATTERN_MouseMoveAction(), false);
    }

        public class PATTERN_MouseOverAction implements EventListener {
        public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         SVGLocatableSupport ls = new SVGLocatableSupport();
         SVGRect rect = ls.getBBox(e1);
//         p1("Size "+rect.getWidth()+" "+rect.getHeight());
//         p1("Location "+rect.getX()+" "+rect.getY());
         e1.setAttribute ("stroke", "red");
//         e1.setAttributeNS (null, "stroke-width", "5");
//         p1("Path Length of this element is "+e1.getAttribute("pathLength"));
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
         p1("The selected Element is "+e1.getAttribute("d"));
         selected_shape = e1;
         Element root = document.getDocumentElement();
         root.removeChild(e1.getParentNode());
         root.appendChild(e1.getParentNode());
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
//         componentBounds(x+width/2, y, 2);
 //        componentBounds(x+width, y, 3);
  //       componentBounds(x+width, y+height/2, 4);
         componentBounds(fx+fwidth-2, fy+fheight-2, 5);
         p1("fx+fwidth-2 is "+(fx+fwidth-2));
         p1("fy+fheight-2 is "+(fy+fheight-2));
      //   componentBounds(x+width/2, y+height, 5);
        // componentBounds(x, y+height, 6);
         //componentBounds(x, y+height/2, 7);
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
        //EventTarget t1 = (EventTarget) document.getElementById (element.getAttribute("id"));
        EventTarget t1 = (EventTarget)element;

        t1.addEventListener ("mouseover", new OnMouseOverDragPointAction (), false);
        t1.addEventListener ("mouseout", new OnMouseOutDragPoint (), false);
        t1.addEventListener ("click", new OnDragPointClickAction (), false);
        t1.addEventListener ("mousedown", new OnDragPointMouseDownAction (), false);
        t1.addEventListener ("mouseup", new OnDragPointMouseUpAction (), false);
//        t1.addEventListener ("mousemove", new OnMouseMoveAction (), false);
    }

        public class OnMouseOverDragPointAction implements EventListener {
        public void handleEvent (Event evt) {
            System.out.println("In the drag");
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
//         System.err.println(x+" "+y);
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
                selected_shape.setAttribute("d", left_pattern_coords+" "+mouse_point.getX()+" "+mouse_point.getY()+" "+right_pattern_coords);
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
        Runnable r = new Runnable(){
            public void run(){
                axis_X.setAttribute("x1","0");
                axis_X.setAttribute("y1",""+mouse_location.getY());
                axis_X.setAttribute("x2",""+getWidth());
                axis_X.setAttribute("y2",""+mouse_location.getY());

                axis_Y.setAttribute("x1",""+mouse_location.getX());
                axis_Y.setAttribute("y1","0");
                axis_Y.setAttribute("x2",""+mouse_location.getX());
                axis_Y.setAttribute("y2",""+getHeight());
            }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }

    public void filePrinter(){
        try{
            Element root = document.getDocumentElement();

            FileWriter file = new FileWriter ("The Document.svg");
            PrintWriter writer = new PrintWriter (file);
            SVGDocument svgDoc = canvas.getSVGDocument();
            DOMUtilities.writeDocument (svgDoc, writer);
            writer.close();
        }
        catch(Exception e){
            System.err.println ("IO problem: " + e.toString () );
        }
    }

    public void keyReleased(KeyEvent e) {
//        displayInfo(e, "KEY RELEASED: ");
        //p1("Key Released"+e.getKeyChar());
        if(e.getKeyCode()==17){
            ctrl = false;
        }
        else if(e.getKeyCode()==18){
            alt = false;
        }
        else if(e.getKeyCode()==16){
            shift = false;
        }
    }

    public void keyPressed(KeyEvent e) {
        //p1("Key Pressed"+e.getKeyChar());
        //p1("Key Code is "+e.getKeyCode());
        if(e.getKeyCode()==10){
            clearComponentBounds();
        }
        else if(e.getKeyCode()==17){
            ctrl = true;
        }
        else if(e.getKeyCode()==18){
            alt = true;
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
        System.out.println(s);
    }
    private void p2(String s){
        System.out.println(s);
    }
}
