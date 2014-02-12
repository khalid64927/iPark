package com.smikeapps.parking.manager;

import com.smikeapps.parking.interfaces.BluetoothConnectionListner;

public class BluetoothConnectionManager implements BluetoothConnectionListner{

	
	private BluetoothConnectionManager mBluetoothConnectionManager;
	private BluetoothConnectionListner listner;
	
	
	public BluetoothConnectionManager managerWithDelegate(BluetoothConnectionListner delegate){
		return new BluetoothConnectionManager(delegate);
	}
	
	private BluetoothConnectionManager(BluetoothConnectionListner listner){
		this.listner = listner;
	}

	@Override
	public void sendFileSuccess() {
		this.listner.sendFileSuccess();
		
	}

	@Override
	public void sendFileFailure(Exception exception) {
		this.listner.sendFileFailure(exception);
	}
}
