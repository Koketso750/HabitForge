package swp.habitforge.habitforge.coach;

import org.springframework.data.repository.CrudRepository;

public interface CoachRepository extends CrudRepository<Coach, Integer> {
    static Coach findByEmail(String email) {
        return null;
    }
}
