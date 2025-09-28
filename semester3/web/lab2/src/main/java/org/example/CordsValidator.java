package org.example;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class CordsValidator {
    public boolean validateParams(double x, double y, double r) {
        try {
            return (x >= -5 && x <= 5) && (y >= -5 && y <= 5) && Arrays.asList(1.0, 1.5, 2.0, 2.5, 3.0).contains(r);
        } catch (Exception e) {
            return false;
        }
    }

    public Map<String, String> splitQuery(String query) {
        return Arrays.stream(query.split("&"))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(a -> a[0], a -> a[1]));
    }
}
