package com.doublechen.androidtest.web;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;

public class CustomWebView extends WebView {
	Context mContext;

	boolean isPaused = true;
	boolean isDestroyed = false;

	public CustomWebView(Context context) {
		super(context);
		init(context);
	}

	public CustomWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CustomWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			this.removeJavascriptInterface("searchBoxJavaBridge_");
		}
	}

	public void loadUrlOriginal(String url) {
		if (isDestroyed) {
			return;
		}
		super.loadUrl(url);
	}

	@Override
	public void onPause() {
		isPaused = true;
		super.onPause();
	}

	@Override
	public void onResume() {
		isPaused = false;
		super.onResume();
	}
}
