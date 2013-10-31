package com.example.jobviewer.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class JobViewService extends Service{
	
	private String urlString = "http://www.healthitjobs.com/hit.healthitjobs.services_deploy/" +
			"jobprocessservice.svc/searchjobs?start=0&len=20&q=0:0:0:0:0:0:0:0";
	private InputStream inputStream;
	private Toast toast;

	@Override
	public IBinder onBind(Intent intent) {	
		return null;
	}
	@Override
	public void onCreate(){
		if(getHttpConnection() != null){
			inputStream = getHttpConnection();
			toast = Toast.makeText(getBaseContext(), "HttpUrlConnection - success!",Toast.LENGTH_SHORT);
			toast.show();
		}else{
			toast = Toast.makeText(getBaseContext(), "HttpUrlConnection - failed!",Toast.LENGTH_SHORT);
			toast.show();
			stopSelf();
		}
	}
	@Override
	public void onStart(Intent intent, int startId){
		if(parseJSONString(loadJSONString(inputStream)) !=null){
			toast = Toast.makeText(getBaseContext(), "parseJSON - success!",Toast.LENGTH_SHORT);
			toast.show();
		}else{
			toast = Toast.makeText(getBaseContext(), "parseJSON - Failed!!",Toast.LENGTH_SHORT);
			toast.show();
			stopSelf();
		}
	}
	@Override
	public void onDestroy(){
		
	}
	
	private InputStream getHttpConnection(){
		try {
			URL connUrl = new URL(urlString);
			HttpURLConnection httpUrl = (HttpURLConnection) connUrl.openConnection();
			httpUrl.setRequestMethod("GET");
			httpUrl.connect();
			
			return httpUrl.getInputStream();

		} catch (MalformedURLException exc) {
			Log.d("MyDebug", "URL Exception!");
			exc.printStackTrace();
		} catch (IOException exc){
			Log.d("MyDebug","IOException");
			exc.printStackTrace();
		}
		return null;
	}
	private String loadJSONString(InputStream inStream){
		BufferedReader bufReader = new BufferedReader(new InputStreamReader(inStream));
		StringBuilder strBuilder = new StringBuilder();
		String line = null;
			try {
				while((line = bufReader.readLine()) != null){
					strBuilder.append(line);
				}
				bufReader.close();
				return strBuilder.toString();
			} catch (IOException exc) {
				Log.d("MyDebug", "GetJSONString Exception");
				exc.printStackTrace();
			}
		return null;
	}
	private ArrayList<String> parseJSONString(String JSONString){
		ArrayList<String> jobItems = new ArrayList<String>();
		try {
			JSONObject jObject = new JSONObject(JSONString);
			JSONArray jArray = jObject.getJSONArray("Jobs");
				for(int i = 0;i<jArray.length();i++){
					jobItems.add(jArray.getJSONObject(i).getString("Title").toString());
				}
				return jobItems;
		} catch (JSONException exc) {
			Log.d("MyDebug","JSONException");
			exc.printStackTrace();
		}
		return null;
	}

}
