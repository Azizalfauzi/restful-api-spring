package zuhaproject.restful.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zuhaproject.restful.entity.Contact;
import zuhaproject.restful.entity.User;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {
    Optional<Contact> findFirstByUserAndId(User user, String id);
}
