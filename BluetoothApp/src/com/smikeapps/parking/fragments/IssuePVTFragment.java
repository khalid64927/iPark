package com.smikeapps.parking.fragments;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.smikeapps.parking.R;
import com.smikeapps.parking.activities.HomeBaseActivity;
import com.smikeapps.parking.activities.HomeScreenActivity;
import com.smikeapps.parking.interfaces.BackButtonInterface;
import com.smikeapps.parking.tabs.HomeContainerInterface;
import com.smikeapps.parking.tabs.StackFragment;

public class IssuePVTFragment extends StackFragment implements BackButtonInterface, HomeContainerInterface{

	private View mRoootView;
	private Button mIssuePVTButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRoootView = inflater.inflate(R.layout.activity_issue_pvt, null);
		initUI();
		return mRoootView;
	}
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	private void initUI(){
		mIssuePVTButton = (Button)mRoootView.findViewById(R.id.issuePVTButton);
		mIssuePVTButton.setOnClickListener(onIssuePVTButtonClicked);
	}
	
	public static  IssuePVTFragment newInstance(int tabPosition) {
		IssuePVTFragment fragment = new IssuePVTFragment();
        Bundle args = new Bundle();
        args.putInt(HomeScreenActivity.TAB_POSITION, tabPosition);
        fragment.setArguments(args);
		return fragment;
	}
	
	private View.OnClickListener onIssuePVTButtonClicked = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			loadChildFragment(R.id.container, PrintPVTFragment.newInstance(0), true);
		}
	};
	
	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		 Fragment fragment = (Fragment) getChildFragmentManager().findFragmentByTag("PrintPVTFragment");
	      if(fragment != null){
	            fragment.onActivityResult(requestCode, resultCode, data);
	      }
	}

	@Override
	public void setUpActionBar() {
		((HomeBaseActivity) getActivity()).getSupportActionBar()
		.setTitle(getActivity().getString(R.string.issue_pvt_button_lable));
getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		((HomeBaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		((HomeBaseActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
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
