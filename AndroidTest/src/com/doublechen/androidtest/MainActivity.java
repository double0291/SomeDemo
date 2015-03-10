package com.doublechen.androidtest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.http.protocol.HTTP;

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
			unzip2(dir + "pic.zip", dir + "pic");
			break;

		default:
			break;
		}
	}

	/**
	 * 利用ZipInputStream解压
	 * 
	 * @param zipFilePath
	 * @param destination
	 */
	public void unzip(String zipFilePath, String destination) {
		try {
			File dir = new File(destination);
			if (!dir.isDirectory()) {
				dir.mkdir();
			}

			ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFilePath));
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
							// 不用buffer，一个一个字节读的话，非常非常慢
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

	/**
	 * 利用ZipFile解压
	 * 
	 * @param zipFilePath
	 * @param destination
	 */
	public void unzip2(String zipFilePath, String destination) {
		try {
			File dir = new File(destination);
			if (!dir.isDirectory()) {
				dir.mkdir();
			}

			ZipFile zipFile = new ZipFile(zipFilePath);

			for (Enumeration<?> entries = zipFile.entries(); entries.hasMoreElements();) {
				ZipEntry entry = (ZipEntry) entries.nextElement();

				String path = destination + File.separator + entry.getName();

				if (entry.isDirectory()) {
					File entryFile = new File(path);
					if (!entryFile.exists()) {
						entryFile.mkdirs();
					}
				} else {
					InputStream in = zipFile.getInputStream(entry);
					String str = new String(path.getBytes("8859_1"), HTTP.UTF_8);
					File desFile = new File(str);

					if (!desFile.exists()) {
						File fileParentDir = desFile.getParentFile();
						if (!fileParentDir.exists()) {
							fileParentDir.mkdir();
						}
						desFile.createNewFile();
					}

					OutputStream out = new FileOutputStream(desFile);
					try {
						byte[] buffer = new byte[BUFFER_SIZE];
						int size;
						while ((size = in.read(buffer)) > 0) {
							out.write(buffer, 0, size);
						}
					} finally {
						if (in != null)
							in.close();
						out.close();
					}
				}
			}

			Log.d(TAG, "unzip success");
		} catch (IOException e) {
			Log.d(TAG, "unzip fail");
		}
	}

}
