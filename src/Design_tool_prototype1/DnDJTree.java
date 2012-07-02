package Design_tool_prototype1;

import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import java.awt.Point;
import org.w3c.dom.Element;

import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetDropEvent;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import java.lang.NullPointerException;
import org.w3c.dom.DOMException;

public class DnDJTree extends JTree implements DragSourceListener, DropTargetListener, DragGestureListener {
    static DataFlavor localObjectFlavor;
    static{
        try{
            localObjectFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType);
        }
        catch(Exception cnfe){
            System.out.println("Class not found.. : "+cnfe);
        }
    }
    static DataFlavor[] supportedFlavors = {localObjectFlavor};
    DragSource dragSource;
    DropTarget dropTarget;

    TreeNode dropTargetNode = null;
    TreeNode draggedNode = null;
    SVGConjurer svgc;

    public DnDJTree(SVGConjurer svgc){
        super();
        this.svgc = svgc;
        setCellRenderer(new DefaultTreeCellRenderer());
        setModel(new DefaultTreeModel(new DefaultMutableTreeNode("Default")));
        dragSource = new DragSource();
        DragGestureRecognizer dgr = dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, this);
        dropTarget = new DropTarget(this, this);
    }

    public void dragGestureRecognized(DragGestureEvent dge){
        System.out.println("New Drag Gesture Recognized");
        Point clickPoint = dge.getDragOrigin();
        TreePath path = getPathForLocation(clickPoint.x, clickPoint.y);

        if(path == null){
            System.out.println("Not a node!");
            return;
        }

        draggedNode = (TreeNode)path.getLastPathComponent();

        if(draggedNode.toString().equals("Pattern History")){
            System.out.println("**** Returning....");
            return;
        }

        if(draggedNode.toString().equals("Rear face")||draggedNode.toString().equals("Front face")){
            System.out.println("**** Returning....");
            return;
        }

        try{
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)draggedNode;
            patternObject po = (patternObject)node.getUserObject();
            System.out.println("Pattern Object "+po.pattern_name+" found.");
            System.out.println("**** Returning....");
            return;
        }
        catch(Exception e){
            System.out.println("Exception: DnDJTree dragGestureRecognized e"+e);
        }
        try{
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)draggedNode;
            projectObject pro = (projectObject)node.getUserObject();
            return;
        }
        catch(Exception e2){
            System.out.println("Not a project object");
        }

        Transferable trans = new RJLTransferable(draggedNode);
        dragSource.startDrag(dge, null, trans, this);
    }

    public void dragDropEnd(DragSourceDropEvent dsde){
        System.out.println("DragDropEnd");
        dropTargetNode = null;
        draggedNode = null;
        repaint();
    }

    public void dragEnter(DragSourceDragEvent dsde){}
    public void dragExit(DragSourceEvent dse){}
    public void dragOver(DragSourceDragEvent dsde){}
    public void dropActionChanged(DragSourceDragEvent dsde){}

    public void dragEnter(DropTargetDragEvent dtde){
        System.out.println("DragEnter");
        dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
        System.out.println("Accepted drag enter");
    }

    public void dragExit(DropTargetEvent dte){}

    public void dragOver(DropTargetDragEvent dtde){
        Point dragPoint = dtde.getLocation();
        TreePath path = getPathForLocation(dragPoint.x, dragPoint.y);
        this.setSelectionPath(path);

        if(path == null){
            dropTargetNode = null;
        }
        else{
            dropTargetNode = (TreeNode)path.getLastPathComponent();
        }
        repaint();
    }

    public void drop(DropTargetDropEvent dtde){
        Point dropPoint = dtde.getLocation();
        TreePath path = getPathForLocation(dropPoint.x, dropPoint.y);
        if(path == null)return;
        boolean dropped = false;
        DefaultMutableTreeNode station = (DefaultMutableTreeNode)path.getLastPathComponent();
        DefaultMutableTreeNode guest = (DefaultMutableTreeNode)draggedNode;
//        System.out.println("Station is: "+station);
//        System.out.println("Guest is: "+guest);
        String document_indicator = "";

        TreeNode[] test = station.getPath();
        for(int x = 0; x< test.length; x++){
            DefaultMutableTreeNode tmp_node = (DefaultMutableTreeNode)test[x];
            try{

                Element ele = (Element)tmp_node.getUserObject();
                document_indicator = ele.getAttribute("id");
                System.out.println("*** Element:"+document_indicator);
            }
            catch(Exception e){
                System.out.println("Exception: DnDJTree; drop(DropTargetDropEvent dtde)"+ e);
            }
        }

        TreeNode[] test2 = guest.getPath();
        for(int x = 0; x< test.length; x++){
            DefaultMutableTreeNode tmp_node = (DefaultMutableTreeNode)test2[x];
            try{
                Element ele = (Element)tmp_node.getUserObject();
                document_indicator = ele.getAttribute("id");
                System.out.println("*** Element:"+document_indicator);
            }
            catch(Exception e){
                System.out.println("Exception: DnDJTree; drop(DropTargetDropEvent dtde)"+ e);
            }
        }

        Element station_element = (Element)station.getUserObject();
        Element guest_element = (Element)guest.getUserObject();
        Boolean copy = false;
        Element new_object = null;

        try{
            System.out.println("The station element is: "+station_element.getLocalName());
            System.out.println("The guest element is: "+guest_element.getLocalName());
            System.out.println("The station element name is: "+station_element.getAttribute("id"));
            System.out.println("The guest element name is: "+guest_element.getAttribute("id"));

            if(station_element.getLocalName().equals("svg")&& guest_element.getLocalName().equals("path")){
                System.out.println("The first option");
                station_element.appendChild(guest_element.getParentNode());
            }
            else if(station_element.getLocalName().equals("path")&& guest_element.getLocalName().equals("path")){
                System.out.println("The second option");
                station_element.getParentNode().appendChild(guest_element.getParentNode());
            }
            else if(station_element.getLocalName().equals("svg")&& guest_element.getLocalName().equals("svg")){
                System.out.println("The third option");
                station_element.appendChild(guest_element.getParentNode());
            }
            dtde.acceptDrop(DnDConstants.ACTION_MOVE);
            System.out.println("Finished the Try clause.");
        }
        catch(NullPointerException npe){
            System.out.println("Exception: DnDJTree Drop e2: "+npe);
            Element copy_layer = (Element)guest_element.cloneNode(true);
            copy_layer.setAttribute("id", "copy_"+guest_element.getAttribute("id"));
            copy_layer.setAttributeNS (null, "stroke", "red");
            new_object = copy_layer;
            try{
                station_element.appendChild(svgc.createNewLayer(copy_layer, document_indicator));
                System.out.println("And now the last child of the station, "+station_element.getAttribute("id")+" is"+((Element)station_element.getLastChild()).getAttribute("id"));
            }
            catch(Exception ex1){
                System.out.println("195 Element drag error:"+ex1);
            }
            copy = true;
            dtde.acceptDrop(DnDConstants.ACTION_COPY);
        }
        catch(DOMException dome){
            System.out.println("Exception: DnDJTree Drop dome: "+dome);
            station_element.appendChild(svgc.createNewLayer(guest_element, document_indicator));
            System.out.println("Move operation completed");
        }

        try{
            Object droppedObject = dtde.getTransferable().getTransferData(localObjectFlavor);
            if(copy){
                droppedObject = new_object;
            }
            MutableTreeNode droppedNode = null;
            if(droppedObject instanceof MutableTreeNode){
                droppedNode = (MutableTreeNode)droppedObject;
                if(!copy){
                    ((DefaultTreeModel)getModel()).removeNodeFromParent(droppedNode);
                }
            }
            else{
                droppedNode = new DefaultMutableTreeNode(droppedObject);
            }

            DefaultMutableTreeNode dropNode = (DefaultMutableTreeNode)path.getLastPathComponent();

//            if(dropNode.isLeaf()){
//                DefaultMutableTreeNode parent = (DefaultMutableTreeNode)dropNode.getParent();
//                int index = parent.getIndex(dropNode);
//                ((DefaultTreeModel)getModel()).insertNodeInto(droppedNode, parent, index);
//            }
//            else{
                ((DefaultTreeModel)getModel()).insertNodeInto(droppedNode, dropNode, dropNode.getChildCount());
//            }
            dropped = true;
            svgc.refresh();
        }

        catch(Exception e){
            System.out.println("Dropping exception: "+e);
        }
        dtde.dropComplete(dropped);
    }
    public void dropActionChanged(DropTargetDragEvent dtde){}

    public class RJLTransferable implements Transferable{
    Object object;
    public RJLTransferable(Object o){
        object = o;
    }

    public Object getTransferData(DataFlavor df) throws UnsupportedFlavorException, IOException{
        if(isDataFlavorSupported(df)){
            return object;
        }
        else throw new UnsupportedFlavorException(df);
    }

    public boolean isDataFlavorSupported(DataFlavor df){
        return (df.equals(localObjectFlavor));
    }

    public DataFlavor[] getTransferDataFlavors(){
        return supportedFlavors;
    }
}
}