package com.smikeapps.parking.comman.utils;

import com.smikeapps.parking.R;
import com.smikeapps.parking.comman.utils.AlertDialogHelper.SmikeAppException;

import android.content.Context;

/**
 * A class to encapsulate the exception of EShop module and the messages to
 * present to the user. Typically you use these exception when you have a
 * failure which needs to be display to the User.
 */
public class EshopException extends Exception implements SmikeAppException {

	private static final long serialVersionUID = -3478294332343001040L;
	private SmikeAppExceptionCode exceptionCode;
	private int statusCode;

	/**
	 * An enum explaining the reason of the exception.
	 */
	public enum SmikeAppExceptionCode {

		InvalidAccessToken(R.string.invalid_access_token), ServerNotResponding(R.string.server_not_responding), BadHttpStatus(
				R.string.server_not_responding);
		private final int rValue;

		/**
		 * EshopExceptionCode default constructor.
		 * 
		 * @param rValue
		 *            The android xml string key of the message to present to
		 *            the user.
		 */
		SmikeAppExceptionCode(int rValue) {
			this.rValue = rValue;
		}

		public String getMessage(Context context) {
			return context.getString(rValue);
		}
	}

	public EshopException(SmikeAppExceptionCode exceptionCode) {
		super();
		this.exceptionCode = exceptionCode;
	}

	/**
	 * Main constructor.
	 * 
	 * @param exceptionCode
	 *            The exception code, the message display to the user will
	 *            change according to this exception code.
	 * @param cause
	 *            The root exception, allow developpers to understand what went
	 *            wrong in the application.
	 */
	public EshopException(SmikeAppExceptionCode exceptionCode, Throwable cause) {
		super(cause);
		this.exceptionCode = exceptionCode;
	}

	/**
	 * Main constructor.
	 * 
	 * @param exceptionCode
	 *            The exception code, the message display to the user will
	 *            change according to this exception code.
	 * @param cause
	 *            The root exception, allow developpers to understand what went
	 *            wrong in the application.
	 * @param actual
	 *            status code .
	 */
	public EshopException(SmikeAppExceptionCode exceptionCode, Throwable cause, int statusCode) {
		super(cause);
		this.exceptionCode = exceptionCode;
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	@Override
	public String getLocalizedMessage(Context context) {
		if (this.exceptionCode != null) {
			return this.exceptionCode.getMessage(context);
		} else {
			return context.getString(R.string.app_failure_case_undefined);
		}
	}

	public SmikeAppExceptionCode getEshopExceptionCode() {
		return this.exceptionCode;
	}
}
