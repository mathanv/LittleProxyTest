package com.proxy.utils;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

import org.littleshoot.proxy.HttpFiltersAdapter;

import com.proxy.model.SessionInfo;

public class CustomHttpFiltersAdapter extends HttpFiltersAdapter {
	
	private static final String TAG = "CustomHttpFiltersAdapter";
	private static List<SessionInfo> responseSessionInfo = new ArrayList<SessionInfo>();
	private static List<SessionInfo> requestSessionInfo = new ArrayList<SessionInfo>();
	private boolean pendingRequest;
	private boolean modifyRequest;
	
	public CustomHttpFiltersAdapter(HttpRequest originalRequest) {		
		super(originalRequest);
	}
		
	@Override
    public HttpResponse requestPre(HttpObject httpObject) {
		Log.d(TAG,"requestPre - Request through proxy: " + super.originalRequest.getUri());
		// We use this filter for on-the-fly redirection of the traffic to some internal servers
		// The issue is reproduceable even without this
        return null;
    }
	 
   @Override
   public HttpResponse requestPost(HttpObject httpObject) {                            	
	   if ( httpObject instanceof HttpRequest) {
		   	Log.d(TAG,"requestPost - Request through proxy: " + super.originalRequest.getUri());
   			pendingRequest = true;                            		
   			HttpRequest request = (HttpRequest) httpObject;
   			HttpHeaders reqHeaders = request.headers();   			   			
   			
   			SessionInfo reqSessionInfo = new SessionInfo();
   			reqSessionInfo.setRequestUrl(super.originalRequest.getUri());   		   			   			                            		                            		   			   			
                                       		
   			for (Map.Entry mapEntry: reqHeaders.entries()) {
   				reqSessionInfo.addHeaderInfo(new String [] {(String) mapEntry.getKey(), (String) mapEntry.getValue()});
   	  			if (super.originalRequest.getUri().contains("vizury.com/analyze") || super.originalRequest.getUri().contains("ssl.vizury.com")) {   				   	   				
   	   					Log.d(TAG, "Header: " + (String) mapEntry.getKey());
   	   					Log.d(TAG, "Header: " + (String )mapEntry.getValue());   	   				   				
   	   			}
   			}   				
           
            requestSessionInfo.add(reqSessionInfo);
   		}   	  
      return null;
   }
   
   @Override
   public HttpObject responsePre(final HttpObject httpObject) {
	   Log.d(TAG,"Response-Pre through proxy: " + super.originalRequest.getUri());	   
	   return httpObject;
   }
   
   @Override
   public HttpObject responsePost(final HttpObject httpObject) {	   
	   if (httpObject instanceof HttpResponse) {                            		
		   Log.d(TAG,"Response through proxy: " + super.originalRequest.getUri());                            		
   		
   			SessionInfo sInfo = new SessionInfo();
   			sInfo.setRequestUrl(super.originalRequest.getUri());
   		
   			HttpResponse response = (HttpResponse) httpObject;
   			sInfo.setStatusCode(response.getStatus().code());                            		                            		
   		
   			HttpHeaders allHeaders = response.headers();
                                       		
   			for (Map.Entry mapEntry: allHeaders.entries())
   				sInfo.addHeaderInfo(new String [] {(String) mapEntry.getKey(), (String) mapEntry.getValue()});
           
             responseSessionInfo.add(sInfo);
             pendingRequest = false;
   	}     	  
      return httpObject;
   }
   
   /*public int getMaximumRequestBufferSizeInBytes() {
       return 1024 * 1024;
   }   
   
   public int getMaximumResponseBufferSizeInBytes() {
       return 1024 * 1024;
   }
   */
   
   public List<SessionInfo> getResponseSessionInfo (){
		return responseSessionInfo;
   }
	
   	public List<SessionInfo> getRequestSessionInfo (){
		return requestSessionInfo;
	}
   
	public void clearSessionInfo() {
		requestSessionInfo = null;
		responseSessionInfo = null;		
	}
	
	public void initSessionInfo() {
		requestSessionInfo = new ArrayList<SessionInfo>();;
		responseSessionInfo = new ArrayList<SessionInfo>();
	}
	
	public boolean isRequestPending () {
		return pendingRequest;
	}

	public boolean isModifyRequest() {
		return modifyRequest;
	}

	public void setModifyRequest(boolean modifyRequest) {
		this.modifyRequest = modifyRequest;
	}

}
