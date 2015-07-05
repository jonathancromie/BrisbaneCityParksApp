package com.jonathancromie.brisbanecityparks;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class RegisterActivity extends Activity implements OnClickListener {

    private EditText email, pass, firstname, lastname, sex, dob, phone, street, suburb, city, postcode, state;
    private Button mRegister;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //php login script

    //localhost :
    //testing on your device
    //put your local ip instead,  on windows, run CMD > ipconfig
    //or in mac's terminal type ifconfig and look for the ip under en0 or en1
    // private static final String LOGIN_URL = "http://xxx.xxx.x.x:1234/webservice/register.php";

    //testing on Emulator:
    private static final String LOGIN_URL = "http://10.0.2.2:80/webservice/register.php";

    //testing from a real server:
    //private static final String LOGIN_URL = "http://www.yourdomain.com/webservice/register.php";

    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (EditText)findViewById(R.id.email);
        pass = (EditText)findViewById(R.id.password);
        firstname = (EditText)findViewById(R.id.firstname);
        lastname = (EditText)findViewById(R.id.lastname);
        sex = (EditText)findViewById(R.id.sex);
        dob = (EditText)findViewById(R.id.dob);
        phone = (EditText)findViewById(R.id.phone);
        street = (EditText)findViewById(R.id.street);
        suburb =  (EditText)findViewById(R.id.suburb);
        city = (EditText)findViewById(R.id.city);
        postcode = (EditText)findViewById(R.id.postcode);
        state = (EditText)findViewById(R.id.state);

        mRegister = (Button)findViewById(R.id.register);
        mRegister.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
    public void onClick(View view) {
        new CreateUser().execute();
    }

    class CreateUser extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Creating User...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            // Check for success tag
            int success;
            String emailAddress = email.getText().toString();
            String password = pass.getText().toString();
            String firstName = firstname.getText().toString();
            String lastName = lastname.getText().toString();
            String gender = sex.getText().toString();
            String dateOfBirth = dob.getText().toString();
            String phoneNumber = phone.getText().toString();
            String addressStreet = street.getText().toString();
            String addressSuburb = suburb.getText().toString();
            String addressCity = city.getText().toString();
            String addressPostCode = postcode.getText().toString();
            String addressState = state.getText().toString();

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("emailAddress", emailAddress));
                params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("firstName", firstName));
                params.add(new BasicNameValuePair("lastName", lastName));
                params.add(new BasicNameValuePair("gender", gender));
                params.add(new BasicNameValuePair("dateOfBirth", dateOfBirth));
                params.add(new BasicNameValuePair("phoneNumber", phoneNumber));
                params.add(new BasicNameValuePair("addressStreet", addressStreet));
                params.add(new BasicNameValuePair("addressSuburb", addressSuburb));
                params.add(new BasicNameValuePair("addressCity", addressCity));
                params.add(new BasicNameValuePair("addressPostCode", addressPostCode));
                params.add(new BasicNameValuePair("addressState", addressState));

                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

                // full json response
                Log.d("Login attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("User Created!", json.toString());
                    finish();
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(RegisterActivity.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }
}
