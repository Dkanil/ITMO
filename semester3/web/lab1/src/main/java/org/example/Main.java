package org.example;

import com.fastcgi.FCGIInterface;

import java.io.BufferedOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Объявим свой логгер по документации тк библиотека доисторическое дерьмо и зажала System.out
        PrintStream output = new PrintStream(new BufferedOutputStream(new FileOutputStream(FileDescriptor.out), 128), true);

        FCGIInterface fcgi = new FCGIInterface();
        output.println("Privet medved");
        while (fcgi.FCGIaccept() >= 0) {
            try {

                output.println("FCGI request accepted");
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
                long timestamp = System.currentTimeMillis();
                double execution_time = (double) (endTime - startTime) / 1_000_000;
                String response = "Content-type: application/json\r\n\r\n" +
                            "{ \"x\": " + x +
                            ", \"y\": " + y +
                            ", \"r\": " + r +
                            ", \"hit\": " + (hit ? "true" : "false") +
                            ", \"execution_time\": " + execution_time +
                            ", \"timestamp\": " + timestamp + " }";
                System.out.print(response);
                output.println("Response sent: " + response);
                output.println("End of FCGI request processing\n");
                output.flush();
                System.out.flush();
            }
            catch (Exception e) {
                output.println("Error: " + e.getMessage());
                output.flush();
            }

        }
    }
}