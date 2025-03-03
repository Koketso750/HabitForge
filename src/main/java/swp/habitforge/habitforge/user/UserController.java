package swp.habitforge.habitforge.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    @Autowired private UserRepository userRepository;
    @Autowired private UserService userService;

    @PostMapping("/register-user")
    public String registerUser(@RequestParam String username,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam(required = false) String name,
                             @RequestParam(required = false) String surname,
                             @RequestParam(required = false) String profilePicture,
                             @RequestParam String role){

        User user = new User(username, email, password, name, surname, profilePicture, role);

        userRepository.save(user);

        return "results";

    }
}
