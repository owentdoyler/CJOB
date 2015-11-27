/*
* A basic activity that hosts the map fragment
* */

package cjob.android.owendoyle.com.cjob;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;


public class MapActivity extends SingleFragmentActivity {
    private static final int REQUESR_ERROR = 0;

    @Override
    protected Fragment createFragment() {
        return new MapFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check if the google play services are available
        int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (errorCode != ConnectionResult.SUCCESS){
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode, this, REQUESR_ERROR, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });
            errorDialog.show();
        }
    }
}
