package com.doublechen.androidtest.ui;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.doublechen.androidtest.R;

public class AnimationFragment extends Fragment implements OnClickListener {
    Button mBtn1, mBtn2;
    ImageView mImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_animation, container, false);

        mBtn1 = (Button) rootView.findViewById(R.id.btn1);
        mBtn1.setOnClickListener(this);
        mBtn2 = (Button) rootView.findViewById(R.id.btn2);
        mBtn2.setOnClickListener(this);
        mImageView = (ImageView) rootView.findViewById(R.id.imageView);
        mImageView.setOnClickListener(this);
        // Bundle args = getArguments();
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.imageView:
            ObjectAnimator.ofFloat(v, "rotationX", 0.0f, 360.0f).setDuration(1000).start();
            break;
        case R.id.btn1: {
            ValueAnimator animator = ValueAnimator.ofFloat(0, 800f);
            // animator.setTarget(mImageView);
            animator.setDuration(2000).start();
            animator.addUpdateListener(new AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mImageView.setTranslationY((Float) animation.getAnimatedValue());
                }
            });
            break;
        }
        case R.id.btn2: {
            ValueAnimator animator = new ValueAnimator();
            animator.setDuration(3000);
            animator.setObjectValues(new PointF(0, 0));
            animator.setInterpolator(new LinearInterpolator());
            animator.setEvaluator(new TypeEvaluator<PointF>() {
                @Override
                public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
                    PointF point = new PointF();
                    point.x = 200 * fraction;
                    point.y = 0.5f * 200 * fraction * fraction;
                    return point;
                }
            });
            animator.start();
            animator.addUpdateListener(new AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    PointF point = (PointF) animation.getAnimatedValue();
                    mImageView.setX(point.x);
                    mImageView.setY(point.y);
                }
            });
        }
            break;
        default:
            break;
        }
    }
}
