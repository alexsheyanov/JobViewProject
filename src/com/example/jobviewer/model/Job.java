package com.example.jobviewer.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Job implements Parcelable {
	
	private String title;
	private String date;
	private String description;
	
	public String getTitle() {
		return title;
	}

	public String getDate() {
		return date;
	}

	public String getDescription() {
		return description;
	}


	public Job(String title,String date,String description){
		this.title = title;
		this.date = date;
		this.description = description;
	}

	public Job(Parcel source) {
		title = source.readString();
		date = source.readString();
		description = source.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(title);
		parcel.writeString(date);
		parcel.writeString(description);		
	}
	public static final Parcelable.Creator<Job> CREATOR = new Parcelable.Creator<Job>() {

		@Override
		public Job createFromParcel(Parcel source) {
			return new Job(source);
		}

		@Override
		public Job[] newArray(int size) {
			return new Job[size];
		}
		
	};
}
