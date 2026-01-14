package id.sajiin.sajiinservices.configuration.model.request;

import id.sajiin.sajiinservices.shared.core.BaseServiceRequest;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateBizparRequestDto extends BaseServiceRequest {

    private String key;

    private String value;

    private String type;

    private String description;


}
