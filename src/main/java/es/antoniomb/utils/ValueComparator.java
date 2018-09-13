package es.antoniomb.utils;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by amiranda on 23/5/17.
 */
public class ValueComparator implements Comparator<String> {
    Map<String, Integer> base;

    public ValueComparator(Map<String, Integer> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with
    // equals.
    public int compare(String a, String b) {
        if (base.get(a) > base.get(b)) {
            return -1;
        } else if (base.get(a).equals(base.get(b))) {
            return a.compareTo(b);
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}