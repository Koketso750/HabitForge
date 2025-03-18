package swp.habitforge.habitforge.coach;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import swp.habitforge.habitforge.user.UserRepository;

@Controller
public class CoachController {

    @Autowired private CoachRepository coachRepository;
    @Autowired private CoachService coachService;

}
