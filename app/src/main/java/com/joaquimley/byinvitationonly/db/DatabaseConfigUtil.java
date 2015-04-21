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

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import com.joaquimley.byinvitationonly.model.Conference;
import com.joaquimley.byinvitationonly.model.Contact;
import com.joaquimley.byinvitationonly.model.Favorite;
import com.joaquimley.byinvitationonly.model.Speaker;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * ORMLite - Creates the configuration file for the ORMLite
 * Configuration file PATH: ./main/src/raw/ormlite_config.txt
 */

public class DatabaseConfigUtil extends OrmLiteConfigUtil{

    private static final Class[] classes = new Class[]{Conference.class, Speaker.class, Contact.class, Favorite.class};

    /**
     * This must be called as a stand alone app by a JRE instance and NOT by android.
     * It will create an Ormlite config file that will make the reflection for annotation and more easier and faster.
     * Make sure you have pathOfProject/build/classes/debug in your class path when running!
     *
     * Reference tutorial: https://www.youtube.com/watch?v=beb-n2yq0kM
     *
     * Project specific: working class path: C:/Git/by-invitation-only-android/app/src/main/
     *
     * @param args none will be used.
     * @throws SQLException, IOException
     */
    public static void main(String[] args) throws  SQLException, IOException{
        writeConfigFile(new File("ormlite_config.txt"), classes);
    }
}
