package ru.levchugov.chat.client.view;

import ru.levchugov.chat.client.controller.ChatController;

import javax.swing.*;
import java.awt.*;

public class ConnectionView {
    private static final int FRAME_WIDTH = 300;
    private static final int FRAME_HEIGHT = 150;

    private static final int TEXT_FIELD_WIDTH = 100;
    private static final int TEXT_FIELD_HEIGHT = 20;


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

        connectionFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        connectionFrame.setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        connectionFrame.setLocationRelativeTo(null);
        connectionFrame.setResizable(false);
    }

    private void init() {
        connectionFrame.setLayout(new GridBagLayout());
        constraints.fill = GridBagConstraints.HORIZONTAL;

        setGridItem(serverHost, 0, 0);

        serverHostText.addKeyListener(new EnterKeyListenerForFocusOnTextField(serverPortText));
        serverHostText.setPreferredSize(new Dimension(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT));
        setGridItem(serverHostText, 1, 0);

        setGridItem(serverPort, 0, 1);

        serverPortText.addKeyListener(new EnterKeyListenerForClickButton(connectServerButton));
        serverPortText.setPreferredSize(new Dimension(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT));
        setGridItem(serverPortText, 1, 1);

        setButtonListener();
        setGridItem(connectServerButton, 1, 2);
    }

    private void setButtonListener() {
        connectServerButton.addActionListener(actionEvent -> {
            String serverAddress = serverHostText.getText();
            try {
                int serverPort = Integer.parseInt(serverPortText.getText());
                chatController.connect(serverAddress, serverPort);
            } catch (NumberFormatException e) {
                showErrorMessage("Port has to be number");
            }
        });
    }

    private void setGridItem(Component component, int horizontalPositionOrder, int verticalPositionOrder) {
        constraints.gridx = horizontalPositionOrder;
        constraints.gridy = verticalPositionOrder;
        connectionFrame.add(component, constraints);
    }

    //написать другие слова
     void showErrorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(connectionFrame, errorMessage,
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    void setVisible(boolean condition) {
        connectionFrame.setVisible(condition);
    }

}
