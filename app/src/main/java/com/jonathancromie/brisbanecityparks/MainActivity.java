package com.jonathancromie.brisbanecityparks;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    private Toolbar toolbar;

//    RecyclerView mainRecyclerView;
//    ReviewAdapter mainAdapter;
//    RecyclerView.LayoutManager mainLayoutManager;

    TextView mLatitudeText;
    TextView mLongitudeText;
    TextView mLastUpdateTimeText;

    RecyclerView drawerRecyclerView;
    RecyclerView.Adapter drawerAdapter;
    RecyclerView.LayoutManager drawerLayoutManager;

    DrawerLayout drawer;

    ActionBarDrawerToggle mDrawerToggle;

    // Progress Dialog
    private ProgressDialog pDialog;

    //php login script location:

    //testing on Emulator:
//    private static final String RECENT_REVIEWS_URL = "http://10.0.2.2:80/webservice/recent.php";

    //testing from a real server:
    //private static final String RECENT_REVIEWS_URL = "http://www.mybringback.com/webservice/comments.php";
//    private static final String RECENT_REVIEWS_URL = "http://192.168.1.9:80/webservice/reviews.php?id=";


    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private static final String TAG_REVIEWS = "reviews";
    private static final String TAG_ID = "park_id";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_REVIEW = "review";
    private static final String TAG_RATING = "rating";
    private static final String TAG_DATE = "date_posted";

    //An array of all of our comments
    private JSONArray mReviews = null;
    //manages all of our comments in a list.
    private ArrayList<ReviewInfo> mReviewList;



    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private boolean mResolvingError = false;
    private boolean mRequestingLocationUpdates = true;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private String mLastUpdateTime;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // favourites?
        mNavItems.add(new NavItem("Home", "Homepage", R.drawable.ic_home_grey_24dp));
        mNavItems.add(new NavItem("Top Rated", "Find awesome parks", R.drawable.ic_grade_grey_24dp));
        mNavItems.add(new NavItem("Trending", "Which parks are popular right now", R.drawable.ic_trending_up_grey_24dp));
        mNavItems.add(new NavItem("Recent", "Latest reviews", R.drawable.ic_access_time_grey_24dp));
        mNavItems.add(new NavItem("Settings", "Customise your settings", R.drawable.ic_settings_grey_24dp));
        mNavItems.add(new NavItem("Help & Feedback", "Get help or submit feedback", R.drawable.ic_help_grey_24dp));

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String user = sp.getString("email", "emailAddress");
        String desc = "Visit Profile";
        int profile = 0;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        mainRecyclerView = (RecyclerView) findViewById(R.id.cardList);
//        mainRecyclerView.setHasFixedSize(true);
//        mainRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
//        mainLayoutManager = new LinearLayoutManager(this);
//        mainRecyclerView.setLayoutManager(mainLayoutManager);


        drawerRecyclerView = (RecyclerView) findViewById(R.id.left_drawer);
        drawerRecyclerView.setHasFixedSize(true);
        drawerAdapter = new DrawerListAdapter(mNavItems, user, desc, profile, this);
        drawerRecyclerView.setAdapter(drawerAdapter);

        final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
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
//                    Toast.makeText(MainActivity.this, "The Item Clicked is: " + recyclerView.getChildPosition(child), Toast.LENGTH_SHORT).show();

                    Intent i;
                    switch (recyclerView.getChildLayoutPosition(child)) {
                        case 0:
//                            i = new Intent(MainActivity.this, ProfileActivity.class);
                            break;
                        case 1:
//                            i = new Intent(MainActivity.this, MainActivity.class);
//                            startActivity(i);
                            break;
                        case 2:
                            i = new Intent(MainActivity.this, TopRatedActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                            break;
                        case 3:
                            i = new Intent(MainActivity.this, TrendingActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                            break;
                        case 4:
                            i = new Intent(MainActivity.this, RecentActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                            break;
                        case 5:
                            i = new Intent(MainActivity.this, SettingsActivity.class);
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

        mLatitudeText = (TextView) findViewById(R.id.mLatitudeText);
        mLongitudeText = (TextView) findViewById(R.id.mLongitudeText);
        mLastUpdateTimeText = (TextView) findViewById(R.id.mLastUpdateTimeText);

        buildGoogleApiClient();


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.search_menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
        }

        final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent i = new Intent(MainActivity.this, ResultsActivity.class);
                i.putExtra("parkName", query);
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

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
//        if (mLastLocation != null) {
//            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
//            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
//        }

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mResolvingError) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    private void updateUI() {
        mLatitudeText.setText(String.valueOf(mCurrentLocation.getLatitude()));
        mLongitudeText.setText(String.valueOf(mCurrentLocation.getLongitude()));
        mLastUpdateTimeText.setText(mLastUpdateTime);
    }
}
