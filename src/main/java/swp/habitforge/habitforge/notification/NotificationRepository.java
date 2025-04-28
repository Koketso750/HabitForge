package swp.habitforge.habitforge.notification;

import org.springframework.data.repository.CrudRepository;
import swp.habitforge.habitforge.user.User;

import java.util.List;

public interface NotificationRepository extends CrudRepository<Notification, Integer> {

    List<Notification> findByUserAndReadFalse(User user);
}
