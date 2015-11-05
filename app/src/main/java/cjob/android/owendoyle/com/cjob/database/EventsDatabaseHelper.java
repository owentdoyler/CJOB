package cjob.android.owendoyle.com.cjob.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Name: Owen Doyle
 * Student Number: 12453618
 * File: EventsDatabaseHelper.java
 *
 * This class manages refrences to the database
 */
public class EventsDatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "EventsDatabase.db";

    public EventsDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + EventsDbSchema.EventsTable.NAME + "("+
                "_id integer primary key autoincrement, " +
                EventsDbSchema.EventsTable.Cols.LAT + ", " +
                EventsDbSchema.EventsTable.Cols.LONG+ ", " +
                EventsDbSchema.EventsTable.Cols.TITLE+ ", "+
                EventsDbSchema.EventsTable.Cols.RADIUS+", " +
                EventsDbSchema.EventsTable.Cols.ADDRESS+", " +
                EventsDbSchema.EventsTable.Cols.EVENT_TYPE+", " +
                EventsDbSchema.EventsTable.Cols.EVENT_TITLE+", " +
                EventsDbSchema.EventsTable.Cols.DELETE_ON_COMPLETE+" INTEGER, "+
                EventsDbSchema.EventsTable.Cols.EVENT_TEXT + ", "+
                EventsDbSchema.EventsTable.Cols.CONTACT + ", "+
                EventsDbSchema.EventsTable.Cols.CONTACT_NUMBER + ", "+
                EventsDbSchema.EventsTable.Cols.EMAIL_ADDRESS + ", "+
                EventsDbSchema.EventsTable.Cols.EMAIL_SUBJECT + ", "+
                EventsDbSchema.EventsTable.Cols.USER_EMAIL+ ", "+
                EventsDbSchema.EventsTable.Cols.USER_PASSWORD +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
