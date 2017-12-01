package com.popularmoviesstage2.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ReviewModel implements BaseModel {

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getAuthor() {
      return author;
   }

   public void setAuthor(String author) {
      this.author = author;
   }

   public String getContent() {
      return content;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public String getUrl() {
      return url;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   private String id;
   private String author;
   private String content;
   private String url;

   public ReviewModel(){

   }

   @Override
   public int describeContents() {
      return 0;
   }

   @Override
   public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(id);
      dest.writeString(author);
      dest.writeString(content);
      dest.writeValue(url);
   }

   private ReviewModel(Parcel in) {
      id = in.readString();
      author = in.readString();
      content = in.readString();
      url = in.readString();
   }

   public static final Parcelable.Creator<ReviewModel> CREATOR = new Parcelable.Creator<ReviewModel>() {
      public ReviewModel createFromParcel(Parcel source) {
         return new ReviewModel(source);
      }

      public ReviewModel[] newArray(int size) {
         return new ReviewModel[size];
      }
   };

}
