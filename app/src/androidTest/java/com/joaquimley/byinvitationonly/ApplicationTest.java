package com.leya.educacao.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.View;
import android.widget.EditText;
import com.leya.educacao.LeyaApp;
import com.leya.educacao.Master;
import com.leya.educacao.R;
import com.leya.educacao.db.models.User;
import com.leya.educacao.db.models.UserModule;
import com.leya.educacao.fragments.*;
import com.leya.educacao.util.Log;
import com.robotium.solo.Solo;

import java.util.ArrayList;

public class ActivityFragmentTest extends ActivityInstrumentationTestCase2<ActivityFragment> {
    protected static final String NO_MSG_ERROR = "No error message displayed";
    protected static final String WRONG_FRAG_ERROR = "ERROR: Not expected fragment showing";

    private ActivityFragment mActivityFragment;
    protected Solo solo;
    // FirstScreenFragment
    protected View mBtnDemo;
    protected View mBtnLogin;
    // RecoverPasswordFragment
    protected View mBtnRecover;

    protected EditText etEmail;
    protected EditText etName;
    protected EditText etSurname;
    protected EditText etPassword;
    protected EditText etConfirmPassword;

    public ActivityFragmentTest() {
        super(ActivityFragment.class);
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
        mActivityFragment = getActivity();
        mBtnDemo = mActivityFragment.findViewById(R.id.btnDemo);
        mBtnLogin = mActivityFragment.findViewById(R.id.btnLogin);
        mBtnRecover = mActivityFragment.findViewById(R.id.btnRecover);
    }

    /**
     * On finish
     * @throws Exception
     */
    @Override
    protected void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

//    /**
//     * Tests if a view is showing
//     * @param origin    the original (source)
//     * @param view      the one to be tested
//     */
//    public void assertViewIsShowing(View origin, View view){
//        ViewAsserts.assertOnScreen(origin, view);
//    }


    /**
     * Tests if the fragment with the TAG is showing
     * @param fragmentTag tag of the fragment to verify
     */
    public void assertCorrectFragment(String fragmentTag){
        assertTrue(WRONG_FRAG_ERROR, solo.waitForFragmentByTag(fragmentTag));
    }

