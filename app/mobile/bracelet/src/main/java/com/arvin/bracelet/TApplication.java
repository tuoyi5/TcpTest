package com.arvin.bracelet;

import android.app.Application;
import android.content.Context;

public class TApplication extends Application {

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		StartApplicationUtils.checkDexInstall(base);
	}
}
