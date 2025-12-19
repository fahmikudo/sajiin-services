package id.sajiin.sajiinservices.shared.core;

import lombok.Getter;
import lombok.Setter;

public class BaseListServiceRequest extends BaseServiceRequest {
    private Boolean isPaginated = Boolean.FALSE;

    @Setter
    @Getter
    private PageEntityRequest pagination;

    public Boolean getPaginated() {
        return isPaginated;
    }

    public void setPaginated(Boolean paginated) {
        isPaginated = paginated;
    }

}
