package com.qin.listener;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
/**
 * Created by Administrator on 2018/3/29 0012.
 */
public class MySensorListener implements SensorEventListener
{

	private Context context;
	private SensorManager sensorManager;
	private Sensor sensor;
	
	private float lastX ; 
	private OnMySensorListener onMySensorListener; 

	public MySensorListener(Context context)
	{
		this.context = context;
	}


	public void start()
	{
		
		sensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		if (sensorManager != null)
		{
			
			sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		}
		if (sensor != null)
		{
			sensorManager.registerListener(this, sensor,
					SensorManager.SENSOR_DELAY_UI);
		}

	}

	public void stop()
	{
		sensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
		
	}

	@Override
	public void onSensorChanged(SensorEvent event)
	{

        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION)  
        {  

            float x = event.values[SensorManager.DATA_X];  
            
            if( Math.abs(x- lastX) > 1.0 )
            {
            	if(onMySensorListener!=null){
					onMySensorListener.onMySensorChanged(x);
				}
            }
            Log.i("DATA_X", x+"");
            lastX = x ; 
            
        }  
	}
	
	public void setOnMySensorListener (OnMySensorListener onMySensorListener)
	{
		this.onMySensorListener = onMySensorListener ;
	}
	
	
	public interface OnMySensorListener 
	{
		void onMySensorChanged(float x);
	}
}
