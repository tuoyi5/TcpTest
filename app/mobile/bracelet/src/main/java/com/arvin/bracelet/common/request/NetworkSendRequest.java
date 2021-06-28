package com.arvin.bracelet.common.request;

import com.arvin.bracelet.common.manager.CloudDataManager;
import com.arvin.bracelet.common.manager.DataManager;
import com.arvin.bracelet.common.socket.connect.client.RxClientService;

public class NetworkSendRequest extends BaseDataRequest {

    RxClientService mRxClientService;
    String  msg;
    boolean isSendSuccess;

    public <T extends DataManager> NetworkSendRequest(CloudDataManager dataManager) {
        super(dataManager);
    }

    public String getIdentifier() {
        return "send";
    }

    public NetworkSendRequest setRxClientService(RxClientService rxClientService) {
        this.mRxClientService = rxClientService;
        return this;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSendSuccess() {
        return isSendSuccess;
    }

    @Override
    public void execute() throws Exception {
        isSendSuccess =  mRxClientService.send(msg);
    }
}
