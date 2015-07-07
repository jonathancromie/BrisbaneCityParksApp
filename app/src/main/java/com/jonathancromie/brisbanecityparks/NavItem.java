package com.jonathancromie.brisbanecityparks;

import android.content.Context;

/**
 * Created by Jonathan on 7/7/2015.
 */
public class NavItem {
    String title;
    String subTitle;
    int icon;

    public NavItem(String title, String subTitle, int icon) {
        this.title = title;
        this.subTitle = subTitle;
        this.icon = icon;
    }
}
