package swp.habitforge.habitforge.coach;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoachService {

    @Autowired private CoachRepository coachRepository;

        public List<Coach> listAll(){
            return (List<Coach>)
                    coachRepository.findAll();
        }

    public boolean emailExists(String email) {
        // Use the repository to check if a coach with the provided email exists
        return coachRepository.findByEmail(email).isPresent();
    }
}

