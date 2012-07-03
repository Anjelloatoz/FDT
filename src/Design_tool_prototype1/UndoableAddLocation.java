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
Location_list.add(Location);
        Runnable r = new Runnable(){
            public void run(){
                Element root = doc.getDocumentElement();
                root.appendChild(Location);
                
//                System.out.println("In the undoable add location Location_list:"+Location_list.size());
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
        Location_list.add(Location);
        Runnable r = new Runnable(){
            public void run(){
                Element root = doc.getDocumentElement();
                root.appendChild(Location);
                
            }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }

    public void undo() throws CannotUndoException{
        super.undo();
//        System.out.println("    UNDO-UndoableAddLocation");
        Location_list.remove(Location);
        Runnable r = new Runnable(){
            public void run(){
                Element root = doc.getDocumentElement();
                root.removeChild(Location);
                
            }
        };
        UpdateManager um = canvas.getUpdateManager();
	um.getUpdateRunnableQueue().invokeLater(r);
    }
}
