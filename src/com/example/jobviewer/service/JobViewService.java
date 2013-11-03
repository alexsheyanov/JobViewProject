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

import com.example.jobviewer.MainActivity;
import com.example.jobviewer.model.Job;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class JobViewService extends Service{
	
	private String urlString = "http://www.healthitjobs.com/hit.healthitjobs.services_deploy/jobprocessservice.svc/searchjobs?start=0&len=20&q=0:0:0:0:0:0:0:0";
	@SuppressWarnings("unused")
	private ArrayList<Job> jobItems = null;
	private InputStream inputStream = null;
	private HttpURLConnection httpUrl = null;
	private BufferedReader bufReader = null;
	private Thread parseThread = null;

	@Override
	public IBinder onBind(Intent intent) {	
		return null;
	}
	@Override
	public void onCreate(){
		super.onCreate();
		parseThread = new Thread(new Runnable(){
			public void run() {
				try {
				Log.d("MyDebug","parseThread start");
				jobItems = parseJSONString(loadJSONString(getHttpConnection()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	@Override
	public int onStartCommand(Intent intent,int flags, int startId){
		parseThread.start();
		return START_STICKY;
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onDestroy(){
		super.onDestroy();
		Log.d("MyDebug", "Destroy service");
		try{
			if(parseThread.isAlive())
				parseThread.stop();
			if(httpUrl != null)
				httpUrl.disconnect();
			if(bufReader != null)
				bufReader.close();
			if(inputStream != null)
			inputStream.close();
		}catch(IOException exc){
			exc.printStackTrace();
		}
	}
	
	private InputStream getHttpConnection(){
		try {
			URL connUrl = new URL(urlString);
			httpUrl = (HttpURLConnection) connUrl.openConnection();
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
		bufReader = new BufferedReader(new InputStreamReader(inStream));
		StringBuilder strBuilder = new StringBuilder();
		String line = null;
			try {
				while((line = bufReader.readLine()) != null){
					strBuilder.append(line);
				}
				return strBuilder.toString();
			} catch (IOException exc) {
				Log.d("MyDebug", "GetJSONString Exception");
				exc.printStackTrace();
			}
		return null;
	}
	private ArrayList<Job> parseJSONString(String JSONString){
		ArrayList<Job> jobItems = new ArrayList<Job>();
		try {
			JSONObject jObject = new JSONObject(JSONString);
			JSONArray jArray = jObject.getJSONArray("Jobs");
				for(int i = 0;i<jArray.length();i++){
					jobItems.add(convertData(jArray.getJSONObject(i)));
				}
				
				Intent in = new Intent(MainActivity.BROADCAST_ACTION);
					in.putExtra("Status", 1);
					in.putParcelableArrayListExtra("Jobs", jobItems);
					sendBroadcast(in);
					
				return jobItems;
		} catch (JSONException exc) {
			Log.d("MyDebug","JSONException");
			exc.printStackTrace();
		}
		return null;
	}
	private Job convertData(JSONObject jObject) throws JSONException{
		String title = jObject.getString("Title");
		String date = jObject.getString("ExpiresDate");
		String description = jObject.getString("Description");
		return new Job(title,date,description);
	}

}
