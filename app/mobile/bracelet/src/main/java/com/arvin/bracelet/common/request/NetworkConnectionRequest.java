package com.arvin.bracelet.common.request;

import com.arvin.bracelet.common.manager.CloudDataManager;
import com.arvin.bracelet.common.manager.DataManager;
import com.arvin.bracelet.common.socket.connect.client.RxClientService;
import com.arvin.bracelet.common.socket.data.ConnectionModel;

public class NetworkConnectionRequest extends BaseDataRequest {

    ConnectionModel connectionModel;
    RxClientService mRxClientService;
    int port = 8888;

    public <T extends DataManager> NetworkConnectionRequest(CloudDataManager dataManager) {
        super(dataManager);
        connectionModel = new ConnectionModel(
            "127.0.0.1",
            port, "","","",5);
    }

    public String getIdentifier() {
        return "connection";
    }

    public RxClientService getRxClientService() {
        return mRxClientService;
    }

    @Override
    public void execute() throws Exception {
        mRxClientService = new RxClientService();
        mRxClientService.setConnectionModel(connectionModel);
        mRxClientService.connectServer();
    }
}
