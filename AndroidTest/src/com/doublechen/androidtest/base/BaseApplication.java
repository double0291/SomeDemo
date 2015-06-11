package com.doublechen.androidtest.base;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;

import com.doublechen.androidtest.util.Logger;

public class BaseApplication extends Application {
	public static BaseApplication mApp;

	@Override
	public void onCreate() {
		mApp = this;
		super.onCreate();
		Logger.d(getProcessName(), true); // 每个进程启动都会走到这里
	}

	/**
	 * 获取当前进程名
	 */
	public String getProcessName() {
		List<RunningAppProcessInfo> appList = null;
		try {
			ActivityManager actMgr = (ActivityManager) mApp.getSystemService(Context.ACTIVITY_SERVICE);
			appList = actMgr.getRunningAppProcesses();
			for (RunningAppProcessInfo info : appList) {
				if (null != info && info.pid == android.os.Process.myPid()) {
					return info.processName;
				}
			}
		} catch (Exception e) {
			Logger.e("get process name error", false);
		}
		return "";
	}
}
