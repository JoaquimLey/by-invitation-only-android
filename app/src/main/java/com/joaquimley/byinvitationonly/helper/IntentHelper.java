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

package com.joaquimley.byinvitationonly.helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.joaquimley.byinvitationonly.R;
import com.joaquimley.byinvitationonly.activities.BookmarksListActivity;
import com.joaquimley.byinvitationonly.activities.EditUserDetailsActivity;
import com.joaquimley.byinvitationonly.activities.MainActivity;
import com.joaquimley.byinvitationonly.activities.ParticipantsListActivity;
import com.joaquimley.byinvitationonly.activities.SessionsListActivity;
import com.joaquimley.byinvitationonly.model.User;

/**
 * Helper class designed for Intent calls
 */

public class IntentHelper {

    /**
     * Creates a MainActivity intent
     *
     * @param context self explanatory
     * @return the intent
     */
    public static Intent createMainActivityIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    /**
     * Creates a ParticipantsListActivity intent
     *
     * @param context self explanatory
     * @return the intent
     */
    public static Intent createParticipantsListIntent(Context context) {
        return new Intent(context, ParticipantsListActivity.class);
    }

    /**
     * Creates a SessionsListActivity intent
     *
     * @param context self explanatory
     * @return the intent
     */
    public static Intent createSessionListActivityIntent(Context context) {
        return new Intent(context, SessionsListActivity.class);
    }

    /**
     * Creates a BookmarksListActivity intent
     *
     * @param context self explanatory
     * @return the intent
     */
    public static Intent createBookmarksActivity(Context context) {
        return new Intent(context, BookmarksListActivity.class);
    }

    public static Intent createUserDetailsActivityIntent(Context context, User user) {
        Intent userDetailsActivityIntent = new Intent(context, EditUserDetailsActivity.class);
        if (user != null) {
            userDetailsActivityIntent.putExtra("user", user);
        }
        return userDetailsActivityIntent;
    }

    public static Intent createPickImageIntent(Context context) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        return galleryIntent;
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
