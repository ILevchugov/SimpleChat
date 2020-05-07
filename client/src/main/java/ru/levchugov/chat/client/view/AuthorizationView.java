package ru.levchugov.chat.client.view;

import ru.levchugov.chat.client.controller.ChatController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AuthorizationView {
    private static final int FRAME_WIDTH = 300;
    private static final int FRAME_HEIGHT = 150;

    private static final int TEXT_FIELD_WIDTH = 150;
    private static final int TEXT_FIELD_HEIGHT = 20;

    private static final int MIN_NAME_SIZE = 3;
    private static final int MAX_NAME_SIZE = 20;

    private static final String USER_NAME_PATTERN = "^[A-Za-z0-9_-]{3,20}$";

    private final JFrame authorizationFrame;

    private final JLabel nameLabel;
    private final JTextField nameText;
    private final JButton authorizationButton;

    private final GridBagConstraints constraints;

    private final ChatController chatController;

    AuthorizationView(ChatController chatController) {
        this.authorizationFrame = new JFrame("Enter name");
        this.nameLabel = new JLabel("Your name");
        this.nameText = new JTextField();
        this.authorizationButton = new JButton("login");

        this.chatController = chatController;
        this.constraints = new GridBagConstraints();

        init();

        authorizationFrame.setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        authorizationFrame.setResizable(false);
        authorizationFrame.setLocationRelativeTo(null);
    }

    private void init() {
        authorizationFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                chatController.disconnect();
                authorizationFrame.dispose();
                System.exit(0);
            }
        });
        authorizationFrame.setLayout(new GridBagLayout());
        constraints.fill = GridBagConstraints.HORIZONTAL;

        setGridItem(nameLabel, 0, 0);

        setGridItem(nameText, 1, 0);

        nameText.setDocument(new TextFieldLimit(MAX_NAME_SIZE));
        nameText.setPreferredSize(new Dimension(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT));
        nameText.addKeyListener(new EnterKeyListenerForClickButton(authorizationButton));

        setButtonListener();
        setGridItem(authorizationButton, 1, 1);
    }

    private void setButtonListener() {
        authorizationButton.addActionListener(actionEvent -> {
            String userName = nameText.getText();
            if (userName.matches(USER_NAME_PATTERN)) {
                chatController.authorize(userName);
            } else {
                showErrorMessage("Not valid username" + System.lineSeparator() +
                        "the username may contain letters, numbers, hyphens, and underscores" + System.lineSeparator() +
                        "from " + MIN_NAME_SIZE + " to " + MAX_NAME_SIZE + " symbols");
            }
        });
    }

    private void setGridItem(Component component, int horizontalPositionOrder, int verticalPositionOrder) {
        constraints.gridx = horizontalPositionOrder;
        constraints.gridy = verticalPositionOrder;
        authorizationFrame.add(component, constraints);
    }

    void setVisible(boolean condition) {
        authorizationFrame.setVisible(condition);
    }

    void showErrorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(authorizationFrame, errorMessage,
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
