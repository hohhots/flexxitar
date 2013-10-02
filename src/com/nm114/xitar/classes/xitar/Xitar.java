/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nm114.xitar.classes.xitar;

import java.util.*;

import com.nm114.xitar.classes.*;
import com.nm114.xitar.exception.*;

/**
 *
 * @author brgd
 */
public abstract class Xitar {

    //<editor-fold defaultstate="collapsed" desc="VARIABLES.">
    //<editor-fold defaultstate="collapsed" desc="STATIC variables.">
    private static final int objLimit = CONFIG.getAllChessNum();
    private static int objCount = 0;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="OBJECT variables.">
    protected int side = -1;
    protected XitarBoard xitarboard = null;
    protected Cell cell = null;
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="METHODS.">
    //<editor-fold defaultstate="collapsed" desc="STATIC methods.">
    public static synchronized Xitar getInstance(int side, int id) throws CreateObjectOutOfLimit {
        if (objCount < objLimit) {
            ++objCount;
            return getXitar(side, id);
        } else {
            throw new CreateObjectOutOfLimit("Create Xitar object Out of limit.");
        }
    }

    private static Xitar getXitar(int side, int id) {
        if (CONFIG.DEBUG) {
            System.out.println("Xitar-getXitar()");
        }
        Xitar tc = null;
        if (id == Hasag.id) {
            tc = new Hasag(side);
        }
        if (id == Mori.id) {
            tc = new Mori(side);
        }
        if (id == Teme.id) {
            tc = new Teme(side);
        }
        if (id == Bars.id) {
            tc = new Bars(side);
        }
        if (id == Haan.id) {
            tc = new Haan(side);
        }
        if (id == Hoo.id) {
            tc = new Hoo(side);
        }
        if (tc == null) {
            System.out.println("Wrong xitar id for create xitar object!");
        }
        return tc;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="INITIAL methods.">
    public Xitar() {
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET methods.">
    public void setCell(Cell ce) { //just can call from cell
        cell = ce;
        if ((cell != null) && (xitarboard == null)) {
            this.xitarboard = cell.getXitarBoard();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GET methods.">
    abstract public int getId();

    public int getSide() {
        return side;
    }

    public Cell getCell() {
        return cell;
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
    //<editor-fold defaultstate="collapsed" desc="other methods.">
    public Boolean isMyChess(Player player) {
        if (CONFIG.DEBUG) {
            System.out.println("Xitar-isMyChess()");
        }
        if (xitarboard.getDesk().isFirstPlayer(player)) {
            if (this.side == CONFIG.BLACK_USER) {
                return true;
            }
        } else {
            if (this.side == CONFIG.WHITE_USER) {
                return true;
            }
        }

        return false;
    }

    public Boolean isMyTurnToMove(Player player) {
        if (CONFIG.DEBUG) {
            System.out.println("Xitar-isMyTurnToMove()");
        }
        Integer[] ls = xitarboard.getLastStep();
        Boolean fp = xitarboard.getDesk().isFirstPlayer(player);
        int lscside = -1;
        if (ls[1] != -1) { //not first step
            lscside = xitarboard.getCell(ls[1]).getXitar().getSide();
            if (fp && (lscside == CONFIG.XITAR_WHITE)) {
                return true;
            }
            if (!fp && (lscside == CONFIG.XITAR_BLACK)) {
                return true;
            }
        } else { //black's first step
            if (fp) {
                if ((this.cell.getId() == 11) || (this.cell.getId() == 12)) {
                    return true;
                }
            }
        }
        return false;
    }

    abstract protected Boolean moveToCellRule(Cell tcell);

    abstract protected Boolean moveToChessRule(Cell tcell);

    public Boolean inGroup(Xitar che) {
        if (CONFIG.DEBUG) {
            System.out.println("Xitar-inGroup()");
        }

        if (side == che.getSide()) {
            return true;
        }
        return false;
    }
    //</editor-fold>
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="INNER OBJECTS. empty">
    //</editor-fold>
}
