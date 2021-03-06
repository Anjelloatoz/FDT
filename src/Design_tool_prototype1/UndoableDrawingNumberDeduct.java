package Design_tool_prototype1;

import javax.swing.*;
import javax.swing.undo.*;

import java.util.*;

import org.w3c.dom.Element;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.bridge.UpdateManager;

public class UndoableDrawingNumberDeduct extends AbstractUndoableEdit{
    SVGConjurer svgc;
    JSVGCanvas canvas;

    public UndoableDrawingNumberDeduct(SVGConjurer svgc){
        this.svgc = svgc;
        svgc.drawing_number = svgc.drawing_number-1;
    }

    public String getPresentationName(){
        return "Presentation name return";
    }

    public void redo() throws CannotRedoException{
        super.redo();
        svgc.drawing_number = svgc.drawing_number-1;
    }

    public void undo() throws CannotUndoException{
        super.undo();
        svgc.drawing_number = svgc.drawing_number+1;
    }
}
