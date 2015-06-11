package com.doublechen.androidtest.ui;

import android.os.Bundle;

import com.doublechen.androidtest.R;
import com.doublechen.androidtest.base.BaseActivity;
import com.doublechen.androidtest.web.CustomWebView;

public class WebViewActivity extends BaseActivity {
	CustomWebView mWebView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);

		mWebView = (CustomWebView) findViewById(R.id.webView);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrlOriginal("http://www.baidu.com");
	}
}
