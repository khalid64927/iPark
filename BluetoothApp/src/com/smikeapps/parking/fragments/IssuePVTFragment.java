package com.smikeapps.parking.fragments;



import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.smikeapps.parking.R;
import com.smikeapps.parking.activities.DiscoverPrinterActivity;
import com.smikeapps.parking.activities.HomeBaseActivity;
import com.smikeapps.parking.activities.HomeScreenActivity;
import com.smikeapps.parking.comman.utils.CommonUtils;
import com.smikeapps.parking.interfaces.BackButtonInterface;
import com.smikeapps.parking.tabs.HomeContainerInterface;
import com.smikeapps.parking.tabs.StackFragment;

public class IssuePVTFragment extends StackFragment implements BackButtonInterface, HomeContainerInterface{

	private static final String TAG = "IssuePVTFragment";
	private View mRoootView;
	private Button mIssuePVTButton;
	private Spinner mPlateSource;
	private Spinner mCategory;
	private Spinner mRegion;
	private EditText plateCode;
	private EditText plateNumber;
	
	private ArrayAdapter<String> mPlateSourceAdapter;
	private ArrayAdapter<String> mCategoryAdaptter;
	private ArrayAdapter<String> mRegionAdaptter;
	private String plateSourceString;
	private String plateCodeString;
	
	ArrayList<String> plateSourcelist;
	ArrayList<String> catlist;
	ArrayList<String> regionlist;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mRoootView = inflater.inflate(R.layout.activity_issue_pvt, null);
		initUI();
		return mRoootView;
	}
	
	/*@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		Log.d(TAG, "onCreateOptionsMenu.....");
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
		Log.d(TAG, "onOptionsItemSelected.....");
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
	private void initUI(){
		mIssuePVTButton = (Button)mRoootView.findViewById(R.id.issuePVTButton);
		mIssuePVTButton.setOnClickListener(onIssuePVTButtonClicked);
		
		mPlateSource = (Spinner) mRoootView.findViewById(R.id.plateSourceSpinner);
		mPlateSource.setOnItemSelectedListener(plateSourceSelected);
		
		mCategory = (Spinner) mRoootView.findViewById(R.id.categorySpinner);
		mCategory.setOnItemSelectedListener(categorySelected);
		
		mRegion = (Spinner) mRoootView.findViewById(R.id.regionSpinner);
		mRegion.setOnItemSelectedListener(regionSelected);
		
		plateSourcelist = new ArrayList<String>();
		for(int i=0;i<CommonUtils.getPlateSources().length;i++){
			Log.d(TAG, " plate "+CommonUtils.getPlateSources()[i]);
			plateSourcelist.add(CommonUtils.getPlateSources()[i]);
		}
		
		mPlateSourceAdapter = new ArrayAdapter<String>
        (getActivity(), android.R.layout.simple_spinner_item,plateSourcelist);
		mPlateSource.setAdapter(mPlateSourceAdapter);
		
		
		catlist = new ArrayList<String>();
		for(int i=0;i<CommonUtils.getCategory().length;i++){
			Log.d(TAG, " cat "+CommonUtils.getCategory()[i]);
			catlist.add(CommonUtils.getCategory()[i]);
		}
		
		mCategoryAdaptter = new ArrayAdapter<String>
        (getActivity(), android.R.layout.simple_spinner_item,catlist);
		mCategory.setAdapter(mCategoryAdaptter);
		
		regionlist = new ArrayList<String>();
		for(int i=0;i<CommonUtils.getRegions().length;i++){
			Log.d(TAG, " region "+CommonUtils.getRegions()[i]);
			regionlist.add(CommonUtils.getRegions()[i]);
		}
		
		mRegionAdaptter = new ArrayAdapter<String>
        (getActivity(), android.R.layout.simple_spinner_item,regionlist);
		mRegion.setAdapter(mRegionAdaptter);
		
		plateCode = (EditText)mRoootView.findViewById(R.id.plateCodeEdtxt);
		plateNumber= (EditText)mRoootView.findViewById(R.id.plateNumberEdtxt);
		
	}
	
	public static  IssuePVTFragment newInstance(int tabPosition) {
		IssuePVTFragment fragment = new IssuePVTFragment();
        Bundle args = new Bundle();
        args.putInt(HomeScreenActivity.TAB_POSITION, tabPosition);
        fragment.setArguments(args);
		return fragment;
	}
	
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
	
	private OnItemSelectedListener regionSelected = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			
		}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	};
	
	private View.OnClickListener onIssuePVTButtonClicked = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(TextUtils.isEmpty(plateCode.getText().toString().trim())){
				Toast.makeText(getActivity(), "enter plate code", Toast.LENGTH_SHORT).show();
				return;
			}else if(TextUtils.isEmpty(plateNumber.getText().toString().trim())){
				Toast.makeText(getActivity(), "enter plate number", Toast.LENGTH_SHORT).show();
				return;
			}else if((TextUtils.isEmpty(plateNumber.getText().toString().trim())) && (TextUtils.isEmpty(plateCode.getText().toString().trim()))){
				Toast.makeText(getActivity(), "enter plate number and plate code", Toast.LENGTH_SHORT).show();
				return;
			}else{
				String pltSrcStr = plateSourcelist.get(mPlateSource.getSelectedItemPosition());
				String catStr = catlist.get(mCategory.getSelectedItemPosition());
				String rgStr = regionlist.get(mRegion.getSelectedItemPosition());
				
				loadChildFragment(R.id.container, PrintPVTFragment.newInstance(pltSrcStr, catStr, rgStr, plateCode.getText().toString().trim(), plateNumber.getText().toString().trim()), true);
			}
			
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
		setHasOptionsMenu(true);
	}

	@Override
	public void onFragmentSwipe() {
		Log.d("TAG", "fffffffffffffffffffffff");
		
	}

	@Override
	public void enableMenuItems() {
		Log.d("TAG", "iiiiiiiiiiiiiiiiiiiiiiiii");
		setUpActionBar();
	}

	@Override
	public void disableMenuItems() {
		Log.d("TAG", "ooooooooooooooooooooooooooo");
	}
}
