package com.smikeapps.parking.fragments;

import java.util.ArrayList;
import java.util.Set;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.smikeapps.parking.R;
import com.smikeapps.parking.activities.DiscoverPrinterActivity;
import com.smikeapps.parking.activities.HomeBaseActivity;
import com.smikeapps.parking.activities.HomeScreenActivity;
import com.smikeapps.parking.adapters.ScannerAdapter;
import com.smikeapps.parking.comman.utils.AlertDialogHelper;
import com.smikeapps.parking.comman.utils.PrinterDetail;
import com.smikeapps.parking.common.context.AccountPreference;
import com.smikeapps.parking.interfaces.BackButtonInterface;
import com.zebra.android.comm.BluetoothPrinterConnection;
import com.zebra.android.comm.ZebraPrinterConnection;
import com.zebra.android.comm.ZebraPrinterConnectionException;
import com.zebra.android.devdemo.util.DemoSleeper;
import com.zebra.android.printer.PrinterLanguage;
import com.zebra.android.printer.ZebraPrinter;
import com.zebra.android.printer.ZebraPrinterFactory;
import com.zebra.android.printer.ZebraPrinterLanguageUnknownException;

public class PrintPVTFragment extends Fragment implements BackButtonInterface {

	private View mRoootView;
	private Button mPrintPVTButton;
	private ZebraPrinter printer;
	private ZebraPrinterConnection zebraPrinterConnection;

	private ScannerAdapter adapter;

	private BluetoothAdapter mBluetoothAdapter;
	private static final String TAG = PrintPVTFragment.class.getName();
	// private
	Set<BluetoothDevice> pairedDevices;
	// private ArrayAdapter<String> mArrayAdapter;
	// requestCode to turn on bluetooth
	private static final int REQUEST_ENABLE_BT = 1;
	private static final int BLUTOOTH_DISCOVERABILITY_DURATION = 300;
	private DiscoverDevicesHandler mDiscoverDevicesHandler;
	private static final int TWELVE_SECONDS = 12000;
	private static final int ONE_SECOND = 1000;
	private static final int CHECK_FOR_DEVICES = REQUEST_ENABLE_BT + 1;
	private int mDiscoveryDuration;
	private int mInquiryDuration;
	private ArrayList<PrinterDetail> printers;
	private boolean onPauseCalled = false;

	private static final int GET_MAC_ADDDRESS_REQUEST_CODE = CHECK_FOR_DEVICES + 1;
	public static final int GET_MAC_ADDDRESS_RESULT_CODE = GET_MAC_ADDDRESS_REQUEST_CODE + 1;

