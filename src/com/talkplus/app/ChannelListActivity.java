package com.talkplus.app;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.talkplus.util.Callback;

public class ChannelListActivity extends ListActivity {
    
	private static final String TAG = "talkplus";
	
	private ChannelAdapter adapter;
	private LoaderThread loader = new LoaderThread();
	private ChannelListCallback channelCallback = new ChannelListCallback();
	private ProgressDialog progressDlg;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_list);
		if (isNetworkAvailable()){
			onPreExecute();		  
		}
		else{
			new AlertDialog.Builder(ChannelListActivity.this)
	        .setTitle("Connection error!")
	        .setMessage("There is no Internet connection!")
	        .setCancelable(true)
	        .setNeutralButton("Close", null)
	        .show();
		}
		
		
		adapter = new ChannelAdapter(this);
		getListView().setAdapter(adapter);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		loader.getChannelList(channelCallback);
	}
	
	private void onReceiveChannelList(List<Channel> channels) {
		adapter.reset(channels);
		//To close progressDlg
		runOnUiThread(new Runnable() {
		    public void run() {
		        progressDlg.dismiss();
		    }
		});
		
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Channel c = (Channel)adapter.getItem(position);
		gotoChannelRoom(c);
	}
	
	private void gotoChannelRoom(Channel channel) {
		Log.d(TAG, "gotoChannelRoom channel:" + channel);
		Intent invokeChannelRoom = new Intent(Intent.ACTION_VIEW);
		invokeChannelRoom.setClass(this, ChatActivity.class);
		invokeChannelRoom.putExtra(ChatActivity.INTENT_EXTRA_CHANNEL_NAME, channel.name);
		invokeChannelRoom.putExtra(ChatActivity.INTENT_EXTRA_CHANNEL_ID, channel.id);
		invokeChannelRoom.putExtra(ChatActivity.INTENT_EXTRA_CHANNEL_DESCRIPTION, channel.description);
		this.startActivity(invokeChannelRoom);
	}
	
	private class ChannelListCallback implements Callback<ArrayList<Channel>> {

		@Override
		public void ready(final ArrayList<Channel> result) {
			Log.d(TAG, "activity ready channel list:" + result);
			ChannelListActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					onReceiveChannelList(result);
				}
				
			});
		}
		
	}
	protected void onPreExecute() {		
		progressDlg = new ProgressDialog(ChannelListActivity.this);
		progressDlg.setCancelable(false);
		progressDlg.setMessage("Connecting to TalkPlus server...");			  
		
		
		progressDlg.setIndeterminate(true);
		progressDlg.show();				
	}//end method
	
    public static boolean isNetworkAvailable() {
    	boolean responded = false;
    	HttpGet requestForTest = new HttpGet("http://m.google.com");
    	try {
    		new DefaultHttpClient().execute(requestForTest); // can last...
            responded = true;
            
    	} catch (Exception e) {}
     	return responded;
    }
	
}