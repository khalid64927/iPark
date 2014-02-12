package com.smikeapps.parking.fragments;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smikeapps.parking.R;
import com.smikeapps.parking.interfaces.BackButtonInterface;
import com.smikeapps.parking.tabs.HomeContainerInterface;
import com.smikeapps.parking.tabs.StackFragment;

public class CheckPVTFragment extends StackFragment implements BackButtonInterface, HomeContainerInterface{

private View mRoootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRoootView = inflater.inflate(R.layout.fragment_check_pvt, null);
		return mRoootView;
	}

	@Override
	public void setUpActionBar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFragmentSwipe() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enableMenuItems() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disableMenuItems() {
		// TODO Auto-generated method stub
		
	}
}
