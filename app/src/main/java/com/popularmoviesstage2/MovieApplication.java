package com.popularmoviesstage2;

import android.app.Application;

import com.popularmoviesstage2.database.SqliteDBHelper;

/**
 * Created by RCHINTA on 11/30/2017.
 */

public class MovieApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SqliteDBHelper.getInstance().init(getApplicationContext());
    }


}
