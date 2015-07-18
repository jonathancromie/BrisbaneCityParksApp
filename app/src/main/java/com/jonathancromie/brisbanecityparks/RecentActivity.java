package com.jonathancromie.brisbanecityparks;

public class RecentActivity extends _BaseResult {

    //php login script location:

    //testing on Emulator:
    private static final String RECENT_REVIEWS_URL = "http://10.0.2.2:80/webservice/recent.php";

    //testing from a real server:
//    private static final String RECENT_REVIEWS_URL = "http://60.240.144.91:80/webservice/recent.php";


    @Override
    protected void onResume() {
        super.onResume();
        setUrl(RECENT_REVIEWS_URL);
        setToolbarTitle("Recent");
    }
}
