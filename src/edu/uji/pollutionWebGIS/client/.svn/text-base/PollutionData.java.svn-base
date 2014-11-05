package edu.uji.pollutionWebGIS.client;
import com.google.gwt.user.client.rpc.IsSerializable;

public class PollutionData implements IsSerializable{
	
	//field to store and transfer data
	public int facilityID;
	public String facilityName;
	public String regionName;
	public String year;
	public double quantity;
	public double longitude;
	public double latitude;
	public String pollName;
	
	private String concInput;
	private String pollutantInput; 
	
	private PollutionData(){
		
	}
	
	public PollutionData(String concInput, String pollutantInput){
		this.concInput = concInput;
		this.pollutantInput = pollutantInput;
	}
	
	/*@Override
	public String toString(){
		return facilityID + " " + facilityName + " " + countryName;
	}*/
}
