package com.doublechen.androidtest.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.doublechen.androidtest.base.BaseApplication;

public class HttpUtil {
	private static final int VIVO_3G = 17;

	private HttpUtil() {
	}

	/**
	 * 获取网络类型
	 * 
	 * @param context
	 * @return {@link NET_TYPE}
	 */
	public static NET_TYPE getNetWorkType() {
		ConnectivityManager cm = (ConnectivityManager) BaseApplication.mApp
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return NET_TYPE.NONE;
		}

		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo == null || !networkInfo.isConnected()) {
			return NET_TYPE.NONE;
		}

		switch (networkInfo.getType()) {
		case ConnectivityManager.TYPE_WIFI:
			return NET_TYPE.WIFI;

		case ConnectivityManager.TYPE_ETHERNET:
			return NET_TYPE.UNKNOWN;

		case ConnectivityManager.TYPE_MOBILE:
			int subType = networkInfo.getSubtype();
			switch (subType) {
			// 参考android.telephony.TelephonyManager#getNetworkClass
			case TelephonyManager.NETWORK_TYPE_GPRS:
			case TelephonyManager.NETWORK_TYPE_EDGE:
			case TelephonyManager.NETWORK_TYPE_CDMA:
			case TelephonyManager.NETWORK_TYPE_1xRTT:
			case TelephonyManager.NETWORK_TYPE_IDEN: {
				return NET_TYPE.TWO_G;
			}
			case TelephonyManager.NETWORK_TYPE_UMTS:
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
			case TelephonyManager.NETWORK_TYPE_HSDPA:
			case TelephonyManager.NETWORK_TYPE_HSUPA:
			case TelephonyManager.NETWORK_TYPE_HSPA:
			case TelephonyManager.NETWORK_TYPE_EVDO_B:
			case TelephonyManager.NETWORK_TYPE_EHRPD:
			case TelephonyManager.NETWORK_TYPE_HSPAP:
			case VIVO_3G: {
				// 17这个是VIVO手机特有的...
				return NET_TYPE.THREE_G;
			}
			case TelephonyManager.NETWORK_TYPE_LTE: {
				return NET_TYPE.FOUR_G;
			}
			default: {
				return NET_TYPE.UNKNOWN;
			}
			}

		default:
			return NET_TYPE.UNKNOWN;

		}
	}

	/**
	 * 网络类型
	 */
	public static enum NET_TYPE {
		NONE, WIFI, TWO_G, THREE_G, FOUR_G, UNKNOWN;
	}
}
