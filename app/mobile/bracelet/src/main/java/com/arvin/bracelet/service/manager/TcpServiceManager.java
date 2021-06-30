package com.arvin.bracelet.service.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.arvin.bracelet.service.ICallBackInterface;
import com.arvin.bracelet.service.ICallInterface;
import com.arvin.bracelet.service.TcpService;

import java.util.List;

public class TcpServiceManager {

	private ICallInterface mInterface;
	private ServiceConnection serviceConnection;
	private Context mContext;

	public TcpServiceManager(Context context) {
		mContext = context;
	}

	public void bindService(Context context) {
		Intent intent = new Intent(context, TcpService.class);
		serviceConnection =  new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName name, IBinder binder) {
				Log.e("arvin", "onServiceConnected: " + (mICallBackInterface == null));
				mInterface = ICallInterface.Stub.asInterface(binder);
				regiter(name.getClassName());
			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
				unregister(name.getClassName());
			}
		};
		context.bindService(intent, serviceConnection, context.BIND_AUTO_CREATE);
	}

	public void unbindService(Context context) {
		context.unbindService(serviceConnection);
	}

	public void regiter(String name) {
		try {
			mInterface.registerCallBack(name, mICallBackInterface);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void unregister(String name) {
		try {
			mInterface.unregisterCallBack(name, mICallBackInterface);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public com.arvin.bracelet.service.ICallBackInterface mICallBackInterface = new ICallBackInterface.Stub() {
		@Override
		public void callBack(List<String> arrayList) throws RemoteException {
			android.util.Log.i("arvinn", "UI线程收到消息: ");
			StringBuffer stringBuffer = new StringBuffer();
			for (String string : arrayList) {
				stringBuffer.append(string).append(" ; ");
			}
			android.util.Log.i("arvinn", "UI线程收到消息: " + stringBuffer.toString());
		}
	};

	public void sendMessage(String value) {
		try {
			mInterface.sendMessage(value);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
