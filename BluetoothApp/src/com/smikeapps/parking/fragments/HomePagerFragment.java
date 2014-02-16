package com.smikeapps.parking.fragments;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.smikeapps.parking.R;
import com.smikeapps.parking.activities.HomeBaseActivity;
import com.smikeapps.parking.activities.HomeScreenActivity;
import com.smikeapps.parking.comman.utils.CommonUtils;
import com.smikeapps.parking.interfaces.BackButtonInterface;
import com.smikeapps.parking.tabs.HomeContainerInterface;
import com.smikeapps.parking.tabs.StackFragment;

public class HomePagerFragment extends Fragment implements BackButtonInterface, TabListener{
	ViewPager mViewPager;
	TabsAdapter mTabsAdapter;
	int mCurrTabPosition = 0;
	int mPrevTabPosition = 0;
	
	public final static int TAB_CHECK_PVT =  0;
	public final static int TAB_ISSUE_PVT =  TAB_CHECK_PVT + 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		((ActionBarActivity)getActivity()).getSupportActionBar().show();
		View fragmentLayout = inflater.inflate(R.layout.fragment_home_container, null);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		final ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		

		if(mTabsAdapter == null) {
			mTabsAdapter = new TabsAdapter(getActivity());
			mTabsAdapter.addTab(getString(R.string.check_pvt), CheckPVTFragment.class, null);
			mTabsAdapter.addTab(getString(R.string.issue_pvt), IssuePVTFragment.class, null);
		}
		
		mViewPager = (ViewPager) fragmentLayout.findViewById(R.id.pager);
		mViewPager.setAdapter(mTabsAdapter);

		
		mViewPager.setOnTouchListener( new OnTouchListener() {
			
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				Fragment fragment = ( StackFragment ) getFragmentAtIndex ( mViewPager.getCurrentItem() );
				return false;
			}
		});
		

		mViewPager.setCurrentItem(mCurrTabPosition);

		mTabsAdapter.notifyDataSetChanged();
		mViewPager.setOffscreenPageLimit(2);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						if (position != mCurrTabPosition) {
							mPrevTabPosition = mCurrTabPosition;
						}
						mCurrTabPosition = position;
						actionBar.setSelectedNavigationItem(mCurrTabPosition);
					}
				});
		
		
		actionBar.removeAllTabs();
		for (int i = 0; i < mTabsAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mTabsAdapter.getTabTitle(i)).setTabListener(this));
		}
		mCurrTabPosition = getTabPostionFromArgs();
		actionBar.setSelectedNavigationItem(mCurrTabPosition);

		return fragmentLayout;
	}
	
	
