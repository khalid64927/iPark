package com.smikeapps.parking.comman.utils;

import java.io.Serializable;

public class IssueTicket implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String plateSource;
	private String plateCode;
	private String plateNumber;
	private String plateRegion;
	private String platecategory;

	public IssueTicket(String plateSource, String plateCode, String plateNumber, String category, String region) {
		this.plateSource = plateSource;
		this.plateCode = plateCode;
		this.plateNumber = plateNumber;
		this.plateRegion = region;
		this.platecategory = category;
	}

	public String getPlateSource() {
		return plateSource;
	}

	public void setPlateSource(String plateSource) {
		this.plateSource = plateSource;
	}

	public String getPlateCode() {
		return plateCode;
	}

	public void setPlateCode(String plateCode) {
		this.plateCode = plateCode;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getPlateRegion() {
		return plateRegion;
	}

	public void setPlateRegion(String plateRegion) {
		this.plateRegion = plateRegion;
	}

	public String getPlatecategory() {
		return platecategory;
	}

	public void setPlatecategory(String platecategory) {
		this.platecategory = platecategory;
	}

}
