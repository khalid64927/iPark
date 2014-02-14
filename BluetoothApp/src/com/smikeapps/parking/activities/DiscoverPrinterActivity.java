package com.smikeapps.parking.activities;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.smikeapps.parking.R;
import com.smikeapps.parking.fragments.DiscoverPrintersFragment;


public class DiscoverPrinterActivity extends HomeBaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_discover_printers_layout);
		Bundle extra = getIntent().getExtras();
		loadFragment(DiscoverPrintersFragment.newInstance(extra), true);
	}
	
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}
}
