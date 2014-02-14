package com.smikeapps.parking.app;

import java.util.ArrayList;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.smikeapps.parking.common.context.ParkingApplication;

public class OmniaProvider {


	// creating only single volley request queue
	public RequestQueue mRequestQueue;

	private static OmniaProvider INSTANCE;
	private ArrayList < WebServiceOperation > operationQueue;

	// storing extra request if logged
	private ArrayList < WebServiceOperation > pendingOperationQueue;

	private OmniaProvider ( ) {

		operationQueue = new ArrayList < WebServiceOperation > ( );
		pendingOperationQueue = new ArrayList < WebServiceOperation > ( );
		mRequestQueue = Volley.newRequestQueue ( ParkingApplication.getInstance ( ) );
		

	}

	public static synchronized OmniaProvider getInstance ( ) {
		if ( INSTANCE == null ) {
			INSTANCE = new OmniaProvider ( );
		}
		return INSTANCE;
	}

	public void askOperationExecution ( WebServiceOperation operationToExecute ) {

		// handling the extra request 
		if ( operationToExecute.request != null && this.operationQueue != null ) {
			mRequestQueue.add( operationToExecute.request );
			this.operationQueue.add ( operationToExecute );

			//mRequestQueue.start();	
		}
	}

	public void finishOperation ( WebServiceOperation operationToFinish ) {
		// checking if we can accomodate pending request or not
		this.operationQueue.remove ( operationToFinish );
	}

	// cancelling all the request on basis of tag
	public void cancelAllOperationsForManager( String managerName ) {

		mRequestQueue.cancelAll( managerName );

		if ( operationQueue.size() > 0 ) {

			ArrayList < WebServiceOperation > removedOperations = new ArrayList < WebServiceOperation > ( );
			for ( int iteOperation = 0; iteOperation < operationQueue.size ( ) ; iteOperation++ ) {

				WebServiceOperation currentOperation = operationQueue.get ( iteOperation );
				if ( ( currentOperation.request.getTag().toString() ).equalsIgnoreCase ( managerName ) ) {
					removedOperations.add(currentOperation);
				}
			}

			for ( int iteRemovedOperation = 0; iteRemovedOperation < removedOperations.size(); iteRemovedOperation++ ) {

				operationQueue.remove ( removedOperations.get( iteRemovedOperation ) );
			}
			removedOperations.clear();
			removedOperations = null;

		}
	}


}
