package com.example.revelio.controllers;

import com.example.revelio.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
//import android.widget.TextView;

public class MainActivity extends Activity {
	//Thread t1 = null;
   @SuppressWarnings("deprecation")
@Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      ImageView imageview1 = (ImageView)findViewById(R.id.imageView1);
      
      Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.layout.fade);
           
      imageview1.startAnimation(animation2);
      try {
		Thread.sleep(2000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }
      public void newUserActivity(View view)
      {
    	  
     Intent newintent = new Intent(this, CameraActivity.class);
      startActivity(newintent);
      }

}