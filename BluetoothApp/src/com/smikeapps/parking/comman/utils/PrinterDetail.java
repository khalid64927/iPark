package com.smikeapps.parking.comman.utils;

import java.util.ArrayList;
import java.util.HashSet;

public class PrinterDetail {

	private String friendlyName;
	private String macAddress;
	
	
	private static HashSet<String> setList;
	private static ArrayList<PrinterDetail> printerList;
	
	public PrinterDetail(String friendlyName, String macAddress){
		this.friendlyName = friendlyName;
		this.macAddress = macAddress;
	}

	public String getFriendlyName() {
		return friendlyName;
	}

	public void setFriendlyName(String friendlyName) {
		this.friendlyName = friendlyName;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	
	public static boolean addPrinter(String friendlyName, String macAddress){
		boolean isAlreadyAdded = false; 
		if(setList == null){
			setList = new HashSet<String>();
		}else{
			if(setList.add(macAddress)){
				if(printerList == null){
					printerList = new ArrayList<PrinterDetail>();
				}
				printerList.add(new PrinterDetail(friendlyName, macAddress));
				isAlreadyAdded = true;
			}else{
				isAlreadyAdded = false;
			}
		}
		return isAlreadyAdded;
	}
	
	public static ArrayList<PrinterDetail> getPrinterList(){
		if(printerList == null){
			printerList = new ArrayList<PrinterDetail>();
		}
		return printerList;
	}
	
	
	
	
}
