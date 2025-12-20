package id.sajiin.sajiinservices.shared.presentation;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonPropertyOrder({"success", "message", "pagination", "data"})
public class BaseListResponse <T> {

    private boolean success;

    private String message;

    private PaginationResponse pagination;

    private T data;

}
