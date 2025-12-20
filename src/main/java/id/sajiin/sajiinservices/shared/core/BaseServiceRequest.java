package id.sajiin.sajiinservices.shared.core;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class BaseServiceRequest {
    private Long userId;
    private Long shopId;

}
