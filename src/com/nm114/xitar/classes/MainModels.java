/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nm114.xitar.classes;

import java.io.*;
import java.util.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

/**
 *
 * @author brgd
 */
public class MainModels {
    public static  Boolean INITIALIZED = false; //Just for call from initialize servlet

    private static final Document _XML_BUILDING_STATUS = setXMLStatus();

    public static Document setXMLStatus(){
        Document doc = MainUtil.getXMLDocument("");
        Building.getStatusXML(doc,true);
        return doc;
    }

    public static Document getBuildingXMLStatus(){
        return _XML_BUILDING_STATUS;
    }

    public static String getXMLStatus(){

        String xmlString = "";

        try{
            Document doc = _XML_BUILDING_STATUS;

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

    public static synchronized void addsOnePlayerToRoom(int roomid){
        NodeList listOfPlayers = _XML_BUILDING_STATUS.getElementsByTagName(CONFIG.XML_PLAYERS_ELEMENT);
        
        int players = listOfPlayers.getLength();
        for(int s = 0; s < players ; s++){
            Node firstPlayersNode = listOfPlayers.item(s);
            if (firstPlayersNode.getNodeType() == Node.ELEMENT_NODE) {
                Node parentNode = firstPlayersNode.getParentNode();
                while(!parentNode.getNodeName().equals(CONFIG.XML_NODE_ROOM)){
                    parentNode = parentNode.getParentNode();
                }
                Element roomNode = (Element)parentNode;
                if(Integer.parseInt(roomNode.getAttribute(CONFIG.ID).trim()) == roomid){
                    int pn = Integer.parseInt(firstPlayersNode.getTextContent().trim());
                    firstPlayersNode.setTextContent(String.valueOf(++pn));
                }
            }
        }
        //System.out.println(getXMLStatus());
    }

    public static synchronized void deletesOnePlayerToRoom(int roomid){
        NodeList listOfPlayers = _XML_BUILDING_STATUS.getElementsByTagName(CONFIG.XML_PLAYERS_ELEMENT);

        int players = listOfPlayers.getLength();
        for(int s = 0; s < players ; s++){
            Node firstPlayersNode = listOfPlayers.item(s);
            if (firstPlayersNode.getNodeType() == Node.ELEMENT_NODE) {
                Node parentNode = firstPlayersNode.getParentNode();
                while(!parentNode.getNodeName().equals(CONFIG.XML_NODE_ROOM)){
                    parentNode = parentNode.getParentNode();
                }
                Element roomNode = (Element)parentNode;
                int tid = Integer.parseInt(roomNode.getAttribute(CONFIG.ID).trim());
                if((tid == roomid) && (tid > 0)){
                    int pn = Integer.parseInt(firstPlayersNode.getTextContent().trim());
                    firstPlayersNode.setTextContent(String.valueOf(--pn));
                }
            }
        }
    }


}
