package com.example.appinventiv.taskfirebasedatabase.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.appinventiv.taskfirebasedatabase.R;
import com.example.appinventiv.taskfirebasedatabase.utility.AppConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageSliderFragment extends Fragment {

private ImageView mIvImage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_slider, container, false);
        mIvImage =view.findViewById(R.id.iv_pager_image);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            int n = bundle.getInt(AppConstants.KEY_IMAGE);
            switch (n) {
                case 0:
                    mIvImage.setImageResource(R.drawable.walkthrough1);
                    break;
                case 1:
                    mIvImage.setImageResource(R.drawable.walkthrough2);
                    break;
                case 2:
                    mIvImage.setImageResource(R.drawable.walkthrough3);
                    break;
                case 3:
                    mIvImage.setImageResource(R.drawable.walkthrough4);
                    break;
            }

        }

        return view;
    }


}
