package cjob.android.owendoyle.com.cjob;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cjob.android.owendoyle.com.cjob.EventsDbSchema.EventsTable;

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
        db.execSQL("create table " + EventsTable.NAME + "("+
                "_id integer primary key autoincrement, " +
                EventsTable.Cols.LAT + ", " +
                EventsTable.Cols.LONG+ ", " +
                EventsTable.Cols.TITLE+ ", "+
                EventsTable.Cols.RADIUS+", " +
                EventsTable.Cols.ADDRESS+", " +
                EventsTable.Cols.EVENT_TYPE+", " +
                EventsTable.Cols.EVENT_TITLE+", " +
                EventsTable.Cols.DELETE_ON_COMPLETE+" INTEGER, "+
                EventsTable.Cols.EVENT_TEXT + ", "+
                EventsTable.Cols.CONTACT + ", "+
                EventsTable.Cols.EMAIL_ADDRESS+
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
