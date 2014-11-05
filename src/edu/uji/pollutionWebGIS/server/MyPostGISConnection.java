package edu.uji.pollutionWebGIS.server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public abstract class MyPostGISConnection{
	
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private ResultSetMetaData metaData;
	private int columnNumber;
	private String query;
	
	
		
	protected Connection getConnection(){
		Connection conn = null;
			
		String url = "jdbc:postgresql://localhost:5432/";
		//String url = "jdbc:postgresql://geoinfo.dlsi.uji.es:5432/";
		String db = "mydb";
		String driver = "org.postgresql.Driver";
		String username = "postgres";
		String password = "jananina";
		url = url + db;
			
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url, username, password);
				
		} catch (Exception e) {
			System.out.println("PostGIS connection failed");
			e.printStackTrace();
		}
			
		if (conn == null){
			System.out.println("--------can't get MyPostGISConnection");
		}
		
		/*getResultSet();
		getResultSet();
		statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);*/
			
		return conn;
		}
	

	// Get RecorSet (table of data)  row count
	public static int getResultSetSize(ResultSet resultSet){
		int size = -1;
				
		try {
			//Moves the cursor to the last row in this ResultSet object
			resultSet.last();
			
			//Retrieves the current row number.
			size = resultSet.getRow();
			
			//Moves the cursor to the front of this ResultSet object, just before the first row
			resultSet.beforeFirst();
		} catch (SQLException e) {
			return size;
		}
		
		return size;
	}
	
	
	public static int getRowCount(ResultSet resultSet) throws SQLException {  
	   int rowCount;  
	   int currentRow = resultSet.getRow(); // Get current row   
	   System.out.println(currentRow);  
	   rowCount = resultSet.last() ? resultSet.getRow() : 0; // Determine number of rows  
	   if (currentRow == 0)                      // If there was no current row  
		   resultSet.beforeFirst();                     // We want next() to go to first row  
	   else                                      // If there WAS a current row  
		   resultSet.absolute(currentRow);              // Restore it  
	   return rowCount;  
	}  

}
