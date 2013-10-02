/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nm114.xitar.listeners;

import javax.servlet.ServletRequestListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.nm114.xitar.classes.*;

/**
 *
 * @author brgd
 */
public class SessionListener implements HttpSessionListener,ServletRequestListener {

    private HttpServletRequest req;

    public void sessionCreated(HttpSessionEvent event) {
        //System.out.println("sessionCreated + " + req.getRemoteAddr() + " + " + req.getServletPath());

        HttpSession hs = event.getSession(); //create http session
        
        if(req.getServletPath().equals(CONFIG.SESSION_SERVLET_URL)){
            hs.setAttribute(CONFIG.STAGE,CONFIG.LOGIN_STAGE);
            hs.setAttribute(CONFIG.REMOTEADDR, req.getRemoteAddr());
            hs.setAttribute(CONFIG.USERAGENT, req.getHeader("user-agent"));
        }else{
            hs.invalidate();
        }

    }

    public void sessionDestroyed(HttpSessionEvent event) {
        System.out.println("sessionDestroyed");
        try {
            HttpSession hs = event.getSession();
            Player p = (Player) hs.getAttribute(CONFIG.PLAYERNAME);
            if (p != null) {
                p.deleteId();
            }
            p.deleteInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void requestDestroyed(ServletRequestEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void requestInitialized(ServletRequestEvent sre){
        req = (HttpServletRequest)sre.getServletRequest();
    }
}
