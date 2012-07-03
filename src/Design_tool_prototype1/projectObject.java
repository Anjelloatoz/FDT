package Design_tool_prototype1;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.util.ArrayList;

public class projectObject {
    String project_name = "Untitled project";
    Document front_document;
    Document rear_document;

    ArrayList<patternObject> patterns = new ArrayList();

    projectObject(Document front, Document rear){
        this.front_document = front;
        this.rear_document = rear;
    }

    public void addPatternObject(patternObject new_pattern){
        patterns.add(new_pattern);
    }

    public void removePatternObject(patternObject old_pattern){
        patterns.remove(old_pattern);
    }

    public patternObject removePatternByElement(Element e){
        System.out.println("%%%%%%%%%%%%%%%%%%% "+e.getAttribute("id"));
        for(int i = 0; i < patterns.size(); i++){
            if(patterns.get(i).front.equals(e)){
                System.out.println("Element: "+patterns.get(i).front.getAttribute("id"));
                System.out.println("One object FOUND");
                patternObject removed_pattern = patterns.get(i);
                removePatternObject(patterns.get(i));
                return removed_pattern;
            }
            else return null;
        }
        return null;
    }
}
