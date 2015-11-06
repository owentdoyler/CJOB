package cjob.android.owendoyle.com.cjob.events;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import cjob.android.owendoyle.com.cjob.MapActivity;
import cjob.android.owendoyle.com.cjob.R;

/**
 * Created by Owner on 05/11/2015.
 */
public class SendEmail extends AsyncTask<Event,Void,Void> {

    @Override
    protected Void doInBackground(Event... events) {
        String email = events[0].getUserEmail();
        String pass = events[0].getUserPassword();
        MailEvent m = new MailEvent(email,pass);

        String[] toArr = {events[0].getEmail()};
        m.set_to(toArr);
        m.set_from(email);
        m.set_subject(events[0].getEmailSubject());
        m.setBody(events[0].getText());

        try {
            if(m.send()) {
                //Toast.makeText(mContext, "Email was sent successfully.", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(mContext, "Email was not sent.", Toast.LENGTH_LONG).show();
            }
        } catch(Exception e) {
            //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
            Log.e("EMAIL", "Could not send email", e);
        }

        return null;
    }

}

