package cjob.android.owendoyle.com.cjob;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import cjob.android.owendoyle.com.cjob.EventsDbSchema.EventsTable;

/**
 * Name: Owen Doyle
 * Student Number: 12453618
 * File: EventManager.java.
 *
 * This class manages the events that are set.
 */
public class EventManager {
    private static final String TAG = "EventManager";

    //types of events
    public static final String NOTIFICATION = "notification";
    public static final String ALARM = "alarmm";
    public static final String EMAIL = "email";
    public static final String SMS = "sms";

    //basic layout of a SQLite where clause
    private static final String WHERE_CLAUSE = EventsTable.Cols.LAT+ " =? AND " +EventsTable.Cols.LONG+ "=?";

    private SQLiteDatabase mDataBase;
    private Context mContext;

    public EventManager(Context context){
        mContext = context;
        //get reference to the database
        mDataBase = new EventsDatabaseHelper(mContext.getApplicationContext()).getWritableDatabase();
    }

    public void checkForEvents(Location location){
        //query the data base for any events that are set for the given location
        EventCursorWrapper cursor = queryEvents(WHERE_CLAUSE, getWhereArgs(location));

        //check if any events were found
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                Event event = cursor.getEventDetails();
                Log.d(TAG, event.toString());
                performEvent(event);
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

    private void performEvent(Event event){
        switch (event.getType()){
            case NOTIFICATION:
                sendNotification(event);
                return;
            //TODO add implementations for other event types
            default:
                return;
        }
    }

    //This method constructs and a query and then queries the database
    private EventCursorWrapper queryEvents(String whereClause, String[] whereArgs){
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
    private String[] getWhereArgs(Location location){                                       //TODO add code for dealing with location radius
        String[] whereArgs = new String[]{
            ""+location.getLatitude(),
            ""+location.getLongitude()
        };
        return whereArgs;
    }

    private void sendNotification(Event event){
        String text = event.getText();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.mipmap.ic_testicon)
                .setContentTitle("Woohoo it works")
                .setContentText(text);

        NotificationManager notifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notifyManager.notify(001, builder.build());
    }
}
