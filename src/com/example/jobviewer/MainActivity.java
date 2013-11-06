package com.example.jobviewer;

import java.util.ArrayList;
import com.example.jobviewer.adapter.JobAdapter;
import com.example.jobviewer.model.Job;
import com.example.jobviewer.service.JobViewService;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class MainActivity extends Activity {
	
	public final static String BROADCAST_ACTION = "ru.example.jobviewer.showjobs";
	private ArrayList<Job> jobs = null;
	private ListView lv_Main;
	private static int status = 0;
	private BroadcastReceiver brRec;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		lv_Main = (ListView) findViewById(R.id.lv_Main);
		Log.d("MyDebug","onCreate");
		if(connectionStatus(this) == true){
			if(savedInstanceState == null){
				Log.d("MyDebug","SIS == null");
				startService(new Intent(this,JobViewService.class));
				status = 0;
			}
		}else{
			final Toast toast	= Toast.makeText(this,"Отсутствует подключение к интернет",Toast.LENGTH_LONG);
			toast.show();
		}
	}
	@Override
	protected void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		outState.putParcelableArrayList("Jobs",jobs);
		Log.d("MyDebug","OSIS: " + jobs);
	}
	//@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState){
		super.onRestoreInstanceState(savedInstanceState);
		jobs = savedInstanceState.getParcelableArrayList("Jobs");
		Log.d("MyDebug","ORIS: " + jobs);
	}
	@Override
	protected void onStart(){
		super.onStart();		
	}
	@Override 
	protected void onRestart(){
		super.onRestart();
	}
	@Override
	protected void onStop(){
		super.onStop();
	}
	@Override
	protected void onDestroy(){
		Log.d("MyDebug","onDestroy");
		super.onDestroy();
		stopService(new Intent(this,JobViewService.class));
	}
	@Override
	protected void onPause() {
		super.onPause();
		    Log.d("MyDebug", "onPause");
		    if(brRec != null){
				unregisterReceiver(brRec);
		    }
	}
	@Override
	protected void onResume() {
		super.onResume();
		Log.d("MyDebug","Status in onResume " + status);
		if(status == 0){
			brRec = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					final Bundle bundle = intent.getExtras().getBundle("Bundle");
					status = bundle.getInt("Status");
						if(status == 1){
							final Toast toast	= Toast.makeText(getBaseContext(),"Вакансии загружены",Toast.LENGTH_LONG);
								toast.show();
							jobs = bundle.getParcelableArrayList("Jobs");
							lv_Main.setAdapter(new JobAdapter(MainActivity.this, jobs));		
						}else{
							final Toast toast	= Toast.makeText(getBaseContext(),"Ошибка загрузки вакансий",Toast.LENGTH_LONG);
								toast.show();
						}
				}
			};
		final IntentFilter intfilter = new IntentFilter(BROADCAST_ACTION);
		registerReceiver(brRec, intfilter);
		}else{
			Log.d("MyDebug","jobs" + jobs);
			lv_Main.setAdapter(new JobAdapter(MainActivity.this, jobs));
		}
	}

	private Boolean connectionStatus(Context context){
		final ConnectivityManager conManager = 
				(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo netInfo = conManager.getActiveNetworkInfo();
		if(netInfo == null || !netInfo.isConnected()){
			return false;
		}
		if(netInfo.isRoaming()){
			return false;
		}
		return true;
	}

}
