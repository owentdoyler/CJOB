/*
* The event manager keeps track of all the events and
 * performs events when they are triggered. It also provides
 * methods for querying the database.
* */

package cjob.android.owendoyle.com.cjob.events;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cjob.android.owendoyle.com.cjob.BackgroundLocationService;
import cjob.android.owendoyle.com.cjob.MapActivity;
import cjob.android.owendoyle.com.cjob.MapFragment;
import cjob.android.owendoyle.com.cjob.database.EventCursorWrapper;
import cjob.android.owendoyle.com.cjob.database.EventsDatabaseHelper;
import cjob.android.owendoyle.com.cjob.database.EventsDbSchema.EventsTable;
import cjob.android.owendoyle.com.cjob.R;

public class EventManager {
    private static final String TAG = "EventManager";

    //types of events
    public static final int ALARM = 1;
    public static final int SMS = 2;
    public static final int NOTIFICATION = 3;
    public static final int EMAIL = 4;

    private static final String SENT = "SMS_SENT";
    private static final String DELIVERED = "SMS_DELIVERED";
    public static final String EXTRA_REFRESH = "com.refresh";

    String curr = "";

    private SQLiteDatabase mDataBase;
    private Context mContext;

    public EventManager(Context context){
        mContext = context;

        //get reference to the database
        mDataBase = new EventsDatabaseHelper(mContext.getApplicationContext()).getWritableDatabase();
    }

    //this method creates the content values object that is used to add data to the database
    private static ContentValues getContentValues(Event event){
        ContentValues values = new ContentValues();
        values.put(EventsTable.Cols.LAT,event.getLatitude().toString());
        values.put(EventsTable.Cols.LONG,event.getLongitude().toString());
        values.put(EventsTable.Cols.RADIUS,Integer.toString(event.getRadius()));
        values.put(EventsTable.Cols.ADDRESS,event.getAddress());
        values.put(EventsTable.Cols.TITLE,event.getTitle());
        values.put(EventsTable.Cols.EVENT_TYPE,event.getType());
        values.put(EventsTable.Cols.EVENT_TITLE,event.getTitle());
        values.put(EventsTable.Cols.DELETE_ON_COMPLETE,event.getDeleteOnComplete());
        values.put(EventsTable.Cols.EVENT_TEXT,event.getText());
        values.put(EventsTable.Cols.TITLE,event.getTitle());
        values.put(EventsTable.Cols.CONTACT,event.getContact());
        values.put(EventsTable.Cols.CONTACT_NUMBER,event.getContactNumber());
        values.put(EventsTable.Cols.EMAIL_ADDRESS,event.getEmail());
        values.put(EventsTable.Cols.EMAIL_SUBJECT,event.getEmailSubject());
        values.put(EventsTable.Cols.USER_EMAIL,event.getUserEmail());
        values.put(EventsTable.Cols.USER_PASSWORD,event.getUserPassword());
        values.put(EventsTable.Cols.ACTIVE,event.getActive());
        return values;
    }

    //adds an event to the database
    public void addEvent(Event event){
        ContentValues values = getContentValues(event);
        mDataBase.insert(EventsTable.NAME, null, values);
    }

    //delets an event from the database
    public void deleteEvent(Event event){
        int eventId = event.getId();
        mDataBase.delete(EventsTable.NAME, "_id = ?", new String[]{Integer.toString(eventId)});
        EventCursorWrapper cursor = queryEvents(null, null);
        if (cursor.getCount() == 0){
            Intent i = new Intent(mContext, BackgroundLocationService.class);
            mContext.stopService(i);
        }
        cursor.close();
    }

    //updates an event in the database
    public void updateEvent(Event event) {
        int eventId = event.getId();
        ContentValues values = getContentValues(event);
        mDataBase.update(EventsTable.NAME, values, "_id = ?",
                new String[]{Integer.toString(eventId)});
    }

    //switch the event to either active or inactive
    public void toggleActive(Event event){
        int eventId = event.getId();
        int active = event.getActive();
        if(active == 1){
            event.setActive(0);
        }
        else if(active == 0) {
            event.setActive(1);
        }

        ContentValues values = getContentValues(event);
        mDataBase.update(EventsTable.NAME, values, "_id = ?",
                new String[]{Integer.toString(eventId)});
    }

