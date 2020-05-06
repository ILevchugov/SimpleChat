package ru.levchugov.chat.common;

import lombok.AllArgsConstructor;
import lombok.Getter;


import java.io.Serializable;
import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
public class Message implements Serializable {
    private static final String DEFAULT_BODY = "System message";
    private static final String DEFAULT_AUTHOR = "System";

    private ZonedDateTime time;
    private String author = DEFAULT_AUTHOR;
    private String body = DEFAULT_BODY;
    private MessageType messageType;

    public Message(String author, String body, MessageType messageType) {
        this.author = author;
        this.body = body;
        this.messageType = messageType;
    }

    public Message(MessageType messageType) {
        this.messageType = messageType;
    }

    public Message(String author, MessageType messageType) {
        this.author = author;
        this.messageType = messageType;
    }
}
