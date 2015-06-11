package com.doublechen.androidtest.web;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
