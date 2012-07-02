package Design_tool_prototype1;

import javax.swing.*;
import javax.swing.undo.*;

import java.util.*;
import java.awt.geom.*;

import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.Element;

public class UndoableReplaceList extends AbstractUndoableEdit{
    SVGConjurer svgc;
    Point2D element;
    Point2D prev_point;
    private ArrayList<Point2D> current_drawing_locations;

    public UndoableReplaceList(SVGConjurer svgc){
        System.out.println("Came into the UndoableReplaceList");
        this.svgc = svgc;
        this.current_drawing_locations = svgc.current_drawing_locations;
        this.prev_point = current_drawing_locations.get(0);
        current_drawing_locations.remove(0);
    }

    public String getPresentationName(){
        return "Presentation name return";
    }

    public void redo() throws CannotRedoException{
        super.redo();
//        System.out.println("In the UndoableAddDrawingLocation");
        current_drawing_locations.remove(0);
    }

    public void undo() throws CannotUndoException{
        super.undo();

        current_drawing_locations.add(0, prev_point);
    }
}