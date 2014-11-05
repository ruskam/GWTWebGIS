package edu.uji.pollutionWebGIS.client;

import org.gwtopenmaps.openlayers.client.Bounds;
import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.MapOptions;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.Style;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.geometry.Point;
import org.gwtopenmaps.openlayers.client.layer.Vector;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.*;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PollutionWebGIS implements EntryPoint {

	private MapWidgetExt mapWidget;
	private DatabaseInfoWidget myDataInfoWidget;
	
	private Style stYtb;

	//Widgets declaration
	
	private HorizontalPanel mainPanel = new HorizontalPanel();
	private	VerticalPanel leftPanel = new VerticalPanel();
	private VerticalPanel rightPanel = new VerticalPanel();
	Button btnNewButton = new Button("dsplRes1");
	Button btnTester = new Button("Test ListBox");
	ScrollPanel myScrollPanel = new ScrollPanel();
	Button btnRpcRun = new Button("Run RPC");
	
	Button btnRpcRunSrf = new Button("Run Surface");
	String desc;
	
	Button btnRpcCalcSrfc = new Button("Run Interpolation");
	//variables related to DB connection
	
	
	//TextBox for query data
	TextBox tboxConc;
	TextBox tbPoll;
	//List Box for query data
	ListBox lbPollCode;
	String itemSelectedValue;
	ListBox lbYear;
	String itemSelectedYear;
	
	//List Box for query data
	ListBox lbPollCode1;
	String itemSelectedValue1;
	ListBox lbYear1;
	String itemSelectedYear1;
	
	
	public void onModuleLoad() {
		initMapWidget();
		initWMSLayers();
		initWFSLayers();
		initVectorLayer();
		initGUI();
			
		
		// Remote Procedure Call
		
//		myDataInfoWidget = new DatabaseInfoWidget(mapWidget);
		
		/*VerticalPanel vp = new VerticalPanel();
		vp.setWidth("100%");
		vp.add(myDataInfoWidget);
		vp.setCellHorizontalAlignment(myDataInfoWidget, HorizontalPanel.ALIGN_CENTER);
				
		//temporarily checked
		myScrollPanel.add(vp);
		boolean alwaysShow = true;
		myScrollPanel.setAlwaysShowScrollBars(alwaysShow);
		myScrollPanel.setSize("300px","300px");
		myScrollPanel.scrollToBottom();
		leftPanel.add(myScrollPanel);
		
		RootPanel.get("pollution-container").add(mainPanel);*/
		
		
		
		//myDataInfoWidget.draw();

	}
	
	public void initMapWidget(){
		MapOptions options = new MapOptions();
		options.setNumZoomLevels(20);
		options.setProjection("EPSG:900913");		
		Projection displayProjection = new Projection("EPSG:4326");		
		options.setDisplayProjection(displayProjection);
		options.setUnits("m");
		options.setMaxResolution(156543.0339f);
		options.setMaxExtent(new Bounds(-20037508.34f, -20037508.34f,
                20037508.34f, 20037508.34f));
		mapWidget = new MapWidgetExt("100%", "100%", options);
		mapWidget.defaultCenter();
				
	}

	public void initWMSLayers(){	
		// Industry
		//mapWidget.addWMSLayer("Industry (WMS)","http://geoinfo.dlsi.uji.es:8080/geoserver/wms","EV:industry");
		mapWidget.addWMSLayer("Industries Valencia (WMS)","http://geoinfo.dlsi.uji.es:8080/geoserver/wms","EV:emission_by_industry_vlc");
		
	}
	
	public void initWFSLayers(){
		// cmapWidget.addWFSLayer("Industry (WFS)","http://geoinfo.dlsi.uji.es:8080/geoserver/wfs","industry","http://www.uji.es");
		//mapWidget.addWFSLayer("Industry (WFS)","http://geoinfo.dlsi.uji.es:8080/geoserver/wfs","emission_by_industry","http://www.uji.es");
		mapWidget.addWFSLayer("Industries Valencia (WFS)","http://geoinfo.dlsi.uji.es:8080/geoserver/wfs","EV:emission_by_industry_vlc","http://www.uji.es");
		
	}
	

	public void initVectorLayer(){
		//mapWidget.addCities("My cities", "Vector layer");
		
	}
	
	public void initGUI(){
		
		myDataInfoWidget = new DatabaseInfoWidget(mapWidget);					
		leftPanel.setBorderWidth(0);
		leftPanel.setSpacing(10);
		
		Label labelTitle1 = new Label("1. Display industries that release more than entered");
		Label labelTitle1a = new Label("amount of emissions");
		// Adding TextBox for Concentration user input
		Label labelConc = new Label("Enter emissions quantity, kg");
		tboxConc = new TextBox();
		leftPanel.add(labelTitle1);
		leftPanel.add(labelTitle1a);
		leftPanel.add(labelConc);
		
		leftPanel.add(tboxConc);
		
		/*// Add TextBox for pollutionName user input
		leftPanel.setSize("357px", "218px");
		tbPoll = new TextBox();
		leftPanel.add(tbPoll);*/
		
		Label labelPoll = new Label("Select pollutant");
		
		leftPanel.add(labelPoll);
		// Add ListBox for pollutionCode user input
		lbPollCode = new ListBox();
		lbPollCode.setTitle("Select pollutant");
		lbPollCode.addItem("BENZENE");
		lbPollCode.addItem("CO");
		lbPollCode.addItem("CO2 in EPER");
		lbPollCode.addItem("FLUORINE AND INORGANIC COMPOUNDS");
		lbPollCode.addItem("NMVOC");
		lbPollCode.addItem("NOX");
		lbPollCode.addItem("PFCS");
		lbPollCode.addItem("PM10");
		lbPollCode.addItem("SOX");
		lbPollCode.addItem("CHLORINE AND INORGANIC COMPOUNDS");
		lbPollCode.addItem("NH3");
		lbPollCode.addItem("HCN");
		lbPollCode.addItem("TETRACHLOROMETHANE (TCM)");
		lbPollCode.addItem("N2O");
		lbPollCode.addItem("CH4");
		lbPollCode.addItem("TRICHLOROMETHANE");
		lbPollCode.addItem("HFCS");
		lbPollCode.addItem("PCDD+PCDF (DIOXINS+FURANS)");
		lbPollCode.addItem("TRICHLOROETHYLENE (TRI)");
		lbPollCode.addItem("TETRACHLOROETHYLENE (PER)");
		lbPollCode.addItem("SF6");
		lbPollCode.addItem("TRICHLOROETHANE-1,1,1 (TCE)");
		lbPollCode.addItem("HEXACHLOROBENZENE (HCB)");
		lbPollCode.addItem("TRICHLOROBENZENES (TCB)");
		lbPollCode.addItem("CO2");
		lbPollCode.addItem("POLYCYCLIC AROMATIC HYDROCARBONS");
		lbPollCode.addItem("DEHP");
		lbPollCode.addItem("POLYCHLORINATED BIPHENYLS (PCBS)");
		lbPollCode.addItem("HCFCS");
		lbPollCode.addItem("NAPHTHALENE");
		lbPollCode.addItem("PENTACHLOROBENZENE");
		lbPollCode.addItem("CFCS");
		lbPollCode.addItem("CO2 EXCL BIOMASS");
		lbPollCode.addItem("ANTHRACENE");
		lbPollCode.addItem("HALONS");
		lbPollCode.addItem("TETRACHLOROETHANE-1,1,2,2");
		lbPollCode.addItem("ETHYLENE OXIDE");
		lbPollCode.addItem("OTHGAS");
		lbPollCode.addItem("GRHGAS");
		
		
		
		lbPollCode.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent arg0) {
				// Get index of the selected item in the list box
				int itemSelectedIndex = lbPollCode.getSelectedIndex();
				// Get the
				itemSelectedValue = lbPollCode.getValue(itemSelectedIndex);
				//Window.alert(itemSelectedValue);
				
			}
			
		});
		
		leftPanel.add(lbPollCode);
		
		
		Label labelYear = new Label("Select reporting year");
		
		leftPanel.add(labelYear);
		lbYear = new ListBox();
		lbYear.setTitle("Select reporting year");
		lbYear.addItem("2001");
		lbYear.addItem("2004");
		lbYear.addItem("2007");
		lbYear.addItem("2008");
		lbYear.addItem("2009");
		
		lbYear.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent arg0) {
				int itemSelectedIndex = lbYear.getSelectedIndex();
				itemSelectedYear = lbYear.getValue(itemSelectedIndex);
				//Window.alert(itemSelectedYear);
			}
			
		});
		leftPanel.add(lbYear);
		
		//Button to run RPC and the query
		btnNewButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				myDataInfoWidget.draw(tboxConc.getText() , getListBoxValue(), getListBoxYear());
				//myDataInfoWidget.draw(tboxConc.getText() , tbPoll.getText());						
				//myDataInfoWidget.draw(tboxConc.getText() , lbPollCode.getValue(index)tbPoll.getText());
			}
		});
		btnNewButton.setText("Display results");
		leftPanel.add(btnNewButton);
		btnNewButton.setWidth("");
		
		///////////////////////////////////////////////////////////////////////////////////////////////
		Label labelBlank = new Label("                                       ");
		leftPanel.add(labelBlank);
		Label labelBlank1 = new Label("                                      ");
		leftPanel.add(labelBlank1);
		Label labelDiv = new Label("====================================");
		leftPanel.add(labelDiv);
		Label labelTitle2 = new Label("2. Display distribution of a selected pollutant");
		Label labelTitle2a = new Label("in the atmosphere, IDW interpolation");
		leftPanel.add(labelTitle2);
		leftPanel.add(labelTitle2a);
				
		Label labelPoll1 = new Label("Select pollutant");
		
		leftPanel.add(labelPoll1);
		// Add ListBox for pollutionCode user input
		lbPollCode1 = new ListBox();
		lbPollCode1.setTitle("Select pollutant");
		lbPollCode1.addItem("BENZENE");
		lbPollCode1.addItem("CO");
		lbPollCode1.addItem("CO2 in EPER");
		lbPollCode1.addItem("FLUORINE AND INORGANIC COMPOUNDS");
		lbPollCode1.addItem("NMVOC");
		lbPollCode1.addItem("NOX");
		lbPollCode1.addItem("PFCS");
		lbPollCode1.addItem("PM10");
		lbPollCode1.addItem("SOX");
		lbPollCode1.addItem("CHLORINE AND INORGANIC COMPOUNDS");
		lbPollCode1.addItem("NH3");
		lbPollCode1.addItem("HCN");
		lbPollCode1.addItem("TETRACHLOROMETHANE (TCM)");
		lbPollCode1.addItem("N2O");
		lbPollCode1.addItem("CH4");
		lbPollCode1.addItem("TRICHLOROMETHANE");
		lbPollCode1.addItem("HFCS");
		lbPollCode1.addItem("PCDD+PCDF (DIOXINS+FURANS)");
		lbPollCode1.addItem("TRICHLOROETHYLENE (TRI)");
		lbPollCode1.addItem("TETRACHLOROETHYLENE (PER)");
		lbPollCode1.addItem("SF6");
		lbPollCode1.addItem("TRICHLOROETHANE-1,1,1 (TCE)");
		lbPollCode1.addItem("HEXACHLOROBENZENE (HCB)");
		lbPollCode1.addItem("TRICHLOROBENZENES (TCB)");
		lbPollCode1.addItem("CO2");
		lbPollCode1.addItem("POLYCYCLIC AROMATIC HYDROCARBONS");
		lbPollCode1.addItem("DEHP");
		lbPollCode1.addItem("POLYCHLORINATED BIPHENYLS (PCBS)");
		lbPollCode1.addItem("HCFCS");
		lbPollCode1.addItem("NAPHTHALENE");
		lbPollCode1.addItem("PENTACHLOROBENZENE");
		lbPollCode1.addItem("CFCS");
		lbPollCode1.addItem("CO2 EXCL BIOMASS");
		lbPollCode1.addItem("ANTHRACENE");
		lbPollCode1.addItem("HALONS");
		lbPollCode1.addItem("TETRACHLOROETHANE-1,1,2,2");
		lbPollCode1.addItem("ETHYLENE OXIDE");
		lbPollCode1.addItem("OTHGAS");
		lbPollCode1.addItem("GRHGAS");
		
		
		
		lbPollCode1.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent arg0) {
				// Get index of the selected item in the list box
				int itemSelectedIndex1 = lbPollCode1.getSelectedIndex();
				// Get the
				itemSelectedValue1 = lbPollCode1.getValue(itemSelectedIndex1);
				//Window.alert(itemSelectedValue);
				
			}
			
		});
		
		leftPanel.add(lbPollCode1);
		
		
		Label labelYear1 = new Label("Select reporting year");
		
		leftPanel.add(labelYear1);
		lbYear1 = new ListBox();
		lbYear1.setTitle("Select reporting year");
		lbYear1.addItem("2001");
		lbYear1.addItem("2004");
		lbYear1.addItem("2007");
		lbYear1.addItem("2008");
		lbYear1.addItem("2009");
		
		lbYear1.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent arg0) {
				int itemSelectedIndex1 = lbYear1.getSelectedIndex();
				itemSelectedYear1 = lbYear1.getValue(itemSelectedIndex1);
				//Window.alert(itemSelectedYear);
			}
			
		});
		leftPanel.add(lbYear1);
		
		
		// button to run surface retrieval from the database
		btnRpcRunSrf.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				myDataInfoWidget.drawSurface(getListBoxValue1(), getListBoxYear1());
			}	
		});
		btnRpcRunSrf.setText("Display results");
		btnRpcRunSrf.setWidth("");
		leftPanel.add(btnRpcRunSrf);
		
		// !!!!!!!!!!!!!!!!!!button to run interpolation
		/*btnRpcCalcSrfc.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				myDataInfoWidget.drawSurface(getListBoxValue(), getListBoxYear());
			}	
		});
		btnRpcCalcSrfc.setText("Interpolate");
		btnRpcCalcSrfc.setWidth("1500");
		leftPanel.add(btnRpcCalcSrfc);*/
		///////////////////////////////
		
		//Add mapWidget to the rightPanel	
		rightPanel.add(mapWidget);
		rightPanel.setSize("1000px", "600px");
				
		/**Add HTML widget to display info about a WMS feature
		final HTMLPanel myHtmlPanel = new HTMLPanel("New HTML");
		leftPanel.add(myHtmlPanel);
		mapWidget.setDisplayPanel(myHtmlPanel);*/
		
		//Begin
		VerticalPanel vp = new VerticalPanel();
		vp.setWidth("100%");
		vp.add(myDataInfoWidget);
		vp.setCellHorizontalAlignment(myDataInfoWidget, HorizontalPanel.ALIGN_CENTER);
				
		//temporarily checked
		myScrollPanel.add(vp);
		boolean alwaysShow = true;
		myScrollPanel.setAlwaysShowScrollBars(alwaysShow);
		myScrollPanel.setSize("300px","300px");
		myScrollPanel.scrollToBottom();
		leftPanel.add(myScrollPanel);
			
		//end 
		
		//Add leftPanel and rightPanel to the mainPanel;
		mainPanel.add(leftPanel);
		mainPanel.add(rightPanel);
		RootPanel.get("pollution-container").add(mainPanel);
		
	}
	
	public String getListBoxValue(){
		return itemSelectedValue;
	}
	
	public String getListBoxYear(){
		return itemSelectedYear;
	}
	
	public String getListBoxValue1(){
		return itemSelectedValue1;
	}
	
	public String getListBoxYear1(){
		return itemSelectedYear1;
	}
		
		
	
}


















