package Design_tool_prototype1;

import javax.swing.*;
import javax.swing.undo.*;
import java.util.*;

import org.w3c.dom.Element;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.bridge.UpdateManager;
import org.w3c.dom.Document;

public class UndoableAddLocation extends AbstractUndoableEdit{
    private String location_name = "";
    private boolean allowAdds = false;
    private Vector addedEdits;
    private SVGConjurer svgc;
    private final Element Location;
    private final Document doc;
    JSVGCanvas canvas;
    
    final java.util.List<Element> Location_list;
    public UndoableAddLocation(SVGConjurer svgc, Element location, java.util.List<Element> location_list, Document document){
        this.svgc = svgc;
        this.Location = location;
        this.Location_list = location_list;
        this.doc = document;
        this.canvas = svgc.canvas;
        svgc.registerEditPointListeners(Location);
//        System.out.println("Came into the UndoableAddLocation");

        Runnable r = new Runnable(){
            public void run(){
                Element root = doc.getDocumentElement();
                root.appendChild(Location);
                Location_list.add(Location);
            }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }

    public String getPresentationName(){
        return location_name;
    }

    public void redo() throws CannotRedoException{
        super.redo();
        Runnable r = new Runnable(){
            public void run(){
                Element root = doc.getDocumentElement();
                root.appendChild(Location);
                Location_list.add(Location);
            }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }

    public void undo() throws CannotUndoException{
        super.undo();
//        System.out.println("    UNDO-UndoableAddLocation");
        Runnable r = new Runnable(){
            public void run(){
                Element root = doc.getDocumentElement();
                root.removeChild(Location);
                Location_list.remove(Location);
            }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }
}
