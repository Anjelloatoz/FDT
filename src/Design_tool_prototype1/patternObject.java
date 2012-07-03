package Design_tool_prototype1;

import org.w3c.dom.Element;
import java.util.ArrayList;

public class patternObject {
    String pattern_name = "Untitled pattern";
    Element front;
    Element rear;

    patternObject(Element front, Element rear, String name){
        this.front = front;
        this.rear = rear;
        this.pattern_name = name;
    }
    patternObject(){

    }
}
