package id.sajiin.sajiinservices.configuration.presentation.impl;

import id.sajiin.sajiinservices.configuration.model.dto.BizparDto;
import id.sajiin.sajiinservices.configuration.model.response.GetBizparResponseDto;
import id.sajiin.sajiinservices.configuration.presentation.GetBizparPresentation;
import id.sajiin.sajiinservices.configuration.presentation.response.BizparResponse;
import id.sajiin.sajiinservices.configuration.presentation.response.GetBizparResponse;
import id.sajiin.sajiinservices.shared.constant.MessageConstant;
import id.sajiin.sajiinservices.shared.exception.GeneralException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class GetBizparPresentationImpl implements GetBizparPresentation {
    @Override
    public GetBizparResponse present(GetBizparResponseDto serviceResponse) throws GeneralException {
        GetBizparResponse response = new GetBizparResponse();
        response.setSuccess(true);
        response.setMessage(MessageConstant.messageCode(HttpStatus.OK.value()));
        response.setData(constructDtoToResponse(serviceResponse.getBizpar()));
        return response;
    }

    public BizparResponse constructDtoToResponse (BizparDto dto) {
        return BizparResponse.builder()
                .id(dto.id())
                .key(dto.key())
                .value(dto.value())
                .type(dto.type())
                .description(dto.description())
                .createdAt(dto.createdAt())
                .createdBy(dto.createdBy())
                .updatedAt(dto.updatedAt())
                .updatedBy(dto.updatedBy())
                .build();
    }
}
