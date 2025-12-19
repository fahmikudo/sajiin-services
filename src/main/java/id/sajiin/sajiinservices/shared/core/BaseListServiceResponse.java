package id.sajiin.sajiinservices.shared.core;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BaseListServiceResponse extends BaseServiceResponse {
    private Long totalRecord;
    private Integer totalPage;
    private Integer pageSize;
    private Integer currentPage;

}
