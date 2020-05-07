package ru.levchugov.chat.client.model;

import lombok.AllArgsConstructor;
import ru.levchugov.chat.common.Message;
import ru.levchugov.chat.common.ProcessingMessageStrategy;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
public class ProcessingChatMessage implements ProcessingMessageStrategy {
    private static final String DATA_TIME_FORMAT = "HH:mm";
    private static final String LEFT_DELIMITER = "<";
    private static final String RIGHT_DELIMITER = ">: ";
    private final Client client;

    @Override
    public void processMessage(Message message) {
        String formattedDateTime = DateTimeFormatter.ofPattern(DATA_TIME_FORMAT).format(message.getTime().withZoneSameInstant(ZoneId.systemDefault()));
        client.addNewMessage(formattedDateTime + LEFT_DELIMITER + message.getAuthor() + RIGHT_DELIMITER + message.getBody());
    }
}
