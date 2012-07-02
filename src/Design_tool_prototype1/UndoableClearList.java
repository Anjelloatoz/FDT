package Design_tool_prototype1;

import javax.swing.*;
import javax.swing.undo.*;

import java.util.*;
import java.awt.geom.*;

import org.w3c.dom.Element;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.bridge.UpdateManager;

public class UndoableClearList extends AbstractUndoableEdit{
    List<Element> current_list;
    List<Element> prev_current_drawing_locations = new ArrayList();

    public UndoableClearList(List<Element> current_list){
        System.out.println("Came into the UndoableClearList");
        this.current_list = current_list;
        System.out.println("The number of elements in the current list are :"+current_list.size());
        for(int i = 0; i < this.current_list.size(); i++){
            this.prev_current_drawing_locations.add((Element)current_list.get(i).cloneNode(true));
        }
System.out.println("The number of elements in the prev current list are :"+prev_current_drawing_locations.size());
        current_list.clear();
    }

    public String getPresentationName(){
        return "Presentation name return";
    }

    public void redo() throws CannotRedoException{
        super.redo();
        System.out.println("Came into the redo");
        for(int i = 0; i < this.current_list.size(); i++){
            this.prev_current_drawing_locations.add((Element)current_list.get(i).cloneNode(true));
        }
        current_list.clear();
    }

    public void undo() throws CannotUndoException{
        super.undo();
        System.out.println("Came into the undo");
        for(int i = 0; i < this.prev_current_drawing_locations.size(); i++){
            this.current_list.add(prev_current_drawing_locations.get(i));
        }
    }
}
