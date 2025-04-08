package swp.habitforge.habitforge.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.habitforge.habitforge.coach.Coach;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> listAll(){


        return (List<User>) userRepository.findAll();
    }
    

}
