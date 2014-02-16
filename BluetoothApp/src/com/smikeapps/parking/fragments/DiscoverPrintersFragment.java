package com.smikeapps.parking.fragments;

import java.util.Set;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.smikeapps.parking.R;
import com.smikeapps.parking.activities.HomeBaseActivity;
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

public class DiscoverPrintersFragment extends Fragment implements BackButtonInterface {

	private View mRoootView;
	private ListView mDiscoveredDevicesListView;
	private ZebraPrinter printer;
	private ZebraPrinterConnection zebraPrinterConnection;
	private ScannerAdapter adapter;
	private BluetoothAdapter mBluetoothAdapter;
	private static final String TAG = DiscoverPrintersFragment.class.getSimpleName();
	Set<BluetoothDevice> pairedDevices;
	// requestCode to turn on bluetooth
	private static final int REQUEST_ENABLE_BT = 1;
	private static final int BLUTOOTH_DISCOVERABILITY_DURATION = 300;
	private DiscoverDevicesHandler mDiscoverDevicesHandler;
	private static final int TWELVE_SECONDS = 12000;
	private static final int ONE_SECOND = 1000;
	private static final int CHECK_FOR_DEVICES = REQUEST_ENABLE_BT + 1;
	private int mDiscoveryDuration;
//	private ArrayList<PrinterDetail> printers;
	public static final String EXTRA_TAG_IS_CALLLED_FOR_RESULT = "is_called_for_result";

	private ProgressDialog progressDialog;
	private boolean isPrinterSelected = false;
	private boolean isCalledForResult = false;
	private boolean isCancled = false;
	private boolean isSacnCompleted = false;
	
	private MenuItem mActionItemStartScan;
	private MenuItem mActionItemStopScan;

	public DiscoverPrintersFragment() {
	}

	public static DiscoverPrintersFragment newInstance(Bundle extra) {
		DiscoverPrintersFragment fragment = new DiscoverPrintersFragment();
		/*
		 * Bundle extra = new Bundle(); extra.putBoolean("is_called_for_result",
		 * isCalledOnResult);
		 */
		fragment.setArguments(extra);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate   :: ...");
		setUpActionBar();
//		printers = new ArrayList<PrinterDetail>();
		isCalledForResult = getArguments().containsKey(EXTRA_TAG_IS_CALLLED_FOR_RESULT);
		// PrinterDetail.getPrinterList().clear();
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRoootView = inflater.inflate(R.layout.fragment_discover_printers, null);
		mDiscoveredDevicesListView = (ListView) mRoootView.findViewById(R.id.printerList);
		mDiscoverDevicesHandler = new DiscoverDevicesHandler();
		return mRoootView;
	}

	private void setUpActionBar() {
		Log.d(TAG, "setUpActionBar   :: ...");
		((HomeBaseActivity) getActivity()).getSupportActionBar().setTitle(getActivity().getString(R.string.discover_printer));
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		((HomeBaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setHasOptionsMenu(true);
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume   :: ...");
		registerForDeviceDiscovery();
		registerForBluetoothStateChanges();
		if (isCalledForResult && !isCancled) {
			startProcess();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, "onPause   :: ...");
		hideProgressDialog();
		getActivity().unregisterReceiver(mReceiver);
		getActivity().unregisterReceiver(mBluetoothState);
		mDiscoverDevicesHandler.removeMessages(CHECK_FOR_DEVICES);
		enableScanButton(true);
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "onStart   :: ...");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG, "onStop()   :: ...");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy()   :: ...");
	}

	@Override
	public boolean onBackButtonPressed() {
		boolean isBackHandled = false;
		if (isCalledForResult) {
			getActivity().setResult(PrintPVTFragment.GET_MAC_ADDDRESS_RESULT_CODE);
			getActivity().finish();
			isBackHandled = true;
		}
		return isBackHandled;
	}

	@Override
	public boolean onNavigationBackButtonPressed() {
		boolean isBackHandled = false;
		if (isCalledForResult) {
			getActivity().setResult(PrintPVTFragment.GET_MAC_ADDDRESS_RESULT_CODE);
			getActivity().finish();
			isBackHandled = true;
		}
		return isBackHandled;
	}

