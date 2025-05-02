package swp.habitforge.habitforge.progress;

import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import swp.habitforge.habitforge.task.Task;
import swp.habitforge.habitforge.user.User;

import java.util.Optional;

public interface ProgressRepository extends CrudRepository<Progress, Integer> {
    @Transactional
    void deleteByTask(Task task);

    Optional<Progress> findByTask(Task task);

    @Transactional
    void deleteByUser(User loggedInUser);
}
