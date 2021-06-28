package com.arvin.bracelet.common.request;

import android.util.Log;

import com.arvin.bracelet.common.manager.CloudDataManager;
import com.arvin.bracelet.common.manager.DataManager;
import com.arvin.bracelet.common.socket.connect.client.RxClientService;
import com.arvin.bracelet.common.socket.connect.server.RxServerService;
import com.arvin.bracelet.common.socket.data.ConnectionModel;

public class NetworkServerRequest extends BaseDataRequest {


    RxServerService mRxServerService;
    int port = 8888;

    public <T extends DataManager> NetworkServerRequest(CloudDataManager dataManager) {
        super(dataManager);

    }

    public String getIdentifier() {
        return "server";
    }

    public RxServerService getRxServerService() {
        return mRxServerService;
    }

    @Override
    public void execute() throws Exception {
        Log.d("arvinn", "NetworkServerRequest execute");
        mRxServerService = new RxServerService(port);
        mRxServerService.start();
    }
}
