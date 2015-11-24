package cjob.android.owendoyle.com.cjob.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import cjob.android.owendoyle.com.cjob.events.Event;

/**
 * Name: Owen Doyle
 * Student Number: 12453618
 * File: EventCursorWrapper.java
 *
 * This class makes it easy to get the data required from a cursor object
 */
public class EventCursorWrapper extends CursorWrapper {
    public EventCursorWrapper(Cursor cursor){
        super(cursor);
    }

    //extract columns containing information about the event
    public Event getEventDetails(){
        Event event = new Event();
        int eventId = getInt(getColumnIndex("_id"));
        String lat = getString(getColumnIndex(EventsDbSchema.EventsTable.Cols.LAT));
        String longitude = getString(getColumnIndex(EventsDbSchema.EventsTable.Cols.LONG));
        String radius = getString(getColumnIndex(EventsDbSchema.EventsTable.Cols.RADIUS));
        event.setAddress(getString(getColumnIndex(EventsDbSchema.EventsTable.Cols.ADDRESS)));
        event.setType(getString(getColumnIndex(EventsDbSchema.EventsTable.Cols.EVENT_TYPE)));
        event.setTitle(getString(getColumnIndex(EventsDbSchema.EventsTable.Cols.EVENT_TITLE)));
        event.setText(getString(getColumnIndex(EventsDbSchema.EventsTable.Cols.EVENT_TEXT)));
        event.setContact(getString(getColumnIndex(EventsDbSchema.EventsTable.Cols.CONTACT)));
        event.setContactNumber(getString(getColumnIndex(EventsDbSchema.EventsTable.Cols.CONTACT_NUMBER)));
        event.setEmail(getString(getColumnIndex(EventsDbSchema.EventsTable.Cols.EMAIL_ADDRESS)));
        event.setEmailSubject(getString(getColumnIndex(EventsDbSchema.EventsTable.Cols.EMAIL_SUBJECT)));
        event.setDeleteOnComplete(getInt(getColumnIndex(EventsDbSchema.EventsTable.Cols.DELETE_ON_COMPLETE)));
        event.setUserEmail(getString(getColumnIndex(EventsDbSchema.EventsTable.Cols.USER_EMAIL)));
        event.setUserPassword(getString(getColumnIndex(EventsDbSchema.EventsTable.Cols.USER_PASSWORD)));
        event.setActive(getInt(getColumnIndex(EventsDbSchema.EventsTable.Cols.ACTIVE)));
        event.setId(eventId);
        event.setLatitude(Double.parseDouble(lat));
        event.setLongitude(Double.parseDouble(longitude));
        event.setRadius(Integer.parseInt(radius));

        return event;
    }
}
