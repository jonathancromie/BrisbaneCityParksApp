package com.jonathancromie.brisbanecityparks;

import android.os.Bundle;
import android.os.PersistableBundle;

public class MainActivity extends _BaseResult {

    //php login script location:

    //testing on Emulator:
    private static final String NEARBY_PARKS_URL = "http://10.0.2.2:80/webservice/nearby.php";

    //testing from a real server:
//    private static final String NEARBY_PARKS_URL = "http://60.240.144.91:80/webservice/nearby.php";

    @Override
    protected void onResume() {
        super.onResume();
        setUrl(NEARBY_PARKS_URL);
        setToolbarTitle("Local");
    }
}
