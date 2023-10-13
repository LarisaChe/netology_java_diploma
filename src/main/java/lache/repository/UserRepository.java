package lache.repository;

import lache.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query(value = "SELECT u.login, u.password, u.salt, u.role  FROM cloud.users u WHERE UPPER(u.login) = UPPER(:login) ", nativeQuery = true)
    Optional<User> findByLogin(@Param("login") String login);
}
