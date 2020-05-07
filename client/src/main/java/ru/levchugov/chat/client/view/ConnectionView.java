package ru.levchugov.chat.client.view;

import ru.levchugov.chat.client.controller.ChatController;

import javax.swing.*;
import java.awt.*;

public class ConnectionView {
    private static final int FRAME_WIDTH = 300;
    private static final int FRAME_HEIGHT = 150;

    private static final int TEXT_FIELD_WIDTH = 100;
    private static final int TEXT_FIELD_HEIGHT = 20;

    private static final int MAX_PORT_SIZE = 5;
    private static final int MAX_HOST_SIZE = 15;

    private static final String HOST_REGEXP = "^(?:(?:25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(?:25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";
    private static final String PORT_REGEXP = "^[0-9]{1,15}$";

    private final JFrame connectionFrame;
    private final JLabel serverHost;
    private final JTextField serverHostText;
    private final JLabel serverPort;
    private final JTextField serverPortText;
    private final JButton connectServerButton;

    private final GridBagConstraints constraints;

    private final ChatController chatController;

    ConnectionView(ChatController chatController) {
        this.connectionFrame = new JFrame("Set connection");
        this.chatController = chatController;
        this.serverHost = new JLabel("Server address");
        this.serverHostText = new JTextField();
        this.serverPort = new JLabel("Server port");
        this.serverPortText = new JTextField();
        this.connectServerButton = new JButton("Connect");

        this.constraints = new GridBagConstraints();

        init();
    }

    private void init() {
        connectionFrame.setLayout(new GridBagLayout());
        constraints.fill = GridBagConstraints.HORIZONTAL;

        setGridItem(serverHost, 0, 0);

        serverHostText.setDocument(new TextFieldLimit(MAX_HOST_SIZE));
        serverHostText.addKeyListener(new EnterKeyListenerForFocusOnTextField(serverPortText));
        serverHostText.setPreferredSize(new Dimension(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT));
        setGridItem(serverHostText, 1, 0);

        setGridItem(serverPort, 0, 1);

        serverPortText.addKeyListener(new EnterKeyListenerForClickButton(connectServerButton));
        serverPortText.setPreferredSize(new Dimension(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT));
        serverPortText.setDocument(new TextFieldLimit(MAX_PORT_SIZE));
        setGridItem(serverPortText, 1, 1);

        setButtonListener();
        setGridItem(connectServerButton, 1, 2);

        connectionFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        connectionFrame.setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        connectionFrame.setLocationRelativeTo(null);
        connectionFrame.setResizable(false);
    }

    private void setButtonListener() {
        connectServerButton.addActionListener(actionEvent -> {
            String serverHost = serverHostText.getText();
            String serverPort = serverPortText.getText();
            if (isValid(serverHost, serverPort)) {
                try {
                    chatController.connect(serverHost, Integer.parseInt(serverPort));
                } catch (NumberFormatException e) {
                    showErrorMessage("Port has to be number");
                }
            } else {
                showErrorMessage("Wrong host or port ");
            }
        });

    }

    private boolean isValid(String serverHost, String serverPort) {
        return (serverHost.matches(HOST_REGEXP) || serverHost.equals("localhost")) && serverPort.matches(PORT_REGEXP);
    }

    private void setGridItem(Component component, int horizontalPositionOrder, int verticalPositionOrder) {
        constraints.gridx = horizontalPositionOrder;
        constraints.gridy = verticalPositionOrder;
        connectionFrame.add(component, constraints);
    }

     void showErrorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(connectionFrame, errorMessage,
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    void setVisible(boolean condition) {
        connectionFrame.setVisible(condition);
    }

}
