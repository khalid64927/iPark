package com.smikeapps.parking.activities;

import junit.framework.Assert;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.smikeapps.parking.R;
import com.smikeapps.parking.comman.utils.AlertDialogHelper;
import com.smikeapps.parking.interfaces.BackButtonInterface;
import com.smikeapps.parking.interfaces.BaseActivityInterface;

public abstract class HomeBaseActivity extends ActionBarActivity implements BaseActivityInterface {

	private boolean mPoopAll = false;
	private boolean mDisableHomeButton = false;
	protected boolean isHomeFragment = false;
	
	public void onBackPressed() {
		navigateBack(false);
	}

	public void onHomeUpPressed() {
		navigateBack(true);
	}

	public void diableHomeButton(boolean res) {
		mDisableHomeButton = res;
	}
	private void navigateBack ( boolean isHomeUpNaigation ) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		final int stackCount = fragmentManager.getBackStackEntryCount();
		final Fragment currentFragment;
		mPoopAll =  false;
		
		if (stackCount > 0) {
			String fragmentTag = fragmentManager.getBackStackEntryAt(stackCount - 1).getName();
			currentFragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);
			if (currentFragment instanceof BackButtonInterface ) {
				
				boolean res = false;
				if (isHomeUpNaigation) {
					res = ((BackButtonInterface) currentFragment).onNavigationBackButtonPressed();
				} else {
					
					
					res = ((BackButtonInterface) currentFragment).onBackButtonPressed();
				}
				
				if (!res) {
					if (stackCount == 1) {
						Log.d("HomeBaseActivity", "stackCount == 1 and hence showing dailong");
						mPoopAll = true;
						showConfirmationDailog();
					} else {
						//super.onBackPressed();
						Log.d("HomeBaseActivity", "stackCount == 0 and hence showing dailong");
						showConfirmationDailog();

					}
				}
				
			} else {
				//super.onBackPressed();
				showConfirmationDailog();

			}
		} else {
			currentFragment = getSupportFragmentManager().findFragmentById(R.id.content);
			if (currentFragment instanceof BackButtonInterface) {
				boolean res = ((BackButtonInterface) currentFragment).onBackButtonPressed();
				
				if (!res) {
					//super.onBackPressed();
					showConfirmationDailog();

				}
				
			} else {
				//super.onBackPressed();
				showConfirmationDailog();

			}
		}

	}

	@Override
	public void loadFragment(Fragment fragment, boolean addtobackstack ) {
		mDisableHomeButton = false;
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.content, fragment, fragment.getClass().getSimpleName());
		if (addtobackstack) {
			Assert.assertNotNull(fragment.getClass().getSimpleName());
			ft.addToBackStack(fragment.getClass().getSimpleName());

		}
		ft.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean res = false;
		switch (item.getItemId()) {

		case android.R.id.home:
			if (!mDisableHomeButton) {
				onHomeUpPressed();
			}
			res = true;
			break;

		default:
			break;
		}
		return res;
	}

	public void showConfirmationDailog() {
		if ( this instanceof HomeScreenActivity ) {
			AlertDialogHelper.showAlertDialog(this, "Exit", "Do you want to exit the application ?", mOKButtonListener,
					mCancelButtonListener, "No", "Yes");
		} else {
			finish();
		}
	}



private android.content.DialogInterface.OnClickListener mCancelButtonListener = new DialogInterface.OnClickListener() {

    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();    
        mPoopAll = false;
    }	
};

private android.content.DialogInterface.OnClickListener mOKButtonListener = new DialogInterface.OnClickListener() {
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        finish();
		
    }
	
};

}
