package Design_tool_prototype1;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.util.ArrayList;
import java.io.Serializable;

public class projectObject implements Serializable{
    String project_name = "Untitled project";
    Document front_document;
    Document rear_document;

    ArrayList<patternObject> patterns = new ArrayList();
    ArrayList<Element> history_elements = new ArrayList();

    projectObject(Document front, Document rear){
        this.front_document = front;
        this.rear_document = rear;
    }

    projectObject(){
        
    }

    public void addPatternObject(patternObject new_pattern){
        patterns.add(new_pattern);
    }

    public void removePatternObject(patternObject old_pattern){
        patterns.remove(old_pattern);
    }

    public void addHistoryElement(Element old_element){
        history_elements.add(old_element);
        System.out.println("History Elements have "+history_elements.size()+" size.");
    }

    public patternObject removePatternByElement(Element e){
        for(int i = 0; i < patterns.size(); i++){
            if(patterns.get(i).front.equals(e)){
                patternObject removed_pattern = patterns.get(i);
                removePatternObject(patterns.get(i));
                return removed_pattern;
            }        }
        return null;
    }
}
