package com.lab6.server.managers;

import com.lab6.common.utility.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkManager {
    private final int port;
    private ServerSocket serverSocket;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public NetworkManager(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void makeConnection() throws IOException {
        socket = serverSocket.accept();
        inputStream = new ObjectInputStream(socket.getInputStream());
        outputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    public void stop() throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
        if (socket != null) {
            socket.close();
        }
        if (serverSocket != null) {
            serverSocket.close();
        }
    }

    public Request receive() throws IOException, ClassNotFoundException {
        return (Request) inputStream.readObject();
    }

    public void send(Request request) throws IOException {
        outputStream.writeObject(request);
    }
}
