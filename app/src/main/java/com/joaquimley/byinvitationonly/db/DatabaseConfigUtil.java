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

package com.joaquimley.byinvitationonly.db;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import com.joaquimley.byinvitationonly.model.Session;
import com.joaquimley.byinvitationonly.model.User;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * ORMLite - Creates the configuration file for the ORMLite
 * Configuration file PATH: ./main/src/raw/ormlite_config.txt
 */

public class DatabaseConfigUtil extends OrmLiteConfigUtil {

    private static final Class[] classes = new Class[]{Session.class, User.class};

    /**
     * This must be called as a stand alone app by a JRE instance and NOT by android.
     * It will create an Ormlite config file that will make the reflection for annotation and more easier and faster.
     * Make sure you have pathOfProject/build/classes/debug in your class path when running!
     * <p/>
     * Reference tutorial: https://www.youtube.com/watch?v=beb-n2yq0kM
     * <p/>
     * Project specific: working class path: C:/Git/by-invitation-only-android/app/src/main/
     *
     * @param args none will be used.
     * @throws SQLException, IOException
     */
    public static void main(String[] args) throws SQLException, IOException {
        writeConfigFile(new File("ormlite_config.txt"), classes);
    }
}
