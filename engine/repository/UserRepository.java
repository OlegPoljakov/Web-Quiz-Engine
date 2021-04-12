package engine.repository;

import engine.model.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.beans.Transient;

@Repository
public interface UserRepository extends JpaRepository<UserInformation, Long> {

    @Transient
    UserInformation getUserByEmail(String username);

    boolean existsByEmail(String email);
}
