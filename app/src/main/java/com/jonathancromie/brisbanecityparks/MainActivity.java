package com.jonathancromie.brisbanecityparks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

    private EditText search;
    private Button mSubmit;
    private TextView results;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = (EditText) findViewById(R.id.search);
        mSubmit = (Button) findViewById(R.id.submit);
        results = (TextView) findViewById(R.id.results);

        mSubmit.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
    public void onClick(View v) {
//        new DisplayResults().execute();
        Intent i = new Intent(MainActivity.this, ResultsActivity.class);
        startActivity(i);
    }

//    private class DisplayResults extends AsyncTask<String, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(MainActivity.this);
//            pDialog.setMessage("Collecting results...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(true);
//            pDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... strins) {
//            // Check for success tag
//            int success;
//            String searchQuery = search.getText().toString();
//            try {
//                // Building Parameters
//                List<NameValuePair> params = new ArrayList<NameValuePair>();
//                params.add(new BasicNameValuePair("searchQuery", searchQuery));
//
//                Log.d("request!", "starting");
//                // getting product details by making HTTP request
//                JSONObject json = jsonParser.makeHttpRequest(
//                        LOGIN_URL, "POST", params);
//
//                // check your log for json response
//                Log.d("Search attempt", json.toString());
//
//                // json success tag
//                success = json.getInt(TAG_SUCCESS);
//                if (success == 1) {
//                    Log.d("Search Successful!", json.toString());
//                    finish();
//                    return json.getString(TAG_MESSAGE);
//                }else{
//                    Log.d("Search Failure!", json.getString(TAG_MESSAGE));
//                    return json.getString(TAG_MESSAGE);
//
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String file_url) {
//            // dismiss the dialog once product deleted
//            pDialog.dismiss();
//            if (file_url != null){
//                Toast.makeText(MainActivity.this, file_url, Toast.LENGTH_LONG).show();
//            }
//        }
//    }
}
