/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nm114.xitar.servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nm114.xitar.classes.*;
import com.nm114.xitar.exception.*;
import com.nm114.xitar.classes.cache.ReadSwfFile;

/**
 *
 * @author brgd
 */
public class SwfServlet extends HttpServlet {

    private static ReadSwfFile readSwfFile = null;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        try {
            readSwfFile = ReadSwfFile.getInstance();
        } catch (CreateObjectOutOfLimit e) {
            e.printStackTrace();
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(CONFIG.DEBUG){
            System.out.println("SwfServlet - processRequest");
        }
       
        int se = CONFIG.INIT_STAGE;
        se = MainUtil.getPlayerStage(request);

        String uri = request.getRequestURI();
        String file = uri.substring(uri.lastIndexOf("/") + 1);
        //String mima = file.substring(file.lastIndexOf(".") + 1).toLowerCase();

        byte[] swf = readSwfFile.getContent(se,file);

        if (swf != null) {
            response.setContentType("application/x-shockwave-flash");
            ServletOutputStream out = response.getOutputStream();
            try {
                out.write(swf);
            } finally {
                out.close();
            }
        } else {
            System.out.println("NULL SWF");
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
        //processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
