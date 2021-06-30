package com.arvin.bracelet.service;

import com.arvin.bracelet.service.ICallBackInterface;

interface ICallInterface {
    	boolean openLink();

    	boolean reLink();

    	boolean closeLink();

    	boolean heartbeat();

    	boolean sendMessage(String value);

    	boolean isLink();

    	boolean registerCallBack(String name, ICallBackInterface callback);

    	boolean unregisterCallBack(String name, ICallBackInterface callback);
}