/*
* this class sends the email in the background
* */
package cjob.android.owendoyle.com.cjob.events;

import android.os.AsyncTask;
import android.util.Log;


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
                //Email was sent successfully
            } else {
                //Email was not sent
            }
        } catch(Exception e) {
            //There was a problem sending the email
            Log.e("EMAIL", "Could not send email", e);
        }

        return null;
    }

}

