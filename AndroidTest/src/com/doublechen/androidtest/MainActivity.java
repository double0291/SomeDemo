package com.doublechen.androidtest;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.doublechen.androidtest.util.ZipUtil;

public class MainActivity extends Activity implements OnClickListener {
	public static final String TAG = "AndroidTest";

	private Button mBtn;
	private TextView mTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mBtn = (Button) findViewById(R.id.button);
		mBtn.setOnClickListener(this);

		mTextView = (TextView) findViewById(R.id.textView);
		mTextView.setText(hello());
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

	public native String hello();

	static {
		System.loadLibrary("Hello");
	}

}
