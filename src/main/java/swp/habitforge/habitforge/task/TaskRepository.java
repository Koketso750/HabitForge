package swp.habitforge.habitforge.task;

import org.springframework.data.repository.CrudRepository;
import swp.habitforge.habitforge.user.User;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Integer> {
    List<Task> findByUser(User user);
}
