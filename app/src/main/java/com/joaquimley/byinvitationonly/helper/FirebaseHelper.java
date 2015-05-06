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
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.joaquimley.byinvitationonly.BioApp;
import com.joaquimley.byinvitationonly.R;
import com.joaquimley.byinvitationonly.model.User;
import com.joaquimley.byinvitationonly.util.CommonUtils;

/**
 * Helper class, handles all Firebase communications
 */

public class FirebaseHelper {

    private static final String TAG = FirebaseHelper.class.getSimpleName();

    private FirebaseHelper() {
        // No args constructor, prevent instantiating
    }

    /**
     * Initiates firebase for the app
     *
     * @param context self explanatory
     * @return firebase reference
     */
    public static Firebase initiateFirebase(Context context) {
        Firebase.setAndroidContext(context);
        return new Firebase(context.getString(R.string.firebase_url));
    }

    /**
     * Retrieve or Create a child reference from root reference
     *
     * @param firebaseRef self explanatory
     * @param childName   self explanatory
     * @return Firebase child reference
     */
    public static Firebase getChildRef(Firebase firebaseRef, String childName) {
        if (firebaseRef == null) {
            Log.e(TAG, "Firebase Reference is null");
            return null;
        }
        return firebaseRef.child(childName);
    }

    /**
     * Change current user's availability/visibility
     *
     * @param context  self explanatory
     * @param user     self explanatory
     * @param usersRef self explanatory
     * @param listener handle callback response
     */
    public static void changeAvailabilityState(final Context context, final Firebase usersRef, final User user,
                                               final Firebase.CompletionListener listener) {

        if (user == null) {
            Log.e(TAG, context.getString(R.string.error_user_null));
            return;
        }

        if (!CommonUtils.isOnline(context)) {
            Toast.makeText(context, context.getString(R.string.error_no_internet), Toast.LENGTH_LONG).show();
            return;
        }

        usersRef.child(BioApp.getCurrentUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null && user.getId() != null && user.isVisible()) {
                    usersRef.child(user.getId()).removeValue(listener);
                    BioApp.setCurrentUserId("");
                    user.setId(BioApp.getCurrentUserId());
                    user.setVisible(!user.isVisible());
                    // TODO: Go to participants list Activity

                } else if (!user.isVisible()) {
                    Firebase newUserRef = usersRef.push();
                    BioApp.setCurrentUserId(newUserRef.getKey());
                    user.setId(BioApp.getCurrentUserId());
                    user.setVisible(true);
                    newUserRef.setValue(user, listener);
                    // TODO: Go to participants list Activity

                } else {
                    user.setVisible(!user.isVisible());
                    Toast.makeText(context, "Server synchronization problem, please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError arg0) {
            }
        });
    }
}