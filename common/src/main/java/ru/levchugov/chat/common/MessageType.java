package ru.levchugov.chat.common;

public enum MessageType {
    //Msg from client
    CHAT_MESSAGE,
    AUTHORIZATION,
    DISCONNECT,
    //Msg from server
    AUTHORIZATION_APPROVED,
    AUTHORIZATION_DECLINED,
    NEW_USER,
    OLD_USER,
    REMOVE_USER,
    CONNECTION_ACCEPT,
    CONNECTION_DECLINED
}
