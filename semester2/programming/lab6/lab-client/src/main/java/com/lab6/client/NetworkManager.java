package com.lab6.client;

import com.lab6.common.utility.Request;
import com.lab6.common.utility.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.nio.channels.SocketChannel;

public class NetworkManager {
    private final int PORT;
    private final String SERVER_HOST;
    private SocketChannel channel;

    public NetworkManager(int port, String host) {
        this.PORT = port;
        this.SERVER_HOST = host;
    }


    public void connect() throws IOException {
        channel = SocketChannel.open();
        channel.connect(new InetSocketAddress(SERVER_HOST, PORT));
    }

    public void close() throws IOException {
        if (channel != null) {
            channel.close();
        }
    }

    public void send(Request request) throws IOException, ClassNotFoundException {
        try(ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bytes)) {

            out.writeObject(request);
            ByteBuffer dataToSend = ByteBuffer.wrap(bytes.toByteArray());
            channel.write(dataToSend); // отправляем серверу запрос
            out.flush();
        }
    }

    public Response receive() throws IOException, ClassNotFoundException {
        ByteBuffer dataToReceiveLength = ByteBuffer.allocate(8);
        channel.read(dataToReceiveLength); // читаем длину ответа от сервера
        dataToReceiveLength.flip();
        int responseLength = dataToReceiveLength.getInt(); // достаём её из буфера

        ByteBuffer responseBytes = ByteBuffer.allocate(responseLength); // создаем буфер нужной нам длины
        ByteBuffer packetFromServer = ByteBuffer.allocate(256);

        while (true){
            channel.read(packetFromServer);
            if (packetFromServer.position() == 2 && packetFromServer.get(0) == 28 && packetFromServer.get(1) == 28) break;
            packetFromServer.flip();
            responseBytes.put(packetFromServer);
            packetFromServer.clear();
        }

        try(ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(responseBytes.array()))){
            return (Response) in.readObject();
        }
    }
}
