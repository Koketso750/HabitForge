package swp.habitforge.habitforge.wellnesscontent;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import swp.habitforge.habitforge.coach.Coach;

import java.util.List;

public interface WellnessContentRepository extends CrudRepository<WellnessContent, Integer> {
    List<WellnessContent> findByCoach(Coach coach);
    Long countByCoach(Coach coach);

    @Query("SELECT w FROM WellnessContent w WHERE w.contentId = :contentId")
    WellnessContent findWellnessContentById(@Param("contentId") Long contentId);

    @Transactional
    void deleteWellnessContentByCoach(Coach coach);

}
