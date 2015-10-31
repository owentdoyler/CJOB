package cjob.android.owendoyle.com.cjob;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by Owner on 30/10/2015.
 */
public class SettingsActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context packageContext,double latitude, double longitude, String address, int event_type ) {
        Intent intent = new Intent(packageContext, SettingsActivity.class);
        intent.putExtra(MapFragment.EXTRA_LATITUDE, latitude);
        intent.putExtra(MapFragment.EXTRA_LONGITUDE, longitude);
        intent.putExtra(MapFragment.EXTRA_ADDRESS, address);
        intent.putExtra(EventTypeFragment.EXTRA_EVENT_TYPE, event_type);
        return intent;
    }

    @Override
    protected Fragment createFragment() {

        double defaultValue = 0.0;
        double latitude = (double) getIntent().getDoubleExtra(MapFragment.EXTRA_LATITUDE,defaultValue);
        double longitude = (double) getIntent().getDoubleExtra(MapFragment.EXTRA_LONGITUDE, defaultValue);
        String address = (String) getIntent().getStringExtra(MapFragment.EXTRA_ADDRESS);
        int event_type = (int) getIntent().getIntExtra(EventTypeFragment.EXTRA_EVENT_TYPE,0);
        return SettingsFragment.newInstance(latitude, longitude, address,event_type);
    }
}
