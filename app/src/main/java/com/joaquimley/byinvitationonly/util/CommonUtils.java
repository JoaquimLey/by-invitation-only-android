/*
 * GNU GENERAL PUBLIC LICENSE
 *                        Version 3, 29 June 2007
 *
 *     Copyright (c) 2015 Joaquim Ley <me@joaquimley.com>
 *
 *     This program is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU General Public License
 *     as published by the Free Software Foundation; either version 2
 *     of the License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.joaquimley.byinvitationonly.util;

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

import java.util.ArrayList;

/**
 * UI/UX related customization other util methods
 */

public class CommonUtils {

    private static final String TAG = CommonUtils.class.getSimpleName();

    /**
     * Changes icon background - Red Checked out, Green checked in
     *
     * @param user    to be considered
     * @param btnView view to have background refreshed
     */
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
     * Changes icon background - Red Checked out, Green checked in
     *
     * @param session    to be considered
     * @param btnView view to have background refreshed
     */
    public static void changeBookmarkIcon(Session session, View btnView) {
        if (session == null) {
            Log.e(TAG, "changeStatusIcon(): Session is null");
            return;
        }
        if (session.isBookmarked()) {
            btnView.setBackgroundResource(R.drawable.ic_star_selected);
            return;
        }
        btnView.setBackgroundResource(R.drawable.ic_star);
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
     * Removes the user from the instance usersList, should only be called by a "onChildRemoved()
     * listener
     *
     * @param user returned by dataSnapshot, compared with the current users list
     */
    public static boolean removeUserFromList(User user, ArrayList<User> usersList) {
        int removeUserIndex = 0;
        boolean removeUserFlag = false;

        for(int i = 0; i < usersList.size(); i++){
            if (user.getId().equals(usersList.get(i).getId())) {
                removeUserIndex = i;
                removeUserFlag = true;
            }
        }

        if(removeUserFlag){
            usersList.remove(removeUserIndex);
        }
        return removeUserFlag;
    }

    /**
     * Removes the user from the instance usersList, should only be called by a "onChildRemoved()
     * listener
     *
     * @param session returned by dataSnapshot, compared with the current sessions list
     */
    public static boolean removeSessionFromList(Session session, ArrayList<Session> sessionsList) {
        int removeSessionIndex = 0;
        boolean removeUserFlag = false;

        for(int i = 0; i < sessionsList.size(); i++){
            if (session.getId().equals(sessionsList.get(i).getId())) {
                removeSessionIndex = i;
                removeUserFlag = true;
            }
        }

        if(removeUserFlag){
            sessionsList.remove(removeSessionIndex);
        }
        return removeUserFlag;
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
     * Creates a simple Yes/No alert dialog with given params, returns user option with a boolean
     * handle in the called context
     *
     * @param activity self explanatory
     * @param title    self explanatory
     * @param message  self explanatory
     * @return boolean value user decision
     */
    public static boolean createDecisionDialog(final Activity activity, String title, String message) {
        final boolean[] result = new boolean[1];
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        result[0] = true;
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        result[0] = false;
                    }
                })
                .create().show();
        return result[0];
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
