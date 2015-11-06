package cjob.android.owendoyle.com.cjob.database;

/**
 * Name: Owen Doyle
 * Student Number: 12453618
 * File: EventsDbSchema.java
 *
 * This class defines the schema of the database
 */
public class EventsDbSchema {
    public static final class EventsTable{
        public static final String NAME = "events";

        public static final class Cols {
            public static final String LAT = "lat";
            public static final String LONG = "long";
            public static final String RADIUS = "radius";
            public static final String ADDRESS = "address";
            public static final String TITLE = "title";
            public static final String EVENT_TYPE = "event_type";
            public static final String EVENT_TITLE = "event_title";
            public static final String DELETE_ON_COMPLETE = "delete_on_complete";
            public static final String EVENT_TEXT = "event_text";
            public static final String CONTACT = "contact";
            public static final String CONTACT_NUMBER = "contact_number";
            public static final String EMAIL_ADDRESS = "email_address";
            public static final String EMAIL_SUBJECT = "email_subject";
            public static final String USER_EMAIL = "user_email";
            public static final String USER_PASSWORD = "user_password";
        }
    }
}
