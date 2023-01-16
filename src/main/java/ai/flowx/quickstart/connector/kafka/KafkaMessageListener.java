package ai.flowx.quickstart.connector.kafka;

import ai.flowx.commons.trace.aop.Trace;
import ai.flowx.quickstart.connector.dto.KafkaRequestMessageDTO;
import ai.flowx.quickstart.connector.service.MessageHandlerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaMessageListener {

    private final MessageHandlerService messageHandlerService;

    private final ObjectMapper objectMapper;

    @Trace
    @KafkaListener(topicPattern = "${kafka.topic.in}", containerFactory = "listenerContainerFactory")
    public void consume(ConsumerRecord<String, String> record) {
        log.info("Received new message from kafka {}.", record.value());

        KafkaRequestMessageDTO dto = null;
        try {
            dto = objectMapper.readValue(record.value(), KafkaRequestMessageDTO.class);
        } catch (JsonProcessingException e) {
            log.error("There was an error when processing received message {}", e.getMessage());
        }

        if (dto == null) {
            log.error("Message invalid: {}", record.value());
            return;
        }

        messageHandlerService.process(dto, record.headers());
    }
}
