/*
* A simple activity that hosts the HomeScreenFragment
* */

package cjob.android.owendoyle.com.cjob;

import android.support.v4.app.Fragment;

/**
 * Created by Owner on 20/11/2015.
 */
public class HomeScreenActivity extends SingleFragmentActivity {

    //creats the fragment for this activity
    @Override
    protected Fragment createFragment() {
        return new HomeScreenFragment();
    }
}
