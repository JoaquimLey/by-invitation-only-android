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

import com.joaquimley.byinvitationonly.R;
import com.joaquimley.byinvitationonly.model.Conference;
import com.joaquimley.byinvitationonly.model.Session;
import com.joaquimley.byinvitationonly.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Helper class to parsing values from .CSV files (Talks/Sessions)
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
    private static BufferedReader getBufferedReaderFromAssets(Context context, String fileName) {

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
    public static void importSessionDataFromFile(Context context, ArrayList<Session> sessions, int skipLines) throws IOException {

        BufferedReader br = getBufferedReaderFromAssets(context, context.getString(R.string.file_sessions_data));
        String[] campos = br.readLine().split("\\|");

        String line;
        while ((line=br.readLine())!=null) {

            String[] parts = line.split("\\|",-1); // separar os diversos valores de uma sessão
            for (int i=0; i<campos.length; i++) {
                item.child(campos[i]).setValue(parts[i]); // definir o valor de cada campo
            }
        }







        BufferedReader reader = getBufferedReaderFromAssets(context, context.getString(R.string.file_sessions_data));
        String row;
        int rowNumba = 0;
        try {
            if (reader != null) {
                while ((row = reader.readLine()) != null) {
                    Log.e(TAG, String.valueOf(rowNumba));
                    String[] values = row.split(context.getString(R.string.csv_split));
                    Log.e(TAG, "values: " + Arrays.toString(values));
//                    sessions.add(new Session(values[0], values[1],  values[2], values[3], values[4], values[5], values[6], values[7]));
                    rowNumba++;
//                    Log.e(TAG, values[0] + " " + values[1] + " " + values[2]+ " " + values[3]);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "No values to import");
        }

//        ArrayList<String> values = getValuesFromCsvFile(
//                getBufferedReaderFromAssets(context, context.getString(R.string.file_sessions_data)),
//                                            0, '|', skipLines);
//
//        Log.e(TAG, "skipLines: " + skipLines);
//
//        if (values == null || values.isEmpty()) {
//            Log.e(TAG, "No values to import");
//            return;
//        }
//
//        boolean flag = false;
//        for(int i = 0; i < values.size(); i++){
//            Log.e(TAG, "Line: " + skipLines + ", i value = " + i + " values.get(i): " + values.get(i));
//            if((values.get(i)).equals("")){
//                flag = true;
//            }
//        }

//        if(!flag){
//            sessions.add(new Session(values.get(0), values.get(1), values.get(2), values.get(3), values.get(4),
//                    values.get(5), values.get(6), values.get(7)));
//
//        }
//        Log.e(TAG, "Flag value: " + String.valueOf(flag));
        // Recursive
        skipLines++;
        importSessionDataFromFile(context, sessions, skipLines);
    }

    public static User hasPersonalData(SharedPreferences sharedPreferences) {
        String name = sharedPreferences.getString("chave_nome", "");
        String email = sharedPreferences.getString("chave_email", "");

        if (!name.isEmpty() && !email.isEmpty()) {
            return new User(sharedPreferences.getString("chave_nome", ""), sharedPreferences.getString("chave_email", ""), null);
        }
        return null;
    }
}
