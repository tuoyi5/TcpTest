package com.arvin.bracelet.common.socket.data;

public class ConnectionModel {

	private String serverIP;
	private int port;

	private String hearString;//心跳数据
	private String loginString;//登陆数据
	private boolean isOnLine = false;
	private String heartAction;
	private int spacingTime = 5 * 1000; //MILLISECONDS


	public ConnectionModel(String serverip,
						   int port,
						   String login,
						   String heartStr,
						   String heartAction,
						   int spacingTime) {
		this.serverIP=serverip;
		this.port=port;
		this.hearString = login;
		this.loginString = heartStr;
		this.heartAction = heartAction;
		this.spacingTime=spacingTime;
	}

	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getHearString() {
		return hearString;
	}

	public void setHearString(String hearString) {
		this.hearString = hearString;
	}

	public String getLoginString() {
		return loginString;
	}

	public void setLoginString(String loginString) {
		this.loginString = loginString;
	}

	public boolean isOnLine() {
		return isOnLine;
	}

	public void setOnLine(boolean onLine) {
		isOnLine = onLine;
	}

	public String getHeartAction() {
		return heartAction;
	}

	public void setHeartAction(String heartAction) {
		this.heartAction = heartAction;
	}

	public int getSpacingTime() {
		return spacingTime;
	}

	public void setSpacingTime(int spacingTime) {
		this.spacingTime = spacingTime;
	}
}

