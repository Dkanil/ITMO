package com.lab6.server.managers;

import com.lab6.common.utility.Request;
import com.lab6.common.utility.Response;

import java.util.Arrays;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.StreamCorruptedException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ServerNetworkManager {
    private final int PORT;
    private final String SERVER_HOST;
    private ServerSocketChannel serverChannel;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public ServerNetworkManager(int port, String SERVER_HOST) {
        this.PORT = port;
        this.SERVER_HOST = SERVER_HOST;
    }

    public void startServer() throws IOException {
        serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(SERVER_HOST, PORT));
        serverChannel.configureBlocking(false);
    }
    public SocketChannel getSocketChannel() throws IOException {
        return serverChannel.accept();
    }

    public ServerSocketChannel getServerSocketChannel() {
        return serverChannel;
    }

    public void connect() throws IOException {
        socket = new Socket(SERVER_HOST, PORT);
        inputStream = new ObjectInputStream(socket.getInputStream());
        outputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    public void close() throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
        if (socket != null) {
            socket.close();
        }
        if (serverChannel != null) {
            serverChannel.close();
        }
    }

    public void send(Response response, SocketChannel clientChannel) throws IOException, ClassNotFoundException {

        try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
             ObjectOutputStream clientDataOut = new ObjectOutputStream(bytes)) {
            clientDataOut.writeObject(response);

            var byteResponse = bytes.toByteArray();

            ByteBuffer dataLength = ByteBuffer.allocate(8).putInt(byteResponse.length);
            dataLength.flip();
            clientChannel.write(dataLength);
            //logger.info("Отправлен пакет с длинной сообщения"); todo: доп

            while(byteResponse.length > 256){
                ByteBuffer packet = ByteBuffer.wrap(Arrays.copyOfRange(byteResponse, 0, 256));
                clientChannel.write(packet);
                byteResponse = Arrays.copyOfRange(byteResponse, 256, byteResponse.length);
                //logger.info("Отправлен пакет байтов длины: " + packet.position()); todo: доп
            }
            ByteBuffer packet = ByteBuffer.wrap(byteResponse);
            clientChannel.write(packet);
            Thread.sleep(300);
            //logger.info("Отправлен последний пакет байтов длины: " + packet.position()); todo: доп
            ByteBuffer stopPacket = ByteBuffer.wrap(new byte[]{28, 28});
            clientChannel.write(stopPacket);
            //logger.info("Отправлен стоп пакет\n"); todo: доп
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Request receive(SocketChannel clientChannel, SelectionKey key) throws IOException, ClassNotFoundException { //todo: проверить
        ByteBuffer clientData = ByteBuffer.allocate(2048);

        int bytesRead = clientChannel.read(clientData);
        if (bytesRead == -1) {
            key.cancel();
            return null; // Клиент закрыл соединение
        }
        clientData.flip(); // Переключаем ByteBuffer в режим чтения

        try (ObjectInputStream clientDataIn = new ObjectInputStream(new ByteArrayInputStream(clientData.array(), 0, bytesRead))) {
            return (Request) clientDataIn.readObject();
        } catch (StreamCorruptedException e) {
            key.cancel();
            return null;
        }
    }
}
