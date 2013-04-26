package com.prototype.cruise;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class EndOfDriveFragment extends Fragment{


	LinearLayout llBar1;
	LinearLayout llBar2;
	LinearLayout llBar3;
	LinearLayout llBar4;
	LinearLayout llBar5;
	LinearLayout llBar6;
	LinearLayout llBar7;
	LinearLayout llBar8;
	LinearLayout llBar9;

	Button bDrive;
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.end_of_drive, container, false);

		init(view);

		return view;
	}

	public void init(View v) {
		llBar1  = (LinearLayout) v.findViewById(R.id.bar1);
		

		
		LinearLayout ll = (LinearLayout) v.findViewById(R.id.ll_main);
		ll.setBackgroundResource(R.drawable.gradgreenyellow);
	}
}
