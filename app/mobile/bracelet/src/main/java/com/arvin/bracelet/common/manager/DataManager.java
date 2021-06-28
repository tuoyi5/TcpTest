package com.arvin.bracelet.common.manager;

import android.content.Context;

import com.arvin.bracelet.common.request.BaseDataRequest;
import com.arvin.bracelet.common.rx.RxCallback;
import com.arvin.bracelet.common.rx.manager.RxManager;
import com.arvin.bracelet.common.rx.manager.ThreadPoolHolder;


public abstract class DataManager {
	private ThreadPoolHolder threadPoolHolder = new ThreadPoolHolder();

	//单线程顺序执行线程池
	public void submit(final Context context, final BaseDataRequest request, final RxCallback callback) {
		getRxManager(context, getIdentifier(request), false).enqueue(request, callback);
	}

	//并发线程池
	public void submitToMulti(final Context context, final BaseDataRequest request, final RxCallback callback) {
		getRxManager(context, getIdentifier(request), true).enqueue(request, callback);
	}

	public RxManager getRxManager(Context context, String identifier, boolean multi) {
		return threadPoolHolder.getRxManager(context, identifier, multi);
	}

	private final String getIdentifier(final BaseDataRequest request) {
		return request.getIdentifier();
	}

}
