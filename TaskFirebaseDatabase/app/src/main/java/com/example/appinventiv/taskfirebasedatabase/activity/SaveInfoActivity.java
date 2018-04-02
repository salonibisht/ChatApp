package com.example.appinventiv.taskfirebasedatabase.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appinventiv.taskfirebasedatabase.R;
import com.example.appinventiv.taskfirebasedatabase.model.UserInfo;
import com.example.appinventiv.taskfirebasedatabase.utility.AppConstants;
import com.example.appinventiv.taskfirebasedatabase.utility.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SaveInfoActivity extends AppCompatActivity {

    @BindView(R.id.et_first_name)
    EditText etFirstName;
    @BindView(R.id.et_surname)
    EditText etSurname;
    ;
    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_dob)
    EditText etDob;
    @BindView(R.id.et_mobile_number)
    EditText etMobileNumber;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    private String mUserId,mEmailId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_info);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        if (checkInputValidation()) {
          saveUserInfo();
            Toast.makeText(this, R.string.your_data_has_been_submitted, Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(SaveInfoActivity.this,UserDataActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Save user information to Firebase Database.....
     */
    private void saveUserInfo() {
        Intent intent=getIntent();
        if(intent!=null){
            mUserId=intent.getStringExtra(AppConstants.KEY_USER_ID);
            mEmailId=intent.getStringExtra(AppConstants.KEY_EMAIL);
        }
       UserInfo userInfo=new UserInfo();
        userInfo.setmSurname(etSurname.getText().toString());
        userInfo.setmMobileNumber(etMobileNumber.getText().toString());
        userInfo.setMdob(etDob.getText().toString());
        userInfo.setmUserName(etUserName.getText().toString());
        userInfo.setmUserName(etUserName.getText().toString());
        userInfo.setmFirstName(etFirstName.getText().toString());
        userInfo.setmUserId(mUserId);
        userInfo.setmEmail(mEmailId);
        AppUtils.getInstance().saveDataInDatabase(userInfo);
    }



    /**
     * check on input fields are filled or not
     */
    private boolean checkInputValidation() {
        if (etFirstName.getText().toString().equals("")) {
            Toast.makeText(this, R.string.please_enter_first_name, Toast.LENGTH_SHORT).show();

        } else if (etSurname.getText().toString().equals("")) {
            Toast.makeText(this, R.string.please_enter_surname, Toast.LENGTH_SHORT).show();

        } else if (etDob.getText().toString().equals("")) {
            Toast.makeText(this, R.string.please_enter_date_of_birth, Toast.LENGTH_SHORT).show();

        } else if (etMobileNumber.getText().toString().equals("")) {
            Toast.makeText(this, R.string.please_enter_mobile_nuber, Toast.LENGTH_SHORT).show();

        } else if (etUserName.getText().toString().equals("")) {
            Toast.makeText(this, R.string.please_enter_user_name, Toast.LENGTH_SHORT).show();

        }
            return true;
    }
}



