package com.ayush.earthquake;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;
/*
 we do not want that main thread will be crowded in our application, if it is done then  our app become slow and crashes
 therefore we wish to use other threads, and from here loaders come into picture we can use loader to perform various task in background
 relieving the main thread.
 in our case we are using the loader to make the request from USGS website
 */

public class EarthQuakeLoader extends AsyncTaskLoader<List<EarthQuake>> {

    private static final String LOG_TAG = EarthQuakeLoader.class.getName();

    private String mUrl;

    public EarthQuakeLoader(Context context,String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<EarthQuake> loadInBackground() {
       if(mUrl == null){
           return null;
       }
       List<EarthQuake> earthQuakes = QueryUtils.fetchEarthQuakeData(mUrl);
       return earthQuakes;
    }
}
