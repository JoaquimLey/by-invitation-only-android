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
package com.joaquimley.byinvitationonly.db;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.joaquimley.byinvitationonly.BioApp;
import com.joaquimley.byinvitationonly.model.Contact;
import com.joaquimley.byinvitationonly.model.Favorite;
import com.joaquimley.byinvitationonly.model.Speaker;
import com.joaquimley.byinvitationonly.model.Talk;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class is responsible for the DataBase handling using ORMLite
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "sal15.db";
    private static final int DATABASE_VERSION = 1;

    // DAOs
    private static Dao<Contact, Integer> mContactDao = null;
    private static RuntimeExceptionDao<Contact, Integer> mContactRuntimeDao = null;
    private static Dao<Favorite, Integer> mFavoriteDao = null;
    private static RuntimeExceptionDao<Favorite, Integer> mFavoriteRuntimeDao = null;
    private static Dao<Speaker, Integer> mSpeakerDao = null;
    private static RuntimeExceptionDao<Speaker, Integer> mSpeakerRuntimeDao = null;
    private static Dao<Talk, Integer> mTalkDao = null;
    private static RuntimeExceptionDao<Talk, Integer> mTalkRuntimeDao = null;

    /**
     * Constructor for DatabaseHelper ORMLite
     *
     * @param context self explanatory
     */
    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    /**
     * Called when the database is first created. Usually you should
     * call createTable statements here to create the tables that will store
     * your data.
     *
     * @param db self explanatory
     * @param source connection source
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource source) {
        createTables(source);
        Log.i(TAG, "onCreate(): Creating Tables");
    }

    /**
     * Creates all necessary tables for the database
     *
     * @param source connection source
     */
    private void createTables(ConnectionSource source) {

        try {
            // Add all necessary tables inside this try/catch clause:
            TableUtils.createTable(source, Contact.class);
            TableUtils.createTable(source, Favorite.class);
            TableUtils.createTable(source, Speaker.class);
            TableUtils.createTable(source, Talk.class);
        } catch (SQLException ex){
            Log.e(TAG, "[SQLException] Unable to create database", ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * Clears table and adds new items from @param
     *
     * @param contactDao self explanatory
     * @param newElements self explanatory
     */
    public void updateContactsTable(Dao<Contact, Integer> contactDao, List<Contact> newElements){
        try {
            TableUtils.createTableIfNotExists(getConnectionSource(), Contact.class);
            TableUtils.clearTable(getConnectionSource(), Contact.class);
            for(Contact contact : newElements){
                contactDao.createIfNotExists(contact);
            }
        } catch (SQLException e) {
            Log.e(TAG, "Couldn't update contact table");
        }
    }

    /**
     * Clears table and adds new items from @param
     *
     * @param favoriteDao self explanatory
     * @param newElements self explanatory
     */
    public void updateFavoriteTable(Dao<Favorite, Integer> favoriteDao, List<Favorite> newElements){
        try {
            TableUtils.createTableIfNotExists(getConnectionSource(), Favorite.class);
            TableUtils.clearTable(getConnectionSource(), Favorite.class);
            for(Favorite favorite : newElements){
                favoriteDao.createIfNotExists(favorite);
            }
        } catch (SQLException e) {
            Log.e(TAG, "Couldn't update favorite table");
        }
    }

    /**
     * Clears table and adds new items from @param
     *
     * @param speakerDao self explanatory
     * @param newElements self explanatory
     */
    public void updateSpeakersTable(Dao<Speaker, Integer> speakerDao, List<Speaker> newElements){
        try {
            TableUtils.createTableIfNotExists(getConnectionSource(), Speaker.class);
            TableUtils.clearTable(getConnectionSource(), Speaker.class);
            for(Speaker speaker : newElements){
                speakerDao.createIfNotExists(speaker);
            }
        } catch (SQLException e) {
            Log.e(TAG, "Couldn't update speaker table");
        }
    }

    /**
     * Clears table and adds new items from @param
     *
     * @param talkDao self explanatory
     * @param newElements self explanatory
     */
    public void updateTalksTable(Dao<Talk, Integer> talkDao, List<Talk> newElements){
        try {
            TableUtils.createTableIfNotExists(getConnectionSource(), Talk.class);
            TableUtils.clearTable(getConnectionSource(), Talk.class);
            for(Talk talk : newElements){
                talkDao.createIfNotExists(talk);
            }
        } catch (SQLException e) {
            Log.e(TAG, "Couldn't update talk table");
        }
    }

    /**
     * DEBUG: Method to create dummy entries in the database for testing purposes
     * @param activity self explanatory
     * @return list with with dummy "Talk" items
     */
    public static ArrayList<Talk> createDummyEntries(Activity activity) {
        ArrayList<Talk> dummyItems = new ArrayList<>();

        dummyItems.add(new Talk("Title example numero uno", new Speaker("Bill Gates", null, null, null), "boa cena lorem ipsoidum feaefoajefapfjaeofa", "https://lh4.ggpht.com/C_B6YXyEaPxC1KYRARAU7xqrMDFf38DC4AKpazbrP4hgfNft1afvdET2Bffk8ZVayXrG=w170", new Date(System.currentTimeMillis()), false));
        dummyItems.add(new Talk("Title example numero dois", new Speaker("Steve Jobs", null, null, null), "boa cena lorem ipsoidum feaefoajefapfjaeofa", "https://lh4.ggpht.com/C_B6YXyEaPxC1KYRARAU7xqrMDFf38DC4AKpazbrP4hgfNft1afvdET2Bffk8ZVayXrG=w170", new Date(System.currentTimeMillis()), false));
        dummyItems.add(new Talk("Title example numero tres", new Speaker("Romain Guy", null, null, null), "boa cena lorem ipsoidum feaefoajefapfjaeofa", "https://lh4.ggpht.com/C_B6YXyEaPxC1KYRARAU7xqrMDFf38DC4AKpazbrP4hgfNft1afvdET2Bffk8ZVayXrG=w170", new Date(System.currentTimeMillis()), false));
        dummyItems.add(new Talk("Title example numero 4", new Speaker("Zecas", null, null, null), "boa cena lorem ipsoidum feaefoajefapfjaeofa", "https://lh4.ggpht.com/C_B6YXyEaPxC1KYRARAU7xqrMDFf38DC4AKpazbrP4hgfNft1afvdET2Bffk8ZVayXrG=w170", new Date(System.currentTimeMillis()), true));
        return dummyItems;
    }

    /**
     * DEBUG: Test Database reachable
     *
     * @param context self explanatory
     */
    public static void testDatabaseReadability(Context context) {
        Log.i(TAG, "testDatabaseReadability()");
        try {
            Dao<Talk, Integer> talkDao = BioApp.getInstance().getDb(context).getTalkDao();
            Log.i(TAG, "Getting DAO");

            ArrayList<Talk> talks = (ArrayList<Talk>) talkDao.queryForAll();
            System.out.println(talks);
        } catch (SQLException ex) {
            Log.e(TAG, "[SQLException] Unable to retrieve DAO", ex);
        }
    }

    /**
     * Overrides onUpgrade to overwrite the existing tables in db
     * Increments DataBase Version.
     *
     * @param db DataBase
     * @param oldVersion self explanatory
     * @param newVersion The new Version of the DataBase (after upgrade)
     * @throws RuntimeException
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource source, int oldVersion, int newVersion) {

        try {
            TableUtils.dropTable(source, Contact.class, true);
            TableUtils.dropTable(source, Favorite.class, true);
            TableUtils.dropTable(source, Speaker.class, true);
            TableUtils.dropTable(source, Talk.class, true);
            Log.i(TAG, "onUpgrade(): Dropping Tables");
            // Calling onCreate method to "re-create" the Database
            onCreate(db, source);
            Log.i(TAG, "Creating Tables (re-creating DataBase)");
        } catch (SQLException ex) {
            Log.e(TAG, "[SQLException] Unable to drop tables (Delete DataBase)", ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * Close all Database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        mContactDao = null;
        mContactRuntimeDao = null;
        mTalkDao = null;
        mTalkRuntimeDao = null;
        mSpeakerDao = null;
        mSpeakerRuntimeDao = null;
        mFavoriteDao = null;
        mFavoriteRuntimeDao = null;
        Log.i(TAG, "Closing Database");
    }

    /**
     * Returns the DATABASE_VERSION value
     *
     * @return current DataBase Version
     */
    public int getDataBaseVersion() {
        return DATABASE_VERSION;
    }

    //********************************** DAO Factory **********************************//

    /**
     * Returns the Database Access Object (DAO) for our Contact class.
     * Creates or just give the cached value.
     *
     * @return mContactDao
     */
    public Dao<Contact, Integer> getContactDao() {
        if(mContactDao == null){
            try {
                mContactDao = getDao(Contact.class);
            } catch (SQLException e) {
                Log.e(TAG, "Couldn't get artistDao");
            }
        }
        return mContactDao;
    }

    /**
     * Returns the Database Access Object (DAO) for our Contact class.
     * Creates or just give the cached value.
     * Note: Using RuntimeExceptionDao will ignore all exceptions.
     *
     * @return mContactRuntimeDao
     */
    public RuntimeExceptionDao<Contact, Integer> getContactRuntimeDao(){
        if(mContactRuntimeDao == null){
            mContactRuntimeDao = getRuntimeExceptionDao(Contact.class);
        }
        return mContactRuntimeDao;
    }

    /**
     * Returns the Database Access Object (DAO) for our Favorite class.
     * Creates or just give the cached value.
     *
     * @return mFavoriteDao
     * @throws SQLException
     */
    public Dao<Favorite, Integer> getFavoriteDao() throws SQLException{
        if(mFavoriteDao == null){
            mFavoriteDao = getDao(Favorite.class);
        }
        return mFavoriteDao;
    }

    /**
     * Returns the Database Access Object (DAO) for our Talk class.
     * Creates or just give the cached value.
     * Note: Using RuntimeExceptionDao will ignore all exceptions.
     *
     * @return mFavoriteRuntimeDao
     */
    public RuntimeExceptionDao<Favorite, Integer> getFavoriteRuntimeDao(){
        if(mFavoriteRuntimeDao == null){
            mFavoriteRuntimeDao = getRuntimeExceptionDao(Favorite.class);
        }
        return mFavoriteRuntimeDao;
    }

    /**
     * Returns the Database Access Object (DAO) for our Speaker class.
     * Creates or just give the cached value.
     *
     * @return mSpeakerDao
     * @throws SQLException
     */
    public Dao<Speaker, Integer> getSpeakerDao() throws SQLException{
        if(mSpeakerDao == null){
            mSpeakerDao = getDao(Speaker.class);
        }
        return mSpeakerDao;
    }

    /**
     * Returns the Database Access Object (DAO) for our Speaker class.
     * Creates or just give the cached value.
     * Note: Using RuntimeExceptionDao will ignore all exceptions.
     *
     * @return mSpeakerRuntimeDao
     */
    public RuntimeExceptionDao<Speaker, Integer> getSpeakereRuntimeDao(){
        if(mSpeakerRuntimeDao == null){
            mSpeakerRuntimeDao = getRuntimeExceptionDao(Speaker.class);
        }
        return mSpeakerRuntimeDao;
    }

    /**
     * Returns the Database Access Object (DAO) for our Talk class.
     * Creates or just give the cached value.
     *
     * @return mTalkDao
     * @throws SQLException
     */
    public Dao<Talk, Integer> getTalkDao() throws SQLException{
        if(mTalkDao == null){
            mTalkDao = getDao(Talk.class);
        }
        return mTalkDao;
    }

    /**
     * Returns the Database Access Object (DAO) for our Talk class.
     * Creates or just give the cached value.
     * Note: Using RuntimeExceptionDao will ignore all exceptions.
     *
     * @return mUserRuntimeDoo
     */
    public RuntimeExceptionDao<Talk, Integer> getTalkRuntimeDao(){
        if(mTalkRuntimeDao == null){
            mTalkRuntimeDao = getRuntimeExceptionDao(Talk.class);
        }
        return mTalkRuntimeDao;
    }
}