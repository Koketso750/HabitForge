package swp.habitforge.habitforge.notification;

import jakarta.persistence.*;
import swp.habitforge.habitforge.task.Task;
import swp.habitforge.habitforge.user.User;

import java.util.Date;

@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notificationId;
    private String notificationText;
    private java.sql.Date notificationDate;
    private Date createdAt;
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    public Notification() {}

    public Notification(Integer notificationId, String notificationText, java.sql.Date notificationDate, Date createdAt, Date updatedAt, User user, Task task) {
        this.notificationId = notificationId;
        this.notificationText = notificationText;
        this.notificationDate = notificationDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.user = user;
        this.task = task;
    }

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public java.sql.Date getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(java.sql.Date notificationDate) {
        this.notificationDate = notificationDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}