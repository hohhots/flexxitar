/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nm114.xitar.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.nm114.xitar.classes.*;
import com.nm114.xitar.classes.xitar.*;
import com.nm114.xitar.classes.comet.*;

/**
 *
 * @author brgd
 */
public class XitarServlet extends HttpServlet {

    public static final int SERVLET_ID = 4;
    private static String SERVLET_URL = "";

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (CONFIG.DEBUG) {
            System.out.println("XitarServlet- doGet()");
        }

        response.setContentType("text/html;charset=UTF-8");
        //PrintWriter out = response.getWriter();

        if (SERVLET_URL.equals("")) {
            SERVLET_URL = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
        }

        if (hasSequence(request, response)) {
            Player player = (Player) request.getSession(false).getAttribute(CONFIG.PLAYERNAME);
            player.setAction(CONFIG.ACTION_XITAR_LASTSTEP);
            if (XitarComet.setInstance(player.getDesk().getXitarBoard().getUrl())) {
                player.addCometHandler(SERVLET_URL, response);
            }
        }
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (CONFIG.DEBUG) {
            System.out.println("XitarServlet- doPost()");
        }

        response.setContentType("text/html;charset=UTF-8");

        if (hasSequence(request, response)) {
            String end = request.getParameter("gameend");
            String exit = request.getParameter("exit");
            if (end != null) {
                endGame(request);
            } else {
                if (exit != null) {
                    exitDesk(request);
                } else {
                    String ls = request.getParameter("laststep");
                    if (ls != null) {
                        lastStep(ls, request);
                    }
                }
            }
        }
    }

    private void exitDesk(HttpServletRequest request) {
        if (CONFIG.DEBUG) {
            System.out.println("XitarServlet- exitDesk()");
        }
        Player player = (Player) request.getSession(false).getAttribute(CONFIG.PLAYERNAME);
        Room room = player.getRoom();
        Desk desk = player.getDesk();
        desk.setPlayersAction(CONFIG.ACTION_XITAR_EXIT);
        XitarBoard xitarboard = player.getDesk().getXitarBoard();

        desk.playerExit(player);

        try {
            XitarComet.notifyRoomComet(room.getId());
            XitarComet.notifyXitarComet(xitarboard.getUrl());
        } catch (Exception e) {
        }

    }

    private void endGame(HttpServletRequest request) {
        try {
            Player player = (Player) request.getSession(false).getAttribute(CONFIG.PLAYERNAME);
            Desk desk = player.getDesk();
            desk.setPlayersAction(CONFIG.ACTION_XITAR_END);
            XitarBoard xitarboard = player.getDesk().getXitarBoard();
            XitarComet.notifyXitarComet(xitarboard.getUrl());
        } catch (Exception e) {
        }
    }

    private void lastStep(String laststep, HttpServletRequest request) {
        if (CONFIG.DEBUG) {
            System.out.println("XitarServlet - lastStep()");
        }
        System.out.println("XitarServlet - lastStep()");
        try {
            int sep = laststep.lastIndexOf(",");
            int fcellid = Integer.parseInt(laststep.substring(0, sep));
            int tcellid = Integer.parseInt(laststep.substring(sep + 1));

            Player player = (Player) request.getSession(false).getAttribute(CONFIG.PLAYERNAME);
            XitarBoard xitarboard = player.getDesk().getXitarBoard();

            Cell fcell = xitarboard.getCell(fcellid);
            Cell tcell = xitarboard.getCell(tcellid);

            if (tcell.moveToThisCell(player, fcell)) {
                XitarComet.notifyXitarComet(xitarboard.getUrl());
            }
        } catch (Exception e) {
        }
    }

    private Boolean hasSequence(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Boolean s = true;

        HttpSession hs = request.getSession(false);
        Player player = ((Player) (hs.getAttribute(CONFIG.PLAYERNAME)));
        try {
            if ((player.getDesk() != null) && (player.getBoard() == false)) {
                if (hs != null) {
                    hs.invalidate();
                }
                s = false;
            }
        } catch (Exception e) {
            s = false;
        }

        return s;
    }

    public static String getUrl() {
        return SERVLET_URL;
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "commit all xitars state in xitarboard.";
    }// </editor-fold>
}
