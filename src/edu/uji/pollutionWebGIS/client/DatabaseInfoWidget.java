package edu.uji.pollutionWebGIS.client;

//import org.apache.commons.lang.StringUtils;
import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.Marker;
import org.gwtopenmaps.openlayers.client.Size;
import org.gwtopenmaps.openlayers.client.Style;
import org.gwtopenmaps.openlayers.client.util.Attributes;
import org.gwtopenmaps.openlayers.client.control.SelectFeature;
import org.gwtopenmaps.openlayers.client.control.SelectFeature.ClickFeatureListener;
import org.gwtopenmaps.openlayers.client.control.SelectFeatureOptions;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.format.WKT;
import org.gwtopenmaps.openlayers.client.geometry.Geometry;
import org.gwtopenmaps.openlayers.client.geometry.GeometryImpl;
import org.gwtopenmaps.openlayers.client.geometry.LinearRing;
import org.gwtopenmaps.openlayers.client.geometry.Point;
import org.gwtopenmaps.openlayers.client.geometry.Polygon;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.popup.Popup;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sun.java.swing.plaf.windows.resources.windows;
import com.sun.org.apache.bcel.internal.generic.PopInstruction;

public class DatabaseInfoWidget extends Composite{
	private MapWidgetExt oMapWid;
	private MyRPCServiceAsync rpc;
	private VerticalPanel pWidget = new VerticalPanel();
	private LoadingWidget loading = new LoadingWidget();
	//aka pBibleTable
	private VerticalPanel pDataTable = new VerticalPanel();
	//Table for the database info
	private Grid grid = null;
	
	//for creation of the query layer
	String retrYear;
	String retrPoll;
	String retrQuantity;
	
	//for creation of the IDW layer
	String retrYearIDW;
	String retrPollIDW;
	
	//private Style myStyle;
	
	public DatabaseInfoWidget(MapWidgetExt m){
		//HorizontalPanel hp = new HorizontalPanel();
		//hp.add(new HTML("Industries located in the Valencian Community"));
		//pWidget.add(hp);
		//pWidget.add(pDataTable);
		initWidget(pWidget);
		// init rpc
		rpc = RPCInit.initRPC();
		oMapWid = m;
		
	}
	
	public void draw(String concInput, String pollutantInput, String yearInput){
		//aka getBibleInfo
		getPollutionData(concInput, pollutantInput, yearInput);
		
		
	}
	
	public void drawSurface(String pollutantInput, String yearInput){
		getSurfaceData(pollutantInput, yearInput);
	}
	
	
	private void getSurfaceData(String pollutantInput, String yearInput) {
		
		rpc.getSurfaceData(pollutantInput, yearInput, new AsyncCallback<SurfaceData[]>(){

			//@Override
			public void onSuccess(SurfaceData[] surfaceData) {
				
				Vector myLayer = getSurfaceLayer(surfaceData);	
				oMapWid.getMap().addLayer(myLayer);
				oMapWid.getMap().zoomToExtent(myLayer.getDataExtent());
			
				SelectFeatureOptions selectFeatureOptions = new SelectFeatureOptions();
		        
				selectFeatureOptions.clickFeature(new ClickFeatureListener() {
		                public void onFeatureClicked(VectorFeature eventObject) {
		                	showPopupSurf(eventObject);
		                }
		        });
		        SelectFeature selectFeatureClick = new SelectFeature(myLayer,
		                selectFeatureOptions);
		        oMapWid.getMap().addControl(selectFeatureClick);
		        selectFeatureClick.activate();
				
			}
			
			
			
			//@Override
			public void onFailure(Throwable caught) {
				RootPanel.get("pollution-container").add(new HTML(caught.toString()));
			}

		});
		
	}

	// Draw db info on the screen after rpc callback
	 
