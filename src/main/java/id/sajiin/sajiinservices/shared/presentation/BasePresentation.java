package id.sajiin.sajiinservices.shared.presentation;

import id.sajiin.sajiinservices.shared.core.BaseServiceResponse;
import id.sajiin.sajiinservices.shared.exception.GeneralException;

public interface BasePresentation <T, U extends BaseServiceResponse> {
    T present(U serviceResponse) throws GeneralException;
}
