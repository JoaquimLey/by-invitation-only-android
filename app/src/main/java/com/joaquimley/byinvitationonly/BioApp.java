package com.joaquimley.byinvitationonly;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Singleton class with application shared data
 */

public class BioApp extends Activity {

    protected static final String TAG = BioApp.class.getSimpleName();
    protected static BioApp sInstance = null;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        sInstance = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /** If we are going to use local database **/
//        if (sDatabase != null) {
//            OpenHelperManager.releaseHelper();
//            sDatabase = null;
//        }
    }

    /**
     * Create if not exist, singleton instance of "BioApp"
     *
     * @return BioApp.class instance
     */
    public static BioApp getInstance() {
        if (sInstance == null) {
            sInstance = new BioApp();
        }
        return sInstance;
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
            Log.e(TAG, "Unable to fetch device connection status", e);
            return false;
        }
    }
}
