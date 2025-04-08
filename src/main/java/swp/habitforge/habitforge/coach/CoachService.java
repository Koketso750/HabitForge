package swp.habitforge.habitforge.coach;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoachService {

    @Autowired private CoachRepository coachRepository;

    public boolean emailExists(String email) {
        return CoachRepository.findByEmail(email) != null;
    }
        public List<Coach> listAll(){
            return (List<Coach>) coachRepository.findAll();
  }
    }

