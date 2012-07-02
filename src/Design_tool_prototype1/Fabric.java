package Design_tool_prototype1;
import java.io.Serializable;
import java.awt.*;
import java.net.URI;
import java.awt.image.PixelGrabber;
import java.awt.image.*;
import javax.swing.ImageIcon;

public class Fabric implements Serializable{
    private String fabric_name_short;
    private String fabric_name_long;
    private String fabric_type;
    private int[] fabric_image;
    int width;
    int height;

    public Fabric(String fabric_name_short, String fabric_name_long, String fabric_type, Image sent_image){
        System.out.println("Fabric Constructor called");
        this.width = sent_image.getWidth(null);
        this.height = sent_image.getHeight(null);
        this.fabric_name_short = fabric_name_short;
        this.fabric_name_long = fabric_name_long;
        this.fabric_type = fabric_type;
        this.fabric_image = new int[width*height];

        PixelGrabber pg = new PixelGrabber(sent_image, 0, 0, width, height, fabric_image, 0, width);

        try{
            pg.grabPixels();
        }
        catch(Exception e){
            System.out.println("Fabric.java pixelgrabber error: "+e);
        }
    }
    
private Image getImageFromArray() {
	MemoryImageSource mis = new MemoryImageSource(width, height, fabric_image, 0, width);
	Toolkit tk = Toolkit.getDefaultToolkit();
	return tk.createImage(mis);
}

private BufferedImage createBufferedImage(Image source){

            BufferedImage buffImage = new BufferedImage(source.getWidth(null), source.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics g = buffImage.getGraphics();
            g.drawImage(source, 0, 0, null);
            return buffImage;
        }

    public String getFabricNameShort(){
        return this.fabric_name_short;
    }

    public String getFabricNameLong(){
        return this.fabric_name_long;
    }

    public String getFabricType(){
        return this.fabric_type;
    }

    public Image getFabricIcon(){
        return this.getImageFromArray();
    }

    public BufferedImage getFabricMainImage(){
        return this.createBufferedImage(this.getImageFromArray());
    }
}
