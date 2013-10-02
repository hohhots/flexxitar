/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nm114.xitar.servlets.longpull;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nm114.xitar.classes.*;
import com.nm114.xitar.classes.comet.*;

/**
 *
 * @author brgd
 */
public class BuildingLongpullServlet extends HttpServlet {

    private static String _SERVLET_URL = null;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("buildingLongpull - get");

        HttpSession hs = request.getSession(false);

        Player player = (Player) hs.getAttribute(CONFIG.PLAYERNAME);
        if(player.getLogin()){
            if (_SERVLET_URL == null) {
                String turl = request.getRequestURI();
                _SERVLET_URL = turl.substring(0,turl.lastIndexOf("/"));
            }

            if (XitarComet.setInstance(_SERVLET_URL)) {
                player.setAction(CONFIG.ACTION_BUILDING);
                player.setBuilding(true);
                player.addCometHandler(_SERVLET_URL, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("buildingLongpull - post");

        PrintWriter writer = response.getWriter();
        writer.write("");
        writer.close();
   }

    @Override
    public String getServletInfo() {
        return "commit all rooms state and desk state in selected room.";
    }// </editor-fold>

    public static String getUrl() {
        return _SERVLET_URL;
    }
}
