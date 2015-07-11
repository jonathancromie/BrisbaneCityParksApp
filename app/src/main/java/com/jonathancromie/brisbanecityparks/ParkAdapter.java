package com.jonathancromie.brisbanecityparks;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jonathancromie on 6/07/15.
 */
public class ParkAdapter extends RecyclerView.Adapter<ParkAdapter.ParkViewHolder> {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ParkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView resultCard;
        TextView parkName;
        TextView parkStreet;
        TextView parkSuburb;

        String parkID;
        String latitude;
        String longitude;

        ImageButton btnRateReview;
        ImageButton btnFavourite;
        ImageButton btnShare;

        ParkViewHolder(View itemView) {
            super(itemView);
            resultCard = (CardView) itemView.findViewById(R.id.result_card);

            resultCard.setBackgroundColor(itemView.getResources().getColor(R.color.primary_light));

            parkName = (TextView) itemView.findViewById(R.id.txtName);
            parkStreet = (TextView) itemView.findViewById(R.id.txtStreet);
            parkSuburb = (TextView) itemView.findViewById(R.id.txtSuburb);

            btnRateReview = (ImageButton) itemView.findViewById(R.id.btnRateReview);
            btnFavourite = (ImageButton) itemView.findViewById(R.id.btnFavourite);
            btnShare = (ImageButton) itemView.findViewById(R.id.btnShare);

            resultCard.setOnClickListener(this);
            btnRateReview.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(v.getContext(), ParkActivity.class);
            i.putExtra("parkID", parkID);
            i.putExtra("parkName", parkName.getText());
            i.putExtra("parkStreet", parkStreet.getText());
            i.putExtra("parkSuburb", parkSuburb.getText());
            i.putExtra("latitude", latitude);
            i.putExtra("longitude", longitude);
            v.getContext().startActivity(i);
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
                .inflate(R.layout.result_card, viewGroup, false);

        return new ParkViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ParkViewHolder parkViewHolder, int i) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ParkInfo pi = parkList.get(i);
        parkViewHolder.parkID = pi.id;
        parkViewHolder.parkName.setText(pi.name);
        parkViewHolder.parkStreet.setText(pi.street);
        parkViewHolder.parkSuburb.setText(pi.suburb);
        parkViewHolder.latitude = pi.latitude;
        parkViewHolder.longitude = pi.longitude;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}