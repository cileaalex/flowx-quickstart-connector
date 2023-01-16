package ai.flowx.quickstart.connector.service;

import org.apache.kafka.common.header.Headers;

public interface MessageSenderService {
    void sendMessage(Headers headers, String key, Object payload);
}
