package Design_tool_prototype1;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

public class fontTest {
    String msg = "My little font test";

    fontTest(){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.getAllFonts();
        Font[] fnt= ge.getAllFonts();
        for(int i = 0; i < fnt.length; i++){
            System.out.println(i+". "+fnt[i].getFontName());
        }
    }

    public static void main(String args[]){
        fontTest ft = new fontTest();
    }
}
