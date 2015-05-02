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
    private static Dao<User, Integer> mUserDao = null;
    private static RuntimeExceptionDao<User, Integer> mUserRuntimeDao = null;
    private static Dao<Session, Integer> mSessionDao = null;
    private static RuntimeExceptionDao<Session, Integer> mSessionRuntimeDao = null;

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
        } catch (SQLException ex) {
            Log.e(TAG, "[SQLException] Unable to create database", ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * Clears table and adds new items from @param
     *
     * @param user self explanatory
     */
    public void updateCurrentUserTable(User user) {
        try {
            TableUtils.createTableIfNotExists(getConnectionSource(), User.class);
            TableUtils.clearTable(getConnectionSource(), User.class);
            mUserDao.createIfNotExists(user);
        } catch (SQLException e) {
            Log.e(TAG, "Couldn't update users table");
        }
    }

    /**
     * Clears table and adds new items from @param
     *
     * @param newElements self explanatory
     */
    public void updateUsersTable(List<User> newElements) {
        try {
            TableUtils.createTableIfNotExists(getConnectionSource(), User.class);
            TableUtils.clearTable(getConnectionSource(), User.class);
            for (User user : newElements) {
                mUserDao.createIfNotExists(user);
            }
        } catch (SQLException e) {
            Log.e(TAG, "Couldn't update users table");
        }
    }

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
            Log.e(TAG, "Couldn't update sessions table");
        }
    }

    /**
     * DEBUG METHOD: Test Database reachable
     *
     * @param context self explanatory
     */
    public static void testDatabaseReadability(Context context) {
        Log.i(TAG, "testDatabaseReadability()");
        try {
            Dao<Session, Integer> talkDao = BioApp.getInstance().getDb(context).getSessionDao();
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
        mUserDao = null;
        mUserRuntimeDao = null;
        mSessionDao = null;
        mSessionRuntimeDao = null;
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

    //*********************************************************************************//
    //********************************** DAO Factory **********************************//
    //*********************************************************************************//

    /**
     * Returns the Database Access Object (DAO) for our User class.
     * Creates or just give the cached value.
     *
     * @return mUserDao
     */
    public Dao<User, Integer> getUserDao() {
        if (mUserDao == null) {
            try {
                mUserDao = getDao(User.class);
            } catch (SQLException e) {
                Log.e(TAG, "Couldn't get artistDao");
            }
        }
        return mUserDao;
    }

    /**
     * Returns the Database Access Object (DAO) for our User class.
     * Creates or just give the cached value.
     * Note: Using RuntimeExceptionDao will ignore all exceptions.
     *
     * @return mUserRuntimeDao
     */
    public RuntimeExceptionDao<User, Integer> getUserRuntimeDao() {
        if (mUserRuntimeDao == null) {
            mUserRuntimeDao = getRuntimeExceptionDao(User.class);
        }
        return mUserRuntimeDao;
    }

    /**
     * Returns the Database Access Object (DAO) for our Session class.
     * Creates or just give the cached value.
     *
     * @return mSessionDao
     * @throws SQLException
     */
    public Dao<Session, Integer> getSessionDao() throws SQLException {
        if (mSessionDao == null) {
            mSessionDao = getDao(Session.class);
        }
        return mSessionDao;
    }

    /**
     * Returns the Database Access Object (DAO) for our Session class.
     * Creates or just give the cached value.
     * Note: Using RuntimeExceptionDao will ignore all exceptions.
     *
     * @return mUserRuntimeDoo
     */
    public RuntimeExceptionDao<Session, Integer> getSessionRuntimeDao() {
        if (mSessionRuntimeDao == null) {
            mSessionRuntimeDao = getRuntimeExceptionDao(Session.class);
        }
        return mSessionRuntimeDao;
    }
}