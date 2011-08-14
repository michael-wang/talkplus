package com.talkplus.app;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter {

	private static final String TAG = "talkplus";
	private List<ChatMessage> messages = new ArrayList<ChatMessage>();
	private LayoutInflater inflater;
	public MessageAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}
	
	public void add(ChatMessage msg) {
		Log.d(TAG, "adapter msg:" + msg);
		messages.add(msg);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return messages.size();
	}

	@Override
	public Object getItem(int position) {
		return messages.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder = null;
		if(view == null) {
			view = inflater.inflate(R.layout.chat_message, null);
			holder = new ViewHolder();
			holder.icon = (ImageView)view.findViewById(R.id.icon);
			holder.name = (TextView)view.findViewById(R.id.name);
			holder.message = (TextView)view.findViewById(R.id.message);
			view.setTag(holder);
		} else {
			holder = (ViewHolder)view.getTag();
		}
		
		ChatMessage m = messages.get(position);
		if(m != null) {
			Log.d(TAG, "adapter getView position:" + position + ",holder:" + holder);
			holder.icon.setImageResource(R.drawable.user);
			holder.name.setText(m.name);
			holder.message.setText(m.message);
		}
		return view;
	}
	
	private class ViewHolder {
		ImageView icon;
		TextView name;
		TextView message;
		@Override
		public String toString() {
			return "ViewHolder icon:" + icon + ",name:" + name + ",msg:" + message;
		}
		
	}

}
