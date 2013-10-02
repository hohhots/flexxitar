/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nm114.xitar.classes.xitar;

import java.util.*;

import com.nm114.xitar.classes.*;

/**
 *
 * @author brgd
 */
public class Hoo extends Xitar {

    //<editor-fold defaultstate="collapsed" desc="VARIABLES.">
    //<editor-fold defaultstate="collapsed" desc="STATIC variables.">
    public static final int id = 6;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="OBJECT variables. empty">
    //</editor-fold>
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="METHODS.">
    //<editor-fold defaultstate="collapsed" desc="STATIC methods.">
    public static Boolean hooInChangeState(Xitar x, Cell c) {
        int xside = x.getSide();
        int cid = c.getId();
        
        if (xside == CONFIG.XITAR_BLACK) {
            if ((cid > 55) && (cid < 64)) {
                return true;
            }
        } else {
            if ((cid > -1) && (cid < 8)) {
                return true;
            }
        }

        return false;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="INITIAL methods.">
    public Hoo(int side) {
        this.side = side;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="SET methods. empty">
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="GET methods.">
    public int getId() {
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
    //<editor-fold defaultstate="collapsed" desc="Other methods.">
    protected Boolean moveToCellRule(Cell tcell) {

        int fceid = this.cell.getId();
        int tceid = tcell.getId();

        Queue steps = this.xitarboard.getSteps();
        if (steps.size() == 0) {
            if (this.side == 0) {
                if ((fceid + 16) == tceid) {
                    return true;
                }
            }
        } else {
            if (steps.size() == 1) {
                if (this.side == 1) {
                    if ((fceid - 16) == tceid) {
                        return true;
                    }
                }
            } else {
                int fidx = (fceid % 8);
                int fidy = ((fceid - (fceid % 8)) / 8) + 1;
                int tidx = (tceid % 8);
                int tidy = ((tceid - (tceid % 8)) / 8) + 1;

                if (fidx == tidx) {
                    if ((this.side == 0) && (fidy == (tidy - 1))) {
                        return true;
                    }
                    if ((this.side == 1) && (fidy == (tidy + 1))) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    protected Boolean moveToChessRule(Cell tcell) {
        int fid = this.cell.getId();
        int tid = tcell.getId();

        int fidx = (fid % 8);
        int fidy = ((fid - (fid % 8)) / 8) + 1;
        int tidx = (tid % 8);
        int tidy = ((tid - (tid % 8)) / 8) + 1;

        if ((fidx == (tidx + 1)) || (fidx == (tidx - 1))) {
            if ((this.side == 0) && (fidy == (tidy - 1))) {
                return true;
            }
            if ((this.side == 1) && (fidy == (tidy + 1))) {
                return true;
            }
        }

        return false;
    }
    //</editor-fold>
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="INNER OBJECTS. empty">
    //</editor-fold>
}
