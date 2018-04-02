package com.example.appinventiv.taskfirebasedatabase.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appinventiv.taskfirebasedatabase.R;
import com.example.appinventiv.taskfirebasedatabase.model.Messages;
import com.example.appinventiv.taskfirebasedatabase.utility.AppConstants;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity {
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.et_message)
    EditText etMessage;
    @BindView(R.id.toolbar_profile)
    ImageView toolbarProfile;
    @BindView(R.id.rv_messages)
    RecyclerView rvMessages;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    private String mSelectedUserId, mCurrentUserId;
    private DatabaseReference mSenderReference;
    private FirebaseUser firebaseUser;
    private Query query;
    private FirebaseRecyclerAdapter mfirebaseRecyclerAdapter;
    private DatabaseReference mUser;
    private Messages mMessages;
    private TextView mTvSenderMessage, mTvRecieverMessage,mTvSenderTime,mTvReceiverTime;
    LinearLayout llsender,llreceiver;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        mUser = FirebaseDatabase.getInstance().getReference();
        toolbarProfile.setVisibility(View.VISIBLE);
        if (getIntent() != null) {
            String type = getIntent().getStringExtra("type");
            if (type.equals("inboxUser")) {
                setConversation();
            }
            if (type.equals("contactUser")) {
                setContactsConversation();
            }


        }
    }

    /**
     * set Converation for user present in contacts....
     */
    private void setContactsConversation() {
        final String number = getIntent().getStringExtra("selectedUserNumber");
        toolbarTitle.setText(getIntent().getStringExtra("SelectedUserName"));
        mUser.child(AppConstants.KEY_USER).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean status = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String databasenumber = snapshot.child("mMobileNumber").getValue().toString();
                    if (databasenumber.equals(number)) {
                        status = true;
                        break;
                    } else {
                        status = false;
                    }
                }
                if (status) {
                    Toast.makeText(ChatActivity.this, R.string.present_in_database, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChatActivity.this, R.string.not_present_in_database, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Set conversation for users present in Inbox
     */
    private void setConversation() {
        mSelectedUserId = getIntent().getStringExtra("selectedUserId");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mCurrentUserId = firebaseUser.getUid();
        mUser.child(AppConstants.KEY_INBOX).child(mSelectedUserId).child(mCurrentUserId);
        mSenderReference = mUser.child(AppConstants.KEY_INBOX).child(mCurrentUserId).child(mSelectedUserId);
        mUser.child(AppConstants.KEY_INBOX).child(mSelectedUserId).child(mCurrentUserId);
        rvMessages.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        mUser.child(AppConstants.KEY_INBOX).child(mCurrentUserId).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String roomId;
                if (dataSnapshot.hasChild(mSelectedUserId)) {
                    roomId = dataSnapshot.child(mSelectedUserId).getValue().toString();
                    setConversationAdapter(roomId);
                } else {
                    DatabaseReference inboxRef = mSenderReference.push();
                    String senderRoomId = inboxRef.getKey();
                    mUser.child(AppConstants.KEY_INBOX).child(mCurrentUserId).child(mSelectedUserId).setValue(senderRoomId);
                    mUser.child(AppConstants.KEY_INBOX).child(mSelectedUserId).child(mCurrentUserId).setValue(senderRoomId);
                    setConversationAdapter(senderRoomId);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mUser.child(AppConstants.KEY_USER).child(mSelectedUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("mFirstName").getValue().toString();
                toolbarTitle.setText(userName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Set adapter to show conversation between sender and receiver....
     */
    private void setConversationAdapter(String roomId) {
        query = FirebaseDatabase.getInstance()
                .getReference()
                .child(AppConstants.KEY_MESSAGES).child(roomId);
        FirebaseRecyclerOptions<Messages> options =
                new FirebaseRecyclerOptions.Builder<Messages>()
                        .setQuery(query, Messages.class)
                        .build();
        mfirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Messages, UserViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull Messages model) {
                String fromId = model.getmIdFrom();

                if (fromId.equals(mCurrentUserId))

                {
                    llsender.setVisibility(View.VISIBLE);
                    holder.setSenderMessage(model.getmMessage(),model.getmTimeStamp());
                } else {
                    llreceiver.setVisibility(View.VISIBLE);
                    holder.setReceiverMessage(model.getmMessage(),model.getmTimeStamp());
                }
            }

            @Override
            public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_row_messages, parent, false);

                return new UserViewHolder(view);
            }


        };
        mfirebaseRecyclerAdapter.startListening();
        rvMessages.setAdapter(mfirebaseRecyclerAdapter);
    }

    @OnClick(R.id.btn_send)
    public void onViewClicked() {

        if (etMessage.getText().toString().equals("")) {
            Toast.makeText(this, R.string.empty_message, Toast.LENGTH_SHORT).show();
        } else {

            mUser.child(AppConstants.KEY_INBOX).child(mCurrentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(mSelectedUserId)) {
                        String currentDateAndTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
                        String roomId = dataSnapshot.child(mSelectedUserId).getValue().toString();
                        final String message = etMessage.getText().toString().trim();
                        DatabaseReference messagesReference = mUser.child(AppConstants.KEY_MESSAGES).child(roomId).push();
                        String key = messagesReference.getKey();
                        Messages m = new Messages();
                        m.setmMessage(message);
                        m.setmTimeStamp(currentDateAndTime);
                        m.setmIdFrom(mCurrentUserId);
                        mUser.child(AppConstants.KEY_MESSAGES).child(roomId).child(key).setValue(m);
                    } else {
                        DatabaseReference inboxRef = mSenderReference.push();
                        String senderRoomId = inboxRef.getKey();
                        String currentDateAndTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
                        mUser.child(AppConstants.KEY_INBOX).child(mCurrentUserId).child(mSelectedUserId).setValue(senderRoomId);
                        mUser.child(AppConstants.KEY_INBOX).child(mSelectedUserId).child(mCurrentUserId).setValue(senderRoomId);
                        String message = etMessage.getText().toString().trim();
                        mMessages = new Messages();
                        mMessages.setmMessage(message);
                        DatabaseReference messageReference = mUser.child(AppConstants.KEY_MESSAGES).child(senderRoomId).push();
                        String msgKey = messageReference.getKey();
                        Messages messages = new Messages();
                        messages.setmMessage(message);
                        messages.setmIdFrom(mCurrentUserId);
                        messages.setmTimeStamp(currentDateAndTime);

                        mUser.child(AppConstants.KEY_MESSAGES).child(senderRoomId).child(msgKey).setValue(messages);
                    }
                    etMessage.setText("");
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }


    public class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mTvRecieverMessage = itemView.findViewById(R.id.tv_reciever_message);
            mTvSenderMessage = itemView.findViewById(R.id.tv_sender_message);
            llreceiver=itemView.findViewById(R.id.ll_receiver);
            llsender=itemView.findViewById(R.id.ll_sender);
            mTvReceiverTime=itemView.findViewById(R.id.tv_sender_time);
            mTvSenderTime=itemView.findViewById(R.id.tv_receiver_time);

        }


        /**
         * set text sends from sender end.....
         *
         * @param s
         */
        public void setSenderMessage(String s,String time) {
            mTvSenderMessage.setText(s);
            mTvSenderTime.setText(time);
        }

        /**
         * set text sends from receiver end...
         *
         * @param s
         */
        public void setReceiverMessage(String s,String time) {
            mTvRecieverMessage.setText(s);
            mTvReceiverTime.setText(time);
        }
    }
}






