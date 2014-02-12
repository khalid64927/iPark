package com.smikeapps.parking.activities;
import android.os.Bundle;

import com.smikeapps.parking.R;
import com.smikeapps.parking.activities.HomeBaseActivity;
import com.smikeapps.parking.fragments.DiscoverPrintersFragment;


public class DiscoverPrinterActivity extends HomeBaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_discover_printers_layout);
		Bundle extra = getIntent().getExtras();
		loadFragment(DiscoverPrintersFragment.newInstance(extra), true);
	}
}
