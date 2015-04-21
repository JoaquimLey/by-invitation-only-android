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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Helper class to parse values from .CSV files (Talks/Sessions)
 */

public class CsvFileHelper {

    private static final String TAG = CsvFileHelper.class.getSimpleName();

    /**
     * *********** Dados Conferencia *************
     */

    /**
     * Updates conference data from assets file "ConferenceData"
     *
     * @param context self explanatory
     * @param firebaseRef self explanatory
     */
    public void updateConferenceData(Context context, Firebase firebaseRef) {

        try {

            InputStream inputStream = context.getAssets().open(context.getString(R.string.file_conference_data)); // binary
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream); // text conversion
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader); // processed stream

            importConferenceData(bufferedReader, firebaseRef);
            bufferedReader.close();

            // Access assets file
            inputStream = context.getAssets().open(context.getString(R.string.file_sessions_data)); // binary
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            importSessionData(bufferedReader, firebaseRef);
            bufferedReader.close();

        } catch (IOException e) {
            Log.e(TAG, "updateConferenceData(): Error parsing/import conference data");
        }
    }

    /**
     * Method used to read conference data
     *
     * @param bufferedReader self explanatory
     * @param firebaseConferenceChild self explanatory
     */
    private void importConferenceData(BufferedReader bufferedReader, Firebase firebaseConferenceChild) {
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split("\\|"); // separar a linha lida pelo delimitador '|' - campo|valor da conferência
                firebaseConferenceChild.child(parts[0]).setValue(parts[1]); // definir o valor de cada campo
            }
        } catch (IOException e) {
            Log.e(TAG, "importConferenceData(): Error reading from bufferedReader");
            e.printStackTrace();
        }
    }

    /**
     * *********** Dados Pessoas *************
     */
    public void updateDadosPessoas(Context context) {

        // URL base definido pelo utilizador na caixa de texto - URL da base de dados Firebase
        String firebaseUrl = "https://by-invitation-only.firebaseio.com";
        // Referência para a raiz do Firebase
        Firebase fb = new Firebase(firebaseUrl);
        try {

            InputStream is = context.getAssets().open("DadosPessoas.csv"); // binário
            InputStreamReader reader = new InputStreamReader(is); // conversão para texto
            BufferedReader br = new BufferedReader(reader); // stream processada

            //método auxiliar para leitura dos dados da conferência
            importConferenceData(br, fb);
            br.close();

            // Acesso ao ficheiro na pasta de assets (incluído no .apk da aplicação)
            is = context.getAssets().open("DadosPessoas.csv"); // binário
            reader = new InputStreamReader(is); // conversão para texto
            br = new BufferedReader(reader); // stream processada

            //método auxiliar para leitura dos dados das sessões da conferência
            importSessionData(br, fb);
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method used to read conference data
     *
     * @param bufferedReader self explanatory
     * @param firebaseChildRef self explanatory
     */
    private void importSessionData(BufferedReader bufferedReader, Firebase firebaseChildRef) {

        String[] fields = new String[0];
        try {
            fields = bufferedReader.readLine().split("\\|");
        } catch (IOException e) {
            // TODO: Log
            e.printStackTrace();
        }

        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                Firebase item = firebaseChildRef.push(); // criação de um ID para uma sessão

                String[] parts = line.split("\\|", -1); // separar os diversos valores de uma sessão
                for (int i = 0; i < fields.length; i++) {
                    item.child(fields[i]).setValue(parts[i]); // definir o valor de cada campo
                }
            }
        } catch (IOException e) {
            // TODO: Log
            e.printStackTrace();
        }
    }
//
//
//
//    private void createData() {
//        try {
//            InputStream is = getAssets().open("DadosPessoas.csv");
//            InputStreamReader reader = new InputStreamReader(is);
//            BufferedReader br = new BufferedReader(reader);
//
//            String[] camposUser = br.readLine().split("\\|");
//
//            ArrayList<User> userList = new ArrayList<>();
//
//            String line;
//            while ((line = br.readLine()) != null) {
//                ArrayList<String> arrayList = new ArrayList<>();
//                String[] parts = line.split("\\|", -1);
//                for (int i = 0; i < camposUser.length; i++) {
//                    arrayList.add(parts[i]);
//                }
//
//                userList.add(new User(Short.parseShort(parts[0]),
//                        parts[1], Short.parseShort(parts[2]), parts[3], parts[4]));
//            }
//            ProgramData.getInstance().setUserList(userList);
//
//            /*******************************************************/
//
//            is = getAssets().open("SessionsData.csv");
//            reader = new InputStreamReader(is);
//            br = new BufferedReader(reader);
//
//            String[] campos = br.readLine().split("\\|");
//
//            ArrayList<Session> sessionsList = new ArrayList<>();
//
//            line = null;
//            while ((line = br.readLine()) != null) {
//
//                ArrayList<String> arrayList = new ArrayList<>();
//                String[] parts = line.split("\\|", -1);
//                for (int i = 0; i < campos.length; i++) {
//                    arrayList.add(parts[i]);
//                }
//                sessionsList.add(new Session(Short.parseShort(parts[0]), Data.parseSession(parts[1]), Time.parse(parts[2]),
//                        Time.parse(parts[3]), parts[4], parts[5], parts[6], parts[7], parts[8]));
//            }
//            ProgramData.getInstance().setSessionsList(sessionsList);
//            ProgramData.getInstance().setSelectedAndClassifiedSession(sessionsList.size());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private boolean temDadosPessoais() {
//        String nome = sharedPreferences.getString("chave_nome", "");
//        String email = sharedPreferences.getString("chave_email", "");
//
//        if (!nome.isEmpty() && !email.isEmpty()) {
//            this.contacto = new Contacto(nome, email);
//            return true;
//        } else {
//            this.contacto = null;
//            return false;
//        }
//    }
}
