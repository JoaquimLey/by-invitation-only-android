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

package com.joaquimley.byinvitationonly;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.joaquimley.byinvitationonly.activities.EditUserDetailsActivity;
import com.joaquimley.byinvitationonly.activities.MainActivity;
import com.joaquimley.byinvitationonly.activities.ParticipantsList;
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
//    private EditText mEditTestUserName;

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

        setActivityInitialTouchMode(true);
        solo = new Solo(getInstrumentation());
        mActivity = getActivity();
        mUser = mActivity.getUser();

        mEditTestUserName = (EditText) mActivity.findViewById(R.id.et_edit_user_details_name);
        mEditTestUserEmail = (EditText) mActivity.findViewById(R.id.et_edit_user_details_email);
        mEditTestUserDescription = (EditText) mActivity.findViewById(R.id.et_edit_user_details_description);

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
        mBtnStatus = (ImageButton) mActivity.findViewById(R.id.ib_user_edit);
        mBtnEditUser = (ImageButton) mActivity.findViewById(R.id.ib_user_status);
        mParticipantsListActivity = (Button) mActivity.findViewById(R.id.ic_menu_group);
    }

    // --------------------------------------- Tests  --------------------------------------- //

    /**
     * Testes if Activity isn't null
     */
    @SmallTest
    public void test01_PreConditions() {
        assertNotNull(mActivity);
        assertTrue(WRONG_ACTIVITY_ERROR, solo.waitForActivity(MainActivity.class.getSimpleName()));
    }

    /**
     * Change user availability status
     */
    @SmallTest
    public void test02_ChangeUserAvailability() {
        solo.clickOnImageButton(1);
        solo.waitForText("You are VISIBLE");
        assertTrue("User status not changed", mUser.isVisible());
    }

    /**
     * Tests if "btnEdit" changes to the correct activity
     */
    @SmallTest
    public void test03_ButtonEditCorrectActivityChange() {
        solo.clickOnImageButton(0);
        assertTrue(WRONG_ACTIVITY_ERROR, solo.waitForActivity(EditUserDetailsActivity.class));
    }

    /**
     * Tests if the is user is UNABLE to see other participants while invisible
     */
    @MediumTest
    public void test04_CannotViewParticipantsWhileInvisible() {
        if (mUser.isVisible()) {
            solo.clickOnImageButton(1);
        }
        solo.clickOnActionBarItem(R.id.ic_menu_group);
        assertTrue(solo.waitForText("You must be VISIBLE"));
        assertFalse(WRONG_ACTIVITY_ERROR, solo.waitForActivity(ParticipantsList.class.getSimpleName()));
    }

    /**
     * Tests if the is user is ABLE to see other participants while visible
     */
    @MediumTest
    public void test05_CanViewParticipantsWhileVisible() {
        solo.assertCurrentActivity(WRONG_ACTIVITY_ERROR, MainActivity.class);
        if (!mUser.isVisible()) {
            solo.clickOnImageButton(1);
        }
        solo.clickOnActionBarItem(R.id.ic_menu_group);
        assertTrue(WRONG_ACTIVITY_ERROR, solo.waitForActivity(ParticipantsList.class.getSimpleName()));
    }

    /**
     * Tests if the is user is ABLE to see other participants while visible
     */
    @SmallTest
    public void test06_UserDetailsEdited() {
        String newName = "new name";
        String newEmail = "new@email.com";
        String newDescription = "new description";

        solo.clickOnImageButton(0);
        assertTrue(solo.waitForActivity(EditUserDetailsActivity.class));
        // Name
        solo.clearEditText(mEditTestUserName);
        solo.typeText(mEditTestUserName, newName);
        // Email
        solo.clearEditText(mEditTestUserEmail);
        solo.typeText(mEditTestUserEmail, newEmail);
        // Description
        solo.clearEditText(mEditTestUserDescription);
        solo.typeText(mEditTestUserDescription, newDescription);

        solo.clickOnButton(R.id.btn_save);
        solo.waitForActivity(MainActivity.class);
        solo.assertCurrentActivity(WRONG_ACTIVITY_ERROR, MainActivity.class);

        assertTrue(mUser.getName().equals(newName));
        assertTrue(mUser.getEmail().equals(newEmail));
        assertTrue(mUser.getDescription().equals(newDescription));
    }
}