package Design_tool_prototype1;

import org.w3c.dom.Element;
import org.apache.batik.dom.svg.SVGLocatableSupport;
import org.w3c.dom.svg.SVGRect;
import org.w3c.dom.svg.SVGMatrix;
import org.w3c.dom.Document;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.Event;
import java.awt.geom.AffineTransform;
import java.awt.Point;
import java.awt.Rectangle;
import org.apache.batik.bridge.UpdateManager;
import org.apache.batik.swing.JSVGCanvas;
import java.awt.geom.Line2D;

public class resizeHandler{
    Rectangle bounding_box;
    Element visible_box;
    int bounding_box_width;
    int bounding_box_height;
    Point bounding_box_centre;
    JSVGCanvas canvas;
    String rotation="";

    int new_x;
    int new_y;


    Element left_top_box;
    Element left_mid_box;
    Element left_bottom_box;
    Element right_top_box;
    Element right_mid_box;
    Element right_bottom_box;
    Element middle_bottom_box;
    Element resizer_holder;

    Document owner_document;
    Element box_holder;
    Element subject;
    Element subject_container;
    String svgNS;
    double drag_points_orig_x = 0;
    double drag_points_orig_y = 0;
    float CosA;
    float SinA;
    float C;
    float D;
    float E;
    float F;
    boolean draggable = false;
    SVGRect tmp_rect;
    SVGConjurer svgc;
    Element dragging_element;
    AffineTransform reverse = null;
    int rotation_angle = 0;
    double x_scaling = 0;
    double y_scaling = 0;

    resizeHandler(SVGConjurer svgc, Document owner_document, Element box_holder, Element subject, Element subject_container, String svgNS, JSVGCanvas canvas){
        this.owner_document = owner_document;
        this.canvas = canvas;
        this.box_holder = box_holder;
        this.subject = subject;
        this.subject_container = subject_container;
        this.svgNS = svgNS;
        this.svgc = svgc;
    }

    public void tester(Document owner_document, Element box_holder, Element subject, Element subjectContainer, String svgNS, JSVGCanvas canvas){
        tmp_rect = SVGLocatableSupport.getBBox(subject);
        SVGRect test_rect = svgc.BoundryFinder4Resize((Element)subject.getParentNode().getParentNode(), null);

//        System.out.println("tmp_rect width: "+tmp_rect.getWidth());
//        System.out.println("test_rect width: "+test_rect.getWidth());
//        tmp_rect = test_rect;

        this.canvas = canvas;
        bounding_box = new Rectangle();
        bounding_box.setBounds((int)tmp_rect.getX(), (int)tmp_rect.getY(), (int)tmp_rect.getWidth(), (int)tmp_rect.getHeight());
        SVGMatrix matrix = SVGLocatableSupport.getScreenCTM(subject);
        double x_shift = Double.parseDouble(((Element)(subject.getParentNode().getParentNode())).getAttribute("x"));
        double y_shift = Double.parseDouble(((Element)(subject.getParentNode().getParentNode())).getAttribute("y"));

        double rotation_angl = Math.toDegrees(Math.asin(matrix.getB()));
        x_scaling = matrix.getA()/Math.cos(rotation_angl);
        y_scaling = matrix.getD()/Math.cos(rotation_angl);
        bounding_box.setBounds((int)(bounding_box.getX()+x_shift), (int)(bounding_box.getY()+y_shift), (int)(bounding_box.getWidth()*x_scaling), (int)(bounding_box.getHeight()*y_scaling));
        AffineTransform af = new AffineTransform(matrix.getA(), matrix.getB(), matrix.getC(), matrix.getD(), matrix.getE(), matrix.getF());

        try{
            reverse = af.createInverse();
        }
        catch(Exception ex){
            System.out.println("AffineTransformation exception.");
        }

        CosA = matrix.getA();
        SinA = matrix.getB();
        C = matrix.getC();
        D = matrix.getD();
        E = matrix.getE();
        F = matrix.getF();

        visible_box = owner_document.createElementNS(svgNS, "rect");
        visible_box.setAttribute("id", "resize_box");
        visible_box.setAttribute("x", ""+bounding_box.getX());
        visible_box.setAttribute("y", ""+bounding_box.getY());
        visible_box.setAttribute("width", ""+bounding_box.getWidth());
        visible_box.setAttribute("height", ""+bounding_box.getHeight());
        visible_box.setAttribute("stroke", "#00cc00");
        visible_box.setAttribute("stroke-width", ""+1);
        visible_box.setAttribute("stroke-dasharray", "9, 5");
        visible_box.setAttribute("fill", "none");

        left_top_box = createDragBox("left_top_box", owner_document, svgNS, (int)bounding_box.getX()-2, (int)bounding_box.getY()-2);
        left_mid_box = createDragBox("left_mid_box", owner_document, svgNS, (int)bounding_box.getX()-2, (int)bounding_box.getY()+((int)bounding_box.getHeight()/2)-2);
        left_bottom_box = createDragBox("left_bottom_box", owner_document, svgNS, (int)bounding_box.getX()-2, (int)bounding_box.getY()+((int)bounding_box.getHeight())-2);
        right_top_box = createDragBox("right_top_box", owner_document, svgNS, (int)bounding_box.getX()+(int)bounding_box.getWidth()-2, (int)bounding_box.getY()-2);
        right_mid_box = createDragBox("right_mid_box", owner_document, svgNS, (int)bounding_box.getX()+(int)bounding_box.getWidth()-2, (int)bounding_box.getY()+((int)bounding_box.getHeight()/2)-2);
        right_bottom_box = createDragBox("right_bottom_box", owner_document, svgNS, (int)bounding_box.getX()+(int)bounding_box.getWidth()-2, (int)bounding_box.getY()+(int)bounding_box.getHeight()-2);
        middle_bottom_box = createDragBox("middle_bottom_box", owner_document, svgNS, (int)bounding_box.getX()+((int)bounding_box.getWidth()/2)-2, (int)bounding_box.getY()+(int)bounding_box.getHeight()-2);

        resizer_holder = owner_document.createElementNS(svgNS, "g");
        resizer_holder.setAttribute("id", "resizer_holder");
        resizer_holder.appendChild(visible_box);
        resizer_holder.appendChild(left_top_box);
        resizer_holder.appendChild(left_mid_box);
        resizer_holder.appendChild(left_bottom_box);
        resizer_holder.appendChild(right_top_box);
        resizer_holder.appendChild(right_mid_box);
        resizer_holder.appendChild(right_bottom_box);
        resizer_holder.appendChild(middle_bottom_box);

        resizer_holder.setAttribute("transform", "rotate("+rotation_angl+")");
        owner_document.getDocumentElement().appendChild(resizer_holder);
    }

