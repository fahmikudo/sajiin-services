package id.sajiin.sajiinservices.identity.model.request;

import id.sajiin.sajiinservices.shared.core.BaseServiceRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthLoginRequest extends BaseServiceRequest {

    private String username;
    private String password;

}
