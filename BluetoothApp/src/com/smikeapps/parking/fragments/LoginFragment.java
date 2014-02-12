package com.smikeapps.parking.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.smikeapps.parking.R;
import com.smikeapps.parking.activities.HomeBaseActivity;
import com.smikeapps.parking.activities.HomeScreenActivity;
import com.smikeapps.parking.comman.utils.CommonUtils;
import com.smikeapps.parking.interfaces.BackButtonInterface;
import com.smikeapps.parking.tabs.StackFragment;

public class LoginFragment extends StackFragment implements BackButtonInterface{

	
	private View mRoootView;
	private EditText mUseName;
	private EditText mPassword;
	private Button mSubmitButton;
	private static final String TAG = LoginFragment.class.getName();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRoootView = inflater.inflate(R.layout.fragment_login, null);
		((HomeBaseActivity)getActivity()).getSupportActionBar().hide();
		//((HomeBaseActivity)getActivity()).getWindow().
		initUI();
		return mRoootView;
	}
	
	private void initUI(){
		mUseName = (EditText)mRoootView.findViewById(R.id.userName);
		mPassword = (EditText)mRoootView.findViewById(R.id.password);
		mSubmitButton = (Button)mRoootView.findViewById(R.id.submit);
		mSubmitButton.setOnClickListener(onSubmitButtonClickListner);
	}
	
	
	private View.OnClickListener onSubmitButtonClickListner = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.d(TAG, "onSubmitButtonClickListner  :: onClick ");
			String username = mUseName.getText().toString().trim();
			String password = mPassword.getText().toString().trim();
			((HomeBaseActivity) getActivity()).loadFragment(HomePagerFragment.newInstance(HomeScreenActivity.TAB_FRAGMENT), false);
		//	validateUser(username, password);
		}
		
	};
	
	private void validateUser(String email, String password){
		Log.d(TAG, "validateUser  :: onClick ");
		if(CommonUtils.validateEmail(email) && CommonUtils.validatePasswordConfirmation(password)){
			((HomeBaseActivity) getActivity()).loadFragment(HomePagerFragment.newInstance(HomeScreenActivity.TAB_FRAGMENT), false);
		}else{
			if(!CommonUtils.validateEmail(email) && CommonUtils.validatePasswordConfirmation(password)){
				showAlertDialog("Invalid username please check the email format", "Invalid Username");
			}else if(CommonUtils.validateEmail(email) && !CommonUtils.validatePasswordConfirmation(password)){
				showAlertDialog("Invalid password please check the password format", "Invalid Password");
			}else if(!CommonUtils.validateEmail(email) && !CommonUtils.validatePasswordConfirmation(password)){
				showAlertDialog("Invalid username password please check email and password format", "Invalid Username and Password");
			}
			
		}
	}
	
	private void showGPSCustomDialog(){
		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.no_gps_dialog);
		dialog.show();
		TextView title = (TextView)dialog.findViewById(R.id.store_map_no_gps_title);
		TextView message = (TextView)dialog.findViewById(R.id.store_map_no_gps_msg_line_one);
		Button okbutton = (Button) dialog.findViewById(R.id.okButton);
		
	}
	
	// no gps dialog
	private void showAlertDialog(String message, String title) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setCancelable(true);
		builder.setPositiveButton(R.string.ok_lable, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});
		/*
		 * builder.setNegativeButton(R.string.eshop_no, new
		 * DialogInterface.OnClickListener() { public void
		 * onClick(DialogInterface dialog, int id) { // stay in the same screen
		 * dialog.dismiss(); } });
		 */
		AlertDialog alert = builder.create();
		alert.show();

	}
	

	
	public static  LoginFragment newInstance(int tabPosition) {
		LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putInt(HomeScreenActivity.TAB_POSITION, tabPosition);
        fragment.setArguments(args);
		return fragment;
	}
	
	
	@Override
	public void setUpActionBar() {
		// TODO Auto-generated method stub
		
	}

}
