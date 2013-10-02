/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nm114.xitar.classes.xitar;

import java.util.*;

/**
 *
 * @author brgd
 */
public class Mori extends Xitar {

    //<editor-fold defaultstate="collapsed" desc="VARIABLES.">
    //<editor-fold defaultstate="collapsed" desc="STATIC variables.">
    public static final int id = 2;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="OBJECT variables. empty">
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="METHODS.">
    //<editor-fold defaultstate="collapsed" desc="STATIC methods. empty">
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="INITIAL methods. empty">
    public Mori(int side) {
        this.side = side;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="SET methods. empty">
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GET methods.">
    public int getId(){
        return id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CAN methods. empty">
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Has methods. empty">
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="DELETE methods. empty">
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ADD methods. empty">
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Other methods. empty">
    protected Boolean moveToCellRule(Cell tcell) {

        int fceid = this.cell.getId();
        int tceid = tcell.getId();
        int fidx = (fceid % 8);
        int fidy = ((fceid - (fceid % 8)) / 8) + 1;
        int tidx = (tceid % 8);
        int tidy = ((tceid - (tceid % 8)) / 8) + 1;
        if (((fidx == (tidx + 2)) || (fidx == (tidx - 2))) && ((fidy == (tidy + 1)) || (fidy == (tidy - 1)))) {
            return true;
        }
        if (((fidy == (tidy + 2)) || (fidy == (tidy - 2))) && ((fidx == (tidx + 1)) || (fidx == (tidx - 1)))) {
            return true;
        }

        return false;
    }

    protected Boolean moveToChessRule(Cell tcell) {
        return this.moveToCellRule(tcell);
    }
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="INNER OBJECTS. empty">
    //</editor-fold>
}