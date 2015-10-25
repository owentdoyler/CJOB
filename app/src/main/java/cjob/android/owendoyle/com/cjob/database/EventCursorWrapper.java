package cjob.android.owendoyle.com.cjob.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import cjob.android.owendoyle.com.cjob.events.Event;

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
        event.setType(getString(getColumnIndex(EventsDbSchema.EventsTable.Cols.EVENT_TYPE)));
        event.setTitle(getString(getColumnIndex(EventsDbSchema.EventsTable.Cols.EVENT_TITLE)));
        event.setText(getString(getColumnIndex(EventsDbSchema.EventsTable.Cols.EVENT_TEXT)));
        event.setContact(getString(getColumnIndex(EventsDbSchema.EventsTable.Cols.CONTACT)));
        event.setEmail(getString(getColumnIndex(EventsDbSchema.EventsTable.Cols.EMAIL_ADDRESS)));
        event.setDeleteOnComplete(getInt(getColumnIndex(EventsDbSchema.EventsTable.Cols.DELETE_ON_COMPLETE)));
        return event;
    }
}
