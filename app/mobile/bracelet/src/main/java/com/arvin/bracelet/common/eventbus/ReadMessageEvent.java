package com.arvin.bracelet.common.eventbus;

public class ReadMessageEvent {

	String message;

	public ReadMessageEvent (String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
