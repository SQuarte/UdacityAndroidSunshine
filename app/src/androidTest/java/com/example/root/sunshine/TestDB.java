package com.example.root.sunshine;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.root.sunshine.data.WeatherDbHelper;

import static com.example.root.sunshine.data.WeatherContract.LocationEntry;
import static com.example.root.sunshine.data.WeatherContract.WeatherEntry;

/**
 * Created by root on 07.11.14.
 */
public class TestDB extends AndroidTestCase {

    public static final String LOG_TAG = TestDB.class.getSimpleName();

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(WeatherDbHelper.DB_NAME);
        SQLiteDatabase db = new WeatherDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }
    public void testInsertReadDb() {
        String testLocationSetting = "99705";
        String testCityName = "North Pole";
        double testLatitude = 64.7488;
        double testLongitude = -147.353;
        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LocationEntry.COLUMN_LOCATION_SETTING, testLocationSetting);
        values.put(LocationEntry.COLUMN_CITY_NAME, testCityName);
        values.put(LocationEntry.COLUMN_COORD_LAT, testLatitude);
        values.put(LocationEntry.COLUMN_COORD_LONG, testLongitude);
        long locationRowId;
        locationRowId = db.insert(LocationEntry.TABLE_NAME, null, values);

        assertTrue(locationRowId != -1);
        Log.d(LOG_TAG, "New row id: " + locationRowId);

        String[] columns = {
                LocationEntry._ID,
                LocationEntry.COLUMN_LOCATION_SETTING,
                LocationEntry.COLUMN_CITY_NAME,
                LocationEntry.COLUMN_COORD_LAT,
               LocationEntry.COLUMN_COORD_LONG
        };

        Cursor cursor = db.query(
                LocationEntry.TABLE_NAME,
                columns,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        if (cursor.moveToFirst()) {

            int locationIndex = cursor.getColumnIndex(LocationEntry.COLUMN_LOCATION_SETTING);
            String location = cursor.getString(locationIndex);
            int nameIndex = cursor.getColumnIndex((LocationEntry.COLUMN_CITY_NAME));
            String name = cursor.getString(nameIndex);
            int latIndex = cursor.getColumnIndex((LocationEntry.COLUMN_COORD_LAT));
            double latitude = cursor.getDouble(latIndex);
            int longIndex = cursor.getColumnIndex((LocationEntry.COLUMN_COORD_LONG));
            double longitude = cursor.getDouble(longIndex);
            assertEquals(testCityName, name);
            assertEquals(testLocationSetting, location);
            assertEquals(testLatitude, latitude);
            assertEquals(testLongitude, longitude);
        } else {
            fail("No values returned :(");
        }
        ContentValues weatherValues = new ContentValues();
        weatherValues.put(WeatherEntry.COLUMN_LOC_KEY, locationRowId);
        weatherValues.put(WeatherEntry.COLUMN_DATETEXT, "20141205");
        weatherValues.put(WeatherEntry.COLUMN_DEGREES, 1.1);
        weatherValues.put(WeatherEntry.COLUMN_HUMIDITY, 1.2);
        weatherValues.put(WeatherEntry.COLUMN_PRESSURE, 1.3);
        weatherValues.put(WeatherEntry.COLUMN_MAX_TEMP, 75);
        weatherValues.put(WeatherEntry.COLUMN_MIN_TEMP, 65);
        weatherValues.put(WeatherEntry.COLUMN_SHORT_DESC, "Asteroids");
        weatherValues.put(WeatherEntry.COLUMN_WIND_SPEED, 5.5);
        weatherValues.put(WeatherEntry.COLUMN_WEATHER_ID, 321);

        long weatherRowID = db.insert(WeatherEntry.TABLE_NAME, null, weatherValues);
        assertTrue(weatherRowID != -1);
        cursor = db.query(
                WeatherEntry.TABLE_NAME,
                null,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );


        if (cursor.moveToFirst()) {

            int locationIndex = cursor.getColumnIndex(WeatherEntry.COLUMN_LOC_KEY);
            long location = cursor.getLong(locationIndex);

            int datatextIndex = cursor.getColumnIndex(WeatherEntry.COLUMN_DATETEXT);
            String datatext = cursor.getString(datatextIndex);

            int  degreesIndex = cursor.getColumnIndex((WeatherEntry.COLUMN_DEGREES));
            double degrees = cursor.getDouble(degreesIndex);

            int  humidityIndex = cursor.getColumnIndex((WeatherEntry.COLUMN_HUMIDITY));
            double humidity = cursor.getDouble(humidityIndex);

            int  pressureIndex = cursor.getColumnIndex((WeatherEntry.COLUMN_PRESSURE));
            double pressure = cursor.getDouble(pressureIndex);

            int  maxTempIndex = cursor.getColumnIndex((WeatherEntry.COLUMN_MAX_TEMP));
            int maxTemp = cursor.getInt(maxTempIndex);

            int  minTempIndex = cursor.getColumnIndex((WeatherEntry.COLUMN_MIN_TEMP));
            int minTemp = cursor.getInt(minTempIndex);

            int descIndex = cursor.getColumnIndex(WeatherEntry.COLUMN_SHORT_DESC);
            String desc = cursor.getString(descIndex);

            int  windSpeedIndex = cursor.getColumnIndex((WeatherEntry.COLUMN_WIND_SPEED));
            double windSpeed = cursor.getDouble(windSpeedIndex);

            int  weatherIDIndex = cursor.getColumnIndex((WeatherEntry.COLUMN_WEATHER_ID));
            int weatherID = cursor.getInt(weatherIDIndex);

            assertEquals(location,locationRowId);
            assertEquals(datatext,"20141205");
            assertEquals(degrees,1.1);
            assertEquals(humidity,1.2);
            assertEquals(pressure,1.3);
            assertEquals(maxTemp,75);
            assertEquals(minTemp,65);
            assertEquals(desc,"Asteroids");
            assertEquals(windSpeed,5.5);
            assertEquals(weatherID,321);

        } else {
            fail("No values returned :(");
        }
        dbHelper.close();
    }
}
