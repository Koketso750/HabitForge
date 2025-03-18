package swp.habitforge.habitforge.progress;

import jakarta.persistence.*;
import swp.habitforge.habitforge.task.Task;
import swp.habitforge.habitforge.user.User;

import java.util.Date;

@Entity
@Table(name = "progress")
public class Progress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer progressId;
    private java.sql.Date progressDate;
    private Integer progressValue;
    private Date createdAt;
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    public Progress() {}

    public Progress(Integer progressId, java.sql.Date progressDate, Integer progressValue, Date createdAt, Date updatedAt, User user, Task task) {
        this.progressId = progressId;
        this.progressDate = progressDate;
        this.progressValue = progressValue;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.user = user;
        this.task = task;
    }

    public Integer getProgressId() {
        return progressId;
    }

    public void setProgressId(Integer progressId) {
        this.progressId = progressId;
    }

    public java.sql.Date getProgressDate() {
        return progressDate;
    }

    public void setProgressDate(java.sql.Date progressDate) {
        this.progressDate = progressDate;
    }

    public Integer getProgressValue() {
        return progressValue;
    }

    public void setProgressValue(Integer progressValue) {
        this.progressValue = progressValue;
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