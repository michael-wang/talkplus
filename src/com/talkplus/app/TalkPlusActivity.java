package com.talkplus.app;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class TalkPlusActivity extends ListActivity {
    
	private static final String TAG = "talkplus";
	
	private ChannelAdapter adapter;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.chat_list);
		
		adapter = new ChannelAdapter(this);
		getListView().setAdapter(adapter);
		
		// test only
		onReceiveChannelList(new Channel("test", "1"));
	}
	
	private void onReceiveChannelList(Channel channel) {
		adapter.add(channel);
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
}