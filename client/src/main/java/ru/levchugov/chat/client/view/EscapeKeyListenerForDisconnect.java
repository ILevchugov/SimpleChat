package ru.levchugov.chat.client.view;

import ru.levchugov.chat.client.controller.ChatController;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class EscapeKeyListenerForDisconnect extends KeyAdapter {
    private final ChatController chatController;

    EscapeKeyListenerForDisconnect(ChatController chatController) {
        this.chatController = chatController;
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
            keyEvent.consume();
            chatController.disconnect();
        }
    }
}
