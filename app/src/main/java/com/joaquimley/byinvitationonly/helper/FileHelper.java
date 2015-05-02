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
import android.util.Log;

import com.firebase.client.Firebase;
import com.joaquimley.byinvitationonly.R;
import com.joaquimley.byinvitationonly.model.Conference;
import com.joaquimley.byinvitationonly.model.Session;

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

    private void importConferenceData(BufferedReader br, Firebase fb) throws IOException {

        Firebase db = fb.child("ConferenceData");

        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split("\\|");
            db.child(parts[0]).setValue(parts[1]);
        }

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
