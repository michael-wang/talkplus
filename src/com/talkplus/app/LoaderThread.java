package com.talkplus.app;

import java.util.ArrayList;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.talkplus.lib.Client;
import com.talkplus.util.Callback;


public class LoaderThread {
	private static final String TAG = "talkplus";
	
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
				com.talkplus.util.Callback< ArrayList<Channel> > callback = 
					(com.talkplus.util.Callback< ArrayList<Channel> >)msg.obj;
				Log.d(TAG, "loader handleMessage msg:" + msg);
				Client.getRecent(callback);
				Log.d(TAG, "loader handleMessage Client.getRecent returns");
			}
			
		};
	}
	
	public void getChannelList(Callback< ArrayList<Channel> > callback) {
		handler.sendMessage(handler.obtainMessage(0, callback));
	}

}
