package org.unipd.nbeghin.models;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.unipd.nbeghin.MainActivity;
import org.unipd.nbeghin.comparator.MeanComparator;
import org.unipd.nbeghin.weka.WekaClassifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ark0n3 on 14/09/13.
 */
public class ClassifierCircularBuffer {
    private List<Sample> samples=new ArrayList<Sample>();
    private Double[] data_row=new Double[12];
    IntentService service;
    public final static String CLASSIFIER_ACTION="org.unipd.nbeghin.classifier.notification";
    public final static String CLASSIFIER_NOTIFICATION_STATUS="CLASSIFIER_NOTIFICATION_STATUS";
    private int axis_to_be_considered=4; // (4 == |V|)
    private int size=0;

    public ClassifierCircularBuffer(int size, IntentService service) {
        if (size%2!=0) {
            size++; // get an even number
            Log.w(MainActivity.AppName, (size-1) + " is not an even number: using " + size + " as circular buffer size");
        }
        this.service=service;
        this.size=size;
    }

    public void add(Sample sample) {
        samples.add(sample);
        if (samples.size()==size) {
            this.classify();
        }
    }

    private void classify() {
        try {
            Batch batch=new Batch(samples);
            List<FeatureSet> features=batch.getFeatures();
            features=features.subList(0, axis_to_be_considered);
            Collections.sort(features, new MeanComparator());
            int i=0;
            for (FeatureSet featureSet : features) {
                data_row[i]=featureSet.getMean(); i++;
                data_row[i]=featureSet.getStd(); i++;
                data_row[i]=featureSet.getVariance(); i++;
            }
            Intent intent=new Intent();
            intent.setAction(CLASSIFIER_ACTION);
            intent.putExtra(CLASSIFIER_NOTIFICATION_STATUS, WekaClassifier.explicit_classify(data_row));
            service.sendBroadcast(intent);
            //System.out.println("From "+(size/2)+" to "+(samples.size()));
            samples=samples.subList(size/2, samples.size()); // overlapping sliding window
            //System.out.println("Size now: "+samples.size());
        } catch (Exception e) {
            System.err.println("Unable to classify batch:" +e.getMessage());
        }
        samples.clear();
    }

}
