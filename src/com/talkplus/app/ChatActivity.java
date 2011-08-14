package com.talkplus.app;

import java.util.Random;

import android.accounts.Account;
import android.accounts.AccountManager;
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
	public static final String INTENT_EXTRA_CHANNEL_DESCRIPTION = "description";
	
	private static final String TAG = "talkplus";
	
	private Channel channel;
	private TextView channelName;
	private TextView userCountText;
	private String userName;
	private MessageAdapter adapter;
	private LoaderThread loader;
	private int userCount;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom);
        
        userName = getUserName();
        
        Button send = (Button)findViewById(R.id.send);
        final EditText message = (EditText)findViewById(R.id.input);
        send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loader.postMessage(userName, message.getText().toString());
				message.setText("");
			}
		});
        
        final String name = getIntent().getStringExtra(INTENT_EXTRA_CHANNEL_NAME);
        final String id = getIntent().getStringExtra(INTENT_EXTRA_CHANNEL_ID);
        final String description = getIntent().getStringExtra(INTENT_EXTRA_CHANNEL_DESCRIPTION);
        Log.d(TAG, "name:" + name + ",id:" + id + ",description:" + description);
        channel = new Channel(name, id, description);
        
        adapter = new MessageAdapter(this);
        getListView().setAdapter(adapter);
        
        channelName = (TextView)findViewById(R.id.channel_name);
        channelName.setText(channel.name);
        
        userCountText = (TextView)findViewById(R.id.user_count);
        updateUserCount(1);
        
        loader = new LoaderThread();
        loader.enteringChatRoom(userName, channel, new MyChannelEventHandler());
    }
    
    private String getUserName() {
    	Account[] googleAccounts = AccountManager.get(ChatActivity.this)
    		.getAccountsByType("com.google");
    	if(googleAccounts == null) {
    		Log.w(TAG, "no account found, gen random name!");
        	Random r = new Random();
        	return "test" + r.nextInt(1000);
    	}
    	Log.d(TAG, "account:" + googleAccounts[0]);
    	final String accountName = googleAccounts[0].name;
    	return accountName.substring(0, accountName.lastIndexOf("@"));
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
			handler.sendMessage(handler.obtainMessage(MSG_ADD_CHAT_MESSAGE, message));
		}

		@Override
		public void onClose() {
			Log.w(TAG, "activity onClose");
			handler.sendEmptyMessage(MSG_ON_CLOSE);
		}

		@Override
		public void onControl(ControlMessage msg) {
			Log.w(TAG, "activity onControl msg:" + msg);
			handler.sendMessage(handler.obtainMessage(MSG_ON_CONTROL, msg));
		}
    	
    }

	private static final int MSG_ADD_CHAT_MESSAGE = 1;
	private static final int MSG_ON_CLOSE = 2;
	private static final int MSG_ON_CONTROL = 3;
    private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case MSG_ADD_CHAT_MESSAGE:
			{
				ChatMessage chat = (ChatMessage)msg.obj;
				adapter.add(chat);
				scrollToBottom();
			}
				break;
			case MSG_ON_CLOSE:
				Toast.makeText(ChatActivity.this, "Chat room closed.", Toast.LENGTH_LONG).show();
				ChatActivity.this.finish();
				break;
			case MSG_ON_CONTROL:
			{
				ControlMessage cm = (ControlMessage)msg.obj;
				boolean left = cm.message.contains("left") ? true : false;
				ChatMessage newUser = new ChatMessage(
						left ? ChatMessage.MessageType.USER_LEFT : ChatMessage.MessageType.USER_JOINED,
						android.R.drawable.ic_dialog_alert, 
						cm.name, cm.message);
				adapter.add(newUser);
				scrollToBottom();
				
				updateUserCount(cm.users_count);
			}
				break;
			}
		}
    	
    };
    
    private void scrollToBottom() {
    	getListView().setSelection(adapter.getCount()-1);
    }
    
    private void updateUserCount(int newCount) {
    	userCount = newCount;
    	userCountText.setText("(" + userCount + " people)");
    }
}
