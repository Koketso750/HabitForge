package swp.habitforge.habitforge.wellnesscontent;

import jakarta.persistence.*;
import swp.habitforge.habitforge.coach.Coach;
import swp.habitforge.habitforge.feedback.Feedback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "wellness_content")
public class WellnessContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer contentId;
    private String contentType;
    private String contentTitle;

    @Column(length = 5000)
    private String contentDescription;

    private String contentUrl;
    private String contentFileType;
    private Date createdAt;
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "coach_id")
    private Coach coach;

    public List<Feedback> getFeedback() {
        return feedback;
    }

    public WellnessContent(String contentType, String contentTitle, String contentDescription, String contentUrl, String contentFileType, Date createdAt, Date updatedAt, Coach coach, List<Feedback> feedback) {
        this.contentType = contentType;
        this.contentTitle = contentTitle;
        this.contentDescription = contentDescription;
        this.contentUrl = contentUrl;
        this.contentFileType = contentFileType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.coach = coach;
        this.feedback = feedback;
    }

    public void setFeedback(List<Feedback> feedback) {
        this.feedback = feedback;
    }

    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feedback> feedback = new ArrayList<>();

    public WellnessContent() {}

    public WellnessContent(Integer contentId, String contentType, String contentTitle, String contentDescription, String contentUrl, String contentFileType, Date createdAt, Date updatedAt, Coach coach) {
        this.contentId = contentId;
        this.contentType = contentType;
        this.contentTitle = contentTitle;
        this.contentDescription = contentDescription;
        this.contentUrl = contentUrl;
        this.contentFileType = contentFileType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.coach = coach;
    }

    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getContentDescription() {
        return contentDescription;
    }

    public void setContentDescription(String contentDescription) {
        this.contentDescription = contentDescription;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getContentFileType() {
        return contentFileType;
    }

    public void setContentFileType(String contentFileType) {
        this.contentFileType = contentFileType;
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

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }
}