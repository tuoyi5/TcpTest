package com.arvin.bracelet.service.model;


import java.util.ArrayList;


public class DataCcache {

	private static ArrayList<String> messageCcache = new ArrayList<>();

	public static synchronized void addMessage(String value){
		messageCcache.add(value);
	}

	public static synchronized void removeMessage() {
		messageCcache.remove(0);
	}

	public static synchronized String getFirstMessage() {
		if (messageCcache.size() > 0) {
			return messageCcache.get(0);
		}
		return null;
	}
}
