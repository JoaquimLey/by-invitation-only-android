package com.joaquimley.byinvitationonly.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.joaquimley.byinvitationonly.R;
import com.joaquimley.byinvitationonly.model.User;
import com.joaquimley.byinvitationonly.ui.NavigationDrawerFragment;
import com.joaquimley.byinvitationonly.util.ImageCircleTransform;
import com.squareup.picasso.Picasso;

/**
 * Helper class for Navigation Drawer
 */

public class NavigationDrawerHelper {

    private static final String TAG = NavigationDrawerHelper.class.getSimpleName();

    public static void loadProfileInfo(Context context, SharedPreferences sharedPreferences, NavigationDrawerFragment navigationDrawerFragment){
        User user = FileHelper.getUserFromSharedPreferences(context, sharedPreferences);
        if(user == null){
            Log.e(TAG, "loadProfileInfo(): User is null");
            return;
        }

        String photoUri = sharedPreferences.getString(context.getString(R.string.shared_pref_user_details_photo_uri), "");
        if(photoUri != null){
            Picasso.with(context).load(photoUri).transform(new ImageCircleTransform()).into(navigationDrawerFragment.getUserAvatar());
        } else {
            Picasso.with(context).load(R.drawable.image_placeholder).transform(new ImageCircleTransform()).into(navigationDrawerFragment.getUserAvatar());
        }
        navigationDrawerFragment.getUserName().setText(user.getName());
        navigationDrawerFragment.getUserEmail().setText(user.getEmail());
    }

}
