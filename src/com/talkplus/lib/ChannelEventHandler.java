package com.talkplus.lib;

import org.json.JSONObject;

import com.talkplus.app.ChatMessage;
import com.talkplus.app.ControlMessage;

public interface ChannelEventHandler {
	public void onOpen();
	public void onMessage(ChatMessage message);
	public void onControl(ControlMessage msg);
	public void onClose();
}
