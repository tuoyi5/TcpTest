package com.arvin.bracelet.common.action;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.arvin.bracelet.common.eventbus.SendMessageFailedEvent;
import com.arvin.bracelet.common.manager.CloudDataManager;
import com.arvin.bracelet.common.manager.DataManager;
import com.arvin.bracelet.common.request.NetworkConnectionRequest;
import com.arvin.bracelet.common.request.NetworkSendRequest;
import com.arvin.bracelet.common.rx.RxCallback;
import com.arvin.bracelet.common.socket.connect.client.RxClientService;
import com.arvin.bracelet.service.model.DataCcache;

import androidx.annotation.NonNull;

public class NetworkClientAction<T extends DataManager> extends DataAction {

	CloudDataManager cloudDataManager;
	NetworkConnectionRequest connectionRequest;

	public NetworkClientAction(Context context, CloudDataManager cloudDataManager) {
		super(context, cloudDataManager);
		this.cloudDataManager = cloudDataManager;
	}

	public NetworkConnectionRequest getConnectionRequest() {
		if (connectionRequest == null) {
			connectionRequest = new NetworkConnectionRequest(cloudDataManager);
		}
		return connectionRequest;
	}

	public void execute() {
		Log.d("arvinn", "NetworkClientAction isStartLink: ");
		if (connectionRequest != null && connectionRequest.isLink()) {
			Log.e("arvinn", "NetworkClientAction isLink: " + connectionRequest.isLink());
			return;
		}
		Log.d("arvinn", "NetworkClientAction isLink: " + connectionRequest.isLink());
		getDataManager().submit(getContext(), getConnectionRequest(), new RxCallback() {
			@Override
			public void onNext(@NonNull Object o) {
				Log.e("arvin", "NetworkClientAction, onNext: isStartConnect: " + connectionRequest.isLink());
			}

			@Override
			public void onComplete() {
				Log.e("arvin", "NetworkClientAction, onComplete: isStartConnect: " + connectionRequest.isLink());
			}

			@Override
			public void onError(@NonNull Throwable e) {
				Log.e("arvin", "NetworkClientAction, onError");
				connectionRequest.setAbort();
				connectionRequest = null;
				Log.e("arvin", "NetworkClientAction, onError: isStartConnect: " + connectionRequest.isLink());
			}
		});
	}

	public boolean isStartLink() {
		return getConnectionRequest()  != null && getConnectionRequest().isLink();
	}

	public void sendManager(String value) {
		if (getConnectionRequest().sendMessage(value)) {
			DataCcache.addMessage(value);
			cloudDataManager.getEventbus().post(new SendMessageFailedEvent());
			Log.e("arvin", "sendManager, 发送失败.检查原因.待发送消息加入缓存");
		}
	}

}