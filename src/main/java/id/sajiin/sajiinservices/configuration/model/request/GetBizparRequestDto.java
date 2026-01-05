package id.sajiin.sajiinservices.configuration.model.request;

import id.sajiin.sajiinservices.shared.core.BaseServiceRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetBizparRequestDto extends BaseServiceRequest {

    private Long id;

}
