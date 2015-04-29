package com.jni;

import android.graphics.Bitmap;

public class ImageBlur {
	public static Bitmap doBlur(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {
		Bitmap bitmap;
		if (canReuseInBitmap) {
			bitmap = sentBitmap;
		} else {
			bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
		}

		if (radius < 1) {
			return (null);
		}
		// Jni BitMap
		ImageBlur.blur(bitmap, radius);

		return (bitmap);
	}

	public static native void blur(Bitmap bitmap, int r);

	static {
		System.loadLibrary("ImageBlur");
	}
}