public void refreshActionBar() {
		
		final ActionBar actionBar = ((HomeBaseActivity)getActivity()).getSupportActionBar();
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setTitle(mTabsAdapter.getTabTitle(mCurrTabPosition));
		actionBar.setDisplayHomeAsUpEnabled(false);
		
		HomeBaseActivity parent =  (HomeBaseActivity) getActivity();
		parent.diableHomeButton(true);

	}
	
	public int getTabPostionFromArgs() {
		Bundle args = getArguments();
		int postion = 0;
		if (null != args) {
			postion = getArguments().getInt(HomeScreenActivity.TAB_POSITION, TAB_CHECK_PVT);
		}
		return TAB_CHECK_PVT;
	}

	public Fragment getFragmentAtIndex(final int index) {
		return mTabsAdapter.getFragment(index);
	}

	@Override
	public boolean onBackButtonPressed() {
		return navigateBack();
	}

	@Override
	public boolean onNavigationBackButtonPressed() {
		return navigateBack();
	}
	
	private boolean navigateBack ( ) {
		boolean res = false;
		Fragment fragment = mTabsAdapter.getFragment(mCurrTabPosition);
		
		if (null != fragment) {
			FragmentManager childFragmentManager = fragment.getChildFragmentManager();
			int childCount = childFragmentManager.getBackStackEntryCount();

			if (childCount > 0) {
				BackStackEntry fragmentEntry = childFragmentManager.getBackStackEntryAt(childCount - 1);
				fragment = childFragmentManager.findFragmentByTag(fragmentEntry.getName());
				if (fragment instanceof BackButtonInterface) {
					res = ((BackButtonInterface) fragment).onBackButtonPressed();
				}
			} else {
				if (fragment instanceof BackButtonInterface) {
					res = ((BackButtonInterface) fragment).onBackButtonPressed();
				}
			}
		}
		return res;
	}
	
	public static class TabsAdapter extends FragmentStatePagerAdapter {
		private final int MAX_TAB_COUNT = 2;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>(MAX_TAB_COUNT);
		private SparseArray<WeakReference<Fragment>> mPageReferenceMap;
		private Context mContext;

		static final class TabInfo {
			private final String mActionTitle;
			private final Class<?> mClass;
			private final Bundle mArgs;

			TabInfo(String tag, Class<?> tclass, Bundle args) {
				mActionTitle = tag;
				mClass = tclass;
				mArgs = args;
			}
		}

		public TabsAdapter(FragmentActivity activity) {
			super(activity.getSupportFragmentManager());
			mPageReferenceMap = new SparseArray<WeakReference<Fragment>>(MAX_TAB_COUNT);
			mContext = activity;
		}

		public void addTab(String title, Class<?> clss, Bundle args) {
			TabInfo info = new TabInfo(title, clss, args);
			mTabs.add(info);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mTabs.size();
		}

		@Override
		public Fragment getItem(int position) {
			TabInfo info = mTabs.get(position);
			Fragment fragemnt = Fragment.instantiate(mContext,info.mClass.getName(), info.mArgs);
			mPageReferenceMap.put(Integer.valueOf(position), new WeakReference<Fragment>(fragemnt));
			return fragemnt;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);
			mPageReferenceMap.remove(Integer.valueOf(position));
		}

		public Fragment getFragment(int key) {
			WeakReference<Fragment> entry = mPageReferenceMap.get(key);
			if ( entry != null ) {
				return entry.get();
			}else {
				return null;
			}
		}

		public String getTabTitle(int index) {
			return mTabs.get(index).mActionTitle;
		}
	}


	@Override
	public void onTabReselected(Tab tab, FragmentTransaction arg1) {
		mCurrTabPosition = tab.getPosition();
		if (mViewPager.getCurrentItem() != mCurrTabPosition) {
			mPrevTabPosition = mViewPager.getCurrentItem();
			mViewPager.setCurrentItem(mCurrTabPosition);
		}
		//disable menu items of previous fragment
		disableMenuItems();
		//enable menu items of previous fragment
		enableMenuItems();

		CommonUtils.hideSoftKeyboard(getActivity());
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		mCurrTabPosition = tab.getPosition();

		if (mViewPager.getCurrentItem() != mCurrTabPosition) {
			mPrevTabPosition = mViewPager.getCurrentItem();
			mViewPager.setCurrentItem(mCurrTabPosition);
		}	
		//disable menu items of previous fragment
		disableMenuItems();
		//enable menu items of previous fragment
		enableMenuItems();
		//notify page change to current fragment
		notifyPageChange();

		CommonUtils.hideSoftKeyboard(getActivity());
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}
	
public void notifyPageChange() {
		
		Fragment currentFragment = getFragmentAtIndex(mCurrTabPosition);
		if ( currentFragment != null ) {
			FragmentManager fm = currentFragment.getChildFragmentManager();
			int childCount = fm.getBackStackEntryCount();
			if (childCount > 0) {
				
				BackStackEntry fragmentEntry = fm.getBackStackEntryAt( childCount -1 ) ;
				currentFragment  = fm.findFragmentByTag(fragmentEntry.getName());
			}
		 }
		
		if ( currentFragment instanceof HomeContainerInterface ) {
			(( HomeContainerInterface ) currentFragment).onFragmentSwipe();
			
			

		}
	}

	public void enableMenuItems() {
		Fragment currentFragment = getFragmentAtIndex(mCurrTabPosition);
		if (currentFragment != null) {
			FragmentManager fm = currentFragment.getChildFragmentManager();
			int childCount = fm.getBackStackEntryCount();
			if (childCount > 0) {

				BackStackEntry fragmentEntry = fm.getBackStackEntryAt(childCount - 1);
				currentFragment = fm.findFragmentByTag(fragmentEntry.getName());
			}
		}

		if (currentFragment instanceof HomeContainerInterface) {
			((HomeContainerInterface) currentFragment).enableMenuItems();
		}
	}

	public void disableMenuItems() {
		Fragment currentFragment = getFragmentAtIndex(mPrevTabPosition);
		if (currentFragment != null) {
			FragmentManager fm = currentFragment.getChildFragmentManager();
			int childCount = fm.getBackStackEntryCount();
			if (childCount > 0) {

				BackStackEntry fragmentEntry = fm.getBackStackEntryAt(childCount - 1);
				currentFragment = fm.findFragmentByTag(fragmentEntry.getName());
			}
		}

		if (currentFragment instanceof HomeContainerInterface) {
			((HomeContainerInterface) currentFragment).disableMenuItems();
		}
	}
	
	public static  HomePagerFragment newInstance(int tabPosition) {
		HomePagerFragment fragment = new HomePagerFragment();
        Bundle args = new Bundle();
        args.putInt(HomeScreenActivity.TAB_POSITION, tabPosition);
        fragment.setArguments(args);
		return fragment;
	}

}
