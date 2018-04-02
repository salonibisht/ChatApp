package com.example.appinventiv.taskfirebasedatabase.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appinventiv.taskfirebasedatabase.R;
import com.example.appinventiv.taskfirebasedatabase.utility.AppConstants;
import com.example.appinventiv.taskfirebasedatabase.utility.AppUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_sign_up)
    Button btnSignUp;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        toolbarTitle.setText(R.string.sign_up);
        mFirebaseAuth = FirebaseAuth.getInstance();
        btnSignUp.setOnClickListener(this);
    }

    /**
     * validation on input fields....
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

    @Override
    public void onClick(View view) {
        if (AppUtils.getInstance().isNetworkAvailable(SignUpActivity.this)) {

            if (checkInputValidation()) {
                mFirebaseAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    sendVerificationEmail();

                                  //Toast.makeText(SignUpActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }

        } else {
            Toast.makeText(this, R.string.please_check_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Verify Email adress by sending a email to user to verify it and send it to LoginActivity...
     */
    private void sendVerificationEmail() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, R.string.please_verify_email_address, Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(SignUpActivity.this, LogInActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SignUpActivity.this, R.string.email_not_send_due_to_some_error, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}

