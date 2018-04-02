package com.example.appinventiv.taskfirebasedatabase.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appinventiv.taskfirebasedatabase.R;
import com.example.appinventiv.taskfirebasedatabase.activity.ChatActivity;
import com.example.appinventiv.taskfirebasedatabase.activity.LogInActivity;
import com.example.appinventiv.taskfirebasedatabase.interfaces.UserInfoInterface;
import com.example.appinventiv.taskfirebasedatabase.model.UserInfo;
import com.example.appinventiv.taskfirebasedatabase.utility.AppConstants;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment implements UserInfoInterface {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    Unbinder unbinder;
    @BindView(R.id.toolbar_profile)
    ImageView toolbarProfile;
    @BindView(R.id.rv_chat_contacts)
    RecyclerView rvChatContacts;
    private DatabaseReference mDatabasereference;
    private FirebaseUser firebaseUser;
    private Query query;
    private TextView tvName;
    private String name;

    private FirebaseRecyclerAdapter<UserInfo,UserViewHolder> firebaseRecyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbarTitle.setText(R.string.messages);
        toolbarProfile.setVisibility(View.VISIBLE);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
         query = FirebaseDatabase.getInstance()
                .getReference()
                .child(AppConstants.KEY_USER);
        rvChatContacts.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();
        setChatAdapter();
    }

    /**
     * set Adapter to show User name on inbox....
     */
    private void setChatAdapter() {
        FirebaseRecyclerOptions<UserInfo> options =
                new FirebaseRecyclerOptions.Builder<UserInfo>()
                        .setQuery(query, UserInfo.class)
                        .build();
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<UserInfo, UserViewHolder>(options) {


            @Override
            public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_status, parent, false);

                return new UserViewHolder(view);            }

            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull UserInfo model) {
                holder.setName(model.getmFirstName(),model.getmSurname());

            }
        };
        firebaseRecyclerAdapter.startListening();
        rvChatContacts.setAdapter(firebaseRecyclerAdapter);
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
    }

    @Override
    public void setOnItemClickListener(View view, int position) {
        Intent intent=new Intent(getActivity(),ChatActivity.class);
        intent.putExtra("selectedUserId",firebaseRecyclerAdapter.getRef(position).getKey());
        intent.putExtra("name",name);
        intent.putExtra("type","inboxUser");
        startActivity(intent);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;

    public UserViewHolder(View itemView) {
        super(itemView);
        tvName=itemView.findViewById(R.id.tv_user_name);
        mView=itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           setOnItemClickListener(view,getAdapterPosition());
            }
        });
    }

    /**
     * Set name present in the database to the text view in the inox..
     * @param s
     * @param s1
     */
    public void setName(String s, String s1) {

name=s+" "+s1;
        tvName.setText(s+" "+s1);
    }
}
}
