package com.example.appinventiv.taskfirebasedatabase.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appinventiv.taskfirebasedatabase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResetPasswordActivity extends AppCompatActivity {
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        ButterKnife.bind(this);
        toolbarTitle.setText(R.string.Reset_passsword);
        mFirebaseAuth = FirebaseAuth.getInstance();
    }


    /**
     * Reset Password by Send message to the email address given by the user.....
     */
    private void resetPassword() {
        mFirebaseAuth.sendPasswordResetEmail(etEmail.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPasswordActivity.this, R.string.please_check_email_to_reset_password, Toast.LENGTH_LONG).show();
                            Intent loginIntent = new Intent(ResetPasswordActivity.this, LogInActivity.class);
                            startActivity(loginIntent);
                        }
                    }
                });
    }


    @OnClick({R.id.iv_back, R.id.btn_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_send:
                if (etEmail.getText().toString().equals("")) {
                    Toast.makeText(this, R.string.please_enter_email_id, Toast.LENGTH_SHORT).show();
                } else {
                    resetPassword();
                }
                break;
        }
    }
}
