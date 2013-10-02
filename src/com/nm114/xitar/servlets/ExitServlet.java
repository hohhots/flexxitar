/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nm114.xitar.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nm114.xitar.classes.*;
import com.nm114.xitar.classes.Player;

/**
 *
 * @author brgd
 */
public class ExitServlet extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if(CONFIG.DEBUG){
            System.out.println("ExitServlet - processRequest");
        }

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        if((Boolean)req.getAttribute(CONFIG.NO_SESSION)){
            return;
        }

        HttpSession hs = req.getSession(false);
        Player p = (Player) hs.getAttribute(CONFIG.PLAYERNAME);

        int s = CONFIG.LOGIN_STAGE;
        if ((Integer)p.getCurrentStage() != s) {
            try {
                if (p != null) {
                    p.deleteId();
                }
                p.deleteInstance();

                hs.removeAttribute(CONFIG.PLAYERNAME);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            out.println(s);
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
            System.out.println("ExitServlet - doGet");
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
            System.out.println("ExitServlet - doPost");
        }
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Exit from Server.";
    }// </editor-fold>
}
