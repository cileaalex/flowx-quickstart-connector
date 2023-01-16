package ai.flowx.quickstart.connector.service;

import ai.flowx.quickstart.connector.dto.BaseApiResponseDTO;

public interface ApiService {
    BaseApiResponseDTO blockingCall(Class<? extends BaseApiResponseDTO> responseClass, String scheme, String host, String path, Object... pathParams);
}
