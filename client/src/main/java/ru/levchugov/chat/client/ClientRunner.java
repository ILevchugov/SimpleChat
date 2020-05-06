package ru.levchugov.chat.client;

import ru.levchugov.chat.client.controller.ChatController;
import ru.levchugov.chat.client.model.Client;
import ru.levchugov.chat.client.view.ChatSwingView;
import ru.levchugov.chat.client.view.ChatView;

public class ClientRunner {
    public static void main(String[] args) {
        Client client = new Client();
        ChatController chatController = new ChatController(client);
        ChatView chatView = new ChatSwingView(chatController);
        client.attachView(chatView);
        client.start();
    }
}
