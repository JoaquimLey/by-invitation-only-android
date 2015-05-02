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

package com.joaquimley.byinvitationonly.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.joaquimley.byinvitationonly.R;
import com.joaquimley.byinvitationonly.model.Conference;
import com.joaquimley.byinvitationonly.model.Session;
import com.joaquimley.byinvitationonly.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Helper class to parsing and editing/saving values from/to .CSV files
 */

public class FileHelper {

    private static final String TAG = FileHelper.class.getSimpleName();

    /**
     * Returns a bufferedReader from assets for the specific @param file
     *
     * @param fileName self explanatory
     * @param context  self explanatory
     * @return Buffered stream from file
     */
    public static BufferedReader getBufferedReaderFromAssets(Context context, String fileName) {

        InputStream inputStream;
        try {
            inputStream = context.getAssets().open(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            return new BufferedReader(inputStreamReader);
        } catch (IOException e) {
            Log.e(TAG, "getBufferedReadFromAssets(): Error getting stream");
            return null;
        }
    }

    /**
     * Get values from Csv file
     *
     * @param bufferedReader file complete path
     * @param rowValuesStart row number where the important values start
     * @param separator      CSV file separator
     * @param skipLines      number of lines to skip (0 for null)
     * @return ArrayList with string values
     */
    public static ArrayList<String> getValuesFromCsvFile(BufferedReader bufferedReader,
                                                         int rowValuesStart, char separator, int skipLines) {
        CSVReader reader;
        if (skipLines != 0) {
            reader = new CSVReader(bufferedReader, separator, '"', skipLines);
        } else {
            reader = new CSVReader(bufferedReader, separator);
        }

        ArrayList<String> values = new ArrayList<>();
        String[] nextRow;
        try {
            while ((nextRow = reader.readNext()) != null) {
                values.add(nextRow[rowValuesStart]);
            }
            bufferedReader.close();
            reader.close();
        } catch (IOException e) {
            Log.e(TAG, "getValuesFromCsvFile(): error parsing values");
            return null;
        }
        return values;
    }

    /**
     * Parses data User from sharedPreferences
     *
     * @param context           self explanatory
     * @param sharedPreferences self explanatory
     * @return user object if data is available
     */
    public static User getUserFromSharedPreferences(Context context, SharedPreferences sharedPreferences) {

        // Using userName value to assert there is info on sharedPreferences
        String userName = sharedPreferences.getString(context.getString(R.string.shared_pref_user_details_name), "");
        if (userName == null || userName.isEmpty()) {
            Toast.makeText(context, "Please create your user profile", Toast.LENGTH_SHORT).show();
            return null;
        }

        String userId = sharedPreferences.getString(context.getString(R.string.shared_pref_user_details_id), "");
        String userEmail = sharedPreferences.getString(context.getString(R.string.shared_pref_user_details_email), "");
        String userDescription = sharedPreferences.getString(context.getString(R.string.shared_pref_user_details_description), "");
        String userPhotoUrl = sharedPreferences.getString(context.getString(R.string.shared_pref_user_details_photo_url), "");
        boolean userLastAvailabilityStatus = sharedPreferences.getBoolean(context.getString(R.string.shared_pref_user_details_availability), false);

        User user;
        if (!userName.isEmpty() && !userEmail.isEmpty() && !userDescription.isEmpty() && !userPhotoUrl.isEmpty()) {

            user = new User(userName, userEmail, userDescription, userPhotoUrl, userLastAvailabilityStatus);
            if (!userId.isEmpty()) {
                user.setId(userId);
            }
            return user;
        }
        user = new User(userName, userEmail, userDescription, "", userLastAvailabilityStatus);

        if (!userId.isEmpty()) {
            user.setId(userId);
        }
        return user;
    }

    /**
     * Updates data on sharedPreferences with User @param information
     *
     * @param context           self explanatory
     * @param sharedPreferences self explanatory
     * @param user              which will be used to fill the details
     */
    public static void updateUserFromSharedPreferences(Context context, SharedPreferences sharedPreferences, User user) {
        sharedPreferences.edit().putString(context.getString(R.string.shared_pref_user_details_id), user.getName()).apply();
        sharedPreferences.edit().putString(context.getString(R.string.shared_pref_user_details_name), user.getName()).apply();
        sharedPreferences.edit().putString(context.getString(R.string.shared_pref_user_details_email), user.getEmail()).apply();
        sharedPreferences.edit().putString(context.getString(R.string.shared_pref_user_details_description), user.getDescription()).apply();
        sharedPreferences.edit().putString(context.getString(R.string.shared_pref_user_details_photo_url), user.getPhotoUrl()).apply();
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_user_details_availability), user.isVisible()).apply();
    }

    /**
     * Creates Conference object from "ConferenceData.csv" file in assets folder
     *
     * @param context self explanatory
     * @return Conference object
     */
    public static Conference importConferenceDataFromFile(Context context) {
        ArrayList<String> values = getValuesFromCsvFile(
                getBufferedReaderFromAssets(context, context.getString(R.string.file_conference_data)), 1, '|', 0);

        if (values == null || values.isEmpty()) {
            Log.e(TAG, "importConferenceData(): No values to import");
            return null;
        }
        return new Conference(values.get(0), values.get(1), values.get(2), values.get(3), values.get(4),
                values.get(5), values.get(6));
    }

    /**
     * Recursive method to read each row of "SessionsData.csv" creating "Session" objects, adding these
     * to sessions @param
     *
     * @param context   self explanatory
     * @param skipLines number of lines to skip (0 for null)
     */
    public static void importSessionDataFromFile(Context context, ArrayList<Session> sessions, int skipLines) {

        ArrayList<String> values = getValuesFromCsvFile(
                getBufferedReaderFromAssets(context, context.getString(R.string.file_sessions_data)),
                0, '|', skipLines);

        Log.e(TAG, "skipLines: " + skipLines);

        if (values == null || values.isEmpty()) {
            Log.e(TAG, "No values to import");
            return;
        }
        sessions.add(new Session(values.get(0), values.get(1), values.get(2), values.get(3), values.get(4),
                values.get(5), values.get(6), values.get(7)));
        // Recursive
        skipLines++;
        importSessionDataFromFile(context, sessions, skipLines);
    }
}
