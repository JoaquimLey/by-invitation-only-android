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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.joaquimley.byinvitationonly.R;

/**
 * Helper class designed for Intent calls
 */

public class IntentHelper {

    /**
     * Create and start a email/share send Intent
     *
     * @param activity self explanatory
     * @param destinationEmail self explanatory
     */
    public static void createEmailIntent(Activity activity, String destinationEmail){
        Intent emailIntent;
        emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{destinationEmail});
        activity.startActivity(Intent.createChooser(emailIntent, activity.getString(R.string.text_contact)));
    }

    /**
     * Create and start a dialer Intent with given number by @param
     *
     * @param context self explanatory
     * @param phoneNumber self explanatory
     */
    public static void createDialerIntent(Context context, String phoneNumber){
        if(!isTelephonyEnabled(context)){
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
    private static boolean isTelephonyEnabled(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null && tm.getSimState() == TelephonyManager.SIM_STATE_READY;
    }
}
