package edu.uji.pollutionWebGIS.client;

import com.google.gwt.core.client.GWT;

public class RPCInit {
	
	public RPCInit(){
		
	}
	
	
	public static MyRPCServiceAsync initRPC(){
		MyRPCServiceAsync rpc = (MyRPCServiceAsync) GWT.create(MyRPCService.class);
		return rpc;
	}

}


