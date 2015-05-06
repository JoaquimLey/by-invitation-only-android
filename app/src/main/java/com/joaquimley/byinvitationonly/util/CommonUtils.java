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
