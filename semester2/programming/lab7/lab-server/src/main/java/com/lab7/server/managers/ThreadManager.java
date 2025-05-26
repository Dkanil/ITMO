package com.lab7.server.managers;

import com.lab7.common.utility.ExecutionStatus;
import com.lab7.common.utility.Pair;
import com.lab7.common.utility.Request;
import com.lab7.common.utility.Response;
import com.lab7.common.validators.ArgumentValidator;
import com.lab7.server.Server;
import com.lab7.server.utility.AskingCommand;
import com.lab7.server.utility.PasswordHasher;

import java.io.IOException;
import java.net.SocketException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;


public class ThreadManager {
    private static volatile ThreadManager instance;
    private final Map<SocketChannel, CompletableFuture<Response>> responseFutures = new ConcurrentHashMap<>();
    private final ForkJoinPool readPool;
    private final ForkJoinPool writePool;
    private Executor executor;

    private ThreadManager() {
        readPool = new ForkJoinPool();
        writePool = new ForkJoinPool();
    }

    public static synchronized ThreadManager getInstance() {
        if (instance == null) {
            synchronized (ThreadManager.class) {
                if (instance == null) {
                    instance = new ThreadManager();
                }
            }
        }
        return instance;
    }

    public void runServer(CommandManager commandManager) throws IOException, ClosedSelectorException, NullPointerException {
        executor = new Executor(commandManager);
        Selector selector = Selector.open();
        ServerNetworkManager.getInstance().startServer();
        ServerNetworkManager.getInstance().getServerSocketChannel().register(selector, SelectionKey.OP_ACCEPT);
        Server.logger.info("Selector started");
        Server.logger.info("To stop the server, press [Ctrl + C]");
        while (true) {
            selector.select();
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                Server.logger.info("Processing key: " + key);
                try {
                    if (key.isValid()) {
                        if (key.isAcceptable()) {
                            // Принимаем новое соединение
                            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                            SocketChannel clientChannel = serverSocketChannel.accept();
                            Server.logger.info("Client connected: " + clientChannel.getRemoteAddress());
                            clientChannel.configureBlocking(false);
                            ThreadManager.getInstance().initialCommandsData(clientChannel, key, commandManager);
                            clientChannel.register(selector, SelectionKey.OP_READ);
                        } else if (key.isReadable()) {
                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            clientChannel.configureBlocking(false);
                            if (responseFutures.get(clientChannel) != null) {
                                continue; // Пропускаем итерацию, если уже идёт обработка этого клиента
                            }
                            CompletableFuture<Response> responseFuture = ThreadManager.getInstance().readMessage(clientChannel, key);
                            responseFutures.put(clientChannel, responseFuture);
                            clientChannel.register(selector, SelectionKey.OP_WRITE);
                        } else if (key.isWritable()) {
                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            clientChannel.configureBlocking(false);
                            ThreadManager.getInstance().writeMessage(clientChannel, key);
                            clientChannel.register(selector, SelectionKey.OP_READ);
                        }
                    }
                } catch (SocketException | CancelledKeyException e) {
                    try (var channel = key.channel()) {
                        Server.logger.severe("Client " + channel.toString() + " disconnected");
                        key.cancel();
                    }
                } finally {
                    keys.remove();
                }
            }
        }
    }

    private void initialCommandsData(SocketChannel clientChannel, SelectionKey key, CommandManager commandManager) {
        Thread processRequestThread = new Thread(() -> {
            try {
                Map<String, Pair<ArgumentValidator, Boolean>> commandsData = new HashMap<>();
                commandManager.getCommandsMap().forEach((key1, value) -> {
                    boolean isAskingCommand = AskingCommand.class.isAssignableFrom(value.getClass());
                    commandsData.put(key1, new Pair<>(value.getArgumentValidator(), isAskingCommand));
                });
                ServerNetworkManager.getInstance().send(new Response(commandsData), clientChannel);
                Server.logger.info("Command list sent to the client: " + clientChannel.getRemoteAddress());
            } catch (IOException e) {
                Server.logger.severe("Error sending command list to the client: " + e.getMessage());
                key.cancel();
            } catch (NullPointerException e) {
                Server.logger.severe("The client disconnected from the server: " + e.getMessage());
                key.cancel();
            }
        });
        processRequestThread.start();
        try {
            processRequestThread.join();
        } catch (InterruptedException e) {
            Server.logger.severe("Error while waiting for request processing: " + e.getMessage());
        }
    }

    private CompletableFuture<Response> readMessage(SocketChannel clientChannel, SelectionKey key) {
        CompletableFuture<Response> responseFuture = new CompletableFuture<>();
        readPool.submit(() -> {
            try {
                Request request = ServerNetworkManager.getInstance().receive(clientChannel, key);
                if (request.getUser() != null) { // Шифруем пароль пользователя, но только если он уже авторизован
                    request.getUser().setSecond(PasswordHasher.getHash(request.getUser().getSecond()));
                }
                Server.logger.info("Request received from client: " + request);
                Thread processRequestThread = getProcessRequestThread(request, responseFuture);
                processRequestThread.start();
            } catch (ServerNetworkManager.NullRequestException | SocketException e) {
                Server.logger.severe("Error receiving request from client: " + e.getMessage());
                key.cancel();
            } catch (NullPointerException e) {
                Server.logger.severe("Error receiving request from client: " + e.getMessage());
            } catch (ClosedChannelException e) {
                Server.logger.severe("Error processing request: " + e.getMessage());
                responseFuture.completeExceptionally(e);
                key.cancel();
            } catch (IOException | ClassNotFoundException e) {
                Server.logger.severe("Fatal error: " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
        return responseFuture;
    }

    private Thread getProcessRequestThread(Request request, CompletableFuture<Response> responseFuture) {
        return new Thread(() -> {
            AuthenticatedExecutor authenticatedExecutor = new AuthenticatedExecutor(executor);
            ExecutionStatus executionStatus = authenticatedExecutor.runCommand(request.getCommand(), request.getBand(), request.getUser());
            if (!executionStatus.isSuccess()) {
                Server.logger.severe(executionStatus.getMessage());
            }
            responseFuture.complete(new Response(executionStatus));
        });
    }

    private void writeMessage(SocketChannel clientChannel, SelectionKey key) {
        writePool.submit(() -> {
            try {
                CompletableFuture<Response> responseFuture = responseFutures.get(clientChannel);
                if (responseFuture != null) {
                    Response response = responseFuture.get();
                    ServerNetworkManager.getInstance().send(response, clientChannel);
                    Server.logger.info("Response sent to client: " + clientChannel.getRemoteAddress());
                    responseFutures.remove(clientChannel);
                }
                else{
                    Server.logger.severe("No response future found for client: " + clientChannel.getRemoteAddress());
                }
            }  catch (IOException e) {
                Server.logger.severe("Error sending response to client: " + e.getMessage());
                key.cancel();
            } catch (ExecutionException | InterruptedException e) {
                Server.logger.severe("Error doing the command: " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }
}
