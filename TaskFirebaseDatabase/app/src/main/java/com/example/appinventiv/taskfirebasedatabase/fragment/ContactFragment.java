package com.example.appinventiv.taskfirebasedatabase.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.appinventiv.taskfirebasedatabase.R;
import com.example.appinventiv.taskfirebasedatabase.activity.ChatActivity;
import com.example.appinventiv.taskfirebasedatabase.adapter.ContactAdapter;
import com.example.appinventiv.taskfirebasedatabase.interfaces.UserInfoInterface;
import com.example.appinventiv.taskfirebasedatabase.model.ContactModel;
import com.example.appinventiv.taskfirebasedatabase.utility.AppConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment implements UserInfoInterface {

    @BindView(R.id.rv_contacts)
    RecyclerView rvContacts;
    Unbinder unbinder;
    private ContactAdapter mAdapterContact;
    private ProgressDialog progressDialog;
    private DatabaseReference mDatabasereference;
    private FirebaseUser mFirebaseUser;
    private ArrayList<ContactModel> mContactInfoList;
    private String name, mDatabasenumber;
    private String mMobileNumber,mMobileName;
    private ArrayList<ContactModel> totalNumberList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        unbinder = ButterKnife.bind(this, view);
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabasereference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(getActivity());
        totalNumberList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvContacts.setLayoutManager(layoutManager);
        requestPermission();
        return view;
    }

    /**
     * Set Adapter for Contacts.....
     */
    private void setAdapter(ArrayList<ContactModel> list) {
        mAdapterContact = new ContactAdapter(getContext(), list, this);
        rvContacts.setAdapter(mAdapterContact);
    }

    /**
     * Request permission to fetch Contacts.....
     */

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, AppConstants.REQUEST_READ_PERMISSION);
            } else {
                progressDialog.setMessage(getString(R.string.loading));
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                new LoadContacts().execute();
            }
        } else {
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.show();
            new LoadContacts().execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == AppConstants.REQUEST_READ_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            new LoadContacts().execute();
        } else {
            Toast.makeText(getActivity(), R.string.permission_denied, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setOnItemClickListener(View view, int position) {
        String name=totalNumberList.get(position).getName();
        String number=totalNumberList.get(position).getNumber();
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("selectedUserNumber", number);
        intent.putExtra("type","contactUser");
        intent.putExtra("SelectedUserName",name);
        startActivity(intent);
    }




    /**
     * Fetch Merged Arraylist of number present in Firebase database and Mobile phone.....
     *
     * @param contactList
     */
    private void contactfetch(ArrayList<ContactModel> contactList) {

        setAdapter(contactList);
    }

    private class LoadContacts extends AsyncTask<Void, Void, ArrayList<ContactModel>> {

        @Override
        protected ArrayList<ContactModel> doInBackground(Void... voids) {

            return readContacts();
        }

        @Override
        protected void onPostExecute(final ArrayList<ContactModel> result) {
            super.onPostExecute(result);
            final ArrayList<ContactModel> databaseList = new ArrayList<>();
            final ArrayList<ContactModel> mobilenumberList = new ArrayList<>();
           // final ArrayList<ContactModel> totalNumberList = new ArrayList<>();
            mDatabasereference.child(AppConstants.KEY_USER).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean status = false;
                    for (int i = 0; i < result.size(); i++) {
                         mMobileNumber = result.get(i).getNumber();
                         mMobileName=result.get(i).getName();
                        for (DataSnapshot s : dataSnapshot.getChildren()) {
                            mDatabasenumber = (String) s.child("mMobileNumber").getValue();
                            if (mMobileNumber.equals(mDatabasenumber)) {
                                databaseList.add(new ContactModel(mMobileName,mMobileNumber));
                                status = true;
                                break;
                            }
                        }
                        if (!status) {
                            mobilenumberList.add(new ContactModel(mMobileName,mMobileNumber));
                        }

                    }
                    totalNumberList.addAll(databaseList);
                    totalNumberList.addAll(mobilenumberList);
                    contactfetch(totalNumberList);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("", "onCancelled: " + databaseError.getMessage());
                }
            });
            progressDialog.hide();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        /**
         * Fetch id,name and mobile number
         *
         * @return contactlist
         */
        @SuppressLint("NewApi")
        private ArrayList<ContactModel> readContacts() {
            ArrayList<ContactModel> contactList = new ArrayList<>();
            Cursor cursor = getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                int hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                String phoneNumber = "";
                if (hasPhone > 0) {
                    Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                    while (phones.moveToNext()) {
                        phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    phones.close();
                }
                //  String contactPhoto = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
                contactList.add(new ContactModel(name, phoneNumber));

            }

            return contactList;
        }


    }

}
