package com.talkplus.lib;

import java.net.URI;
import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import com.talkplus.app.ChatMessage;
import com.talkplus.app.ControlMessage;

import de.roderick.weberknecht.WebSocket;
import de.roderick.weberknecht.WebSocketConnection;
import de.roderick.weberknecht.WebSocketEventHandler;
import de.roderick.weberknecht.WebSocketException;
import de.roderick.weberknecht.WebSocketMessage;

public class ChannelClient {

	private WebSocket websocket;
	ChannelEventHandler handler;

	public ChannelClient() {
		super();
	}
	
	public void connect(ChannelEventHandler handler){
		this.handler = handler;
		try {
	        URI url = new URI("ws://api.talkpl.us/websocket");
	        websocket = new WebSocketConnection(url);
	        
	        // Register Event Handlers
	        websocket.setEventHandler(new WebSocketEventHandler() {
                public void onOpen() {
                        System.out.println("--open");
                        ChannelClient.this.handler.onOpen();
                }         
                public void onMessage(WebSocketMessage message) {
                        System.out.println("--received message: " + message.getText());
						try {
							JSONObject msg = new JSONObject(message.getText());
							String action = msg.optString("action");
							if(action.equals("message")){
	                        	ChannelClient.this.handler.onMessage(new ChatMessage(0, msg.optString("user"), msg.optString("message")));
	                        } else if(action.equals("control")){
	                        	ChannelClient.this.handler.onControl(new ControlMessage(msg));
	                        }
						} catch (JSONException e) {
							e.printStackTrace();
						}
                                   
                }
                public void onClose() {
                        System.out.println("--close");
                        ChannelClient.this.handler.onClose();
                }
	        });
	        // Establish WebSocket Connection
	        websocket.connect();
		} catch (WebSocketException wse) {
		        wse.printStackTrace();
		} catch (URISyntaxException use) {
		        use.printStackTrace();
		}
	}

	public void message(String message) throws JSONException,
			WebSocketException {
		JSONObject msg = new JSONObject()
		.put("action", "message")
		.put("message", message);
		send(msg);
	}

	public void join(String user, int channel) throws JSONException,
			WebSocketException {
		JSONObject login = new JSONObject()
			.put("action", "join")
			.put("user", user)
			.put("channel", channel);
		send(login);
	}

	private void send(JSONObject msg) throws WebSocketException {
		System.out.println(msg.toString());
		websocket.send(msg.toString());
	}
	
	public void close() throws WebSocketException{
		websocket.close();
	}
	
}
