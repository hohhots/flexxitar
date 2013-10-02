/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nm114.xitar.classes;

import org.w3c.dom.*;
import java.io.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;

import com.sun.grizzly.comet.CometContext;
import com.sun.grizzly.comet.CometEngine;
import com.sun.grizzly.comet.CometEvent;
import com.sun.grizzly.comet.CometHandler;

import com.nm114.xitar.classes.comet.XitarComet;
import com.nm114.xitar.servlets.*;
import com.nm114.xitar.servlets.longpull.*;
import com.nm114.xitar.exception.*;

/**
 *
 * @author brgd
 */
public class Player {

    // <editor-fold defaultstate="collapsed" desc="VARIABLES.">
    // <editor-fold defaultstate="collapsed" desc="STATIC variables.">
    private static final int objLimit = CONFIG.getUsersNum();
    private static int objCount = 0;
    //</editor-fold>
    // <editor-fold defaultstate="collapsed" desc="OBJECT variables.">
    private int _id = 0;
    private boolean _login = false;
    private boolean _building = false;
    private Room _room = null;
    private Desk _desk = null;
    private boolean _board = false;
    private XitarComet _xc = null;
    private XitarCometHandler _handler;;
    private int _action = 0;
    private boolean _isFirstPlayer = false; //fist sat on desk player.

