package com.popularmoviesstage2.listeners;

import com.popularmoviesstage2.model.BaseModel;
import com.popularmoviesstage2.model.MovieModel;

import java.util.List;

/**
 * @author Ramireddy
 */
public interface IMovieDowloaded {
    void onMovieDownloaded(List<BaseModel> movies);
}
