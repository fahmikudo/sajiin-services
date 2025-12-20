package id.sajiin.sajiinservices.shared.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PageEntityRequest {

    private Integer page;
    private Integer size;

}
