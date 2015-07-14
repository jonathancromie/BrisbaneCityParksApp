package com.jonathancromie.brisbanecityparks;

import android.os.Bundle;

public class TrendingActivity extends _BaseActivity {

    //php login script location:

    //testing on Emulator:
    private static final String TRENDING_URL = "http://10.0.2.2:80/webservice/trending.php";

    //testing from a real server:
//    private static final String TRENDING_URL = "http://60.240.144.91:80/webservice/trending.php";


    @Override
    protected void onResume() {
        super.onResume();
        setUrl(TRENDING_URL);
        setToolbarTitle("Trending");
    }
}