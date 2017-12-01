package com.popularmoviesstage2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.popularmoviesstage2.listeners.IReviewDowloaded;
import com.popularmoviesstage2.listeners.IVideoDowloaded;
import com.popularmoviesstage2.adapters.ReviewAdapter;
import com.popularmoviesstage2.adapters.VideoAdapter;
import com.popularmoviesstage2.constant.AppConstants;
import com.popularmoviesstage2.model.BaseModel;
import com.popularmoviesstage2.model.MovieModel;
import com.popularmoviesstage2.repository.MovieRepository;
import com.popularmoviesstage2.tasks.ReviewAsyncTask;
import com.popularmoviesstage2.tasks.VideoAsyncTask;
import com.popularmoviesstage2.utils.Utils;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.popularmoviesstage2.constant.AppConstants.API_BASE_URL;
import static com.popularmoviesstage2.constant.AppConstants.API_PARAM_KEY;
import static com.popularmoviesstage2.constant.AppConstants.MOVIE_PARCEL_KEY;
import static com.popularmoviesstage2.constant.AppConstants.REVIEW_PARCEL_KEY;
import static com.popularmoviesstage2.constant.AppConstants.TMDB_POSTER_BASE_URL;
import static com.popularmoviesstage2.constant.AppConstants.VIDEO_PARCEL_KEY;

/**
 * @author Ramireddy
 */
public class MovieDetailsActivity extends AppCompatActivity {

    private final String LOG_TAG = MovieDetailsActivity.class.getSimpleName();
    RecyclerView rvWatchTrailer;
    RecyclerView rvReview;
    VideoAdapter movieVideoAdapter;
    ReviewAdapter movieReviewAdapter;
    private MovieModel mMovieModel;
    private ImageView ivFavorite;
    private MovieRepository mMovieRepository;
    private ArrayList<BaseModel> mVideoModelList;
    private ArrayList<BaseModel> mReviewModelList;
    private LinearLayoutManager mLinearLayoutManager;
    private ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        TextView tvOriginalTitle = (TextView) findViewById(R.id.textview_original_title);
        ImageView ivPoster = (ImageView) findViewById(R.id.ivPoster);
        TextView tvOverView = (TextView) findViewById(R.id.textview_overview);
        TextView tvVoteAverage = (TextView) findViewById(R.id.tvVoteAverage);
        TextView tvReleaseDate = (TextView) findViewById(R.id.tvReleaseDate);
        rvWatchTrailer = (RecyclerView) findViewById(R.id.rvWatchTrailer);
        rvReview = (RecyclerView) findViewById(R.id.rvReview);
        ivFavorite = (ImageView)findViewById(R.id.ivFavorite);
        mScrollView = (ScrollView)findViewById(R.id.scrollView);

        mMovieRepository = new MovieRepository();
        mLinearLayoutManager = new LinearLayoutManager(this);
        rvWatchTrailer.setHasFixedSize(false);
        rvWatchTrailer.setLayoutManager(mLinearLayoutManager);
        rvWatchTrailer.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

