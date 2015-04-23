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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.joaquimley.byinvitationonly.BioApp;
import com.joaquimley.byinvitationonly.model.Session;
import com.joaquimley.byinvitationonly.model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class is responsible for the DataBase handling using ORMLite
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "sal15.db";
    private static final int DATABASE_VERSION = 1;

    // DAOs
    private static Dao<User, Integer> mContactDao = null;
    private static RuntimeExceptionDao<User, Integer> mContactRuntimeDao = null;
    //    private static Dao<Favorite, Integer> mFavoriteDao = null;
//    private static RuntimeExceptionDao<Favorite, Integer> mFavoriteRuntimeDao = null;
//    private static Dao<Speaker, Integer> mSpeakerDao = null;
//    private static RuntimeExceptionDao<Speaker, Integer> mSpeakerRuntimeDao = null;
    private static Dao<Session, Integer> mTalkDao = null;
    private static RuntimeExceptionDao<Session, Integer> mTalkRuntimeDao = null;
    private List<Session> sessions;

    /**
     * Constructor for DatabaseHelper ORMLite
     *
     * @param context self explanatory
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    /**
     * Called when the database is first created. Usually you should
     * call createTable statements here to create the tables that will store
     * your data.
     *
     * @param db     self explanatory
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
            TableUtils.createTable(source, User.class);
//            TableUtils.createTable(source, Favorite.class);
//            TableUtils.createTable(source, Speaker.class);
            TableUtils.createTable(source, Session.class);
        } catch (SQLException ex) {
            Log.e(TAG, "[SQLException] Unable to create database", ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * Clears table and adds new items from @param
     *
     * @param contactDao  self explanatory
     * @param newElements self explanatory
     */
    public void updateContactsTable(Dao<User, Integer> contactDao, List<User> newElements) {
        try {
            TableUtils.createTableIfNotExists(getConnectionSource(), User.class);
            TableUtils.clearTable(getConnectionSource(), User.class);
            for (User user : newElements) {
                contactDao.createIfNotExists(user);
            }
        } catch (SQLException e) {
            Log.e(TAG, "Couldn't update contact table");
        }
    }

//    /**
//     * Clears table and adds new items from @param
//     *
//     * @param favoriteDao self explanatory
//     * @param newElements self explanatory
//     */
//    public void updateFavoriteTable(Dao<Favorite, Integer> favoriteDao, List<Favorite> newElements){
//        try {
//            TableUtils.createTableIfNotExists(getConnectionSource(), Favorite.class);
//            TableUtils.clearTable(getConnectionSource(), Favorite.class);
//            for(Favorite favorite : newElements){
//                favoriteDao.createIfNotExists(favorite);
//            }
//        } catch (SQLException e) {
//            Log.e(TAG, "Couldn't update favorite table");
//        }
//    }

//    /**
//     * Clears table and adds new items from @param
//     *
//     * @param speakerDao  self explanatory
//     * @param newElements self explanatory
//     */
//    public void updateSpeakersTable(Dao<Speaker, Integer> speakerDao, List<Speaker> newElements) {
//        try {
//            TableUtils.createTableIfNotExists(getConnectionSource(), Speaker.class);
//            TableUtils.clearTable(getConnectionSource(), Speaker.class);
//            for (Speaker speaker : newElements) {
//                speakerDao.createIfNotExists(speaker);
//            }
//        } catch (SQLException e) {
//            Log.e(TAG, "Couldn't update speaker table");
//        }
//    }

    /**
     * Clears table and adds new items from @param
     *
     * @param talkDao     self explanatory
     * @param newElements self explanatory
     */
    public void updateSessionsTable(Dao<Session, Integer> talkDao, List<Session> newElements) {
        try {
            TableUtils.createTableIfNotExists(getConnectionSource(), Session.class);
            TableUtils.clearTable(getConnectionSource(), Session.class);
            for (Session session : newElements) {
                talkDao.createIfNotExists(session);
            }
        } catch (SQLException e) {
            Log.e(TAG, "Couldn't update talk table");
        }
    }

    /**
     * DEBUG: Test Database reachable
     *
     * @param context self explanatory
     */
    public static void testDatabaseReadability(Context context) {
        Log.i(TAG, "testDatabaseReadability()");
        try {
            Dao<Session, Integer> talkDao = BioApp.getInstance().getDb(context).getTalkDao();
            Log.i(TAG, "Getting DAO");

            ArrayList<Session> sessions = (ArrayList<Session>) talkDao.queryForAll();
            System.out.println(sessions);
        } catch (SQLException ex) {
            Log.e(TAG, "[SQLException] Unable to retrieve DAO", ex);
        }
    }

    /**
     * Overrides onUpgrade to overwrite the existing tables in db
     * Increments DataBase Version.
     *
     * @param db         DataBase
     * @param oldVersion self explanatory
     * @param newVersion The new Version of the DataBase (after upgrade)
     * @throws RuntimeException
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource source, int oldVersion, int newVersion) {

        try {
            TableUtils.dropTable(source, User.class, true);
//            TableUtils.dropTable(source, Favorite.class, true);
//            TableUtils.dropTable(source, Speaker.class, true);
            TableUtils.dropTable(source, Session.class, true);
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
//        mSpeakerDao = null;
//        mSpeakerRuntimeDao = null;
//        mFavoriteDao = null;
//        mFavoriteRuntimeDao = null;
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
     * Returns the Database Access Object (DAO) for our User class.
     * Creates or just give the cached value.
     *
     * @return mContactDao
     */
    public Dao<User, Integer> getContactDao() {
        if (mContactDao == null) {
            try {
                mContactDao = getDao(User.class);
            } catch (SQLException e) {
                Log.e(TAG, "Couldn't get artistDao");
            }
        }
        return mContactDao;
    }

    /**
     * Returns the Database Access Object (DAO) for our User class.
     * Creates or just give the cached value.
     * Note: Using RuntimeExceptionDao will ignore all exceptions.
     *
     * @return mContactRuntimeDao
     */
    public RuntimeExceptionDao<User, Integer> getContactRuntimeDao() {
        if (mContactRuntimeDao == null) {
            mContactRuntimeDao = getRuntimeExceptionDao(User.class);
        }
        return mContactRuntimeDao;
    }

