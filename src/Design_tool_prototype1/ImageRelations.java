package Design_tool_prototype1;

import java.awt.*;
import java.awt.image.*;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.Document;
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

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.*;

public class ImageRelations {
    private SVGGraphics2D generator;
    JSVGCanvas canvas;
    Document document;
    private final String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
    static final String XLINK_NAMESPACE_URI = "http://www.w3.org/1999/xlink";

    ImageRelations(BufferedImage sent_image, String name){
        DOMImplementation dom = SVGDOMImplementation.getDOMImplementation();
        canvas = new JSVGCanvas();
        canvas.setMySize(new Dimension(sent_image.getWidth(), sent_image.getHeight()));
        canvas.setBounds(0, 0, sent_image.getWidth(), sent_image.getHeight());
        document = dom.createDocument (svgNS, "svg", null);
        generator = new SVGGraphics2D (document);
        canvas.setDocument(document);

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
        fill_image.setAttributeNS(null, "width", ""+sent_image.getWidth());
        fill_image.setAttributeNS(null, "height", ""+sent_image.getHeight());

        Element root = document.getDocumentElement();
        root.appendChild(fill_image);

        //----------------------------//
        try{
            File file = new File("Temp\\"+name);
            OutputStream ostream = new FileOutputStream(file);
            Transcoder t = new JPEGTranscoder();
            t.addTranscodingHint (JPEGTranscoder. KEY_QUALITY, new Float(.8));
            TranscoderInput input = new TranscoderInput (canvas.getSVGDocument());
            TranscoderOutput output = new TranscoderOutput (ostream);
            t.transcode (input, output);
            ostream.flush();
            ostream.close();
            System.out.println("The uri of this image is: "+file.toURI());
        }

        catch(Exception ex1){
            System.out.println("Image Relations class: Output stream error");
        }
    }
}
