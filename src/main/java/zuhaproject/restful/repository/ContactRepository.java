package zuhaproject.restful.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zuhaproject.restful.entity.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {
}
