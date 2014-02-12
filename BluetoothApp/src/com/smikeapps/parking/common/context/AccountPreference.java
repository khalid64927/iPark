package com.smikeapps.parking.common.context;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class AccountPreference {
	
	
	private static final String PARKING_ACCOUNT = "parking_account";
	private static final String PARKING_LOGIN = "parking_login";
	private static final String PARKING_PASSWORD = "parking_password";


	private static final String CURRENT_SHOP_ID = "CurrentShopId";
	private static final String CURRENT_SLOT_ID = "vSlotId";
	
	private static final String SELECTED_PRINTER = "vMacAddress";

	private static SharedPreferences accountPreference;

	private AccountPreference() {
	}

	public synchronized static void init(Context context) {

		if (accountPreference == null && context != null) {
			accountPreference = context.getApplicationContext()
					.getSharedPreferences(PARKING_ACCOUNT, Activity.MODE_PRIVATE);
		}
	}

	public static String getLogin() {

		return accountPreference.getString(PARKING_LOGIN, "");
	}

	public static String getPassword() {

		return accountPreference.getString(PARKING_PASSWORD, "");
	}

	synchronized public static void setLogin(String login) {

		SharedPreferences.Editor editor = accountPreference.edit();
		editor.putString(PARKING_LOGIN, login);
		editor.commit();
	}

	synchronized public static void setPassword(String password) {

		SharedPreferences.Editor editor = accountPreference.edit();
		editor.putString(PARKING_PASSWORD, password);
		editor.commit();
	}

	synchronized public static void deleteLogin() {

		SharedPreferences.Editor editor = accountPreference.edit();
		editor.remove(PARKING_LOGIN);
		editor.commit();
	}

	synchronized public static void deletePassword() {

		SharedPreferences.Editor editor = accountPreference.edit();
		editor.remove(PARKING_PASSWORD);
		editor.commit();
	}

	synchronized public static void setSlotId(String iSlotSaleId) {

		SharedPreferences.Editor editor = accountPreference.edit();
		editor.putString(CURRENT_SLOT_ID, iSlotSaleId);
		editor.commit();
	}

	synchronized public static String getSlotId() {
		return accountPreference.getString(CURRENT_SLOT_ID, null);
	}
	


	synchronized public static void setPrinter(String macAddress) {
		SharedPreferences.Editor editor = accountPreference.edit();
		editor.putString(SELECTED_PRINTER, macAddress);
		editor.commit();
	}

	synchronized public static String getPrinter() {
		return accountPreference.getString(SELECTED_PRINTER, null);
	}
	

	
}
