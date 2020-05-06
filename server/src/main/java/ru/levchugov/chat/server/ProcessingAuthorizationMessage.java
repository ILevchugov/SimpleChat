package ru.levchugov.chat.server;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.levchugov.chat.common.Message;
import ru.levchugov.chat.common.MessageType;
import ru.levchugov.chat.common.ProcessingMessageStrategy;

@AllArgsConstructor
@Slf4j
public class ProcessingAuthorizationMessage implements ProcessingMessageStrategy {
    private final Server server;
    private final ClientHandler clientHandler;

    @Override
    public void processMessage(Message message) {
            log.info("Авторизация пользователя {}", message.getAuthor());
            if (!server.isUserAuthorized(message.getAuthor())) {
                server.authorizeUser(message.getAuthor(), clientHandler);
                clientHandler.setUserName(message.getAuthor());
                clientHandler.sendUsersSetToNewUser();
                clientHandler.sendMessage(new Message(message.getAuthor(), message.getBody(), MessageType.AUTHORIZATION_APPROVED));
                server.sendMessageToAll(new Message(message.getAuthor(), message.getBody(), MessageType.NEW_USER));
            } else {
                clientHandler.sendMessage(new Message(MessageType.AUTHORIZATION_DECLINED));
                log.warn("Пользотваль {} уже зарегистрирован", message.getAuthor());
            }
    }
}
