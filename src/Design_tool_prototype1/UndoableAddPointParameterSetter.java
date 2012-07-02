package Design_tool_prototype1;

import javax.swing.*;
import javax.swing.undo.*;

import java.awt.geom.*;
import java.util.*;

public class UndoableAddPointParameterSetter extends AbstractUndoableEdit{
    SVGConjurer svgc;
    private boolean allowAdds = false;
    private Vector addedEdits;
    private Point2D prev_last_location;
    private Point2D prev_mouse_point;
    private Point2D last_location;
    private List<Point2D> current_drawing_locations;
    private List<Point2D> whole_path_locations_list;
    public UndoableAddPointParameterSetter(SVGConjurer svgc){
//        System.out.println("UndoableAddPointParameterSetter called");
        this.svgc = svgc;
        prev_last_location = svgc.last_location;
        prev_mouse_point = svgc.mouse_point;
        svgc.last_location = svgc.mouse_point;
        svgc.current_drawing_locations.add(svgc.last_location);
        svgc.whole_path_locations_list.add(svgc.last_location);
        System.out.println("Came into the UndoableAddPointParameterSetter");
    }

    public String getPresentationName(){
        return "";
    }

    public void redo() throws CannotRedoException{
        super.redo();
        System.out.println("Came into the UndoableAddPointParameterSetter redo");
        svgc.last_location = prev_last_location;
        svgc.mouse_point = prev_mouse_point;
        System.out.println("before: current_drawing_locations.size() is "+(svgc.current_drawing_locations.size()));
        svgc.current_drawing_locations.add(svgc.last_location);
        System.out.println("After: current_drawing_locations.size() is "+(svgc.current_drawing_locations.size()));
        svgc.whole_path_locations_list.add(svgc.last_location);
    }

    public void undo() throws CannotUndoException{
        super.undo();
        System.out.println("Came into the UndoableAddPointParameterSetter undo");
        svgc.last_location = prev_last_location;
        System.out.println("before: current_drawing_locations.size() is "+(svgc.current_drawing_locations.size()));
//        System.out.println("(svgc.whole_path_locations_list.size()-1) is "+(svgc.whole_path_locations_list.size()-1));
        svgc.current_drawing_locations.remove(svgc.current_drawing_locations.size()-1);
        svgc.whole_path_locations_list.remove(svgc.whole_path_locations_list.size()-1);
        System.out.println("After: current_drawing_locations.size() is "+(svgc.current_drawing_locations.size()));
    }

    public boolean addEdit(UndoableEdit anEdit){
        if(allowAdds){
            addedEdits = new Vector();
            addedEdits.addElement(anEdit);
            return true;
        } else
            return false;
    }
}