	private void turnOnBlutooth() {
		Log.d(TAG, "turnOnBlutooth   :: ...");
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			getActivity().startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		} else {
				// start discovery
				// showProgressDialog("starting printer discovery");
				setUpDiscovery();
		}
	}

	private void setUpDiscovery() {
		Log.d(TAG, "setUpDiscovery   :: ...");
		setDeviceAdapter();
		startDiscoveringDevices();
		Toast.makeText(getActivity(), "starting scanning process ...", Toast.LENGTH_SHORT).show();
		mDiscoverDevicesHandler.sendEmptyMessageDelayed(CHECK_FOR_DEVICES, ONE_SECOND);
	}

	private void scanForPairedDevices() {
		Log.d(TAG, "scanForPairedDevices   :: ...");
		pairedDevices = mBluetoothAdapter.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
			// Loop through paired devices
			for (BluetoothDevice device : pairedDevices) {
				// Add the name and address to an array adapter to show in a
				// ListView
			//	printers.add(new PrinterDetail(device.getName(), device.getAddress()));
				
				if (PrinterDetail.addPrinter(device.getName(), device.getAddress())) {
					Log.d(TAG, "scanForDevices   :: device found and added");
					notifyPrinterDataSetCanged();
				} else {
					Log.d(TAG, "scanForDevices   :: device found not added");
				}

			}
		}
	}
	
	private void notifyPrinterDataSetCanged(){
		Log.d(TAG, "notifyPrinterDataSetCanged :: .....");
		getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if(adapter != null){
					adapter.notifyDataSetChanged();
				}
			}
		});
	}

	// Create a BroadcastReceiver for ACTION_FOUND
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// Add the name and address to an array adapter to show in a
				// ListView

				/*printers.add(new PrinterDetail(device.getName(), device.getAddress()));
				setDeviceAdapter();*/
				
				if (PrinterDetail.addPrinter(device.getName(), device.getAddress())) {
					Log.d(TAG, "BroadcastReceiver :: mReceiver  :: device found and added");
					notifyPrinterDataSetCanged();
				} else {
					Log.d(TAG, "BroadcastReceiver :: mReceiver  :: device found not added");
				}
				 
			}
		}
	};

	/*
	 * private void notifyDataSetCanged() { getActivity().runOnUiThread(new
	 * Runnable() {
	 * 
	 * @Override public void run() { adapter.notifyDataSetChanged();
	 * 
	 * } }); }
	 */

	private final BroadcastReceiver mBluetoothState = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.d(TAG, "onReceive() ::...  bluetooth state   :: ...");
			if (action.equals(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
				int var1 = intent.getIntExtra(mBluetoothAdapter.EXTRA_SCAN_MODE, 0);
				int var2 = intent.getIntExtra(mBluetoothAdapter.EXTRA_PREVIOUS_SCAN_MODE, 0);
				if (var1 == mBluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
					// setUpDiscovery();
				} else if (var1 == mBluetoothAdapter.SCAN_MODE_CONNECTABLE) {
					// setUpDiscovery();
				} else if (var1 == mBluetoothAdapter.SCAN_MODE_NONE) {
					setUpDiscovery();
				}

				if (var2 == mBluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
					// discoverable mode
				} else if (var2 == mBluetoothAdapter.SCAN_MODE_CONNECTABLE) {
					// not in discoverable mode but still able to receive
					// connections
				} else if (var2 == mBluetoothAdapter.SCAN_MODE_NONE) {
					// not in discoverable mode and unable to receive
					// connections
				}
			}
		}

	};

	private void makeDeviceDiscoverable() {
		Log.d(TAG, "makeDeviceDiscoverable   :: ...");
		Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, BLUTOOTH_DISCOVERABILITY_DURATION);
		startActivity(discoverableIntent);
	}

	private void startConnectionTest() {
		Log.d(TAG, "startConnectionTest   :: ...");
		// check connection
		showProgressDialog("Printing your PVT...");
		new Thread(new Runnable() {
			public void run() {
				enableScanButton(false);
				Looper.prepare();
				doConnectionTest();
				Looper.loop();
				Looper.myLooper().quit();
				hideProgressDialog();
			}
		}).start();
	}

	protected void enableScanButton(final boolean enabled) {
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				Log.d(TAG, "enableScanButton  :: ....."+enabled);
				
				if(mActionItemStartScan ==  null){
					return;
				}
				
				if(enabled){
					mActionItemStartScan.setVisible(true);
					mActionItemStopScan.setVisible(false);
				}else{
					mActionItemStartScan.setVisible(false);
					mActionItemStopScan.setVisible(true);
				}
				/* mActionItemStartScan.setEnabled(enabled);
				 mActionItemStopScan.setEnabled(!enabled);*/
				 
			}
		});

	}

	private void doConnectionTest() {
		Log.d(TAG, "doConnectionTest   :: ...");
		printer = connect();
		if (printer != null) {
			sendTestLabel();
		} else {
			disconnect();
		}
	}

	/* Progress dialog management */

	protected void showProgressDialog(final String loadingMessage) {
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				Log.d(TAG, "showProgressDialog.....");
				if (progressDialog == null) {
					Log.d(TAG, "showProgressDialog.. progress is null...");
					progressDialog = ProgressDialog.show(getActivity(), "",
							loadingMessage, true);
				} else if (progressDialog != null && progressDialog.isShowing()) {
					Log.d(TAG, "showProgressDialog..is sshowing...");
					progressDialog.setMessage(loadingMessage);
				}else if(progressDialog != null && !progressDialog.isShowing() && (AlertDialogHelper.alertDialog != null && !AlertDialogHelper.alertDialog.isShowing())) {
					Log.d(TAG, "showProgressDialog..alert dialog is not visible...");
					progressDialog = ProgressDialog.show(getActivity(), "",
							loadingMessage, true);
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
					progressDialog = null;
				}

			}
		});

	}

	public ZebraPrinter connect() {
		Log.d(TAG, "connect.....");
		showProgressDialog("Connecting to printer "+AccountPreference.getPrinter().getFriendlyName() + "\n"+AccountPreference.getPrinter().getMacAddress() );
		zebraPrinterConnection = null;
		if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()
				&& !mBluetoothAdapter.isDiscovering()) {
			zebraPrinterConnection = new BluetoothPrinterConnection(
					AccountPreference.getPrinter().getMacAddress());
			
		} else {
			Log.d(TAG,
					"connect  :: bluetooth is not present or OFF or in Discovering device now ");
		}

		if(zebraPrinterConnection == null){
			if(AccountPreference.getPrinter() != null){
				showAlertDialog("Communication Error",
						"Counld no connect to Printer Disconnecting .....", "Ok",
						errorDialogOkBtnClick);
			}else{
				showAlertDialog("Communication Error",
						"Counld no connect to Printer :"+AccountPreference.getPrinter().getFriendlyName() + "\n"+AccountPreference.getPrinter().getMacAddress()+"\n"+"Disconnecting .....", "Ok",
						errorDialogOkBtnClick);
			}
		
			return null;
		}
		
		try {
			zebraPrinterConnection.open();
		} catch (ZebraPrinterConnectionException e) {
			if(AccountPreference.getPrinter() != null){
				showAlertDialog("Communication Error",
						"Counld no connect to Printer Disconnecting .....", "Ok",
						errorDialogOkBtnClick);
			}else{
				showAlertDialog("Communication Error",
						"Counld no connect to Printer :"+AccountPreference.getPrinter().getFriendlyName() + "\n"+AccountPreference.getPrinter().getMacAddress()+"\n"+"Disconnecting .....", "Ok",
						errorDialogOkBtnClick);
			}
			return null;
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
				return printer;
				/*DemoSleeper.sleep(1000);
				disconnect();*/
			}
		}

		return printer;
	}
	
	/* Cancel Registration ok button click */
	private android.content.DialogInterface.OnClickListener errorDialogOkBtnClick = new DialogInterface.OnClickListener() {
		public void onClick(final DialogInterface dialog, int which) {
			Log.v(TAG, "regCancelOKBtnClick()");
			getActivity().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					dialog.dismiss();
				}
			});
		}
	};

	/* Cancel Registration ok button click */
	private android.content.DialogInterface.OnClickListener logoutOKBtnClick = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			Log.v(TAG, "regCancelOKBtnClick()");
			dialog.dismiss();
		}
	};

	private android.content.DialogInterface.OnClickListener noHardwareOkButtonClicked = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			Log.v(TAG, "noHardwareOkButtonClicked()");
			getActivity().onBackPressed();
		}
	};

	/* Cancel Registration ok button click */
	private android.content.DialogInterface.OnClickListener logoutCancelBtnClick = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			Log.v(TAG, "regCancelOKBtnClick()");

		}

	};

	private void showAlertDialog(final String title, final String message, final String okButtonLable,
			final DialogInterface.OnClickListener regConfirmOKBtnClick) {
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				hideProgressDialog();
				AlertDialogHelper.showAlertDialogForLogout(getActivity(), title, message, regConfirmOKBtnClick, okButtonLable);
			}
		});
	}

	private void sendTestLabel() {
		Log.d(TAG, "sendTestLabel   :: ...");
		showProgressDialog("Sending print job...");
		try {
			byte[] configLabel = getConfigLabel();
			zebraPrinterConnection.write(configLabel);
			DemoSleeper.sleep(1500);
			if (zebraPrinterConnection instanceof BluetoothPrinterConnection) {
				String friendlyName = ((BluetoothPrinterConnection) zebraPrinterConnection).getFriendlyName();
				DemoSleeper.sleep(500);
			}
		} catch (ZebraPrinterConnectionException e) {
			showAlertDialog("Comm Error", " " + e.getMessage() + "! Disconnecting .....", "Ok", logoutOKBtnClick);
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
		Log.d(TAG, "getConfigLabel   :: ...");
		PrinterLanguage printerLanguage = printer.getPrinterControlLanguage();

		byte[] configLabel = null;
		if (printerLanguage == PrinterLanguage.ZPL) {
			configLabel = "^XA^FO17,16^GB379,371,8^FS^FT65,255^A0N,135,134^FDTEST^FS^XZ".getBytes();
		} else if (printerLanguage == PrinterLanguage.CPCL) {
			String cpclConfigLabel = "! 0 200 200 406 1\r\n" + "ON-FEED IGNORE\r\n" + "BOX 20 20 380 380 8\r\n" + "T 0 6 137 177 TEST\r\n"
					+ "PRINT\r\n";
			configLabel = cpclConfigLabel.getBytes();
		}
		return configLabel;
	}

	public void disconnect() {
		Log.d(TAG, "disconnect.....");
		try {
			showProgressDialog("Disconnecting printer connection...");
			if (zebraPrinterConnection != null) {
				zebraPrinterConnection.close();
			}
		} catch (ZebraPrinterConnectionException e) {
			showAlertDialog("Connection Error",
					"Unable to disconnectec!", "Ok",
					logoutOKBtnClick);
		} finally {
			enableScanButton(true);
			hideProgressDialog();
		}
	}
	private void startDiscoveringDevices() {
		Log.d(TAG, "startDiscoveringDevices   :: ...");
		stopDiscoveringDevices();
		mBluetoothAdapter.startDiscovery();
		mDiscoverDevicesHandler.sendEmptyMessageDelayed(CHECK_FOR_DEVICES, ONE_SECOND);
	}

	private void stopDiscoveringDevices() {
		Log.d(TAG, "stopDiscoveringDevices   :: ...");
		mBluetoothAdapter.cancelDiscovery();
	}

	private void registerForBluetoothStateChanges() {
		Log.d(TAG, "registerForBluetoothStateChanges   :: ...");
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		getActivity().registerReceiver(mBluetoothState, filter);
	}

	private void registerForDeviceDiscovery() {
		Log.d(TAG, "registerForDeviceDiscovery   :: ...");
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		getActivity().registerReceiver(mReceiver, filter);
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

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		Log.d(TAG, "onCreateOptionsMenu   ::");
		menu.clear();
		inflater.inflate(R.menu.discover_printers_menu, menu);
		mActionItemStartScan = menu.getItem(0);
		mActionItemStopScan = menu.getItem(1);
		mActionItemStartScan.setVisible(false);
		mActionItemStopScan.setVisible(false);
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(TAG, "onOptionsItemSelected   ::");
		boolean response = false;
		switch (item.getItemId()) {
		case R.id.scan_for_printers:
			response = true;
			if(!isSacnCompleted){
				Toast.makeText(getActivity(), "scanning is in progress ...", Toast.LENGTH_SHORT).show();
				break;
			}
			startProcess();
			break;

		case R.id.cancle_scan_of_printers:
			response = true;
			if(isSacnCompleted){
				Toast.makeText(getActivity(), "No scanning is happening", Toast.LENGTH_SHORT).show();
				break;
			}
			mDiscoverDevicesHandler.removeMessages(CHECK_FOR_DEVICES);
			disconnect();
			break;

		default:
			break;
		}

		return response;
	}
	
	private void startProcess() {
		enableScanButton(false);
		if (mBluetoothAdapter == null) {
			// Device does not support Bluetooth
			Log.d(TAG, "onOptionsItemSelected   :: bluetooth  hardware not present");
			showAlertDialog("No Hardware", "No bluetooth hardware prent on the device ! Disconnecting .....", "Ok",
					noHardwareOkButtonClicked);
		} else {
			turnOnBlutooth();
		}
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
					scanForPairedDevices();
					mDiscoverDevicesHandler.sendEmptyMessageDelayed(CHECK_FOR_DEVICES, ONE_SECOND);
				} else if (mDiscoveryDuration >= 5) {
					mDiscoveryDuration = 0;
					isSacnCompleted = true;
					Toast.makeText(getActivity(), "Finished scanning for devices", Toast.LENGTH_SHORT).show();
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG, "onActivityResult   :: not null");
		if (requestCode == BLUTOOTH_DISCOVERABILITY_DURATION) {
			// successful turning on bluetooth device

		} else if (requestCode == getActivity().RESULT_CANCELED) {
			// failed to turn on Bluetooth
		} else if (requestCode == REQUEST_ENABLE_BT) {
			// Bluetooth turned on successfully
			// setUpDiscovery();

		} else if (requestCode == REQUEST_ENABLE_BT && resultCode != 1) {
			// unsuccessful in turning ON Bluetooth
			hideProgressDialog();
			isCancled = true;
			/*
			 * showAlertDialog("Failure !", "Unable to turn ON Bluetooth", "Ok",
			 * btOkButton);
			 */
		}
	}

	private DialogInterface.OnClickListener btOkButton = new OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub

		}
	};

	protected void setDeviceAdapter() {
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				setDeviceAdapterTwo();
			}
		});

	}

	private void setDeviceAdapterTwo() {

		scanForPairedDevices();
		adapter = new ScannerAdapter(PrinterDetail.getPrinterList());
		Log.d(TAG, "setDeviceAdapter   ::" + PrinterDetail.getPrinterList().size());
		
		  /*AlertDialogHelper.showAlertDialogForDevices(getActivity(), adapter,
		  onScanButtonClicklistener, onCancleScanButtonClicklistener,
		  onDeviceSelectedListner);*/
		 

		mDiscoveredDevicesListView.setAdapter(adapter);
		mDiscoveredDevicesListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				Log.d(TAG, "onItemClick   ::");
				PrinterDetail printerDetail = PrinterDetail.getPrinterList().get(position);
				if (printerDetail != null && printerDetail.getMacAddress() != null) {
					Log.d(TAG, "onItemClick   :: not null"+PrinterDetail.getPrinterList().get(position));
					AccountPreference.setPrinter(printerDetail.getMacAddress(), printerDetail.getFriendlyName());
					Toast.makeText(getActivity(), "Printer selected "+"\n"+ PrinterDetail.getPrinterList().get(position).getFriendlyName() +"\n"+ PrinterDetail.getPrinterList().get(position).getMacAddress(), Toast.LENGTH_SHORT).show();
					isPrinterSelected = true;
					if (isCalledForResult) {
						getActivity().setResult(PrintPVTFragment.GET_MAC_ADDDRESS_RESULT_CODE);
						getActivity().finish();
					}
				}
			}
		});

	}

	/*private OnItemClickListener onDeviceSelectedListner = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
			Log.d(TAG, "onDeviceSelectedListner....." + position);
			AlertDialogHelper.cancelCurrentAlertDialog();
			Log.d(TAG, "onItemClick   ::");
			PrinterDetail printerDetail = PrinterDetail.getPrinterList().get(position);
			if (printerDetail != null && printerDetail.getMacAddress() != null) {
				Log.d(TAG, "onItemClick   :: not null");
				AccountPreference.setPrinter(printerDetail.getMacAddress());
				if (isCalledForResult) {
					getActivity().setResult(PrintPVTFragment.GET_MAC_ADDDRESS_RESULT_CODE);
				}
			}
			;
		}
	};*/
}
