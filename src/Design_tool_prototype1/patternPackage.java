package Design_tool_prototype1;

import java.io.Serializable;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.net.URI;
import java.awt.*;
import java.awt.image.*;
import org.w3c.dom.Element;
import java.util.List;
import java.util.*;

import org.apache.batik.dom.util.DOMUtilities;

public class patternPackage implements Serializable{
    pattern ptrn;
    private int[] pattern_front_view;
    private int[] pattern_rear_view;
    int front_width;
    int front_height;
    int rear_width;
    int rear_height;

    public void setFrontView(Image image){
        this.front_width = image.getWidth(null);
        this.front_height = image.getHeight(null);
        this.pattern_front_view = new int[front_width*front_height];
        imageToArray(image, front_width, front_height, pattern_front_view);
    }
    public void setRearView(Image image){
        this.rear_width = image.getWidth(null);
        this.rear_height = image.getHeight(null);
        this.pattern_rear_view = new int[rear_width*rear_height];
        imageToArray(image, rear_width, rear_height, pattern_rear_view);
    }

    public void setPattern(patternObject pattern_object){
        this.ptrn = new pattern();
        ptrn.pattern_name = pattern_object.pattern_name;
        ptrn.front = this.getTextFromNode(patternCleaner(pattern_object.front));
//        populatePatternFills(pattern_object.associated_fabrics, ptrn.fills, readFabricsList());

    }

    private Element patternCleaner(Element element){
        if(element.getLocalName().equals("svg")){
            for(int i = 0; i < element.getChildNodes().getLength(); i++){
                patternCleaner((Element)element.getChildNodes().item(i));
            }
        }
        else{
            if(element.getLocalName().equals("path")){
                element.setAttribute("fill", "green");
            }
        }
        return element;
    }

    private String getTextFromNode(Element element){
        CharArrayWriter character_array = new CharArrayWriter();
        try{
            DOMUtilities.writeNode(element, character_array);
        }

        catch(Exception e){
            System.out.println("Pattern Package getTextFromNode exception: "+e);
        }

        return character_array.toString();
    }

    private ArrayList readFabricsList(){

        ArrayList fabrics_list = new ArrayList();
        try{
            ObjectInputStream objIn = new ObjectInputStream(new BufferedInputStream(new FileInputStream("fabrics.dat")));
            fabrics_list = (ArrayList)(objIn.readObject());
            objIn.close();
        }
        catch(Exception e){
            System.out.println("Fabrics list import error at patternPackage "+e);
        }

        return fabrics_list;
    }

    private void populatePatternFills(List associated_fabrics, List pattern_fills, List<Fabric> fabrics_list){
        for(int j = 0; j < associated_fabrics.size(); j++){
        try{
            File f = new File(((Element_fill)associated_fabrics.get(j)).fill_uri);

            for(int x = 0; x < fabrics_list.size(); x++){
                if(fabrics_list.get(x).getFabricNameLong().equals(f.getAbsoluteFile().getName())){
                    System.out.println("Relevant fabric found at index: "+x);
                    pattern_fills.add(fabrics_list.get(x).getFabricNameLong());
                }
            }
        }
        catch(Exception e3){
            System.out.println("The exception in ribbon test");
        }
    }
    }

    private void imageToArray(Image image, int width, int height, int [] array){
        PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height, array, 0, width);
        try{
            pg.grabPixels();
        }
        catch(Exception e){
            System.out.println("patternPackage.java pixelgrabber error: "+e);
        }
    }

    private Image imageFromArray(int width, int height, int[] image_array) {
	MemoryImageSource mis = new MemoryImageSource(width, height, image_array, 0, width);
	Toolkit tk = Toolkit.getDefaultToolkit();
	return tk.createImage(mis);
    }

    private BufferedImage createBufferedImage(Image source){
        BufferedImage buffImage = new BufferedImage(source.getWidth(null), source.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = buffImage.getGraphics();
        g.drawImage(source, 0, 0, null);
        return buffImage;
    }

    public BufferedImage getPatternFrontImage(){
        return this.createBufferedImage(this.imageFromArray(front_width, front_height, pattern_front_view));
    }

    public BufferedImage getPatternRearImage(){
        return this.createBufferedImage(this.imageFromArray(rear_width, rear_height, pattern_rear_view));
    }
}
