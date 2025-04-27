package swp.habitforge.habitforge.user;

import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    @Transactional
    void deleteByUserId(Integer id);

    Optional<User> findByUserId(Integer id);

    User findByEmail(String email);

    User findByUsername(String username);
}