    private Element createDragBox(String name, Document owner_document, String svgNS, int x, int y){
        Element box = owner_document.createElementNS(svgNS, "rect");
        box.setAttribute("id", name);
        box.setAttribute("x", ""+x);
        box.setAttribute("y", ""+y);
        box.setAttribute("width", ""+4);
        box.setAttribute("height", ""+4);
        box.setAttribute("stroke", "black");
        box.setAttribute("stroke-width", ""+1);
        box.setAttribute("fill", "brown");

        registerListeners(box);
        return box;
    }

    public void registerListeners(Element element){
        EventTarget et = (EventTarget)element;

        et.addEventListener ("mouseover", new MouseOverAction(), false);
        et.addEventListener ("mouseout", new MouseOutAction(), false);
        et.addEventListener ("click", new ClickAction(), false);
        et.addEventListener ("mousedown", new MouseDownAction(), false);
        et.addEventListener ("mouseup", new MouseUpAction(), false);
    }

    public class MouseOverAction implements EventListener {
        public void handleEvent (Event evt){
            EventTarget tt = evt.getTarget();
            Element e1 = (Element)tt;
        }
    }

    public class MouseOutAction implements EventListener {
        public void handleEvent (Event evt){
            EventTarget tt = evt.getTarget();
            Element e1 = (Element)tt;
        }
    }

    public class ClickAction implements EventListener {
        public void handleEvent (Event evt){
            EventTarget tt = evt.getTarget();
            Element e1 = (Element)tt;
        }
    }

    public class MouseDownAction implements EventListener {
        public void handleEvent (Event evt){
            EventTarget tt = evt.getTarget();
            Element e1 = (Element)tt;
            bounding_box_width = (int)bounding_box.getWidth();
            bounding_box_height = (int)bounding_box.getHeight();
            bounding_box_centre = new Point((int)bounding_box.getX()+bounding_box_width/2, (int)bounding_box.getY()+bounding_box_height/2);
            drag_points_orig_x = Double.parseDouble(e1.getAttribute("x"));
            drag_points_orig_y = Double.parseDouble(e1.getAttribute("y"));
            dragging_element = e1;
            draggable = true;
        }
    }

