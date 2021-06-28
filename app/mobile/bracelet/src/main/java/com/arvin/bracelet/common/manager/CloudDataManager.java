package com.arvin.bracelet.common.manager;

import org.greenrobot.eventbus.EventBus;

public class CloudDataManager extends DataManager{
	public EventBus eventbus;

	public CloudDataManager(EventBus eventbus) {
		this.eventbus = eventbus;
	}

	public EventBus getEventbus() {
		return eventbus;
	}

	public void setEventbus(EventBus eventbus) {
		this.eventbus = eventbus;
	}
}



