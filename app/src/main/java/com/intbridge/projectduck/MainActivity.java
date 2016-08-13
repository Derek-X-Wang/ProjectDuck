package com.intbridge.projectduck;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.intbridge.projectduck.controllers.MapsFragment;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapsFragment mapsFragment = new MapsFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_content, mapsFragment)
                .show(mapsFragment)
                .commit();
    }
}
