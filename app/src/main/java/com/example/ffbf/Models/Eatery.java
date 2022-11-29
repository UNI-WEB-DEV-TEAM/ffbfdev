package com.example.ffbf.Models;

import android.media.Rating;
import android.os.Parcel;
import android.os.Parcelable;

public class Eatery implements Parcelable {

    private String name, description, url, id ,location, userId;
    private Boolean veg, non_veg;
    private Rating rating;

    public Eatery(String name, String desc, String url, String id, String location, Boolean veg, Boolean non_veg, String userId) {
        this.name = name;
        this.description = desc;
        this.url = url;
        this.id = id;
        this.location = location;
        this.veg = veg;
        this.non_veg = non_veg;
        this.userId = userId;
    }

    public Eatery() {
    }

    protected Eatery(Parcel in) {
        name = in.readString();
        description = in.readString();
        url = in.readString();
        id = in.readString();
        location = in.readString();
        byte tmpVeg = in.readByte();
        veg = tmpVeg == 0 ? null : tmpVeg == 1;
        byte tmpNon_veg = in.readByte();
        non_veg = tmpNon_veg == 0 ? null : tmpNon_veg == 1;
        rating = in.readParcelable(Rating.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(url);
        dest.writeString(id);
        dest.writeString(location);
        dest.writeByte((byte) (veg ==null ?  0 : veg ? 1 : 2));
        dest.writeByte((byte) (non_veg ==null ?  0 : non_veg ? 1 : 2));
        dest.writeParcelable(rating, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Eatery> CREATOR = new Creator<Eatery>() {
        @Override
        public Eatery createFromParcel(Parcel in) {
            return new Eatery(in);
        }

        @Override
        public Eatery[] newArray(int size) {
            return new Eatery[size];
        }
    };

    //Getters and Setters

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return description;
    }

    public void setDesc(String desc) {
        this.description = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getVeg() {
        return veg;
    }

    public void setVeg(Boolean veg) {
        this.veg = veg;
    }

    public Boolean getNon_veg() {
        return non_veg;
    }

    public void setNon_veg(Boolean non_veg) {
        this.non_veg = non_veg;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

}


