package id.sajiin.sajiinservices.shared.presentation;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonPropertyOrder({"success", "message", "data"})
public class BaseResponse <T> {

    private boolean success;

    private String message;

    private T data;

    public BaseResponse() {
    }

    public BaseResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

}
