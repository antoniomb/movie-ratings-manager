package es.antoniomb.dto;

import es.antoniomb.utils.ValueComparator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MigrationOuputComplexAnalytics {

    private Map<String, List<String>> directors = new TreeMap<>();
    private Map<String, List<String>> actors = new TreeMap<>();

    public Map<String, List<String>> getDirectors() {
        return directors;
    }

    public void setDirectors(Map<String, List<String>> directors) {
        this.directors = directors;
    }

    public Map<String, List<String>> getActors() {
        return actors;
    }

    public void setActors(Map<String, List<String>> actors) {
        this.actors = actors;
    }
}
