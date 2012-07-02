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
import javax.swing.plaf.TreeUI;
import javax.swing.plaf.basic.BasicTreeUI;

public class treeHandler implements ActionListener, MouseListener{
    DnDJTree tree;
    projectObject project;
    ribbonTest rt;
    JPopupMenu history_element_popup;
    JPopupMenu project_element_popup;
    JPopupMenu drawing_element_popup;
    JPopupMenu pattern_object_popup;
    SVGConjurer svgc;
    Element selected_element;
    patternObject pattern_object;
    TreePath selPath;

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

        drawing_element_popup = new JPopupMenu();
        JMenuItem drawing_rename_item = new JMenuItem("Rename", new ImageIcon("rename.gif"));
        drawing_rename_item.setActionCommand("drawing_rename");
        drawing_rename_item.addActionListener(this);

        JMenuItem drawing_duplicate_item = new JMenuItem("Make Duplicate", new ImageIcon("rename.gif"));
        drawing_duplicate_item.setActionCommand("drawing_duplicate");
        drawing_duplicate_item.addActionListener(this);

        drawing_element_popup.add(drawing_rename_item);
        drawing_element_popup.add(drawing_duplicate_item);

        pattern_object_popup = new JPopupMenu();
        JMenuItem pattern_rename_item = new JMenuItem("Rename", new ImageIcon("rename.gif"));
        pattern_rename_item.setActionCommand("pattern_rename");
        pattern_rename_item.addActionListener(this);
        pattern_object_popup.add(pattern_rename_item);

        JMenuItem pattern_display_item = new JMenuItem("display", new ImageIcon("rename.gif"));
        pattern_display_item.setActionCommand("pattern_display");
        pattern_display_item.addActionListener(this);
        pattern_object_popup.add(pattern_display_item);

        JMenuItem pattern_save_item = new JMenuItem("Save Pattern", new ImageIcon("rename.gif"));
        pattern_save_item.setActionCommand("pattern_save");
        pattern_save_item.addActionListener(this);
        pattern_object_popup.add(pattern_save_item);

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
            front_nodes[i] = new IconNode(project.patterns.get(i).front);
            rear_nodes[i] = new IconNode(project.patterns.get(i).rear);

            pattern_nodes[i].add(front_nodes[i]);
            pattern_nodes[i].add(rear_nodes[i]);
            elementIterator2(front_nodes[i], project.patterns.get(i).front);
            elementIterator2(rear_nodes[i], project.patterns.get(i).rear);

//*            front_nodes[i].add(elementIterator(project.patterns.get(i).front));
//            rear_nodes[i].add(elementIterator(project.patterns.get(i).rear));
        }
        
        IconNode history_node = new IconNode("Pattern History");
        project_node.add(history_node);
            
        for(int i = 0; i < project.history_elements.size(); i++){
            history_node.add(new IconNode(project.history_elements.get(i)));
        }

        tree = new DnDJTree(rt);
        DefaultTreeModel mod = new DefaultTreeModel(project_node);
        tree.setModel(mod);
        tree.setCellRenderer(new NavigatorTreeCellRenderer());
        tree.addMouseListener(this);
        
        TreeUI ui = tree.getUI();
        ui = new BasicTreeUI();
