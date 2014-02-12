package com.smikeapps.parking.common.context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.ActivityManager;
import android.content.Context;

public class ParkingApplication extends ParkingPVTApplication{


	private static ParkingApplication applicationContext;
	private ApplicationContextSingleton appContext;
//	private DatabaseUtil  databaseUtil;
//	private static ImageLoader imageLoader;

	private ExecutorService executor = Executors.newFixedThreadPool(5);


	public ExecutorService getThreadExecutor () {
		return executor;
	}
	public ParkingApplication() {
		applicationContext = this;
	}

	public static ParkingApplication getInstance () {
		return applicationContext;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		appContext = ApplicationContextSingleton.initialize ( getApplicationContext ( ) );
		AppConfiguration.initConfig ( getApplicationContext ( ) );
		AccountPreference.init( this );
	//	databaseUtil = DatabaseUtil.getInstance();
		
		 int memClass = ((ActivityManager)getApplicationContext ().getSystemService(Context.ACTIVITY_SERVICE))
	                .getMemoryClass();
	        // Use 1/8th of the available memory for this memory cache.
	        int cacheSize = 1024 * 1024 * memClass / 8;
	        
		// creating imageloader instance 
		// if caching needs to be changed then one has to change here.
//		imageLoader = new ImageLoader ( OmniaProvider.getInstance().mRequestQueue,  new BitmapLruCache(cacheSize)); 
	}

	/*public ImageLoader getImageLoader () {
		return imageLoader;
	}
	
	public DatabaseUtil getDatabaseUtil () {
		return databaseUtil;
	}*/
}
