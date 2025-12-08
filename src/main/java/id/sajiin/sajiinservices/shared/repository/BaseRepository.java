package id.sajiin.sajiinservices.shared.repository;

import id.sajiin.sajiinservices.shared.core.BaseEntityRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface BaseRepository <T, U extends BaseEntityRequest> {

    void save(T entity);

    T saveAndReturn(T entity);

    Optional<List<T>> find(U request);

    Page<T> findWithPagination(U request);

}