	private void drawDataInfo(PollutionData[] pollutionData){
		//if null nothing to do, then exit
		if (pollutionData == null) {
			return;
		}
		int rows = pollutionData.length;
		//System.out.println("Number of records found: " + rows);
		
		//set up the table the database info will go into
		grid = new Grid(rows + 1, 7);
		
		pDataTable.add(grid);
		
		Label lFacID = new Label ("ID");
		Label lFacName = new Label("Name");
		Label lRegName = new Label("Region");
		Label lYear = new Label("Year");
		Label lQuant = new Label("Quantity");
		Label lLong = new Label("Longitude");
		Label lLat = new Label("Latitude");
		
		lFacID.setTitle("ID");
		lFacName.setTitle("Name");
		lRegName.setTitle("Region");
		lYear.setTitle("Year");
		lQuant.setTitle("Quantity");
		lLong.setTitle("Longitude");
		lLat.setTitle("Latitude");
		
		//label first row of the grid
		grid.setWidget(0, 0, lFacID);
		grid.setWidget(0, 1, lFacName);
		grid.setWidget(0, 2, lRegName);
		grid.setWidget(0, 3, lYear);
		grid.setWidget(0, 4, lQuant);
		grid.setWidget(0, 5, lLong);
		Style myStyle = new Style();
		myStyle.setFillColor("FF0000");
		grid.setWidget(0, 6, lLat);
		
		
		
		// go through the industries
		for (int i = 0; i < rows; i++){
			String facID = Integer.toString(pollutionData[i].facilityID);
			String facName = pollutionData[i].facilityName;
			String regName = pollutionData[i].regionName;
			String year = pollutionData[i].year;
			String quant = Double.toString(pollutionData[i].quantity);
			String lon = Double.toString(pollutionData[i].longitude);
			String lat = Double.toString(pollutionData[i].latitude);
			
			grid.setWidget(i+1, 0, new HTML(facID));
			grid.setWidget(i+1, 1, new HTML(facName));
			grid.setWidget(i+1, 2, new HTML(regName));
			grid.setWidget(i+1, 3, new HTML(year));
			grid.setWidget(i+1, 4, new HTML(quant));
			grid.setWidget(i+1, 5, new HTML(lon));
			grid.setWidget(i+1, 6, new HTML(lat));
			
			//row style
			boolean even = i % 2 == 0;
			String style = "";
			if (even == true) {
				style ="rs-even";
			}
			else {
				style = "rs-odd";
			}
			grid.getRowFormatter().setStyleName(i+1, style);
			
		}
		grid.setStyleName("dataTable");
		
	}
	
	//RPC request to get the industry info
	private void getPollutionData(String concInput, String pollutantInput, String yearInput) {
		// draw loading
		//loading.show();
		
		//remote procedure call to the server to get the database info
		rpc.getPollutionData(concInput, pollutantInput, yearInput, new AsyncCallback<PollutionData[]>() {
			/*public void getPollutionData(AsyncCallback<PollutionData[]> callback){
				System.out.println("calling the method");
			}*/
			
			public void onSuccess(PollutionData[] pollutionData){
				loading.show();
				drawDataInfo(pollutionData);
				Vector myLayer = getLayer(pollutionData);
				oMapWid.getMap().addLayer(myLayer);
				// Zoom to a layer extent
				oMapWid.getMap().zoomToExtent(myLayer.getDataExtent());
								
				//click listener
				SelectFeatureOptions selectFeatureOptions = new SelectFeatureOptions();
		        selectFeatureOptions.clickFeature(new ClickFeatureListener() {
		                public void onFeatureClicked(VectorFeature eventObject) {
		                	showPopup(eventObject);
		                }
		        });
		        SelectFeature selectFeatureClick = new SelectFeature(myLayer,
		                selectFeatureOptions);
		        oMapWid.getMap().addControl(selectFeatureClick);
		        selectFeatureClick.activate();
		        /////////////////////////////////////////////
				loading.hide();
			}
			
			public void onFailure(Throwable caught){
				RootPanel.get("pollution-container").add(new HTML(caught.toString()));
			}
		});
		
	}
	
	public void showPopup(VectorFeature vFeature){
		//Get coordinates
		LonLat myLonLat = vFeature.getGeometry().getBounds().getCenterLonLat();
		
		String industryid = vFeature.getAttributes().getAttributeAsString("ID");
		String facName = vFeature.getAttributes().getAttributeAsString("Industry");
		String pollName = vFeature.getAttributes().getAttributeAsString("Pollutant");
		String year = vFeature.getAttributes().getAttributeAsString("Year");
		String quantity = vFeature.getAttributes().getAttributeAsString("Quantity");

		Popup pop = new Popup(industryid+"popup",myLonLat, new Size(200,200),"",true);
		//pop.setContentHTML("<td>" + industryName +  "</td>" + "<td>" + pollutantName + "</td>");
		pop.setContentHTML("<table border='1'>" + 
			"<tr>" + 
			"<td>" + "ID" + "</td>" + 
			"<td>" + industryid + "</td>" + 
			"</tr>" + 
			"<tr>" + 
			"<td>" + "Industry" + "</td>" +
			"<td>" + facName + "</td>" +
			"</tr>" +
			"<tr>" + 
			"<td>" + "Pollutant" + "</td>" +
			"<td>" + pollName + "</td>" +
			"</tr>" +
			"<td>" + "Reporting year" + "</td>" +
			"<td>" + year + "</td>" +
			"</tr>" +
			"<tr>" + 
			"<td>" + "Quantity, kg" + "</td>" +
			"<td>" + quantity + "</td>" +
			"</tr>" +
			"</table>"
		); 
		//pop.setLonLat(myLonLat);
		pop.setAutoSize(true);
		pop.setOpacity(0.7);
		oMapWid.getMap().addPopup(pop);
		pop.show();
		
	}
	
