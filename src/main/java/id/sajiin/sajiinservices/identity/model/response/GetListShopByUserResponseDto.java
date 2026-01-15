package id.sajiin.sajiinservices.identity.model.response;

import id.sajiin.sajiinservices.shared.core.BaseServiceResponse;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GetListShopByUserResponseDto extends BaseServiceResponse {

    private Set<Long> shopIds;

}
