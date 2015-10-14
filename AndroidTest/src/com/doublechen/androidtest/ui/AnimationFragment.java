package com.doublechen.androidtest.ui;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.doublechen.androidtest.R;

public class AnimationFragment extends Fragment implements OnClickListener {
    ImageView mImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_animation, container, false);

        mImageView = (ImageView) rootView.findViewById(R.id.imageView);
        mImageView.setOnClickListener(this);
        // Bundle args = getArguments();
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.imageView:
            // ObjectAnimator//
            // .ofFloat(view, "rotationX", 0.0F, 360.0F)//
            // .setDuration(500)//
            // .start();
            ObjectAnimator.ofFloat(v, "rotationY", 0.0f, 360.0f).setDuration(1000).start();
            break;
        default:
            break;
        }
    }
}
