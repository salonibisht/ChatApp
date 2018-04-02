package com.example.appinventiv.taskfirebasedatabase.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appinventiv.taskfirebasedatabase.R;
import com.example.appinventiv.taskfirebasedatabase.adapter.ImageSliderAdapter;
import com.example.appinventiv.taskfirebasedatabase.utility.AppUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity  {

    @BindView(R.id.image_pager)
    ViewPager imagePager;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    @BindView(R.id.btn_create_account)
    Button btnCreateAccount;
    @BindView(R.id.btn_log_in)
    Button btnLogIn;
    @BindView(R.id.tv_find_plans)
    TextView tvFindPlans;
    private ImageSliderAdapter mImageSliderAdapter;
    private FirebaseUser firebaseUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        ButterKnife.bind(this);
        setAdapterAndIndicator();

    }
    /**
     * set Adapter and viewpager with indicator...
     */
    private void setAdapterAndIndicator() {
        mImageSliderAdapter = new ImageSliderAdapter(getSupportFragmentManager());
        imagePager.setAdapter(mImageSliderAdapter);
        indicator.setViewPager(imagePager);
    }
    @OnClick({R.id.btn_create_account, R.id.btn_log_in})
    public void onViewClicked(View view) {
        if (AppUtils.getInstance().isNetworkAvailable(MainActivity.this)){
            switch (view.getId()) {
            case R.id.btn_create_account:
                Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_log_in:

    Intent intent1 = new Intent(MainActivity.this, LogInActivity.class);

    startActivity(intent1);
            }
    }else{
            Toast.makeText(this, R.string.please_check_internet_connection, Toast.LENGTH_SHORT).show();

        }
    }

}
