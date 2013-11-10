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
    private boolean mInitialized;
    private final float NOISE = (float) 2.0;
    private DbAdapter db;
    private float mLastX, mLastY, mLastZ;
    private String action;
    private int sensorDelay;
    private static final String UNDEFINED_ACTION="UNDEFINED";

    public void closeDb() {
        db.close();
    }

    public void setSensorDelay(int sensorDelay) {
        this.sensorDelay=sensorDelay;
    }
    public void setAction(String action) {
        this.action=action;
    }

    public AccelerometerStoreListener(Context context) {
        this(context, UNDEFINED_ACTION);
    }

    public AccelerometerStoreListener(Context context, String action) {
        this.action=action;
        db=new DbAdapter(context);
        db.open();
        Log.i(MainActivity.AppName, "DB connection opened successfully ("+db.getCount()+" pre-existing rows)");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        db.saveSample(event.values[0], event.values[1], event.values[2], event.timestamp, action, sensorDelay);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO
    }
}
