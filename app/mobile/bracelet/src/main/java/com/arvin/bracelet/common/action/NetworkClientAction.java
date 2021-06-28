package com.arvin.bracelet.common.action;

import android.content.Context;

import com.arvin.bracelet.common.manager.CloudDataManager;
import com.arvin.bracelet.common.manager.DataManager;
import com.arvin.bracelet.common.request.NetworkConnectionRequest;
import com.arvin.bracelet.common.request.NetworkSendRequest;
import com.arvin.bracelet.common.rx.RxCallback;
import com.arvin.bracelet.common.socket.connect.client.RxClientService;

import androidx.annotation.NonNull;

public class NetworkClientAction<T extends DataManager> extends DataAction {

	CloudDataManager cloudDataManager;
	RxClientService mRxClientService;
	boolean isStartConnect = false;
	public NetworkClientAction(Context context, CloudDataManager cloudDataManager) {
		super(context, cloudDataManager);
	}

	public void execute() {
		if (isStartConnect) {
			return;
		}
		cloudDataManager = (CloudDataManager)getDataManager();
		NetworkConnectionRequest connectionRequest = new NetworkConnectionRequest(cloudDataManager);
		isStartConnect = true;
		cloudDataManager.submit(getContext(), connectionRequest, new RxCallback() {
			@Override
			public void onNext(@NonNull Object o) {
				mRxClientService = connectionRequest.getRxClientService();
				sendTest(mRxClientService);
				isStartConnect = mRxClientService.isConnect();
			}
		});
	}

	public void sendTest(final RxClientService rxClientService) {
		NetworkSendRequest sendRequest = new NetworkSendRequest(cloudDataManager);
		sendRequest.setRxClientService(rxClientService);
		cloudDataManager.submitToMulti(getContext(), sendRequest, new RxCallback() {
			@Override
			public void onNext(@NonNull Object o) {
				boolean sendSuccess = sendRequest.isSendSuccess();
				android.util.Log.d("arvinn", "send success = " + sendSuccess);
			}
		});
	}
}