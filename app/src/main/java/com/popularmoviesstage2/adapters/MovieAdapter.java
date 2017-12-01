package com.popularmoviesstage2.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.popularmoviesstage2.model.BaseModel;
import com.popularmoviesstage2.model.MovieModel;
import com.popularmoviesstage2.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.popularmoviesstage2.constant.AppConstants.TMDB_POSTER_BASE_URL;

/**
 * @author Ramireddy
 */
public class MovieAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<BaseModel> mMoviesList;

    public MovieAdapter(Context context, List<BaseModel> movies) {
        mContext = context;
        mMoviesList = movies;
    }

    @Override
    public int getCount() {
        if (mMoviesList == null || mMoviesList.size() == 0) {
            return 0;
        }

        return mMoviesList.size();
    }

    @Override
    public MovieModel getItem(int position) {
        if (mMoviesList == null || mMoviesList.size() == 0) {
            return null;
        }

        return (MovieModel)mMoviesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
        } else {
            imageView = (ImageView) convertView;
        }
        MovieModel movieModel = (MovieModel)mMoviesList.get(position);
        Picasso.with(mContext)
                .load(TMDB_POSTER_BASE_URL+movieModel.getPosterPath())
                .resize(mContext.getResources().getInteger(R.integer.movie_poster_width),
                        mContext.getResources().getInteger(R.integer.movie_poster_height))
                .error(R.drawable.not_available)
                .placeholder(R.drawable.loading)
                .into(imageView);

        return imageView;
    }

}
