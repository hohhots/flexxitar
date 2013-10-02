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
import com.nm114.xitar.exception.*;

/**
 *
 * @author brgd
 */
public class LoginServlet extends HttpServlet {

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
            System.out.println("LoginServlet - processRequest");
        }
    
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        HttpSession hs = req.getSession(false);

        if((hs.getAttribute(CONFIG.PLAYERNAME) != null)||
                (Boolean)req.getAttribute(CONFIG.NO_SESSION)){
            return;
        }

        int se = CONFIG.LOGIN_STAGE;

        if(!validInterval(hs,out,se)){
            return;
        }

        String passcode = (String) req.getParameter("passwd");
        
        if (CONFIG.PASSWORD.equals(passcode)) {
            try {
                Player p = Player.getInstance();
                p.setLogin(true);
                hs.setAttribute(CONFIG.PLAYERNAME, p);
            } catch (CreateObjectOutOfLimit e) {
                e.printStackTrace();
            }
        }
        try {
            se = MainUtil.getPlayerStage(req);
            out.println(se);
        } finally {
            out.close();
        }
    }

    private Boolean validInterval(HttpSession hs,PrintWriter out,int val){
        if(CONFIG.DEBUG){
            System.out.println("LoginServlet -validInterval");
        }

        long llt = hs.getLastAccessedTime();
        long tt = System.currentTimeMillis();

        if((tt - llt) < CONFIG.LOGIN_INTERVAL){ //login too quikly
            out.println(val);
            out.close();
            return false;
        }

        return true;
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
            System.out.println("LoginServlet - doPost");
        }

        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "check login password.";
    }// </editor-fold>
}
