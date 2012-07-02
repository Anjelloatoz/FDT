package Design_tool_prototype1;

import org.w3c.dom.Element;
import java.util.ArrayList;
import java.io.Serializable;


public class pattern implements Serializable{
    String pattern_name = "Untitled pattern";
    String front;
    String rear;

    pattern(String front, String rear, String name){
        this.front = front;
        this.rear = rear;
        this.pattern_name = name;
    }
    pattern(){
        
    }
}
