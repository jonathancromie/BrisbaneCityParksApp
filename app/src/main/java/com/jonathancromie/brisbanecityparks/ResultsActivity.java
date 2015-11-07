package com.jonathancromie.brisbanecityparks;

<<<<<<< HEAD
public class ResultsActivity extends _BaseResult {
=======
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
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
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResultsActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    private Toolbar toolbar;

    RecyclerView mainRecyclerView;
    ParkAdapter mainAdapter;
    LayoutManager mainLayoutManager;

    RecyclerView drawerRecyclerView;
    RecyclerView.Adapter drawerAdapter;
    RecyclerView.LayoutManager drawerLayoutManager;

    DrawerLayout drawer;

    ActionBarDrawerToggle mDrawerToggle;

    // Progress Dialog
    private ProgressDialog pDialog;

    String parkName;
>>>>>>> parent of c8cb35b... Continued refactoring duplicated code to base class

    //php search parks script

    //localhost :
    //testing on your device
    //put your local ip instead,  on windows, run CMD > ipconfig
    //or in mac's terminal type ifconfig and look for the ip under en0 or en1

    //testing on Emulator:
<<<<<<< HEAD
=======
//    private static final String SEARCH_PARKS_URL = "http://10.0.2.2:80/webservice/search.php";
>>>>>>> parent of c8cb35b... Continued refactoring duplicated code to base class
//    private static final String SEARCH_PARKS_URL = "http://10.0.2.2:80/webservice/search.php?search=";


    //testing from a real server:
<<<<<<< HEAD
    private static final String SEARCH_PARKS_URL = "http://60.240.144.91:80/webservice/search.php?search=";
=======
    //private static final String SEARCH_PARKS_URL = "http://www.mybringback.com/webservice/search.php";
//    private static final String SEARCH_PARKS_URL = "http://192.168.1.4:80/webservice/search.php?search=";
    private static final String SEARCH_PARKS_URL = "http://60.240.144.91:80/webservice/search.php?search=";

    //JSON IDS:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_NAME = "name";
    private static final String TAG_RESULTS = "results";
    private static final String TAG_ID = "id";
    private static final String TAG_SUBURB = "suburb";
    private static final String TAG_STREET = "street";
    private static final String TAG_LAT = "latitude";
    private static final String TAG_LONG = "longitude";
    private static final String TAG_DISTANCE = "distance";

    //it's important to note that the message is both in the parent branch of
    //our JSON tree that displays a "Post Available" or a "No Post Available" message,
    //and there is also a message for each individual post, listed under the "posts"
    //category, that displays what the user typed as their message.

