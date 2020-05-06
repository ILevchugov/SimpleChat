package ru.levchugov.chat.client.view;

public interface ChatView {

    void showNewMessage(String message);

    void unblockButton();

    void addNewUser(String userName);

    void addOldUser(String userName);

    void removeUser(String userName);

    void showConnectionOverflowError();

    void showConnectionViewOnly();

    void showAuthorizationViewOnly();

    void showMainChatViewOnly();

    void showConnectionError();

    void showAuthorizationError();

    void clear();
}
