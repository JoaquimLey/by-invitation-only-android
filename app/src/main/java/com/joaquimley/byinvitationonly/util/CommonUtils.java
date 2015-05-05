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
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.View;

import com.joaquimley.byinvitationonly.R;
import com.joaquimley.byinvitationonly.model.Session;
import com.joaquimley.byinvitationonly.model.User;

/**
 * UI/UX related customization other util methods
 */

public class CommonUtils {

    private static final String TAG = CommonUtils.class.getSimpleName();

    public static void changeStatusIcon(User user, View btnView) {
        if (user == null) {
            Log.e(TAG, "changeStatusIcon(): User is null");
            return;
        }
        if (user.isVisible()) {
            btnView.setBackgroundResource(R.drawable.ic_status_green);
            return;
        }
        btnView.setBackgroundResource(R.drawable.ic_status_red);
    }

    /**
     * Clears the action bar label and sets a custom icon
     *
     * @param actionBar self explanatory
     * @param title     self explanatory
     * @param iconId    self explanatory
     */
    public static void simplifyActionBay(ActionBar actionBar, String title, int iconId) {
        if (actionBar == null) {
            Log.e(TAG, "simplifyActionBay(): Action bar is null");
            return;
        }
        actionBar.setTitle(title);
        actionBar.setIcon(iconId);
    }

    /**
     * Check device connection status
     *
     * @param context self explanatory
     * @return boolean result
     */
    public static boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            Log.e(TAG, "isOnline(): Unable to fetch device connection status", e);
            return false;
        }
    }

    /**
     * Creates a simple alert dialog with given params
     *
     * @param activity self explanatory
     * @param title    self explanatory
     * @param message  self explanatory
     */
    public static void createAlertDialog(final Activity activity, String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.cancel();
                        activity.finish();
                    }
                });
    }

    /**
     * Transforms a Date object into a string to be shown on a List
     *
     * @param session self explanatory
     * @return date with the following format HHh - dd/MOT
     */
    public static String getSessionListDay(Session session) {
        return session.getDate() + " | " + session.getStartHour() + "h " + session.getEndHour() + "h";
    }

    /**
     * Converts a month number into a 3-Letter format/name
     *
     * @param month number
     * @return the month name (abbreviated)
     */
    public static String getMonthName(int month) {
        String[] monthNames = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
        return monthNames[month];
    }
}
