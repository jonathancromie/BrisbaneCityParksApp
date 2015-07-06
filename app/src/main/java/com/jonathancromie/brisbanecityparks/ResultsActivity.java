package com.jonathancromie.brisbanecityparks;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ResultsActivity extends Activity {

    RecyclerView mRecyclerView;
    ParkAdapter mAdapter;
    LayoutManager mLayoutManager;

    // Progress Dialog
    private ProgressDialog pDialog;

    //php search parks script

    //localhost :
    //testing on your device
    //put your local ip instead,  on windows, run CMD > ipconfig
    //or in mac's terminal type ifconfig and look for the ip under en0 or en1
    // private static final String READ_COMMENTS_URL = "http://xxx.xxx.x.x:1234/webservice/comments.php";

    //testing on Emulator:
//    private static final String SEARCH_PARKS_URL = "http://10.0.2.2:80/webservice/search.php";
    private static final String SEARCH_PARKS_URL = "http://10.0.2.2:80/webservice/search.php?search=";

    //testing from a real server:
    //private static final String SEARCH_PARKS_URL = "http://www.mybringback.com/webservice/search.php";

    //JSON IDS:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_NAME = "name";
    private static final String TAG_RESULTS = "results";
    private static final String TAG_POST_ID = "post_id";
    private static final String TAG_SUBURB = "suburb";
    private static final String TAG_STREET = "street";
    //it's important to note that the message is both in the parent branch of
    //our JSON tree that displays a "Post Available" or a "No Post Available" message,
    //and there is also a message for each individual post, listed under the "posts"
    //category, that displays what the user typed as their message.

    //An array of all of our comments
    private JSONArray mResults = null;
    //manages all of our comments in a list.
//    private ArrayList<HashMap<String, String>> mResultList;
    private ArrayList<ParkInfo> mResultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        mRecyclerView = (RecyclerView) findViewById(R.id.cardList);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
//
//        mAdapter = new ParkAdapter(mResultList);
//        mRecyclerView.setAdapter(mAdapter);

//        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
//        mAdapter = new ParkAdapter(mResultList);
//        recList.setHasFixedSize(true);
//        LinearLayoutManager llm = new LinearLayoutManager(this);
//        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        recList.setLayoutManager(llm);

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onResume() {
        super.onResume();
        //loading the results via AsyncTask
        new LoadResults().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

//    public void addComment(View v)
//    {
//        Intent i = new Intent(ReadComments.this, AddComment.class);
//        startActivity(i);
//    }

    /**
     * Retrieves json data of comments
     */
    public void updateJSONData() {
        // Instantiate the arraylist to contain all the JSON data.
        // we are going to use a bunch of key-value pairs, referring
        // to the json element name, and the content, for example,
        // message it the tag, and "I'm awesome" as the content..

//        mResultList = new ArrayList<HashMap<String, String>>();
        mResultList = new ArrayList<ParkInfo>();

        //Retrieving search query
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ResultsActivity.this);
        String search = sp.getString("search", "query");

        // Bro, it's time to power up the J parser
        JSONParser jParser = new JSONParser();
        // Feed the beast our comments url, and it spits us
        //back a JSON object.  Boo-yeah Jerome.
        JSONObject json = jParser.getJSONFromUrl(SEARCH_PARKS_URL+search);

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
                String name = c.getString(TAG_NAME);
                String street = c.getString(TAG_STREET);
                String suburb = c.getString(TAG_SUBURB);


                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                map.put(TAG_NAME, name);
                map.put(TAG_STREET, street);
                map.put(TAG_SUBURB, suburb);

                // adding HashList to ArrayList
                mResultList.add(new ParkInfo(name, street, suburb));

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

//        ParkAdapter mAdapter = new ParkAdapter(mResultList);
////        recList.setAdapter(mAdapter);
//        setListAdapter((ListAdapter) mAdapter);

//        mRecyclerView = (RecyclerView) findViewById(R.id.cardList);
//        mRecyclerView.setHasFixedSize(true);
//
//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ParkAdapter(mResultList);
        mRecyclerView.setAdapter(mAdapter);
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
            updateJSONData();
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


