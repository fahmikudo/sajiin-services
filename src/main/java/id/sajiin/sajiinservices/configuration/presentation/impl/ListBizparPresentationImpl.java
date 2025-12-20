package id.sajiin.sajiinservices.configuration.presentation.impl;

import id.sajiin.sajiinservices.configuration.model.dto.BizparDto;
import id.sajiin.sajiinservices.configuration.model.response.ListBizparResponse;
import id.sajiin.sajiinservices.configuration.presentation.ListBizparPresentation;
import id.sajiin.sajiinservices.configuration.presentation.response.BizparResponse;
import id.sajiin.sajiinservices.configuration.presentation.response.GetListBizparResponse;
import id.sajiin.sajiinservices.shared.constant.MessageConstant;
import id.sajiin.sajiinservices.shared.exception.GeneralException;
import id.sajiin.sajiinservices.shared.presentation.PaginationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ListBizparPresentationImpl implements ListBizparPresentation {
    @Override
    public GetListBizparResponse present(ListBizparResponse serviceResponse) throws GeneralException {

        GetListBizparResponse response = new GetListBizparResponse();
        response.setSuccess(true);
        response.setMessage(MessageConstant.messageCode(HttpStatus.OK.value()));
        response.setPagination(new PaginationResponse(serviceResponse.getCurrentPage(), serviceResponse.getPageSize(), serviceResponse.getTotalRecord(), serviceResponse.getTotalPage()));
        response.setData(constructDtoToResponse(serviceResponse.getBizpars()));

        return response;
    }

    private List<BizparResponse> constructDtoToResponse (List<BizparDto> dtos) {
        List<BizparResponse> bizparResponses = new ArrayList<>();
        for (BizparDto dto : dtos) {
            BizparResponse response = BizparResponse.builder()
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
            bizparResponses.add(response);
        }
        return bizparResponses;
    }

}
