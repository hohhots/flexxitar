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
public class Cell {

    //<editor-fold defaultstate="collapsed" desc="VARIABLES.">
    //<editor-fold defaultstate="collapsed" desc="STATIC variables. empty">
    private static final int objLimit = CONFIG.getAllCellNum();
    private static int objCount = 0;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="OBJECT variables.">
    private int id = 0;
    private XitarBoard xitarboard = null;
    private Xitar xitar = null;
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="METHODS.">
    //<editor-fold defaultstate="collapsed" desc="STATIC methods.">
    public static synchronized Cell getInstance(int id, XitarBoard xitarboard) throws CreateObjectOutOfLimit {
        if (objCount < objLimit) {
            ++objCount;
            return new Cell(id, xitarboard);
        } else {
            throw new CreateObjectOutOfLimit("Create Cell object Out of limit.");
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="INITIAL methods.">
    private Cell(int id, XitarBoard xitarboard) {
        this.id = id;
        this.xitarboard = xitarboard;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="SET methods.">
    public void setXitar(Xitar x) {
        if (CONFIG.DEBUG) {
            System.out.println("Cell - setXitar()");
        }

        if (xitar != null) {
            xitar.setCell(null);
        }

        Xitar tx = x;
        if (tx != null) {
            tx = xitarboard.changeHooToBars(tx, this);
            tx.setCell(this);
        }
        xitar = tx;
    }

    public void removeXitar() {
        if (CONFIG.DEBUG) {
            System.out.println("Cell - removeXitar()");
        }
        xitar = null;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GET methods.">
    public int getId() {
        return id;
    }

    public Xitar getXitar() {
        return xitar;
    }

    public XitarBoard getXitarBoard() {
        return xitarboard;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="IS methods.">
    private Boolean isFirstStepCell(Cell tcell) {
        if (CONFIG.DEBUG) {
            System.out.println("Xitar-isFirstStepCell()");
        }
        Queue steps = xitarboard.getSteps();
        Integer[] laststep = xitarboard.getLastStep();

        if (laststep[0] == -1) { //first player's first step
            if ((id != 11) && (id != 12)) {
                return false;
            } else {
                if ((id != (tcell.getId() - 16))) {
                    return false;
                }
            }
        } else {
            if (steps.size() == 1) { //second player 's frist step
                int ceid = xitarboard.getCell(laststep[1]).getId();
                if ((ceid != (id - 24))) {
                    return false;
                }
            }
        }

        return true;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CAN methods. empty">
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Has methods.">
    public Boolean hasXitar() {
        if (CONFIG.DEBUG) {
            System.out.println("Cell - hasXitar()");
        }
        if (xitar != null) {
            return true;
        }
        return false;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="DELETE methods. empty">
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="ADD methods. empty">
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="other methods.">
    public Boolean moveToThisCell(Player player, Cell fcell) { //xitar move from fcell to this cell
        if (CONFIG.DEBUG) {
            System.out.println("Cell - moveToThisCell()" + fcell.getId() + " - " + id);
        }
        if (this.selectable(player, fcell)) {
            xitarboard.addStep(fcell.getId(), id);
            Xitar x = fcell.getXitar();
            fcell.removeXitar();
            setXitar(x);
            return true;
        }

        return false;
    }

    public Boolean selectable(Player player, Cell fcell) { //if this cell can be selected for xitar move from fcell to this cell
        if (CONFIG.DEBUG) {
            System.out.println("Cell-selectable()" + " - " + fcell.getId() + " - " +
                    fcell.getXitar().isMyChess(player) + " - " + fcell.getXitar().isMyTurnToMove(player) + " - " + this.existCellTo(fcell));
        }
        if (fcell.getXitar().isMyChess(player) && fcell.getXitar().isMyTurnToMove(player)) {
            if (this.existCellTo(fcell)) {
                return true;
            }
        }

        return false;
    }

    private Boolean existCellTo(Cell fcell) { //if is cell that xitar in fcell can move to
        if (CONFIG.DEBUG) {
            System.out.println("Cell-existCellTo()");
        }

        if (hasXitar()) { //this cell has chess
            if (!fcell.getXitar().inGroup(xitar)) {
                if (fcell.getXitar().moveToChessRule(this)) {
                    return true;
                }
            }
        } else {//this cell has no chess
            Integer[] laststep = xitarboard.getLastStep();
            if ((laststep[1] == -1) || (xitarboard.getSteps().size() == 1)) { //last steps is empty when black's first step,white's is last steps have one step
                if (fcell.isFirstStepCell(this)) {
                    return true;
                }
            } else {
                if (fcell.getXitar().moveToCellRule(this)) {
                    return true;
                }
            }

        }
        return false;
    }

    public String toString() {
        if (CONFIG.DEBUG) {
            System.out.println("Cell - toString()");
        }
        if (xitar == null) {
            return "id - " + id + ";\n";
        } else {
            return "id - " + id + "; xitar - " + xitar.getSide() + "\n";
        }
    }
    //</editor-fold>
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="INNER OBJECTS. empty">
    //</editor-fold>
}
