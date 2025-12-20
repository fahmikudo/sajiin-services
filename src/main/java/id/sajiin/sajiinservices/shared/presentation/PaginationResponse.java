package id.sajiin.sajiinservices.shared.presentation;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaginationResponse {

    private int page;
    private int size;
    private long totalItems;
    private int totalPages;

}
