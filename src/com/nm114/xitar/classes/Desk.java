/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nm114.xitar.classes;

import java.util.Date;
import org.w3c.dom.*;

import com.nm114.xitar.classes.xitar.*;
import com.nm114.xitar.servlets.*;
import com.nm114.xitar.exception.*;

/**
 *
 * @author brgd
 */
public class Desk {

    // <editor-fold defaultstate="collapsed" desc="VARIABLES.">
    // <editor-fold defaultstate="collapsed" desc="STATIC variables.">
    private static final int objLimit = CONFIG.ALL_DESKS_NUM;
    private static int objCount = 0;
    //</editor-fold>
    // <editor-fold defaultstate="collapsed" desc="OBJECT variables.">
    private int _id = 0;
    private Room _room = null;
    private Player _player1 = null;
    private Player _player2 = null;
    private XitarBoard _xitarboard = null;
    private Boolean _player2Confirmed = false;
    private int _restrictId = -1;
    private long _restrictTime = -1;
    private Boolean _gameEnd = false;

    private static final boolean test = MainUtil.test("Desk object limit - " + objLimit);

    //</editor-fold>
    //</editor-fold>
    // <editor-fold defaultstate="collapsed" desc="METHODS.">
    // <editor-fold defaultstate="collapsed" desc="STATIC methods.">
    public static synchronized Desk getInstance(int num, Room room) throws CreateObjectOutOfLimit {
        if (objCount < objLimit) {
            ++objCount;
            return new Desk(num, room);
        } else {
            throw new CreateObjectOutOfLimit("Create Desk object Out of limit.");
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="INITIAL methods.">
    private Desk(int num, Room room) {
        _id = num;
        _room = room;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="SET methods.">
    private void setRestrictId(int playerid) {
        if (playerid != -1) {
            _restrictId = playerid;
            _restrictTime = (new Date()).getTime();
        } else {
            _restrictId = -1;
            _restrictTime = -1;
        }
    }

    public void setPlayersAction(int action) {
        if (action == CONFIG.ACTION_XITAR_END) {
            _gameEnd = true;
        }
        if (_player1 != null) {
            _player1.setAction(action);
        }
        if (_player2 != null) {
            _player2.setAction(action);
        }
    }

    //</editor-fold>
    // <editor-fold defaultstate="collapsed" desc="GET methods.">
    public int getId() {
        return _id;
    }

    public Room getRoom() {
        return _room;
    }

    public Boolean getGameEnd() {
        return _gameEnd;
    }

    public String getUrl() {
        return DeskServlet.getUrl() + "/" + _room.getId() + CONFIG.ROOM_DESK_SEPERATOR + _id;
    }

    public XitarBoard getXitarBoard() {
        return _xitarboard;
    }

    public void getDeskInfo(Document doc, Element eroom, Player player) {
        Element edesk = doc.createElement(CONFIG.XML_DESK_ELEMENT);
        edesk.setAttribute(CONFIG.ID, String.valueOf(_id));
   
        if(_player1 != null){
            _player1.getInDeskDisplayInfo(doc,edesk);
            if(_player1 != null){
                _player2.getInDeskDisplayInfo(doc,edesk);
            }
        }

        eroom.appendChild(edesk);
    }

    public void getDeskXMLStatus(Document doc, Element eroom) {
        Element edesk = doc.createElement(CONFIG.XML_DESK_ELEMENT);
        edesk.setAttribute(CONFIG.ID, String.valueOf(_id));

        int id1 = -1;
        int id2 = -1;
        Element player1 = doc.createElement(CONFIG.XML_PLAYER1_ELEMENT);
            if(_player1 != null){
                id1 = _player1.getId();
                if(_player2 != null){
                    id2 = _player2.getId();
                }
            }
            player1.setTextContent(String.valueOf(id1));
        edesk.appendChild(player1);

        Element player2 = doc.createElement(CONFIG.XML_PLAYER2_ELEMENT);
            player2.setTextContent(String.valueOf(id2));
        edesk.appendChild(player2);

        eroom.appendChild(edesk);
    }
    
    public String getPlayersIdInJSON() {
        int id1 = -1;
        int id2 = -1;

        try {
            id1 = _player1.getId();
        } catch (Exception e) {
        }

        try {
            id2 = _player2.getId();
        } catch (Exception e) {
            if (_player2Confirmed == true) {
                id2 = 0;
            }
        }

        StringBuilder players = new StringBuilder("[" + id1 + "," + id2 + "]");
        //System.out.println(players.toString());
        return players.toString();
    }

    //</editor-fold>
    // <editor-fold defaultstate="collapsed" desc="IS methods.">
    public Boolean isFirstPlayer(Player player) {
        if (player == _player1) {
            return true;
        }
        return false;
    }

    public Boolean isplayer2Confirmed() {
        return _player2Confirmed;
    }

    public Boolean isPlayerRestricted(int playerid) {
        if (_restrictTime != -1) {
            long timePeriod = (new Date()).getTime() - _restrictTime;
            if (CONFIG.PLAYER_RESTRICT_TIME < timePeriod) {
                setRestrictId(-1);
                return false;
            } else {
                if (_restrictId != playerid) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ADD methods.">
    public synchronized Boolean addPlayer(Player player) {
        if (_player1 == null) {
            _player1 = player;
            player.setDesk(true,this);
            return true;
        }
        if ((_player2 == null) && (!isPlayerRestricted(player.getId()))) {
            this.setRestrictId(-1);
            _player2 = player;
            player.setDesk(false,this);
            return true;
        }
        return false;
    }

    public Boolean addPlayerConfirm(Player player, int agree) {
        if (player == _player1) {
            if (_player2 != null) {
                if (agree != 0) {
                    addXitarBoard();
                    return true;
                } else {
                    setRestrictId(_player2.getId());
                    _player2.setDesk(false,null);
                    _player2 = null;
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean addXitarBoard() {
        try {
            _player2Confirmed = true;
            _xitarboard = XitarBoard.getInstance(this);
            _player1.setBoard(true);
            _player2.setBoard(true);
            return true;
        } catch (CreateObjectOutOfLimit e) {
            e.printStackTrace();
            return false;
        }

    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="DELETE methods.">
    public Boolean deletePlayer(Player Tplayer) {
        if (_player1 == Tplayer) {
            if (_player2 != null) {
                _player1 = _player2;
                _player2 = null;
            } else {
                _player1 = null;
                _player2Confirmed = false;
            }
            return true;
        }
        if (_player2 == Tplayer) {
            _player2 = null;
            return true;
        }

        return false;
    }

    public void deleteXitarBoard() {
        _player2Confirmed = false;
        _xitarboard = null;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="other methods.">
    public void playerExit(Player Tplayer) {
        if ((_player1 != null) || (_player2 != null)) {
            //deleteXitarBoard();
            Tplayer.deleteDesk();
            _gameEnd = false;
            //player1.setAccessSequence(DeskServlet.SERVLET_ID);
        }
    }
    //</editor-fold>
    //</editor-fold>
}
