package com.smikeapps.parking.fragments;



import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.smikeapps.parking.R;
import com.smikeapps.parking.activities.DiscoverPrinterActivity;
import com.smikeapps.parking.activities.HomeBaseActivity;
import com.smikeapps.parking.comman.utils.CommonUtils;
import com.smikeapps.parking.interfaces.BackButtonInterface;
import com.smikeapps.parking.tabs.HomeContainerInterface;
import com.smikeapps.parking.tabs.StackFragment;

public class CheckPVTFragment extends StackFragment implements BackButtonInterface, HomeContainerInterface{

private View mRoootView;
private static final String TAG = "CheckPVTFragment";

private Button mCheckPVTButton;
private Spinner mPlateSource;
private EditText mPlateCode;
private EditText mPlateNumber;


private ArrayAdapter<String> mPlateSourceAdapter;

private Button checkButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRoootView = inflater.inflate(R.layout.fragment_check_pvt, null);
		initUI();
		return mRoootView;
	}

	private void initUI() {
		mCheckPVTButton = (Button)mRoootView.findViewById(R.id.checkButton);
		mCheckPVTButton.setOnClickListener(onCheckPVTButtonClicked);
		
		mPlateSource = (Spinner) mRoootView.findViewById(R.id.plateSourceSpinner);
		mPlateSource.setOnItemSelectedListener(plateSourceSelected);
		
		
		ArrayList<String> plateSourcelist = new ArrayList<String>();
		for(int i=0;i<CommonUtils.getPlateSources().length;i++){
			Log.d(TAG, " plate "+CommonUtils.getPlateSources()[i]);
			plateSourcelist.add(CommonUtils.getPlateSources()[i]);
		}
		
		mPlateSourceAdapter = new ArrayAdapter<String>
        (getActivity(), android.R.layout.simple_spinner_item,plateSourcelist);
		mPlateSource.setAdapter(mPlateSourceAdapter);
	}

	@Override
	public void setUpActionBar() {
		Log.d(TAG, "setUpActionBar   ::...");
		((HomeBaseActivity) getActivity()).getSupportActionBar()
		.setTitle(getActivity().getString(R.string.check_pvt_button_lable));
getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		((HomeBaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		((HomeBaseActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
		setHasOptionsMenu(true);
	}
	
	
	/*@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.print_pvt_frament_menu, menu);
		((HomeBaseActivity) getActivity()).getSupportActionBar()
				.setDisplayHomeAsUpEnabled(true);
		MenuItem printer = menu.getItem(0);
		}
	
	private void openPrinterDiscovery(final boolean isForResult) {
		Log.d(TAG, "openPrinterDiscovery.....");
		getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Intent intent = new Intent(getActivity(), DiscoverPrinterActivity.class);
				Bundle extra = new Bundle();
				extra.putBoolean(
						DiscoverPrintersFragment.EXTRA_TAG_IS_CALLLED_FOR_RESULT, isForResult);
				intent.putExtras(extra);
				startActivity(intent);
			}
		});
		

	}

	
	@Override
		public boolean onOptionsItemSelected(MenuItem item) {
		boolean response = false;
		switch (item.getItemId()) {
		case R.id.printers:
			Log.d(TAG, "onOptionsItemSelected   ::...");
			openPrinterDiscovery(false);
			break;
		default:
			break;
		}

		return response;
		}*/

	@Override
	public void onFragmentSwipe() {
		Log.d(TAG, "onFragmentSwipe   ::...");
		
	}

	@Override
	public void enableMenuItems() {
		Log.d(TAG, "enableMenuItems   ::...");
		setUpActionBar();
	}

	@Override
	public void disableMenuItems() {
		Log.d(TAG, "disableMenuItems   ::...");
	}
	
private View.OnClickListener onCheckPVTButtonClicked = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Toast.makeText(getActivity(), "Under development", Toast.LENGTH_SHORT).show();
		//	loadChildFragment(R.id.container, PrintPVTFragment.newInstance(0), true);
		}
	};
	
	
	private OnItemSelectedListener plateSourceSelected = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	
	private OnItemSelectedListener categorySelected = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			
		}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
};
}