//        System.out.println("**********The ui is "+ui.toString());
        tree.putClientProperty("JTree.lineStyle", "Angled");
        tree.repaint();
    }

    public void refreshTree(projectObject po, ribbonTest rt){
        updateTree(po);
        rt.resetProject(this);
    }

    private IconNode elementIterator2(IconNode root_node, Element e){
        if(e == null){
            System.out.println("****************    in the treeHandler elementIterator2 E is null");
            return root_node;
        }
        IconNode last_node = null;
        int owner_number = 1000;
        if(e.getLocalName().equals("svg")){
            for(int i = 0; i < e.getChildNodes().getLength(); i++){
                if(((Element)e.getChildNodes().item(i)).getLocalName().equals("path")){
                    owner_number = i;
                    last_node = new IconNode(((Element)e.getChildNodes().item(i)));
                }
                else if(((Element)e.getChildNodes().item(i)).getLocalName().equals("image")){
                    owner_number = i;
                    last_node = new IconNode(((Element)e.getChildNodes().item(i)));
                }
            }
            if(last_node!=null){
                for(int i = 0; i < e.getChildNodes().getLength(); i++){
                   root_node.add(elementIterator2(last_node, (Element)e.getChildNodes().item(i)));
                }
                return root_node;
            }
            else{
                for(int i = 0; i < e.getChildNodes().getLength(); i++){
                   elementIterator2(root_node, (Element)e.getChildNodes().item(i));
                }
                return root_node;
            }
        }
        else return root_node;
    }

    public JTree getTree(){
        return tree;
    }
    public DefaultMutableTreeNode seekNodeByObject(Object object, DefaultMutableTreeNode root){
        DefaultMutableTreeNode found_node = null;

        if(root.getUserObject().equals(object)){
            return root;
        }
        else{
            FOR_loop: for(int i = 0; i < root.getChildCount(); i++){
                found_node = (seekNodeByObject(object, (DefaultMutableTreeNode)root.getChildAt(i)));
                if(found_node!=null) break FOR_loop;
            }
        }
        return found_node;
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
        else if(ae.getActionCommand().equals("pattern_rename")){
            String new_name = renameDialog(pattern_object.pattern_name, "Dress Component");
                if((new_name!=null)||(new_name!="")){
                    pattern_object.pattern_name = new_name;
                }
        }
        else if(ae.getActionCommand().equals("pattern_display")){
            System.out.println(pattern_object.pattern_name+":");
            svgc.getStringPresentation(pattern_object.front);
        }
        else if(ae.getActionCommand().equals("pattern_save")){
            System.out.println(pattern_object.pattern_name+":");
            ImageRelations ir = new ImageRelations(pattern_object.front, pattern_object.pattern_name, svgc);
            patternPackage pp = new patternPackage();
            pp.setPattern(pattern_object);
            pp.setFrontView(ir.image);
            pp.setRearView(ir.image);
            rt.addPattern(pp);
        }
        else if(ae.getActionCommand().equals("drawing_duplicate")){
            Element duplicate = (Element)selected_element.cloneNode(true);
            duplicate.setAttribute("id", "copy_of_"+selected_element.getAttribute("id"));
            ElementLocalizer el = new ElementLocalizer((Element)selected_element.getParentNode().getParentNode(), duplicate, rt.svgF, rt.svgR);
//            station_element.appendChild(el.container);
            selected_element.getParentNode().getParentNode().appendChild(el.container);
            DefaultMutableTreeNode current_node = (DefaultMutableTreeNode)selPath.getLastPathComponent();
            ((DefaultTreeModel)tree.getModel()).insertNodeInto(new IconNode(duplicate), (IconNode)current_node.getParent(), current_node.getParent().getIndex(current_node)+1);
            tree.repaint();

        }
        else if(ae.getActionCommand().equals("drawing_rename")){
            if(selected_element.getLocalName().equals("path")){
                String new_name = renameDialog(selected_element.getAttribute("id"), "drawing");
                if((new_name!=null)||(new_name!="")){
                    selected_element.setAttribute("id", new_name);
                }
            }
            else if(selected_element.getLocalName().equals("svg")){
                String new_name = renameDialog(selected_element.getAttribute("id"), "container");
                if((new_name!=null)||(new_name!="")){
                    selected_element.setAttribute("id", new_name);
                }
            }
        }
    }

    public void mouseClicked(MouseEvent e){
        selPath = tree.getPathForLocation(e.getX(), e.getY());
        if(selPath!=null){
            tree.setSelectionPath(selPath);
            DefaultMutableTreeNode clicked_node = (DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent();
            try{
                Element clicked_element = (Element)clicked_node.getUserObject();
                svgc.setSelectedDrawing(clicked_element);
                svgc.refresh();
                System.out.println("This node has: "+clicked_node.getUserObject());
                if(clicked_element.getOwnerDocument().isSameNode(rt.svgF.document)){
                    System.out.println("This element is from the front document.");
                }
                if(clicked_element.getOwnerDocument().isSameNode(rt.svgR.document)){
                    System.out.println("This element is from the rear document.");
                }
            }
            catch(Exception e2){
                System.out.println("Not an element"+e2);
            }            
        }
    }
    public void mousePressed(MouseEvent e) {
        selPath = tree.getPathForLocation(e.getX(), e.getY());
        tree.setSelectionPath(selPath);
    }

    public void mouseReleased(MouseEvent e){
        if(e.isPopupTrigger()){
            selPath = tree.getSelectionPath();
            try{
                projectObject project_object = (projectObject)((DefaultMutableTreeNode)selPath.getLastPathComponent()).getUserObject();
                project_element_popup.show(tree, e.getX(), e.getY());
            }
            catch(Exception e1){
                try{
                        Element element = (Element)(((DefaultMutableTreeNode)selPath.getLastPathComponent()).getUserObject());
                        selected_element = element;

                        if(element.getParentNode()==null){
                            System.out.println("This is a history element");
                            history_element_popup.show(tree, e.getX(), e.getY());
                        }
                        else{
                            drawing_element_popup.show(tree, e.getX(), e.getY());
                        }

                    if(((DefaultMutableTreeNode)selPath.getLastPathComponent()).getUserObject().equals("Pattern History"))
                        System.out.println("The pattern History node.");
                }
                catch(Exception e2){
                    try{
                        pattern_object = (patternObject)((DefaultMutableTreeNode)selPath.getLastPathComponent()).getUserObject();
                        System.out.println("Pattern object: "+pattern_object.pattern_name);
                        pattern_object_popup.show(tree, e.getX(), e.getY());
                    }
                    catch(Exception e3){
                        try{
                            System.out.println("Try 4");
                        }
                        catch(Exception e4){

                        }
                        System.out.println("The component could not be casted: "+e3);
                        System.out.println("As of here: "+(((DefaultMutableTreeNode)selPath.getLastPathComponent()).getChildAt(0)));
                    }
                }
            }
        }
    }

    public void mouseEntered(MouseEvent e){

    }
    public void mouseExited(MouseEvent e) {
        }
    private String renameDialog(String element, String current_name){
        JTextField new_name_field = new JTextField();

        String new_name_label = "Enter the name for this "+element;

        int ans = JOptionPane.showOptionDialog(
         null,
         new Object[] {new_name_field, new_name_label}, "Rename "+current_name, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if(ans == 0)
           return new_name_field.getText();
        else return null;
    }
}
