package swp.habitforge.habitforge.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.habitforge.habitforge.user.User;

import java.util.List;

@Service
public class TaskService {

    @Autowired private TaskRepository taskRepository;

    public List<Task> getTasksByUser(User user) {
        return taskRepository.findByUser(user);
    }
}
