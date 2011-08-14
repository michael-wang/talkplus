package com.talkplus.app;

public class ChatMessage {

	public enum MessageType {
		NORMAL,
		USER_JOINED,
		USER_LEFT
	}
	public MessageType type;
	public int iconRes;
	public String name;
	public String message;
	public ChatMessage() {
	}
	
	public ChatMessage(int icon, String name, String msg) {
		this(MessageType.NORMAL, icon, name, msg);
	}
	
	public ChatMessage(MessageType type, int icon, String name, String msg) {
		this.type = type;
		iconRes = icon;
		this.name = name;
		message = msg;
	}
	
	@Override
	public String toString() {
		return "ChatMessage name:" + name + ",msg:" + message;
	}
	
}
