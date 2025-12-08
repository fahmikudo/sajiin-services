package id.sajiin.sajiinservices.shared.core;

import id.sajiin.sajiinservices.shared.exception.GeneralException;

public interface BaseService <T extends BaseServiceRequest, U extends BaseServiceResponse> {
    U execute(T request) throws GeneralException;
}
