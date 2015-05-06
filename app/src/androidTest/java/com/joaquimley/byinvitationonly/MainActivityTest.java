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
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.firebase.client.Firebase;
import com.joaquimley.byinvitationonly.activities.EditUserDetailsActivity;
import com.joaquimley.byinvitationonly.activities.MainActivity;
import com.joaquimley.byinvitationonly.helper.FirebaseHelper;
import com.joaquimley.byinvitationonly.model.User;
import com.robotium.solo.Solo;


public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    protected static final String TAG = MainActivityTest.class.getSimpleName();
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
        mUser = mActivity.getUser();
        init();
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
     * Tests if the fragment with the TAG is showing
     *
     * @param activityName self explanatory
     */
    public void correctActivityChange(String activityName, int index) {
        solo.clickOnButton(index);
        assertTrue(WRONG_ACTIVITY_ERROR, solo.waitForActivity(activityName));
    }

    /**
     * Initialisation of UI components
     */
    public void init() {
        Firebase firebaseRef = FirebaseHelper.initiateFirebase(mActivity);
        mSessionsRef = FirebaseHelper.getChildRef(firebaseRef, mActivity.getString(R.string.firebase_child_sessions));
        mUsersRef = FirebaseHelper.getChildRef(firebaseRef, mActivity.getString(R.string.firebase_child_users));

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        mEditTestUserName = (EditText) mActivity.findViewById(R.id.et_edit_user_details_name);
        mEditTestUserEmail = (EditText) mActivity.findViewById(R.id.et_edit_user_details_email);
        mEditTestUserDescription = (EditText) mActivity.findViewById(R.id.et_edit_user_details_description);
        preConditions();
    }

    /**
     * Testes if Activity isn't null
     */
    public void preConditions() {
        assertNotNull(mActivity);
        assertTrue(WRONG_ACTIVITY_ERROR, solo.waitForActivity(MainActivity.class.getSimpleName()));
    }

    /**
     * Clears all sharedPreferences information on device, sets mUser NULL
     */
    private void clearUserProfile() {
        mSharedPreferences.edit().clear().apply();
        mUser = null;
    }

    /**
     * Recreates user profile (sim user behavior)
     */
    public void reCreateUserProfile(boolean forceUpdate) {
        if(!forceUpdate){
            if (mUser != null || !mUser.getName().isEmpty()) {
                return;
            }
        }

        solo.assertCurrentActivity(WRONG_ACTIVITY_ERROR, MainActivity.class);
        solo.clickOnImageButton(0);
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

        solo.clickOnText(solo.getString(R.string.text_save));
        assertTrue(WRONG_ACTIVITY_ERROR, solo.waitForActivity(MainActivity.class.getSimpleName()));

        mUser = ((MainActivity) solo.getCurrentActivity()).getUser();

        assertTrue(mUser.getName().equals(newName));
        assertTrue(mUser.getEmail().equals(newEmail));
        assertTrue(mUser.getDescription().equals(newDescription));
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
    public void test01_Us4NoUserProfileCheckIn() {
        clearUserProfile();
        solo.clickOnImageButton(0);
        assertTrue("ERROR: User was able to check-in profile created",
                solo.searchText(mActivity.getString(R.string.error_no_user_details)));
    }

    /**
     * Dado que estou na mensagem de confirmação da funcionalidade "I'm here", Quando pressiono no
     * botão "Sim" e tenho rede, Então os meus dados devem ser submetidos e partilhados com os
     * restantes participantes que activaram a funcionalidade "I'm here" e o botão deve mudar para
     * o estado activo.
     */
    @SmallTest
    public void test02_Us4ShareDetails() {
        init();
        reCreateUserProfile(false);
        Drawable previousIcon = mActivity.findViewById(R.id.ib_user_status).getBackground();
        boolean prevStatus = mUser.isVisible();

        solo.clickOnImageButton(1);
        solo.waitForActivity(mUser.getName() + " has checked in");
        assertTrue("User didn't change checked-in status", prevStatus != mUser.isVisible());
        assertTrue("User profile info not present on server", BioApp.getInstance().getUsersList().contains(mUser));
        assertTrue(mActivity.findViewById(R.id.ib_user_status).getBackground() != previousIcon);
    }

    /**
     * Dado que estou a visualizar a mensagem perguntando se pretendo preencher as informações do
     * meu contacto, Quando selecciono "Sim", Então devo ser reencaminhado para o ecrã de
     * preenchimento de detalhes do meu contacto.
     */
    @MediumTest
    public void test03_Us4CreateOrEditUserProfile() {
        solo.assertCurrentActivity(WRONG_ACTIVITY_ERROR, MainActivity.class);
        solo.clickOnImageButton(0);
        solo.waitForActivity(EditUserDetailsActivity.class);
        solo.assertCurrentActivity(WRONG_ACTIVITY_ERROR, EditUserDetailsActivity.class);
    }

    /**
     * Dado que estou no ecrã de preenchimento de detalhes do meu contacto, Quando termino o
     * preenchimento dos dados, pressiono o botão de back, e tenho as informações do meu contacto
     * preenchidas (pelo menos nome e email), Então deve-me ser exibida uma mensagem de confirmação
     * da activação desta funcionalidade.
     */
    @MediumTest
    public void test04_Us4ProfileUpdateConfirmation() {
        solo.assertCurrentActivity(WRONG_ACTIVITY_ERROR, MainActivity.class);
        reCreateUserProfile(true);
        solo.waitForText(mActivity.getString(R.string.text_profile_updated));
        assertTrue(WRONG_ACTIVITY_ERROR, solo.waitForActivity(MainActivity.class));
    }

    /**
     * Dado que estou na mensagem de confirmação da funcionalidade "I'm here", Quando pressiono no
     * botão "Sim" e não tenho rede, Então deve-me ser apresentada uma mensagem informativa a
     * indicar a falta de rede.
      */
    @SmallTest
    public void test05_Us4CheckInNoConnection(){
        solo.setWiFiData(false);
        if(mUser.isVisible()){
            solo.clickOnImageButton(1);
        }
        solo.clickOnImageButton(1);
        solo.waitForText(mActivity.getString(R.string.error_no_internet));
    }

    /**
     * Tests if the is user is ABLE to see other participants while visible
     */
    @MediumTest
    public void test06_UserDetailsEdited() {
        String newName = "new name";
        String newEmail = "new@email.com";
        String newDescription = "new description";

        solo.clickOnImageButton(0);
        assertTrue(solo.waitForActivity(EditUserDetailsActivity.class));
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

        solo.clickOnText(solo.getString(R.string.text_save));
        assertTrue(WRONG_ACTIVITY_ERROR, solo.waitForActivity(MainActivity.class.getSimpleName()));

        mUser = ((MainActivity) solo.getCurrentActivity()).getUser();

        assertTrue(mUser.getName().equals(newName));
        assertTrue(mUser.getEmail().equals(newEmail));
        assertTrue(mUser.getDescription().equals(newDescription));
    }
}