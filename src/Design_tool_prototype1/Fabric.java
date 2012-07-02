package Design_tool_prototype1;
import java.io.Serializable;
import java.awt.*;
import java.net.URI;

public class Fabric implements Serializable{
    private String fabric_name_short;
    private String fabric_name_long;
    private String fabric_type;
    private URI icon_uri;
    private URI sample_uri;

    public Fabric(String fabric_name_short, String fabric_name_long, String fabric_type, String icon_uri, String sample_uri){
        System.out.println("Fabric Constructor called");
        this.fabric_name_short = fabric_name_short;
        this.fabric_name_long = fabric_name_long;
        this.fabric_type = fabric_type;
        try{
            this.icon_uri = new URI(icon_uri);
            this.sample_uri = new URI(sample_uri);
        }
        catch(Exception e){
            System.out.println("URI creation error in the Fabric class "+ e);
        }
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

    public URI getFabricIcon(){
        return this.icon_uri;
    }

    public URI getFabricMainImage(){
        return this.sample_uri;
    }
}
