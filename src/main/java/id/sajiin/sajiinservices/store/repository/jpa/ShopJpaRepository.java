package id.sajiin.sajiinservices.store.repository.jpa;

import id.sajiin.sajiinservices.store.domain.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopJpaRepository extends JpaRepository<Shop, Long>, JpaSpecificationExecutor<Shop> {
}
