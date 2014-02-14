package com.smikeapps.parking.tabs;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;

import com.smikeapps.parking.interfaces.BackButtonInterface;


public abstract class StackFragment extends Fragment implements BackButtonInterface {

	private OnBackStackChangedListener onBackStackChangedListener = null;
	
	public void loadChildFragment(int containerResourceId, Fragment childFragment, boolean addtobackstack) {
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		ft.replace(containerResourceId, childFragment, childFragment.getClass().getSimpleName() );
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		if (addtobackstack) {
			ft.addToBackStack( childFragment.getClass().getSimpleName() ); 
		}
		ft.commit();
		if (onBackStackChangedListener == null) {
			onBackStackChangedListener = new OnBackStackChangedListener() {
				@Override
				public void onBackStackChanged() {
					int count = getChildFragmentManager().getBackStackEntryCount();
					
					if ( count == 0 ) {
						setUpActionBar();
					}
				}
			};
			getChildFragmentManager().addOnBackStackChangedListener( onBackStackChangedListener );

		}
	}
	
	
	
	

	@Override
	public boolean onBackButtonPressed() {
		boolean res = false;
		FragmentManager fm = getChildFragmentManager();
		int stackCount = fm.getBackStackEntryCount();
		
		if (stackCount > 0) {
			getChildFragmentManager().popBackStack();
			res = true;
		} 
		return res;
	}
	@Override
	public boolean onNavigationBackButtonPressed() {
		return onBackButtonPressed();
	}

	public abstract void setUpActionBar(); 

}
