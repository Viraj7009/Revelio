
package com.example.revelio.controllers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.revelio.models.Event;

public class ConnectionMySQL {
	 ArrayList<Event> eventList = new ArrayList<Event>();
	private ProgressDialog pDialog;
	boolean status = false;		
	boolean flag = false;
	Context context_temp;
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_EVENTS = "events";
	private static final String TAG_EVENT_ID = "event_id";
	private static final String TAG_EVENT_NAME = "event_name";
	private static final String HOST_IP_ADD = "172.26.32.165";
	
	public ConnectionMySQL(/*Context context*/) {
		// TODO Auto-generated constructor stub
		//context_temp = context;
	}
	
	//Verify whether the user is a registered in polytechnic campus
	
	public boolean userVerification(string net_id,string password){
		flag = false;
		new GetUserVerification().execute();
		return flag;
	}
	
	// check the reservation
	
	public boolean checkReservation(string net_id,int event_id){
		flag = false;
		new GetUserReservation().execute();
		return flag;
	}
	/// make a reservation
	/*
	 * CREATE TABLE rsvp_details
	   
	   r_id int auto_increment primary key ,
	   net_id int  ,
	   event_id int ,
	   create_date datetime,
	   CONSTRAINT member_FK1 FOREIGN KEY (net_id) REFERENCES user(net_id),
	   CONSTRAINT member_FK2 FOREIGN KEY (event_id) REFERENCES events(event_id)
	;
	 */
	public boolean userReservation(string net_id,int event_id){
		flag = false;
		new MakeUserReservation().execute();
		return flag;
	}
	
	// cancel a reservation
	public void cancelReservation(string net_id,int event_id){
		try{
			Connection conn = DriverManager.getConnection("jdbc:mysql://172.29.35.160/revelio","root","root");
			Log.i("connectionObj","Connection Sucessful");
			String query = "delete from rsvp_details where net_id " + net_id
					+" event_id = " + event_id ;
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(query);
			stmt.close();
			conn.close();
		}catch(Exception e){
			Log.i("ConnectionMySQL","Error in database extraction");
		}
	}
	
	//Extract event data from the table for the current time stamp
	
	public ArrayList<Event> fetchcurrentEvents(int building_no,Date current_date,Time current_time){
		eventList.clear();
		new LoadAllCurrentEvents().execute();
		return eventList;
	}
	
	public void fetchcurrentEvents1(int building_no){
		eventList.clear();
		flag = false;
		final String bid = "" + building_no;
		System.out.println("Async task starting");
		LoadAllCurrentEvents lace = new LoadAllCurrentEvents();
		lace.execute(bid);
		while(!lace.fetchEventComplete);
		lace.cancel(true);
		System.out.println("Async task is finished");
	//	return eventList;
	}
	
	
	//Extract event data from the table for future
	
	public ArrayList<Event> fetchfutureEvents(int building_no,Date current_date,Date current_time){
		eventList.clear();
		new LoadAllCurrentEvents().execute();
		return eventList;
	}
	
		/************   ASYNC TASK FOR fetchcurrentEvents  *****************************************/
		
	class LoadAllCurrentEvents extends AsyncTask<String, String, String> {
		boolean fetchEventComplete = false;
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
//			super.onPreExecute();
//			pDialog = new ProgressDialog(context_temp); /*check if error */
//			pDialog.setMessage("Loading Events. Please wait...");
//			pDialog.setIndeterminate(false);
//			pDialog.setCancelable(false);
//			pDialog.show();
		}
		
