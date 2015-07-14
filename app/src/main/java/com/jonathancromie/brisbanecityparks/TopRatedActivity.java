package com.jonathancromie.brisbanecityparks;

import android.os.Bundle;

public class TopRatedActivity extends _BaseActivity {



    //php login script location:

    //testing on Emulator:
    private static final String TOP_RATED_URL = "http://10.0.2.2:80/webservice/toprated.php";

    //testing from a real server:
//    private static final String TOP_RATED_URL = "http://60.240.144.91:80/webservice/toprated.php";

    @Override
    protected void onResume() {
        super.onResume();
        setUrl(TOP_RATED_URL);
        setToolbarTitle("Top Rated");
    }
}
