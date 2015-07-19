package com.jonathancromie.brisbanecityparks;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by jonathancromie on 11/07/15.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private static String latitude;
    private static String longitude;

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        CardView reviewCard;
        TextView email;
        TextView review;
        TextView rating;
        TextView datePosted;
        RatingBar ratingBar;

        String parkID;

        public ReviewViewHolder(View itemView) {
            super(itemView);

            // List of Reviews
            reviewCard = (CardView) itemView.findViewById(R.id.review_card);
            email = (TextView) itemView.findViewById(R.id.email);
            review = (TextView) itemView.findViewById(R.id.review);
            rating = (TextView) itemView.findViewById(R.id.rating);
//            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);

            datePosted = (TextView) itemView.findViewById(R.id.date_posted);
        }

//        @Override
//        public void onClick(View v) {
//            if (v.getId() == R.id.map) {
//                Intent i = new Intent(v.getContext(), MapsActivity.class);
//                i.putExtra("latitude", latitude);
//                i.putExtra("longitude", longitude);
//                v.getContext().startActivity(i);
//            }
//        }
    }

    private ArrayList<ReviewInfo> reviewList;

    public ReviewAdapter(ArrayList<ReviewInfo> reviewList) {
        this.reviewList = reviewList;
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.review_card, viewGroup, false);

        return new ReviewViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder reviewViewHolder, int i) {
        ReviewInfo ri = reviewList.get(i);
        reviewViewHolder.parkID = ri.park_id;
        reviewViewHolder.email.setText(ri.email);
        reviewViewHolder.review.setText(ri.review);
        reviewViewHolder.rating.setText(ri.rating);
        reviewViewHolder.datePosted.setText(ri.date_posted);

    }




}
