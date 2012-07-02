package Design_tool_prototype1;

import javax.swing.*;
import javax.swing.undo.*;

import java.util.*;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.bridge.UpdateManager;

public class UndoableSetAttribute extends AbstractUndoableEdit{
    SVGConjurer svgc;
    JSVGCanvas canvas;
    Document document;
    Element element;
    String atrb;
    String old_value;
    String new_value;

    public UndoableSetAttribute(SVGConjurer svgc, Element subject, String attribute, String value){
        this.svgc = svgc;
        this.canvas = svgc.canvas;
        this.document = svgc.document;
        this.element = subject;
        this.atrb = attribute;
        this.old_value = element.getAttribute(atrb);
        this.new_value = value;
//        System.out.println("Came into the UndoableSetAttribute");
        
        Runnable r = new Runnable(){
          public void run(){
              element.setAttribute(atrb, new_value);
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
              element.setAttribute(atrb, new_value);
          }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }

    public void undo() throws CannotUndoException{
        super.undo();

        Runnable r = new Runnable(){
          public void run(){
              element.setAttribute(atrb, old_value);
          }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
//        System.out.println("current_drawing_locations: "+svgc.current_drawing_locations.size());
    }
}
