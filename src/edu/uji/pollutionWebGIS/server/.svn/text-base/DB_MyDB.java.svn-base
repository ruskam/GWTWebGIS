package edu.uji.pollutionWebGIS.server;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import org.gwtopenmaps.openlayers.client.LonLat;

import org.gwtopenmaps.openlayers.client.geometry.Point;

import edu.uji.pollutionWebGIS.client.PollutionData;
import edu.uji.pollutionWebGIS.client.SurfaceData;

//The class extend MyPostGISConnection
public class DB_MyDB extends MyPostGISConnection{
	
	public DB_MyDB(){
	}
	
	public PollutionData[] getPollutionData(String concInput, String pollutantInput, String yearInput){

		int j = 0;
		String query = "SELECT i.facilityid, i.facilityname, i.regionname, e.reportingyear, e.quantity, i.longitude, i.latitude, p.pollutantcode " +
				"FROM industry i, pollutant p, emission e " +
				"WHERE i.facilityid = e.facilityid " +
				"AND p.pollutantid = e.pollutantid " +
				"AND regionname = 'Valencia' " +
				"AND e.quantity > " + concInput + " " +
				"AND p.pollutantcode = '" + pollutantInput + "' " +
				"AND e.reportingyear = '" + yearInput + "' ";
		
		//query = "SELECT i.facilityid, i.facilityname, i.regionname, e.reportingyear, e.quantity, i.longitude, i.latitude FROM industry i, pollutant p, emission e WHERE i.facilityid = e.facilityid AND p.pollutantid = e.pollutantid AND regionname = 'Valencia' AND e.quantity > 1000 AND p.pollutantname = 'Benzene'";
		
		//This is for RPC transport
		PollutionData[] pollutionData = null;
		
		
		
		try {
			
			/*getResultSet();
			getResultSet();
			statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);*/
			
			Connection connection = getConnection();
			Statement select = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			
			ResultSet result = select.executeQuery(query);
			
			//Initialize object into the size we need, like a recordset
			int rsSize = getResultSetSize(result);
			
			pollutionData = new PollutionData[rsSize];
			
			int i = 0;
			while (result.next()) {
				
				pollutionData[i] = new PollutionData(concInput, pollutantInput);
				pollutionData[i].facilityID = result.getInt(1);
				pollutionData[i].facilityName = result.getString(2);
				pollutionData[i].regionName = result.getString(3);
				pollutionData[i].year = result.getString(4);
				pollutionData[i].quantity = result.getDouble(5);
				pollutionData[i].longitude = result.getDouble(6);
				pollutionData[i].latitude = result.getDouble(7);
				pollutionData[i].pollName = result.getString(8);
				//System.out.println(pollutionData[i].facilityID + " " + pollutionData[i].facilityName + " " + pollutionData[i].countryName);
				
				
				/*int facilityID = result.getInt(1);
				String facilityName = result.getString(2);
				String countryName = result.getString(8);*/
				i++;
			}
			result.close();
			connection.close();
		} catch (SQLException e) {
			System.err.println("PostGIS error: " + query);
			e.printStackTrace();
		}
				
		return pollutionData;
		
	}
	
	public SurfaceData[] getSurfaceData(String pollutantInput, String yearInput){
		
		//String qryInput = "SELECT i.facilityid, i.facilityname, i.regionname, e.reportingyear, e.quantity, i.longitude, i.latitude, p.pollutantcode " +
		//String qryInput = "SELECT i.longitude, i.latitude, e.quantity, p.pollutantcode " +
		String qryInput = "SELECT st_X(st_astext(st_transform(i.geom, 900913))), st_Y(st_astext(st_transform(i.geom, 900913))), e.quantity, p.pollutantcode, i.facilityid " +
				"FROM industry i, pollutant p, emission e " +
				"WHERE i.facilityid = e.facilityid " +
				"AND p.pollutantid = e.pollutantid " +
				"AND regionname = 'Valencia' " +
				"AND p.pollutantcode = '" + pollutantInput + "' " +
				"AND e.reportingyear = '" + yearInput + "' ";
		
		String qryOutput="SELECT gid, st_X(st_centroid(st_astext(st_transform(the_geom, 900913)))), st_Y(st_centroid(st_astext(st_transform(the_geom, 900913)))), st_astext(the_geom) " +
				"FROM surface1 ";/* +
				"WHERE gid = 1" +
				"or gid = 2 " +
				"or gid = 3 " +
				"or gid = 4 " +
				"or gid = 5";*/
		
		SurfaceData[] surfaceData = null;
		
		try {
			Connection connection = getConnection();
			Statement selectInput = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			Statement selectOutput = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			
			ResultSet resInput = selectInput.executeQuery(qryInput);
			ResultSet resOutput = selectOutput.executeQuery(qryOutput);
			
			//	Initialize object into the size we need, like a recordset
			int rsSize = getResultSetSize(resOutput);
			surfaceData = new SurfaceData[rsSize];
			//System.out.println(rsSize);
		
			int i = 0;
			while (resOutput.next()){
				surfaceData[i] = new SurfaceData(pollutantInput, yearInput);
				surfaceData[i].gid = resOutput.getInt(1);
				double xOutput = resOutput.getDouble(2);
				double yOutput = resOutput.getDouble(3);
				//System.out.println(xOutput + " " + yOutput);
				surfaceData[i].wkt = resOutput.getString(4);
				//System.out.println(i); 
				//LonLat myLonLatOutput = new LonLat(xOutput, yOutput);
				//myLonLatOutput.transform("EPSG:4326", "EPSG:900913");
								
				double sumChisl = 0;
				double sumWeight = 0;
				double sumZ = 0;
				int tempCount=1;
				while(resInput.next()){
					double xInput = resInput.getDouble(1);
					double yInput = resInput.getDouble(2);
					
					//System.out.println(xInput + " " + yInput);
					double zInput = resInput.getDouble(3);
					double facID = resInput.getInt(5);
					//LonLat myLonLatInput = new LonLat(xInput, yInput);
					//myLonLatInput.transform("EPSG:4326", "EPSG:900913");
					
					//double distance = Math.sqrt(Math.pow(xInput-xOutput, 2) + 
					//		Math.pow(yInput-yOutput, 2));
					
					double distance = Math.sqrt
							((xInput-xOutput)*(xInput-xOutput) + 
							(yInput-yOutput)*(yInput-yOutput));
					
					//double weight = 1/Math.pow(distance, 2);
					double weight = 1/(distance * distance);
					
					sumWeight = sumWeight + weight;
					System.out.println("weight:" + weight);
					System.out.println("summa of weights=" + sumWeight);
					sumChisl = sumChisl + (weight*zInput);
					System.out.println("temporary chisl=" + weight*zInput);
					System.out.println("summa chislitel = " + sumChisl);
					//sumZ = sumZ + (sumChisl/sumWeight);
					System.out.println("z("+facID + ") = " + sumChisl/sumWeight + " ; distance = " + distance + "; z(source) = " + zInput);
					tempCount++;
					System.out.println("-------------------------------------------------------------------------------------");
				}
				sumZ = sumZ + (sumChisl/sumWeight);
				System.out.println(sumZ);
				resInput.beforeFirst();
				//System.out.println("Inside input loop:" + tempCount);
				surfaceData[i].zValue = sumZ;
				//System.out.println(surfaceData[i].gid +" = " + surfaceData[i].zValue);
				i++;
			}
			resOutput.close();
			connection.close();
		} catch (SQLException e) {
			System.err.println("PostGIS error: " + qryOutput);
			e.printStackTrace();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return surfaceData;
	}
	
	
	

}
