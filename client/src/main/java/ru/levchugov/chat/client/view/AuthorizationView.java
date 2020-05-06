package ru.levchugov.chat.client.view;

import ru.levchugov.chat.client.controller.ChatController;

import javax.swing.*;
import java.awt.*;

public class AuthorizationView {
    private static final int FRAME_WIDTH = 250;
    private static final int FRAME_HEIGHT = 150;

    private final JFrame authorizationFrame;

    private final JLabel nameLabel;
    private final JTextField nameText;
    private final JButton authorizationButton;

    private final GridBagConstraints constraints;

    private final ChatController chatController;

    AuthorizationView(ChatController chatController) {
        this.authorizationFrame = new JFrame();
        this.nameLabel = new JLabel("Your name");
        this.nameText = new JTextField();
        this.authorizationButton = new JButton("login");

        this.chatController = chatController;
        this.constraints = new GridBagConstraints();

        init();

        authorizationFrame.setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        authorizationFrame.setResizable(false);
        authorizationFrame.setLocationRelativeTo(null);

        authorizationFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void init() {
        authorizationFrame.setLayout(new GridBagLayout());
        constraints.fill = GridBagConstraints.HORIZONTAL;

        setGridItem(nameLabel, 0, 0);

        setGridItem(nameText, 1, 0);

        nameText.addKeyListener(new EnterKeyListenerForClickButton(authorizationButton));

        setButtonListener();
        setGridItem(authorizationButton, 1, 1);
    }

    private void setButtonListener() {
        authorizationButton.addActionListener(actionEvent -> {
            String userName = nameText.getText();
            if (userName.contains(" ") || userName.isEmpty()) {
                showErrorMessage("Name can't contains ' '");
            } else {
                chatController.authorize(userName);
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
