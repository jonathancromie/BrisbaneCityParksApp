package com.jonathancromie.brisbanecityparks;

/**
 * Created by jonathancromie on 11/07/15.
 */
public class ReviewInfo {
    String park_id;
    String email;
    String review;
    String rating;
    String date_posted;

    public ReviewInfo(String park_id, String email, String review, String rating, String date_posted) {
        this.park_id = park_id;
        this.email = email;
        this.review = review;
        this.rating = rating;
        this.date_posted = date_posted;
    }
}
