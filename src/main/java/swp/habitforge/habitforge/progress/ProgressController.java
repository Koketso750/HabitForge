package swp.habitforge.habitforge.progress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ProgressController {

    @Autowired private ProgressService progressService;
    @Autowired private ProgressRepository progressRepository;
}
