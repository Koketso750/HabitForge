package swp.habitforge.habitforge.wellnesscontent;

import org.springframework.data.repository.CrudRepository;
import swp.habitforge.habitforge.coach.Coach;

import java.util.List;

public interface WellnessContentRepository extends CrudRepository<WellnessContent, Integer> {
    List<WellnessContent> findByCoach(Coach coach);
    Long countByCoach(Coach coach);
}
