/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nm114.xitar.classes;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import org.xml.sax.InputSource;
import javax.xml.parsers.*;

import java.io.*;
import javax.servlet.http.*;

/**
 *
 * @author brgd
 */
public class MainUtil {

    public static boolean test(String val) {
        System.out.println(val);
        return true;
    }

    private static void sendError(HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        out.write(CONFIG.NO_ACESS_RIGHT);
        out.close();
    }

    public static int getPlayerStage(HttpServletRequest req) {
        int se = CONFIG.INIT_STAGE;
        HttpSession hs = req.getSession(false);

        try{
            if(hs.getAttribute(CONFIG.STAGE) != null){
                se = (Integer)hs.getAttribute(CONFIG.STAGE);
            }
            Player player = (Player)hs.getAttribute(CONFIG.PLAYERNAME);
            se = player.getCurrentStage();
        }catch(Exception e){}

        return se;
    }

    public static Document getXMLDocument(String url) {
        Document doc = null;

        try{
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setValidating(true);
            docFactory.setNamespaceAware(true);
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            docBuilder.setErrorHandler(new XMLSimpleErrorHandler());


            if(url.equals("")){
                doc = docBuilder.newDocument();
            }else{
                doc = docBuilder.parse(new File(url));
            }

        }catch (Exception e) {
            System.out.println(e);
        }
       
       return doc;
    }

    public static Document convertStringToXML(String s) {
        Document doc = null;

        try{
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            //docFactory.setValidating(true);
            docFactory.setNamespaceAware(true);
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            docBuilder.setErrorHandler(new XMLSimpleErrorHandler());

            doc =docBuilder.parse(new InputSource(new StringReader(s)));

        }catch (Exception e) {
            System.out.println(e);
        }

       return doc;
    }

    public static class XMLSimpleErrorHandler implements ErrorHandler {
        public void warning(SAXParseException e) throws SAXException {
            System.out.println("MainUtil - SimpleErrorHandler - warning()");
            System.out.println(e.getMessage());
        }

        public void error(SAXParseException e) throws SAXException {
            System.out.println("MainUtil - SimpleErrorHandler - error()");
            System.out.println(e.getMessage());
        }

        public void fatalError(SAXParseException e) throws SAXException {
            System.out.println("MainUtil - SimpleErrorHandler - fatalError()");
            System.out.println(e.getMessage());
        }
    }

    public static String setXMLToString(Document doc){

        String xmlString = "";

        try{
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();

            //trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            //create string from xml tree
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);
            xmlString = sw.toString();
        }catch (Exception e) {
            System.out.println(e);
        }

        return xmlString;
    }
}
