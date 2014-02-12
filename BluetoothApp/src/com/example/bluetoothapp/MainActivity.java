package com.example.bluetoothapp;

import java.util.Set;

import com.smikeapps.parking.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.widget.ArrayAdapter;

public class MainActivity extends Activity {
	private BluetoothAdapter mBluetoothAdapter;
	Set<BluetoothDevice> pairedDevices;
	private ArrayAdapter<String> mArrayAdapter;
	// requestCode to turn on bluetooth
	private static final int REQUEST_ENABLE_BT = 1; 
	private static final int BLUTOOTH_DISCOVERABILITY_DURATION = 300; 
	private DiscoverDevicesHandler mDiscoverDevicesHandler;
	private static final int TWELVE_SECONDS =  12000;
	private static final int ONE_SECOND =  1000;
	private static final int CHECK_FOR_DEVICES =  REQUEST_ENABLE_BT + 1;
	private int mDiscoveryDuration ;
	private int mInquiryDuration ;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
		    // Device does not support Bluetooth
		}else{
			turnOnBlutooth();
		}

	}

	private void scanForDevices() {
		pairedDevices = mBluetoothAdapter.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
			// Loop through paired devices
			for (BluetoothDevice device : pairedDevices) {
				// Add the name and address to an array adapter to show in a
				// ListView
				mArrayAdapter
						.add(device.getName() + "\n" + device.getAddress());
			}
		}
	}

	// Create a BroadcastReceiver for ACTION_FOUND
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// Add the name and address to an array adapter to show in a
				// ListView
				mArrayAdapter
						.add(device.getName() + "\n" + device.getAddress());
			}
		}
	};

	private final BroadcastReceiver mBluetoothState = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (action.equals(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
				int var1 = intent.getIntExtra(
						mBluetoothAdapter.EXTRA_SCAN_MODE, 0);
				int var2 = intent.getIntExtra(
						mBluetoothAdapter.EXTRA_PREVIOUS_SCAN_MODE, 0);
				if (var1 == mBluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {

				} else if (var1 == mBluetoothAdapter.SCAN_MODE_CONNECTABLE) {

				} else if (var1 == mBluetoothAdapter.SCAN_MODE_NONE) {

				}

				if (var2 == mBluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {

				} else if (var2 == mBluetoothAdapter.SCAN_MODE_CONNECTABLE) {

				} else if (var2 == mBluetoothAdapter.SCAN_MODE_NONE) {

				}
			}
		}

	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	private void makeDeviceDiscoverable() {
		Intent discoverableIntent = new Intent(
				BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(
				BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, BLUTOOTH_DISCOVERABILITY_DURATION);
		startActivity(discoverableIntent);
	}
	
	private void turnOnBlutooth(){
		if (!mBluetoothAdapter.isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
	}
	
	private void startDiscoveringDevices(){
		mBluetoothAdapter.startDiscovery();
	}
	
	private void stopDiscoveringDevices(){
		mBluetoothAdapter.cancelDiscovery();
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == BLUTOOTH_DISCOVERABILITY_DURATION) {
			// sucessfull turning on bluetooth device

		} else if (requestCode == RESULT_CANCELED) {
			// failed to turn on bluetooth
		}else if (requestCode == REQUEST_ENABLE_BT) {
			// bluetooth turned on 
			mDiscoverDevicesHandler.sendEmptyMessageDelayed(CHECK_FOR_DEVICES, ONE_SECOND);
		}
	}

	private void registerForBluetoothStateChanges() {
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(
				mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		registerReceiver(mBluetoothState, filter); // Don't forget to unregister
													// during onDestroy
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private void registerForDeviceDiscovery(){
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter); // Don't forget to unregister
												// during onDestroy
	}

	@Override
	protected void onStart() {
		super.onStart();
		registerForDeviceDiscovery();
		registerForBluetoothStateChanges();
	}
	
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
		unregisterReceiver(mBluetoothState);
	}
	
	
	private class DiscoverDevicesHandler extends Handler{
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			mDiscoveryDuration++;
			switch(msg.what){
			
			case CHECK_FOR_DEVICES:
				if(mDiscoveryDuration != 12){
					// check if printer is found
					scanForDevices();
					mDiscoverDevicesHandler.sendEmptyMessageDelayed(CHECK_FOR_DEVICES, ONE_SECOND);
				}else if(mDiscoveryDuration >= 12){
					// discovery duration finished
				}
				
				
				break;

			default:
				break;
			}
		}
	}

}
