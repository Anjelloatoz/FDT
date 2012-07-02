package Design_tool_prototype1;

import javax.swing.undo.*;

import java.util.*;
import java.awt.geom.*;

public class UndoableAddDrawingLocation extends AbstractUndoableEdit{
    SVGConjurer svgc;
    Point2D element;
    private ArrayList<Point2D> current_drawing_locations;

    public UndoableAddDrawingLocation(SVGConjurer svgc, Point2D last_location){
//        System.out.println("Came into the UndoableAddDrawingLocation");
        this.svgc = svgc;
        this.current_drawing_locations = svgc.current_drawing_locations;
        this.element = last_location;
        current_drawing_locations.add(element);
    }

    @Override
    public String getPresentationName(){
        return "Presentation name return";
    }

    @Override
    public void redo() throws CannotRedoException{
        super.redo();
//        System.out.println("In the UndoableAddDrawingLocation");
        current_drawing_locations.add(element);
    }

    @Override
    public void undo() throws CannotUndoException{
        super.undo();
//        System.out.println("    UNDO-UndoableAddDrawingLocation");
        SVGConjurer svg = new SVGConjurer();
        current_drawing_locations.remove(element);
//        System.out.println("    current_drawing_locations: "+current_drawing_locations.size());
    }
}