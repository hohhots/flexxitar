/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nm114.xitar.classes;

import java.util.*;
import org.w3c.dom.*;
 
import java.net.*;

/**
 *
 * @author brgd
 */
public class CONFIG {

    // <editor-fold defaultstate="collapsed" desc="VARIABLES.">
    // <editor-fold defaultstate="collapsed" desc="STATIC variables.">
    public static final Boolean DEBUG = false;
    public static final String PASSWORD = "1";
    public static final String NO_SESSION = "NoSession";
    public static final String ID = "id";

    //
    public static final int XITAR_WHITE = 1; //white side xitar id
    public static final int XITAR_BLACK = 0; //black side xitar id
    public static final long PLAYER_RESTRICT_TIME = 10000; //10 second
    //

    public static final int ACTION_BUILDING = 0;
    public static final int ACTION_ROOM = 1;
    public static final int ACTION_DESK = 2;
    public static final int ACTION_XITAR_LASTSTEP = 3;
    public static final int ACTION_XITAR_EXIT = 4;
    public static final int ACTION_XITAR_END = 5;
    //
    public static final String STAGE       = "stage";
    public static final int INIT_STAGE     = -1; //player current position
    public static final int ERROR_STAGE    = 0;  //reference client side config
    public static final int LOGIN_STAGE    = 1;
    public static final int BUILDING_STAGE = 2;
    public static final int ROOM_STAGE     = 3;
    public static final int DESK_STAGE     = 4;
    public static final int BOARD_STAGE    = 5;

    public static final String WEB_INF = "WEB-INF";
    public static final String REMOTEADDR = "remoteAddr";
    public static final String USERAGENT = "user-agent";
    public static final String REFRESH = "refresh";
    public static final String ENDGAME = "endgame";
    public static final String NO_ACESS_RIGHT = "error";
    public static final String BUILDING_VAR = "building";
    public static final String PLAYERS_VAR = "players";
    public static final String ROOMNUM_CLASS = "room_num";
    public static final String PLAYERS_CLASS = "players";
    public static final String PLAYERNAME = "player";
    public static final int BLACK_USER = 0;
    public static final int WHITE_USER = 1;
    public static final int CELLS_NUM = 64;
    public static final int CHESS_NUM = 32;
    public static final int chessTypeNum = 6;
    public static final String ROOM_DESK_SEPERATOR = "d";

    public static final String DO_URL              = "/do";
    public static final String SESSION_SERVLET_URL = DO_URL + "/session";
    public static final String DATA_SERVLET_URL    = DO_URL + "/data";
    public static final String LOGIN_SERVLET_URL   = DO_URL + "/login";
    public static final String EXIT_SERVLET_URL    = DO_URL + "/exit";
    public static final String REFRESH_SERVLET_URL = DO_URL + "/refresh";

    public static final String GET_DATA_ERROR = "GET_DATA_ERROR";

    public static final String CONFIG_DIR           = "config/";
    public static final String XML_CONFIG_FILE      = "building_config.xml";
    public static final String XML_NODE_ROOM        = "room";
    public static final String XML_NODE_DESKS       = "desks";
    public static final String XML_NODE_DESKS_NUM   = "desknum";
    
    public static final String XML_BUILDING_STATUS       = "XML_BUILDING_STATUS";
    public static final String XML_ROOT_ELEMENT          = "root";
    public static final String XML_BUILDING_ELEMENT      = "building";
    public static final String XML_ROOM_ELEMENT          = "room";
    public static final String XML_DESK_ELEMENT          = "desk";
    public static final String XML_DESKS_ELEMENT         = "desks";
    public static final String XML_PLAYER_ELEMENT        = "player";
    public static final String XML_PLAYERS_ELEMENT       = "players";
    public static final String XML_PLAYER1_ELEMENT       = "player1";
    public static final String XML_PLAYER2_ELEMENT       = "player2";

    public static final Document  BUILDING_CONFIG_DOC = setXMLDocs();
    public static final int       ALL_DESKS_NUM       = setAllDeskNum();
    public static final int       ALL_ROOMS_NUM       = setAllRoomNum();
    public static final HashMap   DESKS_NUM_OF_ROOMS  = setDesksNumOfRooms();
    public static final int       ALL_PLAYERS         = 1000;
    public static final int       USERID_STARTNUM     = 2000; //user id start from this
    public static final int     COMET_EXPIRATIONDELAY = 60000; //60 seconds,must smaller than client expired time
    public static final int      COMET_NOTIFY_PERIOD  = 3000; //3 seconds
    public static final int       LOGIN_INTERVAL      = 3000; //3 seconds

    


