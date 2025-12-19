package id.sajiin.sajiinservices.shared.model;

import lombok.Getter;

@Getter
public enum ActiveStatus {

    ACTIVE("active"),
    INACTIVE("inactive");

    private final String status;

    ActiveStatus(String status) {
        this.status = status;
    }

}
