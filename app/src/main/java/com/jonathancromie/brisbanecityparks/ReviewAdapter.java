package com.jonathancromie.brisbanecityparks;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jonathancromie on 11/07/15.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        CardView reviewCard;
        TextView email;
        TextView review;
        TextView rating;
        TextView datePosted;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            reviewCard = (CardView) itemView.findViewById(R.id.review_card);

            reviewCard.setBackgroundColor(itemView.getResources().getColor(R.color.primary_light));

            email = (TextView) itemView.findViewById(R.id.email);
            review = (TextView) itemView.findViewById(R.id.review);
            rating = (TextView) itemView.findViewById(R.id.rating);
            datePosted = (TextView) itemView.findViewById(R.id.date_posted);
        }
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
                .inflate(R.layout.result_card, viewGroup, false);

        return new ReviewViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder reviewViewHolder, int i) {
        ReviewInfo ri = reviewList.get(i);
        reviewViewHolder.email.setText(ri.email);
        reviewViewHolder.review.setText(ri.review);
        reviewViewHolder.rating.setText(ri.rating);
        reviewViewHolder.datePosted.setText(ri.date_posted);

    }




}
