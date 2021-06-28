package com.arvin.bracelet.common.model;

import android.content.Context;

import com.arvin.bracelet.common.manager.CloudDataManager;
import com.arvin.bracelet.common.manager.LocalDataManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class DataBundle {

    private Context appContext;
    private CloudDataManager mCloudDataManager;
    private LocalDataManager mLocalDataManager;
    private EventBus eventBus;

    private ArrayList<Integer> entityKeys = new ArrayList<>();

    public DataBundle(Context appContext) {
        this.appContext = appContext;
        eventBus = new EventBus();
        mCloudDataManager = new CloudDataManager(eventBus);
        mLocalDataManager = new LocalDataManager(eventBus);
        initEntityKeys();
    }

    public void initEntityKeys() {
        entityKeys.add(249);
        entityKeys.add(250);
    }

    public Context getAppContext() {
        return appContext;
    }

    public void setAppContext(Context appContext) {
        this.appContext = appContext;
    }

    public CloudDataManager getCloudDataManager() {
        return mCloudDataManager;
    }

    public void setCloudDataManager(CloudDataManager cloudDataManager) {
        mCloudDataManager = cloudDataManager;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

}
