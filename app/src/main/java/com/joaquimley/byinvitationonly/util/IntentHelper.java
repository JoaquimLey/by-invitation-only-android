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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.joaquimley.byinvitationonly.R;
import com.joaquimley.byinvitationonly.activities.EditUserDetailsActivity;
import com.joaquimley.byinvitationonly.activities.MainActivity;
import com.joaquimley.byinvitationonly.activities.ParticipantsList;
import com.joaquimley.byinvitationonly.model.User;

import java.util.ArrayList;

/**
 * Helper class designed for Intent calls
 */

public class IntentHelper {

    /**
     * @param context self explanatory
     * @param user    user to be edited if applied
     * @return the intent
     */

    /**
     * @param context self explanatory
     * @return the intent
     */
    public static Intent createMainActivityIntent(Context context) {
        Intent mainActivityIntent = new Intent(context, MainActivity.class);
//        createMainActivityIntent.putExtra("user", user);
        return mainActivityIntent;
    }

    public static Intent createParticipantsListIntent(Context context, User user, ArrayList<User> users) {
        Intent participantsListIntent = new Intent(context, ParticipantsList.class);
        participantsListIntent.putExtra("user", user);
        participantsListIntent.putExtra("users", users);
        return participantsListIntent;
    }

    public static Intent createUserDetailsActivityIntent(Context context, User user) {
        Intent userDetailsActivityIntent = new Intent(context, EditUserDetailsActivity.class);
        if (user != null) {
            userDetailsActivityIntent.putExtra("user", user);
        }
        return userDetailsActivityIntent;
    }

    /**
     * Create and start a email/share send Intent
     *
     * @param context          self explanatory
     * @param destinationEmail self explanatory
     */
    public static void createEmailIntent(Context context, String destinationEmail) {
        Intent emailIntent;
        emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{destinationEmail});
        context.startActivity(emailIntent);
    }

    /**
     * Create and start a dialer Intent with given number by @param
     *
     * @param context     self explanatory
     * @param phoneNumber self explanatory
     */
    public static void createDialerIntent(Context context, String phoneNumber) {
        if (!isTelephonyEnabled(context)) {
            Toast.makeText(context, context.getString(R.string.error_no_dialer), Toast.LENGTH_LONG).show();
            return;
        }

        Intent dialerIntent = new Intent(Intent.ACTION_DIAL);
        dialerIntent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(dialerIntent);
    }

    /**
     * Check if current has Telephony capabilities
     *
     * @param context self explanatory
     * @return boolean value with capability
     */
    private static boolean isTelephonyEnabled(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null && tm.getSimState() == TelephonyManager.SIM_STATE_READY;
    }
}