    /**
     * Initialisation of UI components
     */
    public void init(){
        etEmail = (EditText) mActivityFragment.findViewById(R.id.etEmail);
        etName = (EditText) mActivityFragment.findViewById(R.id.etName);
        etSurname = (EditText) mActivityFragment.findViewById(R.id.etSurname);
        etPassword = (EditText) mActivityFragment.findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) mActivityFragment.findViewById(R.id.etConfirmPassword);
    }

    /**
     * Goes to FirstScreenFragment
     */
    public void goToFirstScreenFragment(){
        if(!solo.waitForFragmentByTag(FirstScreenFragment.class.getName(), 1) || !solo.searchText("20 Manual Digial")){
            Log.i("ACTIVITY", "Not the FirstScreenFragment showing");
            solo.goBack();
            solo.waitForDialogToOpen();
            solo.clickOnView(solo.getView(android.R.id.button1));
            solo.waitForDialogToClose();
        }
        assertCorrectFragment(FirstScreenFragment.class.getName());
    }

    /**
     * Goes to Demo Activity (Master.class)
     */
    public void goToDemoActivity(){
        init();
        goToFirstScreenFragment();
        solo.clickOnButton(solo.getString(R.string.firstscreen_btn_demo));
        solo.waitForDialogToClose();
        solo.assertCurrentActivity("Not de correct Activity", Master.class);
    }

    /**
     * Goes to LoginFragment
     */
    public void goToLoginFragment(){
        init();
        if(!solo.waitForFragmentByTag(LoginFragment.class.getName(),1)) {
            goToFirstScreenFragment();
            solo.clickOnButton(solo.getString(R.string.login_btn_login));
        }
        assertCorrectFragment(LoginFragment.class.getName());
    }

    /**
     * Goes to RegisterAccountFragment
     */
    public void goToRegisterNewAccountFragment(){
        init();
        if(!solo.waitForFragmentByTag(RegisterAccountFragment.class.getName(),1)) {
            goToLoginFragment();
            solo.clickOnText(solo.getString(R.string.login_tv_new_user));
            assertCorrectFragment(RegisterAccountFragment.class.getName());
        }
    }

    /**
     * Goes to RecoverPasswordFragment
     */
    public void gotoRecoverPasswordFragment(){
        init();
        if(!solo.waitForFragmentByTag(RecoverPasswordFragment.class.getName(),1)) {
            goToLoginFragment();
            solo.clickOnText(solo.getString(R.string.login_tv_forgot_password));
            assertCorrectFragment(RecoverPasswordFragment.class.getName());
        }
    }

    /**
     * WARNING: Ensure you are in the login fragment when calling this
     * Clears all login fields (username & password)
     */
    public void clearLoginFields(){
        solo.enterText(etEmail, "");
        solo.enterText(etPassword, "");
    }

    /**
     * WARNING: Ensure you are in the login fragment when calling this
     * Login into app using parameters
     * 
     * @param userEmail     the user access email
     * @param userPassword  the user access password
     */
    public void loginIntoApp(String userEmail, String userPassword){
        init();
        clearLoginFields();
        solo.enterText((EditText) mActivityFragment.findViewById(R.id.etEmail), userEmail);
        solo.enterText(etPassword, userPassword);
        solo.clickOnButton(solo.getString(R.string.login_btn_login));
    }

    /**
     * WARNING: Ensure you are in the RegisterNewAccount fragment when calling this
     * Clears all fields on the register account fragment
     */
    public void clearRegisterAccountFields(){
        solo.enterText(etEmail, "");
        solo.enterText(etName, "");
        solo.enterText(etSurname, "");
        solo.enterText(etPassword, "");
        solo.enterText(etConfirmPassword, "");
    }

    /**
     * WARNING: Ensure you are in the login fragment when calling this
     * Verifies failed login, and correct error message displayed
     * @param errorMessage  the error message to be displayed
     */
    public void failedLoginVerifyErrorMessage(int errorMessage) {
        solo.clickOnButton(solo.getString(R.string.login_btn_login));
        assertTrue(NO_MSG_ERROR, solo.searchText(solo.getString(errorMessage)));
        assertTrue(solo.waitForFragmentByTag(LoginFragment.class.getName()));
    }

    /**
     * WARNING: Ensure you are in the RegisterNewAccount fragment when calling this
     * Register new account using the params
     * @param email user Email
     * @param nome user Name
     * @param surname user Surname
     * @param password the user password
     * @param passwordConfirm confirmation password (2nd Password)
     */
    public void registerNewAccount(String email, String nome, String surname, String password, String passwordConfirm){
        init();
        clearRegisterAccountFields();
        solo.enterText(etEmail, email);
        solo.enterText(etName, nome);
        solo.enterText(etSurname, surname);
        solo.enterText(etPassword, password);
        solo.enterText(etConfirmPassword, passwordConfirm);
    }

    /**
     * WARNING: Ensure you are in the "RecoverPassword fragment when calling this
     * Recovers the user password via email
     * @param email user email
     */
    public void recoverPassword(String email){
        init();
        solo.enterText(etEmail, "");
        solo.enterText(etEmail, email);
        solo.clickOnButton(solo.getString(R.string.recoverpass_btn_recover));
    }

    /**
     * WARNING: Ensure you are in the RecoverPassword fragment when calling this
     * Verifies failed password recovery, and correct error message displayed
     * @param errorMessage  the error message to be displayed
     */
    public void failedPasswordRecoveryVerifyErrorMessage(int errorMessage) {
        solo.clickOnButton(solo.getString(R.string.recoverpass_btn_recover));
        assertTrue(NO_MSG_ERROR, solo.searchText(solo.getString(errorMessage)));
        assertTrue(solo.waitForFragmentByTag(RecoverPasswordFragment.class.getName()));
    }

    /**
     * WARNING: Ensure you are in the RegisterNewAccount fragment when calling this
     * Verifies failed account creation, and correct error message displayed.
     * 
     * @param errorMessage  the error message to be displayed
     */
    public void failedCreateAccountVerifyErrorMessage (int errorMessage){
        solo.clickOnButton(solo.getString(R.string.register_btn));
        assertTrue(NO_MSG_ERROR, solo.searchText(solo.getString(errorMessage)));
        assertTrue(solo.waitForFragmentByTag(RegisterAccountFragment.class.getName()));
    }

    // --------------------------------------- Tests  --------------------------------------- //

    /**
     * Testes if ActivityFragment and the FirstScreenFragment aren't null
     */
    @SmallTest
    public void test01_PreConditions() {
        assertNotNull(mActivityFragment);
        assertNotNull(mActivityFragment.getSupportFragmentManager().findFragmentByTag(FirstScreenFragment.class.getName()));
    }

    /**
     * Tests if "btnDemo" and "btnLogin" are showing
     */
    @SmallTest
    public void test02_VerifyUIButtons() {
        goToFirstScreenFragment();
        // Button Demo
//            assertViewIsShowing(mActivityFragment.findViewById(R.id.btnDemo), mBtnDemo);
        assertTrue(solo.searchText(solo.getString(R.string.firstscreen_btn_demo)));
        // Button Login
//            assertViewIsShowing(mActivityFragment.findViewById(R.id.btnLogin), mBtnLogin);
        assertTrue(solo.searchText(solo.getString(R.string.firstscreen_btn_login)));
    }

    /**
     * Tests if "btnDemo" changes to the correct activity
     */
    @SmallTest
    public void test03_ButtonDemoCorrectFragmentChange() {
        goToDemoActivity();
    }

    /**
     * Tests if "btnLogin" changes to the correct fragment
     */
    @SmallTest
    public void test04_ButtonLoginCorrectFragmentChange() {
        init();
        goToFirstScreenFragment();
        solo.clickOnButton(solo.getString(R.string.login_btn_login));
    }

    /**
     * Tests if "login_tv_new_user" goes to the correct Fragment
     */
    @SmallTest
    public void test05_TextCreateAccountCorrectFragmentChange(){
        goToRegisterNewAccountFragment();
    }

    /**
     * Tests if "login_tv_recover_password" goes to the correct Fragment
     */
    @SmallTest
    public void test06_TextRecoverPasswordCorrectFragmentChange(){
        gotoRecoverPasswordFragment();
    }

    /**
     * Fails login with bad username/password combo, bad email format, no email, no password
     */
    @MediumTest
    public void test07_failedLogin_BadParams(){
        goToLoginFragment();
        // Bad Username/Password Combo
        loginIntoApp("johndoe@gmail.com", "bad_password");
        failedLoginVerifyErrorMessage(R.string.register_field_error_password);
        // Bad Email format
        loginIntoApp("johndoe", "bad_password");
        failedLoginVerifyErrorMessage(R.string.register_field_error_email);

        loginIntoApp("", "bad_password");
        failedLoginVerifyErrorMessage(R.string.register_field_error_email);
        // No password input
        loginIntoApp("johndoe@gmail.com", "");
        failedLoginVerifyErrorMessage(R.string.register_field_error_password);
    }

    /**
     * Fails in account creation, leaving fields (each at a time) blank, bad email format, bad name(s) format,
     * bad password format, bad password + 2nd password combo
     */
    @LargeTest
    public void test08_failCreateAccount() {
        goToRegisterNewAccountFragment();
        // No "email"
        registerNewAccount("", "John", "Doe", "password", "password");
        failedCreateAccountVerifyErrorMessage(R.string.register_field_error_email);
        // No "name"
        registerNewAccount("johndoe@gmail.com", "", "Doe", "password", "password");
        failedCreateAccountVerifyErrorMessage(R.string.register_field_error_name);
        // No "surname"
        registerNewAccount("johndoe@gmail.com", "John", "", "password", "password");
        failedCreateAccountVerifyErrorMessage(R.string.register_field_error_surname);
        // No "password"
        registerNewAccount("johndoe@gmail.com", "John", "Doe", "", "password");
        failedCreateAccountVerifyErrorMessage(R.string.register_field_error_2nd_password);
        // No "2nd password"
        registerNewAccount("johndoe@gmail.com", "John", "Doe", "password", "");
        failedCreateAccountVerifyErrorMessage(R.string.register_field_error_2nd_password);
        // Bad Email format
        registerNewAccount("johndoe", "John", "Doe", "password", "password");
        failedCreateAccountVerifyErrorMessage(R.string.register_field_error_email);
        // Bad Password / 2nd Password combo
        registerNewAccount("johndoe@gmail.com", "John", "Doe", "password", "bad_password");
        solo.waitForDialogToClose();
        failedCreateAccountVerifyErrorMessage(R.string.register_field_error_2nd_password);
        // Create an account with an already registered email
        registerNewAccount("leapintegration@leya.com", "John", "Doe", "password", "password");
        solo.clickOnButton(solo.getString(R.string.register_btn));
        assertTrue(NO_MSG_ERROR, solo.searchText(solo.getString(R.string.register_error_request)));
        solo.clickOnView(solo.getView(android.R.id.button1));
        solo.waitForDialogToClose();
        assertCorrectFragment(RegisterAccountFragment.class.getName());
    }

    /**
     * This test makes failed attempts to recover a account password and verifies the error messages
     */
    @MediumTest
    public void test09_failRecoverAccount(){
        gotoRecoverPasswordFragment();
        // No "email"
        solo.enterText(etEmail, "");
        failedPasswordRecoveryVerifyErrorMessage(R.string.register_field_error_email);
        // Bad format "email"
        recoverPassword("johndoe");
        failedPasswordRecoveryVerifyErrorMessage(R.string.register_field_error_email);

        // Not registered email
        recoverPassword("ensure_you_dont_register_this@gmail.com");
        solo.waitForDialogToOpen();
        assertTrue("No error message displayed", solo.searchText(solo.getString(R.string.recoverpass_error_email)));
        solo.clickOnText("OK");
        assertTrue("Dialog not closed", solo.waitForDialogToClose());
    }

    /**
     * Requests a new password recovery email (this can't assert if the email is received, and if the password is changed
     * since it's not inside the app).
     */
    @SmallTest
    public void test10_requestPasswordRecovery(){
        init();
        gotoRecoverPasswordFragment();
        // Request a password
        recoverPassword("leapintegration@leya.com");
        assertTrue(solo.waitForDialogToOpen());
        solo.searchText(solo.getString(R.string.recoverpass_success));
        solo.clickOnView(solo.getView(android.R.id.button2));
        assertTrue(solo.waitForDialogToClose());
    }

    /**
     * Trys to login with correct login params
     */
    @SmallTest
    public void test11_userLogin(){
        init();
        goToLoginFragment();
        loginIntoApp("leapintegration@leya.com", "integration");
        assertTrue(solo.waitForDialogToOpen());
        assertTrue(solo.searchText(solo.getString(R.string.a_autenticar)));
        assertTrue(solo.waitForDialogToClose());
        assertTrue(solo.waitForActivity(Master.class.getName()));
    }

    /**
     * Checks if the user's items (books) are in accordance with the server (local DB vs Server Database)
     * Gray-Box Testing
     */
    @SmallTest
    public void test12_correctUserLibraryShown(){
        init();
        goToLoginFragment();
        if(mActivityFragment != getActivity()){
            loginIntoApp("leapintegration@leya.com", "integration");
        }
        ArrayList<User> leapIntegrationTesting = (ArrayList<User>) LeyaApp.getInstance().getDb().getUserDao().queryForEq("username", "leapintegration@leya.com");
        assertNotNull(leapIntegrationTesting);

        for(User u: leapIntegrationTesting){
            for(UserModule um: u.getUserModules()){
                assertTrue("Error: No user module found", solo.searchText(um.getModule().getTitle()));
            }
        }

    }

}