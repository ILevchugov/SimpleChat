package ru.levchugov.chat.client.model;

import lombok.AllArgsConstructor;
import ru.levchugov.chat.common.Message;
import ru.levchugov.chat.common.ProcessingMessageStrategy;

import java.time.format.DateTimeFormatter;

@AllArgsConstructor
public class ProcessingChatMessage implements ProcessingMessageStrategy {
    private static final String DATA_TIME_FORMAT ="HH:mm";
    private final Client client;

    @Override
    public void processMessage(Message message) {
        String formattedDateTime = DateTimeFormatter.ofPattern(DATA_TIME_FORMAT).format(message.getTime());
        client.addNewMessage(formattedDateTime + " <"+ message.getAuthor() + "> : " + message.getBody());
    }
}
