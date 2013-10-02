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
public class RoomLongpullServlet extends HttpServlet {

    private static String _SERVLET_URL = null;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("roomLongpull - get");

        PrintWriter writer = response.getWriter();
        writer.write("");
        writer.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("roomLongpull - post");

        HttpSession hs = request.getSession(false);

        Player player = (Player) hs.getAttribute(CONFIG.PLAYERNAME);
        if(player.getRoom() != null){
            if (_SERVLET_URL == null) {
                _SERVLET_URL = request.getRequestURI();
            }

            if (XitarComet.setInstance(player.getRoom().getUrl())) {
                player.setAction(CONFIG.ACTION_BUILDING);
                player.addCometHandler(_SERVLET_URL, response);
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "commit all rooms state and desk state in selected room.";
    }// </editor-fold>

    public static String getUrl() {
        return _SERVLET_URL;
    }
}
