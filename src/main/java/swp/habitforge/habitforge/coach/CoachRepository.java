package swp.habitforge.habitforge.coach;

import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CoachRepository extends CrudRepository<Coach, Integer> {
    Optional<Coach> findByEmail(String email);

    @Transactional
    void deleteByCoachId(Integer id);

    Coach findByCoachId(Integer id);
}
