package com.talkplus.app;

import java.util.Random;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class ChatActivity extends ListActivity {
	
	public static final String INTENT_EXTRA_CHANNEL_NAME = "name";
	public static final String INTENT_EXTRA_CHANNEL_ID = "id";
	
	private static final String TAG = "talkplus";
	
	private Channel channel;
	private TextView channelName;
	private MessageAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom);
        
        final String name = getIntent().getStringExtra(INTENT_EXTRA_CHANNEL_NAME);
        final String id = getIntent().getStringExtra(INTENT_EXTRA_CHANNEL_ID);
        Log.d(TAG, "name:" + name + ",id:" + id);
        channel = new Channel(name, id);
        
        adapter = new MessageAdapter(this);
        getListView().setAdapter(adapter);
        
        channelName = (TextView)findViewById(R.id.channel_name);
        channelName.setText(channel.name);
        
        handler.sendEmptyMessage(0);
    }
    
    private Handler handler = new Handler() {

    	Random random = new Random(System.currentTimeMillis());
		@Override
		public void handleMessage(Message msg) {
			ChatMessage chat = new ChatMessage();
			chat.iconRes = R.drawable.user;
			chat.name = names[random.nextInt(names.length)];
			chat.message = messages[random.nextInt(messages.length)];
			
			adapter.add(chat);
			
			this.sendEmptyMessageDelayed(0, 1000);
		}
    	
    };
    final String[] names = new String[] {"Chen", "Wang", "Lin", "Joe", "Michael"};
    final String[] messages = new String[] {
    		"Startup Weekend is a global network of passionate leaders and entrepreneurs on a mission to inspire, educate, and empower individuals, teams and communities.", 
    		"Come share ideas, form teams, and launch startups.", 
    		"Call us biased, but we think that there are dozens of reasons why you should come to a Startup Weekend!", 
    		"Local tech and startup leaders participate in Startup Weekends as mentors and judges.", 
    		"Join over 30,000 Startup Weekend alumni, all on a mission to change the world."};
}
