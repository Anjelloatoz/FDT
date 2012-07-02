package Design_tool_prototype1;

import org.w3c.dom.Element;
import java.util.ArrayList;
import java.io.Serializable;

public class patternObject implements Serializable{
    String pattern_name = "Untitled pattern";
    Element front;
    Element rear;
    ArrayList<Element_fill> associated_fabrics = new ArrayList();

    patternObject(Element front, Element rear, String name){
        this.front = front;
        this.rear = rear;
        this.pattern_name = name;
    }
    patternObject(){

    }
}
