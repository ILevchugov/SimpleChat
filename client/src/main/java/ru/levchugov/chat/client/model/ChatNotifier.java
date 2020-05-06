package ru.levchugov.chat.client.model;

import ru.levchugov.chat.client.view.ChatView;

import java.util.ArrayList;
import java.util.List;

public class ChatNotifier {
    private final List<ChatView> chatViews = new ArrayList<>();

    void attachView(ChatView view) {
        chatViews.add(view);
    }

    void notifyViewShowMainChatView() {
        chatViews.forEach(ChatView::showMainChatViewOnly);
    }

    void notifyViewShowNewMessage(String message) {
        chatViews.forEach(chatView -> chatView.showNewMessage(message));
    }

    void notifyViewUnblockButton() {
        chatViews.forEach(ChatView::unblockButton);
    }

    void notifyViewAboutNewUser(String userName) {
        chatViews.forEach(chatView -> chatView.addNewUser(userName));
    }

    void notifyViewAboutOldUser(String userName) {
        chatViews.forEach(chatView -> chatView.addOldUser(userName));
    }

    void notifyViewRemoveUser(String userName) {
        chatViews.forEach(chatView -> chatView.removeUser(userName));
    }

    void notifyViewShowConnectionView() {
        chatViews.forEach(ChatView::showConnectionViewOnly);
    }

    void notifyViewShowAuthorizationView() {
        chatViews.forEach(ChatView::showAuthorizationViewOnly);
    }

    void notifyViewShowConnectionError() {
        chatViews.forEach(ChatView::showConnectionError);
    }

    void notifyViewShowConnectionOverflowError() {
        chatViews.forEach(ChatView::showConnectionOverflowError);
    }

    void notifyViewShowAuthorizationError() {
        chatViews.forEach(ChatView::showAuthorizationError);
    }

    void notifyViewClear() {
        chatViews.forEach(ChatView::clear);
    }
}
