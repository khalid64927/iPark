package com.smikeapps.parking.app;


public class WebServiceError extends Exception  {
    private int errorCode;

    public WebServiceError ( int errorCode, String message ) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getCode() {
        return errorCode;
    }
}
