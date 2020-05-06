package ru.levchugov.chat.client.view;

import ru.levchugov.chat.client.controller.ChatController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChatSwingView implements ChatView{
    private static final int CHAT_SCROLL_PANE_WIDTH = 400;
    private static final int CHAT_SCROLL_PANE_HEIGHT = 600;

    private static final int USERS_PANE_WIDTH = 150;
    private static final int USERS_SCROLL_PANE_HEIGHT = 600;

    private static final int SEND_MESSAGE_TEXT_AREA_WIDTH = 400;
    private static final int SEND_MESSAGE_TEXT_AREA_HEIGHT = 20;

    private static final int SEND_MESSAGE_BUTTON_WIDTH = 100;
    private static final int SEND_MESSAGE_BUTTON_HEIGHT = 20;

    private final JFrame mainFrame;
    private final JMenuBar menuBar;
    private final JScrollPane chatTextScrollPane;
    private final JTextArea chatText;

    private final JScrollPane usersPane;

    private final DefaultListModel<String> usersListModel;
    private final JList<String> usersList;

    private final JButton sendMessageButton;
    private final JTextField sendMessageTextField;

    private final  GridBagConstraints constraints;

    private final ChatController chatController;

    private final AuthorizationView authorizationView;
    private final ConnectionView connectionView;

    public ChatSwingView(ChatController chatController) {
        this.mainFrame = new JFrame("Chat");
        this.chatController = chatController;
        this.connectionView = new ConnectionView(chatController);
        this.authorizationView = new AuthorizationView(chatController);
        this.menuBar = new JMenuBar();
        this.chatTextScrollPane = new JScrollPane();
        this.chatText = new JTextArea();
        this.sendMessageButton = new JButton("send");
        this.sendMessageTextField= new JTextField();

        this.constraints = new GridBagConstraints();

        this.usersPane = new JScrollPane();
        this.usersListModel = new DefaultListModel<>();
        this.usersList = new JList<>(usersListModel);

        init();

        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
    }

    private void init() {
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                chatController.disconnect();
                mainFrame.dispose();
                System.exit(0);
            }
        });

        chatText.setLineWrap(true);
        chatText.setFont(chatText.getFont().deriveFont(14f));
        chatText.setEditable(false);
        chatTextScrollPane.setViewportView(chatText);
        usersPane.setViewportView(usersList);
        initBar();


        mainFrame.setJMenuBar(menuBar);
        mainFrame.setLayout(new GridBagLayout());

        constraints.fill = GridBagConstraints.HORIZONTAL;

        chatTextScrollPane.setPreferredSize(new Dimension(CHAT_SCROLL_PANE_WIDTH, CHAT_SCROLL_PANE_HEIGHT));
        setGridItem(chatTextScrollPane, 0, 0);

        usersPane.setPreferredSize(new Dimension(USERS_PANE_WIDTH, USERS_SCROLL_PANE_HEIGHT));
        setGridItem(usersPane, 1, 0);


        sendMessageTextField.setPreferredSize(new Dimension(SEND_MESSAGE_TEXT_AREA_WIDTH, SEND_MESSAGE_TEXT_AREA_HEIGHT));
        sendMessageTextField.addKeyListener(new EnterKeyListenerForClickButton(sendMessageButton));
        sendMessageTextField.addKeyListener(new EscapeKeyListenerForDisconnect(chatController));
        setGridItem(sendMessageTextField, 0, 1);

        setButtonListener();
        sendMessageButton.setEnabled(false);
        sendMessageButton.setPreferredSize(new Dimension(SEND_MESSAGE_BUTTON_WIDTH, SEND_MESSAGE_BUTTON_HEIGHT));
        setGridItem(sendMessageButton, 1, 1);
    }

    private void initBar() {
        JMenu chatMenu = new JMenu("Chat menu");
        JMenuItem disconnect = new JMenuItem("disconnect");
        disconnect.addActionListener(actionEvent -> chatController.disconnect());
        chatMenu.add(disconnect);
        menuBar.add(chatMenu);

    }
    private void setGridItem(Component component, int horizontalPositionOrder, int verticalPositionOrder) {
        constraints.gridx = horizontalPositionOrder;
        constraints.gridy = verticalPositionOrder;
        mainFrame.add(component, constraints);
    }

    private void setButtonListener() {
        sendMessageButton.addActionListener(actionEvent -> {
            String text = sendMessageTextField.getText();
            chatController.sendChatMessage(text);
            sendMessageTextField.setText("");
        });
    }

    @Override
    public void showNewMessage(String message) {
        chatText.append(message + System.lineSeparator());
    }

    @Override
    public void unblockButton() {
        sendMessageButton.setEnabled(true);
    }

    @Override
    public void addNewUser(String userName) {
        usersListModel.addElement(userName);
        chatText.append("Welcome " + userName + "!!!" + System.lineSeparator());
    }

    @Override
    public void addOldUser(String userName) {
        usersListModel.addElement(userName);
    }

    @Override
    public void removeUser(String userName) {
        usersListModel.removeElement(userName);
        chatText.append("User " + userName + " left chat" + System.lineSeparator());
    }

    @Override
    public void showConnectionViewOnly() {
        mainFrame.setVisible(false);
        authorizationView.setVisible(false);
        connectionView.setVisible(true);
    }

    //названия кал
    @Override
    public void showAuthorizationViewOnly() {
        mainFrame.setVisible(false);
        connectionView.setVisible(false);
        authorizationView.setVisible(true);
    }

    @Override
    public void showMainChatViewOnly() {
        authorizationView.setVisible(false);
        mainFrame.setVisible(true);
        sendMessageTextField.requestFocusInWindow();
    }

    @Override
    public void showConnectionError() {
        connectionView.showErrorMessage("Some connection troubles. Try again.");
    }

    @Override
    public void showConnectionOverflowError() {connectionView.showErrorMessage("Server is full");}

    @Override
    public void showAuthorizationError() {
        authorizationView.showErrorMessage("The name is already taken");
    }

    @Override
    public void clear() {
        usersListModel.clear();
        chatText.setText(null);
    }
}
