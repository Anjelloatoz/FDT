package Design_tool_prototype1;

import javax.swing.*;
import javax.swing.undo.*;

import java.util.*;

import org.w3c.dom.Element;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.bridge.UpdateManager;

public class UndoableEdit extends AbstractUndoableEdit{
    SVGConjurer svgc;
    JSVGCanvas canvas;

    public UndoableEdit(SVGConjurer svgc){
        this.svgc = svgc;
        this.canvas = svgc.canvas;
        System.out.println("Came into the UndoableEdit");

        Runnable r = new Runnable(){
          public void run(){

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
        System.out.println("Came into the redo");

        Runnable r = new Runnable(){
          public void run(){

          }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }

    public void undo() throws CannotUndoException{
        super.undo();
        System.out.println("Came into the undo");

        Runnable r = new Runnable(){
          public void run(){

          }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }
}