    public class MouseUpAction implements EventListener {
        public void handleEvent (Event evt){
            EventTarget tt = evt.getTarget();
            Element e1 = (Element)tt;
            draggable = false;
            dragging_element = null;
        }
    }

    public void drag(int x, int y ){
        if(dragging_element == null) return;
        Point mouse_point = new Point(x,y);
//        reverse.transform(new Point(x,y), mouse_point);
        new_x = mouse_point.x-(int)drag_points_orig_x;
        new_y = mouse_point.y-(int)drag_points_orig_y;
        if(dragging_element.getAttribute("id").contains("right_mid")){
            bounding_box.setSize(bounding_box_width+new_x, bounding_box_height);
            resetAll();
        }
        else if(dragging_element.getAttribute("id").contains("right_bottom")){
            bounding_box.setSize(bounding_box_width+(new_x+new_y)/2, bounding_box_height+(new_x+new_y)/2);
            resetAll();
        }
        else if(dragging_element.getAttribute("id").contains("left_top")){
            Line2D radius = new Line2D.Double(bounding_box_centre, new Point((int)drag_points_orig_x+2, (int)drag_points_orig_y+2));
            double distance = (int)radius.ptLineDist(mouse_point)*(int)radius.relativeCCW(mouse_point);
            double length = (int)bounding_box_centre.distance(mouse_point);
            rotation = "rotate("+(int)Math.toDegrees(Math.asin(-distance/length))+" "+(int)bounding_box_centre.getX()+" "+(int)bounding_box_centre.getY()+")";
            resizer_holder.setAttribute("transform", "rotate("+(int)Math.toDegrees(Math.asin(-distance/length))+" "+(int)bounding_box_centre.getX()+" "+(int)bounding_box_centre.getY()+")");
            resetAll();
        }
    }
    private void resetAll(){
        Runnable r = new Runnable(){
            public void run(){
                double x_ratio = bounding_box.getWidth()/tmp_rect.getWidth();
                double y_ratio = bounding_box.getHeight()/tmp_rect.getHeight();
                double translate_X = -(tmp_rect.getX()-(tmp_rect.getX()/x_ratio));
                double translate_Y = -(tmp_rect.getY()-(tmp_rect.getY()/y_ratio));
                ((Element)subject.getParentNode()).setAttribute("transform", "scale("+x_ratio+" "+y_ratio+") translate("+translate_X+" "+translate_Y+") "+rotation);
                subject.setAttribute("stroke-width", ""+1/((x_ratio+y_ratio)/2));
                visible_box.setAttribute("x", ""+bounding_box.getX());
                visible_box.setAttribute("y", ""+bounding_box.getY());
                visible_box.setAttribute("width", ""+bounding_box.getWidth());
                visible_box.setAttribute("height", ""+bounding_box.getHeight());
                left_top_box.setAttribute("x", ""+((int)bounding_box.getX()-2));
                left_top_box.setAttribute("y", ""+((int)bounding_box.getY()-2));
                left_mid_box.setAttribute("x", ""+((int)bounding_box.getX()-2));
                left_mid_box.setAttribute("y", ""+((int)bounding_box.getY()+((int)bounding_box.getHeight()/2)-2));
                left_bottom_box.setAttribute("x", ""+((int)bounding_box.getX()-2));
                left_bottom_box.setAttribute("y", ""+((int)bounding_box.getY()+((int)bounding_box.getHeight())-2));
                right_top_box.setAttribute("x", ""+((int)bounding_box.getX()+(int)bounding_box.getWidth()-2));
                right_top_box.setAttribute("y", ""+((int)bounding_box.getY()-2));
                right_mid_box.setAttribute("x", ""+((int)bounding_box.getX()+(int)bounding_box.getWidth()-2));
                right_mid_box.setAttribute("y", ""+((int)bounding_box.getY()+((int)bounding_box.getHeight()/2)-2));
                right_bottom_box.setAttribute("x", ""+((int)bounding_box.getX()+(int)bounding_box.getWidth()-2));
                right_bottom_box.setAttribute("y", ""+((int)bounding_box.getY()+(int)bounding_box.getHeight()-2));
                middle_bottom_box.setAttribute("x", ""+((int)bounding_box.getX()+((int)bounding_box.getWidth()/2)-2));
                middle_bottom_box.setAttribute("y", ""+((int)bounding_box.getY()+(int)bounding_box.getHeight()-2));
            }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }
}
