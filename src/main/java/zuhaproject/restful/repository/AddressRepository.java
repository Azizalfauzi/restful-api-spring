package zuhaproject.restful.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zuhaproject.restful.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
}
