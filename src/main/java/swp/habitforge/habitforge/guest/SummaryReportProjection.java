package swp.habitforge.habitforge.guest;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface SummaryReportProjection {
    // User details
    Integer getUser_id();
    String getUser_name();
    String getUser_email();
    String getUser_username();
    String getUser_profile_picture();
    String getUser_surname();
    LocalDateTime getUser_created_at();
    LocalDateTime getUser_updated_at();

    // Task details
    Integer getTask_id();
    String getTask_name();
    String getTask_description();
    LocalDate getDue_date();
    Boolean getTask_completed();
    String getHabit_type();
    String getHabit_frequency();
    LocalDateTime getTask_created_at();
    LocalDateTime getTask_updated_at();

    // Progress details
    Integer getProgress_id();
    Double getProgress_value();
    LocalDate getProgress_date();
    LocalDateTime getProgress_created_at();
    LocalDateTime getProgress_updated_at();

    // Notification details
    Integer getNotification_id();
    String getNotification_text();
    LocalDateTime getNotification_date();
    Boolean getNotification_read();
    LocalDateTime getNotification_created_at();

    // Wellness content and feedback details
    Integer getContent_id();
    String getContent_title();
    String getContent_description();
    String getContent_url();
    String getContent_type();
    LocalDateTime getContent_created_at();
    LocalDateTime getContent_updated_at();
    Integer getCoach_id();

    Integer getFeedback_id();
    String getFeedback_text();
    Integer getRating();
    LocalDateTime getFeedback_created_at();
    LocalDateTime getFeedback_updated_at();
}