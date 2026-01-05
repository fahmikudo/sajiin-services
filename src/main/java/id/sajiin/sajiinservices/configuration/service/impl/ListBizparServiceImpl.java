package id.sajiin.sajiinservices.configuration.service.impl;

import id.sajiin.sajiinservices.configuration.domain.Bizpar;
import id.sajiin.sajiinservices.configuration.model.dto.BizparDto;
import id.sajiin.sajiinservices.configuration.model.request.ListBizparRequest;
import id.sajiin.sajiinservices.configuration.model.response.ListBizparResponse;
import id.sajiin.sajiinservices.configuration.repository.BizparRepository;
import id.sajiin.sajiinservices.configuration.repository.query.BizparEntityRequest;
import id.sajiin.sajiinservices.configuration.service.ListBizparService;
import id.sajiin.sajiinservices.shared.exception.GeneralException;
import id.sajiin.sajiinservices.shared.util.NumberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListBizparServiceImpl implements ListBizparService {

    private final BizparRepository bizparRepository;

    @Override
    public ListBizparResponse execute(ListBizparRequest request) throws GeneralException {
        validateRequest(request);
        ListBizparResponse response = null;
        if (request.getPaginated().equals(Boolean.TRUE)) {
            response = getListBizparWithPagination(request);
        }
        return response;
    }

    private ListBizparResponse getListBizparWithPagination(ListBizparRequest request) {
        BizparEntityRequest entityRequest = new BizparEntityRequest();
        entityRequest.setPageNumber(request.getPagination().getPage() - 1);
        entityRequest.setPageSize(request.getPagination().getSize());
        entityRequest.setValue(request.getSearch());

        Page<Bizpar> bizpars = bizparRepository.findWithPagination(entityRequest);

        ListBizparResponse listBizparResponse = new ListBizparResponse();

        listBizparResponse.setCurrentPage(bizpars.getNumber() + 1);
        listBizparResponse.setTotalRecord(bizpars.getTotalElements());
        listBizparResponse.setTotalPage(bizpars.getTotalPages());
        listBizparResponse.setPageSize(bizpars.getSize());
        listBizparResponse.setBizpars(constructToBizparDtoList(bizpars.getContent()));

        return listBizparResponse;
    }

    private List<BizparDto> constructToBizparDtoList(List<Bizpar> bizparList) {
        return bizparList.stream()
                .map(this::constructToBizparDto)
                .toList();
    }

    private BizparDto constructToBizparDto(Bizpar bizpar) {
        return BizparDto.builder()
                .id(bizpar.getId())
                .key(bizpar.getKey())
                .value(bizpar.getValue())
                .type(bizpar.getType())
                .description(bizpar.getDescription())
                .createdAt(bizpar.getCreatedAt())
                .createdBy(bizpar.getCreatedBy())
                .updatedAt(bizpar.getUpdatedAt())
                .updatedBy(bizpar.getUpdatedBy())
                .build();
    }

    private void validateRequest(ListBizparRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request list bizpar can not be null");
        }
        if (NumberUtil.isNullOrZero(request.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have access to this service");
        }
    }
}
