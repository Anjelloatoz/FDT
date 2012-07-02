package Design_tool_prototype1;

import javax.swing.undo.*;

import java.util.*;
import java.awt.geom.*;

public class UndoableAddPathLocation extends AbstractUndoableEdit{
    SVGConjurer svgc;
    Point2D element;
    private ArrayList<Point2D> whole_path_locations_list;

    public UndoableAddPathLocation(SVGConjurer svgc, Point2D last_location){
        this.svgc = svgc;
        this.whole_path_locations_list = svgc.whole_path_locations_list;
        this.element = last_location;
        whole_path_locations_list.add(element);
//        System.out.println("Came into the UndoableAddPathLocation");
    }

    @Override
    public String getPresentationName(){
        return "Presentation name return";
    }

    @Override
    public void redo() throws CannotRedoException{
        super.redo();
        whole_path_locations_list.add(element);
    }

    @Override
    public void undo() throws CannotUndoException{
        super.undo();
  //      System.out.println("    UNDO-UndoableAddPathLocation");
        whole_path_locations_list.remove(element);
  //      System.out.println("current_drawing_locations: "+svgc.current_drawing_locations.size());
    }
}
