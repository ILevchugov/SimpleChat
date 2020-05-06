package ru.levchugov.chat.server;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.levchugov.chat.common.Message;
import ru.levchugov.chat.common.ProcessingMessageStrategy;

@Slf4j
@AllArgsConstructor
public class ProcessingDisconnectMessage implements ProcessingMessageStrategy {
    private final Server server;
    private final ClientHandler clientHandler;

    @Override
    public void processMessage(Message message) {
        log.info("Получен запрос на отключение пользователя {}", message.getAuthor());
        server.removeUser(message.getAuthor());
        clientHandler.closeStreams();
        Thread.currentThread().interrupt();
    }
}
