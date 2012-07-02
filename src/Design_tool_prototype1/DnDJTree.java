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
import org.w3c.dom.NodeList;

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
    SVGConjurer svgF;
    SVGConjurer svgR;
    ribbonTest rt;

    public DnDJTree(ribbonTest rt){
        super();
        this.svgF = rt.svgF;
        this.svgR = rt.svgR;
        this.rt = rt;
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
        System.out.println("DROP PATH: In the drop begining.");
        Point dropPoint = dtde.getLocation();
        TreePath path = getPathForLocation(dropPoint.x, dropPoint.y);
        if(path == null)return;

        boolean dropped = false;
        DefaultMutableTreeNode station = (DefaultMutableTreeNode)path.getLastPathComponent();
        DefaultMutableTreeNode guest = (DefaultMutableTreeNode)draggedNode;

        Element station_element = (Element)station.getUserObject();
        Element guest_element = (Element)guest.getUserObject();
        Boolean copy = false;
        Element new_object = null;

        try{
            System.out.println("DROP PATH: In the first try clause.");
            System.out.println("The station element is: "+station_element.getLocalName());
            System.out.println("The guest element is: "+guest_element.getLocalName());
            System.out.println("The station element name is: "+station_element.getAttribute("id"));
            System.out.println("The guest element name is: "+guest_element.getAttribute("id"));

            if(station_element.getLocalName().equals("svg")&& guest_element.getLocalName().equals("path")){
                System.out.println("The first option");
                new_object = guest_element;

                station_element.appendChild(guest_element.getParentNode().getParentNode());
            }
            else if(station_element.getLocalName().equals("path")&& guest_element.getLocalName().equals("path")){
                System.out.println("The second option");
                new_object = guest_element;
                station_element.getParentNode().appendChild(guest_element.getParentNode().getParentNode());
            }
            else if(station_element.getLocalName().equals("svg")&& guest_element.getLocalName().equals("svg")){
                System.out.println("The third option");
                station_element.appendChild(guest_element.getParentNode());
            }
            dtde.acceptDrop(DnDConstants.ACTION_MOVE);
            System.out.println("Finished the Try clause.");
        }
        catch(NullPointerException npe){
            System.out.println("DROP PATH: Came into the catch clause with null pointer exception");
            System.out.println("Exception: DnDJTree Drop e2: "+npe);
            Element copy_layer = (Element)guest_element.cloneNode(true);
            copy_layer.setAttribute("id", "copy_"+guest_element.getAttribute("id"));
            copy_layer.setAttributeNS (null, "stroke", "red");
            new_object = copy_layer;
            try{
                ElementLocalizer el = new ElementLocalizer(station_element, copy_layer, svgF, svgR);
                station_element.appendChild(el.container);
                new_object = (Element)el.container.getFirstChild().getFirstChild();
                rt.selectedSVGC.registerPatternListeners((Element)el.container.getFirstChild().getFirstChild());
                System.out.println("And now the last child of the station, "+station_element.getAttribute("id")+" is"+((Element)station_element.getLastChild()).getAttribute("id"));
            }
            catch(Exception ex1){
                System.out.println("195 Element drag error:"+ex1);
            }
            copy = true;
            System.out.println("DROP PATH: ***** Copy flag set to true.");
            dtde.acceptDrop(DnDConstants.ACTION_COPY);
        }
        catch(DOMException dome){
            System.out.println("DROP PATH: Came into Exception with DOM");
            ElementLocalizer el = new ElementLocalizer(station_element, (Element)guest_element.getParentNode().getParentNode(), svgF, svgR);
            station_element.appendChild(el.container);
            NodeList nl = el.container.getElementsByTagName("path");
            new_object = (Element)nl.item(0);
            rt.selectedSVGC.registerPatternListeners((Element)nl.item(0));
            guest_element.getParentNode().getParentNode().getParentNode().removeChild(guest_element.getParentNode().getParentNode());
            System.out.println("Move operation completed");
        }

        try{
            System.out.println("DROP PATH: Came into the object copying try clause");
            Object droppedObject = dtde.getTransferable().getTransferData(localObjectFlavor);
            if(copy){
                System.out.println("DROP PATH: ****** copy flag is true.");
                droppedObject = new_object;
            }
            droppedObject = new_object;
            MutableTreeNode droppedNode = null;
            if(!copy){
                    System.out.println("DROP PATH: In the !copy clause.");
                    ((DefaultTreeModel)getModel()).removeNodeFromParent(guest);
                }
            if(droppedObject instanceof MutableTreeNode){
                droppedNode = (MutableTreeNode)droppedObject;
                System.out.println("Before copy check");
                if(!copy){
                    System.out.println("DROP PATH: In the !copy clause.");
                    ((DefaultTreeModel)getModel()).removeNodeFromParent(droppedNode);
                }
                System.out.println("After copy check");
            }
            else{
                droppedNode = new DefaultMutableTreeNode(droppedObject);
            }

            DefaultMutableTreeNode dropNode = (DefaultMutableTreeNode)path.getLastPathComponent();

            dropNode.setAllowsChildren(true);
            ((DefaultTreeModel)getModel()).insertNodeInto(droppedNode, dropNode, dropNode.getChildCount());
            dropped = true;
            svgF.refresh();
            svgR.refresh();
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