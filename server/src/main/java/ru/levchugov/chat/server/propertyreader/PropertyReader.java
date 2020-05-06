package ru.levchugov.chat.server.propertyreader;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

@Getter
@Slf4j
public class PropertyReader {
    private static final String PROPERTY_PATH = "/server.properties";
    private static final String SERVER_PORT = "server.port";
    private static final String SERVER_THREADS_NUM = "server.threads_num";

    private int serverPort;
    private int serverNumThreads;

    public PropertyReader() {
        readProperty();
    }

    private void readProperty() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader
                (getClass().getResourceAsStream(PROPERTY_PATH)))) {
            Properties properties = new Properties();
            properties.load(reader);
            serverPort = Integer.parseInt(properties.getProperty(SERVER_PORT));
            serverNumThreads = Integer.parseInt(properties.getProperty(SERVER_THREADS_NUM, String.valueOf(0)));

            if (serverPort < 0 || serverPort > 65535) {
                throw new IllegalArgumentException("Wrong server port");
            }

        } catch (IOException e) {
            log.error("Ошибка чтения конфигурационного файла", e);
        }
    }
}
