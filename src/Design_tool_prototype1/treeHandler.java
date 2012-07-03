package Design_tool_prototype1;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.util.*;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import javax.swing.plaf.metal.MetalIconFactory;
import javax.swing.tree.*;
import java.awt.dnd.DnDConstants;


public class treeHandler implements ActionListener, MouseListener{
    JTree tree;
    projectObject project;
    ribbonTest rt;
    JPopupMenu history_element_popup;
    JPopupMenu project_element_popup;
    JPopupMenu pattern_element_popup;
    SVGConjurer svgc;
    Element selected_element;
    TreeDragSource ds;
    TreeDropTarget dt;

    public treeHandler(projectObject project, ribbonTest rt, SVGConjurer svgc){
        this.project = project;
        this.rt = rt;
        this.svgc = svgc;


        history_element_popup = new JPopupMenu();
        JMenuItem new_layer_item = new JMenuItem("Create new layer", new ImageIcon("new_layer.gif"));
        new_layer_item.setActionCommand("new_layer_item");
        new_layer_item.addActionListener(this);
        history_element_popup.add(new_layer_item);

        project_element_popup = new JPopupMenu();
        JMenuItem project_item = new JMenuItem("Create new pattern", new ImageIcon("pattern_object_icon.gif"));
        project_item.setActionCommand("new_pattern");
        project_item.addActionListener(this);
        project_element_popup.add(project_item);

        

        updateTree(project);
    }

    public void updateTree(projectObject po){
        this.project = po;
        IconNode project_node = new IconNode(project);
        project_node.setIcon(MetalIconFactory.getFileChooserHomeFolderIcon());

        IconNode[] pattern_nodes = new IconNode[project.patterns.size()];
        for(int i = 0; i < pattern_nodes.length; i++){
            pattern_nodes[i] = new IconNode(project.patterns.get(i));
            project_node.add(pattern_nodes[i]);
        }

        IconNode[] front_nodes = new IconNode[project.patterns.size()];
        IconNode[] rear_nodes = new IconNode[project.patterns.size()];
        for(int i = 0; i < pattern_nodes.length; i++){
            front_nodes[i] = new IconNode("Front face");
            rear_nodes[i] = new IconNode("Rear face");

            pattern_nodes[i].add(front_nodes[i]);
            pattern_nodes[i].add(rear_nodes[i]);

            front_nodes[i].add(elementIterator(project.patterns.get(i).front));
//            rear_nodes[i].add(elementIterator(project.patterns.get(i).rear));
        }
        
        IconNode history_node = new IconNode("Pattern History");
        project_node.add(history_node);
        System.out.println("The size of history elements is: "+project.history_elements.size());
            
        for(int i = 0; i < project.history_elements.size(); i++){
            history_node.add(new IconNode(project.history_elements.get(i)));
        }

        tree = new JTree(project_node);
        tree.setDragEnabled(true);
        tree.setCellRenderer(new NavigatorTreeCellRenderer());
        tree.addMouseListener(this);
        ds = new TreeDragSource(tree, DnDConstants.ACTION_COPY_OR_MOVE);
        dt = new TreeDropTarget(tree);
    }

    public void refreshTree(projectObject po, ribbonTest rt){
        updateTree(po);
        rt.resetProject(this);
    }

    private IconNode elementIterator(Element e){
        IconNode element_node = new IconNode(e);
//        element_node.setIconName(e.getAttribute("id"));
        if(e.hasChildNodes()){
            NodeList node_list = e.getChildNodes();
            for(int i = 0; i < node_list.getLength();i++){
                element_node.add(elementIterator((Element)node_list.item(i)));
            }
            return element_node;
        }
        else{
            return element_node;
        }
    }

    public JTree getTree(){
        return tree;
    }

    public void actionPerformed(ActionEvent ae){
        System.out.println(ae.getActionCommand());
        if(ae.getActionCommand().equals("new_layer_item")){
            Element new_element = (Element)selected_element.cloneNode(true);
            svgc.addNewLayer(new_element);
        }
        else if(ae.getActionCommand().equals("new_pattern")){
            svgc.addNewPattern();
        }
    }

    public void mouseClicked(MouseEvent e){

    }
    public void mousePressed(MouseEvent e) {
        TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
        tree.setSelectionPath(selPath);
        
    }

    public void mouseReleased(MouseEvent e){
        if(e.isPopupTrigger()){
            TreePath selPath = tree.getSelectionPath();
            System.out.println("The selection path is: "+tree.getSelectionPath());
            try{
                System.out.println("Try 1");
                projectObject project_object = (projectObject)((DefaultMutableTreeNode)selPath.getLastPathComponent()).getUserObject();
                System.out.println("The node is a projectObject: "+project_object.project_name);
                project_element_popup.show(tree, e.getX(), e.getY());
            }
            catch(Exception e1){
                try{
                    System.out.println("Try 2");
                        Element element = (Element)(((DefaultMutableTreeNode)selPath.getLastPathComponent()).getUserObject());
                        System.out.println("Element credentials are: "+element.getLocalName()+" ID: "+element.getAttribute("id"));
                        selected_element = element;

                        if(element.getParentNode()==null){
                            System.out.println("This is a history element");
                            history_element_popup.show(tree, e.getX(), e.getY());
                        }
                    if(((DefaultMutableTreeNode)selPath.getLastPathComponent()).getUserObject().equals("Pattern History"))
                        System.out.println("The pattern History node.");
                }
                catch(Exception e2){
                    try{
                        patternObject pattern_object = (patternObject)((DefaultMutableTreeNode)selPath.getLastPathComponent()).getUserObject();
                        System.out.println("Pattern object: "+pattern_object.pattern_name);
                    }
                    catch(Exception e3){
                        try{
                            System.out.println("Try 4");
                        }
                        catch(Exception e4){

                        }
                        System.out.println("The component could not be casted: "+e3);
                        System.out.println("As of here: "+(((DefaultMutableTreeNode)selPath.getLastPathComponent()).getUserObject()));
                    }
                }
            }
        }
    }

    public void mouseEntered(MouseEvent e){

    }
    public void mouseExited(MouseEvent e) {
        }
}
