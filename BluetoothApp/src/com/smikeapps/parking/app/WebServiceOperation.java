package com.smikeapps.parking.app;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.smikeapps.parking.comman.utils.AuthToken;
import com.smikeapps.parking.comman.utils.EshopException;
import com.smikeapps.parking.common.context.AccountPreference;

/***
 * Handling the web service calls 
 * Manages creation of volley request and callbacks to parse 
 * 
 */

public abstract class WebServiceOperation {

	protected Listener<JSONObject> listener;
	protected ErrorListener errorListener;
	protected Boolean isCanceled;
	protected Request request;

	Manager managerRequestingOperation;


	protected Handler handler = new Handler ();

	public WebServiceOperation ( Manager managerRequestingOperation ) {

		this.isCanceled = false;
		this.managerRequestingOperation = managerRequestingOperation;

		listener = new Listener < JSONObject > ( ) {


			@Override
			public void onResponse ( JSONObject friendJSONObject ) {
				success ( friendJSONObject );

				OmniaProvider.getInstance ( ) .finishOperation ( WebServiceOperation.this );
			}
		};

		errorListener = new ErrorListener ( ) {
			@Override
			public void onErrorResponse ( VolleyError error ) {

				error.printStackTrace();

				if ( ServerError.class == error.getClass ( ) ) {
					ServerError serverError = ( ServerError ) error;

					if ( serverError.networkResponse.statusCode < 200 || serverError.networkResponse.statusCode >= 300 ) {

						Exception exception = new IllegalStateException("Got status code " + serverError.networkResponse.statusCode + "\nResponse : " + serverError.networkResponse );
						EshopException eshopException = new EshopException(EshopException.SmikeAppExceptionCode.BadHttpStatus, exception, serverError.networkResponse.statusCode);

						failedWithError ( eshopException );
						OmniaProvider.getInstance ( ).finishOperation ( WebServiceOperation.this );
						return;
					}
				}

				EshopException eshopException = new EshopException(EshopException.SmikeAppExceptionCode.ServerNotResponding, error );

				failedWithError ( eshopException );
				OmniaProvider.getInstance ( ).finishOperation ( WebServiceOperation.this );
			}
		};

	}


	public Request getRequest ( String url ) {
		return new JsonObjectRequest ( Request.Method.GET, url, null, listener, errorListener );
	}


	public Request postRequest ( String url, JSONObject params ) {
		return new JsonObjectRequest ( Request.Method.POST, url, params, listener, errorListener );
	}
	
	public Request postRequest ( String url, final HashMap < String, String > headers ) {
		return new JsonObjectRequest ( Request.Method.POST, url, null, listener, errorListener ) {
			
			@Override
			public Map<String, String> getHeaders()  {
				return headers;
			}
		};
	}

	public Request postRequest ( String url, JSONObject params, final HashMap < String, String > headers ) {
		return new JsonObjectRequest ( Request.Method.POST, url, params, listener, errorListener ) {

			@Override
			public Map<String, String> getHeaders()  {
				return headers;
			}
		};
	}

