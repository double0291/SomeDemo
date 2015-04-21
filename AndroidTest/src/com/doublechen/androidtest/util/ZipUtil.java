package com.doublechen.androidtest.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import com.doublechen.androidtest.MainActivity;

import android.util.Log;

public class ZipUtil {
	private static final String TAG = MainActivity.TAG;
	private static final int BUFFER_SIZE = 1024;
	
	/**
	 * 利用ZipInputStream解压
	 * 
	 * @param zipFilePath
	 * @param destination
	 */
	public static void unzip(String zipFilePath, String destination) {
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
	 *            压缩包所在路径
	 * @param destination
	 *            解压文件夹
	 */
	public static boolean unzip2(String zipFilePath, String destination) {
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
					CheckedInputStream csumi = new CheckedInputStream(in, new CRC32());

					// 路径编码格式要跟压缩包提供方约定
					String str = new String(path.getBytes("8859_1"), "GB2312");
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
						long size = entry.getSize();
						int length;

						/*
						 * 如果用 while((length = csumi.read(buffer, 0, BUFFER_SIZE)) > 0)的话，
						 * 一旦碰到有问题的压缩包，会出现死循环，然后不停的写文件！！！
						 * 所以要先获取压缩文件大小，再去不停减
						 */
						while (size > 0) {
							length = csumi.read(buffer, 0, BUFFER_SIZE);
							out.write(buffer, 0, length);
							size -= length;
						}

						if (entry.getCrc() != csumi.getChecksum().getValue()) {
							Log.d(TAG, "unzip fail, CRC32 error, " + entry.getName());
							return false;
						}
					} finally {
						if (in != null)
							in.close();
						out.close();
					}
				}
			}

			Log.d(TAG, "unzip success");
			return true;
		} catch (IOException e) {
			Log.d(TAG, "unzip fail");
		}
		return false;
	}
}
