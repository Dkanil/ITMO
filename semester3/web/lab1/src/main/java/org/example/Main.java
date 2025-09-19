package org.example;

import com.fastcgi.FCGIInterface;

import java.io.BufferedOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final String HTTP_RESPONSE = """
                HTTP/1.1 200 OK
                Content-Type: application/json
                Content-Length: %d
                
                %s
                
                """;
        final String HTTP_ERROR = """
                HTTP/1.1 400 Bad Request
                Content-Type: application/json
                Content-Length: %d
                
                %s
                
                """;
        final String JSON_RESPONSE = """
                { "x": %d,
                "y": %.15f,
                "r": %d,
                "hit": %s,
                "execution_time": %f,
                "timestamp": %d }
                """;
        final String JSON_ERROR = """
                { "error": "%s",
                "timestamp": %d }
                """;


        // Объявим свой логгер по документации тк библиотека зажала System.out
        PrintStream output = new PrintStream(new BufferedOutputStream(new FileOutputStream(FileDescriptor.out), 128), true);

        FCGIInterface fcgi = new FCGIInterface();
        output.println("Privet medved");
        while (fcgi.FCGIaccept() >= 0) {
            long timestamp = System.currentTimeMillis();
            try {
                output.println("----------FCGI request accepted----------");

                long startTime = System.nanoTime();
                Scanner input = new Scanner(System.in);
                String query = input.nextLine();
                output.println("Request body read: " + query);

                PointCords point = new PointCords(query);
                int x = point.getX();
                double y = point.getY();
                int r = point.getR();
                output.println("Parsed parameters: " + x + ", " + y + ", " + r);

                boolean hit = point.PointChecker(x, y, r);
                output.println("hit: " + hit);

                long endTime = System.nanoTime();
                double execution_time = (double) (endTime - startTime) / 1_000_000;

                // Locale.US чтобы у дробных чисел был '.' а не ','
                String response = String.format(java.util.Locale.US, JSON_RESPONSE, x, y, r, hit, execution_time, timestamp);
                System.out.printf(HTTP_RESPONSE, response.length(), response);
                output.printf("Response sent: {{" + HTTP_RESPONSE + "}}\n", response.length(), response);
                output.println("----------End of FCGI request processing----------\n");
                output.flush();
                System.out.flush();
            }
            catch (Exception e) {
                output.println("Error: " + e.getMessage() + "\n");
                String response = String.format(java.util.Locale.US, JSON_ERROR, e.getMessage(), timestamp);
                System.out.printf(HTTP_ERROR, response.length(), response);
                output.flush();
            }
        }
        output.println("Server stopped");
        output.flush();
    }
}