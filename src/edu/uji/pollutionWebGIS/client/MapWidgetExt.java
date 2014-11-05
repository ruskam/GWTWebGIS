package edu.uji.pollutionWebGIS.client;

import java.util.HashMap;

import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.MapOptions;
import org.gwtopenmaps.openlayers.client.OpenLayers;
import org.gwtopenmaps.openlayers.client.control.*;
import org.gwtopenmaps.openlayers.client.event.ControlActivateListener;
import org.gwtopenmaps.openlayers.client.event.GetFeatureInfoListener;
import org.gwtopenmaps.openlayers.client.feature.Feature;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.format.GML;
import org.gwtopenmaps.openlayers.client.geometry.Point;
import org.gwtopenmaps.openlayers.client.layer.GMapType;
import org.gwtopenmaps.openlayers.client.layer.Google;
import org.gwtopenmaps.openlayers.client.layer.GoogleOptions;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.LayerOptions;
import org.gwtopenmaps.openlayers.client.layer.OSM;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.layer.VectorOptions;
import org.gwtopenmaps.openlayers.client.layer.WMS;
import org.gwtopenmaps.openlayers.client.layer.WMSOptions;
import org.gwtopenmaps.openlayers.client.layer.WMSParams;
import org.gwtopenmaps.openlayers.client.popup.Popup;
import org.gwtopenmaps.openlayers.client.protocol.WFSProtocol;
import org.gwtopenmaps.openlayers.client.protocol.WFSProtocolOptions;
import org.gwtopenmaps.openlayers.client.strategy.BBoxStrategy;
import org.gwtopenmaps.openlayers.client.strategy.Strategy;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;

public class MapWidgetExt  extends org.gwtopenmaps.openlayers.client.MapWidget {

	//Constants
	public HTMLPanel displayPanel;
	private double defaultLon = -0.376805; 
	private double defaultLat = 39.470239;
	private int defaultZoom = 7;
	
	//Variables and attributes
	
	private HashMap<String, Layer> layers = new HashMap<String, Layer>();
	
	public MapWidgetExt(String width, String height, MapOptions options){
		
		super(width, height, options);

		// Proxy
		OpenLayers.setProxyHost("gwtOpenLayersProxy?targetURL=");

		// Base Map (OSM)
		OSM osmBaseLayer = OSM.Mapnik("OSM");
		osmBaseLayer.setIsBaseLayer(true);
		LayerOptions osmLayerOptions = new LayerOptions();
		osmLayerOptions.setVisibility(false);
		
		// Base Map (Google)
		GoogleOptions googleOptions = new GoogleOptions();
		googleOptions.setIsBaseLayer(true);
		googleOptions.setSphericalMercator(true);
		//googleOptions.setType(GMapType.G_SATELLITE_MAP);
		googleOptions.setType(GMapType.G_NORMAL_MAP);
		//googleOptions.setType(GMapType.G_HYBRID_MAP);
		Google googleLayer = new Google("Google map",googleOptions);
		
		this.getMap().addLayer(osmBaseLayer);
		this.getMap().addLayer(googleLayer);
		
		// Controls
		this.getMap().addControl(new LayerSwitcher());
		this.getMap().addControl(new ZoomBox());
		this.getMap().addControl(new ScaleLine());
		//**new controls
		this.getMap().addControl(new NavToolbar());
		//this.getMap().addControl(new WMSGetFeatureInfo());
		this.getMap().addControl(new OverviewMap());
		
		//this.getMap().addControl(new PanZoom());
		//this.getMap().addControl(new PanZoomBar());
		//this.getMap().addControl(new ScaleLine());
		
		
		
		
				
		
	}
	
	/**
	 * Method to default center the map
	 */
	public void defaultCenter() {
		LonLat lonLat = new LonLat(defaultLon, defaultLat);
		lonLat.transform("EPSG:4326","EPSG:900913");
		this.getMap().setCenter(lonLat, defaultZoom);
	}
	
	/**
	 * Method to add GML layer from string data source
	 * 
	 * @param withName
	 * @param andLayerData
	 */
	public void addGMLLayerFromSource(String withName, String andLayerData) {
		GML gml = new GML();
		Vector vectorLayer = new Vector(withName);
		vectorLayer.addFeatures(gml.read(andLayerData));
		getMap().addLayer(vectorLayer);
		layers.put(withName, vectorLayer);
	}


	/**
	 * Method to add WMS layer from URL
	 * 
	 * @param withName
	 * @param andURL
	 * @param andLayerName
	 */
	public void addWMSLayer(String withName, String andURL, String andLayerName) {
		WMSParams params = new WMSParams();
		params.setFormat("image/png");
		params.setLayers(andLayerName);
		params.setIsTransparent(true);

		WMSOptions options = new WMSOptions();
		options.setIsBaseLayer(false);
		options.setVisibility(false);

		WMS wmsLayer = new WMS(withName, andURL, params, options);
		getMap().addLayer(wmsLayer);
		layers.put(withName, wmsLayer);
		
		//Add "feature info" control
		
		WMSGetFeatureInfoOptions controlOptions = new WMSGetFeatureInfoOptions();
		controlOptions.setURL("http://geoinfo.dlsi.uji.es:8080/geoserver/wms");
		controlOptions.setTitle("Identify features by clicking");
		
		WMS [] vectorWMS = {wmsLayer};
		controlOptions.setLayers(vectorWMS);
		controlOptions.setQueryVisible(true);
		WMSGetFeatureInfo control = new WMSGetFeatureInfo(controlOptions);
		control.addGetFeatureListener( new GetFeatureInfoListener() {
			@Override
			public void onGetFeatureInfo(GetFeatureInfoEvent eventObject) {
				// TODO Auto-generated method stub		
				System.out.println(eventObject.getText());
				//Window.alert(eventObject.getText());
				//Window.alert(eventObject.getElement().getPropertyNames());
				
				displayPanel.clear();
				HTML myHtml = new HTML(eventObject.getText());
				displayPanel.add(myHtml);
				
				
				
			}
		});
		
		getMap().addControl(control);
		control.activate();
		
	}

