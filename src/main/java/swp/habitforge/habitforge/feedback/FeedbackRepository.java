package swp.habitforge.habitforge.feedback;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import swp.habitforge.habitforge.coach.Coach;
import swp.habitforge.habitforge.user.User;
import swp.habitforge.habitforge.wellnesscontent.WellnessContent;

import java.util.List;

public interface FeedbackRepository extends CrudRepository<Feedback, Integer> {

    @Query("SELECT f FROM Feedback f JOIN FETCH f.user WHERE f.content.coach = :coach")
    List<Feedback> findByContentCoach(@Param("coach") Coach coach);

    @Transactional
    void deleteFeedbackByUser(User user);

}
