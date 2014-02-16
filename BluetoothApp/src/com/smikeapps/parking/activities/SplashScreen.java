package com.smikeapps.parking.activities;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.internal.widget.ProgressBarICS;

import com.smikeapps.parking.R;


/**
 * This is splash screen class
*/
public class SplashScreen extends Activity{
	 
	private Handler mSplashHander;
	private static final int FIVE_SECONDS =  2000;
	private static final int FINISH_SPLASH_SCREEN_MSG =  1;
	private ProgressBarICS progressBasICS;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		mSplashHander = new SplashScreenHandler();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mSplashHander.sendEmptyMessageDelayed(FINISH_SPLASH_SCREEN_MSG, FIVE_SECONDS);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mSplashHander.removeMessages(FINISH_SPLASH_SCREEN_MSG);
	}
	
private  class SplashScreenHandler extends Handler {
		
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FINISH_SPLASH_SCREEN_MSG:
				// start app home screen
				Intent intent = new Intent ( SplashScreen.this, HomeScreenActivity.class );
				intent.putExtra ( HomeScreenActivity.FRAGMRNT_NAME, HomeScreenActivity.LOGIN_FRAGMENT );
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity ( intent );
				finish();
				break;

			default:
				break;
			}
		}
		
	}

}
