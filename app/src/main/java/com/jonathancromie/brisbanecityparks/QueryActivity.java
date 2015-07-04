package com.jonathancromie.brisbanecityparks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by jonathancromie on 4/07/15.
 */
public class QueryActivity extends AsyncTask<String, Void, String> {

    private Context context;
    private TextView textView;

    public QueryActivity(Context context, TextView textView) {
        this.context = context;
        this.textView = textView;
    }

    @Override
    protected String doInBackground(String... params) {
        String link = "http://192.168.1.4/BrisbaneCityParks/queryPark.php";

        try {
            URL url = new URL(link);
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(link));
            HttpResponse response = client.execute(request);
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer sb = new StringBuffer("");
            String line="";

            while ((line = in.readLine()) != null) {
                sb.append(line);
                System.out.println(line);
                break;
            }
            in.close();
            return sb.toString();

        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result) {
        this.textView.setText(result);
    }
}