        mLinearLayoutManager = new LinearLayoutManager(this);
        rvReview.setHasFixedSize(false);
        rvReview.setLayoutManager(mLinearLayoutManager);
        rvReview.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            mMovieModel = intent.getParcelableExtra(AppConstants.MOVIE_PARCEL_KEY);
            getMovieData();
            getReviewData();
        } else {
            mMovieModel = savedInstanceState.getParcelable(MOVIE_PARCEL_KEY);
            mVideoModelList = savedInstanceState.getParcelableArrayList(VIDEO_PARCEL_KEY);
            mReviewModelList = savedInstanceState.getParcelableArrayList(REVIEW_PARCEL_KEY);
            rvWatchTrailer.setAdapter(movieVideoAdapter = new VideoAdapter(MovieDetailsActivity.this, mVideoModelList));
            rvReview.setAdapter(movieReviewAdapter = new ReviewAdapter(MovieDetailsActivity.this, mReviewModelList));
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mScrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        }, 100);

        setFavIcon();
        ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFavIcon();
            }
        });


        tvOriginalTitle.setText(getResources().getString(R.string.title)+ mMovieModel.getOriginalTitle());

        Picasso.with(this)
                .load(TMDB_POSTER_BASE_URL+mMovieModel.getPosterPath())
                .resize(getResources().getInteger(R.integer.movie_poster_width),
                        getResources().getInteger(R.integer.movie_poster_height))
                .error(R.drawable.not_available)
                .placeholder(R.drawable.loading)
                .into(ivPoster);

        String overView = mMovieModel.getOverview();
        if (overView == null) {
            tvOverView.setTypeface(null, Typeface.ITALIC);
            overView = getResources().getString(R.string.no_summary_was_found);
        }
        tvOverView.setText(getResources().getString(R.string.synopsys)+overView);
        tvVoteAverage.setText(mMovieModel.getDetailedVoteAverage());

        String releaseDate = mMovieModel.getReleaseDate();
        if(releaseDate != null) {
            try {
                releaseDate = getDate(this,
                        releaseDate, mMovieModel.getDateFormat());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            tvReleaseDate.setTypeface(null, Typeface.ITALIC);
            releaseDate = getResources().getString(R.string.no_release_date_found);
        }
        tvReleaseDate.setText(releaseDate);

    }

    public static String getDate(Context context, String date, String format)
            throws ParseException {
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return dateFormat.format(simpleDateFormat.parse(date));
    }

    private void getMovieData() {
        if (Utils.isNetworkAvailable(this)) {
            IVideoDowloaded taskCompleted = new IVideoDowloaded() {
                @Override
                public void onVideoDownloaded(List<BaseModel> movies) {
                    mVideoModelList = new ArrayList<>();
                    mVideoModelList.addAll(movies);
                    rvWatchTrailer.setAdapter(movieVideoAdapter = new VideoAdapter(MovieDetailsActivity.this, mVideoModelList));
                }
            };
            try {
                VideoAsyncTask videoTask = new VideoAsyncTask(taskCompleted);
                videoTask.execute(getMovieVideoUrl(mMovieModel.getId()));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Internet is not available, Displaying offline data!!!", Toast.LENGTH_LONG).show();
        }
    }

    private void getReviewData() {
        if (Utils.isNetworkAvailable(this)) {
            IReviewDowloaded taskCompleted = new IReviewDowloaded() {
                @Override
                public void onReviewDownloaded(List<BaseModel> reviews) {
                    mReviewModelList = new ArrayList<>();
                    mReviewModelList.addAll(reviews);
                    rvReview.setAdapter(movieReviewAdapter = new ReviewAdapter(MovieDetailsActivity.this, mReviewModelList));
                }
            };
            try {
                ReviewAsyncTask reviewTask = new ReviewAsyncTask(taskCompleted);
                reviewTask.execute(getReviewUrl(mMovieModel.getId()));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, getString(R.string.internet_connection_error), Toast.LENGTH_LONG).show();
        }
    }

    public String getMovieVideoUrl(long movieId) {
        String path = String.format("%s/videos", movieId);
        Uri builtUri = Uri.parse(API_BASE_URL).buildUpon()
                .appendEncodedPath(path)

                .appendQueryParameter(API_PARAM_KEY, AppConstants.API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url.toString();
    }

    public String getReviewUrl(long movieId) {
        String path = String.format("%s/reviews", movieId);
        Uri builtUri = Uri.parse(API_BASE_URL).buildUpon()
                .appendEncodedPath(path)

                .appendQueryParameter(API_PARAM_KEY, AppConstants.API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url.toString();
    }

    private void setFavIcon(){
        int isFavorite = mMovieRepository.getFavorite(mMovieModel.getId());
        if(isFavorite == 0) {
            ivFavorite.setImageResource(R.drawable.fav_icon);

        }
        else {
            ivFavorite.setImageResource(R.drawable.fav_icon_mark);
        }
    }

    private void updateFavIcon(){
        int isFavorite = mMovieRepository.getFavorite(mMovieModel.getId());
        if(isFavorite == 0) {
            ivFavorite.setImageResource(R.drawable.fav_icon_mark);
            mMovieRepository.updateFavorite(mMovieModel.getId(), 1);

        }
        else {
            ivFavorite.setImageResource(R.drawable.fav_icon);
            mMovieRepository.updateFavorite(mMovieModel.getId(), 0);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        if(mMovieModel != null){
            outState.putParcelable(AppConstants.MOVIE_PARCEL_KEY, mMovieModel);
        }

        if(mVideoModelList != null && mVideoModelList.size()>0){
            outState.putParcelableArrayList(AppConstants.VIDEO_PARCEL_KEY, mVideoModelList);
        }

        if(mReviewModelList != null && mReviewModelList.size() >0 ){
            outState.putParcelableArrayList(AppConstants.REVIEW_PARCEL_KEY, mReviewModelList);
        }

        super.onSaveInstanceState(outState);
    }

}
