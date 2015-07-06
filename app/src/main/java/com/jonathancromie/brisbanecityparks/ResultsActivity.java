package com.jonathancromie.brisbanecityparks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ResultsActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;

    //php search parks script

    //localhost :
    //testing on your device
    //put your local ip instead,  on windows, run CMD > ipconfig
    //or in mac's terminal type ifconfig and look for the ip under en0 or en1
    // private static final String READ_COMMENTS_URL = "http://xxx.xxx.x.x:1234/webservice/comments.php";

    //testing on Emulator:
    private static final String SEARCH_PARKS_URL = "http://10.0.2.2:80/webservice/search.php";

    //testing from a real server:
    //private static final String SEARCH_PARKS_URL = "http://www.mybringback.com/webservice/search.php";

    //JSON IDS:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_NAME = "name";
    private static final String TAG_POSTS = "results";
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
    private ArrayList<HashMap<String, String>> mResultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //loading the results via AsyncTask
        new LoadResults().execute();
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

        mResultList = new ArrayList<HashMap<String, String>>();

        // Bro, it's time to power up the J parser
        JSONParser jParser = new JSONParser();
        // Feed the beast our comments url, and it spits us
        //back a JSON object.  Boo-yeah Jerome.
        JSONObject json = jParser.getJSONFromUrl(SEARCH_PARKS_URL);

        //when parsing JSON stuff, we should probably
        //try to catch any exceptions:
        try {

            //I know I said we would check if "Posts were Avail." (success==1)
            //before we tried to read the individual posts, but I lied...
            //mComments will tell us how many "posts" or comments are
            //available
            mResults = json.getJSONArray(TAG_POSTS);

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
                mResultList.add(map);

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

        // For a ListActivity we need to set the List Adapter, and in order to do
        //that, we need to create a ListAdapter.  This SimpleAdapter,
        //will utilize our updated Hashmapped ArrayList,
        //use our single_post xml template for each item in our list,
        //and place the appropriate info from the list to the
        //correct GUI id.  Order is important here.

//        ListAdapter adapter = new SimpleAdapter(this, mCommentList,
//                R.layout.single_post, new String[] { TAG_TITLE, TAG_MESSAGE,
//                TAG_USERNAME }, new int[] { R.id.title, R.id.message,
//                R.id.username });
//
//        // I shouldn't have to comment on this one:
//        setListAdapter(adapter);
//
//        // Optional: when the user clicks a list item we
//        //could do something.  However, we will choose
//        //to do nothing...
//        ListView lv = getListView();
//        lv.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//
//                // This method is triggered if an item is click within our
//                // list. For our example we won't be using this, but
//                // it is useful to know in real life applications.
//
//            }
//        });

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
        protected Boolean doInBackground(Void... voids) {
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


