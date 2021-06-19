package com.ayush.earthquake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
/*
This is the  main class where we are populating the list with the earthquake data
here we have created a custom list view and therefore we need to use a custom adapter as well.
 */
public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<List<EarthQuake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query";;
    private EarthQuakeAdapter adapter;

    private static final int EARTHQUAKE_LOADER_ID = 1;

    private TextView mEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        /*
        this text view will used to show the appropriate message when linking of earthquake data fails.
         */
        mEmptyTextView = findViewById(R.id.empty_view);


        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        adapter = new EarthQuakeAdapter(EarthquakeActivity.this,new ArrayList<EarthQuake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);

        //if list will be empty for some reasons then content of this text view will be shown
        earthquakeListView.setEmptyView(mEmptyTextView);

        /*
        to give better user experience we have integrated the on click event on list item,
        on the list of earthquakes when user will click any of them it will be directed to the webpage of
        USGS website for detailed description of the earthquake.
         */
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EarthQuake earthQuake = adapter.getItem(position);

                /*
                   Here implicit intent is used to open the webpage
                 */
                  String url = earthQuake.getUrl();
                  Uri webpage = Uri.parse(url);
                  Intent intent = new Intent(Intent.ACTION_VIEW,webpage);
                  if(intent.resolveActivity(getPackageManager()) != null){
                    startActivity(intent);
                  }
            }
        });

         // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // If there is a network connection, fetch data
        if(networkInfo != null && networkInfo.isConnected()){
            // Reference Of Loaders
            android.app.LoaderManager loaderManager = getLoaderManager();
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID,null,EarthquakeActivity.this);
        }
        else{
           // Display Error
           // Hide The Loading Bar
           View loadingIndicator = findViewById(R.id.loading_indicator);
           loadingIndicator.setVisibility(View.GONE);
           // Update the empty state
            mEmptyTextView.setText(R.string.no_internet_connection);
        }
    }

    @NonNull
    @Override
    public Loader<List<EarthQuake>> onCreateLoader(int id, Bundle bundle) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        /*
        saving the data with the help of shared preferences which stores the information in key value pair
         */
        String minMagnitude = sharedPreferences.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default)
        );
        String orderBy = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        // specifying the query

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format","geojson");
        uriBuilder.appendQueryParameter("limit","12");
        uriBuilder.appendQueryParameter("minmag",minMagnitude);
        uriBuilder.appendQueryParameter("orderby",orderBy);

        return new EarthQuakeLoader(EarthquakeActivity.this,uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<EarthQuake>> loader, List<EarthQuake> data) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        // clear previous data
        adapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.

        if(data != null && !data.isEmpty()){
            adapter.addAll(data);
        }

        // if list is not updated , this message will be shown
        mEmptyTextView.setText(R.string.no_earthquakes);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<EarthQuake>> loader) {
          adapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    /*
    this method basically opens settings activity where user can choose the magnitude range and sorting  order
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       int id = item.getItemId();

       if(id == R.id.action_settings){
           Intent settingsIntent = new Intent(EarthquakeActivity.this,SettingsActivity.class);
           startActivity(settingsIntent);
           return true;
       }
       return super.onOptionsItemSelected(item);
    }
}