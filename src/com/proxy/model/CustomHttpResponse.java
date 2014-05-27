package com.proxy.model;

public class CustomHttpResponse {
	private int statusCode;
	private String response;
	private long responseTime;
	private String mimeType;
	private String redirectLocation;
	private String requestUrl;
	
	public CustomHttpResponse () {
		this.statusCode = 0;
		this.response = "";
		this.responseTime = 0;
		this.mimeType = "";
		this.redirectLocation = "";
		this.requestUrl = "";
	}
	
	public int getStatusCode() {
		return statusCode;
	}
	
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	public String getResponse() {
		return response;
	}
	
	public void setResponse(String response) {
		this.response = response;
	}
	
	public long getResponseTime() {
		return responseTime;
	}
	
	public void setResponseTime(long responseTime) {
		this.responseTime = responseTime;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getRedirectLocation() {
		return redirectLocation;
	}

	public void setRedirectLocation(String redirectLocation) {
		this.redirectLocation = redirectLocation;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	
}
