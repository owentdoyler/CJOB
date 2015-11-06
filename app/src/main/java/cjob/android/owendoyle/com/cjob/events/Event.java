package cjob.android.owendoyle.com.cjob.events;

import java.util.UUID;

/**
 * Name: Owen Doyle
 * Student Number: 12453618
 * File: Event.java
 *
 * This is a class to hold the information about an event
 */
public class Event {

    private int id;
    private Double latitude;
    private Double longitude;
    private int radius;
    private String address;
    private String mType;
    private String mTitle;
    private String mText;
    private String mContact;
    private String mContactNumber;
    private String mEmail;
    private String mEmailSubject;
    private int mDeleteOnComplete;
    private String mUserEmail;
    private String mUserPassword;

    public Event(){

        latitude = null;
        longitude = null;
        radius = 10;
        address = null;
        mType = null;
        mTitle = null;
        mText = null;
        mContact = null;
        mContactNumber = null;
        mEmail = null;
        mEmailSubject = null;
        mDeleteOnComplete = 1;
        mUserPassword = null;
        mUserEmail = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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

    public String getContactNumber() {
        return mContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        mContactNumber = contactNumber;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getEmailSubject() {
        return mEmailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        mEmailSubject = emailSubject;
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

    public String getUserEmail() {
        return mUserEmail;
    }

    public void setUserEmail(String userEmail) {
        mUserEmail = userEmail;
    }

    public String getUserPassword() {
        return mUserPassword;
    }

    public void setUserPassword(String userPassword) {
        mUserPassword = userPassword;
    }

    public String toString(){
        return "Event{ Id: "+getId()+
                " lat: "+getLatitude()+
                " long: "+getLongitude()+
                " radius: "+getRadius()+
                " address "+getAddress()+
                " title: "+getTitle()+
                " type: "+getType()+
                " text: "+getText()+
                " contact: "+getContact()+
                " contact number: "+getContactNumber()+
                " email: "+getEmail()+
                " email subject: "+getEmailSubject()+
                " delete: "+getDeleteOnComplete()+" }";
    }
}
