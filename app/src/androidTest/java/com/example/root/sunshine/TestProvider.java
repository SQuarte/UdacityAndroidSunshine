package com.example.root.sunshine;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.root.sunshine.data.WeatherDbHelper;

import static com.example.root.sunshine.data.WeatherContract.LocationEntry;
import static com.example.root.sunshine.data.WeatherContract.WeatherEntry;


public class TestProvider extends AndroidTestCase {
    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    public void testDeleteDb() throws Throwable {
        mContext.deleteDatabase(WeatherDbHelper.DB_NAME);
    }

    public void testInsertReadDb() {


        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestDB.createNorthPoleLocationValues();

        long locationRowId;
        locationRowId = db.insert(LocationEntry.TABLE_NAME, null, testValues);

        assertTrue(locationRowId != -1);
        Log.d(LOG_TAG, "New row id: " + locationRowId);

        Cursor cursor = db.query(
                LocationEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null, // columns to filter by row groups
                null // sort order
        );

        TestDB.validateCursor(cursor, testValues);

// Fantastic. Now that we have a location, add some weather!
        ContentValues weatherValues = TestDB.createWeatherValues(locationRowId);

        long weatherRowId = db.insert(WeatherEntry.TABLE_NAME, null, weatherValues);
        assertTrue(weatherRowId != -1);

// A cursor is your primary interface to the query results.
        Cursor weatherCursor = db.query(
                WeatherEntry.TABLE_NAME, // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        TestDB.validateCursor(weatherCursor, weatherValues);

        dbHelper.close();
    }
}
