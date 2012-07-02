package Design_tool_prototype1;

import javax.swing.*;
import javax.swing.undo.*;

import java.util.*;

import org.w3c.dom.Element;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.bridge.UpdateManager;

public class UndoableRemoveChild extends AbstractUndoableEdit{
    SVGConjurer svgc;
    final Element Parent;
    final Element Child;
    JSVGCanvas canvas;

    public UndoableRemoveChild(SVGConjurer svgc, Element parent, Element child){
        this.svgc = svgc;
        this.canvas = svgc.canvas;
        Parent = parent;
        Child = child;
        System.out.println("Came into the UndoableRemoveChild "+child.getAttribute("id"));

        Runnable r = new Runnable(){
          public void run(){
//              Parent.removeChild(Child);
              System.out.println("in the UndoableRemoveChild: "+Child.getAttribute("id"));
              Child.getParentNode().removeChild(Child);
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
        System.out.println("Came into the UndoableRemoveChild redo");

        Runnable r = new Runnable(){
          public void run(){
//              Parent.removeChild(Child);
              Child.getParentNode().removeChild(Child);
          }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }

    public void undo() throws CannotUndoException{
        super.undo();
        System.out.println("Came into the UndoableRemoveChild undo");

        Runnable r = new Runnable(){
          public void run(){
              Parent.appendChild(Child);
          }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }
}
