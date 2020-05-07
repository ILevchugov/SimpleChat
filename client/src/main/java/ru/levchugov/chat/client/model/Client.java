package ru.levchugov.chat.client.model;

import lombok.extern.slf4j.Slf4j;
import ru.levchugov.chat.client.view.ChatView;
import ru.levchugov.chat.common.Message;
import ru.levchugov.chat.common.MessageType;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.ZonedDateTime;

@Slf4j
public class Client {
    private Thread serverHandlerThread;
    private Socket socket;
    private String userName;
    private ServerHandler serverHandler;
    private final ChatNotifier chatNotifier;

    public Client() {
        this.chatNotifier = new ChatNotifier();
    }

    public void start() {
        chatNotifier.notifyViewShowConnectionView();
    }

    public void connect(String host, int port) {
        this.socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port));
            runServerHandler();
        } catch (IOException e) {
            log.error("Ошибка коннекта к серверу", e);
            chatNotifier.notifyViewShowConnectionError();
        }
    }

    private void runServerHandler() throws IOException {
        serverHandler = new ServerHandler(socket, this);
        serverHandlerThread = new Thread(serverHandler);
        serverHandlerThread.start();
    }

    public void authorizeUser(String name) {
        serverHandler.sendMessage(new Message(name, MessageType.AUTHORIZATION));
    }

    public void sendChatMessage(String text) {
        serverHandler.sendMessage(new Message(ZonedDateTime.now(), userName, text, MessageType.CHAT_MESSAGE));
    }

    public void attachView(ChatView view) {
        chatNotifier.attachView(view);
    }

    public void processDisconnectionRequest() {
        serverHandler.sendMessage(new Message(userName, MessageType.DISCONNECT));
        disconnect();
        chatNotifier.notifyViewClear();
        chatNotifier.notifyViewShowConnectionView();
    }

    void processApprovedAuthorization(String userName) {
        this.userName = userName;
        chatNotifier.notifyViewUnblockButton();
        chatNotifier.notifyViewShowMainChatView();
    }

    void addNewUserInChat(String userName) {
        chatNotifier.notifyViewAboutNewUser(userName);
    }

    void addOldUserInChat(String userName) {
        chatNotifier.notifyViewAboutOldUser(userName);
    }

    void removeUser(String userName) {
        chatNotifier.notifyViewRemoveUser(userName);
    }

    void addNewMessage(String text) {
        chatNotifier.notifyViewShowNewMessage(text);
    }

    void processAcceptConnection() {
        chatNotifier.notifyViewShowAuthorizationView();
    }

    void processDeclinedAuthorization() {
        chatNotifier.notifyViewShowAuthorizationError();
    }

    void processDeclineConnection() {
        chatNotifier.notifyViewShowConnectionOverflowError();
        disconnect();
    }

    public void disconnect() {
        log.info("Закрываю поток слушателя сервера");

        serverHandler.closeStreams();
        serverHandlerThread.interrupt();

        log.debug("Слушатель закрыт? {}", serverHandlerThread.isInterrupted());
        log.info("Закрыл поток слушателя сервера");

        try {
            socket.close();
        } catch (IOException e) {
            log.error("Ошибка при закрытии сокета", e);
        }
    }
}
