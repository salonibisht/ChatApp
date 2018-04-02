package com.example.appinventiv.taskfirebasedatabase.activity;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appinventiv.taskfirebasedatabase.R;
import com.example.appinventiv.taskfirebasedatabase.model.UserInfo;
import com.example.appinventiv.taskfirebasedatabase.utility.AppConstants;
import com.example.appinventiv.taskfirebasedatabase.utility.AppUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogInActivity extends AppCompatActivity {

    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_log_in)
    Button btnLogIn;
    @BindView(R.id.tv_forgot_password)
    TextView tvForgotPassword;
    @BindView(R.id.btn_log_in_google)
    Button btnLogInGoogle;
    @BindView(R.id.btn_log_in_facebook)
    Button btnLogInFacebook;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    private FirebaseAuth mFirebaseAuth;
    private CallbackManager mCallbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseUser user;
    private DatabaseReference mDatabasereference;
    private String userName, emailId, mPhoneNumber, gender, userId, Photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);
        toolbarTitle.setText(R.string.log_in);
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabasereference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        facebookInitialisation();
        GoogleSignInInitialisation();

    }
    /**
     * Initialisation of GoogleSignInClient and GoogleSignInOption
     */
    private void GoogleSignInInitialisation() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

    }

    /**
     * Initialisation of facebookSdk and CallbackManager.....
     */
    private void facebookInitialisation() {
        FacebookSdk.isInitialized();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
    }
    /**
     * Check whether User Information is present in data base or not if present opent Chat Activity else Open SaveInfo Activity....
     *
     * @param user
     */

    private void setdata(final FirebaseUser user) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(AppConstants.KEY_USER);
        // final DatabaseReference ref = database.getReference(AppConstants.KEY_USER);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean exist=false;
                for (DataSnapshot s : snapshot.getChildren()) {
                    if (Objects.equals(s.child("mEmail").getValue(), etEmail.getText().toString())) {
                        exist = true;
                        break;
                    }else {
                        exist=false;
                    }

                }
                if (exist) {
                    Intent intent = new Intent(LogInActivity.this, UserDataActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(LogInActivity.this, SaveInfoActivity.class);
                    intent.putExtra(AppConstants.KEY_USER_ID, user.getUid());
                    intent.putExtra(AppConstants.KEY_EMAIL, user.getEmail());
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LogInActivity.this, R.string.some_database_error_occured, Toast.LENGTH_SHORT).show();

            }
        });
    }

    /**
     * Check whether the Input Fields are Filled or not....
     *
     * @return
     */
    private boolean checkInputValidation() {
        if (etEmail.getText().toString().equals("")) {
            Toast.makeText(this, R.string.please_enter_email_id, Toast.LENGTH_SHORT).show();
            return false;
        } else if (etPassword.getText().toString().equals("")) {
            Toast.makeText(this, R.string.please_enter_password, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @OnClick({R.id.btn_log_in, R.id.tv_forgot_password, R.id.btn_log_in_google, R.id.btn_log_in_facebook})
    public void onViewClicked(View view) {
        if (AppUtils.getInstance().isNetworkAvailable(LogInActivity.this)) {

            switch (view.getId()) {
                case R.id.btn_log_in:
                    if (checkInputValidation()) {
                        if(AppUtils.getInstance().isValidEmail(etEmail.getText().toString())){


                        //final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {

                            mFirebaseAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            if (task.isSuccessful()) {

                                                if (user.isEmailVerified()) {
                                                    setdata(user);
                                                } else {
                                                    Toast.makeText(LogInActivity.this, R.string.first_verify_your_email_id, Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(LogInActivity.this, R.string.not_successfully_signed_in, Toast.LENGTH_SHORT).show();

                                            }
                                        }


                                    });


                        }

                        }else{
                            Toast.makeText(this, R.string.please_enter_a_valid_emailid, Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case R.id.tv_forgot_password:
                    Intent intent = new Intent(LogInActivity.this, ResetPasswordActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_log_in_google:
                    signInWithGoogleAccount();
                    break;
                case R.id.btn_log_in_facebook:
                    signInWithFacebook();
                    break;

            }
        }
    }


    /**
     * send account information to check authentication....
     *
     * @param result
     */
    private void handleResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthWithGoogle(account);

        }
    }

    /**
     * set Alertbox to take user mobile number if user is not verified.....
     */
    private String setAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_enter_phone_number);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        builder.setView(input);
        final AlertDialog alertDialog = builder.create();

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPhoneNumber = input.getText().toString();
alertDialog.dismiss();

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
        return mPhoneNumber;
    }

    /**
     * Google account authentication.......
     *
     * @param acct
     */
    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
if(user!=null){
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference ref = database.getReference(AppConstants.KEY_USER);
    ref.addListenerForSingleValueEvent(new ValueEventListener() {
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public void onDataChange(DataSnapshot snapshot) {
            for (DataSnapshot child : snapshot.getChildren()) {
                boolean exist = false;
                if (Objects.equals(child.child("mEmail").getValue(),acct.getEmail())){
                    exist = true;
                    break;
                }else {
                    exist=false;
                }
                if (exist) {
                    Intent intent = new Intent(LogInActivity.this, UserDataActivity.class);
                    startActivity(intent);
                } else {
                    try {
                        String phoneNumber = setAlertDialog();
                        UserInfo userInfo = new UserInfo();
                        userName = acct.getDisplayName();
                        emailId = acct.getEmail();
                        userInfo.setmEmail(emailId);
                        userInfo.setmName(userName);
                        userInfo.setmMobileNumber(phoneNumber);
                        userInfo.setmUserId(userId);
                        AppUtils.getInstance().saveDataInDatabase(userInfo);
                        userInfo.setmMobileNumber(phoneNumber);
                        Intent intent = new Intent(LogInActivity.this, UserDataActivity.class);
                        startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Toast.makeText(LogInActivity.this, R.string.some_database_error_occured, Toast.LENGTH_SHORT).show();

        }
    });
}
                        } else {
                            Toast.makeText(LogInActivity.this, R.string.Authentication_Failed, Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConstants.RC_SIGN_IN && resultCode == RESULT_OK) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Sigin to Google Account.....
     */
    private void signInWithGoogleAccount() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, AppConstants.RC_SIGN_IN);

    }

    /**
     * Sign in with Facebook account...
     */
    private void signInWithFacebook() {
      //  LoginManager.getInstance().logOut();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                firebaseAuthWithFacebook(loginResult, loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(LogInActivity.this, R.string.cancelled, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LogInActivity.this, R.string.facebook_error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Facebook account authentication for corresponding user....
     *
     * @param result
     * @param token
     */

    private void firebaseAuthWithFacebook(final LoginResult result, AccessToken token) {

        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if(user!=null)
                            { GraphRequest request = GraphRequest.newMeRequest(
                                    result.getAccessToken(),
                                    new GraphRequest.GraphJSONObjectCallback() {

                                        @Override
                                        public void onCompleted(JSONObject object, GraphResponse response) {
                                            Log.v("Main", response.toString());
                                            try {
                                                emailId =object.getString(AppConstants.KEY_EMAIL);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                            mDatabasereference.child(AppConstants.KEY_USER).addListenerForSingleValueEvent(new ValueEventListener() {
                                @TargetApi(Build.VERSION_CODES.KITKAT)
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    boolean exist = false;
                                    for (DataSnapshot child : snapshot.getChildren()) {
                                        if (Objects.equals(child.child("mEmail").getValue(),emailId)) {
                                            exist = true;
                                            break;
                                        } else {
                                            exist = false;
                                        }
                                    }if (exist) {
                                        Intent intent = new Intent(LogInActivity.this, UserDataActivity.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        try {
                                            String num = setAlertDialog();
                                            fetchData(result, num);
                                            Intent intent = new Intent(LogInActivity.this, SaveInfoActivity.class);
                                            startActivity(intent);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }

                            });
                            }
                        }

                    }

                });
    }


    /**
     * Fetch data from facebook account of user....
     *
     * @param loginResult
     * @param num
     */
    private void fetchData(LoginResult loginResult, final String num) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("Main", response.toString());
                        setProfileToView(object, num);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString(AppConstants.KEY_FIELDS, "id,name,email,gender,birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    /**
     * set data into corresponding strings....
     *
     * @param jsonObject
     */
    private void setProfileToView(JSONObject jsonObject, String phoneNumber) {
        try {
            UserInfo userInfo = new UserInfo();
            userInfo.setmName(jsonObject.getString(AppConstants.KEY_NAME));
            userInfo.setmEmail(jsonObject.getString(AppConstants.KEY_EMAIL));
            userInfo.setmGender(jsonObject.getString(AppConstants.KEY_GENDER));
            userInfo.setmMobileNumber(phoneNumber);
            AppUtils.getInstance().saveDataInDatabase(userInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


