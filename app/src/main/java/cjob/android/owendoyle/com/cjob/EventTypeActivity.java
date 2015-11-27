/*
* A simple activity that hosts the EventTypeFragment
* */
package cjob.android.owendoyle.com.cjob;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by Owner on 28/10/2015.
 */
//extends the abstract SingleFragmentActivity
public class EventTypeActivity extends SingleFragmentActivity {

    // makes it easier to put extras on intents the launch this activity
    public static Intent newIntent(Context packageContext,double latitude, double longitude, String address ) {
        Intent intent = new Intent(packageContext, EventTypeActivity.class);
        intent.putExtra(MapFragment.EXTRA_LATITUDE, latitude);
        intent.putExtra(MapFragment.EXTRA_LONGITUDE, longitude);
        intent.putExtra(MapFragment.EXTRA_ADDRESS, address);
        return intent;
    }

    //creates the fragment for this activity with arguments
    @Override
    protected Fragment createFragment() {
        double defaultValue = 0.0;
        double latitude = (double) getIntent().getDoubleExtra(MapFragment.EXTRA_LATITUDE,defaultValue);
        double longitude = (double) getIntent().getDoubleExtra(MapFragment.EXTRA_LONGITUDE,defaultValue);
        String address = (String) getIntent().getStringExtra(MapFragment.EXTRA_ADDRESS);
        return EventTypeFragment.newInstance(latitude,longitude,address);
    }
}
