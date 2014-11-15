package com.example.root.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public  class ForecastFragment extends Fragment {

    public ForecastFragment() {
    }

    ArrayAdapter<String> forecastAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public void onStart(){
        super.onStart();
        updateWeather();

    }

    public void updateWeather(){
        String location = Utility.getPreferredLocation(getActivity());
        FetchWeatherTask fetchWeatherTask = new FetchWeatherTask(getActivity(),forecastAdapter);
        fetchWeatherTask.execute(location);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        forecastAdapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview,new ArrayList<String>()
        );
        ListView listView = (ListView)rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(forecastAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),DetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT,forecastAdapter.getItem(position));
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();

        if (R.id.action_refresh == id){
            updateWeather();
            return  true;
        }

        return super.onOptionsItemSelected(item);
    }

}