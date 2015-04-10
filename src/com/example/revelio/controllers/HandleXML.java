
package com.example.revelio.controllers;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.example.revelio.models.EventbrideEvents;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class HandleXML {
   private String event_title = "";
   private String latitude = "";
   private String longitude = "";
   private String event_details = "";
   private String urlString = null;
   private String start_date= null;
   private String start_time = "";
   private String end_time = "";
   private String venue = "";
   ArrayList<EventbrideEvents> ebevents = new ArrayList<EventbrideEvents>();
   
   
   private XmlPullParserFactory xmlFactoryObject;
   public volatile boolean parsingComplete = true;
   public HandleXML(String url){
      this.urlString = url;
   }
   public String getevent_title(){
      return event_title;
   }

   public String getevent_details(){
	      return event_details;
	   }

   public ArrayList<EventbrideEvents> parseXMLAndStoreIt(XmlPullParser myParser) {
	  ArrayList<EventbrideEvents> ebevents = new ArrayList<EventbrideEvents>();;
      int event;
      String text=null;
      try {
         event = myParser.getEventType();
         System.out.println(event);
         EventbrideEvents event1 = null;

         while (event != XmlPullParser.END_DOCUMENT) {
        	 
            String name=myParser.getName();
            
            switch (event){
               case XmlPullParser.START_TAG:
            	   if((myParser.getName()).equals("event")){
             		  event1 = new EventbrideEvents();
             		  System.out.println("Obj created ");
             	  } 
            	   name = myParser.getName();
            	   
               break;
               case XmlPullParser.TEXT:
               text = myParser.getText();
               break;

               case XmlPullParser.END_TAG:
            	  if((myParser.getName()).equals("event")){
            		  ebevents.add(event1);
            		  System.out.println("Obj added to the list "+ myParser.getName());
            	  } 
            	  else if(name.equals("title")){
            		  System.out.println(text);
                	  event1.setEvent_name(text);  
                  }
                  else if(name.equals("description")){
                	  if (event_details.equals("")){
                		  event1.setEvent_desc(text);
                	  }
                  }
                  else if(name.equals("id")){
                	     event1.setEvent_id(text);	  
                   }
                  else if(name.equals("url")){
                	  event1.setEvent_url(text);
                  }
                  else if(name.equals("start_date")){
                	  event1.setStart_time(text);
                  }
                  else if(name.equals("latitude")){
                	  event1.setLatitude(text);
                   }
                  else if(name.equals("longitude")){
                	  event1.setLongitude(text);
                   }
                  else{
                  }
                  break;
                  }
                  event = myParser.next(); 
              }
                 parsingComplete = false;
                 return ebevents;
                 
      } catch (Exception e) {
         e.printStackTrace();
         return ebevents;
      }

   }
   public void fetchXML(){
      Thread thread = new Thread(new Runnable(){
    	  
         @Override
         public void run() {
            try {
               URL url = new URL(urlString);
               HttpURLConnection conn = (HttpURLConnection) 
               url.openConnection();
                  conn.setReadTimeout(10000 /* milliseconds */);
                  conn.setConnectTimeout(15000 /* milliseconds */);
                  conn.setRequestMethod("GET");
                  conn.setDoInput(true);
                  conn.connect();
            InputStream stream = conn.getInputStream();

            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myparser = xmlFactoryObject.newPullParser();

            myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
            myparser.setInput(stream, null);
            
            ebevents = parseXMLAndStoreIt(myparser);
            
            System.out.println(ebevents.size());

            stream.close();
            } catch (Exception e) {
               e.printStackTrace();
            }
        }
    });

    thread.start(); 


   }

}

