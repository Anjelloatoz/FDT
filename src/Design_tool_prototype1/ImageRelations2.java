package Design_tool_prototype1;

import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.File;

public class ImageRelations2 {
    String file_uri = "";
    ImageRelations2(BufferedImage sent_image, String name){
        File image_output_file = new File("Temp\\"+name+" TEST");
        try{
            ImageIO.write(sent_image, "png", image_output_file);
        }
        catch(Exception e){
            System.out.println("ImageRelations2 Exception: "+e);
        }
        file_uri = image_output_file.toURI().toString();
    }

    public String getUri(){
        return file_uri;
    }
}
