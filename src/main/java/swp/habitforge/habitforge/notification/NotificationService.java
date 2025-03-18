package swp.habitforge.habitforge.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired private NotificationRepository notificationRepository;
}
