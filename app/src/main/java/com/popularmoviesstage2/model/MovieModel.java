package com.popularmoviesstage2.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Ramireddy
 */
public class MovieModel implements BaseModel {


    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private long id;
    private String mOriginalTitle;
    private String mPosterPath;
    private String mOverview;
    private Double mVoteAverage;
    private String mReleaseDate;

    // 0=false, 1=true
    private int isFavorite = 0;
    private String mSortingType;

    public String getSortingType() {
        return mSortingType;
    }

    public void setSortingType(String mSortingType) {
        this.mSortingType = mSortingType;
    }

    public int getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }

    public MovieModel() {
    }

    public void setOriginalTitle(String originalTitle) {
        mOriginalTitle = originalTitle;
    }

    public void setPosterPath(String posterPath) {
        mPosterPath = posterPath;
    }

    public void setOverview(String overview) {
        if(!overview.equals("null")) {
            mOverview = overview;
        }
    }

    public void setVoteAverage(Double voteAverage) {
        mVoteAverage = voteAverage;
    }

    public void setReleaseDate(String releaseDate) {
        if(!releaseDate.equals("null")) {
            mReleaseDate = releaseDate;
        }
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public String getOverview() {
        return mOverview;
    }

    private Double getVoteAverage() {
        return mVoteAverage;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getDetailedVoteAverage() {
        return String.valueOf(getVoteAverage()) + "/10";
    }

    public String getDateFormat() {
        return DATE_FORMAT;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mOriginalTitle);
        dest.writeString(mPosterPath);
        dest.writeString(mOverview);
        dest.writeValue(mVoteAverage);
        dest.writeString(mReleaseDate);
        dest.writeLong(id);
    }

    private MovieModel(Parcel in) {
        mOriginalTitle = in.readString();
        mPosterPath = in.readString();
        mOverview = in.readString();
        mVoteAverage = (Double) in.readValue(Double.class.getClassLoader());
        mReleaseDate = in.readString();
        id = in.readLong();
    }

    public static final Parcelable.Creator<MovieModel> CREATOR = new Parcelable.Creator<MovieModel>() {
        public MovieModel createFromParcel(Parcel source) {
            return new MovieModel(source);
        }

        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };
}
