package Design_tool_prototype1;

import javax.swing.*;
import javax.swing.undo.*;

import java.util.*;
import java.awt.geom.*;

import org.w3c.dom.Element;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.bridge.UpdateManager;

public class UndoableRemoveChildList extends AbstractUndoableEdit{
    List<Element> current_elements_list;
    List<Element> prev_elements_list = new ArrayList();
    Element parent_node;
    SVGConjurer svgc;

    public UndoableRemoveChildList(SVGConjurer svgc, Element parent, List<Element> current_list){
        this.current_elements_list = current_list;
        this.svgc = svgc;
        this.parent_node = parent;
        for(int i = 0; i < this.current_elements_list.size(); i++){
            this.prev_elements_list.add((Element)current_elements_list.get(i).cloneNode(true));
        }
        for(int i = 0; i < this.current_elements_list.size(); i++){
            current_list.get(i).getParentNode().removeChild(current_list.get(i));
        }
        current_elements_list.clear();
        svgc.refresh();
    }

    public String getPresentationName(){
        return "Presentation name return";
    }

    public void redo() throws CannotRedoException{
        super.redo();
        for(int i = 0; i < this.current_elements_list.size(); i++){
            this.prev_elements_list.add((Element)current_elements_list.get(i).cloneNode(true));
        }
        for(int i = 0; i < this.current_elements_list.size(); i++){
            prev_elements_list.get(i).getParentNode().removeChild(prev_elements_list.get(i));
        }
        current_elements_list.clear();
        svgc.refresh();
    }

    public void undo() throws CannotUndoException{
        super.undo();
        for(int i = 0; i < this.prev_elements_list.size(); i++){
            this.current_elements_list.add(prev_elements_list.get(i));
            parent_node.appendChild(prev_elements_list.get(i));
        }
        svgc.refresh();
    }
}
