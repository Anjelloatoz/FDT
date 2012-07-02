package Design_tool_prototype1;

import javax.swing.*;
import javax.swing.undo.*;

import java.util.*;

import org.w3c.dom.Element;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.bridge.UpdateManager;

public class UndoableAddLocationCoordinates extends AbstractUndoableEdit{
    private String location_name = "";
    private boolean allowAdds = false;
    private Vector addedEdits;
    private SVGConjurer svgc;
    private final Element coord_x;
    private final Element coord_y;
    private Element axis_X;
    private Element axis_Y;
    java.util.List<Element> location_coordinates_list;
    JSVGCanvas canvas;
    public UndoableAddLocationCoordinates(SVGConjurer svgc, Element coord_X, Element coord_Y){
        location_name = "";
        this.svgc = svgc;
        this.coord_x = coord_X;
        this.coord_y = coord_Y;
        this.axis_X = svgc.axis_X;
        this.axis_Y = svgc.axis_Y;
        this.location_coordinates_list = svgc.location_coordinates_list;
        this.canvas = svgc.canvas;
        System.out.println("Came into the UndoableAddLocationCoordinates");

        Runnable r = new Runnable(){
            public void run(){                
                axis_X.getParentNode().insertBefore(coord_x, axis_X);
                axis_Y.getParentNode().insertBefore(coord_y, axis_Y);

                location_coordinates_list.add(coord_x);
                location_coordinates_list.add(coord_y);
            }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }

    public String getPresentationName(){
        return location_name;
    }

    public void redo() throws CannotRedoException{
        super.redo();

        Runnable r = new Runnable(){
            public void run(){
                axis_X.getParentNode().insertBefore(coord_x, axis_X);
                axis_Y.getParentNode().insertBefore(coord_y, axis_Y);

                location_coordinates_list.add(coord_x);
                location_coordinates_list.add(coord_y);
            }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }

    public void undo() throws CannotUndoException{
        super.undo();
        System.out.println("    UNDO-UndoableAddLocationCoordinates");
        Runnable r = new Runnable(){
            public void run(){
                axis_X.getParentNode().removeChild(coord_x);
                axis_Y.getParentNode().removeChild(coord_y);

                location_coordinates_list.remove(coord_x);
                location_coordinates_list.remove(coord_y);
            }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
        System.out.println("current_drawing_locations: "+svgc.current_drawing_locations.size());
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
