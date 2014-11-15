package com.example.root.sunshine;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.root.sunshine.data.WeatherContract;


public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{



    private String mLocation;
    private String forecastShare;

    private static final String LOCATION_KEY = "location";
    private final static String LOG_TAG = "SUNSHINE";

    private static final int DETAIL_LOADER = 0;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG,"HERE1");
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mLocation = savedInstanceState.getString(LOCATION_KEY);
        }
        getLoaderManager().initLoader(DETAIL_LOADER,null,this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(LOCATION_KEY, mLocation);
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (mLocation != null &&
                !mLocation.equals(Utility.getPreferredLocation(getActivity()))) {
            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        }
    }


/*    @Override
    public boolean onCreateOptionsMenu(Menu menu,MenuInflater inflater)) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        MenuItem item  = menu.findItem(R.id.menu_item_share);
        shareActionProvider = (ShareActionProvider)item.getActionProvider();
        String shareText = findViewById();
        return true;
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.d(LOG_TAG,"HERE2");
        inflater.inflate(R.menu.detailfragment, menu);
        MenuItem item  = menu.findItem(R.id.menu_item_share);
        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        String shareText = forecastShare + "#SunshineApp";
        Intent shareIntent = ShareCompat.IntentBuilder.from(this.getActivity())
                .setType("text/plain").setText(shareText).getIntent();
        if (shareActionProvider != null) {
            shareActionProvider.setShareIntent(shareIntent);
        }else{
            Log.d(LOG_TAG,"action provider null");
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATETEXT + " ASC";
        mLocation = Utility.getPreferredLocation(getActivity());
        String date =  this.getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
                mLocation, date);
          String[] columns= {

                WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
                WeatherContract.WeatherEntry.COLUMN_DATETEXT,
                WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
                WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
                WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
                WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING
        };

        return new CursorLoader(
                getActivity(),
                weatherForLocationUri,
                columns,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (!cursor.moveToFirst()) { return; }
        boolean isMetric = Utility.isMetric(getActivity());
        String date = cursor.getString(
                cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATETEXT)
        );
        date = Utility.formatDate(date);
        ((TextView)getView().findViewById(R.id.detail_date_textview)).setText(date);
        String desc = cursor.getString(
                cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC)
        );
        ((TextView)getView().findViewById(R.id.detail_forecast_textview)).setText(desc);
        String minTemp = Utility.formatTemperature(
                cursor.getDouble(
                        cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP)
                ), isMetric);
        String maxTemp = Utility.formatTemperature(
                cursor.getDouble(
                        cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP)
                ), isMetric);
        ((TextView)getView().findViewById(R.id.detail_low_textview)).setText(minTemp);
        ((TextView)getView().findViewById(R.id.detail_high_textview)).setText(maxTemp);
        forecastShare = String.format("%s - %s - %s/%s", date, desc, maxTemp, minTemp);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
}
