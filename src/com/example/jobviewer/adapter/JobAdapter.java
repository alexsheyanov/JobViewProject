package com.example.jobviewer.adapter;

import java.util.ArrayList;

import com.example.jobviewer.model.Job;

import com.example.jobviewer.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class JobAdapter	extends BaseAdapter {
	
	private Context cont;
	private LayoutInflater linflater;
	private ArrayList<Job> vacations;
	private final int GRAY = 1;
	private final int WHITE = 2;
	
	public JobAdapter(Context context,ArrayList<Job> jobs){
		cont = context;
		vacations = jobs;
		linflater = (LayoutInflater) cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return vacations.size();
	}

	@Override
	public Object getItem(int position) {
		return vacations.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public int getViewTypeCount(){
		return 1;
	}
	@Override
	public int getItemViewType(int position){
		if(position %2 == 0){
			return WHITE;
		}else{
			return GRAY;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if(view == null){
			view = linflater.inflate(R.layout.item, parent, false);
		}
		
		Job job = vacations.get(position);
		
		switch(getItemViewType(position)){
			case WHITE:
				((TextView) view.findViewById(R.id.tv_title)).setText(job.getTitle());
				((TextView) view.findViewById(R.id.tv_title)).setBackgroundColor(Color.WHITE);
				((TextView) view.findViewById(R.id.tv_date)).setText(job.getDate());
				((TextView) view.findViewById(R.id.tv_date)).setBackgroundColor(Color.WHITE);
				((TextView) view.findViewById(R.id.tv_description)).setText(job.getDescription());
				((TextView) view.findViewById(R.id.tv_description)).setBackgroundColor(Color.WHITE);
			break;
		case GRAY:
			((TextView) view.findViewById(R.id.tv_title)).setText(job.getTitle());
			((TextView) view.findViewById(R.id.tv_title)).setBackgroundColor(Color.LTGRAY);
			((TextView) view.findViewById(R.id.tv_date)).setText(job.getDate());
			((TextView) view.findViewById(R.id.tv_date)).setBackgroundColor(Color.LTGRAY);
			((TextView) view.findViewById(R.id.tv_description)).setText(job.getDescription());
			((TextView) view.findViewById(R.id.tv_description)).setBackgroundColor(Color.LTGRAY);
			break;
		}
		
		return view;
	}		
}
