package Design_tool_prototype1;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import java.io.CharArrayWriter;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;

public class ElementLocalizer{
    Element parent;
    Element container;

    ElementLocalizer(Document guest_document, Element station, Element guest, SVGConjurer svgF, SVGConjurer svgR){
        SVGConjurer station_svgc = null;
        if(station.getOwnerDocument().isSameNode(svgF.document)){
            System.out.println("Station is from the svgF");
            station_svgc = svgF;
        }
        if(station.getOwnerDocument().isSameNode(svgR.document)){
            System.out.println("Station is from the svgR");
            station_svgc = svgR;
        }
        converter(guest_document, station_svgc, (Element)guest.cloneNode(true), station.getOwnerDocument());
        System.out.println("Converter called from the new constructor");
    }

    ElementLocalizer(Element station, Element guest, SVGConjurer svgF, SVGConjurer svgR){
        parent = station;
        Document station_doc = station.getOwnerDocument();
        Document guest_doc = guest.getOwnerDocument();
        SVGConjurer station_svgc = null;

        if(station_doc.isSameNode(svgF.document)){
            System.out.println("Station is from the svgF");
            station_svgc = svgF;
        }
        if(station_doc.isSameNode(svgR.document)){
            System.out.println("Station is from the svgR");
            station_svgc = svgR;
        }
        if(guest_doc.isSameNode(svgF.document)){
            System.out.println("Guest is from the svgF");
        }
        if(guest_doc.isSameNode(svgR.document)){
            System.out.println("Guest is from the svgR");
        }

        converter(guest_doc, station_svgc, guest, station_doc);
    }

    public void converter(Document guest_doc, SVGConjurer station_svgc, Element guest, Document station_doc){
        CharArrayWriter tmp_element_holder = new CharArrayWriter();
        try{
            Element tmp_container = guest_doc.createElementNS(station_svgc.svgNS, "svg");
            tmp_container.setAttribute("id", "temporary container");
            tmp_container.setAttribute("x", "0");
            tmp_container.setAttribute("x", "0");
            Element g_element = guest_doc.createElementNS(station_svgc.svgNS, "g");
            g_element.appendChild(guest);
            tmp_container.appendChild(g_element);
            System.out.println("In the converter the guest is "+guest.getAttribute("id"));
            DOMUtilities.writeNode(tmp_container, tmp_element_holder);
//            System.out.println("Element Localizer 65: The element successfully written.- "+tmp_element_holder.toString());
        }
        catch(Exception ex1){
            System.out.println("Element Localizer 37, Element Localizer Exception "+ex1);
        }
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
        Node reborn_node = null;
        container = station_doc.createElementNS(station_svgc.svgNS, "svg");
        container.setAttribute("x", "0");
        container.setAttribute("y", "0");
        try{
            reborn_node = DOMUtilities.parseXML(tmp_element_holder.toString(), station_doc,station_svgc.canvas.getURI(), null, null,f);
            container.appendChild(reborn_node.getFirstChild().getFirstChild());
        }
        catch(Exception ex2){
            System.out.println("Element Localizer: "+ex2);
        }
    }

    public Element getNewContainer(){
        return container;
    }
    public boolean appendChild(){
        boolean success = true;
        try{
            parent.appendChild(container);
        }
        catch(Exception e){
            success = false;
            System.out.println("CHILD APPENDING ERROR: "+e);
        }
        return success;
    }
}
