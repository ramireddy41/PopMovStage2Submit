package com.popularmoviesstage2.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.popularmoviesstage2.constant.AppConstants;
import com.popularmoviesstage2.database.SqliteDBHelper;
import com.popularmoviesstage2.model.BaseModel;
import com.popularmoviesstage2.model.VideoModel;

import java.util.ArrayList;
import java.util.List;

import static com.popularmoviesstage2.constant.AppConstants.ID;
import static com.popularmoviesstage2.constant.AppConstants.ISO;
import static com.popularmoviesstage2.constant.AppConstants.KEY;
import static com.popularmoviesstage2.constant.AppConstants.NAME;
import static com.popularmoviesstage2.constant.AppConstants.SITE;
import static com.popularmoviesstage2.constant.AppConstants.SIZE;
import static com.popularmoviesstage2.constant.AppConstants.TYPE;

/**
 * Created by RCHINTA on 11/30/2017.
 */

public class VideoRepository implements IRepository {
    @Override
    public void insert(BaseModel model) {
        try {
            VideoModel movieModel = (VideoModel) model;

            SQLiteDatabase db = SqliteDBHelper.getSQLiteDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, movieModel.getId());
            contentValues.put(ISO, movieModel.getIso());
            contentValues.put(KEY, movieModel.getKey());
            contentValues.put(NAME, movieModel.getName());
            contentValues.put(SITE, movieModel.getSite());
            contentValues.put(SIZE, movieModel.getSize());
            contentValues.put(TYPE, movieModel.getType());
            db.insert(AppConstants.VIDEO, null, contentValues);
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
                VideoModel movieModel = (VideoModel) baseModel;
                ContentValues contentValues = new ContentValues();
                contentValues.put(ID, movieModel.getId());
                contentValues.put(ISO, movieModel.getIso());
                contentValues.put(KEY, movieModel.getKey());
                contentValues.put(NAME, movieModel.getName());
                contentValues.put(SITE, movieModel.getSite());
                contentValues.put(SIZE, movieModel.getSize());
                contentValues.put(TYPE, movieModel.getType());
                db.insert(AppConstants.VIDEO, null, contentValues);
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
        Cursor cursor = db.rawQuery("select *from "+AppConstants.VIDEO, null);
        List<BaseModel> modelList = null;
        try {
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    modelList = new ArrayList<>();
                    do {
                        VideoModel videoModel = new VideoModel();
                        videoModel.setId(cursor.getString(cursor.getColumnIndex(ID)));
                        videoModel.setIso(cursor.getString(cursor.getColumnIndex(ISO)));
                        videoModel.setKey(cursor.getString(cursor.getColumnIndex(KEY)));
                        videoModel.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                        videoModel.setSite(cursor.getString(cursor.getColumnIndex(SITE)));
                        videoModel.setSize(cursor.getInt(cursor.getColumnIndex(SIZE)));
                        modelList.add(videoModel);
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
        Cursor cursor = db.rawQuery("select * from "+AppConstants.VIDEO+" where id="+id, null);
        VideoModel videoModel = null;
        try {
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        videoModel = new VideoModel();
                        videoModel.setId(cursor.getString(cursor.getColumnIndex(ID)));
                        videoModel.setIso(cursor.getString(cursor.getColumnIndex(ISO)));
                        videoModel.setKey(cursor.getString(cursor.getColumnIndex(KEY)));
                        videoModel.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                        videoModel.setSite(cursor.getString(cursor.getColumnIndex(SITE)));
                        videoModel.setSize(cursor.getInt(cursor.getColumnIndex(SIZE)));
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
        return videoModel;
    }
}
