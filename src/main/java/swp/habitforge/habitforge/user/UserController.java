package swp.habitforge.habitforge.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import swp.habitforge.habitforge.firebase.FirebaseStorageService;

import java.io.IOException;
import java.util.List;

@Controller
public class UserController {
    @Autowired private UserRepository userRepository;
    @Autowired private UserService userService;
    @Autowired private FirebaseStorageService firebaseStorageService;

    @PostMapping("/user/create")
    public String createUser(
            @RequestParam("file") MultipartFile file,
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("name") String name,
            @RequestParam("surname") String surname) throws IOException {

            // Upload profile picture to Firebase and get the URL
            String profilePictureUrl = firebaseStorageService.uploadProfilePicture(file, username);

            // Create new User object
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setPassword(password);  // Make sure to hash this password before saving
            newUser.setName(name);
            newUser.setSurname(surname);
            newUser.setProfilePicture(profilePictureUrl);  // Store the Firebase URL
            newUser.setRole("user");  // Default role as 'user'

            // Save new user to the database
            userRepository.save(newUser);

            // Redirect to the login or success page
            return "redirect:/results";  // Or any page you want to redirect the user to
    }

    @GetMapping("/all/users")
    public String getAllUsers(Model model) {
        // Fetch all users from the database
        List<User> users = (List<User>) userRepository.findAll();

        // Add the list of users to the model
        model.addAttribute("users", users);

        // Return the Thymeleaf template name
        return "all-users";
    }
}