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
import com.nm114.xitar.classes.comet.*;

/**
 *
 * @author brgd
 */
public class DeskServlet extends HttpServlet {

    public static final int SERVLET_ID = 3;
    private static String SERVLET_URL = "";

    HttpSession hs = null;

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //System.out.println("desk- get");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        if (SERVLET_URL.equals("")) {
            SERVLET_URL = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
        }

        hs = request.getSession(false);
        Player player = (Player)hs.getAttribute(CONFIG.PLAYERNAME);

        if ((player.getRoom() != null) && (player.getDesk() == null)) {
            String roomdesk = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/") + 1);
            int dp = roomdesk.lastIndexOf(CONFIG.ROOM_DESK_SEPERATOR);
            int roomid = Integer.parseInt(roomdesk.substring(0, dp));
            int deskid = Integer.parseInt(roomdesk.substring(dp + 1));

            if (roomid < Building.getRooms().length) {
                Room room = Building.getRoom(roomid);
                if (player.hasRoom(room)) {
                    if (deskid < room.getDesks().length) {
                        Desk desk = room.getDesk(deskid);

                        if (XitarComet.setInstance(desk.getUrl())) {
                            player.setAction(CONFIG.ACTION_DESK);
                            if (player.hasDesk(desk)) {
                                player.addCometHandler(SERVLET_URL, response);
                            } else {
                                player.addPlayerToDesk(desk);
                                XitarComet.notifyRoomComet(roomid);

                                try {
                                    //out.println(player.getRoomInitial());
                                } finally {
                                    out.close();
                                }
                            }
                        }
                    }
                }
            }
        } else {
            sendMessage(response, CONFIG.NO_ACESS_RIGHT);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //System.out.println("desk- post");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        hs = request.getSession(false);
        Player player = (Player)hs.getAttribute(CONFIG.PLAYERNAME);

        if (player.getRoom() != null) {
            String roomdesk = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/") + 1);
            int dp = roomdesk.lastIndexOf("d");
            int roomid = Integer.parseInt(roomdesk.substring(0, dp));
            int deskid = Integer.parseInt(roomdesk.substring(dp + 1));

            if (roomid < Building.getRooms().length) {
                Room room = Building.getRoom(roomid);
                if (player.hasRoom(room)) {
                    if (deskid < room.getDesks().length) {
                        Desk desk = room.getDesk(deskid);
                        if ((player.hasDesk(desk)) && (request.getParameter("confirmuser") != null)) {
                            int agree = Integer.parseInt(request.getParameter("confirmuser"));
                            if (desk.addPlayerConfirm(player, agree)) {
                                if (agree == 0) {
                                    XitarComet.notifyRoomComet(roomid);
                                } else {
                                    XitarComet.notifyConfirmPlayerComet(roomdesk);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void sendMessage(HttpServletResponse response, String message) throws IOException {
        PrintWriter out = response.getWriter();
        out.write(message);
        out.close();
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "commit all desks state in selected room.";
    }

    public static String getUrl() {
        return SERVLET_URL;
    }
    // </editor-fold>
}
