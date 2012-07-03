package Design_tool_prototype1;

import javax.swing.*;
import javax.swing.undo.*;

import java.util.*;
import java.awt.geom.*;

import org.apache.batik.swing.JSVGCanvas;

public class UndoableClearDrawingLocations extends AbstractUndoableEdit{
    SVGConjurer svgc;
    JSVGCanvas canvas;
    private ArrayList<Point2D> current_drawing_locations;
    private ArrayList<Point2D> prev_current_drawing_locations;

    public UndoableClearDrawingLocations(SVGConjurer svgc){
        this.svgc = svgc;
        this.canvas = svgc.canvas;
        this.current_drawing_locations = svgc.current_drawing_locations;
        this.prev_current_drawing_locations = (ArrayList)current_drawing_locations.clone();
        current_drawing_locations.clear();
        System.out.println("****: "+prev_current_drawing_locations.size());
        System.out.println("Came into the UndoableClearDrawingLocations");
        System.out.println("3. current_drawing_locations: "+current_drawing_locations.size());
    }

    public String getPresentationName(){
        return "Presentation name return";
    }

    public void redo() throws CannotRedoException{
        super.redo();
        System.out.println("Came into the UndoableClearDrawingLocations redo");
        System.out.println("before current_drawing_locations: "+current_drawing_locations.size());
        current_drawing_locations.clear();

        System.out.println("after current_drawing_locations: "+current_drawing_locations.size());
    }

    public void undo() throws CannotUndoException{
        super.undo();
        System.out.println("    UNDO-UndoableClearDrwingLocations");
//        System.out.println("before current_drawing_locations: "+current_drawing_locations.size());
        current_drawing_locations = prev_current_drawing_locations;
        
        System.out.println("3. current_drawing_locations: "+current_drawing_locations.size());
//        System.out.println("after current_drawing_locations: "+current_drawing_locations.size());
        svgc.printlocations("length: "+current_drawing_locations.size());
    }
}
