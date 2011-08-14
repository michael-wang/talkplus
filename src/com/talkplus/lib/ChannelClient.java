package com.talkplus.lib;

import java.net.URI;
import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import com.talkplus.app.Channel;
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
	                public void onOpen()
	                {
	                        System.out.println("--open");
	                        ChannelClient.this.handler.onOpen();
	                }         
	                public void onMessage(WebSocketMessage message)
	                {
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
	                public void onClose()
	                {
	                        System.out.println("--close");
	                        ChannelClient.this.handler.onClose();
	                }
	        });
	        
	        // Establish WebSocket Connection
	        websocket.connect();
	        
	        // Send UTF-8 Text
//	        try {
//	        	String user = "sleepnova";
//				int ch = 1;
//				join(user, ch);
//				
//				String message = "hello!";
//				message(message);
//			
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
	        // Close WebSocket Connection
//	        websocket.close();
			}
			catch (WebSocketException wse) {
			        wse.printStackTrace();
			}
			catch (URISyntaxException use) {
			        use.printStackTrace();
			}
		}

	public void message(String message) throws JSONException,
			WebSocketException {
		JSONObject msg = new JSONObject()
		.put("action", "message")
		.put("message", message);
		message(msg);
	}

	public void join(String user, int channel) throws JSONException,
			WebSocketException {
		JSONObject login = new JSONObject()
			.put("action", "join")
			.put("user", user)
			.put("channel", channel);
		join(login);
	}

	public void message(JSONObject msg) throws WebSocketException {
		System.out.println(msg.toString());
		websocket.send(msg.toString());
	}

	public void join(JSONObject login) throws WebSocketException {
		System.out.println(login.toString());
		websocket.send(login.toString());
	}
	
	public void close() throws WebSocketException{
		websocket.close();
	}
	
}
