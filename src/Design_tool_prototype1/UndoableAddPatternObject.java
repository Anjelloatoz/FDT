package Design_tool_prototype1;

import javax.swing.*;
import javax.swing.undo.*;

import java.util.*;

import org.w3c.dom.Element;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.bridge.UpdateManager;

public class UndoableAddPatternObject extends AbstractUndoableEdit{
    patternObject pattern;
    projectObject project;
    treeHandler tree_handler;
    ribbonTest ribbon;

    public UndoableAddPatternObject(patternObject po, projectObject project, treeHandler th, ribbonTest rt){
        this.pattern = po;
        this.project = project;
        this.tree_handler = th;
        this.ribbon = rt;
//        System.out.println("Came into the UndoableAddPatternObject");
        project.addPatternObject(pattern);
        pattern.pattern_name = "Dress Component "+project.patterns.size();
        tree_handler.refreshTree(project, ribbon);
    }

    public String getPresentationName(){
        return "Presentation name return";
    }

    public void redo() throws CannotRedoException{
        super.redo();

        project.addPatternObject(pattern);
        tree_handler.refreshTree(project, ribbon);
    }

    public void undo() throws CannotUndoException{
        super.undo();
        project.removePatternObject(pattern);
        tree_handler.refreshTree(project, ribbon);
    }
}
