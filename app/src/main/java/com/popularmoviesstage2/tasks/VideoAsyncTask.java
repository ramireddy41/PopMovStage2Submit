package com.popularmoviesstage2.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.popularmoviesstage2.model.BaseModel;
import com.popularmoviesstage2.model.VideoModel;
import com.popularmoviesstage2.listeners.IVideoDowloaded;
import com.popularmoviesstage2.repository.VideoRepository;

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
import static com.popularmoviesstage2.constant.AppConstants.KEY;
import static com.popularmoviesstage2.constant.AppConstants.NAME;
import static com.popularmoviesstage2.constant.AppConstants.RESULTS;
import static com.popularmoviesstage2.constant.AppConstants.SITE;
import static com.popularmoviesstage2.constant.AppConstants.SIZE;
import static com.popularmoviesstage2.constant.AppConstants.TYPE;

/**
 * @author  Ramireddy
 */
public class VideoAsyncTask extends AsyncTask<String, Void, List<BaseModel>> {
    private final String LOG_TAG = VideoAsyncTask.class.getSimpleName();
    private final IVideoDowloaded mListener;
    private VideoRepository mVideoRepository;

    public VideoAsyncTask(IVideoDowloaded listener) {
        super();
        mListener = listener;
        mVideoRepository = new VideoRepository();
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
            return getMovieVideoFromJson(moviesJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }


    private List<BaseModel> getMovieVideoFromJson(String moviesJsonStr) throws JSONException {


        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray resultsArray = moviesJson.getJSONArray(RESULTS);
        List<BaseModel> videos = new ArrayList<>();

        for (int i = 0; i < resultsArray.length(); i++) {
            VideoModel video = new VideoModel();
            JSONObject movieInfo = resultsArray.getJSONObject(i);
            video.setId(movieInfo.getString(ID));
            video.setKey(movieInfo.getString(KEY));
            video.setName(movieInfo.getString(NAME));
            video.setSite(movieInfo.getString(SITE));
            video.setSize(movieInfo.getInt(SIZE));
            video.setType(movieInfo.getString(TYPE));
            videos.add(video);
        }
        mVideoRepository.insert(videos);

        return videos;
    }


    @Override
    protected void onPostExecute(List<BaseModel> videos) {
        super.onPostExecute(videos);
        mListener.onVideoDownloaded(videos);
    }
}