	public Vector getLayer(PollutionData[] pollutionData){
		
		//Insert style for a point
		Style myStyle = new Style();
		myStyle.setGraphicSize(20, 20);
		myStyle.setExternalGraphic("http://hg-pavlodar.narod.ru/factoryNew.png");
		myStyle.setFillOpacity(0.9);
		if (pollutionData == null) {
			return null;
		}
		//Window.alert("Hello, i have " + String.valueOf(pollutionData.length));
		int rows = pollutionData.length;
		
		//System.out.println(rows);
		
		//Point[] pointList = new Point[rows];
		VectorFeature[] pointVectorFeatureList = new VectorFeature[rows]; 
		for (int i = 0; i < rows; i++){
			
			LonLat myLonLat = new LonLat(pollutionData[i].longitude, pollutionData[i].latitude);
			myLonLat.transform("EPSG:4326", "EPSG:900913");
			Point newPoint = new Point(myLonLat.lon(), myLonLat.lat());
			//System.out.println(pollutionData[i].longitude + "; " + pollutionData[i].latitude);
			//System.out.println(myLonLat.lon() + "; " + myLonLat.lat());
			//System.out.println("-----------------------------------");
			VectorFeature pointVectorFeature = new VectorFeature(newPoint);
			
			pointVectorFeature.setStyle(myStyle);
			WKT myWKT = new WKT();
			//LonLat myLL = new LonLat(24,2342);
			//myWKT = s;
			//Polygon myPolygon = new Polygon();
			
			
			// Start: adding attributes
			final String industryid = Integer.toString(pollutionData[i].facilityID);
			final String facName = pollutionData[i].facilityName;
			final String pollName = pollutionData[i].pollName;
			final String year = pollutionData[i].year;
			final String quantity = Double.toString(pollutionData[i].quantity);
			
			retrYear = year;
			retrPoll = pollName;
			retrQuantity = quantity;
			
			pointVectorFeature.setAttributes(new Attributes(){
				{
					this.setAttribute("ID", industryid);
					this.setAttribute("Industry", facName);
					this.setAttribute("Pollutant", pollName);
					this.setAttribute("Year", year);
					this.setAttribute("Quantity", quantity);
				}
			});
			// End: adding attributes
			/*System.out.println(pointVectorFeature.getAttributes().getAttributeAsString("Industry"));
			System.out.println(pointVectorFeature.getAttributes().getAttributeAsString(pollutantName));
			System.out.println(pointVectorFeature.getAttributes().getAttributeAsString(year));
			System.out.println(pointVectorFeature.getAttributes().getAttributeAsString(concentration));*/
			
			pointVectorFeatureList[i] = pointVectorFeature;
			
		}
		
		if (rows==1) {
			
			Window.alert("A layer containing " + rows + " feature created");
		}
		else if(rows > 1) {
			
			Window.alert("A layer containing " + rows + " features created");
		}
		else { Window.alert("No features meeting the query criteria found");
		}
		
		
        
		if (rows == 1) {
			Vector layer = new Vector("Industry that emitted " + retrPoll + " > " + retrQuantity + " in " + retrYear);
			layer.addFeatures(pointVectorFeatureList);
			return layer;
		}
		else if (rows > 1) {
			Vector layer = new Vector("Industries that emitted " + retrPoll + " > " + retrQuantity + " in " + retrYear);
			layer.addFeatures(pointVectorFeatureList);
			return layer;
		}
		else {
			return null;
		}
		
	}
	
