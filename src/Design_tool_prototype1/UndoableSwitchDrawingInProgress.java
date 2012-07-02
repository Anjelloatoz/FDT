package Design_tool_prototype1;

import javax.swing.*;
import javax.swing.undo.*;

import java.util.*;
import java.awt.geom.*;

import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.Element;

public class UndoableSwitchDrawingInProgress extends AbstractUndoableEdit{
    SVGConjurer svgc;
    boolean drawing_in_progress;
    boolean new_drawing_in_progress;

    public UndoableSwitchDrawingInProgress(SVGConjurer svgc, boolean drawing_in_progress){
        this.svgc = svgc;
        this.drawing_in_progress = svgc.drawing_in_progress;
        this.new_drawing_in_progress = drawing_in_progress;
        svgc.drawing_in_progress = new_drawing_in_progress;
//        System.out.println("Came into the UndoableSwitchDrawingInProgress");
    }

    public String getPresentationName(){
        return "Presentation name return";
    }

    public void redo() throws CannotRedoException{
        super.redo();
        svgc.drawing_in_progress = new_drawing_in_progress;
    }

    public void undo() throws CannotUndoException{
        super.undo();
        svgc.drawing_in_progress = drawing_in_progress;
    }
}