    private static final boolean test = MainUtil.test("Player object limit - " + objLimit);
    //</editor-fold>
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="METHODS.">
    // <editor-fold defaultstate="collapsed" desc="STATIC methods.">
    public static synchronized Player getInstance() throws CreateObjectOutOfLimit {
        if (objCount < objLimit) {//System.out.println(MainModel.getXMLStatus(CONFIG.XML_BUILDING_STATUS));
            ++objCount;
            return new Player();
        } else {
            throw new CreateObjectOutOfLimit("Create player object Out of limit.");
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="INITIAL methods.">
    private Player() {
        _id = UsersId.useUserID();
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="SET methods.">
    public void setAction(int action) {
        _action = action;
    }

    public void setLogin(boolean login) {
        _login = login;
    }

    public void setBuilding(boolean building) {
        _building = building;
    }

    public void setDesk(boolean isFirstPlayer, Desk Tdesk) {
        //System.out.print("Player - setDesk()" );
        this._isFirstPlayer = isFirstPlayer;
        
        if ((_desk != null)) {
            _desk.deletePlayer(this);
        }
        _desk = Tdesk;
    }

    public void setBoard(boolean board) {
        _board = board;
    }

    public void setRoom(Room Troom) {
        //System.out.print("Player - setRoom()" );

        if ((_room != null)) {
            _room.deletePlayer(this);
        }
        _room = Troom;
    }

    //</editor-fold>
    // <editor-fold defaultstate="collapsed" desc="GET methods.">
    public int getId() {
        return _id;
    }

    public boolean getLogin() {
        return _login;
    }

    public boolean getBuilding() {
        return _building;
    }

    public Desk getDesk() {
        return _desk;
    }

    public Room getRoom() {
        return _room;
    }

    public boolean getBoard() {
        return _board;
    }

    public int getCurrentStage() {
        int s = CONFIG.BUILDING_STAGE;

        if(_room != null){
            s = CONFIG.ROOM_STAGE;
            if(_desk != null){
                s = CONFIG.DESK_STAGE;
            }
        }

        return s;
    }

    public String getBuildingInitial() {

        String xmlString = "";

        try{
            Document doc = MainUtil.getXMLDocument("");
            Element root = doc.createElement(CONFIG.XML_ROOT_ELEMENT);
            doc.appendChild(root);

            Element tplayer = this.getBuildingNotifyXML(doc);
            root.appendChild(tplayer);

            Building.getBuildingInfo(doc,root,this);

            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();

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

    public void getInDeskDisplayInfo(Document doc, Element edesk) {
        Element eplayer;
        if(_isFirstPlayer){
            eplayer = doc.createElement("player1");
        }else{
            eplayer = doc.createElement("player2");
        }

        eplayer.setAttribute(CONFIG.ID, String.valueOf(_id));
        edesk.appendChild(eplayer);
    }

    private String getResponseContent() {
        //System.out.println("player - getResponseContent()");
        String cont = "";

        int s = getCurrentStage();
        switch(s){
            case CONFIG.BUILDING_STAGE:
                cont = Building.getNotifyXML(this);
                break;
            case CONFIG.ROOM_STAGE:
                cont = Building.getNotifyXML(this);
                break;
            default:

        }

        return cont;
    }

    public Element getBuildingNotifyXML(Document doc){
        //create child element player,
        Element player = doc.createElement(CONFIG.XML_PLAYER_ELEMENT);
            player.setAttribute(CONFIG.ID, String.valueOf(_id));
        if(_room != null){
            Element room = doc.createElement(CONFIG.XML_ROOM_ELEMENT);
                room.setAttribute(CONFIG.ID, String.valueOf(_room.getId()));
            player.appendChild(room);
            if(_desk != null){
                Element desk = doc.createElement(CONFIG.XML_DESK_ELEMENT);
                    desk.setAttribute(CONFIG.ID, String.valueOf(_room.getId()));
                player.appendChild(desk);
            }
        }

        return player;
    }


    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CAN methods. empty">
    //</editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Has methods. ">
    public Boolean hasRoom(Room Troom) {
        if (Troom == null) {
            if (_room != null) {
                return true;
            }
        } else {
            if (_room == Troom) {
                return true;
            }
        }
        return false;
    }

    public Boolean hasDesk(Desk Tdesk) {
        if (Tdesk == null) {
            if (_desk != null) {
                return true;
            }
        } else {
            if (_desk == Tdesk) {
                return true;
            }
        }
        return false;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="DELETE methods.">
    public synchronized void deleteInstance() {
        --objCount;
    }

    public Boolean deleteId() {
        if (_desk != null) {
            deleteDesk();
        }
        if (_room != null) {
            deleteRoom();
        }

        UsersId.unuseUserID(_id);

        return true;
    }

    public void deleteDesk() {
        if (_desk != null) {
            _desk.deletePlayer(this);
            _desk = null;
        }
    }

    public void deleteRoom() {
        if (_room != null) {
            _room.deletePlayer(this);
            _room = null;
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ADD methods.">
    public Boolean addPlayerToRoom(Room room) {
        if (!hasRoom(room)) {
            if (room.addPlayer(this)) {
                return true;
            }
        }
        return false;
    }

    public Boolean addPlayerToDesk(Desk desk) {
        if (!hasDesk(desk)) {
            this.deleteDesk();
            if (desk.addPlayer(this)) {
                return true;
            }
        }
        return false;
    }

    public void addCometHandler(String url, HttpServletResponse response) {

        if (url.equals(BuildingLongpullServlet.getUrl())) {
            addBuildingHandler(response);
        }
        if (url.equals(RoomLongpullServlet.getUrl())) {
            addRoomHandler(response);
        }
        if (url.equals(DeskServlet.getUrl())) {
            addDeskHandler(response);
        }
        if (url.equals(XitarServlet.getUrl())) {
            addXitarHandler(response);
        }
        
    }

    private void addBuildingHandler(HttpServletResponse response) {
        String uri = BuildingLongpullServlet.getUrl();
        addAllHandler(uri,response);
    }

    private void addRoomHandler(HttpServletResponse response) {
        String uri = _room.getUrl();
        addAllHandler(uri,response);
    }

    private void addDeskHandler(HttpServletResponse response) {
        String uri = _desk.getUrl();
        addAllHandler(uri,response);
    }

    private void addXitarHandler(HttpServletResponse response) {
        String uri = _desk.getXitarBoard().getUrl();
        addAllHandler(uri,response);
    }

    private void addAllHandler(String uri,HttpServletResponse response) {
        _xc = XitarComet.getInstance(uri);
        _handler = new XitarCometHandler(uri,response);
    }

    //</editor-fold>
    // <editor-fold defaultstate="collapsed" desc="other methods.">
    public void exitBuilding() {
        deleteDesk();
        deleteRoom();
        _login = false;
    }
    //</editor-fold>
    // <editor-fold defaultstate="collapsed" desc="INNER OBJECTS.">

    private class XitarCometHandler implements CometHandler<HttpServletResponse> {

        private final String _HTML = "text/html;charset=UTF-8";
        private final String _XML = "text/xml;charset=UTF-8";
        private HttpServletResponse _response = null;
        //private CometContext _context = null;

        public XitarCometHandler(String uri,HttpServletResponse response){
            super();

            attach(response);
            //_context = CometEngine.getEngine().getCometContext(uri);
            XitarComet.getInstance(uri).addCometHandler(this);
        }

        public void onEvent(CometEvent event) throws IOException {
            //System.out.println("onEvent - " + event.getType());
            if (CometEvent.NOTIFY == event.getType()) {
                content();
            }
        }

        public void onInitialize(CometEvent event) throws IOException {
            //System.out.println("onInitialize - " + event.getType());
        }

        public void onInterrupt(CometEvent event) throws IOException {
            System.out.println("onInterrupt - " + event.getType());
            PrintWriter writer = _response.getWriter();
            _response.setContentType(_HTML);
            writer.write("i");
            removeThisFromContext();
        }

        public void onTerminate(CometEvent event) throws IOException {
            System.out.println("onTerminate - " + event.getType());
            removeThisFromContext();
        }

        public void attach(HttpServletResponse res) {
            _response = res;
            _response.setContentType(_XML);
        }

        private void content() throws IOException {
            System.out.println("content");
            
            String cont = getResponseContent();

            PrintWriter writer = _response.getWriter();
            writer.write(cont);
            
            writer.close();
        }

        public void removeThisFromContext() throws IOException {
		_response.getWriter().close();
                try{
                    _xc.resumeCometHandler(this);//remove previous Comet handler;
                }catch(Exception e){}
	}
    //</editor-fold>
    }
}