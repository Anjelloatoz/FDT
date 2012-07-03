package Design_tool_prototype1;

import javax.swing.*;
import javax.swing.undo.*;

import java.util.*;

import org.w3c.dom.Element;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.bridge.UpdateManager;

public class UndoableRemovePatternByElement extends AbstractUndoableEdit{
    Element delete_element;
    projectObject project;
    treeHandler tree_handler;
    ribbonTest ribbon;
    patternObject deleted_pattern;

    public UndoableRemovePatternByElement(projectObject project, Element del_element, treeHandler th, ribbonTest rt){
        this.delete_element = del_element;
        this.project = project;
        this.tree_handler = th;
        this.ribbon = rt;
        deleted_pattern = project.removePatternByElement(delete_element);
        tree_handler.refreshTree(project, ribbon);
    }

    public String getPresentationName(){
        return "Presentation name return";
    }

    public void redo() throws CannotRedoException{
        super.redo();

        deleted_pattern = project.removePatternByElement(delete_element);
        tree_handler.refreshTree(project, ribbon);
    }

    public void undo() throws CannotUndoException{
        super.undo();
        project.addPatternObject(deleted_pattern);
        tree_handler.refreshTree(project, ribbon);
    }
}
