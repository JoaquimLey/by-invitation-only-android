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

package com.joaquimley.byinvitationonly;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.joaquimley.byinvitationonly.db.DatabaseHelper;

/**
 * Singleton class with application shared data
 */

public class BioApp extends Activity {

    protected static final String TAG = BioApp.class.getSimpleName();

    protected static BioApp sInstance = null;
    protected static DatabaseHelper sDatabase = null;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        sInstance = this;
        sDatabase = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sDatabase != null) {
            OpenHelperManager.releaseHelper();
            sDatabase = null;
        }
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

    /**
     * Get application local database, create if not exists
     *
     * @param context self explanatory
     * @return DatabaseHelper current database
     */
    public DatabaseHelper getDb(Context context) {
        if(sDatabase == null) {
            sDatabase = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }
        return sDatabase;
    }

    public void setDb(DatabaseHelper dataBase) {
        sDatabase = dataBase;
    }

}
