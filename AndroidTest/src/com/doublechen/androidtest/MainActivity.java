package com.doublechen.androidtest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	public static final String TAG = "AndroidTest";

	private static final int BUFFER_SIZE = 1024;

	private Button mBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mBtn = (Button) findViewById(R.id.button);
		mBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button:
			String dir = Environment.getExternalStorageDirectory().getPath() + File.separator + "Music"
					+ File.separator;
			unzip(dir + "pic.zip", dir + "pic");
			break;

		default:
			break;
		}
	}

	public void unzip(String zipFile, String destination) {
		try {
			File dir = new File(destination);
			if (!dir.isDirectory()) {
				dir.mkdir();
			}

			ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
			try {
				ZipEntry ze = null;
				while ((ze = zin.getNextEntry()) != null) {
					String path = destination + File.separator + ze.getName();

					if (ze.isDirectory()) {
						File unzipFile = new File(path);
						if (!unzipFile.isDirectory()) {
							unzipFile.mkdirs();
						}
					} else {
						BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(path, false),
								BUFFER_SIZE);
						try {
							int size;
							byte[] buffer = new byte[BUFFER_SIZE];

							while ((size = zin.read(buffer, 0, BUFFER_SIZE)) != -1) {
								out.write(buffer, 0, size);
							}
							zin.closeEntry();
						} finally {
							out.flush();
							out.close();
						}
					}
				}
			} finally {
				zin.close();
			}
			Log.d(TAG, "unzip success");
		} catch (IOException e) {
			Log.d(TAG, "unzip fail");
		}
	}

}
