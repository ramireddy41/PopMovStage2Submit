package com.popularmoviesstage2.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.popularmoviesstage2.R;
import com.popularmoviesstage2.model.BaseModel;
import com.popularmoviesstage2.model.ReviewModel;

import java.util.List;

/**
 * Created by RCHINTA on 11/29/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.RecyclerViewHolders> {

    List<BaseModel> movieReviewList;
    private Context context;

    public ReviewAdapter(Context context, List<BaseModel> movieVideoList){
        this.movieReviewList = movieVideoList;
        this.context = context;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_cell, parent, false);
        ReviewAdapter.RecyclerViewHolders rcv = new ReviewAdapter.RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {

        ReviewModel review = (ReviewModel)movieReviewList.get(position);

        holder.tvAuthor.setText("Author: "+review.getAuthor());
        holder.tvDesc.setText("Review: "+review.getContent());

    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        if(movieReviewList != null && movieReviewList.size()>0)
            return movieReviewList.size();
        return 0;
    }

    public class RecyclerViewHolders extends RecyclerView.ViewHolder {

        public TextView tvAuthor;
        public TextView tvDesc;

        public RecyclerViewHolders(View itemView) {
            super(itemView);
            tvAuthor = (TextView) itemView.findViewById(R.id.tvAuthor);
            tvDesc = (TextView) itemView.findViewById(R.id.tvDesc);
        }
    }
}
