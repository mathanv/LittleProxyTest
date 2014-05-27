package com.proxy.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SessionInfo extends CustomHttpResponse {	
	private List<String []> headerInfo = new ArrayList<String []>();	
	
	public List<String []> getHeaderInfo() {
		return headerInfo;
	}
	
	public void setHeaderInfo(List<String []> headerInfo) {
		synchronized (this.headerInfo) {
			this.headerInfo = headerInfo;
		}
	}
	
	public void addHeaderInfo (String [] header) {
		this.headerInfo.add(header);
	}		
}
