package com.example.root.sunshine;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
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


public class DetailFragment extends Fragment {


    private  TextView detailText;

    private final static String LOG_TAG = "SUNSHINE";

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG,"HERE1");
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        detailText = (TextView)rootView.findViewById(R.id.detail_text);
        detailText.setText(
                this.getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT)
        );
        return rootView;
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
        String shareText = detailText.getText().toString() + "#SunshineApp";
        Intent shareIntent = ShareCompat.IntentBuilder.from(this.getActivity())
                .setType("text/plain").setText(shareText).getIntent();
        if (shareActionProvider != null) {
            shareActionProvider.setShareIntent(shareIntent);
        }else{
            Log.d(LOG_TAG,"action provider null");
        }
    }
}
