/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unipd.nbeghin.comparator;

/**
 *
 * @author Nicola Beghin
 */

import org.unipd.nbeghin.models.FeatureSet;

import java.util.Comparator;

/**
 *
 * @author Nicola Beghin
 */
public class MeanComparator implements Comparator<FeatureSet> {
  
  @Override
  public int compare(FeatureSet first, FeatureSet second) {
    return new Double(second.getMean()).compareTo(new Double(first.getMean()));
  }
  
}
