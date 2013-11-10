package org.unipd.nbeghin.models;

/**
 * Created by Nicola Beghin on 11/09/13.
 */
public class DataTime {

    private long time;
    private double value;
    private int step;

    public DataTime(long time, double value) {
        this.time = time; this.value = value;
    }

    public long getTime() {
        return time;
    }

    public double getValue() {
        return value;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void normalize(double min, double max) {
        this.value = ((this.value - min) / (max - min));
    }

}