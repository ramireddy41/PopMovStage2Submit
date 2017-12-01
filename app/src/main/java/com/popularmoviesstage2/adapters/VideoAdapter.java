package com.popularmoviesstage2.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.popularmoviesstage2.R;
import com.popularmoviesstage2.model.BaseModel;
import com.popularmoviesstage2.model.VideoModel;

import java.util.List;

/**
 * Created by RCHINTA on 11/29/2017.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.RecyclerViewHolders> {

    List<BaseModel> movieVideoList;
    private Context context;

    public VideoAdapter(Context context, List<BaseModel> movieVideoList){
        this.movieVideoList = movieVideoList;
        this.context = context;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_cell, parent, false);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {

        VideoModel video = (VideoModel)movieVideoList.get(position);
        holder.itemView.setTag(video);
        holder.tvVideoName.setText(video.getName());
        holder.tvVideoName.setTextSize(20);

        holder.llTrailerCell.setTag(video);
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        int height = windowManager.getDefaultDisplay().getHeight();
        holder.llTrailerCell.setLayoutParams(new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
        holder.llTrailerCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoModel video = (VideoModel)view.getTag();
                Uri uri = Uri.parse("http://www.youtube.com/watch?v=" + video.getKey());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri) ;
                context.startActivity(intent);
            }
        });
    }



    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        if(movieVideoList != null && movieVideoList.size()>0){
            return movieVideoList.size();
        }
        return 0;
    }


    public class RecyclerViewHolders extends RecyclerView.ViewHolder {

        public TextView tvVideoName;
        public LinearLayout llTrailerCell;

        public RecyclerViewHolders(View itemView) {
            super(itemView);
            tvVideoName = (TextView) itemView.findViewById(R.id.tvVideoName);
            llTrailerCell = (LinearLayout)itemView.findViewById(R.id.llTrailerCell);
        }
    }
}
