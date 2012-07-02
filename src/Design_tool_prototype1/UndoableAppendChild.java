package Design_tool_prototype1;

import javax.swing.*;
import javax.swing.undo.*;

import java.util.*;

import org.w3c.dom.Element;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.bridge.UpdateManager;

public class UndoableAppendChild extends AbstractUndoableEdit{
    SVGConjurer svgc;
    final Element Parent;
    final Element Child;
    final Element prev_Parent;
    JSVGCanvas canvas;
    final Element prev_parent;

    public UndoableAppendChild(SVGConjurer svgc, Element parent, Element child){
        this.svgc = svgc;
        this.canvas = svgc.canvas;
        Parent = parent;
        prev_Parent = (Element)child.getParentNode();
        Child = child;
        prev_parent = (Element)child.getParentNode();
//        System.out.println("Came into the UndoableAppendChild");

        Runnable r = new Runnable(){

          public void run(){
              Parent.appendChild(Child);
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
//        System.out.println("Came into the UndoableAppendChild redo");

        Runnable r = new Runnable(){
          public void run(){
              Parent.appendChild(Child);
          }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }

    public void undo() throws CannotUndoException{
        super.undo();
//        System.out.println("Came into the UndoableAppendChild undo");
//        System.out.println("prev_Parent: "+prev_Parent.getLocalName());
//        System.out.println("Child: "+Child.getLocalName());

        Runnable r = new Runnable(){
          public void run(){
//
              try{
              prev_Parent.appendChild(Child);
              }
              catch(Exception e){
                  Parent.removeChild(Child);
              }
          }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }
}
