package Design_tool_prototype1;

import java.awt.*;
import java.net.URI;
import java.awt.image.*;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.dom.svg.SVGLocatableSupport;
import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.DOMImplementation;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import java.io.ByteArrayOutputStream;
import org.apache.batik.util.Base64EncoderStream;
import org.apache.batik.ext.awt.image.codec.util.ImageEncoder;
import org.apache.batik.ext.awt.image.codec.png.PNGImageEncoder;
import org.w3c.dom.Element;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import java.awt.Rectangle;
import org.w3c.dom.svg.SVGRect;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;

import java.io.OutputStream;
import java.io.FileOutputStream;
import javax.imageio.*;
import java.io.*;
import org.apache.batik.dom.util.DOMUtilities;

public class ImageRelations {
    JSVGCanvas canvas;
    Document document;
    private final String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
    static final String XLINK_NAMESPACE_URI = "http://www.w3.org/1999/xlink";
    String uri = "";
    BufferedImage image;

    ImageRelations(Element element, String name, SVGConjurer svgc){
        Element local_element = (Element)element.cloneNode(true);

        SVGRect rect = svgc.BoundryFinder(element, null);

        elementProcessor(local_element, rect);

        this.InitializeDocument();
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
        CharArrayWriter tmp_character_array = new CharArrayWriter();
        String tmp = "";
        try{
            DOMUtilities.writeNode(local_element, tmp_character_array);
            tmp = tmp_character_array.toString();
        }

        catch(Exception e){
            System.out.println("ImageRelations exception: "+e);
        }

        Element parsed_element = null;

        try{
            Node ef = DOMUtilities.parseXML(tmp, document,canvas.getURI(), null, null,f);
            Element e = document.createElementNS(svgNS, "svg");
            e.appendChild(ef);
            parsed_element = e;
        }
        catch(Exception ee){
            System.out.println("Read exception is: "+ee);
        }

        
        Rectangle rectangle = new Rectangle();
        rectangle.setRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

        float dimension_rationale = 0;
        
        createImage(parsed_element, rectangle.width, rectangle.height,rectangle, name);
    }

    ImageRelations(BufferedImage sent_image, String name){
        final String DATA_PROTOCOL_PNG_PREFIX = "data:image/png;base64,";
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        Base64EncoderStream b64Encoder = new Base64EncoderStream(os);
        ImageEncoder encoder = new PNGImageEncoder(b64Encoder, null);
        try{
            encoder.encode(sent_image);
            b64Encoder.close();
        }
        catch(Exception ec){
            System.out.println("ImageRelations"+ec);
            System.out.println("ImageRelations");
        }

        Element fill_image = document.createElementNS(svgNS, "image");
        fill_image.setAttributeNS(XLINK_NAMESPACE_URI, "xlink:href", DATA_PROTOCOL_PNG_PREFIX+os.toString());
        fill_image.setAttributeNS(null, "x", "0");
        fill_image.setAttributeNS (null, "y", "0");
        fill_image.setAttributeNS(null, "width", +sent_image.getWidth()+"mm");
        fill_image.setAttributeNS(null, "height", +sent_image.getHeight()+"mm");
        this.InitializeDocument();
        createImage(fill_image, sent_image.getWidth(), sent_image.getHeight(), null, name);
    }
    private void InitializeDocument(){
        DOMImplementation dom = SVGDOMImplementation.getDOMImplementation();
        canvas = new JSVGCanvas();
        document = dom.createDocument (svgNS, "svg", null);
        canvas.setDocument(document);
    }

    private void createImage(Element element, float width, float height, java.awt.Rectangle rect, String name){

        Element root = document.getDocumentElement();
        root.appendChild(element);

        try{
            
            File file = new File("Temp\\"+name+".jpg");
            OutputStream ostream = new FileOutputStream(file);
            Transcoder t = new JPEGTranscoder();
            t.addTranscodingHint (JPEGTranscoder. KEY_QUALITY, new Float(.8));
            t.addTranscodingHint(JPEGTranscoder.KEY_WIDTH, width);
            t.addTranscodingHint(JPEGTranscoder.KEY_HEIGHT, height);
            t.addTranscodingHint(JPEGTranscoder.KEY_AOI, rect);
            TranscoderInput input = new TranscoderInput (canvas.getSVGDocument());
            TranscoderOutput output = new TranscoderOutput (ostream);
            t.transcode (input, output);
            ostream.flush();
            ostream.close();
            System.out.println("The uri of this image is: "+file.toURI());
            System.out.println("The url of this image is: "+file.toURI().toURL());
            uri = file.toURI().toString();
            File fl = file.getAbsoluteFile();
            System.out.println("Now The file name is: "+fl.getName());
            URI ur = new URI(uri);
            image = ImageIO.read(file);
            ImageRelations2 ir = new ImageRelations2(ImageIO.read(file), "Test");
        }

        catch(Exception ex1){
            System.out.println("Image Relations class: Output stream error"+ex1);
        }
    }

    private SVGRect elementProcessor(Element element, SVGRect rect){
        if(element.getLocalName().equals("svg")){
            for(int i = 0; i < element.getChildNodes().getLength(); i++){
                rect = elementProcessor((Element)element.getChildNodes().item(i), rect);
            }
        }
        else{
            if(element.getLocalName().equals("path")){
                element.setAttribute("fill", "white");
                element.setAttribute("stroke-width", "10");
            }
/*            System.out.println("In the Image relations the element is a "+element.getTagName());
            System.out.println("In the Image relations the element is a "+element.getAttribute("id"));
            System.out.println("In the Image relations the element is a "+element.getAttribute("d"));
/*
            SVGLocatableSupport ls = new SVGLocatableSupport();
            SVGRect local_rect = ls.getBBox(element);
            System.out.println("The x is "+local_rect.toString());

            float local_x1 = local_rect.getX();
            float local_y1 = local_rect.getY();
            float local_x2 = local_rect.getX()+local_rect.getWidth();
            float local_y2 = local_rect.getY()+local_rect.getHeight();

            float rect_x1 = rect.getX();
            float rect_y1 = rect.getY();
            float rect_x2 = rect.getX()+rect.getWidth();
            float rect_y2 = rect.getY()+rect.getHeight();

            if(local_x1<rect_x1){
                rect.setX(local_x1);
            }
            if(local_y1<rect_y1){
                rect.setY(local_y1);
            }
            if(local_x2 > rect_x2){
                rect.setWidth(local_x2-rect_x1);
            }
            if(local_y2 > rect_y2){
                rect.setHeight(local_y2-rect_y1);
            }*/
        }
        return rect;
    }

    public String getUri(){
        return uri;
    }
}
