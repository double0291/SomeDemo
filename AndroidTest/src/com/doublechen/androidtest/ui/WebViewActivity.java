package com.doublechen.androidtest.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.doublechen.androidtest.R;
import com.doublechen.androidtest.base.BaseActivity;
import com.doublechen.androidtest.util.HttpUtil;
import com.doublechen.androidtest.web.CustomWebView;

public class WebViewActivity extends BaseActivity {
	CustomWebView mWebView;
	private WebViewClient mWebViewClient;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);

		mWebView = (CustomWebView) findViewById(R.id.webView);
		initWebView(mWebView);
		mWebView.loadUrlOriginal("http://www.baidu.com");
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView(WebView webView) {
		if (mWebViewClient == null) {
			mWebViewClient = new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					/* 返回true则系统WebView处理URL跳转，返回false则本WebView处理 */
					if (TextUtils.isEmpty(url) || "about:blank;".equals(url) || "about:blank".equals(url)) {
						return true;
					}

					return false;
				}
			};
		}
		webView.setWebViewClient(mWebViewClient);

		WebSettings webSettings = mWebView.getSettings();
		/* 设置UA */
		String userAgent = webSettings.getUserAgentString();
		// 网络类型
		String netWorkType = "";
		switch (HttpUtil.getNetWorkType()) {
		case NONE:
			netWorkType = " NetType/NONE";
			break;
		case WIFI:
			netWorkType = " NetType/WIFI";
			break;
		case TWO_G:
			netWorkType = " NetType/2G";
			break;
		case THREE_G:
			netWorkType = " NetType/3G";
			break;
		case FOUR_G:
			netWorkType = " NetType/4G";
			break;
		case UNKNOWN:
			netWorkType = " NetType/UNKNOWN";
			break;
		}

		StringBuilder ua = new StringBuilder(userAgent == null ? "" : userAgent);
		ua.append(netWorkType);

		webSettings.setUserAgentString(ua.toString());
		// 是否允许页面执行js脚本，注意不能去掉
		webSettings.setJavaScriptEnabled(true);
		// 设置允许访问本地路径
		webSettings.setAllowContentAccess(true);
		webSettings.setLoadWithOverviewMode(true);
	}
}
