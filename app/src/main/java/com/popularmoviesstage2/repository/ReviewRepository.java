package com.popularmoviesstage2.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.popularmoviesstage2.constant.AppConstants;
import com.popularmoviesstage2.database.SqliteDBHelper;
import com.popularmoviesstage2.model.BaseModel;
import com.popularmoviesstage2.model.ReviewModel;

import java.util.ArrayList;
import java.util.List;

import static com.popularmoviesstage2.constant.AppConstants.AUTHOR;
import static com.popularmoviesstage2.constant.AppConstants.CONTENT;
import static com.popularmoviesstage2.constant.AppConstants.ID;
import static com.popularmoviesstage2.constant.AppConstants.URL;

/**
 * Created by RCHINTA on 11/30/2017.
 */

public class ReviewRepository implements IRepository{

    @Override
    public void insert(BaseModel model) {

        try {
            ReviewModel reviewModel = (ReviewModel) model;

            SQLiteDatabase db = SqliteDBHelper.getSQLiteDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, reviewModel.getId());
            contentValues.put(AUTHOR, reviewModel.getAuthor());
            contentValues.put(CONTENT, reviewModel.getContent());
            contentValues.put(URL, reviewModel.getUrl());
            db.insert(AppConstants.REVIEW, null, contentValues);
        }catch (Exception e){
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
                ReviewModel reviewModel = (ReviewModel) baseModel;
                ContentValues contentValues = new ContentValues();
                contentValues.put(ID, reviewModel.getId());
                contentValues.put(AUTHOR, reviewModel.getAuthor());
                contentValues.put(CONTENT, reviewModel.getContent());
                contentValues.put(URL, reviewModel.getUrl());
                db.insert(AppConstants.REVIEW, null, contentValues);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {

        }
    }

    @Override
    public List<BaseModel> query() {

        SQLiteDatabase db = SqliteDBHelper.getSQLiteDatabase();
        Cursor cursor = db.rawQuery("select *from "+AppConstants.REVIEW, null);

        List<BaseModel> modelList = null;
        try {
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    modelList = new ArrayList<>();
                    do {
                        ReviewModel reviewModel = new ReviewModel();
                        reviewModel.setId(cursor.getString(cursor.getColumnIndex(ID)));
                        reviewModel.setAuthor(cursor.getString(cursor.getColumnIndex(AUTHOR)));
                        reviewModel.setContent(cursor.getString(cursor.getColumnIndex(CONTENT)));
                        reviewModel.setUrl(cursor.getString(cursor.getColumnIndex(URL)));
                        modelList.add(reviewModel);
                    } while (cursor.moveToNext());
                }
            }
        }catch (Exception e){
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
        Cursor cursor = db.rawQuery("select * from "+AppConstants.REVIEW+" where id="+id, null);
        ReviewModel reviewModel = null;
        try {
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        reviewModel = new ReviewModel();
                        reviewModel.setId(cursor.getString(cursor.getColumnIndex(ID)));
                        reviewModel.setAuthor(cursor.getString(cursor.getColumnIndex(AUTHOR)));
                        reviewModel.setContent(cursor.getString(cursor.getColumnIndex(CONTENT)));
                        reviewModel.setUrl(cursor.getString(cursor.getColumnIndex(URL)));
                    } while (cursor.moveToNext());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return reviewModel;
    }
}
