package id.sajiin.sajiinservices.shared.core;

public abstract class BaseServiceRequest {
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
