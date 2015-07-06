package com.jonathancromie.brisbanecityparks;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathancromie on 6/07/15.
 */
public class ParkAdapter extends RecyclerView.Adapter<ParkAdapter.ParkViewHolder> {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ParkViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView parkName;
        TextView parkStreet;
        TextView parkSuburb;

        ParkViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            parkName = (TextView) itemView.findViewById(R.id.txtName);
            parkStreet = (TextView) itemView.findViewById(R.id.txtStreet);
            parkSuburb = (TextView) itemView.findViewById(R.id.txtSuburb);
        }
    }

    private ArrayList<ParkInfo> parkList;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ParkAdapter(ArrayList<ParkInfo> parkList) {
        this.parkList = parkList;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return parkList.size();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ParkViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);

        return new ParkViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ParkViewHolder parkViewHolder, int i) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ParkInfo pi = parkList.get(i);
        parkViewHolder.parkName.setText(pi.name);
        parkViewHolder.parkStreet.setText(pi.street);
        parkViewHolder.parkSuburb.setText(pi.suburb);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}