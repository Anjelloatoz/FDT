package Design_tool_prototype1;

import java.io.Serializable;
import java.awt.*;
import java.awt.image.*;

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
