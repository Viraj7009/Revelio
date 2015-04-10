package com.example.revelio.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.revelio.models.User;

public class UserDatabaseHelper extends SQLiteOpenHelper {
		
	    private static final int DATABASE_VERSION = 1;
	    private static final String DATABASE_NAME= "revelio_user";
	    
        public UserDatabaseHelper(Context context) {
        	super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }
        
        public void onCreate(SQLiteDatabase db) {
        	        // create user table locally
        	       db.execSQL("create table revelio_user (net_id varchar primary key ," +
        	    		    "user_name varchar not null," +
        	        		"password varchar not null," +
        	        		"email_id varchar ," +
        	        		"ph_number varchar," +
        	        		"create_date current_timestamp)");
        }
        
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        	  		// Drop older table if exists
        	  		db.execSQL("DROP TABLE IF EXISTS revelio_user");

        	  		// create tables again
        	  		onCreate(db);
        }
          
        public void insertUserInfo(User user) {
	          ContentValues cv = new ContentValues();
	          
	          cv.put("net_id",user.getNet_id());
	          cv.put("user_name", user.getUser_name());
	          cv.put("password", user.getPassword());
	          cv.put("email_id", user.getEmail_id());
	          cv.put("ph_number", user.getPh_number());
	          // return id of new trip
	          getWritableDatabase().insert("revelio_user", null, cv);
        }

          
        public User getUserDetails() {
        	  User user = new User();
        	  SQLiteDatabase db = this.getReadableDatabase();
        	  Cursor cursor = db.rawQuery("select * from revelio_user  ", null);

        	  if(cursor.getCount() != 0){
        		      cursor.moveToFirst();
        		      user.setNet_id(cursor.getString(0));
        		      user.setUser_name(cursor.getString(1));
        		      user.setPassword(cursor.getString(2));
        		      user.setEmail_id(cursor.getString(3));
        		      user.setPh_number(cursor.getInt(4));
        		}
        	  return user;
        }
}
