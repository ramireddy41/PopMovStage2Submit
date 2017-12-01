package com.popularmoviesstage2.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.popularmoviesstage2.constant.AppConstants;


public class SqliteDBHelper {

    private static final String TAG = SqliteDBHelper.class.getSimpleName();

    public final int CURRENT_DATABASE_VERSION = 1;


    private static SqliteDBHelper dbHelper;
    //    private static final String DB_PATH = Environment.getExternalStorageDirectory() + File.separator + AppConstants.APP_MEMORY_LOCATION + File.separator + AppConstants.DATA_BASE_FOLDER + File.separator + AppConstants.DATABASE_NAME;
    private static final String DB_PATH = AppConstants.DATABASE_NAME;
    private static SQLiteDatabase mSQLiteDatabase = null;

    public static SqliteDBHelper getInstance() {
        if (dbHelper == null) {
            dbHelper = new SqliteDBHelper();
        }


        return dbHelper;

    }

    //To open the database
    public static SQLiteDatabase openDataBase() throws SQLException {
        //Open the database
        if (mSQLiteDatabase == null || !mSQLiteDatabase.isOpen())
            mSQLiteDatabase = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.CREATE_IF_NECESSARY);
        return mSQLiteDatabase;
    }


    public synchronized static void closedatabase() {
        if (mSQLiteDatabase != null)
            mSQLiteDatabase.close();
    }

    public static SQLiteDatabase getSQLiteDatabase() {

        return openDataBase();
    }


    public void init(Context context) {
        try {
            mSQLiteDatabase = new DBCreatorHelper(context).getWritableDatabase();
        } catch (Exception e) {
            Log.i("DBhealper", "Exception::" + e.getMessage());
        }
    }

    public class DBCreatorHelper extends SQLiteOpenHelper {

        private Context mContext = null;

        public DBCreatorHelper(Context context) {
            super(context, DB_PATH, null, CURRENT_DATABASE_VERSION);
            this.mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            for (String statement : AppConstants.CREATE_ALL_TABLES_QUERY) {
                db.execSQL(statement);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            Cursor cc = db.rawQuery("PRAGMA journal_mode=DELETE", null);
            cc.close();
        }
    }


}
