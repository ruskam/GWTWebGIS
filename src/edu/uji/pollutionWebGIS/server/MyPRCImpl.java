/*author Rustam Kamberov, MSc GeoTech 2010*/

package edu.uji.pollutionWebGIS.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.uji.pollutionWebGIS.client.MyRPCService;
import edu.uji.pollutionWebGIS.client.PollutionData;
import edu.uji.pollutionWebGIS.client.SurfaceData;


@SuppressWarnings("serial")
public class MyPRCImpl extends RemoteServiceServlet implements MyRPCService{
	
	public PollutionData[] getPollutionData(String concInput, String pollutantInput, String yearInput){
		DB_MyDB db = new DB_MyDB();
		PollutionData[] pollutionData = db.getPollutionData(concInput, pollutantInput, yearInput);
		return pollutionData;
		
	}
	
	public SurfaceData[] getSurfaceData(String pollutantInput, String yearInput){
		DB_MyDB db = new DB_MyDB();
		SurfaceData[] surfaceData = db.getSurfaceData(pollutantInput, yearInput);
		return surfaceData;
		
	}

		

}





