package com.lab6.server.managers;

import com.lab6.common.utility.Request;
import com.lab6.common.utility.Response;
import com.lab6.server.Server;

import java.util.Arrays;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
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

    public ServerNetworkManager(int port, String SERVER_HOST) {
        this.PORT = port;
        this.SERVER_HOST = SERVER_HOST;
    }

    public void startServer() throws IOException {
        serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(SERVER_HOST, PORT));
        serverChannel.configureBlocking(false);
        Server.logger.info("Server started on " + SERVER_HOST + ":" + PORT);
    }

    public void close() throws IOException {
        if (serverChannel != null) {
            serverChannel.close();
        }
        Server.logger.info("Server shutdown complete");
    }

    public ServerSocketChannel getServerSocketChannel() {
        return serverChannel;
    }

    public void send(Response response, SocketChannel clientChannel) throws IOException, ClassNotFoundException {
        try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
             ObjectOutputStream clientDataOut = new ObjectOutputStream(bytes)) {
            clientDataOut.writeObject(response);
            byte[] byteResponse = bytes.toByteArray();

            ByteBuffer dataLength = ByteBuffer.allocate(8).putInt(byteResponse.length);
            dataLength.flip();
            clientChannel.write(dataLength);
            Server.logger.info("Packet with message length sent: " + byteResponse.length);

            while (byteResponse.length > 256) {
                ByteBuffer packet = ByteBuffer.wrap(Arrays.copyOfRange(byteResponse, 0, 256));
                clientChannel.write(packet);
                byteResponse = Arrays.copyOfRange(byteResponse, 256, byteResponse.length);
                Server.logger.info("Packet of bytes sent with length: " + packet.position());
            }
            ByteBuffer packet = ByteBuffer.wrap(byteResponse);
            clientChannel.write(packet);
            Thread.sleep(300);
            Server.logger.info("Last packet of bytes sent with length: " + packet.position());
            ByteBuffer stopPacket = ByteBuffer.wrap(new byte[]{69, 69});
            clientChannel.write(stopPacket);
            Server.logger.info("Stop packet sent\n");
        } catch (InterruptedException e) {
            Server.logger.severe("Error while sending data: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static class NullRequestException extends Exception {
        public NullRequestException(String message) {
            super(message);
        }
    }

    public Request receive(SocketChannel clientChannel, SelectionKey key) throws IOException, ClassNotFoundException, NullRequestException {
        ByteBuffer clientData = ByteBuffer.allocate(2048);
        int bytesRead = clientChannel.read(clientData);
        Server.logger.info(bytesRead + " bytes received from client");
        if (bytesRead == -1) {
            key.cancel();
            Server.logger.warning("Client closed the connection");
            throw new NullRequestException("Client closed the connection");
        }
        clientData.flip(); // Переключаем ByteBuffer в режим чтения

        try (ObjectInputStream clientDataIn = new ObjectInputStream(new ByteArrayInputStream(clientData.array(), 0, bytesRead))) {
            Server.logger.info("Request received from client");
            return (Request) clientDataIn.readObject();
        } catch (StreamCorruptedException e) {
            key.cancel();
            Server.logger.severe("Request was not received from client: " + e.getMessage());
            throw new NullRequestException("Request was not received from client");
        }
    }
}
