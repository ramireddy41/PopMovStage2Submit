package com.popularmoviesstage2.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.popularmoviesstage2.model.BaseModel;
import com.popularmoviesstage2.model.ReviewModel;
import com.popularmoviesstage2.listeners.IReviewDowloaded;
import com.popularmoviesstage2.constant.AppConstants;
import com.popularmoviesstage2.repository.ReviewRepository;

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

import static com.popularmoviesstage2.constant.AppConstants.AUTHOR;
import static com.popularmoviesstage2.constant.AppConstants.CONTENT;
import static com.popularmoviesstage2.constant.AppConstants.ID;
import static com.popularmoviesstage2.constant.AppConstants.RESULTS;

/**
 * @author  Ramireddy
 */
public class ReviewAsyncTask extends AsyncTask<String, Void, List<BaseModel>> {
    private final String LOG_TAG = ReviewAsyncTask.class.getSimpleName();
    private final IReviewDowloaded mListener;
    private ReviewRepository mReviewRepository;

    public ReviewAsyncTask(IReviewDowloaded listener) {
        super();
        mListener = listener;
        mReviewRepository = new ReviewRepository();
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
            return getMovieReviewFromJson(moviesJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }


    private List<BaseModel> getMovieReviewFromJson(String moviesJsonStr) throws JSONException {


        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray resultsArray = moviesJson.getJSONArray(RESULTS);
        List<BaseModel> reviews = new ArrayList<>();

        for (int i = 0; i < resultsArray.length(); i++) {
            ReviewModel review = new ReviewModel();
            JSONObject movieInfo = resultsArray.getJSONObject(i);
            review.setId(movieInfo.getString(ID));
            review.setAuthor(movieInfo.getString(AUTHOR));
            review.setContent(movieInfo.getString(CONTENT));
            review.setUrl(movieInfo.getString(AppConstants.URL));
            reviews.add(review);
        }
        mReviewRepository.insert(reviews);
        return reviews;
    }


    @Override
    protected void onPostExecute(List<BaseModel> reviews) {
        super.onPostExecute(reviews);
        mListener.onReviewDownloaded(reviews);
    }
}
