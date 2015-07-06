package com.jonathancromie.brisbanecityparks;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jonathancromie on 6/07/15.
 */
public class ParkAdapter extends RecyclerView.Adapter<ParkViewHolder> {

    private List<ParkInfo> parkList;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ParkAdapter(List<ParkInfo> parkList) {
        this.parkList = parkList;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return parkList.size();
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ParkViewHolder parkViewHolder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ParkInfo pi = parkList.get(position);
        parkViewHolder.vName.setText(pi.name);
        parkViewHolder.vStreet.setText(pi.street);
        parkViewHolder.vSuburb.setText(pi.suburb);

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ParkViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);

        return new ParkViewHolder(v);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
//    public static class ParkViewHolder extends RecyclerView.ViewHolder {
//
////        // each data item is just a string in this case
////        public ParkViewHolder(TextView v) {
////            super(v);
////        }
//    }








}