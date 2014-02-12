package com.smikeapps.parking.common.context;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.DisplayMetrics;

public class AppConfiguration {

	private static SharedPreferences sharedPrefs = null;
	private static boolean isInDebugMode = false;
	private static String appVersion = "";
	private static String appName = "";

	private static String currencySymbol;

	private static int minimumFractionDigits;

	private static int maximumFractionDigits;
	
	/** basket sync interval duration 30 seconds */
	private static final int basketSynchronizationPeriod = 30;
	

	// public static boolean isCurrent = false;
	/* Config for Production environment & Crashlytics */
	private static boolean isProdEnvironment = false;

	/* Config for EShop */
	private static final String ESHOP_URL_PROD = "http://omnia-fo-cart-int.carrefour.fr/m/";
	private static final String ESHOP_URL_PREPROD = "http://omnia-fo-cart-int.carrefour.fr/m/";
	
	private static final String ESHOP_URL_PROD_SECURE = "https://omnia-fo-cart-int.carrefour.fr/m/";
	private static final String ESHOP_URL_PREPROD_SECURE = "https://omnia-fo-cart-int.carrefour.fr/m/";

	public static final int PREPROD = 1;
	public static final int PROD = 2;

	private static int iWindowWidth = -1;
	
	private static int iWindowHeight = -1;
	
	public static Typeface mTypefaceHelvetic = null;
	public static Typeface mOmnesMedium = null;
	

	private AppConfiguration() {
		// Currency Symbol
		currencySymbol = "€"; // "€"

		// Minimum Fraction Digits
		minimumFractionDigits = 2;

		// Maximum Fraction Digits
		maximumFractionDigits = 2;
	}
	
	public static int getPixelsFromDp(float dp) {
		DisplayMetrics metrics = ParkingApplication.getInstance().getApplicationContext().getResources().getDisplayMetrics();
		final float scale = metrics.density;
		// Old Implementation
		int oldPx = (int) (dp * scale + 0.5f);
		// New implementation according to this Formula px = dp * (dpi / 160)
		int px = (int) (dp * (metrics.densityDpi / 160));
		
		return px;
	}
	
	
	public static int convertPixelsToDp ( float px, Context context ) {
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float dp = px / (metrics.densityDpi / 160f);
	    return Math.round ( dp );
	}

