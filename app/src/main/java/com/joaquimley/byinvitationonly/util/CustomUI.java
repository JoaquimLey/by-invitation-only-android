/*
 * Copyright (c) 2015 Joaquim Ley - www.joaquimley.com
 * All rights reserved.
 *
 * Redistribution, modification or use of source and binary forms are NOT allowed
 * without permission. The name of Joaquim Ley, or joaquimley.com may not be used
 * to endorse products derived without previous authorization.
 * THIS SOFTWARE IS PROVIDED 'AS IS' AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */

package com.joaquimley.byinvitationonly.util;

import android.app.ActionBar;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Simple UI modifications util class
 */

public class CustomUi {

    private static final String TAG = CustomUi.class.getSimpleName();

    /**
     * Clears the action bar label and sets a custom icon
     *
     * @param actionBar self explanatory
     * @param iconId self explanatory
     */
    public static void setSimpleActionBar(ActionBar actionBar, int iconId){
        if(actionBar == null){
            Log.e(TAG, "setSimpleActionBar(): Action bar is null");
            return;
        }
        actionBar.setTitle("");
        actionBar.setIcon(iconId);
    }

    /**
     * Transforms a Date object into a string to be shown on a List
     *
     * @param date self explanatory
     * @return date with the following format HHh - dd/MOT
     */
    public static String getListDay(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY) + "h - " + cal.get(Calendar.DAY_OF_MONTH) + "/" +
                getMonthName(cal.get(Calendar.MONTH));
    }

    /**
     * Converts a month number into a 3-Letter format/name
     *
     * @param month number
     * @return the month name (abbreviated)
     */
    public static String getMonthName(int month){
        String[] monthNames = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
        return monthNames[month];
    }
}
