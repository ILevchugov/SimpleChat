package ru.levchugov.chat.server;

import lombok.extern.slf4j.Slf4j;
import ru.levchugov.chat.common.Message;
import ru.levchugov.chat.common.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Server {
    private static final int DEFAULT_THREADS_NUM = Runtime.getRuntime().availableProcessors() * 2;
    private static final AtomicInteger CONNECTIONS_COUNTER = new AtomicInteger(0);

    private final ExecutorService clientsPool;
    private final int port;
    private final int threadsNum;
    private final Map<String, ClientHandler> usersMap;

    public Server(int port) {
        this.threadsNum = DEFAULT_THREADS_NUM;
        this.port = port;
        this.clientsPool = Executors.newFixedThreadPool(threadsNum);
        this.usersMap = new ConcurrentHashMap<>();
    }

    public Server(int port, int threadsNum) {
        this.port = port;
        this.threadsNum = threadsNum;
        this.clientsPool = Executors.newFixedThreadPool(threadsNum);
        this.usersMap = new ConcurrentHashMap<>();
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.debug("Сервер запущен на {} потоках", threadsNum);
            while (!Thread.currentThread().isInterrupted()) {
                Socket clientSocket = serverSocket.accept();
                log.info("Установлено соеденение с клиент-сокетом {}", clientSocket.getInetAddress());
                if (CONNECTIONS_COUNTER.get() < threadsNum) {
                    CONNECTIONS_COUNTER.getAndIncrement();
                    log.debug("Коннектов {}", CONNECTIONS_COUNTER);
                    clientsPool.submit(new ClientHandler(clientSocket, this));
                    log.info("Клиент сокет {} будет обрабатываться ", clientSocket.getInetAddress());
                } else {
                    declineConnection(clientSocket);
                    log.warn("Сервер переполнен, в обработке сокета будет отказано");
                }
            }
        } catch (IOException e) {
            log.error("Ошибка работы с сервером ", e);
        }
    }

    void authorizeUser(String userName, ClientHandler clientHandler) {
        usersMap.put(userName, clientHandler);
        log.info("Зарегистрирован новый пользователь {}", userName);
    }

    void removeUser(String userName) {
        CONNECTIONS_COUNTER.decrementAndGet();
        log.debug("Коннектов после уменьшения {}", CONNECTIONS_COUNTER);
        usersMap.remove(userName);
        log.info("Пользователь {} отключен", userName);
        sendMessageToAll(new Message(userName, MessageType.REMOVE_USER));
    }

    boolean isUserAuthorized(String name) {
        return usersMap.containsKey(name);
    }

    void sendMessageToAll(Message message) {
        for (String userName : usersMap.keySet()) {
            usersMap.get(userName).sendMessage(message);
        }
        log.info("Сообщение типа {} от пользователя {}  отправлено", message.getMessageType(), message.getAuthor());
    }

    Set<String> getUsersSet() {
        return new HashSet<>(usersMap.keySet());
    }

    private void declineConnection(Socket clientSocket) throws IOException {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream())) {
            Thread.sleep(100);
            outputStream.writeObject(new Message(MessageType.CONNECTION_DECLINED));
        } catch (InterruptedException e) {
            log.warn("Внезапная ошибка", e);
        }
    }
}
