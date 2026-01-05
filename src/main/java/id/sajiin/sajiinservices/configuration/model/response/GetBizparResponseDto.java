package id.sajiin.sajiinservices.configuration.model.response;

import id.sajiin.sajiinservices.configuration.model.dto.BizparDto;
import id.sajiin.sajiinservices.shared.core.BaseServiceResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetBizparResponseDto extends BaseServiceResponse {

    private BizparDto bizpar;

}
