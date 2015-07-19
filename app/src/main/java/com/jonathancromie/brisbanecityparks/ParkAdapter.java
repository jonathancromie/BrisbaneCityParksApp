package com.jonathancromie.brisbanecityparks;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathancromie on 6/07/15.
 */
public class ParkAdapter extends RecyclerView.Adapter<ParkAdapter.ParkViewHolder> {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ParkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final String ADD_FAVOURITES_URL = "http://10.0.2.2:80/webservice/addfavourites.php";

        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";

        CardView resultCard;
        TextView parkName;
        TextView parkStreet;
        TextView parkSuburb;
        TextView parkDistance;

        String parkID;
        String latitude;
        String longitude;

        Button explore;
        Button share;
        ImageButton favourite;

        ProgressDialog pDialog;

        ParkViewHolder(View itemView) {
            super(itemView);
            resultCard = (CardView) itemView.findViewById(R.id.result_card);

            parkName = (TextView) itemView.findViewById(R.id.txtName);
            parkStreet = (TextView) itemView.findViewById(R.id.txtStreet);
            parkSuburb = (TextView) itemView.findViewById(R.id.txtSuburb);
            parkDistance = (TextView) itemView.findViewById(R.id.txtDistance);

            explore = (Button) itemView.findViewById(R.id.explore);
            share = (Button) itemView.findViewById(R.id.share);
            favourite = (ImageButton) itemView.findViewById(R.id.favourite);

//            resultCard.setOnClickListener(this);
            explore.setOnClickListener(this);
            share.setOnClickListener(this);
            favourite.setOnClickListener(this);
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onClick(View v) {

            int id = v.getId();

            switch (id) {
                case R.id.explore:
                    Intent i = new Intent(v.getContext(), ParkActivity.class);
                    i.putExtra("parkID", parkID);
                    i.putExtra("parkName", parkName.getText());
                    i.putExtra("parkStreet", parkStreet.getText());
                    i.putExtra("parkSuburb", parkSuburb.getText());
                    i.putExtra("latitude", latitude);
                    i.putExtra("longitude", longitude);
                    v.getContext().startActivity(i);
                    break;

                case R.id.share:
                    break;

                case R.id.favourite:
                    new AddFavourite().execute();
                    break;

            }
        }



        private class AddFavourite extends AsyncTask<String, String, String>  {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(itemView.getContext());
                pDialog.setMessage("Adding Favourite...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();

            }

            @Override
            protected String doInBackground(String... args) {
                // Check for success tag
                int success;

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());
                String email = sp.getString("email", "anon");

                try {
                    // Building Parameters
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("email_address", email));
                    params.add(new BasicNameValuePair("park_id", parkID));

                    Log.d("request!", "starting");

                    // JSON parser class
                    JSONParser jsonParser = new JSONParser();

                    //Posting user data to script
                    JSONObject json = jsonParser.makeHttpRequest(
                            ADD_FAVOURITES_URL, "POST", params);

                    // full json response
                    Log.d("Add favourite attempt", json.toString());

                    // json success element
                    success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Log.d("Favourite Added!", json.toString());
                        ((Activity)itemView.getContext()).finish();
                        return json.getString(TAG_MESSAGE);
                    }else{
                        Log.d("Favourite Failure!", json.getString(TAG_MESSAGE));
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
                    Toast.makeText(itemView.getContext(), file_url, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private ArrayList<ParkInfo> parkList;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ParkAdapter(ArrayList<ParkInfo> parkList) {
        this.parkList = parkList;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return parkList.size();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ParkViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.result_card, viewGroup, false);

        return new ParkViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ParkViewHolder parkViewHolder, int i) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ParkInfo pi = parkList.get(i);
        parkViewHolder.parkID = pi.id;
        parkViewHolder.parkName.setText(pi.name);
        parkViewHolder.parkStreet.setText(pi.street);
        parkViewHolder.parkSuburb.setText(pi.suburb);
        parkViewHolder.latitude = pi.latitude;
        parkViewHolder.longitude = pi.longitude;
        parkViewHolder.parkDistance .setText(pi.distance + "kms away");
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}