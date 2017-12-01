package com.popularmoviesstage2.listeners;

import com.popularmoviesstage2.model.BaseModel;
import com.popularmoviesstage2.model.ReviewModel;

import java.util.List;

/**
 * @author Ramireddy
 */
public interface IReviewDowloaded {
    void onReviewDownloaded(List<BaseModel> movies);
}
