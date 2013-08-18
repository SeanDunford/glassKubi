package com.example.glasskubi;

import java.io.IOException;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {

	public String aBaseUrl = "http://stage.kubi.me/pusherphp/?"; 
	public float x = 0.23f; 
	public float y = 0.23f;
//	public float sensorRange = (float) (9.8 * 2);
//	public float sensorBase = sensorRange/2;
//	public static int sensorCounter = 0;
	public int allowUpdate = 0;
	Handler mHandler = new Handler();
//	public Random aRand = new Random();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		Sensor accelerometer = manager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
//		Sensor compass = manager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
//		if(sensorRange < accelerometer.getMaximumRange())
//			sensorRange = accelerometer.getMaximumRange(); 
//		
		if(!manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)){

			// handle error

		}
		Button moveKubiBtn=(Button)findViewById(R.id.moveKubi_Btn);
		moveKubiBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				}
         });
		
		new Thread(new Runnable() {
	        @Override
	        public void run() {
	            // TODO Auto-generated method stub
	            while (true) {
	                try {
	                    Thread.sleep(1000);
	                    mHandler.post(new Runnable() {

	                        @Override
	                        public void run() {
	                            allowUpdate = 1;
	                        }
	                    });
	                    //allowUpdate = 1;
	                } catch (Exception e) {
	                    // TODO: handle exception
	                }
	            }
	        }
	    }).start();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		//The SensorRange value seems to be indeterministic and our robot 
		//wont support values greater that 1.0 this weird code will prevent overflow
				
		float tempY = event.values[1];
		float tempX = event.values[2];
		
//		if (tempX > sensorRange){
//			sensorRange = tempX; 
//			sensorBase = sensorRange/2; 
//		}
//		if (tempY > sensorRange){
//			sensorRange = tempY; 
//			sensorBase = sensorRange/2; 
//		}
//		x = ((sensorBase + tempX)/sensorRange); 
//		y = ((sensorBase + tempY)/sensorRange); 
//		x = tempX;
		x = (float) tempX;
		y = (float) tempY;
		
//		if (y > 1) { y = 1; }
//		if (y < 0) { y = 0; }
		
		String sensorVals = String.format("x is %f and y is %f", x, y); 
	    TextView sensorTxt = (TextView)findViewById(R.id.sensorValues_Txt);
	    sensorTxt.setText(sensorVals);
		if (allowUpdate == 1) {
			allowUpdate = 0;
			Kubi aKubi = new Kubi();
			//Toast.makeText(getApplicationContext(), String.format("x is %f and y is %f", x, y),Toast.LENGTH_SHORT).show(); 
			String aUrl = aBaseUrl+"x="+x+"&y="+y; 
			aKubi.execute(aUrl); 
			Log.d("aKubi","execute");
		}
	}
}

class Kubi extends AsyncTask<String, Integer, Void>{

    private Exception exception;

    protected void onPostExecute() {
        // TODO: check this.exception 
        // TODO: do something with the feed
    }
	private void postCoordsToKubi(String aUrl) {
	    // Create a new HttpClient and Post Header
		Log.d("Tag", "enterPostCoordsToKubi"); 
		HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(aUrl);

	    try {
	        // Add your data
	       // List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	       // nameValuePairs.add(new BasicNameValuePair("id", "12345"));
	       // nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));
	       // httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        Log.d("Tag", response.toString()); 
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    }
	    
	}

	@Override
	protected Void doInBackground(String... URLs) {
		// TODO Auto-generated method stub
		android.os.Debug.waitForDebugger();
		Log.d("Tag", "doInBackGround"); 
		postCoordsToKubi(URLs[0]); 
		Log.d("Tag", "return from post coords"); 
		return null; 
	}
}