package ru.levchugov.chat.client.model;

import lombok.extern.slf4j.Slf4j;
import ru.levchugov.chat.common.Message;
import ru.levchugov.chat.common.MessageType;
import ru.levchugov.chat.common.ProcessingMessageStrategy;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ServerHandler implements Runnable {
    private final Map<MessageType, ProcessingMessageStrategy> processingMessageStrategyMap;

    private final Socket clientSocket;
    private final Client client;
    private final ObjectOutputStream outputStream;

    ServerHandler(Socket clientSocket, Client client) throws IOException {
        this.clientSocket = clientSocket;
        this.client = client;
        this.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        this.processingMessageStrategyMap = new HashMap<>();
        initProcessingMessageStrategyMap();
    }

    private void initProcessingMessageStrategyMap() {
        processingMessageStrategyMap.put(MessageType.CHAT_MESSAGE,
                new ProcessingChatMessage(client));
        processingMessageStrategyMap.put(MessageType.AUTHORIZATION_APPROVED,
                new ProcessingAuthorizationApprovedMessage(client));
        processingMessageStrategyMap.put(MessageType.NEW_USER,
                new ProcessingNewUserMessage(client));
        processingMessageStrategyMap.put(MessageType.OLD_USER,
                new ProcessingOldUserMessage(client));
        processingMessageStrategyMap.put(MessageType.CONNECTION_ACCEPT,
                new ProcessingConnectionAcceptMessage(client));
        processingMessageStrategyMap.put(MessageType.AUTHORIZATION_DECLINED,
                new ProcessingAuthorizationDeclinedMessage(client));
        processingMessageStrategyMap.put(MessageType.REMOVE_USER,
                new ProcessingRemoveUserMessage(client));
        processingMessageStrategyMap.put(MessageType.CONNECTION_DECLINED,
                new ProcessingConnectionDeclineMessage(client));
    }

    public void run() {
        try (ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream())) {
            while (!Thread.currentThread().isInterrupted()) {
                Message message = (Message) inputStream.readObject();
                log.info("Получено сообщение типа {}, автора {}, c телом {}", message.getMessageType(),
                        message.getAuthor(), message.getBody());
                processServerMessage(message);
                log.info("Сообщение обработано");
            }
        } catch (SocketException e) {
            Thread.currentThread().interrupt();
            log.info("Сокет закрыт, прием сообщений прекращен"); //это костыль
        } catch (ClassNotFoundException e) {
            log.error("Ошибка принятия сообщений от сервера", e);
        } catch (IOException e) {
            log.error("Ошибка работы с слушателем сервера", e);
        }
    }


    void sendMessage(Message message) {
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            log.error("Сообщение не отправлено", e);
        }
    }

    void closeStreams() {
        try {
            outputStream.close();
        } catch (IOException e) {
            log.error("Возникла ошибка при закрытии сокета", e);
        }
    }

    void processServerMessage(Message message) {
        ProcessingMessageStrategy processingMessageStrategy = processingMessageStrategyMap.get(message.getMessageType());
        if (processingMessageStrategy == null) {
            log.error("Такого типа стратегии нет");
        } else {
            processingMessageStrategy.processMessage(message);
        }
    }
}

