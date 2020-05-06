package ru.levchugov.chat.server;

import lombok.extern.slf4j.Slf4j;
import ru.levchugov.chat.server.propertyreader.PropertyReader;

@Slf4j
public class Runner {
    public static void main(String[] args) {
        try {
            PropertyReader propertyReader = new PropertyReader();
            if (propertyReader.getServerNumThreads() <= 0) {
                Server server = new Server(propertyReader.getServerPort());
                server.run();
            } else {
                Server server = new Server(propertyReader.getServerPort(), propertyReader.getServerNumThreads());
                server.run();
            }
        } catch (IllegalArgumentException e) {
            log.error("",e);
        }
    }
}
