package id.sajiin.sajiinservices.configuration.presentation.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateBizparRequest {

    @NotEmpty
    private String key;

    @NotEmpty
    private String value;

    private String type;

    private String description;
}
