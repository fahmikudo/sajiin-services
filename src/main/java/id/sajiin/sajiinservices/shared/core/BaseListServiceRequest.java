package id.sajiin.sajiinservices.shared.core;

public class BaseListServiceRequest extends BaseServiceRequest {
    private Boolean isPaginated;
    private PageEntityRequest pagination;

    public Boolean getPaginated() {
        return isPaginated;
    }

    public void setPaginated(Boolean paginated) {
        isPaginated = paginated;
    }

    public PageEntityRequest getPagination() {
        return pagination;
    }

    public void setPagination(PageEntityRequest pagination) {
        this.pagination = pagination;
    }
}
