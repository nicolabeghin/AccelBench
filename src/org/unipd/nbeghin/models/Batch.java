package org.unipd.nbeghin.models;

/**
 * Created by Nicola Beghin on 11/09/13.
 */
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Nicola Beghin
 */
public class Batch {

    private List<SingleCoordinateSet> values=new ArrayList<SingleCoordinateSet>();
    private static HashMap<Integer,String> coordinates_mapping=new HashMap<Integer,String>();
    private String title;
    private int trunk = 0;

    static {
        coordinates_mapping.put(0, "X");
        coordinates_mapping.put(1, "Y");
        coordinates_mapping.put(2, "Z");
        coordinates_mapping.put(3, "|V|");
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public int getTrunk() {
        return trunk;
    }

    public void setTrunk(int trunk) {
        this.trunk = trunk;
    }


    public int size() {
        return values.get(0).size();
    }

    public List<SingleCoordinateSet> getValues() {
        return values;
    }

    public Batch(List<Sample> samples) throws Exception {
        if (samples.isEmpty()) throw new Exception("No element given for this batch");
        for(int i=0; i<4; i++) {
            values.add(new SingleCoordinateSet());
            values.get(i).setTitle(coordinates_mapping.get(i));
        }
        for (int axis = 0; axis < samples.size(); axis++) {
            Sample sample=samples.get(axis);
            values.get(0).addValue(new DataTime(sample.getTime(), sample.getValueX()));
            values.get(1).addValue(new DataTime(sample.getTime(), sample.getValueY()));
            values.get(2).addValue(new DataTime(sample.getTime(), sample.getValueZ()));
            values.get(3).addValue(new DataTime(sample.getTime(), sample.getValueV()));
        }
    }

    public void printFeatures() {
        List<FeatureSet> features=this.getFeatures();
        for(FeatureSet featureSet: features) {
            System.out.println(featureSet);
        }
    }

    public List<FeatureSet> getFeatures() {
        List<FeatureSet> features=new ArrayList<FeatureSet>();
        for(int i=0; i<values.size(); i++) {
            values.get(i).normalize(values);
            features.add(new FeatureSet(coordinates_mapping.get(i), values.get(i).getMean(), values.get(i).getVariance(), values.get(i).getStandardDeviation()));
        }
        return features;
    }

}