package com.example.appinventiv.taskfirebasedatabase.model;

/**
 * Created by appinventiv on 28/3/18.
 */

public class Messages {
    private String mSeen="";
    private String mTimeStamp="";
    private String mMessage="";
    private String mIdFrom="";
    public Messages()
    {
    }

    public String getmSeen() {
        return mSeen;
    }

    public void setmSeen(String mSeen) {
        this.mSeen = mSeen;
    }

    public String getmTimeStamp() {
        return mTimeStamp;
    }

    public void setmTimeStamp(String mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public String getmIdFrom() {
        return mIdFrom;
    }

    public void setmIdFrom(String mIdFrom) {
        this.mIdFrom = mIdFrom;
    }
}
