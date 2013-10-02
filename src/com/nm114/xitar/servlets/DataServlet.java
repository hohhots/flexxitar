/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nm114.xitar.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;

import com.nm114.xitar.classes.*;

/**
 *
 * @author brgd
 */
public class DataServlet extends HttpServlet {

     HttpSession hs = null;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest req, HttpServletResponse rep)
            throws ServletException, IOException {
        if(CONFIG.DEBUG){
            System.out.println("DataServlet - processRequest");
        }
        
        rep.setContentType("text/html;charset=UTF-8");
        PrintWriter out = rep.getWriter();

        hs = req.getSession(false);

        int se = MainUtil.getPlayerStage(req);

        String st = null;
        switch (se) {
            case CONFIG.BUILDING_STAGE:
                st = this.getInitData();
                break;
            default:
                st = CONFIG.GET_DATA_ERROR;
                break;
        }

        try {
            out.print(st);
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(CONFIG.DEBUG){
            System.out.println("DataServlet - doGet");
        }
        
        processRequest(request, response);
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
        if(CONFIG.DEBUG){
            System.out.println("DataServlet - doPost");
        }
        //processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Data Servlet!";
    }// </editor-fold>

    private String getInitData(){
        Player player = (Player) (hs.getAttribute(CONFIG.PLAYERNAME));

        String init = player.getBuildingInitial();

        return init;
    }
}
