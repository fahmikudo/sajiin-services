package id.sajiin.sajiinservices.identity.model.response;

import id.sajiin.sajiinservices.shared.core.BaseServiceResponse;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RefreshTokenResponseDto extends BaseServiceResponse {

    private String accessToken;
    private String refreshToken;
    private Long expiredAt;

}