//    /**
//     * Returns the Database Access Object (DAO) for our Favorite class.
//     * Creates or just give the cached value.
//     *
//     * @return mFavoriteDao
//     * @throws SQLException
//     */
//    public Dao<Favorite, Integer> getFavoriteDao() throws SQLException{
//        if(mFavoriteDao == null){
//            mFavoriteDao = getDao(Favorite.class);
//        }
//        return mFavoriteDao;
//    }
//
//    /**
//     * Returns the Database Access Object (DAO) for our Session class.
//     * Creates or just give the cached value.
//     * Note: Using RuntimeExceptionDao will ignore all exceptions.
//     *
//     * @return mFavoriteRuntimeDao
//     */
//    public RuntimeExceptionDao<Favorite, Integer> getFavoriteRuntimeDao(){
//        if(mFavoriteRuntimeDao == null){
//            mFavoriteRuntimeDao = getRuntimeExceptionDao(Favorite.class);
//        }
//        return mFavoriteRuntimeDao;
//    }

//    /**
//     * Returns the Database Access Object (DAO) for our Speaker class.
//     * Creates or just give the cached value.
//     *
//     * @return mSpeakerDao
//     * @throws SQLException
//     */
//    public Dao<Speaker, Integer> getSpeakerDao() throws SQLException {
//        if (mSpeakerDao == null) {
//            mSpeakerDao = getDao(Speaker.class);
//        }
//        return mSpeakerDao;
//    }
//
//    /**
//     * Returns the Database Access Object (DAO) for our Speaker class.
//     * Creates or just give the cached value.
//     * Note: Using RuntimeExceptionDao will ignore all exceptions.
//     *
//     * @return mSpeakerRuntimeDao
//     */
//    public RuntimeExceptionDao<Speaker, Integer> getSpeakereRuntimeDao() {
//        if (mSpeakerRuntimeDao == null) {
//            mSpeakerRuntimeDao = getRuntimeExceptionDao(Speaker.class);
//        }
//        return mSpeakerRuntimeDao;
//    }

    /**
     * Returns the Database Access Object (DAO) for our Session class.
     * Creates or just give the cached value.
     *
     * @return mTalkDao
     * @throws SQLException
     */
    public Dao<Session, Integer> getTalkDao() throws SQLException {
        if (mTalkDao == null) {
            mTalkDao = getDao(Session.class);
        }
        return mTalkDao;
    }

    /**
     * Returns the Database Access Object (DAO) for our Session class.
     * Creates or just give the cached value.
     * Note: Using RuntimeExceptionDao will ignore all exceptions.
     *
     * @return mUserRuntimeDoo
     */
    public RuntimeExceptionDao<Session, Integer> getTalkRuntimeDao() {
        if (mTalkRuntimeDao == null) {
            mTalkRuntimeDao = getRuntimeExceptionDao(Session.class);
        }
        return mTalkRuntimeDao;
    }

    public List<Session> getSessions() {
        return sessions;
    }
}