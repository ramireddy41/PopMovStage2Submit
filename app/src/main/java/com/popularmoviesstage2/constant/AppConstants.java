package com.popularmoviesstage2.constant;

/**
 * Created by RCHINTA on 11/20/2017.
 */

public class AppConstants {
    public static final String RESULTS = "results";

    //Movie
    public static final String ID = "id";
    public static final String ORIGINAL_TITLE = "original_title";
    public static final String POSTER_PATH = "poster_path";
    public static final String OVERVIEW = "overview";
    public static final String VOTE_AVERAGE = "vote_average";
    public static final String RELEASE_DATE = "release_date";
    public static final String IS_FAVORITE = "is_favorite";
    public static final String SORTING_TYPE = "sorting_type";

    //Video
    public static final String ISO = "iso_639_1";
    public static final String KEY = "key";
    public static final String NAME = "name";
    public static final String SITE = "site";
    public static final String SIZE = "size";
    public static final String TYPE = "type";

    //Review
    public static final String AUTHOR = "author";
    public static final String CONTENT = "content";
    public static final String URL = "url";

    public static final String API_BASE_URL = "http://api.themoviedb.org/3/movie/";
    public static final String API_KEY = "<please put your api key here>";
    public static final String API_PARAM_PAGE = "page";
    public static final String API_PARAM_KEY = "api_key";
    public static final String APP_MEMORY_LOCATION = "PopularMovies2";
    public static final String DATA_BASE_FOLDER = "DataBase";
    public static final String DATABASE_NAME = "Movie.db";
    public static final String MOVIE = "movie";
    public static final String VIDEO = "video";
    public static final String REVIEW = "review";

    public static final String MOVIE_PARCEL_KEY = "movie_parcel";
    public static final String VIDEO_PARCEL_KEY = "video_parcel";
    public static final String REVIEW_PARCEL_KEY = "review_parcel";

    public static final String SORT_BY_MOST_POPULAR = "popularity.desc";
    public static final String SORT_BY_TOP_RATED = "vote_average.desc";



    public static final String[] CREATE_ALL_TABLES_QUERY = {"CREATE TABLE IF NOT EXISTS "+ MOVIE
            +"("+ID+" INTEGER NOT NULL PRIMARY KEY, "
            +ORIGINAL_TITLE + " TEXT, "
            +POSTER_PATH+" TEXT, "
            +OVERVIEW+" TEXT, "
            +VOTE_AVERAGE+" DOUBLE, "
            +RELEASE_DATE+" TEXT, "
            +IS_FAVORITE+ " INTEGER, "
            +SORTING_TYPE+" TEXT);",

            "CREATE TABLE IF NOT EXISTS "+VIDEO
             +"("+ ID+" INTEGER NOT NULL PRIMARY KEY, "
            +ISO+" TEXT, "
            +KEY+" TEXT, "
            +NAME +" DOUBLE, "
            +SITE +" TEXT, "
            +SIZE +" DOUBLE, "
            +TYPE +" TEXT);",

            "CREATE TABLE IF NOT EXISTS "+REVIEW
            +"("+ID+" INTEGER NOT NULL PRIMARY KEY, "
            +AUTHOR+" TEXT, "
            +CONTENT+" TEXT, "
            +URL+" TEXT);"};

    public static final String TMDB_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185";

}
