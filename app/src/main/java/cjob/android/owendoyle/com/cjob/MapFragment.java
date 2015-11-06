package cjob.android.owendoyle.com.cjob;

import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cjob.android.owendoyle.com.cjob.database.EventCursorWrapper;
import cjob.android.owendoyle.com.cjob.database.EventsDatabaseHelper;
import cjob.android.owendoyle.com.cjob.database.EventsDbSchema;
import cjob.android.owendoyle.com.cjob.events.Event;

/**
 * Name: Owen Doyle
 * Student Number: 12453618
 * File: MapFragment.java
 */
public class MapFragment extends SupportMapFragment {
    private static final String TAG = "MapFragment";

    public static final String EXTRA_LATITUDE = "com.latitude"; //TODO change this string
    public static final String EXTRA_LONGITUDE = "com.longitude"; //TODO change this string
    public static final String EXTRA_ADDRESS = "com.address";
    public static final String LOCATION_EXTRA = "com.location";
    public static final String RECEIVER = "com.receiver";

    private HashMap<Marker, Boolean> mMarkerEvents = new HashMap<>();
    private GoogleApiClient mClient;
    private GoogleMap mMap;
    private Location mLastLocation;
    private Marker mSelectedMarker = null;
    private SQLiteDatabase mDataBase;
    private static BufferedWriter out;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            createFileOnDevice(false);
        }catch (IOException e){
            e.printStackTrace();
        }
//        setupLocationListener();
        mDataBase = new EventsDatabaseHelper(getActivity().getApplicationContext()).getWritableDatabase();
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
            case R.id.action_add_event:
                Intent intent = EventTypeActivity.newIntent(getActivity(), mSelectedMarker.getPosition().latitude, mSelectedMarker.getPosition().longitude,"address");
                startActivity(intent);
//                Log.d(TAG, ""+Environment.getExternalStorageDirectory());
//                try {
//                    createFileOnDevice(true);
//                }catch (IOException e){
//                    e.printStackTrace();
//                }
//                writeToFile("Log");
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
                mMap.setMyLocationEnabled(true);
                loadEvents();
                mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        String address = getAddress(latLng);
                        Marker newMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(address)); //TODO add place address/event details
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
                        Intent i = new Intent(getActivity(), BackgroundLocationService.class);
//                        getActivity().stopService(i);
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
//        LocationServices.FusedLocationApi.removeLocationUpdates(mClient, mLocationListener);
        mClient.disconnect();
        super.onStop();
    }

    private void loadEvents(){
        EventCursorWrapper cursor = queryEvents(null, null);
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                Event event = cursor.getEventDetails();
                LatLng latLng = new LatLng(event.getLatitude(), event.getLongitude());
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(event.getTitle()));
                mMarkerEvents.put(marker, true);
                mMap.addCircle(new CircleOptions().center(latLng).radius(event.getRadius()).strokeColor(Color.BLUE));
                cursor.moveToNext();
            }
        }
    }

    private EventCursorWrapper queryEvents(String whereClause, String[] whereArgs){
        Cursor cursor = mDataBase.query(
                EventsDbSchema.EventsTable.NAME,
                null, //select all columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having
                null //orderBy
        );
        return new EventCursorWrapper(cursor);
    }

    private void createFileOnDevice(Boolean append)  throws IOException{
        File root = Environment.getExternalStorageDirectory();
        if(root.canWrite()){
            File logFile = new File(root, "CJOBlog.txt");
            FileWriter logWriter = new FileWriter(logFile, append);
            out = new BufferedWriter(logWriter);
        }
    }

    private void writeToFile(String message){
        try {
            out.write(message+"\n");
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private String getAddress(LatLng latLng){
        String address = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            Address address1 = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<>();
            if (addresses.size() > 0){
                for (int i = 0; i < address1.getMaxAddressLineIndex(); i++){
                    addressFragments.add(address1.getAddressLine(i));
                }
                address = TextUtils.join(System.getProperty("line.separator"), addressFragments);
            }
        }catch (IOException ioe){
            Log.e(TAG, "error", ioe);
        }
        return address;
    }
}
