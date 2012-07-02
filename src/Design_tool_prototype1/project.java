package Design_tool_prototype1;

import java.util.ArrayList;
import java.io.Serializable;

public class project implements Serializable{
    String project_name = "Untitled project";
    String front_document;
    String rear_document;
    String defs;

    ArrayList<pattern> patterns = new ArrayList();
    ArrayList<String> history_elements = new ArrayList();
}
