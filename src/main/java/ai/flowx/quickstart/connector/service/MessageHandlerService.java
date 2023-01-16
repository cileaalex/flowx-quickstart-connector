package ai.flowx.quickstart.connector.service;

import ai.flowx.quickstart.connector.dto.KafkaRequestMessageDTO;
import org.apache.kafka.common.header.Headers;

public interface MessageHandlerService {
    void process(KafkaRequestMessageDTO kafkaMessage, Headers headers);
}
