package es.antoniomb.utils;

import es.antoniomb.dto.MigrationOuputComplexAnalytics;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by amiranda on 23/5/17.
 */
public class ValueAvgComparator implements Comparator<String> {
    Map<String, MigrationOuputComplexAnalytics.TotalAvg> base;

    public ValueAvgComparator(Map<String, MigrationOuputComplexAnalytics.TotalAvg> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with
    // equals.
    public int compare(String a, String b) {
        if (base.get(a).getHits() < base.get(b).getHits()) {
            return -1;
        } else if (base.get(a).equals(base.get(b))) {
            return a.compareTo(b);
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}