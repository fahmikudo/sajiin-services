package id.sajiin.sajiinservices.configuration.service.impl;

import id.sajiin.sajiinservices.configuration.domain.Bizpar;
import id.sajiin.sajiinservices.configuration.model.dto.BizparDto;
import id.sajiin.sajiinservices.configuration.model.mapper.BizparMapper;
import id.sajiin.sajiinservices.configuration.model.request.CreateBizparRequestDto;
import id.sajiin.sajiinservices.configuration.model.response.CreateBizparResponseDto;
import id.sajiin.sajiinservices.configuration.repository.BizparRepository;
import id.sajiin.sajiinservices.configuration.service.CreateBizparService;
import id.sajiin.sajiinservices.shared.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateBizparServiceImpl implements CreateBizparService {

    private final BizparRepository bizparRepository;
    private final BizparMapper bizparMapper;

    @Transactional
    @Override
    public CreateBizparResponseDto execute(CreateBizparRequestDto request) throws GeneralException {
        validateRequest(request);
        Bizpar newBizpar = new Bizpar();
        newBizpar.setKey(request.getKey());
        newBizpar.setValue(request.getValue());
        newBizpar.setType(request.getType());
        newBizpar.setDescription(request.getDescription());
        newBizpar.setCreatedBy(String.valueOf(request.getUserId()));

        Bizpar savedBizpar = bizparRepository.saveAndReturn(newBizpar);
        BizparDto bizparDto = bizparMapper.toDto(savedBizpar);

        return new CreateBizparResponseDto(bizparDto);
    }

    private void validateRequest(CreateBizparRequestDto request) {
        if (request.getUserId() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have access to this service");
        }
        if (request.getKey() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Key can not be null");
        }
        if (request.getValue() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Value can not be null");
        }
    }
}
