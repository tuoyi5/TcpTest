package com.arvin.bracelet.common.action;

import android.content.Context;
import android.util.Log;

import com.arvin.bracelet.common.manager.CloudDataManager;
import com.arvin.bracelet.common.manager.DataManager;
import com.arvin.bracelet.common.request.NetworkConnectionRequest;
import com.arvin.bracelet.common.request.NetworkSendRequest;
import com.arvin.bracelet.common.request.NetworkServerRequest;
import com.arvin.bracelet.common.rx.RxCallback;
import com.arvin.bracelet.common.socket.connect.client.RxClientService;
import com.arvin.bracelet.common.socket.connect.server.RxServerService;

import androidx.annotation.NonNull;

public class NetworkServerAction<T extends DataManager> extends DataAction {

	CloudDataManager cloudDataManager;
	RxServerService mRxServerService;

	public NetworkServerAction(Context context, CloudDataManager cloudDataManager) {
		super(context, cloudDataManager);
	}

	public void execute() {
		cloudDataManager = (CloudDataManager)getDataManager();
		NetworkServerRequest request = new NetworkServerRequest(cloudDataManager);
		cloudDataManager.submit(getContext(), request, new RxCallback() {
			@Override
			public void onNext(@NonNull Object o) {
				Log.w("arvinn", "NetworkServerAction onNext");
			}
		});
	}
}
