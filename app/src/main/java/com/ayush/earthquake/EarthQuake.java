package com.ayush.earthquake;

/*
 * This EarthQuake class contains all those attributes which are needed in our application , these will be received from JSON response
 * from the USGS website.
 * we need basically four parameters , which we are showing in our application they are as follows
 * mMagnitude - it signifies the magnitude of the earthquake
 * mPlace - it signifies the location of the earthquake
 * mTimeInMilliseconds - it signifies the time at which earthquake occured
 * url - it is the link for the usgs website for the particular earthquake
 */

public class EarthQuake {
    private Double mMAngnitude;
    private String mPlace;
    private Long mTimeInMilliseconds;
    String url;

    public EarthQuake(Double mMAngnitude, String mPlace, Long mTimeInMilliseconds,String url) {
        this.mMAngnitude = mMAngnitude;
        this.mPlace = mPlace;
        this.mTimeInMilliseconds = mTimeInMilliseconds;
        this.url = url;
    }

    public Double getmMAngnitude() {
        return mMAngnitude;
    }

    public String getmPlace() {
        return mPlace;
    }

    public Long getmTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

    public String getUrl() {
        return url;
    }
}
