package Design_tool_prototype1;
import java.io.Serializable;
import java.awt.*;
import java.net.URI;

public class button implements Serializable{
    private String button_name_short;
    private String button_name_long;
    private String button_type;
    private URI icon_uri;
    private URI sample_uri;

    public button(String button_name_short, String button_name_long, String button_type, String icon_uri, String sample_uri){
        System.out.println("button Constructor called");
        this.button_name_short = button_name_short;
        this.button_name_long = button_name_long;
        this.button_type = button_type;
        try{
            this.icon_uri = new URI(icon_uri);
            this.sample_uri = new URI(sample_uri);
        }
        catch(Exception e){
            System.out.println("URI creation error in the button class "+ e);
        }
    }

    public String getbuttonNameShort(){
        return this.button_name_short;
    }

    public String getbuttonNameLong(){
        return this.button_name_long;
    }

    public String getbuttonType(){
        return this.button_type;
    }

    public URI getbuttonIcon(){
        return this.icon_uri;
    }

    public URI getbuttonMainImage(){
        return this.sample_uri;
    }

}
