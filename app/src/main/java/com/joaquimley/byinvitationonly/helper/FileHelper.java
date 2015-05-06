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

package com.joaquimley.byinvitationonly.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.joaquimley.byinvitationonly.BioApp;
import com.joaquimley.byinvitationonly.R;
import com.joaquimley.byinvitationonly.model.Conference;
import com.joaquimley.byinvitationonly.model.Session;
import com.joaquimley.byinvitationonly.model.User;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
     * Parses data User from sharedPreferences
     *
     * @param context           self explanatory
     * @param sharedPreferences self explanatory
     * @return user object if data is available
     */
    public static User getUserFromSharedPreferences(Context context, SharedPreferences sharedPreferences) {

        // Using userName value to assert there is user data on sharedPreferences
        String userName = sharedPreferences.getString(context.getString(R.string.shared_pref_user_details_name), "");
        if (userName == null || userName.isEmpty()) {
            Toast.makeText(context, context.getString(R.string.error_create_user_profile_first), Toast.LENGTH_SHORT).show();
            return null;
        }

        String userId = sharedPreferences.getString(context.getString(R.string.shared_pref_user_details_id), "");
        String userEmail = sharedPreferences.getString(context.getString(R.string.shared_pref_user_details_email), "");
        String userDescription = sharedPreferences.getString(context.getString(R.string.shared_pref_user_details_description), "");
        String userPhotoBase64String = sharedPreferences.getString(context.getString(R.string.shared_pref_user_details_photo_base64), "");
        boolean userLastAvailabilityStatus = sharedPreferences.getBoolean(context.getString(R.string.shared_pref_user_details_availability), false);

        User user;
        if (!userName.isEmpty() && !userEmail.isEmpty() && !userDescription.isEmpty() && !userPhotoBase64String.isEmpty()) {

            user = new User(userName, userEmail, userDescription, userPhotoBase64String, userLastAvailabilityStatus);
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
    public static void updateUserDataToSharedPreferences(Context context, SharedPreferences sharedPreferences, User user) {
        sharedPreferences.edit().putString(context.getString(R.string.shared_pref_user_details_id), BioApp.getCurrentUserId()).apply();
        sharedPreferences.edit().putString(context.getString(R.string.shared_pref_user_details_name), user.getName()).apply();
        sharedPreferences.edit().putString(context.getString(R.string.shared_pref_user_details_email), user.getEmail()).apply();
        sharedPreferences.edit().putString(context.getString(R.string.shared_pref_user_details_description), user.getDescription()).apply();
        sharedPreferences.edit().putString(context.getString(R.string.shared_pref_user_details_photo_base64), user.getPhotoBase64()).apply();
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_user_details_availability), user.isVisible()).apply();
    }

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

    public static void exportSessions(Context context, Firebase firebaseChildRef) {

        BufferedReader bufferedReader = FileHelper.getBufferedReaderFromAssets(context, context.getString(R.string.file_sessions_data));
        if (bufferedReader == null) {
            Log.e(TAG, "exportSessions(): Csv file not found");
            return;
        }

        String[] values;
        try {
            values = bufferedReader.readLine().split(context.getString(R.string.csv_split));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Firebase item = firebaseChildRef.push();

                String[] parts = line.split("\\|", -1);
                for (int i = 0; i < values.length; i++) {
                    item.child(values[i]).setValue(parts[i]);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "exportSessions(): Couldn't get bufferedReader");
            e.printStackTrace();
        }
    }

    /**
     * Decodes base64String given by @param and returns the image file's uri
     *
     * @param base64String self explanatory
     * @return correspondent uri
     */
    public static File decodeBase64ToFile(Context context, String base64String) {

        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, byteArrayOutputStream);

        File file = new File(context.getCacheDir(), "userProfilePic");
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
            byte[] bitmapData = byteArrayOutputStream.toByteArray();
            fileOutputStream.write(bitmapData);

            fileOutputStream.flush();
            fileOutputStream.close();
            return file;

        } catch (FileNotFoundException e) {
            Log.e(TAG, "encodeImageToBase64(): ERROR file not found");
            return null;
        } catch (IOException e) {
            Log.e(TAG, "encodeImageToBase64(): ERROR writing values");
            return null;
        }
    }

    /**
     * Encode a file @param Uri into a base64String
     * WARNING: <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/> required
     *
     * @param context self explanatory
     * @param uri     from the file that will get the conversion
     * @return base64string
     */
    public static String encodeUriToBase64(Context context, Uri uri) {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(getImageFilePathFromUri(context, uri));
            byte[] bytes;
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            bytes = output.toByteArray();
            return Base64.encodeToString(bytes, Base64.DEFAULT);

        } catch (FileNotFoundException e) {
            Log.e(TAG, "encodeImageToBase64(): ERROR file not found");
            return null;
        } catch (IOException e) {
            Log.e(TAG, "encodeImageToBase64(): ERROR writing values");
            return null;
        }
    }

    /**
     * Gets the path from a Uri file, image files only
     *
     * @param context  self explanatory
     * @param imageUri self explanatory
     * @return the picture path String
     */
    private static String getImageFilePathFromUri(Context context, Uri imageUri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(imageUri,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String imagePath = cursor.getString(columnIndex);
        cursor.close();
        return imagePath;
    }
}
