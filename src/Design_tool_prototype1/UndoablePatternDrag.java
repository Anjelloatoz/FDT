package Design_tool_prototype1;

import javax.swing.*;
import javax.swing.undo.*;

import java.util.*;
import java.awt.geom.*;

import org.w3c.dom.Element;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.bridge.UpdateManager;

public class UndoablePatternDrag extends AbstractUndoableEdit{
    SVGConjurer svgc;
    JSVGCanvas canvas;
    Element subject_element;
    Element subject_point;
    String before;
    String after;
    Point2D beforeP;
    Point2D afterP;

    public UndoablePatternDrag(SVGConjurer svgc, Point2D before_drag_point, Point2D after_drag_point, String before_drag_pattern, String after_drag_pattern, Element element, Element point){
        this.svgc = svgc;
        this.canvas = svgc.canvas;
        this.subject_element = element;
        this.subject_point = point;
        System.out.println("In the UndoablePatternDrag subject_point:"+subject_point.getAttribute("id"));
        this.before = before_drag_pattern;
        this.after = after_drag_pattern;
        this.beforeP = before_drag_point;
        this.afterP = after_drag_point;
        System.out.println("Came into the UndoablePatternDrag");

        Runnable r = new Runnable(){
          public void run(){

          }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }

    public String getPresentationName(){
        return "Presentation name return";
    }

    public void redo() throws CannotRedoException{
        super.redo();
        System.out.println("Came into the UndoablePAtternDrag redo");

        Runnable r = new Runnable(){
          public void run(){
              subject_point.setAttribute("cx", afterP.getX()+"");
              subject_point.setAttribute("cy", afterP.getY()+"");
              subject_element.setAttribute("d", after);
          }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }

    public void undo() throws CannotUndoException{
        super.undo();
        System.out.println("Came into the UndoablePatternDrag undo");

        Runnable r = new Runnable(){
          public void run(){
              subject_point.setAttribute("cx", beforeP.getX()+"");
              subject_point.setAttribute("cy", beforeP.getY()+"");
              subject_element.setAttribute("d", before);
          }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }
}
