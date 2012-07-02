package Design_tool_prototype1;

import javax.swing.*;
import javax.swing.undo.*;

import java.util.*;
import java.awt.geom.*;

import org.w3c.dom.Element;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.bridge.UpdateManager;

public class UndoableClearPointList extends AbstractUndoableEdit{
    List<Point2D> current_points_list;
    List<Point2D> prev_points_list = new ArrayList();

    public UndoableClearPointList(List<Point2D> current_list){
        this.current_points_list = current_list;
        for(int i = 0; i < this.current_points_list.size(); i++){
            this.prev_points_list.add(new Point2D.Double(current_points_list.get(i).getX(), current_points_list.get(i).getY()));
        }
        current_points_list.clear();
    }

    public String getPresentationName(){
        return "Presentation name return";
    }

    public void redo() throws CannotRedoException{
        super.redo();
        for(int i = 0; i < this.current_points_list.size(); i++){
            this.prev_points_list.add(new Point2D.Double(current_points_list.get(i).getX(), current_points_list.get(i).getY()));
        }
        current_points_list.clear();
    }

    public void undo() throws CannotUndoException{
        super.undo();
        for(int i = 0; i < this.prev_points_list.size(); i++){
            this.current_points_list.add(prev_points_list.get(i));
        }
    }
}
