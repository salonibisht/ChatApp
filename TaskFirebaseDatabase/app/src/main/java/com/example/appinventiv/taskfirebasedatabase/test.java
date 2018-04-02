package com.example.appinventiv.taskfirebasedatabase;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appinventiv.taskfirebasedatabase.activity.ChatActivity;
import com.example.appinventiv.taskfirebasedatabase.model.Messages;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by appinventiv on 28/3/18.
 */

public class test {
    TextView toolbarTitle;
    private String mChatUserId, mName, mCurrentUserId;
    private TextView tvMessage;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser firebaseUser;
    private Query query;
    private ArrayList<Messages> mMessageList;

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        toolbarProfile.setVisibility(View.VISIBLE);
        if (getIntent() != null) {
            mMessageList=new ArrayList<>();
            mDatabaseReference = FirebaseDatabase.getInstance().getReference();
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            rvMessages.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
            mMessageAdapter=new MessageAdapter(mMessageList);
            rvMessages.setAdapter(mMessageAdapter);
            mCurrentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mChatUserId = getIntent().getStringExtra("userId");
            mName = getIntent().getStringExtra("name");
            toolbarTitle.setText(mName);
            query = FirebaseDatabase.getInstance()
                    .getReference().child("messages").child(mCurrentUserId).child(mChatUserId);
            mDatabaseReference.child("messages").child(mCurrentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChild(mChatUserId)) {
                        Map chatAddMap = new HashMap();
                        chatAddMap.put("seen", false);
                        chatAddMap.put("timestamp", ServerValue.TIMESTAMP);
                        Map chatUserMap = new HashMap();
                        chatAddMap.put("messages/" + mCurrentUserId + "/" + mChatUserId, chatAddMap);
                        chatAddMap.put("messages/" + mChatUserId + "/" + mCurrentUserId, chatAddMap);
                        mDatabaseReference.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Toast.makeText(ChatActivity.this, databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            loadMessages();
        }

    }

    *//**
     * Retrieve data from data base,add it to arraylist.....
     *//*
    private void loadMessages() {
        if(firebaseUser!=null){


            mDatabaseReference.child("messages").child(mCurrentUserId).child(mChatUserId).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Messages message=dataSnapshot.getValue(Messages.class);
                    mMessageList.add(message);
                    mMessageAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @OnClick(R.id.btn_send)
    public void onViewClicked() {
        sendMessage();
    }

    *//**
     * Send messages to receiver and save it in Firebase Database....
     *//*
    private void sendMessage() {
        String message = etMessage.getText().toString();
        if (!message.equals("")) {
            String currentUser_ref = "messages/" + mCurrentUserId + "/" + mChatUserId;
            String chatUser_ref = "messages/" + mChatUserId + "/" + mCurrentUserId;
            DatabaseReference databaseReference = mDatabaseReference.child("messages").child(mCurrentUserId).child(mChatUserId).push();
            String room_id = databaseReference.getKey();
            Map messageMap = new HashMap();
            messageMap.put("roomId",room_id);
            messageMap.put("message", message);
            messageMap.put("seen", false);
            messageMap.put("type", "text");
            messageMap.put("time", ServerValue.TIMESTAMP);
            Map userMessageMap = new HashMap();
            userMessageMap.put(currentUser_ref + "/" + room_id, messageMap);
            userMessageMap.put(chatUser_ref + "/" + room_id, messageMap);
            mDatabaseReference.updateChildren(userMessageMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    Log.d("result","completed");
                }
            });
        } }

}*/
}
