package com.smikeapps.parking.common.context;

import com.smikeapps.parking.comman.utils.AuthToken;
import com.smikeapps.parking.comman.utils.PrinterDetail;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class AccountPreference {
	
	
	private static final String PARKING_ACCOUNT = "parking_account";
	private static final String PARKING_LOGIN = "parking_login";
	private static final String PARKING_PASSWORD = "parking_password";


	private static final String CURRENT_SHOP_ID = "CurrentShopId";
	private static final String CURRENT_SLOT_ID = "vSlotId";
	
	private static final String SELECTED_PRINTER_MAC_ADDRESS = "vMacAddress";
	private static final String SELECTED_PRINTER_NAME = "vPrinterName";
	
	private static final String ACCESS_TOKEN = "AccessToken";
	private static final String CREATED_ON = "CreatedOn";
	private static final String USER_ID = "UserId";
	
	private static final String TOKEN_STRING = "tokenString";

	private static SharedPreferences accountPreference;

	private AccountPreference() {
	}

	public synchronized static void init(Context context) {

		if (accountPreference == null && context != null) {
			accountPreference = context.getApplicationContext()
					.getSharedPreferences(PARKING_ACCOUNT, Activity.MODE_PRIVATE);
		}
	}

	synchronized public static void setAuthToken(AuthToken token) {

		SharedPreferences.Editor editor = accountPreference.edit();
		if (token != null) {
			editor.putString(ACCESS_TOKEN, token.getAccessToken());
			editor.putString(CREATED_ON, token.getCreatedOn());
			editor.putString(USER_ID, token.getUserId());
		} else {
			editor.remove(ACCESS_TOKEN);
			editor.remove(CREATED_ON);
			editor.remove(USER_ID);
		}
		editor.commit();
	}

	public static AuthToken getAuthToken() {

		String accessToken = accountPreference.getString(ACCESS_TOKEN, null);
		String createOn = accountPreference.getString(CREATED_ON, null);
		String userId = accountPreference.getString(USER_ID, null);

		return new AuthToken(accessToken, userId, createOn);
	}
	
	public static String getTokenString() {

		return accountPreference.getString(TOKEN_STRING, "");
	}

	synchronized public static void setTokenString(String tokenString) {

		SharedPreferences.Editor editor = accountPreference.edit();
		editor.putString(TOKEN_STRING, tokenString);
		editor.commit();
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
	


	synchronized public static void setPrinter(String macAddress, String printerName) {
		SharedPreferences.Editor editor = accountPreference.edit();
		editor.putString(SELECTED_PRINTER_MAC_ADDRESS, macAddress);
		editor.putString(SELECTED_PRINTER_NAME, printerName);
		editor.commit();
	}

	synchronized public static PrinterDetail getPrinter() {
		PrinterDetail printerDetail = new PrinterDetail(accountPreference.getString(SELECTED_PRINTER_NAME, null), accountPreference.getString(SELECTED_PRINTER_MAC_ADDRESS, null));
		return printerDetail;
	}
	

	
}
