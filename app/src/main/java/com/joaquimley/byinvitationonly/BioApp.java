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

import com.firebase.client.Firebase;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.joaquimley.byinvitationonly.db.DatabaseHelper;
import com.joaquimley.byinvitationonly.model.Conference;
import com.joaquimley.byinvitationonly.model.Speaker;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    //********************************** Debug/Dummy methods **********************************//
    // TODO: Delete or comment before going to production, ensure there are 0 (ZERO) usages

    /**
     * DEBUG: Method to create dummy entries in the database for testing purposes
     * @param activity self explanatory
     * @return list with with dummy "Conference" items
     */
    public static ArrayList<Conference> createDummyEntries(Activity activity) {
        ArrayList<Conference> dummyItems = new ArrayList<>();

        dummyItems.add(new Conference("Title example numero uno", new Speaker("Bill Gates", null, null, null), "boa cena lorem ipsoidum feaefoajefapfjaeofa", "https://lh4.ggpht.com/C_B6YXyEaPxC1KYRARAU7xqrMDFf38DC4AKpazbrP4hgfNft1afvdET2Bffk8ZVayXrG=w170", new Date(System.currentTimeMillis())));
        dummyItems.add(new Conference("Title example numero dois", new Speaker("Steve Jobs", null, null, null), "boa cena lorem ipsoidum feaefoajefapfjaeofa", "https://lh4.ggpht.com/C_B6YXyEaPxC1KYRARAU7xqrMDFf38DC4AKpazbrP4hgfNft1afvdET2Bffk8ZVayXrG=w170", new Date(System.currentTimeMillis())));
        dummyItems.add(new Conference("Title example numero tres", new Speaker("Romain Guy", null, null, null), "boa cena lorem ipsoidum feaefoajefapfjaeofa", "https://lh4.ggpht.com/C_B6YXyEaPxC1KYRARAU7xqrMDFf38DC4AKpazbrP4hgfNft1afvdET2Bffk8ZVayXrG=w170", new Date(System.currentTimeMillis())));
        dummyItems.add(new Conference("Title example numero 4", new Speaker("Zecas", null, null, null), "boa cena lorem ipsoidum feaefoajefapfjaeofa", "https://lh4.ggpht.com/C_B6YXyEaPxC1KYRARAU7xqrMDFf38DC4AKpazbrP4hgfNft1afvdET2Bffk8ZVayXrG=w170", new Date(System.currentTimeMillis())));
        return dummyItems;
    }


    /**
     * Create dummy entries to be inserted on Firebase Cloud
     *
     * @param firebaseChildRef reference for child
     */
    public static void createDummyTalkEntries(Firebase firebaseChildRef){
        Map<String, Conference> talks = new HashMap<>();
        Conference t1 = new Conference("This is my dummy Title t1", new Speaker("Bill Gates", null, null,
                "http://feelgrafix.com/data_images/out/18/926083-bill-gates.jpg"),
                "This is a dummy desp, Lorem Ipsum is simply dummy text of the printing and typesetting industry." +
                        " Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
                "http://www.showmetech.com.br/wp-content/uploads/2013/03/windows-phone-logo.jpg", new Date(System.currentTimeMillis()));

        Conference t2 = new Conference("This is my dummy Title t2", new Speaker("Bill Gates", null, null,
                "http://feelgrafix.com/data_images/out/18/926083-bill-gates.jpg"),
                "This is a dummy desp, Lorem Ipsum is simply dummy text of the printing and typesetting industry." +
                        " Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
                "http://www.showmetech.com.br/wp-content/uploads/2013/03/windows-phone-logo.jpg", new Date(System.currentTimeMillis()));

        Conference t3 = new Conference("This is my dummy Title t3", new Speaker("Bill Gates", null, null,
                "http://feelgrafix.com/data_images/out/18/926083-bill-gates.jpg"),
                "This is a dummy desp, Lorem Ipsum is simply dummy text of the printing and typesetting industry." +
                        " Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
                "http://www.showmetech.com.br/wp-content/uploads/2013/03/windows-phone-logo.jpg", new Date(System.currentTimeMillis()));

        Conference t4 = new Conference("This is my dummy Title t4", new Speaker("Bill Gates", null, null,
                "http://feelgrafix.com/data_images/out/18/926083-bill-gates.jpg"),
                "This is a dummy desp, Lorem Ipsum is simply dummy text of the printing and typesetting industry." +
                        " Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
                "http://www.showmetech.com.br/wp-content/uploads/2013/03/windows-phone-logo.jpg", new Date(System.currentTimeMillis()));


        talks.put(t1.getTitle(), t2);
        talks.put(t2.getTitle(), t2);
        talks.put(t3.getTitle(), t3);
        talks.put(t4.getTitle(), t4);
        firebaseChildRef.setValue(talks);
    }

}
