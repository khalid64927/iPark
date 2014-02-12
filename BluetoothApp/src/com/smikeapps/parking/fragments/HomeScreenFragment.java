package com.smikeapps.parking.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smikeapps.parking.R;

public class HomeScreenFragment extends Fragment{

	
private View mRoootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRoootView = inflater.inflate(R.layout.activity_home_screen, null);
		return mRoootView;
	}
}
