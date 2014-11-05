package edu.uji.pollutionWebGIS.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("MyPRCImpl")
public interface MyRPCService extends RemoteService {
	
	public PollutionData[] getPollutionData(String concInput, String pollutantInput, String yearInput);
	
	public SurfaceData[] getSurfaceData(String pollutantInput, String yearInput);
	
}
