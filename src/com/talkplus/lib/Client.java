package com.talkplus.lib;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.talkplus.app.Channel;
import com.talkplus.util.Callback;

public class Client {
	public static void getRecent(Callback<ArrayList<Channel>> ready){
		ArrayList<Channel> list = new ArrayList<Channel>();
		try {
			JSONArray result = new JSONArray(get("http://www.talkpl.us/channels/recent.json"));
			for(int i=0; i<result.length(); i++) {
				JSONObject obj = result.getJSONObject(i);
				list.add(new Channel(obj.optString("name"), obj.optString("id")));
			}
			ready.call(list);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String get(String url) throws ClientProtocolException, IOException {
		HttpClient httpclient = new DefaultHttpClient();
		System.out.println(url);
		HttpGet method = new HttpGet(url);
		HttpResponse resp = httpclient.execute(method);
		StatusLine statusLine = resp.getStatusLine();
		System.out.println(statusLine.toString());
		String result = EntityUtils.toString(resp.getEntity());
		switch(resp.getStatusLine().getStatusCode()){
			case HttpStatus.SC_OK:
				return result;
			default:
				throw new ClientProtocolException(resp.getStatusLine().toString() + ":" + result);
		}
	}
}
