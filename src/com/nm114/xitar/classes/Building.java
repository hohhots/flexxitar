/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nm114.xitar.classes;

import java.util.*;
import org.w3c.dom.*;

import com.nm114.xitar.exception.*;

/**
 *
 * @author brgd
 */
public class Building {

    private static Room[] _rooms = setRooms();

    private static String _notifyXML = null;

    // <editor-fold defaultstate="collapsed" desc="METHODS.">
    // <editor-fold defaultstate="collapsed" desc="STATIC methods.">
    private static Room[] setRooms() {
        ArrayList<Integer> roomsId = CONFIG.getRoomsId();
        Room[] r = new Room[roomsId.size()];

        try {
            int i = 0;
            for(int id : roomsId){
                r[i] = Room.getInstance(id);
                i++;
            }
        } catch (CreateObjectOutOfLimit e) {
            e.printStackTrace();
        }

        return r;
    }

    public static Boolean setNotifyXML() {
        _notifyXML = null;
        
        Boolean hasChange = false;

        Document doc = MainUtil.getXMLDocument("");

        Element building = doc.createElement(CONFIG.XML_BUILDING_ELEMENT);
        //create child element of building,
        for (int i = 0; i < _rooms.length; i++) {
            if(_rooms[i].getBuildingNotifyXML(doc, building)){
                hasChange = true;
            }
        }
        
        if(hasChange){
            Element root = doc.createElement(CONFIG.XML_ROOT_ELEMENT);
            root.appendChild(building);
            doc.appendChild(root);

            _notifyXML = MainUtil.setXMLToString(doc);
        }

        return hasChange;
    }

    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="INITIAL methods.">
    private Building() {//don't allow to create object
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="GET methods.">
    public static Room[] getRooms() {
        return _rooms;
    }

    public static Room getRoom(int id) {
        for(Room r : _rooms){
            if(r.getId() == id){
                return r;
            }
        }

        return null;
    }

    public static int getRoomNum() {
        return _rooms.length;
    }

    public static String getNotifyXML(Player player) {
        Document doc = MainUtil.convertStringToXML(_notifyXML);

        Element tplayer = player.getBuildingNotifyXML(doc);

        NodeList listOfBuildings = doc.getElementsByTagName(CONFIG.XML_BUILDING_ELEMENT);
        int bs = listOfBuildings.getLength();
        for(int s = 0; s < bs ; s++){
            Node firstBuildingNode = listOfBuildings.item(s);
            if (firstBuildingNode.getNodeType() == Node.ELEMENT_NODE) {
                firstBuildingNode.getParentNode().insertBefore(tplayer, firstBuildingNode);
            }
        }
        return MainUtil.setXMLToString(doc);
    }

    public static void getBuildingInfo(Document doc, Element root, Player player) {

        Element building = doc.createElement(CONFIG.XML_BUILDING_ELEMENT);
        //create child element of building,
        for (int i = 0; i < _rooms.length; i++) {
            _rooms[i].getRoomInfo(doc,building,player);
        }
        root.appendChild(building);
    }

    public static void getStatusXML(Document doc,boolean child) {
        Element building = doc.createElement(CONFIG.XML_BUILDING_ELEMENT);
        //create child element of building,
        for (int i = 0; i < _rooms.length; i++) {
            _rooms[i].getXMLStatus(doc,building,child);
        }
        doc.appendChild(building);
    }
    //</editor-fold>
    //</editor-fold>
}
