package Design_tool_prototype1;

import javax.swing.*;
import javax.swing.undo.*;

import java.util.*;
import java.awt.geom.*;

import org.w3c.dom.Element;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.bridge.UpdateManager;

public class UndoableClearLocationList extends AbstractUndoableEdit{
    List<Element> current_elements_list;
    List<Element> prev_elements_list = new ArrayList();

    public UndoableClearLocationList(List<Element> current_list){
        System.out.println("Came into the UndoableClearElementList");
        this.current_elements_list = current_list;
        for(int i = 0; i < this.current_elements_list.size(); i++){
            this.prev_elements_list.add((Element)current_elements_list.get(i).cloneNode(true));
        }
        current_elements_list.clear();
    }

    public String getPresentationName(){
        return "Presentation name return";
    }

    public void redo() throws CannotRedoException{
        super.redo();
        System.out.println("Came into the UndoableClearLocationList redo");
        for(int i = 0; i < this.current_elements_list.size(); i++){
            this.prev_elements_list.add((Element)current_elements_list.get(i).cloneNode(true));
        }
        current_elements_list.clear();
    }

    public void undo() throws CannotUndoException{
        super.undo();
        System.out.println("Came into the UndoableClearLocationList undo");
        for(int i = 0; i < this.prev_elements_list.size(); i++){
            this.current_elements_list.add(prev_elements_list.get(i));
        }
    }
}
