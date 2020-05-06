package ru.levchugov.chat.server;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.levchugov.chat.common.Message;
import ru.levchugov.chat.common.MessageType;
import ru.levchugov.chat.common.ProcessingMessageStrategy;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class ClientHandler implements Runnable {
    private final Map<MessageType, ProcessingMessageStrategy> processingMessageStrategyMap;
    private final Server server;

    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;

    @Setter
    private String userName;

    ClientHandler(Socket clientSocket, Server server) throws IOException {
        this.server = server;
        this.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        this.inputStream = new ObjectInputStream(clientSocket.getInputStream());
        this.processingMessageStrategyMap = new HashMap<>();

        initProcessingMessageStrategyMap();
    }

    private void initProcessingMessageStrategyMap() {
        processingMessageStrategyMap.put(MessageType.CHAT_MESSAGE,
                new ProcessingChatMessage(server));
        processingMessageStrategyMap.put(MessageType.AUTHORIZATION,
                new ProcessingAuthorizationMessage(server, this));
        processingMessageStrategyMap.put(MessageType.DISCONNECT,
                new ProcessingDisconnectMessage(server, this));
    }

    @Override
    public void run() {
        try {
            sendMessage(new Message(MessageType.CONNECTION_ACCEPT));
            startClientListener();
        } catch (IOException e) {
            log.warn("Проблемы при работе с клиентом", e);
            server.removeUser(userName);
            closeStreams();
        }
    }

    void sendMessage(Message message) {
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            log.error("Сообщение не отправлено", e);
        }
    }

    void startClientListener() throws IOException {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message message = (Message) inputStream.readObject();
                log.info("Получено сообщение типа {}, автора {}, c телом {}", message.getMessageType(),
                        message.getAuthor(), message.getBody());
                processClientMessage(message);
                log.info("Сообщение обработано");
            } catch (ClassNotFoundException e) {
                log.error("Ошибка принятия сообщений сокета ", e);
                break;
            }
        }
    }

    void processClientMessage(Message message) {
        processingMessageStrategyMap.get(message.getMessageType()).processMessage(message);
    }

    void sendUsersSetToNewUser() {
        log.info("отправляю спииок бзеров новому пользователю");
        Set<String> users = server.getUsersSet();
        users.remove(userName);
        for(String name: users) {
            log.debug("отправляю юзеру {} юзера {}", userName, name);
            Message message = new Message(name, MessageType.OLD_USER);
            sendMessage(message);
        }
    }

    void closeStreams() {
        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            log.error("Ошибка закрытия потоков сообщений", e);
        }
    }
}
