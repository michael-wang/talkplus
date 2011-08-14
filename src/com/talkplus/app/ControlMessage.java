package com.talkplus.app;

import org.json.JSONObject;

public class ControlMessage {

	public String name;
	public String message;
	public int users_count;
	public ControlMessage(){}
	
	public ControlMessage(JSONObject obj) {
		this(obj.optString("name"), obj.optString("id"));
		this.users_count = obj.optInt("users_count");
	}
	
	public ControlMessage(String name, String msg) {
		this.name = name;
		message = msg;
	}
	@Override
	public String toString() {
		return "ChatMessage name:" + name + ",msg:" + message;
	}
	
}
