package swp.habitforge.habitforge.coach;

import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

public interface CoachRepository extends CrudRepository<Coach, Integer> {
    static Coach findByEmail(String email) {
        return null;
    }

    @Transactional
    void deleteByCoachId(Integer id);

    Coach findByCoachId(Integer id);
}
