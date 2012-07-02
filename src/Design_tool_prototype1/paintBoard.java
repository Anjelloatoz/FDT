package Design_tool_prototype1;

import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.util.XMLConstants;
import org.w3c.dom.svg.SVGDocument;
import java.util.*;

import java.awt.geom.Point2D;
import java.awt.Color;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.svg.SVGLoadEventDispatcherAdapter;
import org.apache.batik.swing.svg.SVGLoadEventDispatcherEvent;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.apache.batik.script.Window;
import org.w3c.dom.Document;

public class paintBoard extends JFrame {

    private final String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
    private SVGGraphics2D generator;
    private JSVGCanvas canvas = new JSVGCanvas ();
    private Window window;
    private Document doc;
    private java.util.List segment = new ArrayList();
    private java.util.List<Point2D> points = new ArrayList();
    int shape_num = 0;
    private Point2D last_point = null;
    int drawnum = 0;
    private java.util.List<List> segments = new ArrayList();
    Point2D firstpoint = null;
    String shape_string = "";
    Color color = new Color(0,0,0);
    private Element selected_element;
    alterStation alt;
    navigator nv;
    Element root;
    boolean line = false;
            Element path0;

    public paintBoard(){

    canvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
    canvas.addSVGLoadEventDispatcherListener(
			new SVGLoadEventDispatcherAdapter(){
				public void svgLoadEventDispatchStarted(SVGLoadEventDispatcherEvent e){
					window = canvas.getUpdateManager().getScriptingEnvironment().createWindow();
				}
			}
		);

    DOMImplementation dom = SVGDOMImplementation.getDOMImplementation ();
    doc = dom.createDocument(svgNS, "svg", null);
    canvas.setDocument(doc);
    root = doc.getDocumentElement ();

    MouseListener ml = new MouseListener(){
		public void mouseClicked(MouseEvent arg0) {
                    if(shape_num == 0){}
                    else{
                        drawPoint(arg0.getX(),arg0.getY());
                        if(points.size()==0) firstpoint = arg0.getPoint();
                        points.add(arg0.getPoint());

                    if(points.size() == shape_num){

                        drawShape();
                    }
                    }
                    return;
		}

		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
    };

    canvas.addMouseListener(ml);

    this.setContentPane (canvas);
//    generator = new SVGGraphics2D(doc);
    }

    private void drawPoint(int x, int y){

        Element location = doc.createElementNS(svgNS, "circle");
        location.setAttributeNS(null, "id", "location"+drawnum);
        location.setAttributeNS (null, "fill", "lightsteelblue");
        location.setAttributeNS (null, "stroke", "darkslateblue");

        location.setAttributeNS (null, "r", "4");
        location.setAttributeNS (null, "cx", ""+x);
        location.setAttributeNS (null, "cy", ""+y);
        root.appendChild(location);
        canvas.setDocument(doc);
}
    public void drawShape(){
        if(shape_num == 2) drawLine();
        if(shape_num == 4) drawArc();
    }

    public void drawLine(){

        System.out.println("in the line "+shape_string);

        if (shape_string.equals("")){
            System.out.println("Draw Line if clause");
            System.out.print(" drawnum :"+drawnum);
            System.out.print(" shape_string :"+shape_string);
            root = doc.getDocumentElement();
            path0 = doc.createElementNS(svgNS, "path");
            path0.setAttributeNS(null, "id", "path"+drawnum);
            shape_string = shape_string+"M"+points.get(0).getX()+" "+points.get(0).getY();
            //root.appendChild(path0);
        }

        else{
            System.out.println("Draw Line else clause");
            System.out.print(" drawnum :"+drawnum);
            System.out.print(" shape_string :"+shape_string);
            Element ed = doc.getElementById("path"+drawnum);
            path0 = doc.getElementById("path"+drawnum);
            shape_string = ""+path0.getAttributeNS(null, "d");
            root.removeChild(ed);
            canvas.setDocument(doc);
        }

        if(!line)shape_string = shape_string+" L";

        shape_string = shape_string+" "+points.get(1).getX()+" "+points.get(1).getY();
        System.out.println("before setting attribute: "+shape_string);
        path0.setAttributeNS(null, "d", shape_string);
        System.out.println("path set in the line.");

        path0.setAttributeNS (null, "stroke", "black");
        path0.setAttributeNS (null, "stroke-width", "1");
        path0.setAttributeNS(null, "fill", "none");
        root.appendChild(path0);
        last_point = points.get(points.size()-1);
        points.clear();
        points.add(last_point);
        line = true;
}

