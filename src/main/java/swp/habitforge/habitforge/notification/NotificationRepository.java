package swp.habitforge.habitforge.notification;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import swp.habitforge.habitforge.task.Task;
import swp.habitforge.habitforge.user.User;

import java.util.List;

public interface NotificationRepository extends CrudRepository<Notification, Integer> {

    List<Notification> findByUserAndReadFalse(User user);

    @Transactional
    void deleteByTask(Task task);

    @Transactional
    void deleteByUser(User loggedInUser);

    @Modifying
    @Transactional
    @Query("DELETE FROM Notification n WHERE n.user = :user OR n.task.user = :user")
    void deleteByUserOrTaskUser(@Param("user") User user);
}
