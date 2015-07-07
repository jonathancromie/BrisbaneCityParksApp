package com.jonathancromie.brisbanecityparks;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.support.widget.v7.Toolbar;

import java.util.ArrayList;
import java.util.Locale;



public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private EditText search;
    private Button mSubmit;

    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    private Toolbar toolbar;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    DrawerLayout drawer;

    ActionBarDrawerToggle mDrawerToggle;

    //php login script location:

    //localhost :
    //testing on your device
    //put your local ip instead,  on windows, run CMD > ipconfig
    //or in mac's terminal type ifconfig and look for the ip under en0 or en1
    // private static final String LOGIN_URL = "http://xxx.xxx.x.x:1234/webservice/login.php";

    //testing on Emulator:
    private static final String LOGIN_URL = "http://10.0.2.2:80/webservice/login.php";

    //testing from a real server:
    //private static final String LOGIN_URL = "http://www.yourdomain.com/webservice/login.php";

    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = (EditText) findViewById(R.id.search);
        mSubmit = (Button) findViewById(R.id.submit);

        mSubmit.setOnClickListener(this);

        mNavItems.add(new NavItem("Home", "Homepage", R.drawable.ic_home_black_24dp));
        mNavItems.add(new NavItem("Top Rated", "Find awesome parks", R.drawable.ic_grade_black_24dp));
        mNavItems.add(new NavItem("Trending", "Which parks are popular right now", R.drawable.ic_trending_up_black_24dp));
        mNavItems.add(new NavItem("Settings", "Customise your settings", R.drawable.ic_settings_black_24dp));
        mNavItems.add(new NavItem("Help & Feedback", "Get help or submit feedback", R.drawable.ic_help_black_24dp));

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String user = sp.getString("email", "emailAddress");
        String desc = "Visit Profile";
        int profile = 0;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.left_drawer);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new DrawerListAdapter(mNavItems, user, desc, profile, this);
        mRecyclerView.setAdapter(mAdapter);

        final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());


                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    drawer.closeDrawers();
                    Toast.makeText(MainActivity.this, "The Item Clicked is: " + recyclerView.getChildPosition(child), Toast.LENGTH_SHORT).show();

                    return true;

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }
        });

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.openDrawer,R.string.closeDrawer){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }



        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State

















    @Override
    public void onClick(View v) {
        saveSearch();
    }

    private void saveSearch() {
        // save user data
        String query = search.getText().toString();
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("search", query);
        edit.commit();

        Intent i = new Intent(MainActivity.this, ResultsActivity.class);
        startActivity(i);
    }
}
