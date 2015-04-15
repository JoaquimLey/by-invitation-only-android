package com.joaquimley.byinvitationonly.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.baoyz.widget.PullRefreshLayout;
import com.firebase.client.Firebase;
import com.joaquimley.byinvitationonly.R;
import com.joaquimley.byinvitationonly.adapter.CustomListAdapter;
import com.joaquimley.byinvitationonly.helper.FirebaseHelper;
import com.joaquimley.byinvitationonly.model.Contact;
import com.joaquimley.byinvitationonly.model.Talk;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends Activity implements View.OnClickListener, PullRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    private Firebase mContactsChildRef;
    private Firebase mTalksChildRef;
    private Contact mMyContact;
    private ListView mList;
    private SharedPreferences mSharedPreferences;
    private PullRefreshLayout mPullRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
//
        ArrayList<Talk> dummyItems = new ArrayList<>();

        dummyItems.add(new Talk("Title example numero uno", "Bill Gates", "boa cena lorem ipsoidum feaefoajefapfjaeofa", "https://lh4.ggpht.com/C_B6YXyEaPxC1KYRARAU7xqrMDFf38DC4AKpazbrP4hgfNft1afvdET2Bffk8ZVayXrG=w170", new Date(System.currentTimeMillis())));
        dummyItems.add(new Talk("Title example numero dois", "Ze Gates", "boa cena lorem ipsoidum feaefoajefapfjaeofa", "https://lh4.ggpht.com/C_B6YXyEaPxC1KYRARAU7xqrMDFf38DC4AKpazbrP4hgfNft1afvdET2Bffk8ZVayXrG=w170", new Date(System.currentTimeMillis())));
        dummyItems.add(new Talk("Title example numero tres", "John Gates", "boa cena lorem ipsoidum feaefoajefapfjaeofa", "https://lh4.ggpht.com/C_B6YXyEaPxC1KYRARAU7xqrMDFf38DC4AKpazbrP4hgfNft1afvdET2Bffk8ZVayXrG=w170", new Date(System.currentTimeMillis())));
        dummyItems.add(new Talk("Title example numero 4", "Marry Gates", "boa cena lorem ipsoidum feaefoajefapfjaeofa", "https://lh4.ggpht.com/C_B6YXyEaPxC1KYRARAU7xqrMDFf38DC4AKpazbrP4hgfNft1afvdET2Bffk8ZVayXrG=w170", new Date(System.currentTimeMillis())));

        CustomListAdapter dummyAdapter = new CustomListAdapter(MainActivity.this, dummyItems);
        mList.setAdapter(dummyAdapter);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    /**
     * Initialize Firebase references, UI elements, listeners
     */
    private void init(){
        // Firebase
        Firebase firebaseRef = FirebaseHelper.initiateFirebase(this);
        mContactsChildRef = FirebaseHelper.getChildRef(firebaseRef, "contacts");
        mTalksChildRef = FirebaseHelper.getChildRef(firebaseRef, "talks");
        // UI
        Picasso.with(MainActivity.this).load(R.drawable.ic_intro).into((ImageView) findViewById(R.id.iv_intro));
        findViewById(R.id.btn_current_talks).setOnClickListener(this);
        findViewById(R.id.btn_favourite_talks).setOnClickListener(this);
        // List
        mPullRefreshLayout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mPullRefreshLayout.setOnRefreshListener(this);
        mList = (ListView) findViewById(R.id.list);
        mList.setOnItemClickListener(this);
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

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        // TODO: Create Talk activity with item details (getItemAtPosition(position))
//    }

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

    @Override
    public void onRefresh() {
        // TODO: See what type of list is and update according
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
