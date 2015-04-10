package com.example.revelio.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.revelio.R;
import com.example.revelio.models.Event;
import com.example.revelio.models.EventbrideEvents;
import com.example.revelio.models.YelpModel;

public class CameraActivity extends ActionBarActivity{
	  private static SensorManager sensorService;
	  private Sensor sensor;
	  double latitude = 0.0;
	  double longitude = 0.0;

	  static int building_no = 2;
	Bundle savedInstanceState;
	static int start_point = 0;
	public static int SOURCE_ID = 2;
	private Camera mCamera;
    private CameraPreview mPreview;
    public ArrayList<Event> eventList = new ArrayList<Event>();
    private static final String TAG_SUCCESS = "success";
	private static final String TAG_EVENTS = "events";
	private static final String TAG_EVENT_ID = "event_id";
	private static final String TAG_EVENT_NAME = "event_name";
	private static final String HOST_IP_ADD = "172.29.19.254";

	public static boolean flag=false;
	
   private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 3; // in Meters
   private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
   protected LocationManager locationManager;
   private String provider;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.savedInstanceState=savedInstanceState;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_activity);

		loadfragment(savedInstanceState);
	       LoadGPS();
	       LoadSensor();
	       Load();
	}
	public void loadfragment(Bundle savedInstanceState){
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment(CameraActivity.this)).commit();
		}
	}
	
	public void LoadSensor(){
		sensorService = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    sensor = sensorService.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	    //sensor = sensorService.getOrientation(R, values);
	    if (sensor != null) {
	      sensorService.registerListener(mySensorEventListener, sensor,
	          SensorManager.SENSOR_DELAY_NORMAL);
	      Log.i("Compass MainActivity", "Registerered for ORIENTATION Sensor");

	    } else {
	      Log.e("Compass MainActivity", "Registerered for ORIENTATION Sensor");
	      Toast.makeText(this, "ORIENTATION Sensor not found",
	          Toast.LENGTH_LONG).show();
	}
	    
	}
	
	
	private void LoadGPS(){
	    // Get the location manager
	    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    boolean enabled = locationManager
	            .isProviderEnabled(LocationManager.GPS_PROVIDER);

	          // check if enabled and if not send user to the GSP settings
	          // Better solution would be to display a dialog and suggesting to 
	          // go to the settings
	          if (!enabled) {
	            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	            startActivity(intent);
	          }
	    // Define the criteria how to select the locatioin provider -> use
	    // default
	    Criteria criteria = new Criteria();
	    provider = locationManager.getBestProvider(criteria, false);
	    //Location location = locationManager.getLastKnownLocation(provider);
	    locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 
                MINIMUM_TIME_BETWEEN_UPDATES, 
                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                new MyLocationListener()
        );
	    showCurrentLocation();
	    // Initialize the location fields
	    /*if (location != null) {
	      System.out.println("Provider " + provider + " has been selected.");
	      locationManager.requestLocationUpdates(provider, 400, 1, this);
	      onLocationChanged(location);
	    } else {
	      //latituteField.setText("Location not available");
	      //longitudeField.setText("Location not available");
	    }*/
	    

	}
	
	 protected void showCurrentLocation() {

	        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

	        if (location != null) {
	            String message = String.format(
	                    "Current Location \n Longitude: %1$s \n Latitude: %2$s",
	                    location.getLongitude(), location.getLatitude()
	            );
	            System.out.println("long"+location.getLongitude()+"lat"+location.getLatitude());
	            Toast.makeText(CameraActivity.this, message,
	                    Toast.LENGTH_LONG).show();
	        }	

	    }  

	private void Load() {
		 mCamera = getCameraInstance();

	        // Create our Preview view and set it as the content of our activity.
	       mPreview = new CameraPreview(this, mCamera);//new CameraPreview(this, mCamera);
	        FrameLayout preview = (FrameLayout) findViewById(R.id.container);
	        
	        preview.addView(mPreview);

		FrameLayout alParent;
        if (mCamera != null){
            alParent = new FrameLayout(this);
               mPreview = new CameraPreview(this,mCamera);
            alParent.addView(mPreview);
     		
        }
        else {
           Toast toast = Toast.makeText(getApplicationContext(),
              "Unable to find camera. Closing.", Toast.LENGTH_SHORT);
           toast.show();
          // finish();
        }
	}
	public static Camera getCameraInstance(){
        Camera c= null;
        try {
        	
            c = Camera.open(); // attempt to get a Camera instance
           // return c;
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        	return null;
        }
        return c; // returns null if camera is unavailable
    }



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	

	/**
	 * A placeholder fragment containing a simple view.
	 */
	@SuppressLint("ValidFragment")
	public static class PlaceholderFragment extends Fragment {
		public ArrayList<Event> eventList = new ArrayList<Event>();
		Context context ;
		String link_url = "";
		@SuppressLint("ValidFragment")

		public PlaceholderFragment(Context c) {
			context = c;
		}

		@Override
		public View onCreateView(final LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			//LoadListView(rootView,inflater);
			MainList(rootView);
			Button btn1=(Button)rootView.findViewById(R.id.btn1);
			btn1.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					SOURCE_ID = 2;
					LoadListView(rootView,inflater);
					
				}
			});
			Button btn2=(Button)rootView.findViewById(R.id.btn2);
			btn2.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					SOURCE_ID = 3;
					LoadListView(rootView,inflater);
					
					
				}
			});
			Button btn3=(Button)rootView.findViewById(R.id.btn3);
			btn3.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					MainList(rootView);
					
				}
			});
			
			Button btn4=(Button)rootView.findViewById(R.id.webbackbutton);
			btn4.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					LoadListView(rootView,inflater);
					
				}
			});
			
			Button previousList=(Button)rootView.findViewById(R.id.btnCreate);
			//previousList.setTextColor(Color.WHITE);
			previousList.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					OpenPreviousList(rootView);
					
				}
			});
			
			Button rsvp=(Button)rootView.findViewById(R.id.btnRsvp);
			//rsvp.setTextColor(Color.WHITE);
			rsvp.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(SOURCE_ID == 2 || SOURCE_ID == 3){
						
						WebView view = (WebView) rootView.findViewById(R.id.webView);
						 view.getSettings().setJavaScriptEnabled(true);
						 view.loadUrl(link_url);
						 Button btn4=(Button)rootView.findViewById(R.id.webbackbutton);
						 btn4.bringToFront();
						 
						 OpenWebView(rootView);
					}
					else{
						OpenPreviousList(rootView);
					}	
					
				}
			});
			
			return rootView;
			
		}
		public void OpenWebView(final View rootView)
		{
			LinearLayout relativeLayoutControls11 = (LinearLayout)rootView.findViewById(R.id.relLay3);
			relativeLayoutControls11.setVisibility(View.INVISIBLE);
			LinearLayout relativeLayoutControls1 = (LinearLayout)rootView.findViewById(R.id.relLay1);
			relativeLayoutControls1.setVisibility(View.INVISIBLE);	
			
			LinearLayout relativeLayoutControls = (LinearLayout)rootView.findViewById(R.id.relLay2);
	        relativeLayoutControls.setVisibility(View.INVISIBLE);
	        LinearLayout relativeLayoutControls4 = (LinearLayout)rootView.findViewById(R.id.relLay4);
	        relativeLayoutControls4.setVisibility(View.VISIBLE);
		}
		
		
		public void OpenPreviousList(final View rootView)
		{
			LinearLayout relativeLayoutControls11 = (LinearLayout)rootView.findViewById(R.id.relLay3);
			relativeLayoutControls11.setVisibility(View.INVISIBLE);
			LinearLayout relativeLayoutControls1 = (LinearLayout)rootView.findViewById(R.id.relLay1);
			relativeLayoutControls1.setVisibility(View.VISIBLE);
	    	
			LinearLayout relativeLayoutControls = (LinearLayout)rootView.findViewById(R.id.relLay2);
	        relativeLayoutControls.setVisibility(View.INVISIBLE);
	        LinearLayout relativeLayoutControls4 = (LinearLayout)rootView.findViewById(R.id.relLay4);
	        relativeLayoutControls4.setVisibility(View.INVISIBLE);
		}
		
		public void MainList(final View rootView)
		{
			LinearLayout relativeLayoutControls1 = (LinearLayout)rootView.findViewById(R.id.relLay3);
			relativeLayoutControls1.setVisibility(View.VISIBLE);
			LinearLayout relativeLayoutControls2= (LinearLayout)rootView.findViewById(R.id.relLay1);
			relativeLayoutControls2.setVisibility(View.INVISIBLE);
			LinearLayout relativeLayoutControls = (LinearLayout)rootView.findViewById(R.id.relLay2);
	        relativeLayoutControls.setVisibility(View.INVISIBLE);
	        LinearLayout relativeLayoutControls4 = (LinearLayout)rootView.findViewById(R.id.relLay4);
	        relativeLayoutControls4.setVisibility(View.INVISIBLE);
		}
		
		private void LoadListView(final View rootView,final LayoutInflater inflater){
			OpenPreviousList(rootView);
	    	final CameraListAdapter adapter=new CameraListAdapter(inflater);
	    	//final CameraListEventAdapter adapterEvents=new CameraListEventAdapter(inflater);
	    	final ListView lv = (ListView)rootView.findViewById(android.R.id.list);
	    	TextView label=(TextView)rootView.findViewById(R.id.tvlabel);
	    	label.setText("Event Details:");
	    	label.setTextColor(Color.DKGRAY);
	    	
	    	// web server data code
	    	System.out.println("connecting database");
			//final ConnectionMySQL myconn = new ConnectionMySQL();
			
	    	
	    	// eventbrite object code
			
	    	final HandleXML obj1 ;
		    obj1 = new HandleXML("https://www.eventbrite.com/xml/event_search?app_key=B5KBKT5IZNBOOFXF2G&within=1&longitude=-73.9865&latitude=40.6944");
		    obj1.fetchXML();
		    while(obj1.parsingComplete);
		    System.out.println("fetching event brite done");
		    
		    //  yelp object code
		    String yelpResponse = "";
		    ArrayList<YelpModel> yelplist = new ArrayList<YelpModel>();
		    String consumerKey = "UBSOyMBBZ4NDEgdi66vW0g";
			String consumerSecret = "6Q-h1yT33TqPH5Ok1AtBKYXc6Aw";
			String token = "kYXUMwOldYuyp0FQrxWdLkVr3zLP77WJ";
			String tokenSecret = "YsxJePi-fEJw2OHH9uPg7rKjG2E";
			final Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);
			yelp.search();
			while(yelp.parsingComplete);
			System.out.println("fetching yelp done");
		     
			//load eventbrite data to list
			if(building_no != 0){
			if(SOURCE_ID == 2){
				List<String> values=new ArrayList<String>();
//				 myconn.fetchcurrentEvents1(building_no);
//		    	 
//		    	 for(Iterator<Event> i = myconn.eventList.iterator();i.hasNext();){
//		    		 Event e =i.next();
//		    		 values.add(e.getEvent_name());
//		    	 }
//				
				obj1.ebevents.get(0).setBuilding_no(4);
				obj1.ebevents.get(1).setBuilding_no(4);
				obj1.ebevents.get(2).setBuilding_no(2);
				obj1.ebevents.get(3).setBuilding_no(1);
				obj1.ebevents.get(4).setBuilding_no(1);
				obj1.ebevents.get(5).setBuilding_no(2);
				obj1.ebevents.get(6).setBuilding_no(1);
				obj1.ebevents.get(7).setBuilding_no(1);
				obj1.ebevents.get(8).setBuilding_no(3);
				obj1.ebevents.get(9).setBuilding_no(3);


				start_point = values.size();
				    System.out.println(start_point);
				    for(Iterator<EventbrideEvents> i = obj1.ebevents.iterator();i.hasNext();){
				    EventbrideEvents e =i.next();
				    System.out.println(e.getBuilding_no());
				    if(e.getBuilding_no()==building_no){
				    System.out.println(e.getBuilding_no());
				        values.add(e.getEvent_name());
				    }
				    }
				    
				    adapter.setData(values);
		    }
		  //load yelp data to list
		    if(SOURCE_ID == 3){
		    	List<String> values=new ArrayList<String>();
		    	yelpResponse = yelp.yelpResponse;
		    	yelplist = yelp.getYelp(yelpResponse);
		    	
		    	for(Iterator<YelpModel> i = yelplist.iterator();i.hasNext();){
		    		YelpModel e =i.next();
		    		values.add(e.getName());
		    	 }
		    	adapter.setData(values);
		    }
		    
		    lv.setAdapter(adapter);
		    lv.setFastScrollEnabled(true);
		    lv.setHovered(true);
		    lv.setFadingEdgeLength(10);
			}
		    
		    lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){  

				@Override
				public void onItemClick(AdapterView parentView, View childView, int position,
						long id) {
					// TODO Auto-generated method stub
					if(SOURCE_ID == 2){
						if(position < start_point){
//							Event evnt = new Event();
//							evnt = myconn.eventList.get(position);
//							TextView tv=(TextView)rootView.findViewById(R.id.tvedit);
//							tv.setText(evnt.getEvent_name()+"\n\n"
//										+evnt.getEvent_desc()+"\n\nDate :"+evnt.getEvent_date()+"\nTime :"+evnt.getStart_time());
//							tv.setTextColor(Color.WHITE);
//							
//						    LinearLayout relativeLayoutControls1 = (LinearLayout)rootView.findViewById(R.id.relLay1);
//							relativeLayoutControls1.setVisibility(View.GONE);//.VISIBLE=0;
//							LinearLayout relativeLayoutControls = (LinearLayout)rootView.findViewById(R.id.relLay2);
//						    relativeLayoutControls.setVisibility(View.VISIBLE);
						}
						else{
							EventbrideEvents evnt  = new EventbrideEvents();
							System.out.println((position-start_point));
							evnt = obj1.ebevents.get((position-start_point));
							TextView tv=(TextView)rootView.findViewById(R.id.tvedit);
							tv.setText(evnt.getEvent_name()+"\n"
										+evnt.getEvent_desc()+"\n\nTime :"+evnt.getStart_time());
							tv.setTextColor(Color.WHITE);
						    LinearLayout relativeLayoutControls1 = (LinearLayout)rootView.findViewById(R.id.relLay1);
							relativeLayoutControls1.setVisibility(View.GONE);//.VISIBLE=0;
							link_url = evnt.getEvent_url();
							LinearLayout relativeLayoutControls = (LinearLayout)rootView.findViewById(R.id.relLay2);
						    relativeLayoutControls.setVisibility(View.VISIBLE);
						}
					} 
					if(SOURCE_ID == 3){
							YelpModel ylp = new YelpModel();
							ylp = yelp.yelplist.get(position);
							TextView tv=(TextView)rootView.findViewById(R.id.tvedit);
							tv.setText(ylp.getName()+"\n"+ylp.getDisplay_address()+"\n'\n" +
									"Discount Info :\n"+ylp.getTitle()+"\nPhone Number :"+ylp.getPhone_number()+
									"\n\nComments :\n"+ylp.getSnippet_text());
							
					    	LinearLayout relativeLayoutControls1 = (LinearLayout)rootView.findViewById(R.id.relLay1);
							relativeLayoutControls1.setVisibility(View.GONE);//.VISIBLE=0;
					    	link_url = ylp.getMobile_url();
							LinearLayout relativeLayoutControls = (LinearLayout)rootView.findViewById(R.id.relLay2);
					        relativeLayoutControls.setVisibility(View.VISIBLE);
							
					}
				}}); 
	    }

	}

	private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
         // showCurrentLocation();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
