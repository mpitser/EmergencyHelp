package com.example.emergencyhelp;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Toast;

public class FullscreenActivity extends Activity {
	
	private static String hardcoded_phone = "7047807895";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_fullscreen);

		final View contentView = findViewById(R.id.main_layout);

		contentView.setOnLongClickListener(new OnLongClickListener(){
			@Override
			public boolean onLongClick(View v) {               
               sendHelpMessage();
               return false;
			}
		});
	}
	

	protected void sendHelpMessage() {
	
		/* get sending info (friend name, phone num) */
		//String phoneNum = ... (get phone from contacts? settings?)
		String phoneNum = hardcoded_phone;
			
		/* get GPS coordinates */
		Coordinate coords = getGPScoords();
		if (coords == null){
			//Toast.makeText(this, "coords are not given, no help message sent", Toast.LENGTH_LONG).show();
		} else {
			//Toast.makeText(this, "coords are: "+coords.latitude + " " + coords.longitude, Toast.LENGTH_LONG).show();
			
			String coordsLink = "https://maps.google.com/maps?q=" + coords.latitude+ "," + coords.longitude;
			
			SmsManager sm = SmsManager.getDefault();
			sm.sendTextMessage(phoneNum, null, "Help! Please call 911 and tell them where I am! My GPS coordinates are: " + coordsLink, null, null); // first param should be phone
			
			/* tell user of success/failure */
			Toast.makeText(this, "Help Message Sent!", Toast.LENGTH_LONG).show();
			Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
			 // Vibrate for 500 milliseconds
			 v.vibrate(500);
		}
	}

	public static class Coordinate {
		double longitude;
		double latitude;
		
		public Coordinate(double lon, double lat){
			longitude = lon;
			latitude = lat;
		}
		
	}
	
	private Coordinate getGPScoords() {	   

		LocationManager locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

		Location location;
		Coordinate coords = null;
		
		boolean network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if(network_enabled){

		   location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		   if(location!=null){
			   coords = new Coordinate(location.getLongitude(), location.getLatitude());
		   }                
		} else {
			Toast.makeText(this, "network not enabled, no gps coordinates found", Toast.LENGTH_LONG).show();
		}
		return coords;  
		}

}
