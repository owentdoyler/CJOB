package cjob.android.owendoyle.com.cjob;

import android.support.v4.app.Fragment;

/**
 * Created by Owner on 20/11/2015.
 */
public class HomeScreenActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new HomeScreenFragment();
    }
}
