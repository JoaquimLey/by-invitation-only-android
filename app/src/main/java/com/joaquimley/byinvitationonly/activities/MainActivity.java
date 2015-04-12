package com.joaquimley.byinvitationonly.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.joaquimley.byinvitationonly.R;
import com.joaquimley.byinvitationonly.helper.FirebaseHelper;
import com.joaquimley.byinvitationonly.model.Contact;


public class MainActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Firebase mContactsChildRef;
    private Firebase mTalksChildRef;
    private Contact mMyContact;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    /**
     * Initialize Firebase references UI elements, listeners
     */
    private void init(){
//        mMyContact =
        findViewById(R.id.swipeRefreshLayout).isInEditMode();
        // Firebase
        Firebase firebaseRef = FirebaseHelper.initiateFirebase(this);
        mContactsChildRef = FirebaseHelper.getChildRef(firebaseRef, "contacts");
        mTalksChildRef = FirebaseHelper.getChildRef(firebaseRef, "talks");
        // UI
        findViewById(R.id.btn_current_talks).setOnClickListener(this);
        findViewById(R.id.btn_favourite_talks).setOnClickListener(this);
        ((ListView) findViewById(R.id.list)).setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.btn_current_talks:
                // TODO: repopulate list view with Current Talks
                break;

            case R.id.btn_favourite_talks:
                // TODO: repopulate list view with Favourite Talks
                break;

            case R.id.action_checkin:
                FirebaseHelper.changeAvailabilityState(this, mMyContact, mContactsChildRef);
                break;
            default:
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO: Create conference details (getItemAtPosition(position))
    }

    public Firebase getContactsChildRef() {
        return mContactsChildRef;
    }

    public void setContactsChildRef(Firebase contactsChildRef) {
        mContactsChildRef = contactsChildRef;
    }

    public Firebase getTalksChildRef() {
        return mTalksChildRef;
    }

    public void setTalksChildRef(Firebase talksChildRef) {
        mTalksChildRef = talksChildRef;
    }
}
