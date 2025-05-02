package swp.habitforge.habitforge.notification;

import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import swp.habitforge.habitforge.task.Task;
import swp.habitforge.habitforge.user.User;

import java.util.List;

public interface NotificationRepository extends CrudRepository<Notification, Integer> {

    List<Notification> findByUserAndReadFalse(User user);

    @Transactional
    void deleteByTask(Task task);

    @Transactional
    void deleteByUser(User loggedInUser);
}
