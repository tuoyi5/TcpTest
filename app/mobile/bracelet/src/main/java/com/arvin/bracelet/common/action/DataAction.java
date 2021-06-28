package com.arvin.bracelet.common.action;

import android.content.Context;

import com.arvin.bracelet.common.manager.DataManager;


public class DataAction <T extends DataManager> {

	private Context mContext;
	public T dataManager;

	public DataAction(Context context, T dataManager) {
		this.mContext = context;
		this.dataManager = dataManager;
	}

	public Context getContext() {
		return mContext;
	}

	public T getDataManager() {
		return dataManager;
	}

	public void setDataManager(T dataManager) {
		this.dataManager = dataManager;
	}
}