	/**
	 * Method to add WFS layer from URL
	 * 
	 * @param withName
	 * @param andURL
	 * @param andFeatureType
	 * @param andFeatureNamespace
	 */
	public void addWFSLayer(String withName, String andURL,
			String andFeatureType, String andFeatureNamespace) {
		WFSProtocolOptions wfsProtocolOptions = new WFSProtocolOptions();
		wfsProtocolOptions.setUrl(andURL);
		wfsProtocolOptions.setFeatureType(andFeatureType);
		wfsProtocolOptions.setFeatureNameSpace(andFeatureNamespace);

		WFSProtocol wfsProtocol = new WFSProtocol(wfsProtocolOptions);

		VectorOptions vectorOptions = new VectorOptions();
		vectorOptions.setProtocol(wfsProtocol);
		vectorOptions.setProjection("EPSG:4326");
		vectorOptions.setStrategies(new Strategy[] { new BBoxStrategy() });
		vectorOptions.setVisibility(false);

		Vector wfsLayer = new Vector(withName, vectorOptions);
		
		getMap().addLayer(wfsLayer);
		layers.put(withName, wfsLayer);
		
		
		GetFeatureOptions getFeatureOptions = new GetFeatureOptions();
		getFeatureOptions.setBox(true);
		getFeatureOptions.setProtocol(wfsProtocol);
		getFeatureOptions.setHover(true);
		getFeatureOptions.setMultipleKey("shiftKey");
		getFeatureOptions.setToggleKey("ctrlKey");
		GetFeature getFeatureControl = new GetFeature(getFeatureOptions);
		this.getMap().addControl(getFeatureControl);
		getFeatureControl.addControlActivateListener(
			new ControlActivateListener() {
				
				public void onActivate(ControlActivateEvent eventObject) 
				{
					Window.alert("Feature clicked!!");
				}
			}
			
		);		
	
	}

	
	
	/**
	 * Method to add a geometry layer
	 * 
	 * @param withName
	 * @param andLayerVector
	 */
	public void addGeometryLayer(String withName, Vector andLayer) {
		getMap().addLayer(andLayer);
		layers.put(withName, andLayer);
	}

	/**
	 * Method to remove a layer
	 * 
	 * @param withName
	 * @return success
	 */
	public Boolean removeLayer(String withName) {
		if (layers.containsKey(withName)) {
			getMap().removeLayer(layers.get(withName));
			layers.remove(withName);
			return true;
		}
		return false;
	}
	
	//setter for HTMLPanel widget object
	public void setDisplayPanel(HTMLPanel p){
		this.displayPanel = p;
	}

	public void addCities(String layerName, String vectorName) {
		// TODO Auto-generated method stub

		//stYtb = new Style();
		//stYtb.setGraphicSize(20, 20);
		//stYtb.setFillOpacity(0.5);
		//stYtb.setExternalGraphic("http://elcano.dlsi.uji.es:8082/WB20_BROKER/ico/youtube.png");
		
		
		LonLat myLonLat = new LonLat(0, 40);
		myLonLat.transform("EPSG:4326","EPSG:900913");
		Point point = new Point(myLonLat.lon(),myLonLat.lat());
		VectorFeature myPoint = new VectorFeature(point);
		//myPoint.setStyle(stYtb);
		
		LonLat myLonLat1 = new LonLat(7, 52);
		myLonLat1.transform("EPSG:4326","EPSG:900913");
		Point point1 = new Point(myLonLat1.lon(),myLonLat1.lat());
		VectorFeature myPoint1 = new VectorFeature(point1);
		//myPoint1.setStyle(stYtb);
		
		LonLat myLonLat2 = new LonLat(76.89, 43.27);
		myLonLat2.transform("EPSG:4326", "EPSG:900913");
		Point point2 = new Point(myLonLat2.lon(),myLonLat2.lat());
		VectorFeature myPoint2 = new VectorFeature(point2);
		/*Popup pop = myPoint2.createPopup(true);	
		pop.setContentHTML("<H1>" + "Almaty" +  "</H1>");
		pop.show();*/
		//myPoint2.setStyle(stYtb);
		
		VectorFeature[] vfArray = new VectorFeature[3];
		vfArray[0] = myPoint;
		vfArray[1] = myPoint1;
		vfArray[2] = myPoint2;
		
		Vector myV = new Vector(layerName);
		//myV.addFeature(myPoint);
		//myV.addFeature(myPoint1);
		
		myV.addFeatures(vfArray);
		getMap().addLayer(myV);
		
		layers.put(layerName, myV);
		
	}
	
	
	
}