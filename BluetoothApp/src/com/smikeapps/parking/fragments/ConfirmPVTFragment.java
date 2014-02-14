package com.smikeapps.parking.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.smikeapps.parking.R;
import com.smikeapps.parking.activities.HomeScreenActivity;
import com.smikeapps.parking.interfaces.BackButtonInterface;
import com.smikeapps.parking.tabs.HomeContainerInterface;

public class ConfirmPVTFragment extends Fragment implements BackButtonInterface, HomeContainerInterface{

	
	private Button mPrintPVTButton;
	
	private View mRootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_confirm_pvt, null);
		return mRootView;
	}
	
	private void initUI(){
		mRootView.findViewById(R.id.printPVTButton).setOnClickListener(onPrintButtonClickListner);
	}
	
	private View.OnClickListener onPrintButtonClickListner = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// check is there is any saved printer
			
		}
	};
	
	@Override
	public boolean onBackButtonPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onNavigationBackButtonPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onFragmentSwipe() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enableMenuItems() {
		// TODO Auto-generated method stub
		
	}
	

	public static  ConfirmPVTFragment newInstance(int tabPosition) {
		ConfirmPVTFragment fragment = new ConfirmPVTFragment();
        Bundle args = new Bundle();
        args.putInt(HomeScreenActivity.TAB_POSITION, tabPosition);
        fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void disableMenuItems() {
		// TODO Auto-generated method stub
		
	}

}