		/**
		 * getting All events from url
		 * */
		@Override
		protected String doInBackground(String... args) {
			// Building Parameters
			try{
				String url_all_events = "http://"+HOST_IP_ADD+"/revelio_connect/getAllCurrentEvents.php";
				String bid = args[0];
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("bid",bid));
				JSONArray events = null;
				JSONParser jParser = new JSONParser();
				JSONObject json = jParser.makeHttpRequest(url_all_events,"GET", params);
				
				Log.i("LoadAllCurrentEvents async","Connection Sucessful getAllCurrentEvents.php async");
				
					// Checking for SUCCESS TAG
					int success = json.getInt(TAG_SUCCESS);
					if (success == 1) {
						// products found
						// Getting Array of Products
						events = json.getJSONArray(TAG_EVENTS);
							// looping through All Products
						for (int i = 0; i < events.length(); i++) {
							Event event = new Event();
							JSONObject c = events.getJSONObject(i);
							// Storing each json item in variable
							event.setEvent_id(c.getInt("event_id"));
							event.setEvent_name(c.getString("event_name"));
							event.setEvent_date(c.getString("event_date"));
							event.setStart_time(c.getString("start_time"));
							event.setEnd_time(c.getString("end_time"));
							event.setRsvp(c.getInt("rsvp"));
							event.setEvent_desc(c.getString("description"));
							System.out.println(c.getString("event_name"));
							eventList.add(event);
							
						}
						System.out.println("loop end");
						fetchEventComplete = true;
					} else {
						// no events found
						Toast.makeText(context_temp, "No events found", Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					System.out.println("value async :"+e);
					flag = true;
					e.printStackTrace();
				}
			return null;
		}
		

			/**
			 * After completing background task Dismiss the progress dialog
			 * **/
		@Override
		protected void onPostExecute(String file_url) {
			fetchEventComplete = true;
				// dismiss the dialog after getting all products
		//		pDialog.dismiss();
			
			}

		}
		/************   ASYNC TASK FOR fetchfutureEvents  *****************************************/
		
		class LoadAllFutureEvents extends AsyncTask<String, String, String> {
			
			/**
			 * Before starting background thread Show Progress Dialog
			 * */
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(context_temp); /*check if error */
				pDialog.setMessage("Loading products. Please wait...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
			}
			
