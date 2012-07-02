package Design_tool_prototype1;

import org.apache.batik.swing.JSVGCanvas;
/**
 *
 * @author Anjello
 */
public class LJSVGCanvas extends JSVGCanvas{
    public void displayError(String message){
        System.out.println("In the displayError: "+message);
    }
    public void displayError(Exception ex){
        System.out.println("In the displayError: "+ex);
    }
    public void displayMessage(String message){
        System.out.println("In the displayError: "+message);
    }
}
