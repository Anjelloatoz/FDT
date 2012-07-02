package Design_tool_prototype1;

import javax.swing.*;
import javax.swing.undo.*;

import java.util.*;
import java.awt.geom.*;

import org.w3c.dom.Element;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.bridge.UpdateManager;
import org.w3c.dom.Document;

public class UndoableAddLine extends AbstractUndoableEdit{
    private String location_name = "";
    private SVGConjurer svgc;
    private final Element Line;
    private JSVGCanvas canvas;
    private java.util.List<Point2D> current_drawing_locations;
    private Point2D last_location;
    private boolean drawing_in_progress;
    private Document document;
    private final java.util.List<Point2D> prev_current_drawing_locations;
    private final boolean prev_drawing_in_progress;

    public UndoableAddLine(SVGConjurer svgc, Element line){
        this.svgc = svgc;
        this.Line = line;
        this.current_drawing_locations = svgc.current_drawing_locations;
        this.prev_current_drawing_locations = svgc.current_drawing_locations;
        this.last_location = svgc.last_location;
        this.drawing_in_progress = svgc.drawing_in_progress;
        this.prev_drawing_in_progress = svgc.drawing_in_progress;
        this.document = svgc.document;
        this.canvas = svgc.canvas;

        Runnable r = new Runnable(){
            public void run(){
                try{
                Element root = document.getDocumentElement();
                root.appendChild(Line);
                current_drawing_locations.clear();
                current_drawing_locations.add(last_location);
                drawing_in_progress = true;
                }
                catch(Exception e){
                    System.out.println("Error is "+e);
                }
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
        System.out.println("Came into the AddLine redo");
        Runnable r = new Runnable(){
            public void run(){
                Element root = document.getDocumentElement();
                root.appendChild(Line);
                current_drawing_locations.clear();
                current_drawing_locations.add(last_location);
                drawing_in_progress = true;
            }
        };
        UpdateManager um = canvas.getUpdateManager();
        um.getUpdateRunnableQueue().invokeLater(r);
    }

    public void undo() throws CannotUndoException{
        super.undo();
        System.out.println("Came into the AddLine undo");
        Runnable r = new Runnable(){
            public void run(){
                Element root = document.getDocumentElement();
                root.removeChild(Line);
                current_drawing_locations = prev_current_drawing_locations;
                drawing_in_progress = prev_drawing_in_progress;
            }
        };
        UpdateManager um = canvas.getUpdateManager();
        um.getUpdateRunnableQueue().invokeLater(r);
    }
}
