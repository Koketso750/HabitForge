package swp.habitforge.habitforge.coach;

import jakarta.persistence.*;
import swp.habitforge.habitforge.user.User;

import java.util.Date;

@Entity
@Table(name = "coach")
public class Coach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer coachId;
    private String email;
    private String password;
    private String bio;
    private String expertise;
    private String profilePicture;
    private Date createdAt;
    private Date updatedAt;

    @OneToOne
    @MapsId
    @JoinColumn(name = "coach_id")
    private User user;

    public Coach() {}

    public Coach(Integer coachId, String email, String password, String bio, String expertise, String profilePicture, Date createdAt, Date updatedAt, User user) {
        this.coachId = coachId;
        this.email = email;
        this.password = password;
        this.bio = bio;
        this.expertise = expertise;
        this.profilePicture = profilePicture;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.user = user;
    }

    public Integer getCoachId() {
        return coachId;
    }

    public void setCoachId(Integer coachId) {
        this.coachId = coachId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
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
}
