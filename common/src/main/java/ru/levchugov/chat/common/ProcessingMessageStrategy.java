package ru.levchugov.chat.common;

public interface ProcessingMessageStrategy {
    void processMessage(Message message);
}
