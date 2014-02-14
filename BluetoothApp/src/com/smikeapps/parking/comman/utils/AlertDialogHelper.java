package com.smikeapps.parking.comman.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.smikeapps.parking.R;
import com.smikeapps.parking.adapters.ScannerAdapter;


public class AlertDialogHelper {
	
	
	private static AlertDialog alertDialog;
	private static Dialog dialog;
	
	public interface SmikeAppException {
		public String getLocalizedMessage(Context context);
	}
	
	public static void showAlertDialogForLogout(Context context, String title, String message,
			DialogInterface.OnClickListener regConfirmOKBtnClick,DialogInterface.OnClickListener regConfirmCancelBtnClick, String negativeButtonLable, String positiveButtonLable) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		if (title != null) {
			builder.setTitle(title);
		}
		if (message != null) {
			builder.setMessage(message);
		}
		builder.setCancelable(false);

		builder.setNegativeButton(negativeButtonLable, regConfirmCancelBtnClick);
		builder.setPositiveButton(positiveButtonLable, regConfirmOKBtnClick);


		try {
			cancelCurrentAlertDialog ();
			alertDialog = builder.show();
		} catch ( Exception e ) {
			alertDialog = null;
			e.printStackTrace();
		}

	}
	
	public static void showAlertDialogForLogout(Context context, String title, String message,
			DialogInterface.OnClickListener regConfirmOKBtnClick, String positiveButtonLable) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		if (title != null) {
			builder.setTitle(title);
		}
		if (message != null) {
			builder.setMessage(message);
		}
		builder.setCancelable(false);

		builder.setPositiveButton(positiveButtonLable, regConfirmOKBtnClick);


		try {
			cancelCurrentAlertDialog ();
			alertDialog = builder.show();
		} catch ( Exception e ) {
			alertDialog = null;
			e.printStackTrace();
		}

	}
	
	public static void showAlertDialog(Context context, String title, String message,
			DialogInterface.OnClickListener regConfirmOKBtnClick,DialogInterface.OnClickListener regConfirmCancelBtnClick, String cancelButtonText, String okButtonText) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		if (title != null) {
			builder.setTitle(title);
		}
		if (message != null) {
			builder.setMessage(message);
		}
		builder.setCancelable(false);

		builder.setNegativeButton(cancelButtonText, regConfirmCancelBtnClick);
		builder.setPositiveButton(okButtonText, regConfirmOKBtnClick);


		try {
			cancelCurrentAlertDialog ();
			alertDialog = builder.show();
		} catch ( Exception e ) {
			alertDialog = null;
			e.printStackTrace();
		}
	}
	
	public static void showNoHardwareAlertDialog(Context context, String title, String message,
			DialogInterface.OnClickListener regConfirmOKBtnClick,DialogInterface.OnClickListener regConfirmCancelBtnClick, String cancelButtonText, String okButtonText) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		if (title != null) {
			builder.setTitle(title);
		}
		if (message != null) {
			builder.setMessage(message);
		}
		builder.setCancelable(false);

		builder.setNegativeButton(cancelButtonText, regConfirmCancelBtnClick);
		builder.setPositiveButton(okButtonText, regConfirmOKBtnClick);


		try {
			cancelCurrentAlertDialog ();
			alertDialog = builder.show();
		} catch ( Exception e ) {
			alertDialog = null;
			e.printStackTrace();
		}
	}
	
	public static void showAlertDialogForDevices ( Context context, ScannerAdapter adapter, 
			OnClickListener onScanButtonClicklistener, OnClickListener onCancleScanButtonClicklistener, OnItemClickListener onDeviceSelectedListner ) {
		
		try {
			cancelCurrentAlertDialog ();
			dialog = new Dialog ( context );
			dialog.setCancelable( false );
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView ( R.layout.dialog_discovered_printers );
			
			Button scanButton = ( Button ) dialog.findViewById( R.id.scanForDevicesButton );
			Button cancleScanButton = ( Button ) dialog.findViewById( R.id.cancleScanButton);
			ListView listView = (ListView) dialog.findViewById(R.id.printerList);
			
			listView.setAdapter(adapter);
			
			
			if(onScanButtonClicklistener == null){
				scanButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						cancelCurrentAlertDialog();
						
					}
				});
			}else{
				scanButton.setOnClickListener(onScanButtonClicklistener);
			}
			
			if(onCancleScanButtonClicklistener == null){
				cancleScanButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						cancelCurrentAlertDialog();
					}
				});
			}else{
				cancleScanButton.setOnClickListener(onCancleScanButtonClicklistener);
			}
			
			if(onDeviceSelectedListner == null){
				
			}
			
			dialog.show();
		} catch ( Exception e ) {
			alertDialog = null;
			e.printStackTrace();
		}
	}
	
	
	
	public static void cancelCurrentAlertDialog() {
		if (alertDialog != null && alertDialog.isShowing()) {
			alertDialog.cancel();
		}
		if ( dialog != null && dialog.isShowing() ) {
			dialog.cancel();
		}
	}
}
