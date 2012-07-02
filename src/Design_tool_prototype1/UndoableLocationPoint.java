package Design_tool_prototype1;

import javax.swing.*;
import javax.swing.undo.*;

import java.util.*;

public class UndoableLocationPoint extends AbstractUndoableEdit{
    private String location_name = "";
    private boolean allowAdds = false;
    private Vector addedEdits;
    public UndoableLocationPoint(String name){
        location_name = name;
    }

    public String getPresentationName(){
        return location_name;

    }

    public void redo() throws CannotRedoException{
        super.redo();
    }

    public void undo() throws CannotUndoException{
        super.undo();
    }

    public boolean addEdit(UndoableEdit anEdit){
        if(allowAdds){
            addedEdits = new Vector();
            addedEdits.addElement(anEdit);
            return true;
        } else
            return false;
    }
}
