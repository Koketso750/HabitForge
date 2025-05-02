package swp.habitforge.habitforge.guest;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface GuestRepository extends CrudRepository<Guest, Integer> {

    @Query(value = "SELECT u.user_id, u.name AS user_name, u.email AS user_email, " +
            "u.username AS user_username, u.profile_picture AS user_profile_picture, " +
            "u.surname AS user_surname, u.created_at AS user_created_at, " +
            "u.updated_at AS user_updated_at, t.task_id, t.task_name, " +
            "t.task_description, t.due_date, t.completed AS task_completed, " +
            "t.habit_type, t.habit_frequency, t.created_at AS task_created_at, " +
            "t.updated_at AS task_updated_at, p.progress_id, p.progress_value, " +
            "p.progress_date, p.created_at AS progress_created_at, " +
            "p.updated_at AS progress_updated_at, n.notification_id, " +
            "n.notification_text, n.notification_date, n.read AS notification_read, " +
            "n.created_at AS notification_created_at, wc.content_id, " +
            "wc.content_title, wc.content_description, wc.content_url, " +
            "wc.content_type, wc.created_at AS content_created_at, " +
            "wc.updated_at AS content_updated_at, wc.coach_id, f.feedback_id, " +
            "f.feedback_text, f.rating, f.created_at AS feedback_created_at, " +
            "f.updated_at AS feedback_updated_at " +
            "FROM user u LEFT JOIN task t ON u.user_id = t.user_id " +
            "LEFT JOIN progress p ON t.task_id = p.task_id AND p.user_id = u.user_id " +
            "LEFT JOIN notification n ON u.user_id = n.user_id AND n.task_id = t.task_id " +
            "LEFT JOIN wellness_content wc ON wc.coach_id = (SELECT coach_id FROM coach WHERE coach_id = wc.coach_id) " +
            "LEFT JOIN feedback f ON f.user_id = u.user_id AND f.content_id = wc.content_id " +
            "ORDER BY u.user_id, t.due_date, wc.content_title, f.created_at", nativeQuery = true)
    List<SummaryReportProjection> getSummaryReport();


    @Query(value = "SELECT wc.content_id, wc.content_title, wc.content_type, " +
            "wc.content_description, c.name AS coach_name, c.surname AS coach_surname, " +
            "f.feedback_id, f.feedback_text, f.rating, u.name AS user_name, " +
            "u.surname AS user_surname, f.created_at AS feedback_created_at " +
            "FROM wellness_content wc " +
            "LEFT JOIN coach c ON wc.coach_id = c.coach_id " +
            "LEFT JOIN feedback f ON wc.content_id = f.content_id " +
            "LEFT JOIN user u ON f.user_id = u.user_id " +
            "ORDER BY wc.content_id, f.created_at DESC", nativeQuery = true)
    List<WellnessFeedbackProjection> getWellnessFeedbackReport();
}