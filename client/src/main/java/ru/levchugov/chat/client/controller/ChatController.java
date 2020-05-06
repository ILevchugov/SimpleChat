package ru.levchugov.chat.client.controller;

import ru.levchugov.chat.client.model.Client;

public class ChatController {
    private final Client client;

    public ChatController(Client client) {
        this.client = client;
    }

    public void connect(String host, int port) {
        client.connect(host, port);
    }

    public void disconnect() {
        client.processDisconnectionRequest();
    }

    public void authorize(String name) {
        client.authorizeUser(name);
    }

    public void sendChatMessage(String text) {
        client.sendChatMessage(text);
    }
}
