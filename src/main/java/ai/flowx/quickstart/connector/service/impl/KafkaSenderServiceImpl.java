package ai.flowx.quickstart.connector.service.impl;

import ai.flowx.quickstart.connector.service.MessageSenderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static ai.flowx.commons.trace.service.KafkaUtils.headersToList;

@Slf4j
@Component
public class KafkaSenderServiceImpl implements MessageSenderService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String responseTopic;

    public KafkaSenderServiceImpl(
            final KafkaTemplate<String, Object> kafkaTemplate,
            final @Value("${kafka.topic.out}") String responseTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.responseTopic = responseTopic;
    }

    @Override
    public void sendMessage(Headers headers, String key, Object payload) {
        ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(responseTopic, key, payload);
        populateHeaders(producerRecord, headers);

        kafkaTemplate.send(producerRecord);

        log.info("Sent message {} with headers {}", producerRecord.value(), headersToList(producerRecord.headers()));
    }

    private void populateHeaders(ProducerRecord<String, Object> producerRecord,
                                 Headers headers) {
        if (headers != null) {
            headers.forEach(header -> producerRecord.headers().add(header));
        }
    }
}
