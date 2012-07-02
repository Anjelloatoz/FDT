package Design_tool_prototype1;

import org.w3c.dom.Document;
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
}
