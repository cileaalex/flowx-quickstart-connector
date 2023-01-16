package ai.flowx.quickstart.connector.service.impl;

import ai.flowx.commons.trace.aop.Trace;
import ai.flowx.quickstart.connector.dto.BaseApiResponseDTO;
import ai.flowx.quickstart.connector.service.ApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Trace
@Slf4j
@Service
@RequiredArgsConstructor
public class ApiServiceImpl implements ApiService {

    @Override
    public BaseApiResponseDTO blockingCall(Class<? extends BaseApiResponseDTO> responseClass, String scheme, String host, String path, Object... pathParams) throws WebClientException {
        WebClient client = WebClient.create();

        URI uri = UriComponentsBuilder.newInstance()
                .scheme(scheme)
                .host(host)
                .path(path)
                .buildAndExpand(pathParams)
                .toUri();

        WebClient.ResponseSpec responseSpec = client.get()
                .uri(uri)
                .retrieve();

        return responseSpec.bodyToMono(responseClass).block();
    }
}
