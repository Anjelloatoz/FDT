package Design_tool_prototype1;

import javax.swing.*;
import javax.swing.undo.*;

import java.util.*;

import org.w3c.dom.Element;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.bridge.UpdateManager;

public class UndoableDrawingNumberIncrease extends AbstractUndoableEdit{
    SVGConjurer svgc;
    JSVGCanvas canvas;

    public UndoableDrawingNumberIncrease(SVGConjurer svgc){
        this.svgc = svgc;
        System.out.println("Came into the UndoableDrawingNumberIncrease");
        svgc.drawing_number = svgc.drawing_number+1;
    }

    public String getPresentationName(){
        return "Presentation name return";
    }

    public void redo() throws CannotRedoException{
        super.redo();
        System.out.println("Came into the UndoableDrawingNumberIncrease redo");
        svgc.drawing_number = svgc.drawing_number+1;
    }

    public void undo() throws CannotUndoException{
        super.undo();
        System.out.println("Came into the UndoableDrawingNumberIncrease undo");
        svgc.drawing_number = svgc.drawing_number-1;
    }
}