	private ProgressDialog progressDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRoootView = inflater.inflate(R.layout.fragment_confirm_pvt, null);
		initUI();
		setUpActionBar();
		return mRoootView;
	}

	private void setUpActionBar() {
		((HomeBaseActivity) getActivity()).getSupportActionBar().setTitle(
				getActivity().getString(R.string.print_pvt_button_lable));
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.print_pvt_frament_menu, menu);
		((HomeBaseActivity) getActivity()).getSupportActionBar()
				.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean response = false;
		switch (item.getItemId()) {
		case R.id.printers:
			mDiscoverDevicesHandler.removeMessages(CHECK_FOR_DEVICES);
			disconnect();
			Intent intent = new Intent(getActivity(),
					DiscoverPrinterActivity.class);
			Bundle extra = new Bundle();
			extra.putBoolean(
					DiscoverPrintersFragment.EXTRA_TAG_IS_CALLLED_FOR_RESULT,
					true);
			intent.putExtras(extra);
			startActivity(intent);
			response = true;
			openPrinterDiscovery(false);
			break;

		case R.id.cancle_scan_of_printers:
			mDiscoverDevicesHandler.removeMessages(CHECK_FOR_DEVICES);
			disconnect();
			response = true;
			break;

		default:
			break;
		}

		return response;
	}

	private void initUI() {
		mPrintPVTButton = (Button) mRoootView.findViewById(R.id.printPVTButton);
		mPrintPVTButton.setOnClickListener(onPrintPVTButtonClicked);

		mDiscoverDevicesHandler = new DiscoverDevicesHandler();
		printers = new ArrayList<PrinterDetail>();
	}

	public static PrintPVTFragment newInstance(int tabPosition) {
		PrintPVTFragment fragment = new PrintPVTFragment();
		Bundle args = new Bundle();
		args.putInt(HomeScreenActivity.TAB_POSITION, tabPosition);
		fragment.setArguments(args);
		return fragment;
	}

	private View.OnClickListener onPrintPVTButtonClicked = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			showProgressDialog("intializing print...");
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			if (mBluetoothAdapter == null) {
				// Device does not support Bluetooth
				Log.d(TAG,
						"onPrintPVTButtonClicked   :: bluetooth  hardware not present");
			} else {
				turnOnBlutooth();
			}
		}
	};

	private void scanForDevices() {
		pairedDevices = mBluetoothAdapter.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
			// Loop through paired devices
			for (BluetoothDevice device : pairedDevices) {
				// Add the name and address to an array adapter to show in a
				// ListView
				if (PrinterDetail.addPrinter(device.getName(),
						device.getAddress())) {
					Log.d(TAG, "scanForDevices   :: device found and added");
				} else {
					Log.d(TAG, "scanForDevices   :: device found not added");
				}
				/*
				 * mArrayAdapter .add(device.getName() + "\n" +
				 * device.getAddress());
				 */
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
				if (PrinterDetail.addPrinter(device.getName(),
						device.getAddress())) {
					Log.d(TAG,
							"BroadcastReceiver :: mReceiver  :: device found and added");
					notifyDataSetCanged();
				} else {
					Log.d(TAG,
							"BroadcastReceiver :: mReceiver  :: device found not added");
				}
				/*
				 * mArrayAdapter .add(device.getName() + "\n" +
				 * device.getAddress());
				 */
			}
		}
	};

	private void notifyDataSetCanged() {
		if (onPauseCalled) {
			return;
		}
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				adapter.notifyDataSetChanged();

			}
		});
	}

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

	private void makeDeviceDiscoverable() {
		Intent discoverableIntent = new Intent(
				BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(
				BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
				BLUTOOTH_DISCOVERABILITY_DURATION);
		startActivity(discoverableIntent);
	}

	private void turnOnBlutooth() {
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			getParentFragment().startActivityForResult(enableBtIntent,
					REQUEST_ENABLE_BT);
		} else {
			if (AccountPreference.getPrinter() != null) {
				startConnectionTest();
			} else {
				showProgressDialog("Printer not set, moving to printer discovery");
				// start discovery
				openPrinterDiscovery(true);
			}
		}
	}

	private void startConnectionTest() {
		// check connection
		showProgressDialog("Printing your PVT...");
		new Thread(new Runnable() {
			public void run() {
				enableTestButton(false);
				Looper.prepare();
				doConnectionTest();
				Looper.loop();
				Looper.myLooper().quit();
				hideProgressDialog();
			}
		}).start();
	}

	private View.OnClickListener onScanButtonClicklistener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.d(TAG, "onScanButtonClicklistener.....");
			AlertDialogHelper.cancelCurrentAlertDialog();

		}
	};

	private View.OnClickListener onCancleScanButtonClicklistener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.d(TAG, "onCancleScanButtonClicklistener.....");
			// / AlertDialogHelper.cancelCurrentAlertDialog();

		}
	};

	private OnItemClickListener onDeviceSelectedListner = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			Log.d(TAG, "onDeviceSelectedListner....." + position);
			// AlertDialogHelper.cancelCurrentAlertDialog();
		}
	};

	protected void enableTestButton(final boolean enabled) {
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				mPrintPVTButton.setEnabled(enabled);
			}
		});

	}

	private void doConnectionTest() {
		printer = connect();
		if (printer != null) {
			sendTestLabel();
		} else {
			disconnect();
		}
	}

	private void showAlertDialog(final String title, final String message,
			final String okButtonLable,
			final DialogInterface.OnClickListener regConfirmOKBtnClick) {
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				hideProgressDialog();
				AlertDialogHelper.showAlertDialogForLogout(getActivity(),
						title, message, regConfirmOKBtnClick, okButtonLable);
			}
		});
	}

	/* Progress dialog management */

	protected void showProgressDialog(final String loadingMessage) {
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				if (progressDialog == null) {
					progressDialog = ProgressDialog.show(getActivity(), "",
							loadingMessage, true);
				} else {
					if (progressDialog != null && progressDialog.isShowing()) {
						progressDialog.setMessage(loadingMessage);
					}
				}
			}
		});

	}

	/**
	 * Closing progress dialog if shown
	 */
	protected void hideProgressDialog() {
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				if (progressDialog != null) {
					progressDialog.dismiss();
				}

			}
		});

	}

	public ZebraPrinter connect() {
		showProgressDialog("Connecting...");
		zebraPrinterConnection = null;
		if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()
				&& !mBluetoothAdapter.isDiscovering()) {
			zebraPrinterConnection = new BluetoothPrinterConnection(
					AccountPreference.getPrinter());
			// SettingsHelper.saveBluetoothAddress(this,
			// getMacAddressFieldText());
		} else {
			Log.d(TAG,
					"connect  :: bluetooth is not present or OFF or in Discovering device now ");
		}

		try {
			zebraPrinterConnection.open();
		} catch (ZebraPrinterConnectionException e) {
			showAlertDialog("Comm Error",
					"Communication error! Disconnecting .....", "Ok",
					logoutOKBtnClick);
			DemoSleeper.sleep(1000);
			disconnect();
		}

		ZebraPrinter printer = null;

		if (zebraPrinterConnection.isConnected()) {
			try {
				printer = ZebraPrinterFactory
						.getInstance(zebraPrinterConnection);
				showProgressDialog("Determining Printer Language");
				PrinterLanguage pl = printer.getPrinterControlLanguage();
			} catch (ZebraPrinterConnectionException e) {
				showAlertDialog("Communication Error",
						"Unable to connect with printer! Disconnecting .....",
						"Ok", logoutOKBtnClick);
				printer = null;
				DemoSleeper.sleep(1000);
				disconnect();
			} catch (ZebraPrinterLanguageUnknownException e) {
				showAlertDialog(
						"Communication Error",
						"Unknown Printer Language! unable to connect with printer Disconnecting .....",
						"Ok", logoutOKBtnClick);
				printer = null;
				DemoSleeper.sleep(1000);
				disconnect();
			}
		}

		return printer;
	}

	/* Cancel Registration ok button click */
	private android.content.DialogInterface.OnClickListener logoutOKBtnClick = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			Log.v(TAG, "regCancelOKBtnClick()");

		}
	};

	/* Cancel Registration ok button click */
	private android.content.DialogInterface.OnClickListener logoutCancelBtnClick = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			Log.v(TAG, "regCancelOKBtnClick()");

		}

	};

	private void sendTestLabel() {
		showProgressDialog("Sending print job...");
		try {
			byte[] configLabel = getConfigLabel();
			zebraPrinterConnection.write(configLabel);
			DemoSleeper.sleep(1500);
			if (zebraPrinterConnection instanceof BluetoothPrinterConnection) {
				String friendlyName = ((BluetoothPrinterConnection) zebraPrinterConnection)
						.getFriendlyName();
				DemoSleeper.sleep(500);
			}
		} catch (ZebraPrinterConnectionException e) {
			showAlertDialog("Comm Error", " " + e.getMessage()
					+ "! Disconnecting .....", "Ok", logoutOKBtnClick);
		} finally {
			disconnect();
		}
	}

	/*
	 * Returns the command for a test label depending on the printer control
	 * language The test label is a box with the word "TEST" inside of it
	 * 
	 * _________________________ | | | | | TEST | | | | |
	 * |_______________________|
	 */
	private byte[] getConfigLabel() {
		PrinterLanguage printerLanguage = printer.getPrinterControlLanguage();

		byte[] configLabel = null;
		if (printerLanguage == PrinterLanguage.ZPL) {
			configLabel = "^XA^FO17,16^GB379,371,8^FS^FT65,255^A0N,135,134^FDTEST^FS^XZ"
					.getBytes();
		} else if (printerLanguage == PrinterLanguage.CPCL) {
			String cpclConfigLabel = "! 0 200 200 406 1\r\n"
					+ "ON-FEED IGNORE\r\n" + "BOX 20 20 380 380 8\r\n"
					+ "T 0 6 137 177 TEST\r\n" + "PRINT\r\n";
			configLabel = cpclConfigLabel.getBytes();
		}
		return configLabel;
	}

	public void disconnect() {
		try {
			showProgressDialog("Disconnecting...");
			if (zebraPrinterConnection != null) {
				zebraPrinterConnection.close();
			}
			showProgressDialog("Printer not connected");
		} catch (ZebraPrinterConnectionException e) {
			showAlertDialog("Comm Error",
					"Communication error! Disconnecting .....", "Ok",
					logoutOKBtnClick);
		} finally {
			enableTestButton(true);
		}
	}

	private void startDiscoveringDevices() {
		stopDiscoveringDevices();
		mBluetoothAdapter.startDiscovery();
		mDiscoverDevicesHandler.sendEmptyMessageDelayed(CHECK_FOR_DEVICES,
				ONE_SECOND);
	}

	private void stopDiscoveringDevices() {
		mBluetoothAdapter.cancelDiscovery();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == BLUTOOTH_DISCOVERABILITY_DURATION) {
			// successful turning on bluetooth device

		} else if (requestCode == getActivity().RESULT_CANCELED) {
			// failed to turn on Bluetooth
		} else if (requestCode == REQUEST_ENABLE_BT && resultCode == 1) {
			// Bluetooth turned on successfully
			if (AccountPreference.getPrinter() != null) {
				startConnectionTest();
			} else {
				showProgressDialog("Bluetooth turned on, No printer set ");
				openPrinterDiscovery(true);
			}
			// mDiscoverDevicesHandler.sendEmptyMessageDelayed(CHECK_FOR_DEVICES,
			// ONE_SECOND);
		} else if (requestCode == GET_MAC_ADDDRESS_REQUEST_CODE
				&& resultCode == GET_MAC_ADDDRESS_RESULT_CODE) {
			startConnectionTest();
		}
	}

	private void openPrinterDiscovery(boolean isForResult) {
		disconnect();

		Intent intent = new Intent(getActivity(), DiscoverPrinterActivity.class);
		Bundle extra = new Bundle();
		extra.putBoolean(
				DiscoverPrintersFragment.EXTRA_TAG_IS_CALLLED_FOR_RESULT, true);
		intent.putExtras(extra);
		if (isForResult) {
			startActivityForResult(intent, GET_MAC_ADDDRESS_REQUEST_CODE);
		} else {
			startActivity(intent);
		}

	}

	private void registerForBluetoothStateChanges() {
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(
				mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		getActivity().registerReceiver(mBluetoothState, filter); // Don't forget
																	// to
																	// unregister
		// during onDestroy
	}

	private void registerForDeviceDiscovery() {
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		getActivity().registerReceiver(mReceiver, filter); // Don't forget to
															// unregister
		// during onDestroy
	}

	@Override
	public void onStart() {
		super.onStart();
		registerForDeviceDiscovery();
		registerForBluetoothStateChanges();
	}

	@Override
	public void onResume() {
		super.onResume();
		onPauseCalled = false;
	}

	@Override
	public void onPause() {
		super.onPause();
		hideProgressDialog();
		onPauseCalled = true;
		mDiscoverDevicesHandler.removeMessages(CHECK_FOR_DEVICES);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(mReceiver);
		getActivity().unregisterReceiver(mBluetoothState);
	}

	private class DiscoverDevicesHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			mDiscoveryDuration++;
			switch (msg.what) {

			case CHECK_FOR_DEVICES:
				if (mDiscoveryDuration <= 5) {
					// check if printer is found
					scanForDevices();
					mDiscoverDevicesHandler.sendEmptyMessageDelayed(
							CHECK_FOR_DEVICES, ONE_SECOND);
				} else if (mDiscoveryDuration >= 5) {
					// discovery duration finished
					mDiscoveryDuration = 0;
					mDiscoverDevicesHandler.removeMessages(CHECK_FOR_DEVICES);
					disconnect();
				}

				break;

			default:
				break;
			}
		}
	}

	@Override
	public boolean onBackButtonPressed() {
		 getParentFragment().getChildFragmentManager().popBackStack();
		return true;
	}

	@Override
	public boolean onNavigationBackButtonPressed() {
		getParentFragment().getChildFragmentManager().popBackStack();
		return true;
	}
}
