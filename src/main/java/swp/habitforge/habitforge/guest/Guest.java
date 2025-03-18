package swp.habitforge.habitforge.guest;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "guest")
public class Guest {
    @Id
    private Integer guestId;
    private String guestName;
    private String guestEmail;
    private Date createdAt;
    private Date updatedAt;

    public Guest() {}

    public Guest(Integer guestId, String guestName, String guestEmail, Date createdAt, Date updatedAt) {
        this.guestId = guestId;
        this.guestName = guestName;
        this.guestEmail = guestEmail;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getGuestId() {
        return guestId;
    }

    public void setGuestId(Integer guestId) {
        this.guestId = guestId;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestEmail() {
        return guestEmail;
    }

    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
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