			/**
			 * getting All events from url
			 * */
			@Override
			protected String doInBackground(String... args) {
				// Building Parameters
				try{
					String url_all_events = "http://"+HOST_IP_ADD+"/revelio_connect/getAllFutureEvents.php";
					
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					JSONArray events = null;
					JSONParser jParser = new JSONParser();
					JSONObject json = jParser.makeHttpRequest(url_all_events,"GET", params);
					
					Log.i("LoadAllFutureEvents async","Connection Sucessful getAllCurrentEvents.php async");
					
						// Checking for SUCCESS TAG
					int success = json.getInt(TAG_SUCCESS);
					if (success == 1) {
						// products found
						// Getting Array of Products
						events = json.getJSONArray(TAG_EVENTS);
							// looping through All Products
						for (int i = 0; i < events.length(); i++) {
							Event event = new Event();
							JSONObject c = events.getJSONObject(i);
							// Storing each json item in variable
							event.setEvent_id(c.getInt("event_id"));
							event.setEvent_name(c.getString("event_name"));
							event.setEvent_date(c.getString("event_date"));
							event.setStart_time(c.getString("start_time"));
							event.setEnd_time(c.getString("end_time"));
							event.setRsvp(c.getInt("rsvp"));
							event.setEvent_desc(c.getString("description"));
							eventList.add(event);
						}
					} else {
						// no events found
						Toast.makeText(context_temp, "No events found", Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					System.out.println("value async :"+e);
					e.printStackTrace();
				}
				return null;
			}
			

			/**
			 * After completing background task Dismiss the progress dialog
			 * **/
			@Override
			protected void onPostExecute(String file_url) {
				// dismiss the dialog after getting all products
				pDialog.dismiss();
			}

		}
		/************   ASYNC TASK FOR User verification  *****************************************/
		
		class GetUserVerification extends AsyncTask<String, String, String> {
			
			/**
			 * Before starting background thread Show Progress Dialog
			 * */
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(context_temp); /*check if error */
				pDialog.setMessage("Verifying User...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
			}
			
			/**
			 * getting All events from url
			 * */
			protected String doInBackground(String... args) {
				// Building Parameters
				try{
					String url_all_events = "http://"+HOST_IP_ADD+"/revelio_connect/getUserVerification.php";
					
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					JSONArray events = null;
					JSONParser jParser = new JSONParser();
					JSONObject json = jParser.makeHttpRequest(url_all_events,"GET", params);
					
					Log.i("User Verification async","Connection Sucessful getUserVerification.php async");
					
						// Checking for SUCCESS TAG
					int success = json.getInt(TAG_SUCCESS);
					if (success == 1) {
							flag = true;
					} else {
						// no events found
						Toast.makeText(context_temp, "Unauthorised User !!", Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					System.out.println("value async :"+e);
					e.printStackTrace();
				}
				return null;
			}

			/**
			 * After completing background task Dismiss the progress dialog
			 * **/
			protected void onPostExecute(String file_url) {
				// dismiss the dialog after getting all products
				pDialog.dismiss();
			}

		}
		/************   ASYNC TASK FOR User Reservation  *****************************************/
		
		class MakeUserReservation extends AsyncTask<String, String, String> {
			
			/**
			 * Before starting background thread Show Progress Dialog
			 * */
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(context_temp); /*check if error */
				pDialog.setMessage("Making User reservation...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
			}
			
			/**
			 * getting All events from url
			 * */
			protected String doInBackground(String... args) {
				// Building Parameters
				try{
					String url_all_events = "http://"+HOST_IP_ADD+"/revelio_connect/makeUserReservation.php";
					
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					JSONArray events = null;
					JSONParser jParser = new JSONParser();
					JSONObject json = jParser.makeHttpRequest(url_all_events,"GET", params);
					
					Log.i("User Reservation async","Connection Sucessful makeUserReservation.php async");
					
						// Checking for SUCCESS TAG
					int success = json.getInt(TAG_SUCCESS);
					if (success == 1) {
							flag = true;
					} else {
						// no events found
						Toast.makeText(context_temp, "Unable to make reservation. Try again later !!", Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					System.out.println("value async :"+e);
					e.printStackTrace();
				}
				return null;
			}

			/**
			 * After completing background task Dismiss the progress dialog
			 * **/
			protected void onPostExecute(String file_url) {
				// dismiss the dialog after getting all products
				pDialog.dismiss();
			}

		}
		
		/************   ASYNC TASK FOR  Get User Reservation  *****************************************/
		
		class GetUserReservation extends AsyncTask<String, String, String> {
			
			/**
			 * Before starting background thread Show Progress Dialog
			 * */
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(context_temp); /*check if error */
				pDialog.setMessage("Getting User Reservation...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
			}
			
			/**
			 * getting All events from url
			 * */
			protected String doInBackground(String... args) {
				// Building Parameters
				try{
					String url_all_events = "http://"+HOST_IP_ADD+"/revelio_connect/getUserReservation.php";
					
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					JSONArray events = null;
					JSONParser jParser = new JSONParser();
					JSONObject json = jParser.makeHttpRequest(url_all_events,"GET", params);
					
					Log.i("User Reservation async","Connection Sucessful getUserReservation.php async");
					
						// Checking for SUCCESS TAG
					int success = json.getInt(TAG_SUCCESS);
					if (success == 1) {
							flag = true;
					} else {
						// no events found
						Log.i("User Reservation async","User nor reserved getUserReservation.php async");
					}
				} catch (JSONException e) {
					System.out.println("value async :"+e);
					e.printStackTrace();
				}
				return null;
			}

			/**
			 * After completing background task Dismiss the progress dialog
			 * **/
			protected void onPostExecute(String file_url) {
				// dismiss the dialog after getting all products
				pDialog.dismiss();
			}

		}
	/************   ASYNC TASK FOR  Cancel User Reservation  *****************************************/
		
		class CancelUserReservation extends AsyncTask<String, String, String> {
			
			/**
			 * Before starting background thread Show Progress Dialog
			 * */
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(context_temp); /*check if error */
				pDialog.setMessage("Cancel User reservation...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
			}
			
			/**
			 * getting All events from url
			 * */
			protected String doInBackground(String... args) {
				// Building Parameters
				try{
					String url_all_events = "http://"+HOST_IP_ADD+"/revelio_connect/cancelUserReservation.php";
					
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					JSONArray events = null;
					JSONParser jParser = new JSONParser();
					JSONObject json = jParser.makeHttpRequest(url_all_events,"GET", params);
					
					Log.i("Cancel User Reservation async","Connection Sucessful cancelUserReservation.php async");
					
						// Checking for SUCCESS TAG
					int success = json.getInt(TAG_SUCCESS);
					if (success == 1) {
							flag = true;
					} else {
						// no events found
						Log.i("User Reservation async","Reservation cannot be canceled cancelUserReservation.php async");
					}
				} catch (JSONException e) {
					System.out.println("value async :"+e);
					e.printStackTrace();
				}
				return null;
			}

			/**
			 * After completing background task Dismiss the progress dialog
			 * **/
			protected void onPostExecute(String file_url) {
				// dismiss the dialog after getting all products
			//	pDialog.dismiss();
			}

		}

}
