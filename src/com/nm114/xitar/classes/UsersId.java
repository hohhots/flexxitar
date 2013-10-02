/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nm114.xitar.classes;

import java.util.*;

/**
 *
 * @author brgd
 */
public abstract class UsersId {

    private static ArrayList<Integer> Ids = setIds();
    private static ArrayList<Integer> usedIds = new ArrayList<Integer>();
    private static Random rand = new Random();

    private static ArrayList<Integer> setIds() {
        ArrayList<Integer> t = new ArrayList<Integer>();

        int userNum = CONFIG.getUsersNum();

        int i = 0;
        while (i < userNum) {
            t.add(i + CONFIG.USERID_STARTNUM);
            i++;
        }

        return t;
    }

    public static synchronized int useUserID() { //get user id
        int nowId = 0;

        int a = rand.nextInt(Ids.size());
        nowId = Ids.get(a);
        Ids.remove(a);
        usedIds.add(nowId);

        //System.out.println("ids=" + Ids.size() + " " + "usedid=" + usedIds.size());
        return nowId;
    }

    public static synchronized void unuseUserID(int id) { //return user id
        Integer tid = new Integer(id);
        Ids.add(tid);
        usedIds.remove(tid);

        //System.out.println("id=" + id + " " + "ids=" + Ids.size() + " " + "usedid=" + usedIds.size());
    }
}
