package zuhaproject.restful.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zuhaproject.restful.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
