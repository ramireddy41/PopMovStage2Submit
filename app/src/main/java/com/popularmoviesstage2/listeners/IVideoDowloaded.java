package com.popularmoviesstage2.listeners;

import com.popularmoviesstage2.model.BaseModel;
import com.popularmoviesstage2.model.VideoModel;

import java.util.List;

/**
 * @author Ramireddy
 */
public interface IVideoDowloaded {
    void onVideoDownloaded(List<BaseModel> movies);
}
