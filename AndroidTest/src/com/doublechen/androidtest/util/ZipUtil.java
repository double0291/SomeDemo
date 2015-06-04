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

import android.util.Log;

public class ZipUtil {
    private static final String TAG = Constants.TAG;
    private static final int BUFFER_SIZE = 1024;

    /**
     * unzip by ZipInputStream
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
                            // it will be quite slow without buffer
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
     * unzip by ZipFile
     *
     * @param zipFilePath path of the zip package
     * @param destination unzip to where
     */
    public static boolean unzip2(String zipFilePath, String destination) {
        try {
            File dir = new File(destination);
            if (!dir.isDirectory()) {
                dir.mkdir();
            }

            ZipFile zipFile = new ZipFile(zipFilePath);

            for (Enumeration<?> entries = zipFile.entries(); entries.hasMoreElements(); ) {
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

                    // encoding of paths in the zip package according to the package author
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
                         * if use @code while((length = csumi.read(buffer, 0, BUFFER_SIZE)) > 0) @code
						 * once there is a error file, a dead loop will occur and continually write to the file system!
						 * so we should get the file length first, and then continually decrement
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
