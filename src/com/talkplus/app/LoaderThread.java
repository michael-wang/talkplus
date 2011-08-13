package com.talkplus.app;

import java.util.ArrayList;

import org.json.JSONException;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.talkplus.lib.ChannelClient;
import com.talkplus.lib.ChannelEventHandler;
import com.talkplus.lib.Client;
import com.talkplus.util.Callback;

import de.roderick.weberknecht.WebSocketException;


public class LoaderThread {
	private static final String TAG = "talkplus";
	
	private static final int MSG_GET_CHANNEL_LIST = 0;
	private static final int MSG_CONNECT = 1;
	private static final int MSG_JOIN = 2;
	private static final int MSG_POST = 3;
	private static final int MSG_CLOSE = 4;
	
	private HandlerThread thread = new HandlerThread("loader");
	private Handler handler;
	public LoaderThread() {
		thread.start();
		while(thread.getLooper() == null)
			;
		
		handler = new Handler(thread.getLooper()) {

			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				switch(msg.what) {
				case MSG_GET_CHANNEL_LIST:
					com.talkplus.util.Callback< ArrayList<Channel> > callback = 
						(com.talkplus.util.Callback< ArrayList<Channel> >)msg.obj;
					Log.d(TAG, "loader handleMessage msg:" + msg);
					Client.getRecent(callback);
					Log.d(TAG, "loader handleMessage Client.getRecent returns");
					break;
				case MSG_CONNECT:
					if(channelClient == null) {
						channelClient = new ChannelClient();
					}
					Log.d(TAG, "loader before connect");
					channelClient.connect(clientEventHandler);
					Log.d(TAG, "loader after connect");
					break;
				case MSG_JOIN:
				{
					JoinParam param = (JoinParam)msg.obj;
					try {
						Log.d(TAG, "loader before join");
						channelClient.join(param.name, param.channel);
						Log.d(TAG, "loader after join");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (WebSocketException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
					break;
				case MSG_POST:
					try {
						channelClient.message((String)msg.obj);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (WebSocketException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
				switch(msg.what) {
				case MSG_GET_CHANNEL_LIST:
					com.talkplus.util.Callback< ArrayList<Channel> > callback = 
						(com.talkplus.util.Callback< ArrayList<Channel> >)msg.obj;
					Log.d(TAG, "loader handleMessage msg:" + msg);
					Client.getRecent(callback);
					Log.d(TAG, "loader handleMessage Client.getRecent returns");
					break;
				case MSG_CONNECT:
					if(channelClient == null) {
						channelClient = new ChannelClient();
					}
					Log.d(TAG, "loader before connect");
					channelClient.connect(clientEventHandler);
					Log.d(TAG, "loader after connect");
					break;
				case MSG_JOIN:
				{
					JoinParam param = (JoinParam)msg.obj;
					try {
						Log.d(TAG, "loader before join");
						channelClient.join(param.name, param.channel);
						Log.d(TAG, "loader after join");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (WebSocketException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
					break;
				case MSG_POST:
					try {
						channelClient.message((String)msg.obj);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (WebSocketException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
			
		};
	}
	
	public void getChannelList(Callback< ArrayList<Channel> > callback) {
		handler.sendMessage(handler.obtainMessage(MSG_GET_CHANNEL_LIST, callback));
	}
	
	private ChannelClient channelClient;
	private ChannelEventHandler clientEventHandler;
	class JoinParam {
		public String name;
		public int channel;
		public JoinParam(String n, int c) {
			name = n;
			channel = c;
		}
	}
	public void enteringChatRoom(String name, Channel channel, ChannelEventHandler clientHandler) {
		clientEventHandler = clientHandler;
		handler.sendMessage(handler.obtainMessage(MSG_CONNECT, channel));
		handler.sendMessage(handler.obtainMessage(MSG_JOIN, new JoinParam(name, Integer.parseInt(channel.id))));
	}
	
	public void postMessage(String name, String msg) {
		handler.sendMessage(handler.obtainMessage(MSG_POST, msg));
	}
	
	public void leaveChatRoom() {
		try {
			channelClient.close();
		} catch (WebSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
