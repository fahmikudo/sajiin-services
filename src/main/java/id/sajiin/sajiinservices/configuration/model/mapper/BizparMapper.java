package id.sajiin.sajiinservices.configuration.model.mapper;

import id.sajiin.sajiinservices.configuration.domain.Bizpar;
import id.sajiin.sajiinservices.configuration.model.dto.BizparDto;
import id.sajiin.sajiinservices.configuration.presentation.response.BizparResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BizparMapper {

    BizparDto toDto(Bizpar bizpar);

    Bizpar toEntity(BizparDto bizparDto);

    BizparResponse toResponse(BizparDto bizparDto);

}
