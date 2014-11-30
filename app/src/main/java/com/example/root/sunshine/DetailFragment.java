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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.root.sunshine.data.WeatherContract;

import static com.example.root.sunshine.data.WeatherContract.WeatherEntry;


public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{



    private String mLocation;
    private String forecastShare;

    private static final String LOCATION_KEY = "location";
    private final static String LOG_TAG = "SUNSHINE";

    private static final int DETAIL_LOADER = 0;


    private TextView dateTextView;
    private TextView dayTextView;
    private TextView maxTempTextView;
    private TextView minTempTextView;
    private ImageView weatherImageView;
    private TextView humidityTextView;
    private TextView windSpeedTextView;
    private TextView pressureTextView;
    private TextView infoTextView;
    private String dateString ;
    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG,"HERE1");
        Bundle arguments = getArguments();
        if (arguments != null) {
            dateString = arguments.getString(DetailActivity.DATE_KEY);
        }
        View rootView = inflater.inflate(R.layout.fragment2_detail, container, false);
        dateTextView = (TextView)rootView.findViewById(R.id.detail_date_textview);
        dayTextView = (TextView)rootView.findViewById(R.id.detail_day_textview);
        maxTempTextView = (TextView)rootView.findViewById(R.id.detail_high_temp_textview);
        minTempTextView = (TextView)rootView.findViewById(R.id.detaiL_low_temp_textview);
        weatherImageView = (ImageView)rootView.findViewById(R.id.detail_weather_imageview);
        humidityTextView = (TextView)rootView.findViewById(R.id.detail_humidity_textview);
        windSpeedTextView = (TextView)rootView.findViewById(R.id.detail_wind_textview);
        pressureTextView = (TextView)rootView.findViewById(R.id.detail_pressure_textview);
        infoTextView = (TextView)rootView.findViewById(R.id.detail_info_textview);

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
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(DetailActivity.DATE_KEY)  && mLocation != null &&
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
        String sortOrder = WeatherEntry.COLUMN_DATETEXT + " ASC";
        mLocation = Utility.getPreferredLocation(getActivity());
        Uri weatherForLocationUri = WeatherEntry.buildWeatherLocationWithDate(
                mLocation, dateString);
          String[] columns= {

                WeatherEntry.TABLE_NAME + "." + WeatherEntry._ID,
                WeatherEntry.COLUMN_DATETEXT,
                WeatherEntry.COLUMN_SHORT_DESC,
                WeatherEntry.COLUMN_MAX_TEMP,
                WeatherEntry.COLUMN_MIN_TEMP,
                WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING,
                WeatherEntry.COLUMN_HUMIDITY,
                WeatherEntry.COLUMN_PRESSURE,
                WeatherEntry.COLUMN_WIND_SPEED,
                  WeatherEntry.COLUMN_DEGREES,
                  WeatherEntry.COLUMN_WEATHER_ID

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
                cursor.getColumnIndex(WeatherEntry.COLUMN_DATETEXT)
        );
        dayTextView.setText(Utility.getFriendlyDayString(getActivity(),date));
        date = Utility.formatDate(date);

        dateTextView.setText(date);
        String desc = cursor.getString(
                cursor.getColumnIndex(WeatherEntry.COLUMN_SHORT_DESC)
        );
        infoTextView.setText(desc);
        String minTemp = Utility.formatTemperature(
                cursor.getDouble(
                        cursor.getColumnIndex(WeatherEntry.COLUMN_MIN_TEMP)
                ), isMetric);
        String maxTemp = Utility.formatTemperature(
                cursor.getDouble(
                        cursor.getColumnIndex(WeatherEntry.COLUMN_MAX_TEMP)
                ), isMetric);
        minTempTextView.setText(minTemp);
        maxTempTextView.setText(maxTemp);
        Integer state = cursor.getInt(cursor.getColumnIndex(WeatherEntry.COLUMN_WEATHER_ID));
        weatherImageView.setImageResource(Utility.getArtResourceForWeatherCondition(state));
        float humidity =  cursor.getFloat(
                cursor.getColumnIndex(WeatherEntry.COLUMN_HUMIDITY)
        );
        humidityTextView.setText(getActivity().getString(R.string.format_humidity, humidity));
        float windSpeedStr = cursor.getFloat(cursor.getColumnIndex(WeatherEntry.COLUMN_WIND_SPEED));
        float windDirStr = cursor.getFloat(cursor.getColumnIndex(WeatherEntry.COLUMN_DEGREES));
        windSpeedTextView.setText(Utility.getFormattedWind(getActivity(), windSpeedStr, windDirStr));
        float pressure = cursor.getFloat(cursor.getColumnIndex(WeatherEntry.COLUMN_PRESSURE));
        pressureTextView.setText(getActivity().getString(R.string.format_pressure, pressure));
        forecastShare = String.format("%s - %s - %s/%s", date, desc, maxTemp, minTemp);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
}
