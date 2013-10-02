/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nm114.xitar.classes.xitar;

import java.util.*;

import com.nm114.xitar.classes.*;
import com.nm114.xitar.exception.*;
import com.nm114.xitar.servlets.*;

/**
 *
 * @author brgd
 */
public class XitarBoard {

    //<editor-fold defaultstate="collapsed" desc="VARIABLES.">
    //<editor-fold defaultstate="collapsed" desc="STATIC variables.">
    private static final int objLimit = CONFIG.ALL_DESKS_NUM;
    private static int objCount = 0;
    private static final Integer[][] standardposition = {{0, 1, 0}, {0, 2, 1}, {0, 3, 2}, {0, 5, 3}, {0, 4, 4}, {0, 3, 5}, {0, 2, 6}, {0, 1, 7}, {0, 6, 8}, {0, 6, 9}, {0, 6, 10}, {0, 6, 11}, {0, 6, 12}, {0, 6, 13}, {0, 6, 14}, {0, 6, 15},
        {1, 6, 48}, {1, 6, 49}, {1, 6, 50}, {1, 6, 51}, {1, 6, 52}, {1, 6, 53}, {1, 6, 54}, {1, 6, 55}, {1, 1, 56}, {1, 2, 57}, {1, 3, 58}, {1, 5, 59}, {1, 4, 60}, {1, 3, 61}, {1, 2, 62}, {1, 1, 63}};
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="OBJECT variables.">
    private String id;
    private Desk desk = null;
    private Cell[] cells = null;
    private Integer[][] initposition = standardposition;
    private ArrayList<Integer[]> currentposition = new ArrayList<Integer[]>(); //position after every last step
    private Queue<Integer[]> steps = new LinkedList<Integer[]>();//{{12,34}} chess move from cell 12 to cell 34
    //Integer[] ff = {20, 26};
    //steps.offer(ff);
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="METHODS.">
    //<editor-fold defaultstate="collapsed" desc="STATIC methods.">
    public static synchronized XitarBoard getInstance(Desk desk) throws CreateObjectOutOfLimit {
        if (objCount < objLimit) {
            ++objCount;
            return new XitarBoard(desk);
        } else {
            throw new CreateObjectOutOfLimit("Create XitarBoard object Out of limit.");
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="INITIAL methods.">
    private XitarBoard(Desk desk) {
        id = desk.getRoom().getId() + CONFIG.ROOM_DESK_SEPERATOR + desk.getId();
        this.desk = desk;
        initCells();
        initXitars();

        int len = initposition.length;
        for (int i = 0; i < len; i++) {
            Integer[] ff = {initposition[i][0], initposition[i][1], initposition[i][2]};
            currentposition.add(ff);
        }

    }

    private void initCells() {
        if (CONFIG.DEBUG) {
            System.out.println("XitarBoard - initCells()");
        }
        int z = CONFIG.CELLS_NUM;
        Cell[] tc = new Cell[z];

        int i;
        try {
            for (i = 0; i < z; i++) {
                tc[i] = Cell.getInstance(i, this);
            }
        } catch (CreateObjectOutOfLimit e) {
            e.printStackTrace();
        }

        cells = tc;
    }

    private void initXitars() {
        if (CONFIG.DEBUG) {
            System.out.println("XitarBoard - initXitars()");
        }

        int z = initposition.length;
        Xitar tc;

        int i;
        try {
            for (i = 0; i < z; i++) {
                tc = Xitar.getInstance(initposition[i][0], initposition[i][1]);
                cells[initposition[i][2]].setXitar(tc);
            }
        } catch (CreateObjectOutOfLimit e) {
            e.printStackTrace();
        }

    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET methods. empty">
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="GET methods.">
    public String getId() {
        return id;
    }

    public Queue getSteps() {
        return steps;
    }

    public Integer[] getLastStep() {
        if (CONFIG.DEBUG) {
            System.out.println("XitarBoard - getLastStep()");
        }
        Integer[] ls = {-1, -1};
        try {
            ls = (Integer[]) ((LinkedList) steps).getLast();
        } catch (Exception e) {
        }
        //System.out.println(ls[0] + " - " + ls[1]);
        return ls;
    }

    public Desk getDesk() {
        return desk;
    }

    public String getUrl() {
        return XitarServlet.getUrl() + "/" + id;
    }

    public Cell[] getCells() {
        return cells;
    }

    public Cell getCell(int id) {
        return cells[id];
    }

    public String getPositionInJSON() {
        if (CONFIG.DEBUG) {
            System.out.println("XitarBoard - getPositionInJSON()");
        }

        StringBuilder position = new StringBuilder();
        position.append("[");

        for (Integer[] s : initposition) {
            position.append("[" + s[0] + "," + s[1] + "," + s[2] + "],");
        }
        position.deleteCharAt(position.length() - 1).append("]");

        return position.toString();
    }

    public String getStepsInJSON() {
        if (CONFIG.DEBUG) {
            System.out.println("XitarBoard - getStepsInJSON()");
        }
        StringBuilder tsteps = new StringBuilder();

        for (Integer[] s : steps) {
            tsteps.append("[" + s[0] + "," + s[1] + "],");
        }
        if (tsteps.length() > 1) {
            tsteps.insert(0, ("["));
            tsteps.deleteCharAt(tsteps.length() - 1).append("]");
        }

        return tsteps.toString();
    }

    public String getLastStepInJSon() {
        if (CONFIG.DEBUG) {
            System.out.println("XitarBoard - getLastStepInJSon()");
        }
        Integer[] t = getLastStep();

        StringBuilder tstep = new StringBuilder();
        tstep.append("[" + t[0] + "," + t[1] + "]");

        return tstep.toString();
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="CAN methods. empty">
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Has methods. empty">
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="DELETE methods. empty">
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="ADD methods.">

    public void addStep(int fcid, int tcid) {
        if (CONFIG.DEBUG) {
            System.out.println("XitarBoard - addStep()");
        }
        Integer[] step = {fcid, tcid};

        Integer[] t = getLastStep();
        if (t[0] == -1) {
            steps.poll(); //clear steps
        }
        steps.offer(step); //offer a step

        removeXitarInCell(tcid); //clear in this cell origin xitar
        changeCellOfXitar(fcid, tcid);//change xitar position
    }

    public Xitar changeHooToBars(Xitar x, Cell c) {
        Xitar tx = x;
        try {
            if (x.getId() == Hoo.id) {
                if (((Hoo)x).hooInChangeState(x, c)) {
                    tx = Xitar.getInstance(x.getSide(), Bars.id);//int side, int id, xitarboard
                }
            }
        } catch (CreateObjectOutOfLimit e) {
            e.printStackTrace();
        }

        return tx;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="other methods.">
    public void removeXitarInCell(int tcid) {
        Iterator iter = currentposition.iterator();
        Integer[] p;
        while (iter.hasNext()) {
            p = (Integer[]) iter.next();
            if (p[2] == tcid) {
                currentposition.remove(p);
                break;
            }
        }
    }

    public void changeCellOfXitar(int fcid, int tcid) {
        Iterator iter = currentposition.iterator();
        Integer[] p;
        int index;
        while (iter.hasNext()) {
            p = (Integer[]) iter.next();
            index = currentposition.indexOf(p);
            if (p[2] == fcid) {
                p[2] = tcid;
                currentposition.set(index, p);
                break;
            }
        }
    }

    public String toString() {
        StringBuilder contents = new StringBuilder();

        for (int i = 0; i < this.cells.length; i++) {
            contents.append(this.cells[i].toString());
        }

        return contents.toString();
    }
    //</editor-fold>
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="INNER OBJECTS. empty">
    //</editor-fold>
}
