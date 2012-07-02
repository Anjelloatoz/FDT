package Design_tool_prototype1;

import javax.swing.*;
import javax.swing.undo.*;
import java.awt.geom.*;

import java.util.*;

import org.w3c.dom.Element;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.bridge.UpdateManager;

public class UndoablePointDrag extends AbstractUndoableEdit{
    SVGConjurer svgc;
    final Element pattern;
    final Element pnt;
    final String prev_pattern_coords;
    final String new_pattern_coords;
    Point2D prev_point;
    
    String new_point_X;
    String new_point_Y;

    JSVGCanvas canvas;

    public UndoablePointDrag(SVGConjurer svgc, Element element, Element point, String prev_pat, String point_prev_x, String point_prev_y){
        this.svgc = svgc;
        this.pattern = element;
        this.pnt = point;
        this.prev_pattern_coords = prev_pat;
        this.new_pattern_coords = pattern.getAttribute("d");
        this.canvas = svgc.canvas;

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

        Runnable r = new Runnable(){
          public void run(){

          }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }

    public void undo() throws CannotUndoException{
        super.undo();

        Runnable r = new Runnable(){
          public void run(){

          }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }
}
