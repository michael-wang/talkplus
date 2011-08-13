package com.talkplus.app;

public class Channel {

	public String name;
	public String id;
	public Channel(String name, String id) {
		this.name = name;
		this.id = id;
	}
	@Override
	public String toString() {
		return "Channel name:" + name + ",id:" + id;
	}
	
}
