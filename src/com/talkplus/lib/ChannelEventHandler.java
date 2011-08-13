package com.talkplus.lib;

import com.talkplus.app.ChatMessage;

public interface ChannelEventHandler {
	public void onOpen();
	public void onMessage(ChatMessage message);
	public void onClose();
}
