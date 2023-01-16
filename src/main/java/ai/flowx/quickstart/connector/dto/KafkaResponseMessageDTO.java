package ai.flowx.quickstart.connector.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class KafkaResponseMessageDTO {
    //private String fromCurrency;
   // private String toCurrency;
   // private Double initialAmount;
   // private Double exchangedAmount;
    private String errorMessage;
    private String accessToken;
}
