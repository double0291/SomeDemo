package com.doublechen.androidtest.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.doublechen.androidtest.R;

public class AnimationFragment extends Fragment implements OnClickListener {
    Button mButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_animation, container, false);

        // Bundle args = getArguments();
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        default:
            break;
        }
    }
}
