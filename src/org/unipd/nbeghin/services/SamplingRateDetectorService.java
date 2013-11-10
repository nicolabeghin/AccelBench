package org.unipd.nbeghin.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;

import org.unipd.nbeghin.MainActivity;
import org.unipd.nbeghin.R;
import org.unipd.nbeghin.listeners.AccelerometerClassifyListener;
import org.unipd.nbeghin.listeners.AccelerometerSamplingRateDetect;

/**
 * Created by Nicola Beghin on 10/06/13.
 */
public class SamplingRateDetectorService extends IntentService {
    private AccelerometerSamplingRateDetect accelerometerListener;
    private Sensor mAccelerometer;
    private SensorManager mSensorManager;
    private int notification_id=1;
    private int sensorDelay=SensorManager.SENSOR_DELAY_NORMAL;
    private NotificationManager notificationManager;


    public SamplingRateDetectorService() {
        super("SamplingRateDetectorService");
    }

    @Override
    public void onCreate() {
        try {
            notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            showNotification();
            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            Log.i(MainActivity.AppName, "Sensor manager instanced");
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            accelerometerListener=new AccelerometerSamplingRateDetect(getApplicationContext(), this);
        } catch (Exception e) {
            Log.e(MainActivity.AppName, e.getMessage());
        }
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        CharSequence text="Accelbench sampling rate detection enabled";
        Notification notification = new Notification(R.drawable.ic_launcher, text, System.currentTimeMillis());

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        notification.setLatestEventInfo(getApplicationContext(), "Accelbench sampling rate detection", text, contentIntent);
        notificationManager.notify(notification_id, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            sensorDelay=intent.getExtras().getInt(MainActivity.SAMPLING_DELAY);
            accelerometerListener.setSensorDelay(sensorDelay);
        } catch(NullPointerException e) {
            Log.i(MainActivity.AppName, "No sampling delay detected.");
        }
        switch(sensorDelay) {
            case SensorManager.SENSOR_DELAY_FASTEST: Log.i(MainActivity.AppName, "Chosen FASTEST sensor delay"); break;
            case SensorManager.SENSOR_DELAY_NORMAL: Log.i(MainActivity.AppName, "Chosen NORMAL sensor delay"); break;
            case SensorManager.SENSOR_DELAY_UI: Log.i(MainActivity.AppName, "Chosen UI sensor delay"); break;
            case SensorManager.SENSOR_DELAY_GAME: Log.i(MainActivity.AppName, "Chosen GAME sensor delay"); break;
        }
        startAccelerometer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopAccelerometer();
        notificationManager.cancel(notification_id);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        Log.i(MainActivity.AppName, "handleIntent");
    }

    public void startAccelerometer() {
        Log.i(MainActivity.AppName, "Registering accelerometer listener");
        mSensorManager.registerListener(accelerometerListener, mAccelerometer, sensorDelay); // SensorManager.SENSOR_DELAY_NORMAL
    }

    public void stopAccelerometer() {
        Log.i(MainActivity.AppName, "Un-registering accelerometer listener");
        mSensorManager.unregisterListener(accelerometerListener);
    }
}
