package swp.habitforge.habitforge.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class TaskController {

    @Autowired private TaskRepository taskRepository;
    @Autowired private TaskService taskService;
}
