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
                Content-Length: %d
                
                %s
                """;

        final String HTTP_ERROR = """
                HTTP/1.1 400 Bad Request
                Content-Type: application/json
                Content-Length: %d
                
                %s
                """;

        // Объявим свой логгер по документации тк библиотека доисторическое дерьмо и зажала System.out
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
                String response = "{ \"x\": " + x +
                        ", \"y\": " + y +
                        ", \"r\": " + r +
                        ", \"hit\": " + (hit ? "true" : "false") +
                        ", \"execution_time\": " + execution_time +
                        ", \"timestamp\": " + timestamp + " }";

                //System.out.printf((HTTP_RESPONSE) + "%n", response.length(), response); // todo проверить мб накладываются заголовки
                System.out.println(response);
                output.println("Response sent: " + response);
                output.println("----------End of FCGI request processing----------\n");
                output.flush();
                System.out.flush();
            }
            catch (Exception e) {
                output.println("Error: " + e.getMessage() + "\n");
                String response = "{ \"error\": \"" + e.getMessage() + "\"" +
                        ", \"timestamp\": " + timestamp + " }";
                //System.out.println(HTTP_ERROR.formatted(response.length(), response));
                System.out.println(response);
                output.flush();
            }
        }
    }
}