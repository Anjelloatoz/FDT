package Design_tool_prototype1;

import javax.swing.*;
import javax.swing.undo.*;

import java.util.*;
import java.awt.geom.*;

import org.w3c.dom.Element;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.bridge.UpdateManager;

public class UndoableClearLocation extends AbstractUndoableEdit{
    SVGConjurer svgc;
    JSVGCanvas canvas;
    Point2D location;
    Point2D foreign_location;

    public UndoableClearLocation(Point2D location){
        this.foreign_location = location;
        this.location = (Point2D)location.clone();
        foreign_location = null;
        System.out.println("Came into the UndoableEdit");
    }

    public String getPresentationName(){
        return "Presentation name return";
    }

    public void redo() throws CannotRedoException{
        super.redo();
        foreign_location = null;
        System.out.println("Came into the UndoableClearLocation redo");
    }

    public void undo() throws CannotUndoException{
        super.undo();
        System.out.println("Came into the UndoableClearLocation undo");
        foreign_location = location;
    }
}
