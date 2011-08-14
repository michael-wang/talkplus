package com.talkplus.app;

import org.json.JSONObject;

public class ControlMessage {

	public String name;
	public String message;
	public int users_count;
	public ControlMessage(){}
	
	public ControlMessage(JSONObject obj) {
		this(obj.optString("user"), obj.optString("message"));
		this.users_count = obj.optInt("users_count");
	}
	
	public ControlMessage(String name, String msg) {
		this.name = name;
		message = msg;
	}
	@Override
	public String toString() {
		return "ChatMessage name:" + name + ",msg:" + message + ",users_count:" + users_count;
	}
	
}
