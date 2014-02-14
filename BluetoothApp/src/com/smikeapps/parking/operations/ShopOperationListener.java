package com.smikeapps.parking.operations;

import com.smikeapps.parking.comman.utils.AlertDialogHelper;
import com.smikeapps.parking.comman.utils.AlertDialogHelper.SmikeAppException;
import com.smikeapps.parking.comman.utils.EshopException;


public interface ShopOperationListener {
	
	public void foundSearchOpenStores();
	
	public void foundSearchStoresWithKeyWord();
	
	public void foundSearchStoreWithStoreRef();
	
	public void deliverySlotSucceed ( );


    public void failedToSearchOpenStores(AlertDialogHelper.SmikeAppException failure);

    public void failedToSearchStoreWithKeyword(AlertDialogHelper.SmikeAppException failure);

    public void failedToSearchStore(EshopException failure);
    
    public void failedDeliverySlots ( SmikeAppException failure );

    
}

