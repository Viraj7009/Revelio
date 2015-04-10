package com.example.revelio.controllers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.example.revelio.models.YelpModel;

/**
 * Example for accessing the Yelp API.
 */
public class Yelp {
	  
	  OAuthService service;
	  Token accessToken;
	  String term = "restaurant";
	  String yelpResponse = "";
	  double latitude = 40.6944;
	  double longitude = -73.9865;
	  Boolean parsingComplete = true;
	  ArrayList<YelpModel> yelplist = new ArrayList<YelpModel>();
  /**
   * Setup the Yelp API OAuth credentials.
   *
   * OAuth credentials are available from the developer site, under Manage API access (version 2 API).
   *
   * @param consumerKey Consumer key
   * @param consumerSecret Consumer secret
   * @param token Token
   * @param tokenSecret Token secret
   */
  
  public Yelp(String consumerKey, String consumerSecret, String token, String tokenSecret) {
    this.service = new ServiceBuilder().provider(YelpApi2.class).apiKey(consumerKey).apiSecret(consumerSecret).build();
    this.accessToken = new Token(token, tokenSecret);
  }

  /**
   * Search with term and location.
   *
   * @param term Search term
   * @param latitude Latitude
   * @param longitude Longitude
   * @return JSON string response
   */
  public void search() {
	  Thread thread = new Thread(new Runnable(){
	        @Override
	         public void run() {
	            try {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
    request.addQuerystringParameter("term", term);
    request.addQuerystringParameter("ll", latitude + "," + longitude);
    request.addQuerystringParameter("radius_filter","1000");
    request.addQuerystringParameter("deals_filter","true");
    service.signRequest(accessToken, request);
    Response response = request.send();
    yelpResponse = response.getBody();
    parsingComplete = false;
	  } catch (Exception e) {
          e.printStackTrace();
       }
   }
});

thread.start(); 
  }
	public ArrayList<YelpModel> getYelp(String yelpResponse){
		try {
            JSONObject json = new JSONObject(yelpResponse);
            JSONArray businesses;
            businesses = json.getJSONArray("businesses");
            System.out.println(businesses.length());
            for (int i = 0; i < businesses.length(); i++) {
                    JSONObject business = businesses.getJSONObject(i);
                    YelpModel yelpdata = new YelpModel();
                    yelpdata.setName(business.getString("name"));
                    yelpdata.setDisplay_address(business.getJSONObject("location").getString("display_address"));
                    yelpdata.setTitle(business.getJSONArray("deals").getJSONObject(0).getString("title"));
                    yelpdata.setMobile_url(business.getString("mobile_url"));
                    yelpdata.setPhone_number(business.getString("display_phone"));    
                    yelpdata.setSnippet_text(business.getString("snippet_text")) ; 
                    yelplist.add(yelpdata);
            }
            return yelplist;
		}
		catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
	}
  // CLI
}
