package org.unipd.nbeghin.listeners;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import org.unipd.nbeghin.MainActivity;
import org.unipd.nbeghin.utils.DbAdapter;

/**
 * Created by Nicola Beghin on 15/06/13.
 */
public class AccelerometerStoreListener implements SensorEventListener {
	private boolean				mInitialized			= false;
	private float				minDiff					= 0.0f;
	private DbAdapter			db;
	private float				mLastX, mLastY, mLastZ;
	private String				action;
	private int					sensorDelay;
	private String				accelerometer_position	= null;
	private static final String	UNDEFINED_ACTION		= "UNDEFINED";

	public void closeDb() {
		db.close();
	}

	public float getMinDiff() {
		return minDiff;
	}

	public void setMinDiff(float minDiff) {
		this.minDiff = minDiff;
	}

	public void setAccelerometerPosition(String position) {
		this.accelerometer_position = position;
	}

	public void setSensorDelay(int sensorDelay) {
		this.sensorDelay = sensorDelay;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public AccelerometerStoreListener(Context context) {
		this(context, UNDEFINED_ACTION);
	}

	public AccelerometerStoreListener(Context context, String action) {
		this.action = action;
		db = new DbAdapter(context);
		db.open();
		Log.i(MainActivity.AppName, "DB connection opened successfully (" + db.getCount() + " pre-existing rows)");
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		if (!mInitialized) {
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			mInitialized = true;
		}
		float deltaX = Math.abs(mLastX - x);
		float deltaY = Math.abs(mLastY - y);
		float deltaZ = Math.abs(mLastZ - z);
		if (deltaX <= minDiff) x = mLastX; // if delta < NOISE then use previous value
		if (deltaY <= minDiff) y = mLastY; // if delta < NOISE then use previous value
		if (deltaZ <= minDiff) z = mLastZ; // if delta < NOISE then use previous value
		// update last value for next onSensorChanged
		mLastX = x;
		mLastY = y;
		mLastZ = z;
		db.saveSample(x, y, z, event.timestamp, action, sensorDelay, accelerometer_position);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO
	}
}
