/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nm114.xitar.classes;

import java.util.*;
import org.w3c.dom.*;

import com.nm114.xitar.exception.*;
import com.nm114.xitar.servlets.longpull.*;

/**
 *
 * @author brgd
 */
public class Room {

    private static final int objLimit = CONFIG.ALL_ROOMS_NUM;
    private static int objCount = 0;
    private int _id = 0;
    private ArrayList<Player> _players = new ArrayList<Player>();
    private int _oldPlayersNum = 0;
    private Desk[] _desks;

    private static final boolean test = MainUtil.test("Room object limit - " + objLimit);

    private Room(int num) {
        _id = num;
        _desks = new Desk[CONFIG.getDesksNumOfRoom(num)];

        int z = _desks.length;
        int i;
        try {
            for (i = 0; i < z; i++) {
                _desks[i] = Desk.getInstance(i, this);
            }
        } catch (CreateObjectOutOfLimit e) {
            e.printStackTrace();
        }

    }

    public static synchronized Room getInstance(int num) throws CreateObjectOutOfLimit {
        if (objCount < objLimit) {
            ++objCount;
            return new Room(num);
        } else {
            throw new CreateObjectOutOfLimit("Create Room object Out of limit.");
        }
    }

    public int getId() {
        return _id;
    }

    public String getUrl() {
        return RoomLongpullServlet.getUrl() + "/" + _id;
    }

    public int getPlayers() {
        return _players.size();
    }

    public Desk getDesk(int id) {
        return _desks[id];
    }

    public int getDesksNum() {
        return _desks.length;
    }

    public Desk[] getDesks() {
        return _desks;
    }

    public synchronized Boolean addPlayer(Player player) {
        if (!_players.contains(player)) {
            _players.add(player);
            player.setRoom(this);
            return true;
        }
        return false;
    }

    public synchronized Boolean deletePlayer(Player player) {
        if (_players.contains(player)) {
            _players.remove(player);
            player.deleteDesk();
            return true;
        }
        return false;
    }

    public void getRoomInfo(Document doc, Element ebuilding, Player player) {
  
        Element eroom = doc.createElement(CONFIG.XML_ROOM_ELEMENT);
            eroom.setAttribute(CONFIG.ID, String.valueOf(_id));

        Element eplayers = doc.createElement(CONFIG.XML_PLAYERS_ELEMENT);
            eplayers.setTextContent(String.valueOf(_players.size()));
        eroom.appendChild(eplayers);

        Element edesks = doc.createElement(CONFIG.XML_DESKS_ELEMENT);
            edesks.setTextContent(String.valueOf(_desks.length));
        eroom.appendChild(edesks);

        Room room = player.getRoom();
        if(room != null){
            for (int i = 0; i < _desks.length; i++) {
                _desks[i].getDeskInfo(doc,eroom,player);
            }
        }

        ebuilding.appendChild(eroom);
    }

    public synchronized Boolean getBuildingNotifyXML(Document doc, Element ebuilding) { //synchronized - prevent from add or delete
        if(_oldPlayersNum != _players.size()){                                                           //player during notify.
            Element room = doc.createElement(CONFIG.XML_ROOM_ELEMENT);
                room.setAttribute(CONFIG.ID, String.valueOf(_id));

            try{
                Element players = doc.createElement(CONFIG.XML_PLAYERS_ELEMENT);
                    players.setTextContent(String.valueOf(_players.size()));
                room.appendChild(players);
            }catch(Exception e){}

            ebuilding.appendChild(room);

            _oldPlayersNum = _players.size();
            return true;
        }else{
            return false;
        }
    }

    public void getXMLStatus(Document doc, Element ebuilding,boolean child) {
        Element room = doc.createElement(CONFIG.XML_ROOM_ELEMENT);
        room.setAttribute(CONFIG.ID, String.valueOf(_id));

        try{
            Element players = doc.createElement(CONFIG.XML_PLAYERS_ELEMENT);
                players.setTextContent(String.valueOf(_players.size()));
            room.appendChild(players);
            if(child){
                for (int i = 0; i < _desks.length; i++) {
                    _desks[i].getDeskXMLStatus(doc,room);
                }
            }else{
                Element desks = doc.createElement(CONFIG.XML_DESKS_ELEMENT);
                    desks.setTextContent(String.valueOf(_desks.length));
                room.appendChild(desks);
            }
        }catch(Exception e){}

        ebuilding.appendChild(room);
    }

    public String getDesksStatus() {
        StringBuilder players = new StringBuilder("[");

        for (int i = 0; i < _desks.length; i++) {
            players.append(_desks[i].getPlayersIdInJSON());
            if (i != (_desks.length - 1)) {
                players.append(",");
            }
        }
        players.append("]");
        //System.out.println(players.toString());
        return players.toString();
    }

}
