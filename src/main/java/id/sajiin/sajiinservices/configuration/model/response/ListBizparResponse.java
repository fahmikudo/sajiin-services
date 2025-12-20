package id.sajiin.sajiinservices.configuration.model.response;

import id.sajiin.sajiinservices.configuration.model.dto.BizparDto;
import id.sajiin.sajiinservices.shared.core.BaseListServiceResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListBizparResponse extends BaseListServiceResponse {

    private List<BizparDto> bizpars;

}
