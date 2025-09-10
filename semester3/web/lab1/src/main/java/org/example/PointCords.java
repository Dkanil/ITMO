package org.example;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class PointCords {
    private final int x;
    private final double y;
    private final int r;

    public PointCords(String query) {
        if (query == null || query.isEmpty()) {
            throw new IllegalArgumentException("Query string cannot be null or empty");
        }
        Map<String, String> params = splitQuery(query);
        try {
            this.x = Integer.parseInt(params.get("x"));
            this.y = Double.parseDouble(params.get("y"));
            this.r = Integer.parseInt(params.get("r"));
            if (!validateParams(x, y, r)) {
                throw new IllegalArgumentException("Parameter values are out of valid range");
            }
        }
        catch (NumberFormatException | NullPointerException e) {
            throw new IllegalArgumentException("Invalid parameter format");
        }
    }

    private boolean validateParams(int x, double y, int r) {
        try {
            return Arrays.asList(-3, -2, -1, 0, 1, 2, 3, 4, 5).contains(x) && (y > -3 && y < 3) && Arrays.asList(1, 2, 3, 4, 5).contains(r);
        } catch (Exception e) {
            return false;
        }
    }

    private Map<String, String> splitQuery(String query) {
        return Arrays.stream(query.split("&"))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(a -> a[0], a -> a[1]));
    }

    public boolean PointChecker(int x, double y, int r) {
        if (x >= 0 && y >= 0) {
            return (x * x + y * y) <= ((double) r * r / 4); // 1 четверть
        } else if (x <= 0 && y <= 0) {
            return y >= - x - (double) r / 2; // 3 четверть
        } else if (x >= 0 && y <= 0) {
            return (y >= - (double) r / 2) && (x <= r); // 4 четверть
        }
        return false; // 2 четверть
    }

    public int getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getR() {
        return r;
    }
}
