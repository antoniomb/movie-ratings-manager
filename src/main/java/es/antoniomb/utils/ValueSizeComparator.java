package es.antoniomb.utils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ValueSizeComparator {
    Map<String, List<String>> base;

    public ValueSizeComparator(Map<String, List<String>> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with
    // equals.
    public int compare(List<String> a, List<String> b) {
        int aSize = base.get(a).size();
        int bSize = base.get(b).size();
        if (aSize > bSize) {
            return -1;
        } else if (aSize == bSize) {
            return Integer.compare(a.size(), b.size());
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}
