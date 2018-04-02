package com.example.appinventiv.taskfirebasedatabase.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.appinventiv.taskfirebasedatabase.fragment.ImageSliderFragment;
import com.example.appinventiv.taskfirebasedatabase.utility.AppConstants;

/**
 * Created by appinventiv on 21/3/18.
 */

public class ImageSliderAdapter extends FragmentPagerAdapter {
    public ImageSliderAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle=new Bundle();
        switch(position)
        {
            case 0:
                bundle.putInt(AppConstants.KEY_IMAGE,0);
                break;
            case 1:
                bundle.putInt(AppConstants.KEY_IMAGE,1);
                break;
            case 2:
                bundle.putInt(AppConstants.KEY_IMAGE,2);
                break;
            case 3:
                bundle.putInt(AppConstants.KEY_IMAGE,3);

        }
        ImageSliderFragment fragment=new ImageSliderFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
