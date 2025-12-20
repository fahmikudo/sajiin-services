package id.sajiin.sajiinservices.configuration.presentation.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GetListBizparRequest {

    private Integer page;
    private Integer size;
    private String search;

}
