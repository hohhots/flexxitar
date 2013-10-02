/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nm114.xitar.classes.comet;

import com.nm114.xitar.servlets.longpull.*;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import com.sun.grizzly.comet.CometContext;
import com.sun.grizzly.comet.CometEngine;
import com.sun.grizzly.comet.CometHandler;

import com.nm114.xitar.classes.*;
import com.nm114.xitar.servlets.*;

/**
 *
 * @author brgd
 */
public class XitarComet{ 

    private static HashMap<String, XitarComet> _comets = new HashMap();
    //private static Notifier _notifier = new Notifier();
    private String _uri = "";
    private CometContext _cometContext = null;
 
    private XitarComet(String uri) {
        _uri = uri;
        CometEngine engine = CometEngine.getEngine();
        _cometContext = engine.register(uri);
        _cometContext.setExpirationDelay(CONFIG.COMET_EXPIRATIONDELAY);
    }

    public static synchronized Boolean setInstance(String uri) {
        Boolean sucess = false;

        if (_comets.containsKey(uri)) {
            sucess = true;
        } else {
            XitarComet xc = new XitarComet(uri);
            _comets.put(uri, xc);
            sucess = true;
        }

        return sucess;
    }

    public static synchronized void notifyBuildingComet() throws IOException {
        if(Building.setNotifyXML()){
            Iterator it = _comets.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                if (pairs.getKey().toString().contains(BuildingLongpullServlet.getUrl()) ||
                        pairs.getKey().toString().contains(RoomLongpullServlet.getUrl()) ||
                        pairs.getKey().toString().contains(DeskServlet.getUrl())) {
                    ((XitarComet) (pairs.getValue())).notifyComet();
                }
            }
        }
    }

    public static synchronized void notifyRoomComet(int roomid) throws IOException {
        Iterator it = _comets.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            //if (pairs.getKey().toString().contains(RoomActionServlet.getUrl() + "/" + roomid) ||
            //        pairs.getKey().toString().contains(DeskServlet.getUrl() + "/" + roomid)) {
                ((XitarComet) (pairs.getValue())).notifyComet();
            //}
        }
    }

    public static synchronized void notifyConfirmPlayerComet(String roomdesk) throws IOException {
        int dp = roomdesk.lastIndexOf("d");
        int roomid = Integer.parseInt(roomdesk.substring(0, dp));
        //int deskid = Integer.parseInt(roomdesk.substring(dp + 1));

        Iterator it = _comets.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            if (pairs.getKey().toString().contains(DeskServlet.getUrl() + "/" + roomdesk)) {
                ((XitarComet) (pairs.getValue())).notifyComet();
            } else {
                //if (pairs.getKey().toString().contains(RoomActionServlet.getUrl() + "/" + roomid) ||
                 //       pairs.getKey().toString().contains(DeskServlet.getUrl() + "/" + roomid)) {
                    ((XitarComet) (pairs.getValue())).notifyComet();
                //}
            }
        }
    }

     public static synchronized void notifyXitarComet(String url) throws IOException {
        Iterator it = _comets.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            if (pairs.getKey().toString().contains(url)) {
                ((XitarComet) (pairs.getValue())).notifyComet();
            }
        }
     }

    public static XitarComet getInstance(String uri) {
        XitarComet xc = null;

        if (_comets.containsKey(uri)) {
            xc = _comets.get(uri);
        }

        return xc;
    }

    public synchronized void notifyComet() throws IOException {
        _cometContext.notify(null);
    }

    public void addCometHandler(CometHandler handler) {
        _cometContext.addCometHandler(handler);
    }

    public void resumeCometHandler(CometHandler handler) {
        _cometContext.resumeCometHandler(handler);
    }

}


