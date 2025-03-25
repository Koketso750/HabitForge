package swp.habitforge.habitforge.coach;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoachService {

    @Autowired private CoachRepository coachRepository;

    public boolean emailExists(String email) {
        return CoachRepository.findByEmail(email) != null;
    }
}
