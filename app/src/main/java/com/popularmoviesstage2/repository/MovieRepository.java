package com.popularmoviesstage2.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.popularmoviesstage2.constant.AppConstants;
import com.popularmoviesstage2.database.SqliteDBHelper;
import com.popularmoviesstage2.model.BaseModel;
import com.popularmoviesstage2.model.MovieModel;

import java.util.ArrayList;
import java.util.List;

import static com.popularmoviesstage2.constant.AppConstants.ID;
import static com.popularmoviesstage2.constant.AppConstants.IS_FAVORITE;
import static com.popularmoviesstage2.constant.AppConstants.ORIGINAL_TITLE;
import static com.popularmoviesstage2.constant.AppConstants.OVERVIEW;
import static com.popularmoviesstage2.constant.AppConstants.POSTER_PATH;
import static com.popularmoviesstage2.constant.AppConstants.RELEASE_DATE;
import static com.popularmoviesstage2.constant.AppConstants.SORTING_TYPE;
import static com.popularmoviesstage2.constant.AppConstants.VOTE_AVERAGE;

/**
 * Created by RCHINTA on 11/30/2017.
 */

public class MovieRepository implements IRepository {
    @Override
    public void insert(BaseModel model) {

        try {
            MovieModel movieModel = (MovieModel) model;

            SQLiteDatabase db = SqliteDBHelper.getSQLiteDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, movieModel.getId());
            contentValues.put(ORIGINAL_TITLE, movieModel.getOriginalTitle());
            contentValues.put(POSTER_PATH, movieModel.getPosterPath());
            contentValues.put(OVERVIEW, movieModel.getOverview());
            contentValues.put(VOTE_AVERAGE, movieModel.getDetailedVoteAverage());
            contentValues.put(RELEASE_DATE, movieModel.getReleaseDate());
            contentValues.put(IS_FAVORITE, movieModel.getIsFavorite());
            contentValues.put(SORTING_TYPE, movieModel.getSortingType());
            db.insert(AppConstants.MOVIE, null, contentValues);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
        }

    }

    @Override
    public void insert(List<BaseModel> modelList) {

        SQLiteDatabase db = SqliteDBHelper.getSQLiteDatabase();
        try {
            for (BaseModel baseModel : modelList) {
                MovieModel movieModel = (MovieModel) baseModel;
                ContentValues contentValues = new ContentValues();
                contentValues.put(ID, movieModel.getId());
                contentValues.put(ORIGINAL_TITLE, movieModel.getOriginalTitle());
                contentValues.put(POSTER_PATH, movieModel.getPosterPath());
                contentValues.put(OVERVIEW, movieModel.getOverview());
                contentValues.put(VOTE_AVERAGE, movieModel.getDetailedVoteAverage());
                contentValues.put(RELEASE_DATE, movieModel.getReleaseDate());
                contentValues.put(IS_FAVORITE, movieModel.getIsFavorite());
                contentValues.put(SORTING_TYPE, movieModel.getSortingType());
                db.insert(AppConstants.MOVIE, null, contentValues);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
        }
    }

    @Override
    public List<BaseModel> query() {

        SQLiteDatabase db = SqliteDBHelper.getSQLiteDatabase();
        Cursor cursor = db.rawQuery("select *from "+AppConstants.MOVIE, null);

        List<BaseModel> modelList = null;
        try {

            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    modelList = new ArrayList<>();
                    do {
                        MovieModel movieModel = new MovieModel();
                        movieModel.setId(cursor.getLong(cursor.getColumnIndex(ID)));
                        movieModel.setOriginalTitle(cursor.getString(cursor.getColumnIndex(ORIGINAL_TITLE)));
                        movieModel.setOverview(cursor.getString(cursor.getColumnIndex(OVERVIEW)));
                        movieModel.setPosterPath(cursor.getString(cursor.getColumnIndex(POSTER_PATH)));
                        movieModel.setReleaseDate(cursor.getString(cursor.getColumnIndex(RELEASE_DATE)));
                        movieModel.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(VOTE_AVERAGE)));
                        movieModel.setIsFavorite(cursor.getInt(cursor.getColumnIndex(IS_FAVORITE)));
                        movieModel.setSortingType(cursor.getString(cursor.getColumnIndex(SORTING_TYPE)));
                        modelList.add(movieModel);
                    } while (cursor.moveToNext());
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return modelList;
    }

    public List<BaseModel> queryByType(String sortingType) {

        SQLiteDatabase db = SqliteDBHelper.getSQLiteDatabase();
        Cursor cursor = db.rawQuery("select *from "+AppConstants.MOVIE+" where "+SORTING_TYPE+"='"+sortingType+"'", null);

        List<BaseModel> modelList = null;
        try {

            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    modelList = new ArrayList<>();
                    do {
                        MovieModel movieModel = new MovieModel();
                        movieModel.setId(cursor.getLong(cursor.getColumnIndex(ID)));
                        movieModel.setOriginalTitle(cursor.getString(cursor.getColumnIndex(ORIGINAL_TITLE)));
                        movieModel.setOverview(cursor.getString(cursor.getColumnIndex(OVERVIEW)));
                        movieModel.setPosterPath(cursor.getString(cursor.getColumnIndex(POSTER_PATH)));
                        movieModel.setReleaseDate(cursor.getString(cursor.getColumnIndex(RELEASE_DATE)));
                        movieModel.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(VOTE_AVERAGE)));
                        movieModel.setIsFavorite(cursor.getInt(cursor.getColumnIndex(IS_FAVORITE)));
                        movieModel.setSortingType(cursor.getString(cursor.getColumnIndex(SORTING_TYPE)));
                        modelList.add(movieModel);
                    } while (cursor.moveToNext());
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return modelList;
    }

    @Override
    public BaseModel query(long id) {
        SQLiteDatabase db = SqliteDBHelper.getSQLiteDatabase();
        Cursor cursor = db.rawQuery("select * from "+AppConstants.MOVIE+" where id="+id, null);
        MovieModel movieModel = null;
        try {
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        movieModel = new MovieModel();
                        movieModel.setId(cursor.getLong(cursor.getColumnIndex(ID)));
                        movieModel.setOriginalTitle(cursor.getString(cursor.getColumnIndex(ORIGINAL_TITLE)));
                        movieModel.setOverview(cursor.getString(cursor.getColumnIndex(OVERVIEW)));
                        movieModel.setPosterPath(cursor.getString(cursor.getColumnIndex(POSTER_PATH)));
                        movieModel.setReleaseDate(cursor.getString(cursor.getColumnIndex(RELEASE_DATE)));
                        movieModel.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(VOTE_AVERAGE)));
                        movieModel.setIsFavorite(cursor.getInt(cursor.getColumnIndex(IS_FAVORITE)));
                        movieModel.setSortingType(cursor.getString(cursor.getColumnIndex(SORTING_TYPE)));
                    } while (cursor.moveToNext());
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return movieModel;
    }

    public void updateFavorite(long id, int isFavorite){
        SQLiteDatabase db = SqliteDBHelper.getSQLiteDatabase();
        try {
            db.execSQL("update " + AppConstants.MOVIE + " SET " + IS_FAVORITE + "=" + isFavorite + " where id=" + id);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public int getFavorite(long id){
        SQLiteDatabase db = SqliteDBHelper.getSQLiteDatabase();
        Cursor cursor = db.rawQuery("select "+IS_FAVORITE+" from "+AppConstants.MOVIE+" where id="+id, null);
        int isFavorite = 0;
        try{

            if(cursor != null && cursor.getCount()>0){
                if(cursor.moveToFirst()){
                    isFavorite = cursor.getInt(cursor.getColumnIndex(IS_FAVORITE));
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return isFavorite;
    }

    public List<BaseModel> getFavorites() {

        SQLiteDatabase db = SqliteDBHelper.getSQLiteDatabase();
        Cursor cursor = db.rawQuery("select *from "+AppConstants.MOVIE+" where "+IS_FAVORITE + "= 1", null);

        List<BaseModel> modelList = null;
        try {

            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    modelList = new ArrayList<>();
                    do {
                        MovieModel movieModel = new MovieModel();
                        movieModel.setId(cursor.getLong(cursor.getColumnIndex(ID)));
                        movieModel.setOriginalTitle(cursor.getString(cursor.getColumnIndex(ORIGINAL_TITLE)));
                        movieModel.setOverview(cursor.getString(cursor.getColumnIndex(OVERVIEW)));
                        movieModel.setPosterPath(cursor.getString(cursor.getColumnIndex(POSTER_PATH)));
                        movieModel.setReleaseDate(cursor.getString(cursor.getColumnIndex(RELEASE_DATE)));
                        movieModel.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(VOTE_AVERAGE)));
                        movieModel.setIsFavorite(cursor.getInt(cursor.getColumnIndex(IS_FAVORITE)));
                        movieModel.setSortingType(cursor.getString(cursor.getColumnIndex(SORTING_TYPE)));
                        modelList.add(movieModel);
                    } while (cursor.moveToNext());
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return modelList;
    }
}