//         int temp_building_no = 0;
//         if(latitude> 40.693431 && latitude <40.693805 && longitude> -73.986239 && longitude < -73.985204){
//         temp_building_no = 1;
//         }
//         else if(latitude> 40.694265 && latitude < 40.69444&&longitude > -73.986009 && longitude < -73.98529){
//         temp_building_no = 2;
//         }
//         else if(latitude> 40.693842 && latitude < 40.694188 &&longitude >-73.985252 && longitude < -73.985049){
//         temp_building_no = 3;
//         }
//         else if(latitude> 40.683887 && latitude < 40.694448 &&longitude > -73.986272 && longitude < -73.986079){
//         temp_building_no = 4;
//         }
//         else{
//         temp_building_no = 0;
//         }
//         if(temp_building_no != building_no){
//         Toast.makeText(CameraActivity.this, "Out of range of current Building. Loading data again", Toast.LENGTH_SHORT).show();
//         building_no = temp_building_no;
//         if(building_no != 0)
//         loadfragment(savedInstanceState);
//         Load();
//         }
//         String message = String.format(
//                    "New Location \n Longitude: %1$s \n Latitude: %2$s",
//                    location.getLongitude(), location.getLatitude()
//            );
//            Toast.makeText(CameraActivity.this, message,Toast.LENGTH_LONG).show();
        }

        public void onStatusChanged(String s, int i, Bundle b) {
            Toast.makeText(CameraActivity.this, "Provider status changed",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderDisabled(String s) {
            Toast.makeText(CameraActivity.this,
                    "Provider disabled by the user. GPS turned off",
                    Toast.LENGTH_LONG).show();

        }

        public void onProviderEnabled(String s) {
            Toast.makeText(CameraActivity.this,
                    "Provider enabled by the user. GPS turned on",
                    Toast.LENGTH_LONG).show();
        }


    }
private SensorEventListener mySensorEventListener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
          // angle between the magnetic north direction
          // 0=North, 90=East, 180=South, 270=West
          float azimuth = event.values[0];
          //boolean flag = true;
          int temp_building_no = 0;
          if((((0.0f  < azimuth) && (azimuth < 55.0f) || (320.0f <
azimuth ) &&(azimuth < 360.0f)))&&(latitude> 40.694265 && latitude <
40.69444&& longitude > -73.986009 && longitude < -73.98529))
          {
         temp_building_no = 2;
          }
          else if(((255.0f  < azimuth) && (azimuth <
310.0f))&&(latitude> 40.683887 && latitude < 40.694448 && longitude >
-73.986272 && longitude < -73.986079))
          {
         temp_building_no = 4;
          }
          else if(((140.0f  < azimuth) && (azimuth <
230.0f))&&(latitude> 40.693431 && latitude <40.693805 && longitude >
-73.986239 && longitude < -73.985204))
          {
         temp_building_no = 1;
          }
          else if(((80.0f  < azimuth) && (azimuth <
125.0f))&&(latitude> 40.693842 && latitude < 40.694188 && longitude
>-73.985252 && longitude < -73.985049))
          {
         temp_building_no = 3;
          }
          else{
         temp_building_no = 0;
        }
        if(temp_building_no != building_no){
        Toast.makeText(CameraActivity.this, "Out of range of current Building.", Toast.LENGTH_SHORT).show();
        building_no = temp_building_no;
        if(building_no != 0)
        loadfragment(savedInstanceState);
        Load();
        }
          }
        //  invalidate();



      };
}






