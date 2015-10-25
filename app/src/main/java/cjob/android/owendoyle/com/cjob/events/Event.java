package cjob.android.owendoyle.com.cjob.events;

/**
 * Name: Owen Doyle
 * Student Number: 12453618
 * File: Event.java
 *
 * This is a class to hold the information about an event
 */
public class Event {
    private String mType;
    private String mTitle;
    private String mText;
    private String mContact;
    private String mEmail;
    private int mDeleteOnComplete;


    public String getTitle() {
        return mTitle;
    }

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    public String getContact() {
        return mContact;
    }

    public void setContact(String mName) {
        this.mContact = mName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public int getDeleteOnComplete() {
        return mDeleteOnComplete;
    }

    public void setDeleteOnComplete(int mDeleteOnComplete) {
        this.mDeleteOnComplete = mDeleteOnComplete;
    }

    public void setTitle(String mTitle) {

        this.mTitle = mTitle;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public String toString(){
        return "Event{ title: "+getTitle()+" type: "+getType()+" text: "+getText()+" contact: "+getContact()+" email: "+getEmail()+" delete: "+getDeleteOnComplete()+" }";
    }
}
