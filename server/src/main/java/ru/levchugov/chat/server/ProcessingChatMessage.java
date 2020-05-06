package ru.levchugov.chat.server;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.levchugov.chat.common.Message;
import ru.levchugov.chat.common.ProcessingMessageStrategy;

@AllArgsConstructor
@Slf4j
public class ProcessingChatMessage implements ProcessingMessageStrategy {
    private final Server server;

    @Override
    public void processMessage(Message message) {
        log.info("Отправка сообщения от пользователя {}", message.getAuthor());
        if (server.isUserAuthorized(message.getAuthor())) {
            server.sendMessageToAll(message);
        } else {
            log.warn("Отправка невозможна так как пользователь не авторизован");
        }
    }
}
