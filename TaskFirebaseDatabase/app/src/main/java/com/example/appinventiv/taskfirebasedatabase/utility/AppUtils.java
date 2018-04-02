package com.example.appinventiv.taskfirebasedatabase.utility;

import android.content.Context;
import android.net.ConnectivityManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by appinventiv on 21/3/18.
 */

public class AppUtils {
    private static AppUtils mNetworkUtils;

    public static AppUtils getInstance(){
        if(mNetworkUtils==null){
            mNetworkUtils = new AppUtils();
        }
        return mNetworkUtils;

    }

    /**
     * Validation on Network Connection......
     * @param context
     * @return
     */

    public boolean isNetworkAvailable(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if( cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public void saveDataInDatabase(com.example.appinventiv.taskfirebasedatabase.model.UserInfo userInfo) {
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = database.getReference();
        if(firebaseUser!=null)
        {
            databaseReference.child(AppConstants.KEY_USER).child(firebaseUser.getUid()).setValue(userInfo);
        }
    }
    public  boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

}
