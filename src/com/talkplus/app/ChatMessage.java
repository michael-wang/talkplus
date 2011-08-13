package com.talkplus.app;

public class ChatMessage {

	public int iconRes;
	public String name;
	public String message;
	public ChatMessage(){}
	public ChatMessage(int icon, String name, String msg) {
		iconRes = icon;
		this.name = name;
		message = msg;
	}
	@Override
	public String toString() {
		return "ChatMessage name:" + name + ",msg:" + message;
	}
	
}
