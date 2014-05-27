package com.proxy.utils;


import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.channel.ChannelHandlerContext;

import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.littleshoot.proxy.ChainedProxyManager;
import org.littleshoot.proxy.ChainedProxy;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.extras.SelfSignedMitmManager;
import org.littleshoot.proxy.extras.SelfSignedSslEngineSource;

import com.proxy.utils.CustomHttpFiltersAdapter;

import com.proxy.model.SessionInfo;

public class ProxyServer {

	private static HttpProxyServer proxyServer;
	private static final int proxyPort = 9090;
	private static List<SessionInfo> sessionInfo = new ArrayList<SessionInfo>();
	private static final String TAG = "ProxyServer";
	private static boolean pendingRequest = false;
	private static boolean modifyRequest = false;
	private static CustomHttpFiltersAdapter customHttpFilter;	
	
	public ProxyServer (int portNumber) {				
		proxyServer = DefaultHttpProxyServer.bootstrap()
						.withPort(portNumber)												
                       /*.withChainProxyManager(new ChainedProxyManager() {
                        	@Override
                        	public void lookupChainedProxies(HttpRequest httpRequest,
                        			Queue<ChainedProxy> chainedProxies) {
                        	}
                        })*/
						.withManInTheMiddle(new SelfSignedMitmManager())										
						.withFiltersSource(new HttpFiltersSourceAdapter() {								
							public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
							Log.d(TAG, "Proxy Invoked");
							customHttpFilter = new CustomHttpFiltersAdapter(originalRequest);
							if (modifyRequest)
								customHttpFilter.setModifyRequest(true);
							else 
								customHttpFilter.setModifyRequest(false);
							return customHttpFilter; 
                        }
                    })                    	
						.start();
	}				
		
	public void stop (){
		proxyServer.stop();
	}
	
	public List<SessionInfo> getResponseSessionInfo (){
		if (customHttpFilter != null)
			return customHttpFilter.getResponseSessionInfo();
		else 
			return null;
	}
	
	public List<SessionInfo> getRequestSessionInfo (){
		if (customHttpFilter != null)
			return customHttpFilter.getRequestSessionInfo();
		else 
			return null;
	}
	
	public void clearSessionInfo() {
		if (customHttpFilter != null)
			customHttpFilter.clearSessionInfo();
	}
	
	public void initSessionInfo() {
		if (customHttpFilter != null)
			customHttpFilter.initSessionInfo();
	}
	
	public boolean isRequestPending () {
		if (customHttpFilter != null)
			return customHttpFilter.isRequestPending();
		else
			return false;
	}

	public boolean isModifyRequest() {
		return customHttpFilter.isModifyRequest();
	}

	public void setModifyRequest(boolean modifyRequest) {
		ProxyServer.modifyRequest = modifyRequest;
		if (customHttpFilter != null)
			customHttpFilter.setModifyRequest(modifyRequest);
	}
}