	public Request getRequestParse ( String url, final int type ) {

		return new JsonObjectRequest ( Request.Method.GET,url, null,listener, errorListener ) {
			@Override
			protected Response<JSONObject> parseNetworkResponse ( NetworkResponse response ) {
				try {
					try {
						String result = new String ( response.data, HttpHeaderParser.parseCharset( response.headers ) );
						if ( result != null && result.trim().length() > 0 ) {
							parseResponse ( new JSONObject ( new String ( response.data, HttpHeaderParser.parseCharset( response.headers ) ) ), type );
						}
						return Response.success ( new JSONObject(), HttpHeaderParser.parseCacheHeaders ( response ) );
					} catch (EshopException e) {
						e.printStackTrace();
						return Response.error ( new VolleyError( e ) );
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					return Response.error(new ParseError( e ) );
				} catch (JSONException e) {
					e.printStackTrace();
					return Response.error(new ParseError( e ) );
				}
			}

		};
	}



	public Request getRequestParse ( String url, final HashMap < String, String > headers, final int type ) {

		return new JsonObjectRequest ( Request.Method.GET,url, null,listener, errorListener ) {

			@Override
			public Map<String, String> getHeaders()  {
				return headers;
			}


			@Override
			protected Response<JSONObject> parseNetworkResponse ( NetworkResponse response ) {
				try {
					try {
						String result = new String ( response.data, HttpHeaderParser.parseCharset( response.headers ) );
						if ( result != null && result.trim().length() > 0 ) {
							parseResponse ( new JSONObject ( new String ( response.data, HttpHeaderParser.parseCharset( response.headers ) ) ), type );
						}
						return Response.success ( new JSONObject(), HttpHeaderParser.parseCacheHeaders ( response ) );
					} catch (EshopException e) {
						e.printStackTrace();
						return Response.error ( new VolleyError( e ) );
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					return Response.error(new ParseError( e ) );
				} catch (JSONException e) {
					e.printStackTrace();
					return Response.error(new ParseError( e ) );
				}
			}

		};
	}


	public Request postRequestParse ( String url, JSONObject params, final int type ) {

		return new JsonObjectRequest ( Request.Method.POST, url, params, listener, errorListener ) {
			@Override
			protected Response<JSONObject> parseNetworkResponse ( NetworkResponse response ) {
				try {
					try {
						String result = new String ( response.data, HttpHeaderParser.parseCharset( response.headers ) );
						if ( result != null && result.trim().length() > 0 ) {
							parseResponse ( new JSONObject ( new String ( response.data, HttpHeaderParser.parseCharset( response.headers ) ) ), type );
						}
						parseResponse ( new JSONObject ( new String ( response.data, HttpHeaderParser.parseCharset( response.headers ) ) ), type );
						return Response.success ( new JSONObject(), HttpHeaderParser.parseCacheHeaders ( response ) );
					} catch (EshopException e) {
						e.printStackTrace();
						return Response.error ( new VolleyError( e ) );
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					return Response.error(new ParseError( e ) );
				} catch (JSONException e) {
					e.printStackTrace();
					return Response.error(new ParseError( e ) );
				}
			}

		};
	}

	public Request postRequestParse ( String url, JSONObject params, final HashMap < String, String > headers, final int type ) {

		return new JsonObjectRequest( Request.Method.POST, url, params, listener, errorListener ) {

			@Override
			public Map<String, String> getHeaders()  {
				return headers;
			}

			@Override
			protected Response<JSONObject> parseNetworkResponse(
					NetworkResponse response) {

				try {
					if (response.statusCode == HttpStatus.SC_OK && type == 2) {
						return Response.success(new JSONObject(),
								HttpHeaderParser.parseCacheHeaders(response));
					} else {
						String result = new String(response.data,HttpHeaderParser.parseCharset(response.headers));
						if (result != null && result.trim().length() > 0) {
							parseResponse(new JSONObject(new String(response.data,HttpHeaderParser.parseCharset(response.headers))),type);
						}
						return Response.success(new JSONObject(new String(response.data,HttpHeaderParser.parseCharset(response.headers))),HttpHeaderParser.parseCacheHeaders(response));
					}
				} catch (EshopException e) {
					e.printStackTrace();
					return Response.error(new VolleyError(e));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					return Response.error(new ParseError(e));
				} catch (JSONException e) {
					e.printStackTrace();
					return Response.error(new ParseError(e));
				}
			}
		};
	}

	public abstract void parseResponse ( JSONObject response, int type ) throws EshopException;


	/**
	 * delete request parser
	 * @param url
	 * @param headers
	 * @param type
	 * @return
	 */

	public Request deleteRequestParse ( String url, final HashMap < String, String > headers, final int type ) {

		return new JsonObjectRequest ( Request.Method.DELETE, url, null, listener, errorListener ) {

			@Override
			public Map<String, String> getHeaders()  {
				return headers;
			}


			@Override
			protected Response<JSONObject> parseNetworkResponse ( NetworkResponse response ) {
				try {
					try {
						String result = new String ( response.data, HttpHeaderParser.parseCharset( response.headers ) );
						if ( result != null && result.trim().length() > 0 ) {
							parseResponse ( new JSONObject ( new String ( response.data, HttpHeaderParser.parseCharset( response.headers ) ) ), type );
						}
						return Response.success ( new JSONObject(), HttpHeaderParser.parseCacheHeaders ( response ) );
					} catch (EshopException e) {
						e.printStackTrace();
						return Response.error ( new VolleyError( e ) );
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					return Response.error(new ParseError( e ) );
				} catch (JSONException e) {
					e.printStackTrace();
					return Response.error(new ParseError( e ) );
				}
			}

		};
	}


	public Request putRequestParse ( String url, JSONObject params,final HashMap < String, String > headers, final int type ) {

		return new JsonObjectRequest ( Request.Method.PUT,url, params,listener, errorListener ) {

			@Override
			public Map<String, String> getHeaders()  {
				return headers;
			}


			@Override
			protected Response<JSONObject> parseNetworkResponse ( NetworkResponse response ) {
				try {
					try {
						String result = new String ( response.data, HttpHeaderParser.parseCharset( response.headers ) );
						if ( result != null && result.trim().length() > 0 ) {
							parseResponse ( new JSONObject ( new String ( response.data, HttpHeaderParser.parseCharset( response.headers ) ) ), type );
						}
						return Response.success ( new JSONObject(), HttpHeaderParser.parseCacheHeaders ( response ) );
					} catch (EshopException e) {
						e.printStackTrace();
						return Response.error ( new VolleyError( e ) );
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					return Response.error(new ParseError( e ) );
				} catch (JSONException e) {
					e.printStackTrace();
					return Response.error(new ParseError( e ) );
				}
			}

		};
	}




	public void cancel ( ) {
		this.isCanceled = true;
	}

	public void success ( JSONObject jsonAnswer ) {
		assert false :"method success should have been overriden" ;
	}

	public void failedWithError ( EshopException eshopException ) {
		assert false :"method failedWithError should have been overriden" ;

	}

	public Manager getManagerRequestingOperation () {
		return managerRequestingOperation;
	}

}
