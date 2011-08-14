package com.talkplus.app;

import org.json.JSONObject;

public class Channel {

	public String name;
	public String id;
	public String description;
//	public int users_count;
	
	public Channel(JSONObject obj) {
		this(obj.optString("name"), obj.optString("id"), obj.optString("description"));
//		if(obj.optInt("users_count", -1) != -1) {
//			this.users_count = obj.optInt("users_count");
//		}
	}
	
	public Channel(String name, String id, String description) {
		this.name = name;
		this.id = id;
		this.description = description;
	}
	@Override
	public String toString() {
		return "Channel name:" + name + ",id:" + id;
	}
	
}
