package com.joaquimley.byinvitationonly.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.joaquimley.byinvitationonly.R;
import com.joaquimley.byinvitationonly.helper.FirebaseHelper;


public class MainActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static Firebase mContactsChildRef;
    private static Firebase mConferencesChildRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    /**
     * Initialize Firebase references UI elements, listeners
     */
    private void init(){
        // Firebase
        Firebase firebaseRef = FirebaseHelper.initiateFirebase(this);
        mContactsChildRef = FirebaseHelper.getChildRef(firebaseRef, "contacts");
        mConferencesChildRef = FirebaseHelper.getChildRef(firebaseRef, "talks");
        // UI
        findViewById(R.id.btn_register).setOnClickListener(this);
        findViewById(R.id.btn_current_talks).setOnClickListener(this);
        findViewById(R.id.btn_favorite_talks).setOnClickListener(this);
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

            case R.id.btn_register:
                // TODO: intent to register/settings activity
                break;

            case R.id.btn_current_talks:
                // TODO: repopulate list view with Current Talks
                break;

            case R.id.btn_favorite_talks:
                // TODO: repopulate list view with Favourite Talks
                break;

            default:
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO: Create conference details (getItemAtPoistion(position))
    }

}
