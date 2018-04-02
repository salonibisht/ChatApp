package com.example.appinventiv.taskfirebasedatabase.model;

/**
 * Created by appinventiv on 26/3/18.
 */

public class ContactModel {
    private String mNumber="",mName="",mId="",mContactPhoto="";
    public  ContactModel(String id,String number,String name,String contactPhoto)
    {
        this.mId=id;
        this.mName=name;
        this.mNumber=number;
    }
    public ContactModel(String name,String number)
    {
        mNumber=number;
        mName=name;
    }
    public String getNumber()
    {
        return mNumber;
    }
    public String getName(){
        return mName;
    }
    public String getId(){
        return mId;
    }
    public String getContactPhoto()
    {
        return mContactPhoto;
    }
}
