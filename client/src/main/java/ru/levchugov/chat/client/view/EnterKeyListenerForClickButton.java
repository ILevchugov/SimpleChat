package ru.levchugov.chat.client.view;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class EnterKeyListenerForClickButton extends KeyAdapter {
    private final JButton button;

    EnterKeyListenerForClickButton(JButton button) {
        this.button = button;
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
            keyEvent.consume();
            button.doClick();
        }
    }
}
