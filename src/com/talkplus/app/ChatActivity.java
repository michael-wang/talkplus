package com.talkplus.app;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.talkplus.lib.ChannelEventHandler;

public class ChatActivity extends ListActivity {
	
	public static final String INTENT_EXTRA_CHANNEL_NAME = "name";
	public static final String INTENT_EXTRA_CHANNEL_ID = "id";
	
	private static final String TAG = "talkplus";
	
	private Channel channel;
	private TextView channelName;
	private MessageAdapter adapter;
	private LoaderThread loader;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom);
        
        Button send = (Button)findViewById(R.id.send);
        final EditText message = (EditText)findViewById(R.id.input);
        send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loader.postMessage("test", message.getText().toString());
				message.setText("");
			}
		});
        
        final String name = getIntent().getStringExtra(INTENT_EXTRA_CHANNEL_NAME);
        final String id = getIntent().getStringExtra(INTENT_EXTRA_CHANNEL_ID);
        Log.d(TAG, "name:" + name + ",id:" + id);
        channel = new Channel(name, id);
        
        adapter = new MessageAdapter(this);
        getListView().setAdapter(adapter);
        
        channelName = (TextView)findViewById(R.id.channel_name);
        channelName.setText(channel.name);
        
        loader = new LoaderThread();
        loader.enteringChatRoom("test", channel, new MyChannelEventHandler());
    }
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
		loader.leaveChatRoom();
	}
    
	private class MyChannelEventHandler implements ChannelEventHandler {

		@Override
		public void onOpen() {
			Log.w(TAG, "activity onOpen");
		}

		@Override
		public void onMessage(ChatMessage message) {
			Log.w(TAG, "activity onMessage message:" + message);
			handler.sendMessage(handler.obtainMessage(0, message));
		}

		@Override
		public void onClose() {
			Log.w(TAG, "activity onClose");
			Toast.makeText(ChatActivity.this, "Chat room closed.", Toast.LENGTH_LONG).show();
			ChatActivity.this.finish();
		}
    	
    }

    private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			ChatMessage chat = (ChatMessage)msg.obj;
			adapter.add(chat);
		}
    	
    };
    
}
