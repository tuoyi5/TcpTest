package com.arvin.bracelet.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.arvin.bracelet.common.eventbus.ReadMessageEvent;
import com.arvin.bracelet.common.eventbus.SendMessageFailedEvent;
import com.arvin.bracelet.common.model.TcpDataBundle;
import com.arvin.bracelet.common.utils.EventBusUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class TcpService extends Service {

	private TcpDataBundle mDataBundle;
	private RemoteCallbackList<ICallBackInterface> mCallBacks = new RemoteCallbackList<>();

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("arvinn", "TcpService, onCreate");
		initBundle();
		registerEventBus();
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		Log.d("arvinn", "TcpService, onBind");
		return new Proxy(this);
	}

	public void onDestroy() {
		super.onDestroy();
		unregisterEventBus();
		mCallBacks.kill();
	}

	public TcpDataBundle getDataBundle() {
		return mDataBundle;
	}

	public void initBundle() {
		mDataBundle = new TcpDataBundle(this);
	}

	private void registerEventBus() {
		EventBusUtils.ensureRegister(getDataBundle().getEventBus(), this);
	}

	private void unregisterEventBus() {
		EventBusUtils.ensureUnregister(getDataBundle().getEventBus(), this);
	}


	private void callBackInterface(String value) {
		ArrayList arrayList = new ArrayList<String>();
		arrayList.add(value);
		final int len = mCallBacks.beginBroadcast();
		for (int i = 0; i < len; i++) {
			try {
				mCallBacks.getBroadcastItem(i).callBack(arrayList);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		mCallBacks.finishBroadcast();
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onReadMessageEvent(ReadMessageEvent event) {
		Log.d("arvinn", "onReadMessageEvent: 主线程收到消息: " + event.getMessage());
		callBackInterface(event.getMessage());
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onSendMessageFailedEvent(SendMessageFailedEvent event) {
		if (getDataBundle().reLink()) {

		}
	}

	public final class Proxy extends ICallInterface.Stub {

		private TcpService mTcpService;

		public Proxy(TcpService tcpService) {
			mTcpService = tcpService;
		}

		@Override
		public boolean openLink() throws RemoteException {
			return mTcpService.getDataBundle().openLink();
		}

		@Override
		public boolean reLink() throws RemoteException {
			return mTcpService.getDataBundle().reLink();
		}

		@Override
		public boolean closeLink() throws RemoteException {
			return mTcpService.getDataBundle().closeLink();
		}

		@Override
		public boolean heartbeat() throws RemoteException {
			return mTcpService.getDataBundle().heartbeat();
		}

		@Override
		public boolean sendMessage(String value) throws RemoteException {
			Log.d("arvinn", "service, sendMessage: " + value);
			if (!mTcpService.getDataBundle().isStartLink()) {
				mTcpService.getDataBundle().reLink();
			}
			return mTcpService.getDataBundle().sendMessage(value);
		}

		@Override
		public boolean isLink() throws RemoteException {
			return mTcpService.getDataBundle().isStartLink();
		}

		@Override
		public boolean registerCallBack(String name, ICallBackInterface callback) throws RemoteException {
			mCallBacks.register(callback);
			return false;
		}

		@Override
		public boolean unregisterCallBack(String name, ICallBackInterface callback) throws RemoteException {
			mCallBacks.unregister(callback);
			return false;
		}
	}
}
