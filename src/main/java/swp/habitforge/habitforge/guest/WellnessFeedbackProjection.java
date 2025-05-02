package swp.habitforge.habitforge.guest;

import java.time.LocalDateTime;

public interface WellnessFeedbackProjection {
    Integer getContent_id();
    String getContent_title();
    String getContent_type();
    String getContent_description();

    String getCoach_name();
    String getCoach_surname();

    Integer getFeedback_id();
    String getFeedback_text();
    Integer getRating();

    String getUser_name();
    String getUser_surname();

    LocalDateTime getFeedback_created_at();
}