package id.sajiin.sajiinservices.configuration.service.impl;

import id.sajiin.sajiinservices.configuration.domain.Bizpar;
import id.sajiin.sajiinservices.configuration.model.dto.BizparDto;
import id.sajiin.sajiinservices.configuration.model.mapper.BizparMapper;
import id.sajiin.sajiinservices.configuration.model.request.UpdateBizparRequestDto;
import id.sajiin.sajiinservices.configuration.model.response.UpdateBizparResponseDto;
import id.sajiin.sajiinservices.configuration.repository.BizparRepository;
import id.sajiin.sajiinservices.configuration.repository.query.BizparEntityRequest;
import id.sajiin.sajiinservices.configuration.service.UpdateBizparService;
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
public class UpdateBizparServiceImpl implements UpdateBizparService {

    private final BizparRepository bizparRepository;
    private final BizparMapper bizparMapper;

    @Override
    public UpdateBizparResponseDto execute(UpdateBizparRequestDto request) throws GeneralException {
        validateRequest(request);
        BizparEntityRequest bizparEntityRequest = new BizparEntityRequest();
        bizparEntityRequest.setId(request.getId());

        Optional<List<Bizpar>> bizpars = bizparRepository.find(bizparEntityRequest);
        if (bizpars.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bizpar not found");
        }
        Bizpar bizpar = bizpars.get().getFirst();
        bizpar.setKey(request.getKey());
        bizpar.setValue(request.getValue());
        bizpar.setType(request.getType());
        bizpar.setDescription(request.getDescription());
        bizpar.setUpdatedBy(String.valueOf(request.getUserId()));

        Bizpar updatedBizpar = bizparRepository.saveAndReturn(bizpar);
        BizparDto bizparDto = bizparMapper.toDto(updatedBizpar);

        return new UpdateBizparResponseDto(bizparDto);
    }

    private void validateRequest(UpdateBizparRequestDto request) {
        if (NumberUtil.isNullOrZero(request.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have access to this service");
        }
        if (NumberUtil.isNullOrZero(request.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id can not be null");
        }
    }
}
