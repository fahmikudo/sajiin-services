package id.sajiin.sajiinservices.configuration.service.impl;

import id.sajiin.sajiinservices.configuration.domain.Bizpar;
import id.sajiin.sajiinservices.configuration.model.dto.BizparDto;
import id.sajiin.sajiinservices.configuration.model.mapper.BizparMapper;
import id.sajiin.sajiinservices.configuration.model.request.GetBizparRequestDto;
import id.sajiin.sajiinservices.configuration.model.response.GetBizparResponseDto;
import id.sajiin.sajiinservices.configuration.repository.BizparRepository;
import id.sajiin.sajiinservices.configuration.repository.query.BizparEntityRequest;
import id.sajiin.sajiinservices.configuration.service.GetBizparService;
import id.sajiin.sajiinservices.shared.exception.GeneralException;
import id.sajiin.sajiinservices.shared.util.NumberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetBizparServiceImpl implements GetBizparService {

    private final BizparRepository bizparRepository;
    private final BizparMapper bizparMapper;

    @Override
    public GetBizparResponseDto execute(GetBizparRequestDto request) throws GeneralException {
        validateRequest(request);
        Bizpar bizparById = getBizparById(request);
        BizparDto bizparDto = bizparMapper.toDto(bizparById);
        return new GetBizparResponseDto(bizparDto);
    }

    private Bizpar getBizparById(GetBizparRequestDto request) {
        BizparEntityRequest bizparEntityRequest = new BizparEntityRequest();
        bizparEntityRequest.setId(request.getId());

        Optional<List<Bizpar>> bizpars = bizparRepository.find(bizparEntityRequest);
        if (bizpars.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bizpar not found with id: " + request.getId());
        }
        return bizpars.get().getFirst();
    }

    private void validateRequest(GetBizparRequestDto request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request can not be null");
        }
        if (NumberUtil.isNullOrZero(request.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id can not be null or zero");
        }
    }
}
