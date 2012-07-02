package Design_tool_prototype1;

import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DnDConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultTreeModel;

public class TreeDragSource implements DragSourceListener, DragGestureListener{
    DragSource source;
    DragGestureRecognizer recognizer;
    TransferableTreeNode transferable;
    DefaultMutableTreeNode oldNode;
    JTree sourceTree;

    public TreeDragSource(JTree tree, int actions){
        sourceTree = tree;
        source = new DragSource();
        recognizer = source.createDefaultDragGestureRecognizer(sourceTree, actions, this);
    }

    public void dragGestureRecognized(DragGestureEvent dge){
        TreePath path = sourceTree.getAnchorSelectionPath();
        if((path == null)||(path.getPathCount()<1)){
            return;
        }
        oldNode = (DefaultMutableTreeNode)path.getLastPathComponent();
        transferable = new TransferableTreeNode(path);
        source.startDrag(dge, DragSource.DefaultMoveNoDrop, transferable, this);
    }

    public void dragEnter(DragSourceDragEvent dsde){

    }

    public void dragExit(DragSourceEvent dse){

    }

    public void dragOver(DragSourceDragEvent dsde){

    }

    public void dropActionChanged(DragSourceDragEvent dsde){
        System.out.println("ACTION: "+dsde.getDropAction());
        System.out.println("TARGET ACTION: "+dsde.getTargetActions());
        System.out.println("USER ACTION: "+dsde.getUserAction());
    }

    public void dragDropEnd(DragSourceDropEvent dsde){
        System.out.println("DROP ACTION: "+dsde.getDropAction());
        if(dsde.getDropSuccess()&&(dsde.getDropAction()==DnDConstants.ACTION_MOVE)){
            ((DefaultTreeModel)sourceTree.getModel()).removeNodeFromParent(oldNode);
        }
    }
}





