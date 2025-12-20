package id.sajiin.sajiinservices.security;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class UserContext {

    private Long userId;

    private Set<Long> shopIds;

}
