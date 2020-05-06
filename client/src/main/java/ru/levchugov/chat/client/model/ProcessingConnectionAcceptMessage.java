package ru.levchugov.chat.client.model;

import lombok.AllArgsConstructor;
import ru.levchugov.chat.common.Message;
import ru.levchugov.chat.common.ProcessingMessageStrategy;

@AllArgsConstructor
public class ProcessingConnectionAcceptMessage implements ProcessingMessageStrategy {
    private final Client client;

    @Override
    public void processMessage(Message message) {
        client.processAcceptConnection();
    }
}
