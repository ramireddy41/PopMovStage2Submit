package com.popularmoviesstage2.repository;

import com.popularmoviesstage2.model.BaseModel;

import java.util.List;

/**
 * Created by RCHINTA on 11/30/2017.
 */

public interface IRepository {

    public void insert(BaseModel model);
    public void insert(List<BaseModel> modelList);
    public List<BaseModel> query();
    public BaseModel query(long id);


}
