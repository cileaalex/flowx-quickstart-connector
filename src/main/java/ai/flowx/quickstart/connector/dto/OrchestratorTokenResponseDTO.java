package ai.flowx.quickstart.connector.dto;

import lombok.Data;

@Data
public class OrchestratorTokenResponseDTO implements BaseApiResponseDTO {
    private String accessToken;
    private String idToken;
    private String scope;
    private int expiresIn;
    private String tokenType;

    //private String base_code;
   // private String target_code;
   // private double conversion_rate;
   // private double conversion_result;
}
