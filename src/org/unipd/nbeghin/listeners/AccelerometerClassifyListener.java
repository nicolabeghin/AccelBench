package org.unipd.nbeghin.listeners;

import android.app.IntentService;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import org.unipd.nbeghin.MainActivity;
import org.unipd.nbeghin.models.ClassifierCircularBuffer;
import org.unipd.nbeghin.models.Sample;

/**
 * Created by Nicola Beghin on 11/09/13.
 */
public class AccelerometerClassifyListener implements SensorEventListener {
    private final float NOISE = (float) 2.0;
    private int sensorDelay;
    private static double samplingRate=18; // default
    private static double step_per_second=0.77; // nexus 4: 14 samples per step (77% of 18Hz)
    ClassifierCircularBuffer buffer;
    IntentService service;

    public void setSensorDelay(int sensorDelay) {
        this.sensorDelay=sensorDelay;
    }

    private void initialize_buffer(double samplingRate) {
        int buffer_size=(int)(samplingRate*step_per_second);
        buffer=new ClassifierCircularBuffer(buffer_size, service);
        Log.i(MainActivity.AppName, "Accelerometer listener instanced with a circular buffer size of "+buffer_size+ " (detected sampling rate: "+samplingRate+" Hz)");
    }

    public void setSamplingRate(double samplingRate) {
        this.samplingRate=samplingRate;
//        initialize_buffer(samplingRate);
    }

    public AccelerometerClassifyListener(Context context, IntentService service) {
        this.service=service;
        initialize_buffer((int) samplingRate);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        buffer.add(new Sample(event.timestamp, event.values[0], event.values[1], event.values[2], null));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO
    }
}
