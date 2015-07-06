package com.jonathancromie.brisbanecityparks;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by jonathancromie on 6/07/15.
 */
public class ParkViewHolder extends RecyclerView.ViewHolder {

    protected TextView vName;
    protected TextView vStreet;
    protected TextView vSuburb;
    protected TextView vTitle;

    public ParkViewHolder(View v) {
        super(v);

        vName = (TextView) v.findViewById(R.id.txtName);
        vStreet = (TextView) v.findViewById(R.id.txtStreet);
        vSuburb = (TextView) v.findViewById(R.id.txtSuburb);
        vTitle = (TextView) v.findViewById(R.id.title);
    }
}
