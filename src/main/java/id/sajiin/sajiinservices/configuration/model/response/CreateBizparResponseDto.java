package id.sajiin.sajiinservices.configuration.model.response;

import id.sajiin.sajiinservices.configuration.model.dto.BizparDto;
import id.sajiin.sajiinservices.shared.core.BaseServiceResponse;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateBizparResponseDto extends BaseServiceResponse {
    private BizparDto bizpar;
}
