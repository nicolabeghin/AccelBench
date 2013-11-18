/**
 * Created by Nicola Beghin on 05/06/13.
 */

package org.unipd.nbeghin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.unipd.nbeghin.listeners.AccelerometerSamplingRateDetect;
import org.unipd.nbeghin.models.ClassifierCircularBuffer;
import org.unipd.nbeghin.services.SamplingClassifyService;
import org.unipd.nbeghin.services.SamplingRateDetectorService;
import org.unipd.nbeghin.services.SamplingStoreService;
import org.unipd.nbeghin.utils.DbAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends Activity {
    public static final String AppName = "AccelBench";
    public static final String SAMPLING_TYPE = "ACTION_SAMPLING";
    public static final String SAMPLING_TYPE_STAIR_UPSTAIRS = "STAIR_UPSTAIRS";
    public static final String SAMPLING_TYPE_STAIR_DOWNSTAIRS = "STAIR_DOWNSTAIRS";
    public static final String SAMPLING_TYPE_NON_STAIR = "NON_STAIR";
    public static final String SAMPLING_DELAY="DELAY";
	public static final String	ACCELEROMETER_POSITION_ACTION	= "org.unipd.nbeghin.accelerometer.position";
	public static final String	ACCELEROMETER_MIN_DIFF	= "org.unipd.nbeghin.accelerometer.min_diff";
    private boolean samplingEnabled=false;
    private double detectedSamplingRate=0;
    private double minimumSamplingRate=13;
    private Intent backgroundStoreSampler;
    private Intent backgroundClassifySampler;
    private Intent backgroundSamplingRateDetector;
    private IntentFilter classifierFilter =new IntentFilter(ClassifierCircularBuffer.CLASSIFIER_ACTION);
    private IntentFilter samplingRateDetectorFilter=new IntentFilter(AccelerometerSamplingRateDetect.SAMPLING_RATE_ACTION);
    private BroadcastReceiver classifierReceiver=new ClassifierReceiver();
    private BroadcastReceiver sampleRateDetectorReceiver=new SamplingRateDetectorReceiver();
    private int num_steps=0;

    public class ClassifierReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String result=intent.getExtras().getString(ClassifierCircularBuffer.CLASSIFIER_NOTIFICATION_STATUS);
            Log.i(MainActivity.AppName, result);
            if (result.equals("STAIR")) {
                num_steps++;
                ((TextView) findViewById(R.id.lblNumSteps)).setText(Integer.toString(num_steps));
            }
            ((TextView) findViewById(R.id.lblClassifierOutput)).setText(result);
        }
    }

    public class SamplingRateDetectorReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            detectedSamplingRate=intent.getExtras().getDouble(AccelerometerSamplingRateDetect.SAMPLING_RATE);
            Log.i(MainActivity.AppName, Double.toString(detectedSamplingRate));
            if (detectedSamplingRate>=minimumSamplingRate) { // sampling rate high enough
                ((TextView) findViewById(R.id.lblSamplingRate)).setText(detectedSamplingRate + " Hz");
                backgroundClassifySampler.putExtra(AccelerometerSamplingRateDetect.SAMPLING_RATE, detectedSamplingRate);
                backgroundClassifySampler.putExtra(SAMPLING_DELAY, backgroundSamplingRateDetector.getExtras().getInt(MainActivity.SAMPLING_DELAY));
                unregisterReceiver(this);
                stopService(backgroundSamplingRateDetector);
            } else { // sampling rate not high enough: try to decrease the sampling delay
                if (backgroundSamplingRateDetector.getExtras().getInt(MainActivity.SAMPLING_DELAY)!=SensorManager.SENSOR_DELAY_UI) {
                    Log.w(MainActivity.AppName, "Sampling rate not high enough: trying decreasing the sampling delay");
                    stopService(backgroundSamplingRateDetector);
                    backgroundSamplingRateDetector.putExtra(SAMPLING_DELAY, SensorManager.SENSOR_DELAY_UI);
                    startService(backgroundSamplingRateDetector);
                } else { // unable to determine a sampling rate high enough for our purposes: stop
                    Log.e(MainActivity.AppName, "Sampling rate not high enough for this application");
                    unregisterReceiver(this);
                    stopService(backgroundSamplingRateDetector);
                    ((TextView) findViewById(R.id.lblSamplingRate)).setText("TOO LOW: " + detectedSamplingRate + " Hz");
                    AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
                    alert.setTitle("Sampling rate not high enough");
                    alert.setMessage("Your accelerometer is not fast enough for this application. Make sure to use at least "+minimumSamplingRate+" Hz");
                    alert.show();
//                    Toast.makeText(getApplicationContext(), "Your accelerometer is not fast enough for this application (detected a frequency of "+detectedSamplingRate+"Hz)", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void onBtnStartSamplingAltro(View v) {
        ((Button) findViewById(R.id.btnStartSampling)).setEnabled(false);
        v.setEnabled(false); // disable start button
        ((Button) findViewById(R.id.btnStopSamplingAltro)).setEnabled(true); // enable stop button
        backgroundStoreSampler.putExtra(SAMPLING_TYPE, SAMPLING_TYPE_NON_STAIR);
        setGeneralSamplingParams();
        startSamplingService();
    }

    private void setGeneralSamplingParams() {
    	// accelerometer position
        int selectedId = ((RadioGroup) findViewById(R.id.radioAccelerometerPosition)).getCheckedRadioButtonId();
        switch(selectedId) {
        	case R.id.radioManoMode: backgroundStoreSampler.putExtra(ACCELEROMETER_POSITION_ACTION, "MANO"); break;
        	case R.id.radioTascaMode: backgroundStoreSampler.putExtra(ACCELEROMETER_POSITION_ACTION, "TASCA"); break;
        }
        // accelerometer min diff
        try {
        	float minDiff=Float.parseFloat(((EditText) findViewById(R.id.minDiff)).getText().toString());
        	backgroundStoreSampler.putExtra(ACCELEROMETER_MIN_DIFF, minDiff);
        } catch(Exception e) {
        	Log.e(AppName, "Unable to set min diff: "+e.getMessage());
        }
    }
    
    public void onBtnStopSamplingAltro(View v) {
        stopService(backgroundStoreSampler); // stop background server
        samplingEnabled=false;
        v.setEnabled(false); // disable stop button
        ((Button) findViewById(R.id.btnStartSamplingAltro)).setEnabled(true); // enable start button
        ((Button) findViewById(R.id.btnStartSampling)).setEnabled(true); // enable start button
    }

    public void onBtnStartSampling(View v) {
        ((Button) findViewById(R.id.btnStartSamplingAltro)).setEnabled(false);
        v.setEnabled(false); // disable start button
        ((Button) findViewById(R.id.btnStopSampling)).setEnabled(true); // enable stop button
        int selectedId = ((RadioGroup) findViewById(R.id.radioStairsType)).getCheckedRadioButtonId();
        switch(selectedId) {
            case R.id.radioDownstairs: backgroundStoreSampler.putExtra(SAMPLING_TYPE, SAMPLING_TYPE_STAIR_DOWNSTAIRS); break;
            case R.id.radioUpstairs: backgroundStoreSampler.putExtra(SAMPLING_TYPE, SAMPLING_TYPE_STAIR_UPSTAIRS); break;
        }
        setGeneralSamplingParams();
        startSamplingService();
    }

    public void onBtnStopSampling(View v) {
        stopService(backgroundStoreSampler); // stop background server
        samplingEnabled=false;
        v.setEnabled(false); // disable stop button
        ((Button) findViewById(R.id.btnStartSamplingAltro)).setEnabled(true); // enable start button
        ((Button) findViewById(R.id.btnStartSampling)).setEnabled(true); // enable start button
    }

    public void startSamplingService() {
        int selectedId = ((RadioGroup) findViewById(R.id.radioSamplingGroup)).getCheckedRadioButtonId();
        switch(selectedId) {
            case R.id.radioSamplingFastest: backgroundStoreSampler.putExtra(SAMPLING_DELAY, SensorManager.SENSOR_DELAY_FASTEST); break;
            case R.id.radioSamplingUI: backgroundStoreSampler.putExtra(SAMPLING_DELAY, SensorManager.SENSOR_DELAY_UI); break;
            case R.id.radioSamplingGame: backgroundStoreSampler.putExtra(SAMPLING_DELAY, SensorManager.SENSOR_DELAY_GAME); break;
            default: backgroundStoreSampler.putExtra(SAMPLING_DELAY, SensorManager.SENSOR_DELAY_NORMAL);
        }
        startService(backgroundStoreSampler); // start background service
        samplingEnabled=true;
    }

    public void startClassifyService() {
//        int selectedId = ((RadioGroup) findViewById(R.id.radioSamplingGroup)).getCheckedRadioButtonId();
//        switch(selectedId) {
//            case R.id.radioSamplingFastest: backgroundClassifySampler.putExtra(SAMPLING_DELAY, SensorManager.SENSOR_DELAY_FASTEST); break;
//            case R.id.radioSamplingUI: backgroundClassifySampler.putExtra(SAMPLING_DELAY, SensorManager.SENSOR_DELAY_UI); break;
//            case R.id.radioSamplingGame: backgroundClassifySampler.putExtra(SAMPLING_DELAY, SensorManager.SENSOR_DELAY_GAME); break;
//            default: backgroundClassifySampler.putExtra(SAMPLING_DELAY, SensorManager.SENSOR_DELAY_NORMAL);
//        }
        startService(backgroundClassifySampler); // start background service
        registerReceiver(classifierReceiver, classifierFilter);
        num_steps=0;
        ((TextView) findViewById(R.id.lblNumSteps)).setText(Integer.toString(num_steps));
        samplingEnabled=true;
    }

    public void onBtnStartClassifying(View v) {
        if (detectedSamplingRate==0 || detectedSamplingRate<minimumSamplingRate) {
            Toast.makeText(getApplicationContext(), "Accelerometer calibration is not ready yet. Please wait", Toast.LENGTH_SHORT).show();
            return;
        }
        if (samplingEnabled==false) {
            startClassifyService();
            ((Button)v).setText("Stop classifier");
        } else {
            stopClassify();
            samplingEnabled=false;
        }
    }

    public void stopClassify() {
        stopService(backgroundClassifySampler); // stop background server
        samplingEnabled=false;
        ((Button)findViewById(R.id.btnStartClassifier)).setText("Start classifier");
        unregisterReceiver(classifierReceiver);
        ((TextView) findViewById(R.id.lblClassifierOutput)).setText("Classifier output");
    }

    public void onBtnStopClassify(View v) {
        stopService(backgroundClassifySampler); // stop background server
        samplingEnabled=false;
        v.setEnabled(false); // disable stop button
    }

    private void stopAllServices() {
        try {
            stopService(backgroundSamplingRateDetector);
            unregisterReceiver(sampleRateDetectorReceiver);
        } catch (Exception e) {
            Log.e(MainActivity.AppName, "Unable to stop sampling rate detector service");
            e.printStackTrace();
        }
        try {
            stopService(backgroundClassifySampler);
            unregisterReceiver(classifierReceiver);
        } catch (Exception e) {
            Log.e(MainActivity.AppName, "Unable to stop classifier service");
            e.printStackTrace();
        }
        try {
            stopService(backgroundStoreSampler);
        } catch (Exception e) {
            Log.e(MainActivity.AppName, "Unable to stop background store");
            e.printStackTrace();
        }
    }

    public void onBtnClearDb(MenuItem v) {
        clearDb();
    }

    public void onBtnStopAllServices(MenuItem v) {
        stopAllServices();
    }

    public void onBtnUploadDb(MenuItem v) {
        shareDb();
    }

    private void clearDb() {
        DbAdapter dbAdapter = new DbAdapter(this);
        dbAdapter.open();
        dbAdapter.cleanDb();
        Log.i(AppName, "Database cleared");
        Toast.makeText(getApplicationContext(), "Database cleared", Toast.LENGTH_SHORT).show();
        dbAdapter.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backgroundStoreSampler = new Intent(this, SamplingStoreService.class); // instance (without starting) background sampler
        backgroundClassifySampler = new Intent(this, SamplingClassifyService.class); // instance (without starting) background classifier
        backgroundSamplingRateDetector = new Intent(this, SamplingRateDetectorService.class); // instance (without starting) background sampling rate detected
        backgroundSamplingRateDetector.putExtra(SAMPLING_DELAY, SensorManager.SENSOR_DELAY_NORMAL);
        registerReceiver(sampleRateDetectorReceiver, samplingRateDetectorFilter);
        startService(backgroundSamplingRateDetector); // start background service
    }

    @Override
    protected void onResume() {
        Log.i(AppName, "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i(AppName, "onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.i(AppName, "onDestroy");
        stopAllServices();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (samplingEnabled==false) super.onBackPressed();
        else Toast.makeText(getApplicationContext(), "Sampling running - Stop it before exiting", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    private void shareDb() {
        SimpleDateFormat df=new SimpleDateFormat("yyyyMMddHHmmss");
        String output_name="accelbench_"+df.format(new Date())+".db";
        try {
            DbAdapter dbAdapter = new DbAdapter(this); // get reference to db connection
            dbAdapter.open();
            File file=new File(dbAdapter.getDbPath()); // get private db reference
            dbAdapter.close();
            if (file.exists()==false || file.length()==0) throw new Exception("Empty DB");
            this.copyFile(new FileInputStream(file), this.openFileOutput(output_name, MODE_WORLD_READABLE));
            file=this.getFileStreamPath(output_name);
            Intent i=new Intent(Intent.ACTION_SEND);
            i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            i.setType("*/*");
            startActivity(Intent.createChooser(i, "Share to"));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Unable to export db: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(AppName, e.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (samplingEnabled==false) {
            if (item.getItemId() == R.id.action_cleardb) {
                clearDb();
                return (true);
            }
            if (item.getItemId() == R.id.action_upload_db) {
                shareDb();
                return true;
            }
            if (item.getItemId()==R.id.action_stop_all_services) {
                stopAllServices();
                return true;
            }
        }
        return (super.onOptionsItemSelected(item));
    }
}
