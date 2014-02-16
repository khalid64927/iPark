package com.smikeapps.parking.comman.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


public class CommonUtils {
	
	static String [] mPlateSource  =  {"Abu Dhabi",
"Ajman",
"Dubai",
"Fujairah",
"Bahrain",
"Ras Al-Khaimah",
"Sharjah",
"UAE",
"UMM AL QUWAIN",
"General Organization",
"IRAQ",
"Jordan",
"Kuwait",
"LEBANON",
"MINISTRY OF INTERIOR",
"Military",
"Oman",
"Qatar",
"Saudi Arabia",
"Syria",
"Yemen"};
	
	static String [] mZoneNumbers = {"S102","S201","S302","S408","S504","S604","S706","S805","D902","B408","R504","F604","G706","H805"};
	
	static String [] mCategory = {"Parking in a prohibited space",
			"Parking on a sidewalk",
			"Parking in or too close to or within a street crossing, railroad crossing or crosswalk",
			"Double parking",
			"Parking at a parking meter without paying, or for longer than the paid time",
			"Parking in a handicapped zone without an appropriate permit",
			"Parking without a zone permit in places where parking is severely impacted",
			"Parking with the parking permit or payment receipt not visible",
			"Parking for longer than the maximum time",
			"Parking facing against the direction of traffic",
			"Parking outside marked squares, for example angle parking where only parallel parking is allowed"};
	
	
	public static String[] getPlateSources(){
		return mPlateSource;
	}
	
	public static String[] getCategory(){
		return mCategory;
	}
	
	public static String[] getRegions(){
		return mZoneNumbers;
	}
	
	public static String loadJsonFromAsset( Context context, String fileName) {
		String json = null;
		try {
			InputStream is = context.getAssets().open("jsons/" + fileName);
			int size = is.available();
			
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			
			json = new String(buffer, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return json;
	}
	
	 /* Email must be in correct email pattern */
    public static boolean validateEmail(String emailString){
        if (TextUtils.isEmpty(emailString)
                || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
        	return false;
        }else{
        	return true;
        }
    }
    
    /* Password confirmation must match Password */
    public static boolean validatePasswordConfirmation(String passwordString) {
        if (TextUtils.isEmpty(passwordString)
                || passwordString.length() <= 0) {
            return false;
        }else{
        	return true;
        }

    }
	
	public static void hideActionBar(ActionBarActivity context){
		if (Build.VERSION.SDK_INT < 16) {
			context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else if(Build.VERSION.SDK_INT > 16){
        	View decorView = context.getWindow().getDecorView();
        	// Hide the status bar.
        	int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        	decorView.setSystemUiVisibility(uiOptions);
        	// Remember that you should never show the action bar if the
        	// status bar is hidden, so hide that too if necessary.
        	ActionBar actionBar = context.getSupportActionBar();
        	actionBar.hide();
        }
	}
	
	public static void hideSoftKeyboard(FragmentActivity fragmentActivity) {
		
		if ( fragmentActivity == null ) {
			return;
		}
        InputMethodManager inputMethodManager = (InputMethodManager)fragmentActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View currentFocus = fragmentActivity.getCurrentFocus();
        if ( null != currentFocus ) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } else {
            inputMethodManager.hideSoftInputFromWindow(fragmentActivity.getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        
     //   inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
    }
	
	
	public static String titleize(String source){
        boolean cap = true;
        char[]  out = source.toLowerCase().toCharArray();
        int i, len = source.length();
        for(i=0; i<len; i++){
            if(Character.isWhitespace(out[i])){
                cap = true;
                continue;
            }
            if(cap){
                out[i] = Character.toUpperCase(out[i]);
                cap = false;
            }
        }
        return new String(out);
    }
	
	/**
	 * function to check if device has GPS hardware 
	 * @return Boolean: true present, false otherwise
	*/
	public static boolean isGPSHardwarePresent(Context context){
		PackageManager pm = context.getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
	}
	
	
	/**
	 * function to check if device GPS hardware is enabled
	 * @return Boolean: true if enabled, false otherwise
	*/
	public static boolean isGPSEnables(Context context){
		 LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
		
		 if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
	            return true;
	        }else{
	        	 return false;
	        }
	}
	
	public static boolean isValidEmail(String email) {
		Pattern pattern = Patterns.EMAIL_ADDRESS;
		return pattern.matcher(email).matches();
	}
	
	
	  public static boolean isConnectedToNetwork(Context context) {
	    	boolean res = false;
	    	if (null != context)  {
	            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
	            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
	            res = (activeNetInfo != null) ? true : false;
	    	}
	        return res;
	    }

}
