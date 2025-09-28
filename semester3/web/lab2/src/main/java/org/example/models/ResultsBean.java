package org.example.models;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class ResultsBean implements Serializable {
    private final ArrayList<PointCords> results = new ArrayList<>();

    @Serial
    private static final long serialVersionUID = 1L;

    public ArrayList<PointCords> getResults() {
        return results;
    }

    public void addResult(PointCords result) {
        results.add(result);
    }

    @Override
    public String toString() {
        return "ResultsBean{" +
                "results=" + results +
                '}';
    }
}
