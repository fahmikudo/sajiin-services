package id.sajiin.sajiinservices.shared.core;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseEntityRequest {

    private int pageNumber;
    private int pageSize;
    private String sortBy;
    private String orderBy;

}
