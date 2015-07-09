package com.jonathancromie.brisbanecityparks;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ParkActivity extends Activity implements View.OnClickListener {

    TextView txtID;
    TextView txtName;
    TextView txtStreet;
    TextView txtSuburb;

    String latitude;
    String longitude;

    private Button btnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park);

        txtID = (TextView) findViewById(R.id.txtID);
        txtName = (TextView) findViewById(R.id.txtName);
        txtStreet = (TextView) findViewById(R.id.txtStreet);
        txtSuburb = (TextView) findViewById(R.id.txtSuburb);

        Intent intent = getIntent();
        txtID.setText(intent.getStringExtra("id"));
        txtName.setText(intent.getStringExtra("parkName"));
        txtStreet.setText(intent.getStringExtra("parkStreet"));
        txtSuburb.setText(intent.getStringExtra("parkSuburb"));
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");

        btnMap = (Button) findViewById(R.id.btnMap);
        btnMap.setOnClickListener(this);
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

    public void viewMap(String latitude, String longitude) {

    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(ParkActivity.this, MapsActivity.class);
        i.putExtra("latitude", latitude);
        i.putExtra("longitude", longitude);
        startActivity(i);
    }
}
