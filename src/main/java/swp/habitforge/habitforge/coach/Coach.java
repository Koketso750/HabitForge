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

    private String name;
    private String surname;
    private String profilePicture;
    private String email;
    private String password;
    private String bio;
    private String expertise;
    private Date createdAt;
    private Date updatedAt;

    public Coach() {}

    public Coach(String name, String surname, String profilePicture, String email, String password, String bio, String expertise, Date createdAt, Date updatedAt) {
        this.name = name;
        this.surname = surname;
        this.profilePicture = profilePicture;
        this.email = email;
        this.password = password;
        this.bio = bio;
        this.expertise = expertise;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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
}