    //An array of all of our comments
    private JSONArray mResults = null;
    //manages all of our comments in a list.
//    private ArrayList<HashMap<String, String>> mResultList;
    private ArrayList<ParkInfo> mResultList;

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private boolean mResolvingError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        mNavItems.add(new NavItem("Local", R.drawable.ic_place_grey_24dp));
        mNavItems.add(new NavItem("Top Rated", R.drawable.ic_grade_grey_24dp));
        mNavItems.add(new NavItem("Trending", R.drawable.ic_trending_up_grey_24dp));
        mNavItems.add(new NavItem("Recent", R.drawable.ic_access_time_grey_24dp));
        mNavItems.add(new NavItem("Settings", R.drawable.ic_settings_grey_24dp));
        mNavItems.add(new NavItem("Help & Feedback", R.drawable.ic_help_grey_24dp));

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ResultsActivity.this);
        String user = sp.getString("email", "emailAddress");
        String desc = "Visit Profile";
        int profile = 0;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Results");
        setSupportActionBar(toolbar);

        handleIntent(getIntent());

        mainRecyclerView = (RecyclerView) findViewById(R.id.cardList);
        mainRecyclerView.setHasFixedSize(true);

        mainLayoutManager = new LinearLayoutManager(this);
        mainRecyclerView.setLayoutManager(mainLayoutManager);

        drawerRecyclerView = (RecyclerView) findViewById(R.id.left_drawer);
        drawerRecyclerView.setHasFixedSize(true);
        drawerAdapter = new DrawerListAdapter(mNavItems, user, desc, profile, this);
        drawerRecyclerView.setAdapter(drawerAdapter);

        final GestureDetector mGestureDetector = new GestureDetector(ResultsActivity.this, new GestureDetector.SimpleOnGestureListener() {
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
//                    Toast.makeText(ResultsActivity.this, "The Item Clicked is: " + recyclerView.getChildPosition(child), Toast.LENGTH_SHORT).show();

                    Intent i;
                    switch (recyclerView.getChildLayoutPosition(child)) {
                        case 0:
//                            i = new Intent(MainActivity.this, ProfileActivity.class);
                            break;
                        case 1:
                            i = new Intent(ResultsActivity.this, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                            break;
                        case 2:
                            i = new Intent(ResultsActivity.this, TopRatedActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                            break;
                        case 3:
                            i = new Intent(ResultsActivity.this, TrendingActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                            break;
                        case 4:
                            i = new Intent(ResultsActivity.this, RecentActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                            break;
                        case 5:
                            i = new Intent(ResultsActivity.this, SettingsActivity.class);
                            startActivity(i);
                            break;
                        case 6:
//                            i = new Intent(MainActivity.this, HelpActivity.class);
//                            startActivity(i);
                            break;
                        default:
                            break;
                    }

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

        // Create a GoogleApiClient instance
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void handleIntent(Intent intent) {
        parkName = intent.getStringExtra("parkName");
        new LoadResults().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            query = intent.getStringExtra("query");
            //use the query to search your data somehow
//            new LoadResults().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    /**
     * Retrieves json data of comments
     */
    public void updateJSONData(String query) {
        // Instantiate the arraylist to contain all the JSON data.
        // we are going to use a bunch of key-value pairs, referring
        // to the json element name, and the content, for example,
        // message it the tag, and "I'm awesome" as the content..

        mResultList = new ArrayList<ParkInfo>();

        //Retrieving search query
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ResultsActivity.this);
        String search = sp.getString("search", "query");

        // Bro, it's time to power up the J parser
        JSONParser jParser = new JSONParser();
        // Feed the beast our comments url, and it spits us
        //back a JSON object.  Boo-yeah Jerome.
        JSONObject json = jParser.getJSONFromUrl(SEARCH_PARKS_URL+query);

        //when parsing JSON stuff, we should probably
        //try to catch any exceptions:
        try {

            //I know I said we would check if "Posts were Avail." (success==1)
            //before we tried to read the individual posts, but I lied...
            //mComments will tell us how many "posts" or comments are
            //available
            mResults = json.getJSONArray(TAG_RESULTS);

            // looping through all posts according to the json object returned
            for (int i = 0; i < mResults.length(); i++) {
                JSONObject c = mResults.getJSONObject(i);

                //gets the content of each tag
                String id = c.getString(TAG_ID);
                String name = c.getString(TAG_NAME);
                String street = c.getString(TAG_STREET);
                String suburb = c.getString(TAG_SUBURB);
                String latitude = c.getString(TAG_LAT);
                String longitude = c.getString(TAG_LONG);
                String distance = c.getString(TAG_DISTANCE);

                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                map.put(TAG_ID, id);
                map.put(TAG_NAME, name);
                map.put(TAG_STREET, street);
                map.put(TAG_SUBURB, suburb);
                map.put(TAG_LAT, latitude);
                map.put(TAG_LONG, longitude);
                map.put(TAG_DISTANCE, distance);

                // adding HashList to ArrayList
                mResultList.add(new ParkInfo(id, name, street, suburb, latitude, longitude, distance));
>>>>>>> parent of c8cb35b... Continued refactoring duplicated code to base class

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

//        For a ListActivity we need to set the List Adapter, and in order to do
//        that, we need to create a ListAdapter.  This SimpleAdapter,
//        will utilize our updated Hashmapped ArrayList,
//        use our single_post xml template for each item in our list,
//        and place the appropriate info from the list to the
//        correct GUI id.  Order is important here.

        mainAdapter = new ParkAdapter(mResultList);
        mainRecyclerView.setAdapter(mainAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
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

    @Override
    public void onConnected(Bundle bundle) {
        //        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        mLastLocation = new Location("mLocation");
        mLastLocation.setLatitude(Double.parseDouble("-27.3139480"));
        mLastLocation.setLongitude(Double.parseDouble("153.0576320"));
//        handleIntent(getIntent());
        if (mLastLocation != null) {
            handleIntent(getIntent());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mResolvingError) {  // more about this later
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private class LoadResults extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ResultsActivity.this);
            pDialog.setMessage("Loading Results...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {
            updateJSONData(parkName);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            updateList();
        }
    }
}


