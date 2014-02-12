package com.smikeapps.parking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.smikeapps.parking.R;
import com.smikeapps.parking.fragments.HomePagerFragment;
import com.smikeapps.parking.fragments.LoginFragment;

public class HomeScreenActivity extends HomeBaseActivity{
	
	private static final String TAG = HomeScreenActivity.class.getName();

	public final static String FRAGMRNT_NAME = "FRAGMENT_NAME";
	public static final String TAB_POSITION = "TAB_POSITION";
	
	public final static int LOGIN_FRAGMENT =  0;
	public final static int TAB_FRAGMENT =  LOGIN_FRAGMENT + 1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);
		
		Intent receivedIntent = getIntent();
		int tabPosition = -1;
		
		switch (receivedIntent.getIntExtra(FRAGMRNT_NAME, TAB_FRAGMENT )) {

		
		case LOGIN_FRAGMENT:
			Log.d(TAG, " SWITCH CASE LOGIN_FRAGMENT  ::  ");
			tabPosition = receivedIntent.getIntExtra(TAB_POSITION, 0);
			loadFragment(LoginFragment.newInstance(tabPosition), false);
			break;
			
		case TAB_FRAGMENT:
			Log.d(TAG, " SWITCH CASE TAB_FRAGMENT  ::  ");
			tabPosition = receivedIntent.getIntExtra(TAB_POSITION, 0);
			loadFragment(HomePagerFragment.newInstance(tabPosition), false);
			break;
			
		default:
			isHomeFragment = true;
			loadFragment(HomePagerFragment.newInstance(LOGIN_FRAGMENT), false);
			break;
		
		}
		
	}
	
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "  onResume() ::  ");
	}
	
	
	
}
