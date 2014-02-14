package com.smikeapps.parking.managers;

import com.smikeapps.parking.comman.utils.AlertDialogHelper;


public interface ShopManagerListener {
	
	public void searchOpenStoreSuccess();
	
	public void searchStoreWithKeyWordSuccess();
	
	public void searchStoreDetailSuccess();

    public void searchOpenStoreFailure(AlertDialogHelper.SmikeAppException failure);

    public void searchStoreWithKeyWordFailure(AlertDialogHelper.SmikeAppException failure);

    public void searchStoreDetailFailure( AlertDialogHelper.SmikeAppException failure );
    
}

