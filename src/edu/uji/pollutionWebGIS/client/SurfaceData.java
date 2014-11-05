package edu.uji.pollutionWebGIS.client;
import com.google.gwt.user.client.rpc.IsSerializable;

/*author Rustam Kamberov, MSc GeoTech 2010*/

public class SurfaceData implements IsSerializable{
	
	public int gid;
	public double longitude;
	public double latitude;
	public double zValue;
	public String wkt;
	
	private String pollutantInput;
	private String yearInput; 
	
	private SurfaceData(){
		
	}
	
	public SurfaceData(String pollutantInput, String yearInput){
		this.pollutantInput = pollutantInput;
		this.yearInput = yearInput;
	}

}
