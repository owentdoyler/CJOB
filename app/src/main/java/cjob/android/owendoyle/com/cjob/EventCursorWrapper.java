package cjob.android.owendoyle.com.cjob;

import android.database.Cursor;
import android.database.CursorWrapper;

import cjob.android.owendoyle.com.cjob.EventsDbSchema.EventsTable;

/**
 * Name: Owen Doyle
 * Student Number: 12453618
 * File: EventCursorWrapper.java
 *
 * This class makes it easy to get the dat required from a cursor object
 */
public class EventCursorWrapper extends CursorWrapper {
    public EventCursorWrapper(Cursor cursor){
        super(cursor);
    }

    //extract columns containing information about the event
    public Event getEventDetails(){
        Event event = new Event();
        event.setType(getString(getColumnIndex(EventsTable.Cols.EVENT_TYPE)));
        event.setTitle(getString(getColumnIndex(EventsTable.Cols.EVENT_TITLE)));
        event.setText(getString(getColumnIndex(EventsTable.Cols.EVENT_TEXT)));
        event.setContact(getString(getColumnIndex(EventsTable.Cols.CONTACT)));
        event.setEmail(getString(getColumnIndex(EventsTable.Cols.EMAIL_ADDRESS)));
        event.setDeleteOnComplete(getInt(getColumnIndex(EventsTable.Cols.DELETE_ON_COMPLETE)));
        return event;
    }
}
