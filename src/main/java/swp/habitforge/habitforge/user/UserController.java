package swp.habitforge.habitforge.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import swp.habitforge.habitforge.firebase.FirebaseStorageService;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    // Display admin panel with list of all users
    @GetMapping("/admin_patience")
    public String manageUsers(Model model) {
        List<User> listUsers = userService.listAll();
        model.addAttribute("listUsers", listUsers);
        return "admin_patience";
    }

    // Show form for adding a new user
    @GetMapping("/user_form")
    public String showUserForm(Model model) {
        model.addAttribute("user", new User());
        return "user_form";
    }

    // Show form for creating a new user (alternative endpoint)
    @GetMapping("/user/create")
    public String showUserCreateForm(Model model) {
        model.addAttribute("user", new User());
        return "user/create";
    }

    // Process form submission for creating a new user
    @PostMapping("/user/create")
    public String createUser(
            @ModelAttribute("user") User user,
            RedirectAttributes redirectAttributes
    ) throws IOException {
        try {
            // Set timestamps
            user.setCreatedAt(new Date());
            user.setUpdatedAt(new Date());

            // Save the user
            userRepository.save(user);

            redirectAttributes.addFlashAttribute("success", "User created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating user: " + e.getMessage());
        }
        return "redirect:/admin_patience";
    }

    // Show form for editing an existing user
    @GetMapping("/user/update/{id}")
    public String editUser(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
            model.addAttribute("user", user);
            return "edit_user";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin_patience";
        }
    }

    // Process form submission for updating a user
    @PostMapping("/user/update/{id}")
    public String updateUser(
            @PathVariable("id") Integer id,
            @ModelAttribute("user") User user,
            RedirectAttributes redirectAttributes
    ) {
        try {
            // Get existing user data
            User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

            // Update fields
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setName(user.getName());
            existingUser.setSurname(user.getSurname());
            existingUser.setProfilePicture(user.getProfilePicture());

            // Only update password if a new one was provided
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(user.getPassword());
            }

            // Update timestamp
            existingUser.setUpdatedAt(new Date());

            // Save the updated user
            userRepository.save(existingUser);

            redirectAttributes.addFlashAttribute("success", "User updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating user: " + e.getMessage());
        }
        return "redirect:/admin_patience";
    }

    // Delete a user
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
            userRepository.delete(user);
            redirectAttributes.addFlashAttribute("success", "User deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting user: " + e.getMessage());
        }
        return "redirect:/admin_patience";
    }
}