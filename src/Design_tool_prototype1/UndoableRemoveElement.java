package Design_tool_prototype1;

import javax.swing.*;
import javax.swing.undo.*;

import java.util.*;

import org.w3c.dom.Element;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.bridge.UpdateManager;
import org.w3c.dom.Document;

public class UndoableRemoveElement extends AbstractUndoableEdit{
    SVGConjurer svgc;
    JSVGCanvas canvas;
    Element old_element;
    private Document document;

    public UndoableRemoveElement(SVGConjurer svgc, Element element){
        this.svgc = svgc;
        this.canvas = svgc.canvas;
        this.old_element = element;
        this.document = svgc.document;
        System.out.println("Came into the UndoableEdit");
        Element root = document.getDocumentElement();
        root.removeChild(old_element);
    }

    public String getPresentationName(){
        return "Presentation name return";
    }

    public void redo() throws CannotRedoException{
        super.redo();
        System.out.println("Came into the redo");
        Element root = document.getDocumentElement();
        root.removeChild(old_element);
    }

    public void undo() throws CannotUndoException{
        super.undo();
        System.out.println("Came into the undo");
        Element root = document.getDocumentElement();
        root.appendChild(old_element);
    }
}
