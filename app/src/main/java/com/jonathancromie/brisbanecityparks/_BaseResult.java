package com.jonathancromie.brisbanecityparks;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jonathancromie on 14/07/15.
 */
public class _BaseResult extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

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

    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    private Toolbar toolbar;

    RecyclerView mainRecyclerView;
    ParkAdapter mainAdapter;
    RecyclerView.LayoutManager mainLayoutManager;

    RecyclerView drawerRecyclerView;
    RecyclerView.Adapter drawerAdapter;
    RecyclerView.LayoutManager drawerLayoutManager;

    DrawerLayout drawer;

    ActionBarDrawerToggle mDrawerToggle;

    // Progress Dialog
    private ProgressDialog pDialog;

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private boolean mResolvingError;

    private ArrayList<ParkInfo> mResultList;
    private JSONArray mResults = null;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_result);

        mNavItems.add(new NavItem("Reviews", R.drawable.ic_rate_review_blue_24dp));
        mNavItems.add(new NavItem("Favourites", R.drawable.ic_favorite_pink_24dp));
        mNavItems.add(new NavItem("Local", R.drawable.ic_place_grey_24dp));
        mNavItems.add(new NavItem("Top Rated", R.drawable.ic_grade_grey_24dp));
        mNavItems.add(new NavItem("Trending", R.drawable.ic_trending_up_grey_24dp));
        mNavItems.add(new NavItem("Recent", R.drawable.ic_access_time_grey_24dp));
        mNavItems.add(new NavItem("Settings", R.drawable.ic_settings_grey_24dp));
        mNavItems.add(new NavItem("Help & Feedback", R.drawable.ic_help_grey_24dp));

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(_BaseResult.this);
        String user = sp.getString("user", "user");
        String email = sp.getString("email", "emailAddress");
        int profile = 0;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        handleIntent(getIntent());

        mainRecyclerView = (RecyclerView) findViewById(R.id.cardList);
        mainRecyclerView.setHasFixedSize(true);
        mainRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mainLayoutManager = new LinearLayoutManager(this);
        mainRecyclerView.setLayoutManager(mainLayoutManager);

        drawerRecyclerView = (RecyclerView) findViewById(R.id.left_drawer);
        drawerRecyclerView.setHasFixedSize(true);
        drawerRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        drawerAdapter = new DrawerListAdapter(mNavItems, user, email, profile, this);
        drawerRecyclerView.setAdapter(drawerAdapter);

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

    protected void onResume() {
        super.onResume();
        handleIntent(getIntent());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void handleIntent(Intent intent) {
        new LoadResults().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    @Override
    public void onConnected(Bundle bundle) {
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        mLastLocation = new Location("mLocation");
        mLastLocation.setLatitude(Double.parseDouble("-27.3139480"));
        mLastLocation.setLongitude(Double.parseDouble("153.0576320"));
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.search_menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) _BaseResult.this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(_BaseResult.this.getComponentName()));
        }

        final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent i = new Intent(_BaseResult.this, ResultsActivity.class);
                i.putExtra("query", query);
                startActivity(i);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return true;
            }
        };

        searchView.setOnQueryTextListener(queryTextListener);
//        return super.onCreateOptionsMenu(menu);

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

    /**
     * Retrieves json data of comments
     */
    public void updateJSONData(String url) {
        // Instantiate the arraylist to contain all the JSON data.
        // we are going to use a bunch of key-value pairs, referring
        // to the json element name, and the content, for example,
        // message it the tag, and "I'm awesome" as the content..

        mResultList = new ArrayList<ParkInfo>();

        // Bro, it's time to power up the J parser
        JSONParser jParser = new JSONParser();
        // Feed the beast our comments url, and it spits us
        //back a JSON object.  Boo-yeah Jerome.
        JSONObject json = jParser.getJSONFromUrl(url);



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

                //annndddd, our JSON data is up to date same with our array list
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts the parsed data into our listview
     */
    protected void updateList() {

//        For a ListActivity we need to set the List Adapter, and in order to do
//        that, we need to create a ListAdapter.  This SimpleAdapter,
//        will utilize our updated Hashmapped ArrayList,
//        use our single_post xml template for each item in our list,
//        and place the appropriate info from the list to the
//        correct GUI id.  Order is important here.

        mainAdapter = new ParkAdapter(mResultList);
        mainRecyclerView.setAdapter(mainAdapter);
    }

//    public RecyclerView.Adapter getMainAdapter() {
//        return mainAdapter;
//    }
//
//    public RecyclerView getMainRecyclerView() {
//        return mainRecyclerView;
//    }

    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    private class LoadResults extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(_BaseResult.this);
            pDialog.setMessage("Loading Results...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            updateJSONData(getUrl());
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            updateList();
        }
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}

