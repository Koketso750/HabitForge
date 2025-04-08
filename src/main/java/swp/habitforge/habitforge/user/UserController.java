package swp.habitforge.habitforge.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import swp.habitforge.habitforge.firebase.FirebaseStorageService;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
public class UserController {
    @Autowired private UserRepository userRepository;
    @Autowired private UserService userService;
    @Autowired private FirebaseStorageService firebaseStorageService;

    @GetMapping("/admin_patience")
    public String ManageUsers (Model model ) {
        List<User>listUsers =userService.listAll();
        model.addAttribute("listUsers",listUsers);
        return "admin_patience";
    }

    @GetMapping("/user_form")
    public String showAddCoachForm ( Model model ){
 model.addAttribute("user", new User());
 return "user_form";
    }

    //add a user
    @GetMapping("/user/create")
    public String showUserCreatedForm ( Model model ){
        model.addAttribute("user", new User());
        return "/user/create";

    }

    //edit a user


    //delete a user




    @PostMapping("/user/create")
    public String createUser(
          //  @RequestParam("file") MultipartFile file,
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("name") String name,
            @RequestParam("surname") String surname,
            @RequestParam("url") String url,

             RedirectAttributes redirectAttributes)


            throws IOException {

            // Upload profile picture to Firebase and get the URL
          //  String profilePictureUrl = firebaseStorageService.uploadProfilePicture(file, username);

            // Create new User object
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setPassword(password);  // Make sure to hash this password before saving
            newUser.setName(name);
            newUser.setSurname(surname);
            newUser.setProfilePicture(url);  // Store the Firebase URL
            //newUser.setRole("user");  // Default role as 'user'
        newUser.setCreatedAt(new Date());
        newUser.setUpdatedAt(new Date());

            // Save new user to the database
            userRepository.save(newUser);
          if (newUser.getUserId()== null) {
            throw new IllegalStateException("User was not saved!");
          }

            // Redirect to the login or success page.
        // used to redirect to results
        // added
//
            redirectAttributes.addFlashAttribute("success", "User registration successful!.");
            return "redirect:/user/create";  // Or any page you want to redirect the user to
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


    /* @GetMapping("/admin_shauntel")
  public String ManageCoaches (Model model){
        List<Coach>listCoaches=coachService.listAll();
        model.addAttribute("listCoaches",listCoaches);
        return "admin_shauntel";
    }*/

}