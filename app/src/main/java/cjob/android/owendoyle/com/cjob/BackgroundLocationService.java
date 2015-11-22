package cjob.android.owendoyle.com.cjob;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import cjob.android.owendoyle.com.cjob.events.EventManager;

/**
 * Name: Owen Doyle
 * Student Number: 12453618
 * File: BackgroundLocationService.java
 *
 * This class is a background service that constantly polls for location updates.
 *
 * The basic layout of this code was based on the code described
 * in the git gist repository: https://gist.github.com/blackcj/20efe2ac885c7297a676.
 * I had to modify the code found in this gist to use the new googleApi location services
 */
public class BackgroundLocationService extends Service implements GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "BkgroundLocationService";

    IBinder mBinder = new Binder();
    private LocationRequest mLocationRequest;
    private GoogleApiClient mClient;
    private boolean mInProgress;
    private boolean serviceAvailable = false;
    private Context mContext  = this;
    private LatLng latLng;

    @Override
    public void onCreate() {
        super.onCreate();

        mInProgress = false;

        //set up the google api client
        mClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {
                Log.d(TAG,">>>>>>>>>>>>>>>>>>>>>>>> Service created <<<<<<<<<<<<<<<<<<<<<<<<<<<<");

                //set up a location request to continually listen for location updates
                mLocationRequest = LocationRequest.create();
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                mLocationRequest.setInterval(5000);
                mLocationRequest.setFastestInterval(5000);
                mLocationRequest.setSmallestDisplacement(1);
                LocationServices.FusedLocationApi.requestLocationUpdates(mClient, mLocationRequest, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.d(TAG, "Location changed to: "+location);
                        //send any new location to the event manager to check if any events are set for that location
                        EventManager em = new EventManager(mContext);
                        em.checkForEvents(location);
                    }
                });
            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        }).addApi(LocationServices.API).build();

        serviceAvailable = servicesAvailable();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Service Started <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

        if (intent != null){
            latLng = new LatLng(intent.getDoubleExtra(MapFragment.EXTRA_LATITUDE, 0), intent.getDoubleExtra(MapFragment.EXTRA_LONGITUDE, 0));
        }

        if (!serviceAvailable || mClient.isConnected() || mInProgress)return START_STICKY;

        if (!mClient.isConnected() || mClient.isConnecting() && mInProgress){
            mInProgress = true;
            mClient.connect();
        }
        return START_STICKY;
    }

    private boolean servicesAvailable(){
        //check if google play services are available on the device
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == resultCode) return true;
        else return false;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Service Destroyed <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        mInProgress = false;
        if (serviceAvailable && mClient != null){
            mClient.disconnect();
            mClient = null;
        }
        super.onDestroy();
    }
}
