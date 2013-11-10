/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unipd.nbeghin.comparator;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import org.unipd.nbeghin.models.Sample;

import java.util.Comparator;

/**
 *
 * @author Nicola Beghin
 */
public class SampleTimeComparator implements Comparator<Sample> {
  
  @Override
  public int compare(Sample first, Sample second) {
    return new Double(first.getTime()).compareTo(new Double(second.getTime()));
  }
  
}
