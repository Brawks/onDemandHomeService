package com.uottawa.despacithree.despacithree;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class RatingList extends ArrayAdapter<Rating> {
    private Activity context;
    private List<Rating> ratings;

    public RatingList (Activity context, List<Rating> ratings){
        super(context, R.layout.datalist_rating, ratings);
        this.context = context;
        this.ratings = ratings;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View ratingListView = inflater.inflate(R.layout.datalist_rating, null, true);

        TextView ratingName = (TextView) ratingListView.findViewById(R.id.ratingRate);
        TextView ratingComment = (TextView) ratingListView.findViewById(R.id.ratingComment);
        Rating currentRating = ratings.get(position);

        ratingName.setText("Rated: " + currentRating.getRating() + " by user : " + currentRating.getHomeownerName());
        ratingComment.setText("Commentary :\n" + currentRating.getComment());


        return ratingListView;
    }
}
