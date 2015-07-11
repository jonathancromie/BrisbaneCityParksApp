package com.jonathancromie.brisbanecityparks;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ParkActivity extends AppCompatActivity {

    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    private Toolbar toolbar;

    RecyclerView mainRecyclerView;
    ReviewAdapter mainAdapter;
    RecyclerView.LayoutManager mainLayoutManager;

    RecyclerView drawerRecyclerView;
    RecyclerView.Adapter drawerAdapter;
    RecyclerView.LayoutManager drawerLayoutManager;

    DrawerLayout drawer;

    ActionBarDrawerToggle mDrawerToggle;

    // Progress Dialog
    private ProgressDialog pDialog;

    String parkID;

    //php read comments script

    //localhost :
    //testing on your device
    //put your local ip instead,  on windows, run CMD > ipconfig
    //or in mac's terminal type ifconfig and look for the ip under en0 or en1
    // private static final String READ_COMMENTS_URL = "http://xxx.xxx.x.x:1234/webservice/comments.php";

    //testing on Emulator:
    private static final String READ_REVIEWS_URL = "http://10.0.2.2:80/webservice/reviews.php?id=";

    //testing from a real server:
    //private static final String READ_COMMENTS_URL = "http://www.mybringback.com/webservice/comments.php";
//    private static final String READ_REVIEWS_URL = "http://192.168.1.9:80/webservice/reviews.php?id=";

    //JSON IDS:
    private static final String TAG_SUCCESS = "success";

    private static final String TAG_REVIEWS = "reviews";
    private static final String TAG_ID = "park_id";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_REVIEW = "review";
    private static final String TAG_RATING = "rating";
    private static final String TAG_DATE = "date_posted";
    //it's important to note that the message is both in the parent branch of
    //our JSON tree that displays a "Post Available" or a "No Post Available" message,
    //and there is also a message for each individual post, listed under the "posts"
    //category, that displays what the user typed as their message.

    //An array of all of our comments
    private JSONArray mReviews = null;
    //manages all of our comments in a list.
    private ArrayList<ReviewInfo> mReviewList;


    private Button btnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park);

        mNavItems.add(new NavItem("Home", "Homepage", R.drawable.ic_home_grey_24dp));
        mNavItems.add(new NavItem("Top Rated", "Find awesome parks", R.drawable.ic_grade_grey_24dp));
        mNavItems.add(new NavItem("Trending", "Which parks are popular right now", R.drawable.ic_trending_up_grey_24dp));
        mNavItems.add(new NavItem("Settings", "Customise your settings", R.drawable.ic_settings_grey_24dp));
        mNavItems.add(new NavItem("Help & Feedback", "Get help or submit feedback", R.drawable.ic_help_grey_24dp));

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ParkActivity.this);
        String user = sp.getString("email", "emailAddress");
        String desc = "Visit Profile";
        int profile = 0;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        handleIntent(getIntent());

        mainRecyclerView = (RecyclerView) findViewById(R.id.cardList);

        mainRecyclerView.setHasFixedSize(true);

        mainLayoutManager = new LinearLayoutManager(this);

        mainRecyclerView.setLayoutManager(mainLayoutManager);

//        btnMap = (Button) findViewById(R.id.btnMap);
//        btnMap.setOnClickListener(this);

        drawerRecyclerView = (RecyclerView) findViewById(R.id.left_drawer);
        drawerRecyclerView.setHasFixedSize(true);
        drawerAdapter = new DrawerListAdapter(mNavItems, user, desc, profile, this);
        drawerRecyclerView.setAdapter(drawerAdapter);

        final GestureDetector mGestureDetector = new GestureDetector(ParkActivity.this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });

        drawerRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());


                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    drawer.closeDrawers();
                    Toast.makeText(ParkActivity.this, "The Item Clicked is: " + recyclerView.getChildPosition(child), Toast.LENGTH_SHORT).show();

                    return true;

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        drawerLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        drawerRecyclerView.setLayoutManager(drawerLayoutManager);                 // Setting the layout Manager

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);        // Drawer object Assigned to the view

        mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

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
        drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void handleIntent(Intent intent) {
        parkID = intent.getStringExtra("parkID");
        new LoadReviews().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    //    @Override
//    protected void onResume() {
//        super.onResume();
//        //loading the comments via AsyncTask
//        new LoadReviews().execute();
//    }

    /**
     * Retrieves json data of comments
     */
    public void updateJSONdata(String query) {
        // Instantiate the arraylist to contain all the JSON data.
        // we are going to use a bunch of key-value pairs, referring
        // to the json element name, and the content, for example,
        // message it the tag, and "I'm awesome" as the content..

        mReviewList = new ArrayList<ReviewInfo>();

        // Bro, it's time to power up the J parser
        JSONParser jParser = new JSONParser();
        // Feed the beast our comments url, and it spits us
        //back a JSON object.  Boo-yeah Jerome.
        JSONObject json = jParser.getJSONFromUrl(READ_REVIEWS_URL+query);

        //when parsing JSON stuff, we should probably
        //try to catch any exceptions:
        try {

            //I know I said we would check if "Posts were Avail." (success==1)
            //before we tried to read the individual posts, but I lied...
            //mComments will tell us how many "posts" or comments are
            //available
            mReviews = json.getJSONArray(TAG_REVIEWS);

            // looping through all posts according to the json object returned
            for (int i = 0; i < mReviews.length(); i++) {
                JSONObject c = mReviews.getJSONObject(i);

                //gets the content of each tag
                String id = c.getString(TAG_ID);
                String email = c.getString(TAG_EMAIL);
                String review = c.getString(TAG_REVIEW);
                String rating = c.getString(TAG_RATING);
                String date = c.getString(TAG_DATE);

                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                map.put(TAG_ID, id);
                map.put(TAG_EMAIL, email);
                map.put(TAG_REVIEW, review);
                map.put(TAG_RATING, rating);
                map.put(TAG_DATE, date);

                // adding HashList to ArrayList
                mReviewList.add(new ReviewInfo(id, email, review, rating, date));

                //annndddd, our JSON data is up to date same with our array list
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Inserts the parsed data into our listview
     */
    private void updateList() {
        mainAdapter = new ReviewAdapter(mReviewList);
        mainRecyclerView.setAdapter(mainAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_park, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class LoadReviews extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ParkActivity.this);
            pDialog.setMessage("Loading Reviews...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            updateJSONdata(parkID);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            updateList();
        }
    }


//    @Override
//    public void onClick(View v) {
//        Intent i = new Intent(ParkActivity.this, MapsActivity.class);
//        i.putExtra("latitude", latitude);
//        i.putExtra("longitude", longitude);
//        startActivity(i);
//    }
}
