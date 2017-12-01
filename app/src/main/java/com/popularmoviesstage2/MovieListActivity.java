package com.popularmoviesstage2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.popularmoviesstage2.adapters.MovieAdapter;
import com.popularmoviesstage2.constant.AppConstants;
import com.popularmoviesstage2.listeners.IMovieDowloaded;
import com.popularmoviesstage2.model.BaseModel;
import com.popularmoviesstage2.model.MovieModel;
import com.popularmoviesstage2.repository.MovieRepository;
import com.popularmoviesstage2.tasks.MovieAsyncTask;
import com.popularmoviesstage2.utils.Utils;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static com.popularmoviesstage2.constant.AppConstants.MOVIE_PARCEL_KEY;

/**
 * @author Ramiredy
 */
public class MovieListActivity extends AppCompatActivity {

    private GridView mGridView;
    private MovieRepository mMovieRepository;
    private List<BaseModel> mMovieList;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridView = (GridView) findViewById(R.id.gridview);
        mGridView.setOnItemClickListener(moviePosterClickListener);
        mMovieRepository = new MovieRepository();

        showProgressDialog();
        if (savedInstanceState == null) {
            if (Utils.isNetworkAvailable(this)) {
                getMovieData(AppConstants.SORT_BY_MOST_POPULAR);
            }
            else{
                Toast.makeText(this, "Internet is not available, Displaying offline data!!!", Toast.LENGTH_LONG).show();
                mMovieList = mMovieRepository.queryByType(AppConstants.SORT_BY_MOST_POPULAR);
                mGridView.setAdapter(new MovieAdapter(getApplicationContext(), mMovieList));
                dismissProgressDialog();
            }
        } else {
            Parcelable[] parcelable = savedInstanceState.
                    getParcelableArray(MOVIE_PARCEL_KEY);

            if (parcelable != null) {
                int numMovieObjects = parcelable.length;
                List<BaseModel> movies = new ArrayList<>();
                for (int i = 0; i < numMovieObjects; i++) {
                    movies.add((MovieModel) parcelable[i]);
                }

                mGridView.setAdapter(new MovieAdapter(this, movies));
                dismissProgressDialog();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.add(Menu.NONE, 0, Menu.NONE, null).setVisible(true).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        menu.add(Menu.NONE, 0, Menu.NONE, null).setVisible(false).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        menu.add(Menu.NONE, 0, Menu.NONE, null).setVisible(false).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        int numMovieObjects = mGridView.getCount();
        if (numMovieObjects > 0) {
            MovieModel[] movies = new MovieModel[numMovieObjects];
            for (int i = 0; i < numMovieObjects; i++) {
                movies[i] = (MovieModel) mGridView.getItemAtPosition(i);
            }

            outState.putParcelableArray(AppConstants.MOVIE_PARCEL_KEY, movies);
        }

        super.onSaveInstanceState(outState);
    }

    public void showProgressDialog(){
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Downloading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.most_popular:
                showProgressDialog();
                getMovieData(AppConstants.SORT_BY_MOST_POPULAR);
                return true;
            case R.id.top_rated:
                showProgressDialog();
                getMovieData(AppConstants.SORT_BY_TOP_RATED);
                return true;
            case R.id.favorite:
                showProgressDialog();
                getFavoriteMovieData();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private final GridView.OnItemClickListener moviePosterClickListener = new GridView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MovieModel movie = (MovieModel) parent.getItemAtPosition(position);

            Intent intent = new Intent(getApplicationContext(), MovieDetailsActivity.class);
            intent.putExtra(AppConstants.MOVIE_PARCEL_KEY, movie);

            startActivity(intent);
        }
    };

    public void dismissProgressDialog(){
        if(mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    private void getMovieData(String sortingType) {
        if (Utils.isNetworkAvailable(this)) {
            IMovieDowloaded taskCompleted = new IMovieDowloaded() {
                @Override
                public void onMovieDownloaded(List<BaseModel> movies) {
                    mMovieList = movies;
                    mGridView.setAdapter(new MovieAdapter(getApplicationContext(), mMovieList));
                    dismissProgressDialog();
                }
            };
            try {
                MovieAsyncTask movieTask = new MovieAsyncTask(taskCompleted, sortingType);
                movieTask.execute(getMovieUrl(sortingType));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Internet is not available, Displaying offline data!!!", Toast.LENGTH_LONG).show();
            mMovieList = mMovieRepository.queryByType(sortingType);
            mGridView.setAdapter(new MovieAdapter(getApplicationContext(), mMovieList));
            dismissProgressDialog();
        }
    }


    private void getFavoriteMovieData() {
        List<BaseModel> movies = mMovieRepository.getFavorites();
        mGridView.setAdapter(new MovieAdapter(getApplicationContext(), movies));
        dismissProgressDialog();
    }


    private String getMovieUrl(String sortType) throws MalformedURLException {
        final String TMDB_BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
        final String SORT_BY_PARAM = "sort_by";
        final String API_KEY_PARAM = "api_key";

        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendQueryParameter(SORT_BY_PARAM, sortType)
                .appendQueryParameter(API_KEY_PARAM, AppConstants.API_KEY)
                .build();

        return builtUri.toString();
    }
}