    // </editor-fold>
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="METHODS.">
    // <editor-fold defaultstate="collapsed" desc="INITIAL methods.">
    private CONFIG() {} //don't allow to create object
    //</editor-fold>
    // <editor-fold defaultstate="collapsed" desc="STATIC methods.">
    public static int getUsersNum() {
        int ps = ALL_PLAYERS;
        if((ALL_DESKS_NUM*2) > ps){
            ps = ALL_DESKS_NUM*2;
        }
        return ps;
    }

    public static int getAllCellNum() {
        return ALL_DESKS_NUM * CELLS_NUM;
    }

    public static int getAllChessNum() {
        return ALL_DESKS_NUM * CHESS_NUM;
    }

    public static ArrayList getRoomsId() {
        ArrayList<Integer> dd = new ArrayList<Integer>();
        Set k = DESKS_NUM_OF_ROOMS.keySet();
        for(Object f : k){
            dd.add((Integer)f);
        }

        return dd;
    }

    public static int getDesksNumOfRoom(int roomId) {
        int desks = 0;
        try{
            desks = (Integer)(CONFIG.DESKS_NUM_OF_ROOMS).get(roomId);
        }catch(Exception e){}

        return desks;
    }

    public static Document setXMLDocs(){
        URL location;
        String classLocation = Building.class.getName().replace('.', '/') + ".class";
        ClassLoader loader = Building.class.getClassLoader();
        location = loader.getResource(classLocation);
        String stl = location.toString();
        String absolutePath = stl.substring(0, (stl.lastIndexOf(WEB_INF) + WEB_INF.length() + 1));

        if (absolutePath.indexOf(":/") != -1) { //windows
            absolutePath = absolutePath.substring(absolutePath.indexOf('/'));
        }

        try{
            Document doc = MainUtil.getXMLDocument(absolutePath + CONFIG_DIR + XML_CONFIG_FILE);
            //doc.getDocumentElement().normalize();

            return doc;
        }catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    public static ArrayList setAllRoomId() {
        ArrayList<Integer> roomsId = new ArrayList<Integer>();
        NodeList listOfRooms = BUILDING_CONFIG_DOC.getElementsByTagName(CONFIG.XML_NODE_ROOM);

        int rooms = listOfRooms.getLength();
        for(int s = 0; s < rooms ; s++){
            Node firstRoomNode = listOfRooms.item(s);
            if (firstRoomNode.getNodeType() == Node.ELEMENT_NODE) {
                Element roomNode = (Element) firstRoomNode;
                roomsId.add(Integer.parseInt(roomNode.getAttribute(ID).trim().substring(1))); //remove "r" in room id
            }
        }

        return roomsId;
    }

    public static int setAllRoomNum() {
        int num = 0;
        NodeList listOfRooms = BUILDING_CONFIG_DOC.getElementsByTagName(XML_NODE_ROOM);
        Node firstDeskNode = listOfRooms.item(0);
        if (firstDeskNode.getNodeType() == Node.ELEMENT_NODE) {
            num = listOfRooms.getLength();
        }

        return num;
    }

    public static int setAllDeskNum() {
        NodeList listOfDesks = BUILDING_CONFIG_DOC.getElementsByTagName(XML_NODE_DESKS_NUM);
        int rooms = listOfDesks.getLength();
        int totalDesks = 0;
        for(int s = 0; s < rooms ; s++){
            Node firstDeskNode = listOfDesks.item(s);
            if (firstDeskNode.getNodeType() == Node.ELEMENT_NODE) {
                int desks = Integer.parseInt(firstDeskNode.getTextContent().trim());
                totalDesks = totalDesks + Math.abs(desks);
            }
        }

        return totalDesks;
    }

    public static HashMap setDesksNumOfRooms() {
        HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();

        NodeList listOfDesks = BUILDING_CONFIG_DOC.getElementsByTagName(XML_NODE_DESKS_NUM);
        int rooms = listOfDesks.getLength();
        for(int s = 0; s < rooms ; s++){
            Node firstDeskNumNode = listOfDesks.item(s);
            if (firstDeskNumNode.getNodeType() == Node.ELEMENT_NODE) {
                Node parentNode = firstDeskNumNode.getParentNode();
                while(!parentNode.getNodeName().equals(XML_NODE_ROOM)){
                    parentNode = parentNode.getParentNode();
                }
                Element roomNode = (Element)parentNode;
                map.put(Math.abs(Integer.parseInt(roomNode.getAttribute(ID).trim().substring(1))), //remove "r" in room id
                        Math.abs(Integer.parseInt(firstDeskNumNode.getTextContent().trim())));
              }
        }

        return map;
    }
    // </editor-fold>
    // </editor-fold>
}
