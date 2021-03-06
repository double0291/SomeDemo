package com.doublechen.androidtest.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.*;
import com.doublechen.androidtest.R;
import com.doublechen.androidtest.util.ZipUtil;
import com.jni.ImageBlur;

import java.io.File;

public class DrawableFragment extends Fragment implements View.OnClickListener {
    private final String DOWNSCALE_FILTER = "downscale_filter";

    private ImageView image;
    private TextView text;
    private CheckBox downScale;
    private TextView statusText;
    private Button mBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_drawable, container, false);

        // Bundle args = getArguments();

        mBtn = (Button) rootView.findViewById(R.id.button);
        mBtn.setOnClickListener(this);

        image = (ImageView) rootView.findViewById(R.id.picture);
        text = (TextView) rootView.findViewById(R.id.text);
        image.setImageResource(R.drawable.picture);
        statusText = addStatusText((ViewGroup) rootView.findViewById(R.id.controls));
        addCheckBoxes((ViewGroup) rootView.findViewById(R.id.controls));

        if (savedInstanceState != null) {
            downScale.setChecked(savedInstanceState.getBoolean(DOWNSCALE_FILTER));
        }
        applyBlur();
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                String dir = Environment.getExternalStorageDirectory().getPath() + File.separator + "Music"
                        + File.separator;
                ZipUtil.unzip2(dir + "pic.zip", dir + "pic");
                break;

            default:
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(DOWNSCALE_FILTER, downScale.isChecked());
        super.onSaveInstanceState(outState);
    }

    private void blur(Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();
        float scaleFactor = 1;
        float radius = 20;
        if (downScale.isChecked()) {
            scaleFactor = 8;
            radius = 5;
        }

        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth() / scaleFactor),
                (int) (view.getMeasuredHeight() / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop() / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = ImageBlur.doBlur(overlay, (int) radius, true);
        view.setBackgroundDrawable(new BitmapDrawable(getResources(), overlay));
        statusText.setText(System.currentTimeMillis() - startMs + "ms");
    }

    private void applyBlur() {
        image.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                image.getViewTreeObserver().removeOnPreDrawListener(this);
                image.buildDrawingCache();

                Bitmap bmp = image.getDrawingCache();
                blur(bmp, text);
                return true;
            }
        });
    }

    private void addCheckBoxes(ViewGroup container) {
        downScale = new CheckBox(this.getActivity());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        downScale.setLayoutParams(lp);
        downScale.setText("Compressed Image");
        downScale.setVisibility(View.VISIBLE);
        downScale.setTextColor(0xFFFFFFFF);
        downScale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                applyBlur();
            }
        });
        container.addView(downScale);
    }

    private TextView addStatusText(ViewGroup container) {
        TextView result = new TextView(this.getActivity());
        result.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        result.setTextColor(0xFFFFFFFF);
        container.addView(result);
        return result;
    }
}
