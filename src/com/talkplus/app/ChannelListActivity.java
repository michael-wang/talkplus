package com.talkplus.app;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
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
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.chat_list);
		
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
}