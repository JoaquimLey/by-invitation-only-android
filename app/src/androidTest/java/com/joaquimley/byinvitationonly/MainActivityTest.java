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

package com.joaquimley.byinvitationonly;

import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.firebase.client.Firebase;
import com.joaquimley.byinvitationonly.activities.EditUserDetailsActivity;
import com.joaquimley.byinvitationonly.activities.MainActivity;
import com.joaquimley.byinvitationonly.activities.ParticipantsListActivity;
import com.joaquimley.byinvitationonly.helper.FirebaseHelper;
import com.joaquimley.byinvitationonly.model.User;
import com.robotium.solo.Solo;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    protected static final String TAG = MainActivityTest.class.getSimpleName();
    private static final long DEFAULT_WAIT_TIME = 5000;
    protected static final String NO_MSG_ERROR = "No error message displayed";
    protected static final String WRONG_ACTIVITY_ERROR = "ERROR: Not expected activity showing";

    private MainActivity mActivity;
    protected Solo solo;
    // Views
    protected ImageButton mBtnStatus;
    protected ImageButton mBtnEditUser;
    protected Button mParticipantsListActivity;
    // Objects
    protected User mUser;
    private EditText mEditTestUserName;
    private EditText mEditTestUserEmail;
    private EditText mEditTestUserDescription;
    private SharedPreferences mSharedPreferences;
    private Firebase mSessionsRef;
    private Firebase mUsersRef;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    /**
     * On the start of the testing
     *
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation());
        setActivityInitialTouchMode(true);
        mActivity = getActivity();
        mSharedPreferences = mActivity.getSharedPreferences();

        mUser = BioApp.getInstance().getCurrentUser();
        if (mUser == null) {
            reCreateUserProfile(true, false);
        }

//        init();
    }

    /**
     * On finish
     *
     * @throws Exception
     */
    @Override
    protected void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    /**
     * Initialisation of UI components
     */
    public void init() {
        Firebase firebaseRef = FirebaseHelper.initiateFirebase(mActivity);
        mSessionsRef = FirebaseHelper.getChildRef(firebaseRef, mActivity.getString(R.string.firebase_child_sessions));
        mUsersRef = FirebaseHelper.getChildRef(firebaseRef, mActivity.getString(R.string.firebase_child_users));

        mEditTestUserName = (EditText) getActivity().findViewById(R.id.et_edit_user_details_name);
        mEditTestUserEmail = (EditText) getActivity().findViewById(R.id.et_edit_user_details_email);
        mEditTestUserDescription = (EditText) getActivity().findViewById(R.id.et_edit_user_details_description);
//        preConditions();
    }

    /**
     * Testes if start Activity isn't null
     */
    public void preConditions() {
        assertNotNull(mActivity);
//        assertTrue(WRONG_ACTIVITY_ERROR, solo.waitForActivity(MainActivity.class.getSimpleName()));
    }

    /**
     * Clears all sharedPreferences information on device, sets mUser to NULL
     */
    private boolean clearUserProfile(long waitTime) {
        if (mActivity.getSharedPreferences().edit().clear().commit()) {
            Log.i(TAG, "clearUserProfile(): " + mActivity.getSharedPreferences().edit().clear().commit());
            mUser = null;
            BioApp.getInstance().setCurrentUser(null);
            return true;
        }
        return false;
    }

    /**
     * Recreates user profile simulating user behavior
     */
    public void reCreateUserProfile(boolean forceUpdate, boolean shareInfo) {
        if (!forceUpdate) {
            if (mUser != null && mUser.getName() != null && !mUser.getName().isEmpty()) {
                return;
            }
        }

        if (clearUserProfile(DEFAULT_WAIT_TIME)) {
            solo.assertCurrentActivity(WRONG_ACTIVITY_ERROR, MainActivity.class);
        }

        solo.clickOnView(mActivity.findViewById(R.id.ib_user_status));
        if (solo.waitForText("New Profile")) {
            solo.clickOnText("Yes");
        }
        solo.waitForActivity(EditUserDetailsActivity.class);

        String newName = "Joaquim Ley";
        String newEmail = "me@joaquimley.com";
        String newDescription = "Android Developer\nGraphic Designer";

        init();
        // Name
        solo.clearEditText(0);
        solo.typeText(0, newName);
        // Email
        solo.clearEditText(1);
        solo.typeText(1, newEmail);
        // Description
        solo.clearEditText(2);
        solo.typeText(2, newDescription);

        solo.clickOnText(solo.getString(R.string.text_save));

        solo.waitForDialogToOpen();
        if (shareInfo) {
            solo.clickOnText("Yes");
        } else {
            solo.clickOnText("No");
        }

        mUser = BioApp.getInstance().getCurrentUser();
        assertTrue(WRONG_ACTIVITY_ERROR, solo.waitForActivity(MainActivity.class.getSimpleName()));
    }

    // -------------------------------------------------------------------------------------- //
    // --------------------------------------- Tests ---------------------------------------- //
    // -------------------------------------------------------------------------------------- //

    /**
     * Dado que estou na aplicação, Quando pressiono o botão "I'm here" e as informações do meu
     * contacto estão preenchidas (pelo menos nome e email), Então deve ser-me exibida uma mensagem
     * de confirmação da activação desta funcionalidade.
     */
    @SmallTest
    public void test01_Us4ShareDetails() {

        solo.clickOnView(mActivity.findViewById(R.id.ib_user_status));
        solo.waitForText(mActivity.getString(R.string.text_status_updated));
        mUser = BioApp.getInstance().getCurrentUser();
        assertTrue("User didn't change checked-in status", mUser.isVisible());
        assertTrue(getActivity().findViewById(R.id.ib_user_status) != null);
    }

    /**
     * Dado que estou na mensagem de confirmação da funcionalidade "I'm here", Quando pressiono no
     * botão "Sim" e tenho rede, Então os meus dados devem ser submetidos e partilhados com os
     * restantes participantes que activaram a funcionalidade "I'm here" e o botão deve mudar para
     * o estado activo.
     */
    @SmallTest
    public void test02_Us4CheckInConfirm() {
        init();
        solo.clickOnView(mActivity.findViewById(R.id.ib_user_status));
        solo.waitForText(mActivity.getString(R.string.text_status_updated));
        assertTrue("No status icon changed", getActivity().findViewById(R.id.ib_user_status) != null);
    }

    /**
     * Dado que estou na aplicação, Quando pressiono o botão "I'm here" e as informações do meu
     * contacto não estão preenchidas (pelo menos nome e email), Então deve ser-me exibida uma
     * mensagem perguntando se pretendo preencher as informações do meu contacto ou cancelar a acção.
     */
    @SmallTest
    public void test03_Us4CreateProfileMessage() {
        if(mUser.isVisible()){
            solo.clickOnView(getActivity().findViewById(R.id.ib_user_status));
            solo.waitForText(mActivity.getString(R.string.text_profile_updated));
        }

        if(clearUserProfile(DEFAULT_WAIT_TIME)){
            solo.assertCurrentActivity(WRONG_ACTIVITY_ERROR, MainActivity.class);
            solo.clickOnView(mActivity.findViewById(R.id.ib_user_status));
            assertTrue(solo.waitForText(mActivity.getString(R.string.error_must_create_profile_first)));
        }
    }

    /**
     * Dado que estou a visualizar a mensagem perguntando se pretendo preencher as informações do
     * meu contacto, Quando selecciono "Sim", Então devo ser reencaminhado para o ecrã de
     * preenchimento de detalhes do meu contacto.
     */
    @MediumTest
    public void test04_Us4CreateOrEditUserProfile() {
        if(clearUserProfile(DEFAULT_WAIT_TIME)){
            solo.assertCurrentActivity(WRONG_ACTIVITY_ERROR, MainActivity.class);
            solo.clickOnView(mActivity.findViewById(R.id.ib_user_status));
            solo.waitForText(mActivity.getString(R.string.error_must_create_profile_first));
            solo.clickOnText("Yes");
            solo.waitForActivity(EditUserDetailsActivity.class);
            solo.assertCurrentActivity(WRONG_ACTIVITY_ERROR, EditUserDetailsActivity.class);
        }
    }

    /**
     * Dado que estou a visualizar a mensagem perguntando se pretendo preencher as informações do
     * meu contacto, Quando selecciono "Não", Então deve ser exibido o ecrã inicial mantendo-se a
     * funcionalidade "I'm here" desactivada.
     */
    @MediumTest
    public void test05_Us4NoCreateProfile() {
        if(clearUserProfile(DEFAULT_WAIT_TIME)){
            solo.assertCurrentActivity(WRONG_ACTIVITY_ERROR, MainActivity.class);
            solo.clickOnView(mActivity.findViewById(R.id.ib_user_status));
            solo.waitForText(mActivity.getString(R.string.error_must_create_profile_first));
            solo.clickOnText("No");
            solo.assertCurrentActivity(WRONG_ACTIVITY_ERROR, MainActivity.class);
        }
    }


    /**
     * Dado que estou no ecrã de preenchimento de detalhes do meu contacto, Quando termino o
     * preenchimento dos dados, pressiono o botão de back, e tenho as informações do meu contacto
     * preenchidas (pelo menos nome e email), Então deve-me ser exibida uma mensagem de confirmação
     * da activação desta funcionalidade.
     */
    @MediumTest
    public void test06_Us5CancelCreateProfile() {
        if(clearUserProfile(DEFAULT_WAIT_TIME)){

            solo.assertCurrentActivity(WRONG_ACTIVITY_ERROR, MainActivity.class);
            solo.clickOnView(mActivity.findViewById(R.id.ib_user_status));
            solo.waitForText(mActivity.getString(R.string.error_must_create_profile_first));
            solo.clickOnText("Yes");
            solo.waitForActivity(EditUserDetailsActivity.class);
            solo.assertCurrentActivity(WRONG_ACTIVITY_ERROR, EditUserDetailsActivity.class);

            solo.clickOnText("Cancel");
            assertTrue(solo.waitForText(mActivity.getString(R.string.error_confirm_cancel)));
        }
    }

    /**
     * Dado que estou no ecrã de preenchimento de detalhes do meu contacto, Quando termino o
     * preenchimento dos dados, pressiono o botão de back, e não tenho as informações do meu
     * contacto preenchidas (pelo menos nome e email), Então deve ser exibido o ecrã inicial
     * mantendo-se a funcionalidade "I'm here" desactivada.
     */
    @MediumTest
    public void test07_Us4PressBackOnProfileCreate() {
        solo.assertCurrentActivity(WRONG_ACTIVITY_ERROR, MainActivity.class);
        solo.clickOnMenuItem(mActivity.getString(R.string.action_check_in));
        solo.waitForActivity(EditUserDetailsActivity.class);

        String newName = "Joaquim Ley";
        String newEmail = "me@joaquimley.com";
        String newDescription = "Android Developer\nGraphic Designer";

        init();
        // Name
        solo.clearEditText(mEditTestUserName);
        solo.typeText(mEditTestUserName, newName);
        // Email
        solo.clearEditText(mEditTestUserEmail);
        solo.typeText(mEditTestUserEmail, newEmail);
        // Description
        solo.clearEditText(mEditTestUserDescription);
        solo.typeText(mEditTestUserDescription, newDescription);

        solo.goBack();
        assertTrue(WRONG_ACTIVITY_ERROR, solo.waitForActivity(MainActivity.class.getSimpleName()));
        assertTrue(getActivity().findViewById(R.id.ib_user_status) != null);
    }

    /**
     * Dado que estou na mensagem de confirmação da funcionalidade "I'm here", Quando pressiono no
     * botão "Sim" e não tenho rede, Então deve-me ser apresentada uma mensagem informativa a
     * indicar a falta de rede.
     */
    @SmallTest
    public void test08_Us4CheckInNoConnection() {
        solo.setWiFiData(false);
        if (mUser.isVisible()) {
            solo.clickOnView(mActivity.findViewById(R.id.ib_user_status));
        }
        solo.clickOnView(mActivity.findViewById(R.id.ib_user_status));
        solo.waitForText(mActivity.getString(R.string.error_no_internet));
    }

    /**
     * Dado que estou no ecrã de preenchimento de detalhes do meu contacto, Quando termino o
     * preenchimento dos dados, pressiono o botão de back, e tenho as informações do meu contacto
     * preenchidas (pelo menos nome e email), Então deve-me ser exibida uma mensagem de confirmação
     * da activação desta funcionalidade.
     */
    @MediumTest
    public void test09_Us6ProfileUpdateConfirmation() {
        solo.assertCurrentActivity(WRONG_ACTIVITY_ERROR, MainActivity.class);
        reCreateUserProfile(true, false);
        solo.waitForText(mActivity.getString(R.string.text_profile_updated));
        assertTrue(WRONG_ACTIVITY_ERROR, solo.waitForActivity(MainActivity.class));
    }

    /**
     * Dado que estou no ecrã da lista de participantes, quando faço "pull to refresh" e tenho
     * rede, a lista de participantes deve ser actualizada.
     */
    @SmallTest
    public void test10_Us6PullToRefreshParticipantsUi() {
        if (!mUser.isVisible()) {
            solo.clickOnView(mActivity.findViewById(R.id.ib_user_status));
            solo.waitForText(mActivity.getString(R.string.text_status_updated));
        }

        mActivity.getNavigationDrawerFragment().openDrawer();
        solo.clickOnText(mActivity.getString(R.string.title_activity_participants_list));
        assertTrue(solo.waitForActivity(ParticipantsListActivity.class));
        solo.clickLongInList(1, 4, 111);
    }

    /**
     * Dado que estou no ecrã da lista de participantes, Quando não tenho rede, ao fazer
     * "pulltorefresh", deverá aparecer uma mensagem a dizer que não está conectado à rede.
     * A lista de participantes deve permanecer sem alterações.
     */
    @MediumTest
    public void test11_Us4UserDetailsEdited() {
        if (!mUser.isVisible()) {
            solo.clickOnView(mActivity.findViewById(R.id.ib_user_status));
            solo.waitForActivity(mActivity.getString(R.string.text_status_updated));
        }

        mActivity.getNavigationDrawerFragment().openDrawer();
        solo.clickOnText(mActivity.getString(R.string.title_activity_participants_list));
        assertTrue(solo.waitForActivity(ParticipantsListActivity.class));
        solo.setWiFiData(false);
        solo.clickLongInList(1, 4, 111);
        assertTrue(solo.waitForText(mActivity.getString(R.string.error_no_internet)));
    }

    /**
     * Dado que estou no ecrã da lista de participantes, Quando escolho um participante para
     * contactar, Então deve ser-me exibida uma mensagem de confirmação de contacto a esse
     * participante.
     */
    @SmallTest
    public void test12_Us6ConfirmContactUser() {
        if (!mUser.isVisible()) {
            solo.clickOnView(mActivity.findViewById(R.id.ib_user_status));
            solo.waitForActivity(mActivity.getString(R.string.text_status_updated));
        }

        mActivity.getNavigationDrawerFragment().openDrawer();
        solo.clickOnText(mActivity.getString(R.string.title_activity_participants_list));
        assertTrue(solo.waitForActivity(ParticipantsListActivity.class));
        solo.clickInList(2);
        assertTrue(solo.waitForText(mActivity.getString(R.string.text_confirm_contact_user)));
    }

    /**
     * Dado que estou na mensagem de confirmação de contacto a um participante, Quando pressiono o
     * botão "Sim" e não tenho rede, Então deve-me ser apresentada uma mensagem informativa a
     * indicar a falta de rede.
     */
    @SmallTest
    public void test13_Us6NoWifiContactUser() {
        if (!mUser.isVisible()) {
            solo.clickOnView(mActivity.findViewById(R.id.ib_user_status));
            solo.waitForActivity(mActivity.getString(R.string.text_status_updated));
        }

        mActivity.getNavigationDrawerFragment().openDrawer();
        solo.clickOnText(mActivity.getString(R.string.title_activity_participants_list));

        assertTrue(solo.waitForActivity(ParticipantsListActivity.class));
        solo.setWiFiData(false);
        solo.clickInList(2);
        assertTrue(solo.waitForText(mActivity.getString(R.string.error_no_internet)));
    }

    /**
     * Dado que estou na mensagem de confirmação de contacto a um participante, Quando pressiono o
     * botão "Não", Então deve ser exibido o ecrã da lista de participantes.
     */
    @SmallTest
    public void test14_Us6NoConfirmContactUser() {
        if (!mUser.isVisible()) {
            solo.clickOnMenuItem(mActivity.getString(R.string.action_check_in));
            solo.waitForActivity(mActivity.getString(R.string.text_status_updated));
        }

        mActivity.getNavigationDrawerFragment().openDrawer();
        solo.clickOnText(mActivity.getString(R.string.title_activity_participants_list));
        assertTrue(solo.waitForActivity(ParticipantsListActivity.class));
        solo.setWiFiData(false);
        solo.clickInList(2);
        assertTrue(solo.waitForText(mActivity.getString(R.string.text_confirm_contact_user)));
        solo.clickOnText("No");
        solo.waitForDialogToClose();
        solo.assertCurrentActivity(WRONG_ACTIVITY_ERROR, ParticipantsListActivity.class);
    }
}