package Design_tool_prototype1;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.util.*;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import javax.swing.plaf.metal.MetalIconFactory;


public class treeHandler {
    JTree tree;
    projectObject project;

    public treeHandler(projectObject project){
        this.project = project;
        IconNode project_node = new IconNode(project.project_name);
        project_node.setIcon(MetalIconFactory.getFileChooserHomeFolderIcon());

        IconNode[] pattern_nodes = new IconNode[project.patterns.size()];
        for(int i = 0; i < pattern_nodes.length; i++){
            pattern_nodes[i] = new IconNode(project.patterns.get(i));
            project_node.add(pattern_nodes[i]);
        }

        IconNode[] front_nodes = new IconNode[project.patterns.size()];
        IconNode[] rear_nodes = new IconNode[project.patterns.size()];
        for(int i = 0; i < pattern_nodes.length;){
            front_nodes[i] = new IconNode(project.patterns.get(i).front);
            rear_nodes[i] = new IconNode(project.patterns.get(i).rear);

            pattern_nodes[i].add(front_nodes[i]);
            pattern_nodes[i].add(rear_nodes[i]);

            front_nodes[i].add(elementIterator(project.patterns.get(i).front));
            rear_nodes[i].add(elementIterator(project.patterns.get(i).rear));
        }
        tree = new JTree(project_node);
    }

    private IconNode elementIterator(Element e){
        IconNode element_node = new IconNode(e);
        element_node.setIconName(e.getAttribute("id"));
        if(e.hasChildNodes()){
            NodeList node_list = e.getChildNodes();
            for(int i = 0; i < node_list.getLength();i++){
                element_node.add(elementIterator((Element)node_list.item(i)));
            }
            return element_node;
        }
        else return element_node;
    }

    public JTree getTree(){
        return tree;
    }
}