	public Vector getSurfaceLayer(SurfaceData[] surfaceData){
		
		Style redStyle = new Style();
		redStyle.setFillColor("#990000");
		redStyle.setFillOpacity(1);
		redStyle.setStrokeColor("#990000");
		redStyle.setStrokeWidth(1);
		
		
		Style orangeStyle = new Style();
		orangeStyle.setFillColor("#FF9900");
		orangeStyle.setFillOpacity(1);
		orangeStyle.setStrokeColor("#FF9900");
		orangeStyle.setStrokeWidth(1);
		
				
		Style yellowStyle = new Style();
		yellowStyle.setFillColor("#FFFF00");
		yellowStyle.setFillOpacity(1);
		yellowStyle.setStrokeColor("#FFFF00");
		yellowStyle.setStrokeWidth(1);
		
		Style greenStyle = new Style();
		greenStyle.setFillColor("#00FF00");
		greenStyle.setFillOpacity(1);
		greenStyle.setStrokeColor("#00FF00");
		greenStyle.setStrokeWidth(1);
		
		if (surfaceData== null){
			return null;
		}
		
		int rows = surfaceData.length;
		
		VectorFeature[] polygonFeatureList = new VectorFeature[rows];
		
		// Array to find min
		double[] array = new double[rows];
		for (int i=0; i < array.length; i++){
			array[i]=surfaceData[i].zValue;
		}
		
		double[] maxArray = new double[rows];
		for (int i=0; i < maxArray.length; i++){
			maxArray[i]=surfaceData[i].zValue;
		}
		
		double minValue = getMinValue(array);
		double maxValue = getMaxValue(array);
		System.out.println("min=" + minValue);
		System.out.println("max=" + maxValue);
		double difference = getMaxValue(array) - getMinValue(array);
		double range = difference/4;
		double range1 = getMinValue(array);
		double range2 = range1 + range;
		double range3 = range2 + range;
		double range4 = range3 + range;
		double range5 = range4 + range;
	
		// Array to find maz
		
		//temp
		//VectorFeature[] pointFeatureList = new VectorFeature[1000];
		
		for (int i=0; i < rows; i++){
			int cID = surfaceData[i].gid;
			String wktRaw1 = surfaceData[i].wkt;
			String wktRaw2 = wktRaw1.replaceAll("MULTIPOLYGON\\(\\(\\(", "");
			String wktFinal = wktRaw2.replaceAll("\\)\\)\\)", "");
			String [] coords = wktFinal.split(",");
			Point[] pointList = new Point[coords.length];
			
			for (int pointNumber = 0; pointNumber < coords.length; pointNumber++){
				//String temp = null;
				//System.out.println("Cell ID=" + cID);
				//System.out.println("Point in the cell");
				//System.out.println(coords[pointNumber]);
				//double longitude;
				//double latitude;
				String temp = coords[pointNumber];
				//System.out.println(temp);
				String [] crds = temp.split(" ");
				String X = crds[0];
				String Y = crds[1];
				
				//Float longTemp = new Float (X);
				//double longitude = longTemp.doubleValue();
				Double longitude = new Double(X);
				//Float latTemp = new Float (Y);
				//double latitude = latTemp.doubleValue();
				Double latitude = new Double(Y);
				LonLat myLonLat = new LonLat(longitude, latitude);
				
				//myLonLat.transform("EPSG:4326", "EPSG:900913");
				myLonLat.transform("EPSG:4326", "EPSG:900913");
				Point newPoint = new Point(myLonLat.lon(), myLonLat.lat());
				//System.out.println(myLonLat.lon()  + "; " + myLonLat.lat());
				//System.out.println(myLonLat.lon()  + "; " + myLonLat.lat());
				pointList[pointNumber] = newPoint;
				//System.out.println("----------------------------------------------");
				
				//temp
				//VectorFeature pointVectorFeature = new VectorFeature(newPoint);
				//pointFeatureList[pointNumber] = pointVectorFeature;
				//LonLat myLonLat = new LonLat(pollutionData[i].longitude, pollutionData[i].latitude);
			}
			LinearRing linearRing = new LinearRing(pointList);
			LinearRing[] linearRingArray = { linearRing };
			Polygon polygon = new Polygon(linearRingArray);
			VectorFeature polygonFeature = new VectorFeature(polygon);
			
			double z = surfaceData[i].zValue;
			if ((z >= range1) && (z < range2)){
				polygonFeature.setStyle(greenStyle);
			}
			else if ((z >= range2) && (z < range3)){
				polygonFeature.setStyle(yellowStyle);
			}
			else if ((z >= range3) && (z < range4)){
				polygonFeature.setStyle(orangeStyle);
			}
			else if	//((z >= range4) && (z <= range5))
				(z >=range4){
				polygonFeature.setStyle(redStyle);
			}
			
			//polygonFeature.setStyle(mySurfStyle);
			//creating attributes
			
			final String gid = Integer.toString(surfaceData[i].gid);
			System.out.println("Result from the client");
			System.out.println(gid);
			final String zValue = Double.toString(surfaceData[i].zValue);
			System.out.println(zValue);
			
			
			
			polygonFeature.setAttributes(new Attributes(){
				{
					this.setAttribute("id", gid);
					this.setAttribute("conc", zValue);
					
				}
			});
			
			polygonFeatureList[i] = polygonFeature;
			
			
			
			//System.out.println("point vectorfeatureArray size is " + pointFeatureList.length);
			//layer.addFeatures(pointVectorFeatureList);
			
			
		}
		
		if (rows==1) {
			Window.alert("A layer containing " + rows + " feature created");
		}
		else if(rows > 1) {
			Window.alert("A layer containing " + rows + " features created");
		}
		else { Window.alert("No features meeting the query criteria found");
		}
		
		///////////////////////////////////////////////////////////////////
		/*Point [] pointL = new Point[4];
		
		double lon = -1.510688;
		double lat = 39.455223; 
		LonLat myLonLat = new LonLat(lon, lat);
		myLonLat.transform("EPSG:4326", "EPSG:900913");
		Point newPoint = new Point(myLonLat.lon(),myLonLat.lat());
		VectorFeature pointVectorFeature = new VectorFeature(newPoint);
		pointL[0] = newPoint;
		double lon1 = -1.508364;
		double lat1 = 39.462417; 
		LonLat myLonLat1 = new LonLat(lon1, lat1);
		myLonLat1.transform("EPSG:4326", "EPSG:900913");
		Point newPoint1 = new Point(myLonLat1.lon(),myLonLat1.lat());
		VectorFeature pointVectorFeature1 = new VectorFeature(newPoint1);
		pointL[1] = newPoint1;
		double lon2 = -1.499067;
		double lat2 = 39.460619; 
		LonLat myLonLat2 = new LonLat(lon2, lat2);
		myLonLat2.transform("EPSG:4326", "EPSG:900913");
		Point newPoint2 = new Point(myLonLat2.lon(),myLonLat2.lat());
		VectorFeature pointVectorFeature2 = new VectorFeature(newPoint2);
		pointL[2] = newPoint2;
		double lon3 = -1.510688;
		double lat3 = 39.455223; 
		LonLat myLonLat3 = new LonLat(lon3, lat3);
		myLonLat3.transform("EPSG:4326", "EPSG:900913");
		Point newPoint3 = new Point(myLonLat3.lon(),myLonLat3.lat());
		VectorFeature pointVectorFeature3 = new VectorFeature(newPoint3);
		pointL[3] = newPoint3;
		LinearRing lrTemp = new LinearRing(pointL);
		LinearRing[] lrArray = { lrTemp };
		Polygon pl = new Polygon(lrArray);
		VectorFeature vf = new VectorFeature(pl);*/
		
		//////////////////////////////////
		
		
		
		if (rows == 1) {
			
			Vector layer = new Vector("IDW Interpolation Layer");
			
			//layer.addFeature(pointVectorFeature);
			//layer.addFeature(pointVectorFeature1);
			//layer.addFeature(pointVectorFeature2);
			//layer.addFeature(pointVectorFeature3);
			//layer.addFeature(vf);
			layer.addFeatures(polygonFeatureList);
			return layer;
		}
		else if (rows > 1) {
			
			Vector layer = new Vector("IDW Interpolation Layer");
			
			layer.addFeatures(polygonFeatureList);
			return layer;
		}
		else {
			return null;
		}
		
		
	}
	
