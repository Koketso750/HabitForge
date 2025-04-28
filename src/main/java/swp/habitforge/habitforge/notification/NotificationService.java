package swp.habitforge.habitforge.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.habitforge.habitforge.user.User;

import java.util.List;

@Service
public class NotificationService {

    @Autowired private NotificationRepository notificationRepository;

    public List<Notification> getUnreadNotificationsByUser(User user) {
        return notificationRepository.findByUserAndReadFalse(user);
    }
}