	public static void initConfig(Context context) {
		if (sharedPrefs == null) {
			sharedPrefs = context.getSharedPreferences("ConfigOmnia", Context.MODE_PRIVATE);
		}

		try {
			PackageInfo infos = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

			appVersion = infos.versionName;
			ApplicationInfo appinfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
			if ((appinfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
				isInDebugMode = true;
			}

			appName = context.getPackageName();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		AssetManager assetManager = context.getAssets();
		mTypefaceHelvetic = Typeface.createFromAsset(assetManager, "fonts/HelveticaNeueLTStd-Bd.otf");
		mOmnesMedium = Typeface.createFromAsset(assetManager, "fonts/omnes_medium.ttf");

	}

	public static int getWindowWidth(Activity activityContext) {

		if (iWindowWidth == -1) {
			
			DisplayMetrics metrics = ParkingApplication.getInstance().getApplicationContext().getResources().getDisplayMetrics();
			iWindowWidth = metrics.widthPixels;
			iWindowHeight = metrics.heightPixels;
			
		}

		return iWindowWidth;

	}

	public static int getWindowHeight(Activity activityContext) {

		if (iWindowHeight == -1) {
			
			DisplayMetrics metrics = ParkingApplication.getInstance().getApplicationContext().getResources().getDisplayMetrics();
			iWindowWidth = metrics.widthPixels;
			iWindowHeight = metrics.heightPixels;
			
		}

		return iWindowHeight;

	}
	
	public static boolean isFirstShoppingList() {
		return sharedPrefs.getBoolean("isFirstShoppingList", true);
	}

	public static void setFirstShoppingList(boolean isFirstShoppingList) {
		SharedPreferences.Editor editor = sharedPrefs.edit();
		System.out.println("setFirstShoppingList ================" + isFirstShoppingList);
		editor.putBoolean("isFirstShoppingList", isFirstShoppingList);
		editor.commit();
	}

	public static void setFirstShopMap(boolean isFirstShopMap) {
		SharedPreferences.Editor editor = sharedPrefs.edit();
		System.out.println("setisFirstShopMap ================================" + isFirstShopMap);
		editor.putBoolean("isFirstShopMap", isFirstShopMap);
		editor.commit();
	}

	public static boolean isFirstShopMap() {
		return sharedPrefs.getBoolean("isFirstShopMap", true);
	}

	public static boolean databaseExists() {
		return sharedPrefs.getBoolean("IS_DATABASE_CREATED", false);
	}

	public static void setDatabaseCreated(boolean databaseCreated) {
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.putBoolean("IS_DATABASE_CREATED", databaseCreated);
		editor.commit();
	}

	public static boolean isFirstLaunch() {
		return sharedPrefs.getBoolean("IS_FIRST_LAUNCH", true);
	}

	public static void setFirstLaunch(boolean isFirstLaunch) {
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.putBoolean("IS_FIRST_LAUNCH", isFirstLaunch);
		editor.commit();
	}

	public static void setSelectedSvg(int selectedSVGPos) {
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.putInt("selectedSVGPos", selectedSVGPos);
		editor.commit();
	}

	public static int getSelectedSVG(Context context) {
		if (sharedPrefs == null) {
			sharedPrefs = context.getSharedPreferences("ConfigEShop", Context.MODE_PRIVATE);
		}
		return sharedPrefs.getInt("selectedSVGPos", -1);
	}

	private static void checkInitConfig() {
		if (sharedPrefs == null) {
			throw new RuntimeException("Configuration have to be initialize with AppConfig.initConfig(Context context)");
		}
	}

	/**
	 * For test only. Switch environment without calling System.exit(0)
	 */
	public static void setEnvironment(int environment) {
		switch (environment) {

		case PREPROD:
			setProductionFlag(false);
			setPreProductionAndRestart(true);
			break;
		case PROD:
			setProductionFlag(true);
			setProductionAndRestart(true);
			break;
		default:
			setProductionFlag(false);
			break;
		}
	}

	private static void setProductionAndRestart(boolean shouldRestart) {
		Editor editPrefs = sharedPrefs.edit();
		editPrefs.putString("ESHOP_URL", ESHOP_URL_PROD);
		editPrefs.putString("ESHOP_URL_SECURE", ESHOP_URL_PROD_SECURE);
		editPrefs.commit();
		if (shouldRestart) {
			System.exit(0); // yes this is nasty, but Romain S. agree with this
		}
	}

	private static void setProductionFlag(boolean bProdFlag) {
		isProdEnvironment = bProdFlag;

	}

	public static boolean isInProduction() {
		return isProdEnvironment;
	}

	public static void setPreProductionAndRestart(boolean shouldRestart) {
		Editor editPrefs = sharedPrefs.edit();
		editPrefs.putString("ESHOP_URL", ESHOP_URL_PREPROD);
		editPrefs.commit();
		if (shouldRestart) {
			System.exit(0); // yes this is nasty, but Romain S. agree with this
		}
	}

	public static String getServerUrl() {

		checkInitConfig();

		if (isInDebugMode) {
			return sharedPrefs.getString("ESHOP_URL", ESHOP_URL_PREPROD);
		}
		return ESHOP_URL_PREPROD;
	}
	
	public static String getSecureServerUrl() {
		
		checkInitConfig();

		if (isInDebugMode) {
			return sharedPrefs.getString("ESHOP_URL_SECURE", ESHOP_URL_PREPROD_SECURE);
		}
		return ESHOP_URL_PREPROD_SECURE;
		
	}

	public static boolean isInDebugMode() {
		return isInDebugMode;
	}

	/**
	 * For test only !
	 */
	public static void forceDebugMode() {
		isInDebugMode = true;
	}

	public static String getAppVersion() {
		return appVersion;
	}

	public static String getAppName() {
		return appName;
	}

	public static HashMap<String, String[]> storeMap = new HashMap<String, String[]>();
	public static HashMap<String, Integer> storeName = new HashMap<String, Integer>();

	public static String getCurrencySymbol() {
		return currencySymbol;
	}

	public static int getMinimumFractionDigits() {
		return minimumFractionDigits;
	}

	public static int getMaximumFractionDigits() {
		return maximumFractionDigits;
	}
	
	 /**
 	 * @return Time in second between two basket synchronization with Omnia server which is set to 30 seconds 
 	 */
 	public static int getBasketSynchronizationPeriod() {
 		return basketSynchronizationPeriod;
 	}
}
