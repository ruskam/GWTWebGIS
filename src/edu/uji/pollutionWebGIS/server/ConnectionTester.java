/**
package edu.uji.pollutionWebGIS.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tools.ant.util.StringUtils;

import edu.uji.pollutionWebGIS.client.PollutionData;
import edu.uji.pollutionWebGIS.client.SurfaceData;



public class ConnectionTester {
	
	
	
	/**
	 * @param args
	 * @throws SQLException 
	 */
	/*
	public static void main(String[] args) throws SQLException {
		PollutionData[] myPollutionData;
		
		DB_MyDB myConnection = new DB_MyDB();
		
		String concInput = "100";
		String pollutantInput = "BENZENE";
		String yearInput = "2001";
		myPollutionData = myConnection.getPollutionData(concInput, pollutantInput, yearInput);
		
		System.out.println("-----------------------");
		int count = 0;
		for (PollutionData element: myPollutionData){
			System.out.println(element.facilityID + "; " + element.facilityName + "; " + element.regionName + "; " + element.year + "; ");
			count += 1;
		}
		System.out.println(count); 
		 */
		/*SurfaceData[] mySurfaceData;
		
		DB_MyDB myConnection = new DB_MyDB();
		
		String concInput = "100";
		String pollutantInput = "BENZENE";
		String yearInput = "2001";
		mySurfaceData = myConnection.getSurfaceData(concInput);
		
		System.out.println("-----------------------");
		int count = 0;
		//String [] coords;
		//String [] coordX = null;
		//String [] coordY = null;
		for (SurfaceData element: mySurfaceData){
			String wktRaw1 = element.wkt;
			String wktRaw2 = StringUtils.replace(wktRaw1, "MULTIPOLYGON(((", "");
			String wktFinal = StringUtils.replace(wktRaw2, ")))", "");
			System.out.println("string " + count + ": " + wktFinal);
			System.out.println("---------------------");
			count += 1;
			String [] fields = wktFinal.split(",");
			for (int j = 0; j < fields.length; j++){
				//coords = new String[fields.length]; 
				//coords[j] = fields[j];
				String temp = fields[j];
				//System.out.println(temp);
				String [] crds = temp.split(" ");
				String X = crds[0];
				String Y = crds[1];
				//coordX = coords[j].split(" ");
				System.out.println("POINT " + j + ": " + temp + " X=" + X + "; Y=" + Y);
			}
		}*/
		
		
		
		//System.out.println(count);
				
		
	//}
		

//}
