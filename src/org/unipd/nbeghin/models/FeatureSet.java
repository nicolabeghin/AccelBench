package org.unipd.nbeghin.models;

/**
 * Created by Nicola Beghin on 11/09/13.
 */
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 *
 * @author Nicola Beghin
 */
public class FeatureSet {
    private String title="";
    private double mean;
    private double variance;
    private double std;

    public FeatureSet(double mean, double variance, double std) {
        this.mean = mean;
        this.variance = variance;
        this.std = std;
    }

    public FeatureSet(String title, double mean, double variance, double std) {
        this(mean, variance, std);
        this.title=title;
    }

    public double getMean() {
        return mean;
    }

    public double getVariance() {
        return variance;
    }

    public double getStd() {
        return std;
    }

    @Override
    public String toString() {
        NumberFormat nf = new DecimalFormat("0.#####");
        return "\n"+title+"\tMEAN "+nf.format(this.mean)+"\tVARIANCE "+nf.format(this.variance)+"\tSTD "+nf.format(this.std);
    }


}