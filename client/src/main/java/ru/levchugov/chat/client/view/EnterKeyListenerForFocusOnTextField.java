package ru.levchugov.chat.client.view;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class EnterKeyListenerForFocusOnTextField extends KeyAdapter {
    private final JTextField textField;

    EnterKeyListenerForFocusOnTextField(JTextField textField) {
        this.textField = textField;
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
            keyEvent.consume();
            textField.requestFocusInWindow();
        }
    }
}
