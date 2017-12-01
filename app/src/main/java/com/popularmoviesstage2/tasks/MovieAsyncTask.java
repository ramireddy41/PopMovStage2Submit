package com.popularmoviesstage2.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.popularmoviesstage2.model.BaseModel;
import com.popularmoviesstage2.model.MovieModel;
import com.popularmoviesstage2.listeners.IMovieDowloaded;
import com.popularmoviesstage2.repository.MovieRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.popularmoviesstage2.constant.AppConstants.ID;
import static com.popularmoviesstage2.constant.AppConstants.RESULTS;
import static com.popularmoviesstage2.constant.AppConstants.ORIGINAL_TITLE;
import static com.popularmoviesstage2.constant.AppConstants.OVERVIEW;
import static com.popularmoviesstage2.constant.AppConstants.POSTER_PATH;
import static com.popularmoviesstage2.constant.AppConstants.RELEASE_DATE;
import static com.popularmoviesstage2.constant.AppConstants.VOTE_AVERAGE;

/**
 * @author  Ramireddy
 */
public class MovieAsyncTask extends AsyncTask<String, Void, List<BaseModel>> {
    private final String LOG_TAG = MovieAsyncTask.class.getSimpleName();
    private final IMovieDowloaded mListener;
    private MovieRepository mMovieRepository;
    private String mSortingType;

    public MovieAsyncTask(IMovieDowloaded listener, String sortingType) {
        super();
        mSortingType = sortingType;
        mListener = listener;
        mMovieRepository = new MovieRepository();
    }

    @Override
    protected List<BaseModel> doInBackground(String... params) {
        HttpURLConnection httpURLConnection = null;
        BufferedReader reader = null;

        String moviesJsonStr = null;

        try {
            URL url = new URL(params[0]);

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            StringBuilder builder = new StringBuilder();

            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }

            if (builder.length() == 0) {
                return null;
            }

            moviesJsonStr = builder.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getMoviesDataFromJson(moviesJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<BaseModel> getMoviesDataFromJson(String moviesJsonStr) throws JSONException {


        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray resultsArray = moviesJson.getJSONArray(RESULTS);
        List<BaseModel> movies = new ArrayList<>();

        for (int i = 0; i < resultsArray.length(); i++) {
            MovieModel movie = new MovieModel();
            JSONObject movieInfo = resultsArray.getJSONObject(i);
            movie.setOriginalTitle(movieInfo.getString(ORIGINAL_TITLE));
            movie.setPosterPath(movieInfo.getString(POSTER_PATH));
            movie.setOverview(movieInfo.getString(OVERVIEW));
            movie.setVoteAverage(movieInfo.getDouble(VOTE_AVERAGE));
            movie.setReleaseDate(movieInfo.getString(RELEASE_DATE));
            movie.setId(movieInfo.getLong(ID));
            movie.setSortingType(mSortingType);
            movies.add(movie);
        }
        mMovieRepository.insert(movies);
        return movies;
    }



    @Override
    protected void onPostExecute(List<BaseModel> movies) {
        super.onPostExecute(movies);
        mListener.onMovieDownloaded(movies);
    }
}
