package ai.flowx.quickstart.connector.service.impl;

import ai.flowx.commons.trace.aop.Trace;
import ai.flowx.commons.trace.service.KafkaUtils;
import ai.flowx.quickstart.connector.dto.OrchestratorTokenResponseDTO;
import ai.flowx.quickstart.connector.dto.KafkaRequestMessageDTO;
import ai.flowx.quickstart.connector.dto.KafkaResponseMessageDTO;
import ai.flowx.quickstart.connector.exception.ExchangeException;
import ai.flowx.quickstart.connector.service.ApiService;
import ai.flowx.quickstart.connector.service.MessageHandlerService;
import ai.flowx.quickstart.connector.service.MessageSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.Headers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientException;

@Trace
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageHmandlerServiceIpl implements MessageHandlerService {

    private final MessageSenderService messageSenderService;
    private final ApiService apiService;

    @Value("${orchestratorgettoken-api.scheme}")
    private String scheme;

    @Value("${orchestratorgettoken-api.host}")
    private String host;

    @Value("${orchestratorgettoken-api.path}")
    private String path;

    @Value("${orchestratorgettoken-api.key}")
    private String apiKey;

    @Override
    public void process(KafkaRequestMessageDTO kafkaMessage, Headers headers) {
        String processInstanceUuid = KafkaUtils.extractHeaderString(headers, "processInstanceUuid");

        KafkaResponseMessageDTO.KafkaResponseMessageDTOBuilder responseMessageDTOBuilder = KafkaResponseMessageDTO.builder();

        try {
            OrchestratorTokenResponseDTO responseBody = (OrchestratorTokenResponseDTO) apiService.blockingCall(OrchestratorTokenResponseDTO.class, scheme, host, path, kafkaMessage.getClientId().toString(), kafkaMessage.getGrantType(), kafkaMessage.getRefreshToken().toString());
            //?????????
            responseMessageDTOBuilder.accessToken()
                    .fromCurrency(kafkaMessage.getClientId())
                    .initialAmount(kafkaMessage.getGrantType());

        } catch (WebClientException exc) {
            responseMessageDTOBuilder.errorMessage(exc.getMessage());
            messageSenderService.sendMessage(headers, processInstanceUuid, responseMessageDTOBuilder.build());
            throw new ExchangeException(exc.getMessage());
        }

        messageSenderService.sendMessage(headers, processInstanceUuid, responseMessageDTOBuilder.build());
    }
}
