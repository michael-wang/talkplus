package com.talkplus.app;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChannelAdapter extends BaseAdapter {

	private List<Channel> channels;
	private LayoutInflater inflater;
	
	public ChannelAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		channels = new ArrayList<Channel>();
	}
	
	public void reset(Channel channel) {
		channels.clear();
		channels.add(channel);
		notifyDataSetChanged();
	}
	
	public void reset(List<Channel> list) {
		channels.clear();
		channels.addAll(list);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return channels.size();
	}

	@Override
	public Object getItem(int position) {
		return channels.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		
		ViewHolder holder;
		if(view == null) {
			view = inflater.inflate(R.layout.channel, null);
			holder = new ViewHolder();
			holder.name = (TextView)view.findViewById(R.id.name);
			view.setTag(holder);
		} else {
			holder = (ViewHolder)view.getTag();
		}
		
		Channel c = (Channel)getItem(position);
		holder.name.setText(c.name);
		
		return view;
	}
	
	private class ViewHolder {
		TextView name;
	}

}
