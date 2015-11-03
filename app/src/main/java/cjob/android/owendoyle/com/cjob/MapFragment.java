package cjob.android.owendoyle.com.cjob;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

/**
 * Name: Owen Doyle
 * Student Number: 12453618
 * File: MapFragment.java
 */
public class MapFragment extends SupportMapFragment {
    private static final String TAG = "MapFragment";

    public static final String EXTRA_LATITUDE = "com.latitude"; //TODO change this string
    public static final String EXTRA_LONGITUDE = "com.longitude"; //TODO change this string

    private HashMap<Marker, Boolean> mMarkerEvents = new HashMap<>();
    private GoogleApiClient mClient;
    private GoogleMap mMap;
    private Location mLastLocation;
    private Marker mSelectedMarker = null;
    private SQLiteDatabase mDataBase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        setupClient();
        setUpMap();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        //if a marker is selected and it has an event show this options menu
        if(markerSelected() && mMarkerEvents.get(mSelectedMarker)){
            inflater.inflate(R.menu.menu_marker_event, menu);
        }
        //else if a marker is selected and it has no event show this options menu
        else if (markerSelected() && !mMarkerEvents.get(mSelectedMarker)){
            inflater.inflate(R.menu.menu_no_marker_event, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete_marker:
                mSelectedMarker.remove();
                mMarkerEvents.remove(mSelectedMarker);
                mSelectedMarker = null;
                getActivity().invalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //This method sets up the googleApiClient and gets the last known location
    private void setupClient(){
        mClient  = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mClient);
                        updateMap();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addApi(LocationServices.API)
                .build();
    }

    // This method instantiates the map and all listeners on the map
    private void setUpMap(){
        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        Marker newMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Click the + to add an event")); //TODO add place address/event details
                        //store a reference to the new marker
                        mMarkerEvents.put(newMarker, false);

                    }
                });
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        //no marker selected anymore
                        mSelectedMarker = null;
                        //update the options menu
                        getActivity().invalidateOptionsMenu();
                    }
                });
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        //get reference to the marker that was selected
                        mSelectedMarker = marker;
                        //update the options menu
                        getActivity().invalidateOptionsMenu();
                        return false;
                    }
                });
            }
        });
    }

    //This method updated the map to focus on the last known location
    private void updateMap(){
        if (mMap == null){
            return;
        }

        LatLng myLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        //zoom in on current location
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(myLocation, 10, 1, 1)));
    }

    private boolean markerSelected(){
        return mSelectedMarker != null;
    }

    @Override
    public void onStart() {
        super.onStart();
        mClient.connect();
    }

    @Override
    public void onStop() {
        mClient.disconnect();
        super.onStop();
    }
}
