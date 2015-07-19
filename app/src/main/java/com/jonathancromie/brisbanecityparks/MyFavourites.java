package com.jonathancromie.brisbanecityparks;

public class MyFavourites extends _BaseResult {

    //php search parks script

    //localhost :
    //testing on your device
    //put your local ip instead,  on windows, run CMD > ipconfig
    //or in mac's terminal type ifconfig and look for the ip under en0 or en1

    //testing on Emulator:
//    private static final String LOAD_FAVOURITES_URL = "http://10.0.2.2:80/webservice/loadfavourites.php?email=";


    //testing from a real server:
    private static final String LOAD_FAVOURITES_URL = "http://60.240.144.91:80/webservice/myfavourites.php?email=";


    @Override
    protected void onResume() {
        super.onResume();
        String query;
        query = getIntent().getStringExtra("extra");
        setUrl(LOAD_FAVOURITES_URL+query);
        setToolbarTitle("My Favourites");
    }
}
