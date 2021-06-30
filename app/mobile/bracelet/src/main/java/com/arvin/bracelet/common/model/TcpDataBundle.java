package com.arvin.bracelet.common.model;

import android.content.Context;
import android.util.Log;

import com.arvin.bracelet.common.action.NetworkClientAction;
import com.arvin.bracelet.common.action.NetworkServerAction;

public class TcpDataBundle extends BaseDataBundle {

	private NetworkClientAction mNetworkClientAction;
	private NetworkServerAction mNetworkServerAction;//Test用途.

	public TcpDataBundle(Context appContext) {
		super(appContext);
		initAction();
	}

	public void initAction() {
		if (mNetworkServerAction == null) {
			mNetworkServerAction = new NetworkServerAction(getAppContext(), getCloudDataManager());
			mNetworkServerAction.execute();
		}

		if (mNetworkClientAction == null) {
			mNetworkClientAction = new NetworkClientAction(getAppContext(), getCloudDataManager());
		}
	}

	public boolean openLink() {
		return false;
	}

	public boolean reLink() {
		Log.w("arvinn", "mNetworkClientAction == null : " + (mNetworkClientAction == null));
		mNetworkClientAction.execute();
		return false;
	}

	public boolean closeLink (){

		return false;
	}

	public boolean heartbeat () {

		return false;
	}

	public boolean isStartLink() {
		return mNetworkClientAction.isStartLink();
	}

	public boolean sendMessage(Object object) {
		mNetworkClientAction.sendManager((String) object);
		return false;
	}


}
