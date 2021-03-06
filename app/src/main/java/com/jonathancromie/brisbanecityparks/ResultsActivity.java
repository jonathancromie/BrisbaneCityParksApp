package com.jonathancromie.brisbanecityparks;

public class ResultsActivity extends _BaseResult {

    //php search parks script

    //localhost :
    //testing on your device
    //put your local ip instead,  on windows, run CMD > ipconfig
    //or in mac's terminal type ifconfig and look for the ip under en0 or en1

    //testing on Emulator:
//    private static final String SEARCH_PARKS_URL = "http://10.0.2.2:80/webservice/search.php?search=";


    //testing from a real server:
    private static final String SEARCH_PARKS_URL = "http://60.240.144.91:80/webservice/search.php?search=";


    @Override
    protected void onResume() {
        super.onResume();
        String query;
        query = getIntent().getStringExtra("query");
        setUrl(SEARCH_PARKS_URL+query);
        setToolbarTitle("Results");
    }
}


