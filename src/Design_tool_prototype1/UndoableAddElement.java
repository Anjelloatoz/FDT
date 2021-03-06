package Design_tool_prototype1;

import javax.swing.*;
import javax.swing.undo.*;

import java.util.*;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.bridge.UpdateManager;

public class UndoableAddElement extends AbstractUndoableEdit{
    SVGConjurer svgc;
    JSVGCanvas canvas;
    Element element;
    Document document;
    Element top_element;

    public UndoableAddElement(SVGConjurer svgc, Element new_element, Element loc_element){
        this.svgc = svgc;
        this.canvas = svgc.canvas;
        this.element = new_element;
        this.document = svgc.document;
        this.top_element = loc_element;
//        System.out.println("Came into the UndoableAddElement");

        Runnable r = new Runnable(){
          public void run(){
              Element root = document.getDocumentElement();
//              root.appendChild(element);
              root.insertBefore(element, top_element);
          }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }

    public String getPresentationName(){
        return "Presentation name return";
    }

    public void redo() throws CannotRedoException{
        super.redo();

        Runnable r = new Runnable(){
          public void run(){
              Element root = document.getDocumentElement();
//              root.appendChild(element);
              root.insertBefore(element, top_element);
          }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }

    public void undo() throws CannotUndoException{
        super.undo();
//        System.out.println("    UNDO-UndoableAddElement");

        Runnable r = new Runnable(){
          public void run(){
              Element root = document.getDocumentElement();
//              root.removeChild(element);
              element.getParentNode().removeChild(element);
          }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }
}