    public void drawArc(){
        System.out.println("Arc called");

        if(points.size()<4){
        for(int i = points.size(); i < 3; i++){
            points.add(i, last_point);
        }
        points.add(3, firstpoint);
        }
//        Element defs = doc.createElementNS(svgNS, "defs");
//        root.appendChild(defs);
        Element path0;

        if (shape_string.equals("")){
            root = doc.getDocumentElement();
            path0 = doc.createElementNS(svgNS, "path");
            path0.setAttributeNS(null, "id", "path"+drawnum);
            shape_string = shape_string+" M "+points.get(0).getX()+" "+points.get(0).getY()+" C";
        }
        else{
            System.out.println("Now In here");
            path0 = doc.getElementById("path"+drawnum);
            shape_string = ""+path0.getAttributeNS(null, "d");
        }

        if(line)shape_string = shape_string+" C";

//        path0.setAttributeNS(null, "d", "M 50 50 L 70 80 Q 100 40 70 30");
        path0.setAttributeNS(null, "d", shape_string+" "+points.get(1).getX()+" "+points.get(1).getY()+" "+points.get(2).getX()+" "+points.get(2).getY()+" "+points.get(3).getX()+" "+points.get(3).getY());

        path0.setAttributeNS (null, "stroke", "black");
        path0.setAttributeNS (null, "stroke-width", "1");
        path0.setAttributeNS(null, "fill", "none");
//        root.appendChild(path0);

//        Element use = doc.createElementNS(svgNS, "use");
//        use.setAttributeNS(XMLConstants.XLINK_NAMESPACE_URI, "href", "#path"+drawnum);

//        root.appendChild(use);
//        defs.appendChild(path0);
        last_point = points.get(points.size()-1);
        points.clear();
        points.add(last_point);
        line = false;
}

    public void finishDrawing(){
        //points.add(firstpoint);
        //drawShape();
        points.clear();

        org.w3c.dom.NodeList nl = root.getElementsByTagName("circle");
        System.out.println("have got "+nl.getLength());

        for(int i = 0; i < nl.getLength(); i++){
            root.removeChild(nl.item(i));
        }

        shape_string = "";
        Element el = doc.getElementById("path"+drawnum);
        System.out.println(el.getAttribute("d"));
        el.setAttribute("d", el.getAttribute("d")+" Z");
//        el.setAttributeNS(null, "d", "M 100 290 A 150 150 0 0 1 400 290 Z");


        System.out.println(el.getAttribute("d"));
        System.out.println("in the finish method path length is "+el.getAttribute("pathLength"));
        el.setAttribute("fill", "rgb("+color.getRed()+","+color.getGreen()+","+color.getBlue()+")");
        //el.setAttribute("transform", "scale("+50+")");
        canvas.setDocument(doc);
//        root.appendChild(el);
        System.out.println("The parent is "+el.getParentNode());
        registerListeners(el);
        nv.setElement(el);
        drawnum++;
    }

    public void registerListeners(Element element){
        EventTarget t1 = (EventTarget) doc.getElementById (element.getAttribute("id"));
        System.out.println("in the register");
        System.out.println(element.getAttribute("id"));
        t1.addEventListener ("mouseover", new OnMouseOverCircleAction (), false);
        t1.addEventListener ("mouseout", new mouseOut (), false);
        t1.addEventListener ("click", new OnClickAction (), false);

    }

    public class OnMouseOverCircleAction implements EventListener {
     public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         System.out.println("Event target is "+e1.getAttribute("id"));
         System.out.println("in the events");
         Element elt = doc.getElementById ("path0");
         e1.setAttribute ("stroke", "yellow");
         e1.setAttributeNS (null, "stroke-width", "5");
         e1.setAttribute("transform", "scale("+50+")");
        }
      }

    public class mouseOut implements EventListener {
     public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         e1.setAttribute ("stroke", "black");
         e1.setAttributeNS (null, "stroke-width", "1");
        }
      }

    public class OnClickAction implements EventListener {
     public void handleEvent (Event evt) {
         EventTarget tt = evt.getTarget();
         Element e1 = (Element)tt;
         System.out.println("path length :"+e1.getAttribute("pathLength"));
         e1.setAttribute("d", "blue");
         e1.setAttribute("pathLength","200");
         selected_element = e1;
         //alt.setElement(e1);
        }
      }



    public JSVGCanvas getBoard(){
        return canvas;
    }
}