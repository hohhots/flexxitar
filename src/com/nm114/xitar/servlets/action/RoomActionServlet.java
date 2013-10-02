/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nm114.xitar.servlets.action;

import java.io.IOException;
import java.io.PrintWriter;

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
public class RoomActionServlet extends HttpServlet {

    //private static String _SERVLET_URL = "";

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("roomActionServlet- get");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String tu = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/")); //remove random created number

        //if (_SERVLET_URL.equals("")) {
        //    _SERVLET_URL = tu.substring(0, tu.lastIndexOf("/")); //remove room id
        //}

        HttpSession hs = request.getSession(false);
        Player player = (Player) hs.getAttribute(CONFIG.PLAYERNAME);

        if (player.getBuilding() == true) {
            int roomid = Integer.parseInt(tu.substring(tu.lastIndexOf("/") + 1));
            Room room = Building.getRoom(roomid);
            if (room != null) {
                player.addPlayerToRoom(room);
            }
        }
        out.write("");
        XitarComet.notifyBuildingComet();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException,
            IOException {
        
    }

    @Override
    public String getServletInfo() {
        return "commit all desks state and room state in selected room.";
    }// </editor-fold>

    //public static String getUrl() {
    //    return _SERVLET_URL;
    //}
}