	public void showPopupSurf(VectorFeature vFeature){
		
		LonLat myLonLat = vFeature.getGeometry().getBounds().getCenterLonLat();
		
		String cellid = vFeature.getAttributes().getAttributeAsString("id");
		String cellconc = vFeature.getAttributes().getAttributeAsString("conc");
		
		Popup myPopup = new Popup(cellid + "Popup", myLonLat, new Size(200,200), "", true);
		myPopup.setContentHTML("<table border='1'>" + 
				"<tr>" + 
				"<td>" + "ID" + "</td>" + 
				"<td>" + cellid + "</td>" + 
				"</tr>" + 
				"<tr>" + 
				"<td>" + "Concentration" + "</td>" +
				"<td>" + cellconc + "</td>" +
				"</tr>" +
				"</table>"
		);
		myPopup.setAutoSize(true);
		myPopup.setOpacity(0.7);
		oMapWid.getMap().addPopup(myPopup);
		myPopup.show();
	}
	
	public static double getMaxValue(double[] array){
		double maxValue = array[0];
        for(int i=1;i < array.length;i++){  
        	if(array[i] > maxValue){  
	            maxValue = array[i];  
	        }  
        }
        return maxValue;  
	}  

	public static double getMinValue(double[] array){
        double minValue = array[0];  
	    for(int i=1;i<array.length;i++){
	    	if(array[i] < minValue){
	    		minValue = array[i];  
	        }  
	    }  
	    return minValue;  
	} 
	
}



















