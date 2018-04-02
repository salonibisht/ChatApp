package com.example.appinventiv.taskfirebasedatabase.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.appinventiv.taskfirebasedatabase.R;
import com.example.appinventiv.taskfirebasedatabase.fragment.ChatFragment;
import com.example.appinventiv.taskfirebasedatabase.fragment.ContactFragment;
import com.example.appinventiv.taskfirebasedatabase.fragment.ProfileFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserDataActivity extends AppCompatActivity {

    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private int[] tabIcons = {
            R.drawable.chat_deselect,
            R.drawable.ic_roam_logo_grey,
            R.drawable.mutual_friends_grey
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
        ButterKnife.bind(this);
        tabs.setupWithViewPager(viewpager);
        setupViewPager();
        viewpager.setCurrentItem(1);
        setupTabIcons();
    }

    /**
     * Set icons to tablayout.........
     */
    private void setupTabIcons() {
        tabs.getTabAt(0).setIcon(tabIcons[0]);
        tabs.getTabAt(1).setIcon(tabIcons[1]);
        tabs.getTabAt(2).setIcon(tabIcons[2]);
    }

    /**
     * Add Fragment to viewpager adapter
     */
    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ContactFragment());
        adapter.addFragment(new ChatFragment());
        adapter.addFragment(new ProfileFragment());
        viewpager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }
}
