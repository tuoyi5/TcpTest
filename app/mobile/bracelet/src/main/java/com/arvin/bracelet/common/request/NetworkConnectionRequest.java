package com.arvin.bracelet.common.request;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.arvin.bracelet.common.eventbus.ReadMessageEvent;
import com.arvin.bracelet.common.manager.CloudDataManager;
import com.arvin.bracelet.common.manager.DataManager;
import com.arvin.bracelet.common.rx.RxCallback;
import com.arvin.bracelet.common.socket.connect.client.RxClientHandler;
import com.arvin.bracelet.common.socket.connect.client.RxClientService;
import com.arvin.bracelet.common.socket.data.ConnectionModel;

import androidx.annotation.NonNull;

public class NetworkConnectionRequest extends BaseDataRequest{

	CloudDataManager mCloudDataManager;
	ConnectionModel connectionModel;
	RxClientService mRxClientService;
	int port = 8888;
	String ip = "127.0.0.1";

	public <T extends DataManager> NetworkConnectionRequest(CloudDataManager dataManager) {
		super(dataManager);
		mCloudDataManager = (CloudDataManager)getManger();
		connectionModel = new ConnectionModel(ip, port, "", "", "", 5);
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIdentifier() {
		return "connection";
	}

	public RxClientService getRxClientService() {
		return mRxClientService;
	}

	@Override
	public void execute() throws Exception {
		Log.i("arvinn", "NetworkConnectionRequest, execute: " + ip);
		mRxClientService = new RxClientService();
		mRxClientService.setConnectionModel(connectionModel);
		mRxClientService.setConnectServer(new RxClientHandler(new RxCallback<String>() {
			@Override
			public void onNext(@NonNull String string) {
				mCloudDataManager.getEventbus().post(new ReadMessageEvent(string));
			}
		}));
		mRxClientService.connectServer();
	}

	public boolean openLink() {
		if (mRxClientService != null) {
			mRxClientService.reConnect();
			return true;
		}
		return false;
	}

	public boolean reLink() {
		if (mRxClientService != null) {
			mRxClientService.reConnect();
			return true;
		}
		return false;
	}

	public boolean closeLink() {
		if (mRxClientService != null) {
			mRxClientService.shutdownFully();
			return true;
		}
		return false;
	}

	public boolean heartbeat() {
		return false;
	}

	public boolean sendMessage(String value) {
		if (!isLink()) {
			return false;
		}
		if (mRxClientService != null) {
			return mRxClientService.sendMessage(value);
		}
		return false;
	}

	public boolean isLink() {
		if (mRxClientService != null) {
			return mRxClientService.isConnect();
		}
		return false;
	}
}