    //logs all the events in the database
    public void logEvents(){
        EventCursorWrapper cursor = queryEvents(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Log.d(TAG,"Current Database: "+cursor.getEventDetails().toString());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
    }

    //checks if the distance between two coordinates is <= the given radius
    public double checkdistance(double lat1, double lng1, double lat2, double lng2, double radius){
        double p = 0.017453292519943295;
        double a = 0.5 - Math.cos((lat2 - lat1) * p)/2 +
                Math.cos(lat1 * p) * Math.cos(lat2 * p) *
                        (1 - Math.cos((lng2 - lng1) * p))/2;

        double distance = 12742 * Math.asin(Math.sqrt(a));
        Log.d(TAG, "ID: " + curr + " Distance = " + distance * 1000 + " Radius = " + radius);
        return distance*1000;
    }

    //check your location is within any of the event radius'
    public void checkForEvents(Location location){
        EventCursorWrapper cursor = queryEvents(null,null);
        //check if any events were found
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){

                Event event = cursor.getEventDetails();
                curr = event.getTitle();
                double radius = (double) event.getRadius();
                double currentDistance = checkdistance(location.getLatitude(),location.getLongitude(),event.getLatitude(),event.getLongitude(),radius);

                //if inside event radius
                if(currentDistance <= radius) {

                    //if the event is active
                    if(event.getActive() == 1) {
                        toggleActive(event);
                        performEvent(event);
                    }
                }   //if outside the radius and the event is not active
                else if(event.getActive() == 0){
                    toggleActive(event);
                }
                cursor.moveToNext();
            }
            cursor.close();
            return;
        }
        else {
            cursor.close();
            return;
        }
    }

    //executes the given event
    private void performEvent(Event event){
        switch (Integer.parseInt(event.getType())){
            case NOTIFICATION:
                sendNotification(event);
                if(event.getDeleteOnComplete() == 1){
                    deleteEvent(event);
                }
                return;
            case SMS:
                sendSms(event);
                if(event.getDeleteOnComplete() == 1){
                    deleteEvent(event);
                }
                return;
            case EMAIL:
                new SendEmail().execute(event);
                if(event.getDeleteOnComplete() == 1){
                    deleteEvent(event);
                }
                return;
            case ALARM:
                setOffAlarm();
                if(event.getDeleteOnComplete() == 1){
                    deleteEvent(event);
                }
                return;
            default:
                return;
        }
    }

    //This method returns a list of event objects.
    public List<Event> getEventList(){
        List<Event> eventList = new ArrayList<Event>();
        EventCursorWrapper eventCursorWrapper = queryEvents(null, null);
        try{
            eventCursorWrapper.moveToFirst();
            while(!eventCursorWrapper.isAfterLast()){
                eventList.add(eventCursorWrapper.getEventDetails());
                eventCursorWrapper.moveToNext();
            }
        }finally {
            eventCursorWrapper.close();
        }
        return eventList;
    }

    //This method constructs and a query and then queries the database
    public EventCursorWrapper queryEvents(String whereClause, String[] whereArgs){
        Cursor cursor = mDataBase.query(
                EventsTable.NAME,
                null, //select all columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having
                null //orderBy
        );
        return new EventCursorWrapper(cursor);
    }

    //construct the arguments for the where clause based on the given location
    private String[] getWhereArgs(Location location){
        String[] whereArgs = new String[]{
                ""+location.getLatitude(),
                ""+location.getLongitude()
        };
        return whereArgs;
    }

    //sets up a notification
    private void sendNotification(Event event){
        Uri path =  Uri.parse("android.resource://" + "cjob.android.owendoyle.com.cjob" + "/" + R.raw.notification1);
        String text = event.getText();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.mipmap.ic_name_small)
                .setContentTitle(event.getTitle())
                .setContentText(text);


        NotificationManager notifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notifyManager.notify(001, builder.build());
        try {
            Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(mContext, notification);
            vibrator.vibrate(new long[]{1000,1000},-1);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendSms(Event event){
        PendingIntent sentPI = PendingIntent.getBroadcast(mContext, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(mContext, 0,
                new Intent(DELIVERED), 0);

        mContext.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(mContext, "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(mContext, "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(mContext, "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(mContext, "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(mContext, "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //when the SMS has been delivered
        mContext.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(mContext, "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "SMS SENT");

                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(mContext, "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(event.getContactNumber(), null, event.getText(), sentPI, deliveredPI);
    }


    private void setOffAlarm(){
        Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);

        vibrator.vibrate(new long[]{1000, 1000, 1000, 1000}, -1);
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            Ringtone r = RingtoneManager.getRingtone(mContext, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

