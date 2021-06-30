package com.arvin.bracelet;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.arvin.bracelet.common.utils.ResManager;
import com.arvin.bracelet.common.utils.StartApplicationUtils;
import com.squareup.leakcanary.LeakCanary;

public class TApplication extends Application {

	private static TApplication instance;
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		StartApplicationUtils.checkDexInstall(base);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		if (StartApplicationUtils.isMimiProcess(getApplicationContext())) {
			return;
		}

		if (initLeakCanary()) {
			Log.i(getClass().getSimpleName(), "onCreate: initLeakCanary return");
			return;
		}

		if (isMainProcess()) {
			initAppComponents();
		}
	}

	private boolean initLeakCanary() {
		if (LeakCanary.isInAnalyzerProcess(this)) {
			return true;
		}
		LeakCanary.install(this);
		return false;
	}


	private boolean isMainProcess() {
		return getApplicationContext().getPackageName().equals(getCurrentProcessName());
	}

	private String getCurrentProcessName() {
		int pid = android.os.Process.myPid();
		String processName = "";
		ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		if (manager == null) {
			return processName;
		}
		for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
			if (process.pid == pid) {
				processName = process.processName;
			}
		}
		return processName;
	}

	private void initAppComponents() {
		initApp();
		ResManager.init(this);
	}

	private void initApp() {
		instance = this;
	}
}
