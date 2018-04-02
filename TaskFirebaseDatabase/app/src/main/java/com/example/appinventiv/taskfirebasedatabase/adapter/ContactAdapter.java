package com.example.appinventiv.taskfirebasedatabase.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appinventiv.taskfirebasedatabase.R;
import com.example.appinventiv.taskfirebasedatabase.interfaces.UserInfoInterface;
import com.example.appinventiv.taskfirebasedatabase.model.ContactModel;

import java.util.ArrayList;

/**
 * Created by appinventiv on 26/3/18.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.RecyclerViewHolder> {
    private ArrayList<ContactModel> mContactList;
    private Context mContext;
    private UserInfoInterface mUserInfoInterface;

    public ContactAdapter(Context context, ArrayList<ContactModel> contactList,UserInfoInterface mUserInfoInterface) {
      this.mContext = context;
        mContactList = contactList;
        this.mUserInfoInterface=mUserInfoInterface;
    }

    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contacts, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.mTvnumber.setText(mContactList.get(position).getNumber());
        holder.mTvName.setText(mContactList.get(position).getName());
        /*String image = mContactList.get(position).getContactPhoto();
        if (image != null && !image.equals("")) {
            holder.mIvContact.setImageURI(Uri.parse(mContactList.get(position).getContactPhoto()));
        } else {
            holder.mIvContact.setImageResource(R.drawable.mutual_friends);
        }*/


    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvName, mTvnumber;
       // private ImageView mIvContact;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvnumber = itemView.findViewById(R.id.tv_number);
           // mIvContact = itemView.findViewById(R.id.iv_contact);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
             mUserInfoInterface.setOnItemClickListener(view,getAdapterPosition());
                }
            });
        }


}}

