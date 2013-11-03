package com.example.jobviewer;

import java.util.ArrayList;
import com.example.jobviewer.adapter.JobAdapter;
import com.example.jobviewer.model.Job;
import com.example.jobviewer.service.JobViewService;

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
	private int status = 0;
	private JobAdapter adapter = null;
	private BroadcastReceiver brRec;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		startService(new Intent(this,JobViewService.class));
		
		lv_Main = (ListView) findViewById(R.id.lv_Main);
		
			brRec = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				status = intent.getIntExtra("Status", 0);
				jobs = intent.getParcelableArrayListExtra("Jobs");
				Log.d("MyDebug","JOBS: " + jobs);
				if(status == 1){
					Toast toast	= Toast.makeText(getBaseContext(),"Task is finish!",Toast.LENGTH_LONG);
					toast.show();
					adapter = new JobAdapter(MainActivity.this, jobs);
					lv_Main.setAdapter(adapter);
				}
			}
		};
		
		IntentFilter intfilter = new IntentFilter(BROADCAST_ACTION);
		registerReceiver(brRec, intfilter);	
	}
	@Override
	protected void onStart(){
		super.onStart();
		
	}
	@Override
	protected void onDestroy(){
		Log.d("MyDebug","Destroy");
		super.onDestroy();
		unregisterReceiver(brRec);
		stopService(new Intent(this,JobViewService.class));
	}

}
