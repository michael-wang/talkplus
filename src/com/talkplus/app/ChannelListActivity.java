package com.talkplus.app;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.talkplus.util.Callback;

public class ChannelListActivity extends ListActivity {
    
	private static final String TAG = "talkplus";
	
	private ChannelAdapter adapter;
	private LoaderThread loader = new LoaderThread();
	private ChannelListCallback channelCallback = new ChannelListCallback();
	private ProgressDialog progressDlg;
	private TextView channelCount;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_list);
		onPreExecute();		  
		
		channelCount = (TextView)findViewById(R.id.channel_count);
		showChannelCount(0);
		
		adapter = new ChannelAdapter(this);
		getListView().setAdapter(adapter);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		loader.getChannelList(channelCallback);
	}
	
	private void onReceiveChannelList(final List<Channel> channels) {
		adapter.reset(channels);
		//To close progressDlg
		runOnUiThread(new Runnable() {
		    public void run() {
		        progressDlg.dismiss();
		        showChannelCount(channels.size());
		    }
		});
		
	}
	
	private void showChannelCount(int count) {
		channelCount.setText("#channels: " + count);
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
		progressDlg.setCancelable(true);
		progressDlg.setMessage("Connecting to TalkPlus server...");			  
		progressDlg.setIndeterminate(true);
		progressDlg.setOnCancelListener(new OnCancelListener(){
			@Override
			public void onCancel(DialogInterface dialog) {
				finish();
			}});
		progressDlg.show();				
	}//end method
	
}